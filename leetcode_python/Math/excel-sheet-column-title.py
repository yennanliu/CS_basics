# V0
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        result = ""
        while n > 0:
            m = (n-1) % 26
            result += tar[m]
            if m == 0:
                n = n + 1
            n = (n-1) // 26
        return result[::-1]

# V1
# https://www.jianshu.com/p/591d3a2ab45d
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        result = ""
        while n > 0:
            m = (n-1) % 26
            result += tar[m]
            if m == 0:
                n = n + 1
            n = (n-1) // 26
        return result[::-1]

# V1
# https://www.cnblogs.com/loadofleaf/p/5093574.html
# python 2
class Solution(object):
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        res = ""
        while n:
            h = (n - 1) % 26
            res = chr(ord('A') + h) + res
            n = (n - 1) / 26
        return res
        
# V1''
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
