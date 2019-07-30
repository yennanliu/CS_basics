# Time:  O(logn) = O(32)
# Space: O(1)
#
# Write a function that takes an unsigned integer
# and returns the number of '1' bits it has (also known as the Hamming weight).
#
# For example, the 32-bit integer '11' has binary representation 00000000000000000000000000001011,
# so the function should return 3.


# V0 

# V1 
# http://bookshadow.com/weblog/2015/03/10/leetcode-number-1-bits/
# IDEA : BITWISE OPERATOR
# https://wiki.python.org/moin/BitwiseOperators
# x & y
# Does a "bitwise and". Each bit of the output is 1 if the corresponding bit of x AND of y is 1, otherwise it's 0.
# e.g. : 
# 111 & 111 = 111
# 111 & 100 = 100
# 1 & 0 = 0
# 1 & 1 = 1
# 0 & 0 = 0 
class Solution:
    # @param n, an integer
    # @return an integer
    def hammingWeight(self, n):
        ans = 0
        while n:
            ans += n & 1
            n >>= 1
        return ans

# V1' 
# http://bookshadow.com/weblog/2015/03/10/leetcode-number-1-bits/
class Solution:
    # @param n, an integer
    # @return an integer
    def hammingWeight(self, n):
        ans = 0
        while n:
            n &= n - 1
            ans += 1
        return ans

# V1''
# https://blog.csdn.net/coder_orz/article/details/51323188
# IDEA 
# The bin() method returns the binary string equivalent to the given integer.
class Solution(object):
    def hammingWeight(self, n):
        """
        :type n: int
        :rtype: int
        """
        return bin(n).count('1')

# V2 
# Time:  O(logn) = O(32)
# Space: O(1)
class Solution(object):
    # @param n, an integer
    # @return an integer
    def hammingWeight(self, n):
        result = 0
        while n:
            n &= n - 1
            result += 1
        return result
