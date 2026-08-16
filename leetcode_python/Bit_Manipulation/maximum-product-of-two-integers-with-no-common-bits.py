"""

3670. Maximum Product of Two Integers With No Common Bits
Medium

You are given an integer array nums.

Your task is to find the maximum value of nums[i] * nums[j], where nums[i] and nums[j] have no common set bits, i.e. nums[i] & nums[j] == 0.

Return the maximum product, or 0 if no such pair of indices exists.


Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation:
The only pair with no common set bits is (1, 2), since 1 & 2 == 0, and its product is 1 * 2 = 2.


Constraints:

2 <= nums.length <= 10^5
0 <= nums[i] < 2^18

"""

# V0
# IDEA : SUM-OVER-SUBSETS DP — BEST PARTNER FOR EVERY MASK
#
#   "no common bits with v" means the partner is a submask of ~v. so if
#   best[m] = the largest array element that is a submask of m, then the best
#   partner of v is best[FULL ^ v] and the answer is max over v of
#   v * best[FULL ^ v].
#
#   best is the classic SOS dp: seed best[v] = v for every present value, then
#   for each bit b let every mask having b absorb the same mask with b cleared.
#   that is O(2^B * B) instead of the O(3^B) submask enumeration.
#
#   the i != j requirement is free: a value can only pair with itself when
#   v & v == 0, i.e. v == 0, and then the product is 0 anyway.
#
# time = O(2^B * B + n) with B = 18, space = O(2^B)
class Solution(object):
    def maxProduct(self, nums):
        mx = max(nums)
        bits = max(1, mx.bit_length())
        size = 1 << bits
        best = [0] * size
        for v in nums:
            if v > best[v]:
                best[v] = v
        for b in range(bits):
            half = 1 << b
            step = half << 1
            for lo in range(0, size, step):
                mid = lo + half
                hi = mid + half
                best[mid:hi] = list(map(max, best[mid:hi], best[lo:mid]))
        full = size - 1
        ans = 0
        for v in nums:
            w = best[full ^ v]
            if v * w > ans:
                ans = v * w
        return ans
