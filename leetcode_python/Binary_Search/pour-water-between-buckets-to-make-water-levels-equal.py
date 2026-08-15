"""

2137. Pour Water Between Buckets to Make Water Levels Equal
Medium
🔒 (premium)

You have n buckets each containing some gallons of water in it, represented by a 0-indexed integer array buckets, where the ith bucket contains buckets[i] gallons of water. You are also given an integer loss.

You want to make the amount of water in each bucket equal. You can pour any amount of water from one bucket to another bucket (not necessarily an integer). However, every time you pour k gallons of water, you spill loss percent of k.

Return the maximum amount of water in each bucket after making the amount of water equal. Answers within 10^-5 of the actual answer will be accepted.


Example 1:

Input: buckets = [1,2,7], loss = 80
Output: 2.00000
Explanation: Pour 5 gallons of water from buckets[2] to buckets[0].
5 * 80% = 4 gallons are spilled and buckets[0] only receives 5 - 4 = 1 gallon of water.
All buckets have 2 gallons of water in them so return 2.

Example 2:

Input: buckets = [2,4,6], loss = 50
Output: 3.50000
Explanation: Pour 0.5 gallons of water from buckets[1] to buckets[0].
0.5 * 50% = 0.25 gallons are spilled and buckets[0] only receives 0.5 - 0.25 = 0.25 gallons of water.
Now, buckets = [2.25, 3.5, 6].
Pour 2.5 gallons of water from buckets[2] to buckets[0].
2.5 * 50% = 1.25 gallons are spilled and buckets[0] only receives 2.5 - 1.25 = 1.25 gallons of water.
All buckets have 3.5 gallons of water in them so return 3.5.

Example 3:

Input: buckets = [3,3,3,3], loss = 40
Output: 3.00000
Explanation: All buckets already have the same amount of water in them.


Constraints:

1 <= buckets.length <= 10^5
0 <= buckets[i] <= 10^5
0 <= loss <= 99

"""

# V0
# IDEA : BINARY SEARCH ON THE FINAL LEVEL (MONOTONE FEASIBILITY)
#
#   a target level x is reachable iff the water the over-full buckets can
#   DELIVER covers what the under-full ones NEED :
#
#       supply = sum(b - x for b > x) * (1 - loss/100)     # after spillage
#       demand = sum(x - b for b < x)
#       feasible(x)  <=>  supply >= demand
#
#   raising x makes supply shrink and demand grow, so feasibility is
#   monotone — binary search on the real interval [0, max(buckets)].
#
#   100 iterations of bisection shrink the interval by 2^-100, far inside
#   the 10^-5 tolerance.
#
# time = O(n * 100), space = O(1)
class Solution(object):
    def equalizeWater(self, buckets, loss):
        keep = 1.0 - loss / 100.0

        def feasible(x):
            supply = demand = 0.0
            for b in buckets:
                if b > x:
                    supply += (b - x) * keep
                else:
                    demand += (x - b)
            return supply >= demand

        lo, hi = 0.0, float(max(buckets))
        for _ in range(100):
            mid = (lo + hi) / 2
            if feasible(mid):
                lo = mid
            else:
                hi = mid
        return lo
