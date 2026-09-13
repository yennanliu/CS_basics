"""

258. Add Digits
Easy

Given an integer num, repeatedly add all its digits until the result has only one digit, and return it.

Example 1:

Input: num = 38
Output: 2
Explanation: The process is
38 --> 3 + 8 --> 11
11 --> 1 + 1 --> 2
Since 2 has only one digit, return it.

Example 2:

Input: num = 0
Output: 0

Constraints:

0 <= num <= 2^31 - 1

Follow up: Could you do it without any loop/recursion in O(1) runtime?

"""

# V0

# V1
# class Solution(object):
# 	"""
# 	:type num: int
# 	:rtype: int
# 	"""
# 	def addDigits(self, num):
# 		output = 0 
# 		for i in str(num):
# 			output = output + int(i)
# 			status = output
# 		if status > 9:
# 			Solution().addDigits(status)
# 		return status


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/49161129
# time = O(log n)  # n = num
# space = O(1)
class Solution(object):
    def addDigits(self, num):
        """
        :type num: int
        :rtype: int
        """
        while num >= 10:
            temp = 0
            while num:
                temp += num % 10
                num /= 10
            num = temp
        return num


# V2'
# time = O(1)
# space = O(1)

class Solution(object):
    """
    :type num: int
    :rtype: int
    """
    def addDigits(self, num):
        return (num - 1) % 9 + 1 if num > 0 else 0

