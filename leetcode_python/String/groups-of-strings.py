"""

2157. Groups of Strings
Hard

You are given a 0-indexed array of strings words. Each string consists of lowercase English letters only. No letter occurs more than once in any string of words.

Two strings s1 and s2 are said to be connected if the set of letters of s2 can be obtained from the set of letters of s1 by any one of the following operations:

Adding exactly one letter to the set of the letters of s1.
Deleting exactly one letter from the set of the letters of s1.
Replacing exactly one letter from the set of the letters of s1 with any letter, including itself.

The array words can be divided into one or more non-intersecting groups. A string belongs to a group if any one of the following is true:

It is connected to at least one other string of the group.
It is the only string present in the group.

Note that the strings in words should be grouped in such a manner that a string belonging to a group cannot be connected to a string present in any other group. It can be proved that such an arrangement is always unique.

Return an array ans of size 2 where:

ans[0] is the maximum number of groups words can be divided into, and
ans[1] is the size of the largest group.


Example 1:

Input: words = ["a","b","ab","cde"]
Output: [2,3]
Explanation:
- words[0] can be used to obtain words[1] (by replacing 'a' with 'b'), and words[2] (by adding 'b'). So words[0] is connected to words[1] and words[2].
- words[1] can be used to obtain words[0] (by replacing 'b' with 'a'), and words[2] (by adding 'a'). So words[1] is connected to words[0] and words[2].
- words[2] can be used to obtain words[0] (by deleting 'b'), and words[1] (by deleting 'a'). So words[2] is connected to words[0] and words[1].
- words[3] is not connected to any string in words.
Thus, words can be divided into 2 groups ["a","b","ab"] and ["cde"]. The size of the largest group is 3.

Example 2:

Input: words = ["a","ab","abc"]
Output: [1,3]
Explanation:
- words[0] is connected to words[1].
- words[1] is connected to words[0] and words[2].
- words[2] is connected to words[1].
Since all strings are connected to each other, they should be grouped together.
Thus, the size of the largest group is 3.


Constraints:

1 <= words.length <= 2 * 10^4
1 <= words[i].length <= 26
words[i] consists of lowercase English letters only.
No letter occurs more than once in any string of words.

"""

# V0
# IDEA : EACH WORD IS A 26-BIT SET -> UNION-FIND OVER THE MASKS
#
#   letters are distinct within a word and order is irrelevant, so a word is
#   exactly a bitmask. the three operations become :
#       delete  : mask -> mask without one set bit
#       add     : mask -> mask with one extra bit  (the inverse of delete)
#       replace : mask -> drop bit i, set bit j
#
#   so for every mask, for every set bit i :
#       - union with  sub = mask ^ (1 << i)          if that mask exists
#         (this single direction covers ADD as well, since add is delete
#          seen from the other side)
#       - union with  sub | (1 << j)  for each unset j  (the replacements)
#
#   duplicate words collapse to one node whose weight is its multiplicity, so
#   group sizes are summed over counts, not over distinct masks.
#
# time = O(n * 26^2 * alpha), space = O(n)
from collections import Counter


class Solution(object):
    def groupStrings(self, words):
        def mask_of(w):
            m = 0
            for c in w:
                m |= 1 << (ord(c) - ord('a'))
            return m

        cnt = Counter(mask_of(w) for w in words)
        parent = {m: m for m in cnt}
        size = dict(cnt)                 # component size, counting duplicates

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                size[rb] += size[ra]

        for m in cnt:
            for i in range(26):
                if not (m >> i & 1):
                    continue
                sub = m ^ (1 << i)
                if sub in parent:
                    union(m, sub)
                for j in range(26):
                    if m >> j & 1:
                        continue
                    rep = sub | (1 << j)
                    if rep in parent:
                        union(m, rep)

        roots = set(find(m) for m in cnt)
        return [len(roots), max(size[r] for r in roots)]
