# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079495
# IDEA :  LINEAR SCAN 
class Solution(object):
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        binary = bin(N)[2:]
        dists = [0] * len(binary)
        left = 0
        for i, b in enumerate(binary):
            if b == '1':
                dists[i] = i - left
                left = i
        return max(dists)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079495
class Solution:
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        nbins = bin(N)[2:]
        index = -1
        res = 0
        for i, b in enumerate(nbins):
            if b == "1":
                if index != -1:
                    res = max(res, i - index)
                index = i
        return res

# V2 
# Time:  O(logn) = O(1) due to n is a 32-bit number
# Space: O(1)
class Solution(object):
    def binaryGap(self, N):
        """
        :type N: int
        :rtype: int
        """
        result = 0
        last = None
        for i in range(32):
            if (N >> i) & 1:
                if last is not None:
                    result = max(result, i-last)
                last = i
        return result