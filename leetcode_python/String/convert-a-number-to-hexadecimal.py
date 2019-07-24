# Time:  O(logn)
# Space: O(1)

# Given an integer, write an algorithm to convert it to hexadecimal.
# For negative integer, two’s complement method is used.
#
# IMPORTANT:
# You must not use any method provided by the library which converts/formats
# the number to hex directly. Such solution will result in disqualification of
# all your submissions to this problem. Users may report such solutions after the
# contest ends and we reserve the right of final decision and interpretation
# in the case of reported solutions.
#
# Note:
#
# All letters in hexadecimal (a-f) must be in lowercase.
# The hexadecimal string must not contain extra leading 0s. If the number is zero,
# it is represented by a single zero character '0'; otherwise,
# the first character in the hexadecimal string will not be the zero character.
# The given number is guaranteed to fit within the range of a 32-bit signed integer.
# You must not use any method provided by the library which converts/formats the number to hex directly.
# Example 1:
#
# Input:
# 26
#
# Output:
# "1a"
# Example 2:
#
# Input:
# -1
#
# Output:
# "ffffffff"

# V0 

# V1 
# IDEA : Decimal -> Hexadecimal
# Decimal : {0,1,2,3,4,5,6,7,8,9} 
# Hexadecimal : {0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f}
# https://www.cnblogs.com/grandyang/p/5926674.html
# e.g.   
# 50 -> 32 ( 50%16 = 3 + 2)
# 26 -> 1a ( 26%16 = 1 + 10 = 1 + a)
# https://www.jianshu.com/p/2d57cec55393
# divmod(17, 16) = (1,1)
# divmod(3, 1) = (3,0)
def toHex(num):
    ret = ''
    map = ('0', '1','2','3','4','5','6','7','8','9','a','b','c','d','e','f')
    if num == 0:
        return '0'
    if num < 0:
        num += 2**32 # if num < 0, use num = num + 2**32 to deal with it 
    while num > 0 :
        num, val = divmod(num, 16)
        ret += map[val]
    return ret[::-1]

# V2
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def toHex(self, num):
        """
        :type num: int
        :rtype: str
        """
        if not num:
            return "0"

        result = []
        while num and len(result) != 8:
            h = num & 15
            if h < 10:
                result.append(str(chr(ord('0') + h)))
            else:
                result.append(str(chr(ord('a') + h-10)))
            num >>= 4
        result.reverse()

        return "".join(result)
