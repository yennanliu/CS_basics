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


 
# V1  : dev 

"""
  
class Solution(object):
    def validPalindrome(self, s):
        left =  s[0:int((len(s)-1) /2)]
        right = s[ - int((len(s)-1)/2 ) ]
        print (left, right)  
"""

"""

        
class Solution(object):
    def validPalindrome(self, s):
        s_ = [ i for i in s]
        for i in s_:
        # https://docs.python.org/2/tutorial/datastructures.html
            #del s[i]
            pass 
        
        left =  s[0:int((len(s)-1) /2)]
        right = s[ - int((len(s)-1)/2 ) ]
        print (left, right)  
        


"""


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


        