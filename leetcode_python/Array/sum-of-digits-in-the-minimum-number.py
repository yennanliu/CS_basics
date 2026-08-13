"""

1085. Sum of Digits in the Minimum Number
Easy

Given an integer array nums, return 0 if the sum of the digits of the minimum
integer in nums is odd, or 1 otherwise.


Example 1:

Input: nums = [34,23,1,24,75,33,54,8]
Output: 0
Explanation: The minimal element is 1, and the sum of those digits is 1 which is odd, so the answer is 0.

Example 2:

Input: nums = [99,77,33,66,55]
Output: 1
Explanation: The minimal element is 33, and the sum of those digits is 3 + 3 = 6 which is even, so the answer is 1.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : find min, sum its digits, return the INVERTED parity
#        (odd digit sum -> 0, even digit sum -> 1)
# time = O(n)
# space = O(1)
class Solution(object):
    def sumOfDigits(self, nums):
        x = min(nums)
        s = 0
        while x:
            s += x % 10
            x //= 10
        return 1 - (s % 2)
