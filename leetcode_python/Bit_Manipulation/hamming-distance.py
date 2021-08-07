"""
The Hamming distance between two integers is the number of positions at which the corresponding bits are different.

Given two integers x and y, return the Hamming distance between them.

 

Example 1:

Input: x = 1, y = 4
Output: 2
Explanation:
1   (0 0 0 1)
4   (0 1 0 0)
       ↑   ↑
The above arrows point to positions where the corresponding bits are different.

Example 2:

Input: x = 3, y = 1
Output: 1
 

Constraints:

0 <= x, y <= 231 - 1

"""

# V0
# IDEA : STR -> BIN -> COMPARE
class Solution:
    def hammingDistance(self, x: int, y: int):
        _x, _y = str(bin(x)[2:]), str(bin(y)[2:])
        _len = max(len(_x), len(_y))
        if _len > len(_y):
            _y = "0" * (_len - len(_y)) + _y
        else:
            _x = "0" * (_len - len(_x)) + _x
        r = 0
        for i in range(len(_x)):
            if _x[i] != _y[i]:
                r += 1
        return r

# V0'
# IDEA  : BITWISE OPERATOR 
# https://github.com/yennanliu/CS_basics/blob/master/doc/bit_manipulation.md
# XOR : if (a,b) = (1,0) or (0,1), then a ^ b = 1, otherwise = 0
class Solution(object):
    def hammingDistance(self, x, y):
        return bin(x ^ y).count('1')

# V1 
# http://bookshadow.com/weblog/2016/12/18/leetcode-hamming-distance/
# IDEA  : BITWISE OPERATOR 
# https://wiki.python.org/moin/BitwiseOperators
# x ^ y
# Does a "bitwise exclusive or". Each bit of the output is the same as the corresponding bit in x if that bit in y is 0, and it's the complement of the bit in x if that bit in y is 1.
class Solution(object):
    def hammingDistance(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        return bin(x ^ y).count('1')

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/54138423
class Solution(object):
    def hammingDistance(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        return bin(x ^ y).count('1')

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/54138423
class Solution:
    def hammingDistance(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        res = 0
        while x or y:
            if (x & 1) ^ (y & 1):
                res += 1
            x >>= 1
            y >>= 1
        return res

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/54138423
class Solution:
    def hammingDistance(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        xor = x ^ y
        res = 0
        while xor:
            res += xor & 1
            xor >>= 1
        return res

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def hammingDistance(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        distance = 0
        z = x ^ y
        while z:
            distance += 1
            z &= z - 1
        return distance

    def hammingDistance2(self, x, y):
        """
        :type x: int
        :type y: int
        :rtype: int
        """
        return bin(x ^ y).count('1')