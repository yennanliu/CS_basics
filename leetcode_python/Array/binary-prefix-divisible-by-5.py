"""

1018. Binary Prefix Divisible By 5
Easy

You are given a binary array nums (0-indexed).

We define xi as the number whose binary representation is the subarray nums[0..i] (from most-significant-bit to least-significant-bit).

For example, if nums = [1,0,1], then x0 = 1, x1 = 2, and x2 = 5.

Return an array of booleans answer where answer[i] is true if xi is divisible by 5.


Example 1:

Input: nums = [0,1,1]
Output: [true,false,false]
Explanation: The input numbers in binary are 0, 01, 011; which are 0, 1, and 3 in base-10.
Only the first number is divisible by 5, so answer[0] is true.

Example 2:

Input: nums = [1,1,1]
Output: [false,false,false]


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : RUNNING REMAINDER (mod 5)
#
#   x_i = x_(i-1) * 2 + nums[i]
#   we only ever need x_i mod 5, so keep the remainder instead of the
#   (possibly 10^5 bit long) number itself.
#
#     cur = (cur * 2 + nums[i]) % 5
#
# time = O(n)
# space = O(1), excluding the output
class Solution(object):
    def prefixesDivBy5(self, nums):
        res = []
        cur = 0
        for v in nums:
            cur = (cur * 2 + v) % 5
            res.append(cur == 0)
        return res
