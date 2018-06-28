"""


Given two binary strings, return their sum (also a binary string).

The input strings are both non-empty and contains only characters 1 or 0.

Example 1:

Input: a = "11", b = "1"
Output: "100"
Example 2:

Input: a = "1010", b = "1011"
Output: "10101"


"""


# V1 : dev 
    
"""
class Solution:
    def addBinary(self, a, b):
        output = []
        str_a = str(a) 
        str_b = str(b) 
        len_ = max(len(str_a), len(str_b))
        str_a_ = '0'*(len_ - len(str_a))  + str(a)
        str_b_ = '0'*(len_ - len(str_b))  + str(b)
        for i in range(len(str_a_)):
            if int(str_a_[i]) + int(str_a_[i]) <=1:
                str_a_[i] = str(int(str_a_[i]) + int(str_a_[i]))
            else:
                 str_a_[i], str_a_[i-1] = str(0),  str(int(str_a_[i-1]) +1 )
        return str_a_, str_b_

"""



# V2 

try:
    xrange          # Python 2
except NameError:
    xrange = range  # Python 3


class Solution:
    # @param a, a string
    # @param b, a string
    # @return a string
    def addBinary(self, a, b):
        result, carry, val = "", 0, 0
        for i in xrange(max(len(a), len(b))):
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

