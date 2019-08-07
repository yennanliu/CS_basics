# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82468653
# IDEA : 2**k 
# << PYTHON OPERATOR 
# https://wiki.python.org/moin/BitwiseOperators
# x << y : Returns x with the bits shifted to the left by y places (and new bits on the right-hand-side are zeros). This is the same as multiplying x by 2**y.
# DEMO 
# In [17]: for i in range(11):
#     ...:     print ( 1 << i)
#     ...:     
# 1
# 2
# 4
# 8
# 16
# 32
# 64
# 128
# 256
# 512
# 1024
class Solution(object):
    def reorderedPowerOf2(self, N):
        """
        :type N: int
        :rtype: bool
        """
        c = collections.Counter(str(N))
        return any(c == collections.Counter(str(1 << i)) for i in range(32))

# V2
# Time:  O((logn)^2) = O(1) due to n is a 32-bit number
# Space: O(logn) = O(1)
import collections
class Solution(object):
    def reorderedPowerOf2(self, N):
        """
        :type N: int
        :rtype: bool
        """
        count = collections.Counter(str(N))
        return any(count == collections.Counter(str(1 << i)) for i in range(31))
