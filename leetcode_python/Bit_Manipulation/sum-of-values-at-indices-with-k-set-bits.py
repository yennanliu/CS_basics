"""

2859. Sum of Values at Indices With K Set Bits
Easy

You are given a 0-indexed integer array nums and an integer k.

Return an integer that denotes the sum of elements in nums whose corresponding indices have exactly k set bits in their binary representation.

The set bits in an integer are the 1's present when it is written in binary.

For example, the binary representation of 21 is 10101, which has 3 set bits.


Example 1:

Input: nums = [5,10,1,5,2], k = 1
Output: 13
Explanation: The binary representation of the indices are:
0 = 000
1 = 001
2 = 010
3 = 011
4 = 100
Indices 1, 2, and 4 have k = 1 set bits in their binary representation.
Hence, the answer is nums[1] + nums[2] + nums[4] = 13.

Example 2:

Input: nums = [4,3,2,1], k = 2
Output: 1
Explanation: The binary representation of the indices are:
0 = 00
1 = 01
2 = 10
3 = 11
Only index 3 has k = 2 set bits in its binary representation.
Hence, the answer is nums[3] = 1.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 10^5
0 <= k <= 10

"""

# V0
# IDEA : POPCOUNT VIA `n & (n - 1)` BIT TRICK
#
#   Walk every index, count its set bits, and accumulate nums[i] when the count
#   equals k.
#
#   To count set bits we repeatedly clear the LOWEST set bit with `x &= x - 1`,
#   which runs once per 1-bit rather than once per bit position.
#
#   NOTE : k == 0 is allowed, and only index 0 has zero set bits — the loop
#          handles that naturally (no special case needed).
#   NOTE : bin(i).count('1') is the idiomatic one-liner, but the mask trick keeps
#          this Python 2/3 friendly and allocation free.
#
# time = O(n * log n), space = O(1)
class Solution(object):
    def sumIndicesWithKSetBits(self, nums, k):
        res = 0
        for i in range(len(nums)):
            x = i
            bits = 0
            while x:
                x &= x - 1
                bits += 1
            if bits == k:
                res += nums[i]
        return res
