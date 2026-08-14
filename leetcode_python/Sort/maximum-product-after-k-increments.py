"""

2233. Maximum Product After K Increments
Medium

You are given an array of non-negative integers nums and an integer k. In one operation, you may choose any element from nums and increment it by 1.

Return the maximum product of nums after at most k operations. Since the answer may be very large, return it modulo 10^9 + 7. Note that you should maximize the product before taking the modulo.


Example 1:

Input: nums = [0,4], k = 5
Output: 20
Explanation: Increment the first number 5 times.
Now nums = [5, 4], with a product of 5 * 4 = 20.
It can be shown that 20 is maximum product possible, so we return 20.
Note that there may be other ways to increment nums to have the maximum product.

Example 2:

Input: nums = [6,3,3,2], k = 2
Output: 216
Explanation: Increment the second number 1 time and increment the fourth number 1 time.
Now nums = [6, 4, 3, 3], with a product of 6 * 4 * 3 * 3 = 216.
It can be shown that 216 is maximum product possible, so we return 216.
Note that there may be other ways to increment nums to have the maximum product.


Constraints:

1 <= nums.length, k <= 10^5
0 <= nums[i] <= 10^6

"""

# V0
# IDEA : GREEDY + MIN-HEAP (always feed the currently smallest element)
#
#   exchange argument : if a <= b, moving one unit from b to a multiplies the
#   pair product from a*b to (a+1)*(b-1) = a*b + (b - a - 1) >= a*b whenever
#   b > a. so an optimal allocation keeps the values as EQUAL as possible,
#   which is exactly "give every +1 to the current minimum".
#
#   NOTE : take the modulo only at the very end, when multiplying out - the
#          greedy choice itself must compare the true (unmodded) values.
#
# time = O((n + k) log n), space = O(n)
import heapq
class Solution(object):
    def maximumProduct(self, nums, k):
        MOD = 10 ** 9 + 7
        h = list(nums)
        heapq.heapify(h)
        for _ in range(k):
            heapq.heapreplace(h, h[0] + 1)

        res = 1
        for v in h:
            res = res * v % MOD
        return res
