"""

89. Gray Code
Medium

An n-bit gray code sequence is a sequence of 2n integers where:

Every integer is in the inclusive range [0, 2n - 1],
The first integer is 0,
An integer appears no more than once in the sequence,
The binary representation of every pair of adjacent integers differs by exactly one bit, and
The binary representation of the first and last integers differs by exactly one bit.
Given an integer n, return any valid n-bit gray code sequence.

 

Example 1:

Input: n = 2
Output: [0,1,3,2]
Explanation:
The binary representation of [0,1,3,2] is [00,01,11,10].
- 00 and 01 differ by one bit
- 01 and 11 differ by one bit
- 11 and 10 differ by one bit
- 10 and 00 differ by one bit
[0,2,3,1] is also a valid gray code sequence, whose binary representation is [00,10,11,01].
- 00 and 10 differ by one bit
- 10 and 11 differ by one bit
- 11 and 01 differ by one bit
- 01 and 00 differ by one bit
Example 2:

Input: n = 1
Output: [0,1]
 

Constraints:

1 <= n <= 16

"""

# V0
# IDEA : RECURSION & grayCode property
# -> binary representation of grayCode(n) can be composed of the solution of grayCode(n - 1) padded with zeros, concatenated with the reversed grayCode(n - 1) padded with ones.
# -> example : the binary representation of grayCode(2) is [00, 01, 11, 10]
#     -> the first half of which can be thought of as 0 followed by [0, 1] (the binary representation of grayCode(1)), 
#     -> and the second half of which can be thought of as 1 followed by [1, 0] (the reversed binary representation of grayCode(1)).
#     -> (0 and 1 are treated as a special case)
#
# How does int(x[,base]) work?
# -> https://stackoverflow.com/questions/33664451/how-does-intx-base-work
# -> int(string, base) accepts an arbitrary base. You are probably familiar with binary and hexadecimal, and perhaps octal; these are just ways of noting an integer number in different bases:
# exmaple :
# In [76]: int('10',2)      # transform '10' to a 2 bases int                                                 
# Out[76]: 2
#
# In [77]: int('11',2)      # transform '11' to a 2 bases int                                                      
# Out[77]: 3
#
# In [78]: int('100',2)     # transform '100' to a 2 bases int                                                       
# Out[78]: 4
class Solution:
    def grayCode(self, n):
        
        def help(n):
            if n == 0:
                return ['0']
            if n == 1:
                return ['0', '1']
            prev = help(n - 1)
            return ['0' + code for code in prev] + ['1' + code for code in reversed(prev)]
        
        return [int(code, 2) for code in help(n)]

# V1
# https://leetcode.com/problems/gray-code/discuss/312866/40-ms-recursive-Python-solution
class Solution:
    def grayCode(self, n):
        
        def help(n):
            if n == 0:
                return ['0']
            if n == 1:
                return ['0', '1']
            prev = help(n - 1)
            return ['0' + code for code in prev] + ['1' + code for code in reversed(prev)]
        
        return [int(code, 2) for code in help(n)]

# V1'
# https://ithelp.ithome.com.tw/articles/10213273
# DEMO
# In [23]: add=1

# In [24]: add<<=1

# In [25]: add
# Out[25]: 2

# In [26]: add<<=1

# In [27]: add
# Out[27]: 4

# In [28]: add<<=1

# In [29]: add
# Out[29]: 8

# In [30]: add<<=1

# In [31]: add
# Out[31]: 16

# In [32]: add<<=1

# In [33]: add
# Out[33]: 32
class Solution:
    def grayCode(self, n):
        res = [0]
        add = 1
        for _ in range(n):
            for i in range(add):
                res.append(res[add - 1 - i] + add);
            add <<= 1
        return res

### Test case : dev 

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80664204
# IDEA : ITERATION
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        grays = dict()
        grays[0] = ['0']
        grays[1] = ['0', '1']
        for i in range(2, n + 1):
            n_gray = []
            for pre in grays[i - 1]:
                n_gray.append('0' + pre)
            for pre in grays[i - 1][::-1]:
                n_gray.append('1' + pre)
            grays[i] = n_gray
        return map(lambda x: int(x, 2), grays[n])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80664204
# IDEA : RECURSION
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        return map(lambda x: int(x, 2), self.bit_gray(n))
    
    def bit_gray(self, n):
        ans = None
        if n == 0:
            ans = ['0']
        elif n == 1:
            ans = ['0', '1']
        else:
            pre_gray = self.bit_gray(n - 1)
            ans = ['0' + x for x in pre_gray] + ['1' + x for x in pre_gray[::-1]]
        return ans

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78371259
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        res = []
        size = 1 << n  # if n=4, left move 4 digits, from 1 to 10000, which is 16 
        for i in range(size):
            res.append((i >> 1) ^ i)
        return res

# V2 
# Time:  O(2^n)
# Space: O(1)
class Solution(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        result = [0]
        for i in range(n):
            for n in reversed(result):
                result.append(1 << i | n)
        return result

# Proof of closed form formula could be found here:
# http://math.stackexchange.com/questions/425894/proof-of-closed-form-formula-to-convert-a-binary-number-to-its-gray-code
class Solution2(object):
    def grayCode(self, n):
        """
        :type n: int
        :rtype: List[int]
        """
        return [i >> 1 ^ i for i in range(1 << n)]
