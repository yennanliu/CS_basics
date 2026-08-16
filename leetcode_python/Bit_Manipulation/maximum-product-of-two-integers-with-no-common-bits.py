"""

3670. Maximum Product of Two Integers With No Common Bits
Medium

You are given an integer array nums.

Your task is to find two distinct indices i and j such that the product
nums[i] * nums[j] is maximized, and the binary representations of nums[i]
and nums[j] do not share any common set bits.

Return the maximum possible product of such a pair. If no such pair exists,
return 0.

Example 1:

Input: nums = [1,2,3,4,5,6,7]
Output: 12
Explanation:
The best pair is 3 (011) and 4 (100). They share no set bits and 3 * 4 = 12.

Example 2:

Input: nums = [5,6,4]
Output: 0
Explanation:
Every pair of numbers has at least one common set bit. Hence, the answer is 0.

Example 3:

Input: nums = [64,8,32]
Output: 2048
Explanation:
No pair of numbers share a common bit, so the answer is the product of the
two maximum elements, 64 and 32 (64 * 32 = 2048).

Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^6

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
# time = O(2^B * B + n) with B = 20 (nums[i] <= 10^6), space = O(2^B)
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
