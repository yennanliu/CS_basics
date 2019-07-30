# Time:  O(1)
# Space: O(1)
#
# Given a range [m, n] where 0 <= m <= n <= 2147483647,
# return the bitwise AND of all numbers in this range, inclusive.
#
# For example, given the range [5, 7], you should return 4.
#

# V0 

# V1 
# http://bookshadow.com/weblog/2015/04/17/leetcode-bitwise-and-numbers-range/
# https://blog.csdn.net/fuxuemingzhu/article/details/79495633
# https://wiki.python.org/moin/BitwiseOperators
# IDEA : BITWISE OPERATOR 
#x << y : Returns x with the bits shifted to the left by y places (and new bits on the right-hand-side are zeros). This is the same as multiplying x by 2**y.
#x >> y : Returns x with the bits shifted to the right by y places. This is the same as //'ing x by 2**y.

# e.g. 
# https://stackoverflow.com/questions/22832615/what-do-and-mean-in-python
# example 1) 12 << 2 
# bin(12) = '0b1100'.     # decimal -> binary
# -> '1100' + '00' = '110000' # 2 left shift 
# int('110000', 2) = 48   # binary -> decimal 
# example 2) 48 >> 2
# bin(48) =  'b110000'.     # decimal -> binary
# -> '110000' - '00' = '1100' # 2 left shift
# int('1100', 2) = 12   # binary -> decimal
class Solution:
    # @param m, an integer
    # @param n, an integer
    # @return an integer
    def rangeBitwiseAnd(self, m, n):
        p = 0
        while m != n:
            m >>= 1
            n >>= 1
            p += 1
        return m << p

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    # @param m, an integer
    # @param n, an integer
    # @return an integer
    def rangeBitwiseAnd(self, m, n):
        while m < n:
            n &= n - 1
        return n

class Solution2(object):
    # @param m, an integer
    # @param n, an integer
    # @return an integer
    def rangeBitwiseAnd(self, m, n):
        i, diff = 0, n-m
        while diff:
            diff >>= 1
            i += 1
        return n & m >> i << i
