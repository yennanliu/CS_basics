# Time:  O(n)
# Soace: O(1)
# Given a non-empty string s, you may delete at most one character.
# Judge whether you can make it a palindrome.
#
# Example 1:
# Input: "aba"
# Output: True
# Example 2:
# Input: "abca"
# Output: True
# Explanation: You could delete the character 'c'.
# Note:
# The string will only contain lowercase characters a-z. The maximum length of the string is 50000.

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79252936
# The isalnum() returns:
# True if all characters in the string are alphanumeric
# False if at least one character is not alphanumeric
# https://www.programiz.com/python-programming/methods/string/isalnum
class Solution(object):
    def isPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        isValid = lambda x : x == x[::-1]
        string = ''.join([x for x in s.lower() if x.isalnum()])
        return isValid(string)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79252936
# IDEA : REGULAR EXPRESSION 
class Solution(object):
    def isPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        s = s.lower()
        s = re.sub("\W", "", s)
        N = len(s)
        left, right = 0, N - 1
        while left <= right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79252936
# IDEA : TWO POINTERS (DEV)

# V1'''
# http://bookshadow.com/weblog/2017/09/17/leetcode-valid-palindrome-ii/
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        isPalindrome = lambda s: s == s[::-1]
        strPart = lambda s, x: s[:x] + s[x + 1:]
        size = len(s)
        lo, hi = 0, size - 1
        while lo < hi:
            if s[lo] != s[hi]:
                return validPalindrome(strPart(s, lo)) or validPalindrome(strPart(s, hi))
            lo += 1
            hi -= 1
        return True
        
# V2 
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        def validPalindrome(s, left, right):
            while left < right:
                if s[left] != s[right]:
                    return False
                left, right = left+1, right-1
            return True

        left, right = 0, len(s)-1
        while left < right:
            if s[left] != s[right]:
                return validPalindrome(s, left, right-1) or validPalindrome(s, left+1, right)
            left, right = left+1, right-1
        return True

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param s, a string
    # @return a boolean
    def isPalindrome(self, s):
        i, j = 0, len(s) - 1
        while i < j:
            while i < j and not s[i].isalnum():
                i += 1
            while i < j and not s[j].isalnum():
                j -= 1
            if s[i].lower() != s[j].lower():
                return False
            i, j = i + 1, j - 1
        return True

# V4 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        def validPalindrome(s, left, right):
            while left < right:
                if s[left] != s[right]:
                    return False
                left, right = left+1, right-1
            return True

        left, right = 0, len(s)-1
        while left < right:
            if s[left] != s[right]:
                return validPalindrome(s, left, right-1) or validPalindrome(s, left+1, right)
            left, right = left+1, right-1
        return True
        