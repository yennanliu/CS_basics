

"""
# https://github.com/kamyu104/LeetCode/blob/master/Python/plus-one.py

Given a non-empty array of digits representing a non-negative integer, plus one to the integer.

The digits are stored such that the most significant digit is at the head of the list, and each element in the array contain a single digit.

You may assume the integer does not contain any leading zero, except the number 0 itself.

Example 1:

Input: [1,2,3]
Output: [1,2,4]
Explanation: The array represents the integer 123.
Example 2:

Input: [4,3,2,1]
Output: [4,3,2,2]
Explanation: The array represents the integer 4321.

"""



# V1  : dev 
"""
class Solution:
	def plusOne(self, digits):
		if len(digits) == 1:
			return digits+1
		else:
			for k in list(reversed(range(len(digits)))):
				digits[k] = digits[k]+1
				if digits[k] < 9:
					pass
				else:
					digits[k] == 0
					Solution().plusOne(digits[-1])
				return  digits

"""


# V2 

class Solution(object):
    def plusOne(self, digits):
        """
        :type digits: List[int]
        :rtype: List[int]
        """
        for i in reversed(range(len(digits))):
            if digits[i] == 9:
                digits[i] = 0
            else:
                digits[i] += 1
                return digits
        # for [9], [9,9], amd [9,9,9] cases...
        digits[0] = 1
        digits.append(0)
        return digits




# V3 

class Solution2(object):
    def plusOne(self, digits):
        """
        :type digits: List[int]
        :rtype: List[int]
        """
        result = digits[::-1]
        carry = 1
        for i in xrange(len(result)):
            result[i] += carry
            carry, result[i] = divmod(result[i], 10)
        if carry:
            result.append(carry)
        return result[::-1]




        







