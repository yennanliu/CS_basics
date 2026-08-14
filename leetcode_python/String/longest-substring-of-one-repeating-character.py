"""

2213. Longest Substring of One Repeating Character
Hard

You are given a 0-indexed string s. You are also given a 0-indexed string queryCharacters of length k and a 0-indexed array of integer indices queryIndices of length k, both of which are used to describe k queries.

The ith query updates the character in s at index queryIndices[i] to the character queryCharacters[i].

Return an array lengths of length k where lengths[i] is the length of the longest substring of s consisting of only one repeating character after the ith query is performed.


Example 1:

Input: s = "babacc", queryCharacters = "bcb", queryIndices = [1,3,3]
Output: [3,3,4]
Explanation:
- 1st query updates s = "bbbacc". The longest substring consisting of one repeating character is "bbb" with length 3.
- 2nd query updates s = "bbbccc".
  The longest substring consisting of one repeating character can be "bbb" or "ccc" with length 3.
- 3rd query updates s = "bbbbcc".
  The longest substring consisting of one repeating character is "bbbb" with length 4.
Thus, we return [3,3,4].

Example 2:

Input: s = "abyzz", queryCharacters = "aa", queryIndices = [2,1]
Output: [2,3]
Explanation:
- 1st query updates s = "abazz". The longest substring consisting of one repeating character is "zz" with length 2.
- 2nd query updates s = "aaazz". The longest substring consisting of one repeating character is "aaa" with length 3.
Thus, we return [2,3].


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.
k == queryCharacters.length == queryIndices.length
1 <= k <= 10^5
queryCharacters consists of lowercase English letters.
0 <= queryIndices[i] < s.length

"""

# V0
# IDEA : SEGMENT TREE WHERE EACH NODE KEEPS PREFIX / SUFFIX / BEST RUNS
#
#   point updates with a global "longest run" question is the textbook case
#   for a segment tree storing, per segment :
#       pref  = length of the run starting at the segment's left edge
#       suf   = length of the run ending at its right edge
#       best  = longest run fully inside
#       size, lc, rc  (the two boundary characters)
#
#   MERGE : the children join only when left.rc == right.lc. then
#       best = max(l.best, r.best, l.suf + r.pref)
#   and pref/suf extend across the boundary when the corresponding child is
#   entirely one character.
#
#   each query rewrites one leaf and recomputes the O(log n) nodes above it;
#   the answer is the root's `best`.
#
# time = O((n + k) log n), space = O(n)
class Solution(object):
    def longestRepeating(self, s, queryCharacters, queryIndices):
        n = len(s)
        arr = list(s)
        tree = [None] * (4 * n)     # (pref, suf, best, size, lc, rc)

        def merge(left, right):
            lp, ls, lb, lsz, llc, lrc = left
            rp, rs, rb, rsz, rlc, rrc = right
            joins = (lrc == rlc)
            pref = lp + (rp if joins and lp == lsz else 0)
            suf = rs + (ls if joins and rs == rsz else 0)
            best = max(lb, rb, (ls + rp) if joins else 0)
            return (pref, suf, best, lsz + rsz, llc, rrc)

        def build(node, lo, hi):
            if lo == hi:
                tree[node] = (1, 1, 1, 1, arr[lo], arr[lo])
                return
            mid = (lo + hi) // 2
            build(2 * node + 1, lo, mid)
            build(2 * node + 2, mid + 1, hi)
            tree[node] = merge(tree[2 * node + 1], tree[2 * node + 2])

        def update(node, lo, hi, idx):
            if lo == hi:
                tree[node] = (1, 1, 1, 1, arr[lo], arr[lo])
                return
            mid = (lo + hi) // 2
            if idx <= mid:
                update(2 * node + 1, lo, mid, idx)
            else:
                update(2 * node + 2, mid + 1, hi, idx)
            tree[node] = merge(tree[2 * node + 1], tree[2 * node + 2])

        build(0, 0, n - 1)

        res = []
        for c, i in zip(queryCharacters, queryIndices):
            arr[i] = c
            update(0, 0, n - 1, i)
            res.append(tree[0][2])
        return res
