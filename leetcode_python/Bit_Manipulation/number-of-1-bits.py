"""
191. Number of 1 Bits (Hamming weight)
Easy

Share
Write a function that takes an unsigned integer and returns the number of '1' bits it has (also known as the Hamming weight).

Note:

Note that in some languages, such as Java, there is no unsigned integer type. In this case, the input will be given as a signed integer type. It should not affect your implementation, as the integer's internal binary representation is the same, whether it is signed or unsigned.
In Java, the compiler represents the signed integers using 2's complement notation. Therefore, in Example 3, the input represents the signed integer. -3.
 

Example 1:

Input: n = 00000000000000000000000000001011
Output: 3
Explanation: The input binary string 00000000000000000000000000001011 has a total of three '1' bits.
Example 2:

Input: n = 00000000000000000000000010000000
Output: 1
Explanation: The input binary string 00000000000000000000000010000000 has a total of one '1' bit.
Example 3:

Input: n = 11111111111111111111111111111101
Output: 31
Explanation: The input binary string 11111111111111111111111111111101 has a total of thirty one '1' bits.
 

Constraints:

The input must be a binary string of length 32.
 

Follow up: If this function is called many times, how would you optimize it?

"""

# V0
# The bin() method returns the binary string equivalent to the given integer.
class Solution(object):
    def hammingWeight(self, n):
        """
        :type n: int
        :rtype: int
        """
        return bin(n).count('1')

# V0'
# IDEA : bit manipulation : n&(n-1) CAN REMOVE LAST 1 PER LOOP
# https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%B8%B8%E7%94%A8%E7%9A%84%E4%BD%8D%E6%93%8D%E4%BD%9C.md
class Solution:
    def hammingWeight(self, n: int) -> int:
        
        # define a count, number of `1`
        count = 0

        # before n != 0 (binary), we will use a while loop keep remove `1` and update number of `1`
        while n!=0:
           
            # use the ` n&(n-1)` trick mentioned in fucking-algorithm to remove last `1`
            n = n & (n-1)

            count+=1
        
        # when n==0, return the total count of `1`
        return count
    
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
