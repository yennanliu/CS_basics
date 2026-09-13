"""

717. 1-bit and 2-bit Characters
Easy

We have two special characters:

The first character can be represented by one bit 0.
The second character can be represented by two bits (10 or 11).

Given a binary array bits that ends with 0, return true if the last character must be a one-bit character.

Example 1:

Input: bits = [1,0,0]
Output: true
Explanation: The only way to decode it is two-bit character and one-bit character.
So the last character is one-bit character.

Example 2:

Input: bits = [1,1,1,0]
Output: false
Explanation: The only way to decode it is two-bit character and two-bit character.
So the last character is not one-bit character.

Constraints:

1 <= bits.length <= 1000
bits[i] is either 0 or 1.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79130681
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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