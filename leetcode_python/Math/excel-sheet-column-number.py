# V0


# V1
# https://blog.csdn.net/coder_orz/article/details/51406455
class Solution(object):
    # transform   26 Carry ->  10 Carry (Decimal)  
    def titleToNumber(self, s):
        """
        :type s: str
        :rtype: int
        """
        sum = 0
        for c in s:
            sum = sum*26 + ord(c) - 64 # 64 = ord('A') - 1
        return sum

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def titleToNumber(self, s):
        """
        :type s: str
        :rtype: int
        """
        result = 0
        for i in range(len(s)):
            result *= 26
            result += ord(s[i]) - ord('A') + 1
        return result