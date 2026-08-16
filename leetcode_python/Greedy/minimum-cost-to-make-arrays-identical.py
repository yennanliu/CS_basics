"""

3424. Minimum Cost to Make Arrays Identical
Medium

You are given two integer arrays arr and brr of length n, and an integer k. You
can perform the following operations on arr any number of times:

Split arr into any number of contiguous subarrays and rearrange these subarrays
in any order. This operation has a fixed cost of k.

Choose any element in arr and add or subtract a positive integer x to it. The
cost of this operation is x.

Return the minimum total cost to make arr equal to brr.

Example 1:

Input: arr = [-7,9,5], brr = [7,-2,-5], k = 2

Output: 13

Explanation:

Split arr into two contiguous subarrays: [-7] and [9, 5] and rearrange them as
[9, 5, -7], with a cost of 2.
Subtract 2 from element arr[0]. The array becomes [7, 5, -7]. The cost of this
operation is 2.
Subtract 7 from element arr[1]. The array becomes [7, -2, -7]. The cost of this
operation is 7.
Add 2 to element arr[2]. The array becomes [7, -2, -5]. The cost of this
operation is 2.

The total cost to make the arrays equal is 2 + 2 + 7 + 2 = 13.

Example 2:

Input: arr = [2,1], brr = [2,1], k = 0

Output: 0

Explanation:

Since the arrays are already equal, no operations are needed, and the total cost
is 0.

Constraints:

1 <= arr.length == brr.length <= 10^5
0 <= k <= 2 * 10^10
-10^5 <= arr[i] <= 10^5
-10^5 <= brr[i] <= 10^5

"""

# V0
# IDEA : ONLY TWO SCENARIOS EXIST — NEVER REARRANGE, OR REARRANGE ONCE
#
#   the split-and-reorder move costs a flat k no matter how finely we cut, and
#   cutting into single elements makes *every* permutation reachable.  so paying
#   for it once buys us complete freedom over the order, and paying for it twice
#   buys nothing extra.  that leaves exactly two candidate plans.
#
#   without the move, position i is stuck facing brr[i] and the bill is
#   sum |arr[i] - brr[i]|.
#
#   with the move, we get to choose the matching, and the cheapest matching for
#   a sum of absolute differences is the sorted one: if two pairs cross, i.e.
#   a1 < a2 are matched to b1 > b2, uncrossing them never increases the total
#   (a classic exchange argument on the four possible orderings).
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minCost(self, arr, brr, k):
        direct = sum(abs(a - b) for a, b in zip(arr, brr))
        a = sorted(arr)
        b = sorted(brr)
        shuffled = k + sum(abs(x - y) for x, y in zip(a, b))
        return min(direct, shuffled)
