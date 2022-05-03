"""

67. Add Binary
Easy


Given two binary strings a and b, return their sum as a binary string.

 

Example 1:

Input: a = "11", b = "1"
Output: "100"
Example 2:

Input: a = "1010", b = "1011"
Output: "10101"
 

Constraints:

1 <= a.length, b.length <= 104
a and b consist only of '0' or '1' characters.
Each string does not contain leading zeros except for the zero itself.

"""

# V0
# IDEA : Bit-by-Bit Computation
class Solution:
    def addBinary(self, a, b):
        n = max(len(a), len(b))
        """
        NOTE : zfill syntax
            -> fill n-1 "0" to a string at beginning

            example :
                In [10]: x = '1'

                In [11]: x.zfill(2)
                Out[11]: '01'

                In [12]: x.zfill(3)
                Out[12]: '001'

                In [13]: x.zfill(4)
                Out[13]: '0001'

                In [14]: x.zfill(10)
                Out[14]: '0000000001'
        """
        a, b = a.zfill(n), b.zfill(n)
        
        carry = 0
        answer = []
        for i in range(n - 1, -1, -1):
            if a[i] == '1':
                carry += 1
            if b[i] == '1':
                carry += 1
                
            if carry % 2 == 1:
                answer.append('1')
            else:
                answer.append('0')
            
            carry //= 2
        
        if carry == 1:
            answer.append('1')
        answer.reverse()
        
        return ''.join(answer)

# V0'
# IDEA : STRING + BINARY
class Solution(object):
    def addBinary(self, a, b):

        _len = max(len(a), len(b))
        if len(a) < _len:
            a = (_len - len(a)) * '0' + a
        if len(b) < _len:
            b = (_len - len(b)) * '0' + b

        plus = 0
        result = ""

        # INVERSE LOOPING THE a, b
        for i in range(len(a))[::-1]:
            tmp = int(a[i]) + int(b[i]) + plus
            if tmp > 1:
                tmp -= 2
                plus = 1
            else:
                plus = 0

            result += str(tmp)

        if plus == 1:
            return '1' + result[::-1]  ### NOTE WE NEED TO REVERSE IT!
        else: 
            return result[::-1] ### NOTE WE NEED TO REVERSE IT!

# V0''
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        res = ''
        i, j, plus = len(a)-1, len(b)-1, 0
        while i>=0 or j>=0 or plus==1:
            plus += int(a[i]) if i>= 0 else 0
            plus += int(b[j]) if j>= 0 else 0
            res = str(plus % 2) + res
            i, j, plus = i-1, j-1, plus//2  # since max of "plus" is 3 in bimary case, so "plus//2" works here
        return res

# V0'''
class Solution(object):
    # @param a, a string
    # @param b, a string
    # @return a string
    def addBinary(self, a, b):
        result, carry, val = "", 0, 0
        for i in range(max(len(a), len(b))):
            val = carry
            if i < len(a):
                val += int(a[-(i + 1)])
            if i < len(b):
                val += int(b[-(i + 1)])
            carry, val = divmod(val, 2)
            result += str(val)
        if carry:
            result += str(carry)
        return result[::-1]

# V0''''
# IDEA : py default
class Solution:
    def addBinary(self, a, b) -> str:
        return '{0:b}'.format(int(a, 2) + int(b, 2))

# V0''''
# IDEA : Bit Manipulation
class Solution:
    def addBinary(self, a, b) -> str:
        x, y = int(a, 2), int(b, 2)
        while y:
            answer = x ^ y
            carry = (x & y) << 1
            x, y = answer, carry
        return bin(x)[2:]

# V0'''''
# class Solution(object):
#     # @param a, a string
#     # @param b, a string
#     # @return a string
#     def addBinary(self, a, b):
#         res = ''
#         i, j, plus = len(a)-1, len(b)-1, 0
#         while i or j:
#             val = plus
#             if i:
#                 val += int(a[i])
#                 i -= 1 
#             if j:
#                 val += int(b[j])
#                 j -= 1 
#             plus, val = divmod(val, 2)
#             res = str(val) + res
#         if plus:
#             res += str(plus)
#         return res[::-1]

# V1
# IDEA : py default
# https://leetcode.com/problems/add-binary/solution/
class Solution:
    def addBinary(self, a, b) -> str:
        return '{0:b}'.format(int(a, 2) + int(b, 2))

# V1'
# IDEA : Bit-by-Bit Computation
# https://leetcode.com/problems/add-binary/solution/
class Solution:
    def addBinary(self, a, b) -> str:
        n = max(len(a), len(b))
        a, b = a.zfill(n), b.zfill(n)
        
        carry = 0
        answer = []
        for i in range(n - 1, -1, -1):
            if a[i] == '1':
                carry += 1
            if b[i] == '1':
                carry += 1
                
            if carry % 2 == 1:
                answer.append('1')
            else:
                answer.append('0')
            
            carry //= 2
        
        if carry == 1:
            answer.append('1')
        answer.reverse()
        
        return ''.join(answer)

# V1''
# IDEA : Bit Manipulation
# https://leetcode.com/problems/add-binary/solution/
class Solution:
    def addBinary(self, a, b) -> str:
        x, y = int(a, 2), int(b, 2)
        while y:
            answer = x ^ y
            carry = (x & y) << 1
            x, y = answer, carry
        return bin(x)[2:]

# V1'''
# IDEA : Bit Manipulation V2
# https://leetcode.com/problems/add-binary/solution/
class Solution:
    def addBinary(self, a, b) -> str:
        x, y = int(a, 2), int(b, 2)
        while y:
            x, y = x ^ y, (x & y) << 1
        return bin(x)[2:]

# V1''''
# https://blog.csdn.net/coder_orz/article/details/51706532
# IDEA : syntax of int() func
# int(x=0, base=10), 
# base : Base of the number in x. Can be 0 (code literal) or 2-36.
# https://www.programiz.com/python-programming/methods/built-in/int
# DEMO 
# In [16]: a='11'

# In [17]: b='1'

# In [18]: bin(int(a, 2) + int(b, 2))[2:]
# Out[18]: '100'

# In [19]: bin(int(a, 2) + int(b, 2))
# Out[19]: '0b100'
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        return bin(int(a, 2) + int(b, 2))[2:]

# V1''''''
# https://blog.csdn.net/coder_orz/article/details/51706532
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        res = ''
        i, j, plus = len(a)-1, len(b)-1, 0
        while i>=0 or j>=0 or plus==1:
            plus += int(a[i]) if i>= 0 else 0
            plus += int(b[j]) if j>= 0 else 0
            res = str(plus % 2) + res
            i, j, plus = i-1, j-1, plus//2
        return res

# V1'''''''
# https://blog.csdn.net/coder_orz/article/details/51706532
# IDEA : RECURSION 
class Solution(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        if not a or not b:
            return a if a else b
        if a[-1] == '1' and b[-1] == '1':
            return self.addBinary(self.addBinary(a[:-1], b[:-1]), '1') + '0'
        elif a[-1] == '0' and b[-1] == '0':
            return self.addBinary(a[:-1], b[:-1]) + '0'
        else:
            return self.addBinary(a[:-1], b[:-1]) + '1'

# V1''''''''
# https://www.jiuzhang.com/solution/add-binary/#tag-highlight-lang-python
class Solution:
    # @param {string} a a number
    # @param {string} b a number
    # @return {string} the result
    def addBinary(self, a, b):
        indexa = len(a) - 1
        indexb = len(b) - 1
        carry = 0
        sum = ""
        while indexa >= 0 or indexb >= 0:
            x = int(a[indexa]) if indexa >= 0 else 0
            y = int(b[indexb]) if indexb >= 0 else 0
            if (x + y + carry) % 2 == 0:
                sum = '0' + sum
            else:
                sum = '1' + sum
            carry = (x + y + carry) / 2
            indexa, indexb = indexa - 1, indexb - 1
        if carry == 1:
            sum = '1' + sum
        return sum

# V2 
class Solution:
    def addBinary(self, a, b):
        len_ =  max(len(a),len(b))
        a_ = [int(x) for x in a ]
        b_ = [int(x) for x in b ]
        a_ = [0]*(len_ - len(a)) + a_ 
        b_ = [0]*(len_ - len(b)) + b_ 
        sum_  = 0
        extra = 0
        output=[]
        for i in reversed(list(range(len(a_)))):
            sum_ = a_[i]+b_[i]+extra
            if sum_ >= 2:
                sum_ = sum_ -2 
                output=[sum_] + output
                extra=1 
            else:
                output=[sum_] + output
                extra=0
        if extra==1:
        # in case : a = 100, b = 100 
            output=[1] + output
        return ''.join( str(i) for i in output )

# V3  
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param a, a string
    # @param b, a string
    # @return a string
    def addBinary(self, a, b):
        result, carry, val = "", 0, 0
        for i in range(max(len(a), len(b))):
            val = carry
            if i < len(a):
                val += int(a[-(i + 1)])
            if i < len(b):
                val += int(b[-(i + 1)])
            carry, val = divmod(val, 2)
            result += str(val)
        if carry:
            result += str(carry)
        return result[::-1]

# Time:  O(n)
# Space: O(1)
from itertools import izip_longest
class Solution2(object):
    def addBinary(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        result = ""
        carry = 0
        for x, y in izip_longest(reversed(a), reversed(b), fillvalue="0"):
            carry, remainder = divmod(int(x)+int(y)+carry, 2)
            result += str(remainder)
        
        if carry:
            result += str(carry)
        
        return result[::-1]

# V4 
class Solution:
    # @param a, a string
    # @param b, a string
    # @return a string
    def addBinary(self, a, b):
        result, carry, val = "", 0, 0
        for i in range(max(len(a), len(b))):
            val = carry
            if i < len(a):
                val += int(a[-(i + 1)])
            if i < len(b):
                val += int(b[-(i + 1)])
            carry, val = val / 2, val % 2
            result += str(val)
        if carry:
            result += str(carry)
        return result[::-1]