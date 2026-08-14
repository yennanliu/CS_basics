"""

2234. Maximum Total Beauty of the Gardens
Hard

Alice is a caretaker of n gardens and she wants to plant flowers to maximize the total beauty of all her gardens.

You are given a 0-indexed integer array flowers of size n, where flowers[i] is the number of flowers already planted in the ith garden. Flowers that are already planted cannot be removed. You are then given another integer newFlowers, which is the maximum number of flowers that Alice can additionally plant. You are also given the integers target, full, and partial.

A garden is considered complete if it has at least target flowers. The total beauty of the gardens is then determined as the sum of the following:

The number of complete gardens multiplied by full.
The minimum number of flowers in any of the incomplete gardens multiplied by partial. If there are no incomplete gardens, then this value will be 0.

Return the maximum total beauty that Alice can obtain after planting at most newFlowers flowers.


Example 1:

Input: flowers = [1,3,1,1], newFlowers = 7, target = 6, full = 12, partial = 1
Output: 14
Explanation: Alice can plant
- 2 flowers in the 0th garden
- 3 flowers in the 1st garden
- 1 flower in the 2nd garden
- 1 flower in the 3rd garden
The gardens will then be [3,6,2,2]. She planted a total of 2 + 3 + 1 + 1 = 7 flowers.
There is 1 garden that is complete.
The minimum number of flowers in the incomplete gardens is 2.
Thus, the total beauty is 1 * 12 + 2 * 1 = 12 + 2 = 14.
No other way of planting flowers can obtain a total beauty higher than 14.

Example 2:

Input: flowers = [2,4,5,3], newFlowers = 10, target = 5, full = 2, partial = 6
Output: 30
Explanation: Alice can plant
- 3 flowers in the 0th garden
- 0 flowers in the 1st garden
- 0 flowers in the 2nd garden
- 2 flowers in the 3rd garden
The gardens will then be [5,4,5,5]. She planted a total of 3 + 0 + 0 + 2 = 5 flowers.
There are 3 gardens that are complete.
The minimum number of flowers in the incomplete gardens is 4.
Thus, the total beauty is 3 * 2 + 4 * 6 = 6 + 24 = 30.
No other way of planting flowers can obtain a total beauty higher than 30.
Note that Alice could make all the gardens complete but in this case, she would obtain a lower total beauty.


Constraints:

1 <= flowers.length <= 10^5
1 <= flowers[i], target <= 10^5
1 <= newFlowers <= 10^10
1 <= full, partial <= 10^5

"""

# V0
# IDEA : SORT + ENUMERATE HOW MANY GARDENS END UP COMPLETE + BINARY SEARCH
#
#   sort flowers ascending.  if x gardens are complete in the end, the cheapest
#   choice is to complete the x LARGEST ones -> suffix of the sorted array.
#   the remaining prefix flowers[0..n-x-1] stays incomplete; with the leftover
#   budget we raise its MINIMUM as high as possible (capped at target - 1).
#
#   raising the minimum = "water filling" : level up the prefix [0..l] to a
#   common height.  cost(l, h) = h * (l + 1) - prefixSum[l + 1], increasing in
#   l, so binary search the largest l affordable at height flowers[l], then
#   spread whatever is left evenly over those l + 1 gardens.
#
#   NOTE : x runs from (gardens already >= target) up to n; the completion cost
#          is accumulated incrementally, stop as soon as the budget goes < 0.
#   NOTE : the resulting min must be capped at target - 1, otherwise the garden
#          would have become complete and x would be wrong.
#
# time = O(n log n), space = O(n)
from bisect import bisect_left
class Solution(object):
    def maximumBeauty(self, flowers, newFlowers, target, full, partial):
        flowers = sorted(flowers)
        n = len(flowers)

        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + flowers[i]

        already = n - bisect_left(flowers, target)

        res = 0
        budget = newFlowers
        for x in range(already, n + 1):
            if x > 0:
                need = target - flowers[n - x]
                if need > 0:
                    budget -= need
            if budget < 0:
                break

            y = 0
            m = n - x                      # incomplete gardens : flowers[0..m-1]
            if m > 0:
                lo, hi = 0, m - 1
                while lo < hi:
                    mid = (lo + hi + 1) // 2
                    if flowers[mid] * (mid + 1) - pre[mid + 1] <= budget:
                        lo = mid
                    else:
                        hi = mid - 1
                cost = flowers[lo] * (lo + 1) - pre[lo + 1]
                y = flowers[lo] + (budget - cost) // (lo + 1)
                if y > target - 1:
                    y = target - 1

            cur = x * full + y * partial
            if cur > res:
                res = cur
        return res
