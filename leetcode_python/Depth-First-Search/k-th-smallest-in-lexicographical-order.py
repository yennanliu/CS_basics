"""

440. K-th Smallest in Lexicographical Order
Hard

Given two integers n and k, return the kth lexicographically smallest integer in
the range [1, n].

Example 1:

Input: n = 13, k = 2
Output: 10
Explanation: The lexicographical order is [1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7,
8, 9], so the second smallest number is 10.

Example 2:

Input: n = 1, k = 1
Output: 1

Constraints:

1 <= k <= n <= 10^9

"""

# V0
# IDEA : 10-ARY TRIE (PREFIX TREE) WALK + SUBTREE COUNTING
#
#  Lexicographical order == pre-order DFS of a 10-ary trie:
#
#     root
#      |- 1
#      |   |- 10, 11, ... 19
#      |        |- 100 ...
#      |- 2
#      ...
#
#  n can be 10^9 so we cannot walk node by node. Instead, at prefix `cur`
#  we count how many numbers <= n live in that subtree:
#
#     count(cur) = sum over levels of  min(n + 1, next) - cur
#                  where the level ranges are [cur, next), [cur*10, next*10) ...
#
#  Then:
#    - k >= count(cur)  -> the answer is NOT in this subtree
#                          skip it: k -= count, cur += 1   (next sibling)
#    - k <  count(cur)  -> the answer IS inside
#                          descend: k -= 1, cur *= 10      (first child)
#
# time = O(log(n)^2)
# space = O(1)
class Solution(object):
    def findKthNumber(self, n, k):

        def count(cur):
            # how many integers in [1, n] have `cur` as a prefix
            total = 0
            nxt = cur + 1
            while cur <= n:
                # this trie level covers [cur, nxt), clipped by n
                total += min(n + 1, nxt) - cur
                cur *= 10
                nxt *= 10
            return total

        cur = 1
        k -= 1          # the prefix "1" itself is the 1st number
        while k > 0:
            c = count(cur)
            if k >= c:
                # skip the whole subtree, move to the next sibling
                k -= c
                cur += 1
            else:
                # go one level deeper (first child)
                k -= 1
                cur *= 10
        return cur
