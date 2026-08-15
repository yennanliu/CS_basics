"""

3079. Find the Sum of Encrypted Integers
Easy

You are given an integer array nums containing positive integers. We define a function encrypt such that encrypt(x) replaces every digit in x with the largest digit in x.

For example, encrypt(523) = 555 and encrypt(213) = 333.

Return the sum of encrypted elements.


Example 1:

Input: nums = [1,2,3]
Output: 6
Explanation: The encrypted elements are [1,2,3]. The sum of encrypted elements is 1 + 2 + 3 == 6.

Example 2:

Input: nums = [10,21,31]
Output: 66
Explanation: The encrypted elements are [11,22,33]. The sum of encrypted elements is 11 + 22 + 33 == 66.


Constraints:

1 <= nums.length <= 50
1 <= nums[i] <= 1000

"""

# V0
# IDEA : REPEAT THE LARGEST DIGIT AS MANY TIMES AS THERE ARE DIGITS
#
#   encrypt(x) is the string max(str(x)) repeated len(str(x)) times, read
#   back as an integer — no arithmetic on the digits is needed.
#
# time = O(n * digits), space = O(digits)
class Solution(object):
    def sumOfEncryptedInt(self, nums):
        total = 0
        for x in nums:
            s = str(x)
            total += int(max(s) * len(s))
        return total
