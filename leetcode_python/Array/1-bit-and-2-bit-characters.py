# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79130681
class Solution(object):
    def isOneBitCharacter(self, bits):
        """
        :type bits: List[int]
        :rtype: bool
        """
        pos = 0
        while pos < len(bits) - 1:
            if bits[pos] == 1:
                pos += 2
            else:
                pos += 1
        return pos == len(bits) - 1 and bits[pos] == 0
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79130681
class Solution(object):
    def isOneBitCharacter(self, bits):
        """
        :type bits: List[int]
        :rtype: bool
        """
        pos = 0
        while pos < len(bits) - 1:
            pos += 2 if bits[pos] == 1 else 1
        return pos == len(bits) - 1

# V1''
# http://bookshadow.com/weblog/2017/10/29/leetcode-1-bit-and-2-bit-characters/
class Solution(object):
    def isOneBitCharacter(self, bits):
        """
        :type bits: List[int]
        :rtype: bool
        """
        size = len(bits)
        c = 0
        while c < size:
            if c == size - 1: return True
            if bits[c] == 0: c += 1
            else: c += 2
        return False

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isOneBitCharacter(self, bits):
        """
        :type bits: List[int]
        :rtype: bool
        """
        parity = 0
        for i in reversed(range(len(bits)-1)):
            if bits[i] == 0:
                break
            parity ^= bits[i]
        return parity == 0