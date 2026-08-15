"""

2791. Count Paths That Can Form a Palindrome in a Tree
Hard

You are given a tree (i.e. a connected, undirected graph that has no cycles) rooted at node 0 consisting of n nodes numbered from 0 to n - 1. The tree is represented by a 0-indexed array parent of size n, where parent[i] is the parent of node i. Since node 0 is the root, parent[0] == -1.

You are also given a string s of length n, where s[i] is the character assigned to the edge between i and parent[i]. s[0] can be ignored.

Return the number of pairs of nodes (u, v) such that u < v and the characters assigned to edges on the path from u to v can be rearranged to form a palindrome.

A string is a palindrome when it reads the same backwards as forwards.


Example 1:

Input: parent = [-1,0,0,1,1,2], s = "acaabc"
Output: 8
Explanation: The valid pairs are:
- All the pairs (0,1), (0,2), (1,3), (1,4) and (2,5) result in one character which is always a palindrome.
- The pair (2,3) result in the string "aca" which is a palindrome.
- The pair (1,5) result in the string "cac" which is a palindrome.
- The pair (3,5) result in the string "acac" which can be rearranged into the palindrome "acca".

Example 2:

Input: parent = [-1,0,0,0,0], s = "aaaaa"
Output: 10
Explanation: Any pair of nodes (u,v) where u < v is valid.


Constraints:

n == parent.length == s.length
1 <= n <= 10^5
0 <= parent[i] <= n - 1 for all i >= 1
parent[0] == -1
parent represents a valid tree.
s consists of only lowercase English letters.

"""

# V0
# IDEA : ROOT-TO-NODE XOR BITMASK + PAIR COUNTING
#
#   a multiset of letters can be rearranged into a palindrome iff AT MOST ONE
#   letter has an odd count. Encode "parity of each of the 26 letters" as a
#   26-bit mask -> the condition becomes popcount(mask) <= 1.
#
#   let mask[v] = xor of the edge letters on the path root -> v. For any pair
#   (u, v) the shared prefix down to their LCA cancels out, so
#       mask(path u..v) = mask[u] ^ mask[v]
#   and we never have to find the LCA at all.
#
#   so: count pairs (u, v) with popcount(mask[u] ^ mask[v]) <= 1, i.e.
#   mask[v] equals mask[u] or mask[u] ^ (1 << k) for one of the 26 letters.
#
#   NOTE : one pass with a running counter avoids double counting - for each
#          node we look up the 27 candidate masks among the nodes ALREADY
#          inserted, then insert ourselves. Order of the pass is irrelevant
#          because we are just counting unordered pairs over the mask list.
#
#   NOTE : the root itself must take part (mask 0) - pairs (0, v) are real
#          pairs, as example 1 shows.
#
#   NOTE : parent[i] is NOT guaranteed to be smaller than i, so masks are
#          computed by an explicit top-down traversal, done ITERATIVELY:
#          n reaches 1e5 and the tree can be a single chain, which would blow
#          Python's recursion limit.
#
# time = O(26 * n), space = O(n)
class Solution(object):
    def countPalindromePaths(self, parent, s):
        n = len(parent)
        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[parent[i]].append(i)

        # iterative top-down walk: mask[child] = mask[node] ^ bit(s[child])
        mask = [0] * n
        stack = [0]
        while stack:
            node = stack.pop()
            base = mask[node]
            for child in children[node]:
                mask[child] = base ^ (1 << (ord(s[child]) - ord('a')))
                stack.append(child)

        res = 0
        cnt = {}
        for m in mask:
            res += cnt.get(m, 0)
            for k in range(26):
                res += cnt.get(m ^ (1 << k), 0)
            cnt[m] = cnt.get(m, 0) + 1
        return res
