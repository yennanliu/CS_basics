"""

3574. Maximize Subarray GCD Score
Hard

You are given an array of positive integers nums and an integer k.

You may perform at most k operations. In each operation, you can choose one element in the array and double its value. Each element can be doubled at most once.

The score of a contiguous subarray is defined as the product of its length and the greatest common divisor (GCD) of all its elements.

Your task is to return the maximum score that can be achieved by selecting a contiguous subarray from the modified array.

Note:

The greatest common divisor (GCD) of an array is the largest integer that evenly divides all the array elements.


Example 1:

Input: nums = [2,4], k = 1
Output: 8
Explanation:
Double nums[0] to 4 using one operation. The modified array becomes [4, 4].
The GCD of the subarray [4, 4] is 4, and the length is 2.
Thus, the maximum possible score is 2 * 4 = 8.

Example 2:

Input: nums = [3,5,7], k = 2
Output: 14
Explanation:
Double nums[2] to 14 using one operation. The modified array becomes [3, 5, 14].
The GCD of the subarray [14] is 14, and the length is 1.
Thus, the maximum possible score is 1 * 14 = 14.

Example 3:

Input: nums = [5,5,5], k = 1
Output: 15
Explanation:
The subarray [5, 5, 5] has a GCD of 5, and its length is 3.
Thus, the maximum possible score is 3 * 5 = 15.


Constraints:

1 <= n == nums.length <= 1500
1 <= nums[i] <= 10^9
1 <= k <= n

"""

# V0
# IDEA : DOUBLING CAN ONLY LIFT THE GCD BY ONE FACTOR OF 2
#
#   write every element as (odd part) * 2^v. the gcd of a window is
#   (gcd of odd parts) * 2^(min v). doubling an element bumps its v by one
#   and leaves its odd part alone, so the gcd never gains a new odd factor —
#   at best its power of two rises by exactly one, and only if *every*
#   element sitting at the minimal v gets doubled.
#
#   so for each window we just need the gcd and how many elements attain the
#   minimal 2-adic valuation; if that count fits in k, the doubled score is
#   available too.
#
# time = O(n^2 * log(max)), space = O(n)
from math import gcd


class Solution(object):
    def maxGCDScore(self, nums, k):
        n = len(nums)
        # 2-adic valuation of each element
        val = [(v & -v).bit_length() - 1 for v in nums]

        ans = 0
        for i in range(n):
            g = 0
            lo = 64
            cnt = 0
            for j in range(i, n):
                g = gcd(g, nums[j])
                vj = val[j]
                if vj < lo:
                    lo = vj
                    cnt = 1
                elif vj == lo:
                    cnt += 1
                length = j - i + 1
                s = length * g
                if s > ans:
                    ans = s
                if cnt <= k:
                    s = length * g * 2
                    if s > ans:
                        ans = s
        return ans
