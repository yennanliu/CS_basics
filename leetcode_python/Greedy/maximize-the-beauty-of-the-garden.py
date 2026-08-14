"""

1788. Maximize the Beauty of the Garden
Hard

There is a garden of n flowers, and each flower has an integer beauty value. The flowers are arranged in a line. You are given an integer array flowers of size n and each flowers[i] represents the beauty of the i^th flower.

A garden is valid if it meets these conditions:

The garden has at least two flowers.

The first and the last flower of the garden have the same beauty value.

As the appointed gardener, you have the ability to remove any (possibly none) flowers from the garden. You want to remove flowers in a way that makes the remaining garden valid. The beauty of the garden is the sum of the beauty of all the remaining flowers.

Return the maximum possible beauty of some valid garden after you have removed any (possibly none) flowers.

Example 1:

Input: flowers = [1,2,3,1,2]

Output: 8

Explanation: You can produce the valid garden [2,3,1,2] to have a total beauty of 2 + 3 + 1 + 2 = 8.

Example 2:

Input: flowers = [100,1,1,-3,1]

Output: 3

Explanation: You can produce the valid garden [1,1,1] to have a total beauty of 1 + 1 + 1 = 3.

Example 3:

Input: flowers = [-1,-2,0,-1]

Output: -2

Explanation: You can produce the valid garden [-1,-1] to have a total beauty of -1 + -1 = -2.

Constraints:

2 <= flowers.length <= 10^5

-10^4 <= flowers[i] <= 10^4

It is possible to create a valid garden by removing some (possibly none) flowers.

"""

# V0
# IDEA : FIRST OCCURRENCE + PREFIX SUM OF POSITIVE VALUES
#
#   the kept garden is decided by its two endpoints, which must hold the same
#   beauty value. between them we may drop anything, so we keep exactly the
#   POSITIVE flowers -> prefix sums over max(v, 0).
#   for a value v seen first at index p and now at index i:
#       beauty = 2 * v + (sum of positive flowers strictly between p and i)
#   NOTE : only the FIRST occurrence needs to be remembered as the left end -
#          a later left end would just drop a non-negative middle chunk, and
#          both endpoints contribute the same v either way.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumBeauty(self, flowers):
        n = len(flowers)
        pre = [0] * (n + 1)          # pre[i] = sum of max(flowers[t], 0), t < i
        first = {}
        res = float("-inf")
        for i in range(n):
            v = flowers[i]
            if v in first:
                res = max(res, pre[i] - pre[first[v] + 1] + 2 * v)
            else:
                first[v] = i
            pre[i + 1] = pre[i] + max(v, 0)
        return res
