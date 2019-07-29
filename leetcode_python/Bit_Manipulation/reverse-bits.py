
"""
https://leetcode.com/problems/reverse-bits/description/

# Time : O(logn) = O(32)
# Space: O(1)
#
# Reverse bits of a given 32 bits unsigned integer.
#
# For example, given input 43261596 (represented in binary as
# 00000010100101000001111010011100), return 964176192 (represented in binary
# as 00111001011110000010100101000000).
#
# Follow up:
# If this function is called many times, how would you optimize it?
#

"""

# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51705094
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        b = bin(n)[:1:-1]
        return int(b + '0'*(32-len(b)), 2)

# V1'
# https://blog.csdn.net/coder_orz/article/details/51705094
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        res = 0
        for i in range(32):
            res <<= 1
            res |= ((n >> i) & 1)
        return res

# V1''
# https://blog.csdn.net/coder_orz/article/details/51705094
class Solution(object):
    def reverseBits(self, n):
        """
        :type n: int
        :rtype: int
        """
        n = (n >> 16) | (n << 16);
        n = ((n & 0xff00ff00) >> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);
        return n

# V2 
# Time : O(logn) = O(32)
# Space: O(1)
class Solution(object):
    # @param n, an integer
    # @return an integer
    def reverseBits(self, n):
        result = 0
        for i in range(32):
            result <<= 1
            result |= n & 1
            n >>= 1
        return result

    def reverseBits2(self, n):
        string = bin(n)
        if '-' in string:
            string = string[:3] + string[3:].zfill(32)[::-1]
        else:
            string = string[:2] + string[2:].zfill(32)[::-1]
        return int(string, 2)

