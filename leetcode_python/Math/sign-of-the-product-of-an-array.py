"""

1822. Sign of the Product of an Array
Easy

Implement a function signFunc(x) that returns:

1 if x is positive.
-1 if x is negative.
0 if x is equal to 0.

You are given an integer array nums. Let product be the product of all values in the array nums.

Return signFunc(product).


Example 1:

Input: nums = [-1,-2,-3,-4,3,2,1]
Output: 1
Explanation: The product of all values in the array is 144, and signFunc(144) = 1

Example 2:

Input: nums = [1,5,0,2,-3]
Output: 0
Explanation: The product of all values in the array is 0, and signFunc(0) = 0

Example 3:

Input: nums = [-1,1,-1,1,-1]
Output: -1
Explanation: The product of all values in the array is -1, and signFunc(-1) = -1


Constraints:

1 <= nums.length <= 1000
-100 <= nums[i] <= 100

"""

# V0
# IDEA : TRACK THE SIGN ONLY (never build the product)
#
#   a single 0 makes the whole product 0 -> return immediately.
#   otherwise the sign flips once per negative element, so the answer is
#   determined by the PARITY of the negative count.
#
#   NOTE : multiplying the values out would overflow in most languages and is
#          pure waste in python -- only the sign carries information.
#
# time = O(n), space = O(1)
class Solution(object):
    def arraySign(self, nums):
        sign = 1
        for v in nums:
            if v == 0:
                return 0
            if v < 0:
                sign = -sign
        return sign
