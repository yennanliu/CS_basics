
#V1 
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


# V2 
# Time:  O(1)
# Space: O(1)

class Solution(object):
    """
    :type num: int
    :rtype: int
    """
    def addDigits(self, num):
        return (num - 1) % 9 + 1 if num > 0 else 0

