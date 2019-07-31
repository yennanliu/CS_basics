# https://leetcode.com/problems/power-of-four/description/
# Time:  O(1)
# Space: O(1)
# Given an integer (signed 32 bits), write a function to check whether it is a power of 4.
#
# Example:
# Given num = 16, return true. Given num = 5, return false.
#
# Follow up: Could you solve it without loops/recursion?

# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/51291091
# IDEA :  RECURSION 
class Solution(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        if num <= 0: return False
        if num == 1: return True
        if num % 4 == 0:
            return self.isPowerOfFour(num / 4)
        return False

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/51291091
# IDEA : ITERARION 
class Solution(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        if num <= 0: return False
        while num % 4 == 0:
            num /= 4
        return num == 1

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/51291091
# IDEA : BIT MANIPULATION 
class Solution(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        return num > 0 and (num & (num - 1)) == 0 and (num & 0x55555555) != 0

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/51291091
# IDEA : FUNC + LOG 
class Solution(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        return num > 0 and (4 ** (int(math.log(num, 4)))) == num

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        return num > 0 and (num & (num - 1)) == 0 and \
               ((num & 0b01010101010101010101010101010101) == num)


# Time:  O(1)
# Space: O(1)
class Solution2(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        while num and not (num & 0b11):
            num >>= 2
        return (num == 1)


class Solution3(object):
    def isPowerOfFour(self, num):
        """
        :type num: int
        :rtype: bool
        """
        num = bin(num)
        return True if num[2:].startswith('1') and len(num[2:]) == num.count('0') and num.count('0') % 2 and '-' not in num else False
