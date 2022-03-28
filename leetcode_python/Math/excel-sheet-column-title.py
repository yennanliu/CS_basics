"""

168. Excel Sheet Column Title
Easy

Given an integer columnNumber, return its corresponding column title as it appears in an Excel sheet.

For example:

A -> 1
B -> 2
C -> 3
...
Z -> 26
AA -> 27
AB -> 28 
...
 

Example 1:

Input: columnNumber = 1
Output: "A"
Example 2:

Input: columnNumber = 28
Output: "AB"
Example 3:

Input: columnNumber = 701
Output: "ZY"
 

Constraints:

1 <= columnNumber <= 231 - 1

"""

# V0
# https://www.jianshu.com/p/591d3a2ab45d
class Solution(object):
    def convertToTitle(self, n):
        tar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        res = ""
        while n > 0:
            # why (n-1) ? : idx start from 0
            m = (n-1) % 26
            #result += tar[m]
            res = (tar[m] + res)
            if m == 0:
                # why n=n+1 ? : since there is no 0 residual (m = (n-1) % 26), so we need to "pass" this case
                n = n + 1
            n = (n-1) // 26
        return res

# V0'
# TODO : fix below
# class Solution(object):
#     def convertToTitle(self, columnNumber):
#         alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
#         # edge case
#         if not columnNumber:
#             return
#         if columnNumber <= 25:
#             return alpha[columnNumber-1]
#         res = ""
#         while columnNumber > 0:
#             print ("columnNumber = " + str(columnNumber))
#             a, b = divmod(columnNumber, 26)
#             columnNumber = a
#             #res += str(alpha[a])
#             res += alpha[b-1]
#             if b == 0:
#                 return res[::-1]
#         print ("res = " + str(res))
#         return res[::-1]

# V0'
class Solution(object):
    def titleToNumber(self, s):
        """
        :type s: str
        :rtype: int
        """
        sum = 0
        for c in s:
            print ("sum = " + str(sum))
            sum = sum*26 + ord(c) - 64 # 64 = ord('A') - 1
        return sum

# V0''
# https://leetcode.com/problems/excel-sheet-column-title/discuss/205987/Python-Solution-with-explanation
class Solution:
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        d='0ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        res=''
        if n<=26:
            return d[n]
        else:
            while n > 0:
                n,r=divmod(n,26)
                # This is the catcha on this problem where when r==0 as a result of n%26. eg, n=52//26=2, r=52%26=0. 
                #To get 'AZ' as known for 52, n-=1 and r+=26. Same goes to 702.
                if r == 0:
                    n-=1
                    r+=26
                res = d[r] + res
        return res

# V0'''
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

# V1'
# https://leetcode.com/problems/excel-sheet-column-title/discuss/205987/Python-Solution-with-explanation
class Solution:
    def convertToTitle(self, n):
        """
        :type n: int
        :rtype: str
        """
        d='0ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        res=''
        if n<=26:
            return d[n]
        else:
            while n > 0:
                n,r=divmod(n,26)
                # This is the catcha on this problem where when r==0 as a result of n%26. eg, n=52//26=2, r=52%26=0. 
                #To get 'AZ' as known for 52, n-=1 and r+=26. Same goes to 702.
                if r == 0:
                    n-=1
                    r+=26
                res = d[r] + res
        return res

# V1''
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
        
# V1'''
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