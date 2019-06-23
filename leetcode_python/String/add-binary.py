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


# V1 :  
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



# V2  

# V3 

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








