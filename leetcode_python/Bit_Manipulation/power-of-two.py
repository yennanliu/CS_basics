"""
# https://leetcode.com/problems/power-of-two/description/
# https://github.com/yennanliu/LeetCode/blob/master/Python/power-of-two.py
Example 1:

Input: 1
Output: true 
Explanation: 20 = 1
Example 2:

Input: 16
Output: true
Explanation: 24 = 16
Example 3:

Input: 218
Output: false
"""

# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51322908
# IDEA : GREEDY 
class Solution:
    def isPowerOfTwo(self,n):
        if n < 1:
            return False
        while n%2 == 0:
            n = n/2 
        return True if  n==1 else False

# V2 
# http://bookshadow.com/weblog/2015/07/06/leetcode-power-of-two/
# IDEA : if x = 2**k, k is a positive integer
# -> bin(x) should be this form : '1000000..000'
# i.e. only the highest bin(x) == 1, others == 0 
# -> equal n & (n - 1) = 0 and n > 0 

# &	 : binary AND	
# https://www.tutorialspoint.com/python/bitwise_operators_example
# & Binary AND  Operator copies a bit to the result if it exists in both operands   (a & b) (means 0000 1100)
# e.g. : x& y = 0 (0000 0000)
class Solution:
    # @param {integer} n
    # @return {boolean}
    def isPowerOfTwo(self, n):
        return n > 0 and (n & (n - 1)) == 0

class Solution:
    def isPowerOfTwo(self,n):
        for k in range(n):
            if n == 2**k:
                return True
            else:
                pass
            k=k+1
        return False

# V3
# Time:  O(1)
# Space: O(1)
class Solution(object):
    # @param {integer} n
    # @return {boolean}
    def isPowerOfTwo(self, n):
        return n > 0 and (n & (n - 1)) == 0

class Solution2(object):
    # @param {integer} n
    # @return {boolean}
    def isPowerOfTwo(self, n):
        return n > 0 and (n & ~-n) == 0
        