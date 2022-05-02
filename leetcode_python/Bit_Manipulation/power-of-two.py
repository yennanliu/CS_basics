"""

231. Power of Two
Easy

Given an integer n, return true if it is a power of two. Otherwise, return false.

An integer n is a power of two, if there exists an integer x such that n == 2x.


Example 1:

Input: n = 1
Output: true
Explanation: 20 = 1
Example 2:

Input: n = 16
Output: true
Explanation: 24 = 16
Example 3:

Input: n = 3
Output: false
 

Constraints:

-231 <= n <= 231 - 1
 

Follow up: Could you solve it without loops/recursion?

"""

# V0 
# IDEA : BRUTE FORCE
class Solution(object):
    def isPowerOfTwo(self, n):
        if n < 1:
            return False
        while n%2 == 0:
            n /= 2
        return True if n==1 else False

# V0'
# IDEA : BIT OP
# IDEA : Bitwise operators : Turn off the Rightmost 1-bit
# https://leetcode.com/problems/power-of-two/solution/
# IDEA :
#    -> if x is power of 2 : 
#        -> x's binary MUST as "100000" or "100" or "1"
#        -> e.g. ONLY one "1" and "0" for else
#    -> so for x - 1, it will become
#        -> (x-1) binary : "11111", "11", "0"
#    -> so x & (x-1) ALWAYS == 0 (if x is power of 2)
class Solution(object):
    def isPowerOfTwo(self, n):
        if n == 0:
            return False
        return n & (n - 1) == 0

# V0''
# IDEA : BIT MANIPULATION
# IDEA :
# -> IF n is power of 2 
# -> binary(n) must in the form like this : "10000" or "100" or "1000000"...
# -> so we can know that n & (n-1) == 0 when n > 0 by BIT MANIPULATION 
class Solution:
    def isPowerOfTwo(self, n):
        return n > 0 and (n & (n - 1)) == 0

# V1
# IDEA : Bitwise Operators : Get the Rightmost 1-bit
# https://leetcode.com/problems/power-of-two/solution/
class Solution(object):
    def isPowerOfTwo(self, n):
        if n == 0:
            return False
        return n & (-n) == n

# V1'
# IDEA : Bitwise operators : Turn off the Rightmost 1-bit
# https://leetcode.com/problems/power-of-two/solution/
class Solution(object):
    def isPowerOfTwo(self, n):
        if n == 0:
            return False
        return n & (n - 1) == 0

# V1''
# https://blog.csdn.net/coder_orz/article/details/51322908
# IDEA : GREEDY 
class Solution:
    def isPowerOfTwo(self,n):
        if n < 1:
            return False
        while n%2 == 0:
            n = n/2 
        return True if  n==1 else False

### Test case
s=Solution()
assert s.isPowerOfTwo(0)==False
assert s.isPowerOfTwo(1)==True
assert s.isPowerOfTwo(-4)==False
assert s.isPowerOfTwo(15)==False
assert s.isPowerOfTwo(16)==True
assert s.isPowerOfTwo(36)==False
assert s.isPowerOfTwo(20)==False
assert s.isPowerOfTwo(7)==False
assert s.isPowerOfTwo(150)==False
assert s.isPowerOfTwo(2**32)==True
assert s.isPowerOfTwo(2**100)==True

# V1'''
# http://bookshadow.com/weblog/2015/07/06/leetcode-power-of-two/
# IDEA : if x = 2**k, k is a positive integer
# -> bin(x) should be this form : '1000000..000'
# i.e. only the highest bin(x) == 1, others == 0 
# -> equal n & (n - 1) = 0 and n > 0 
#
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

# V2
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