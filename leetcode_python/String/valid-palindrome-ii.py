"""

680. Valid Palindrome II
Easy

Given a string s, return true if the s can be palindrome after deleting at most one character from it.

 

Example 1:

Input: s = "aba"
Output: true
Example 2:

Input: s = "abca"
Output: true
Explanation: You could delete the character 'c'.
Example 3:

Input: s = "abc"
Output: false
 

Constraints:

1 <= s.length <= 105
s consists of lowercase English letters.

"""

# V0 
class Solution:
    def validPalindrome(self, s):
        
        l, r = 0, len(s) - 1
        
        while l < r:
            if s[l] != s[r]:
                """
                # NOTE this !!!!
                -> break down the problem to even, odd cases
                """
                even, odd = s[l:r], s[l+1:r+1]
                # NOTE this !!!!
                return even == even[::-1] or odd == odd[::-1]
            else:
                l += 1
                r -= 1
                
        return True 

# V1
# IDEA : 2 pointers + array op
# https://leetcode.com/problems/valid-palindrome-ii/discuss/469677/JavaScript-and-Python-Solution
class Solution:
    def validPalindrome(self, s: str) -> bool:
        
        l, r = 0, len(s) - 1
        
        while l < r:
            if s[l] != s[r]:
                """
                # NOTE this !!!!
                -> break down the problem to even, odd cases
                """
                even, odd = s[l:r], s[l+1:r+1]
                # NOTE this !!!!
                return even == even[::-1] or odd == odd[::-1]
            else:
                l += 1
                r -= 1
                
        return True 

# V1'
# IDEA : 2 pointers + array op
# https://leetcode.com/problems/valid-palindrome-ii/discuss/107723/Super-Simple-Python-Solution
class Solution(object):
    def validPalindrome(self, s):
        i = 0
        j = len(s)-1
        while i <= j:
            if s[i] == s[j]:
                i += 1
                j -= 1
            else:
                return s[i:j] == s[i:j][::-1] or s[i+1:j+1] == s[i+1:j+1][::-1]
        return True

# V1''
# IDEA : 2 pointers + RECURSIVE
# https://leetcode.com/problems/valid-palindrome-ii/discuss/1123875/Python
class Solution:
    def validPalindrome(self, s: str) -> bool:
        isPalindrome = lambda x: x == x[:: -1]
        left, right = 0, len(s) - 1
        while left < right:
            if s[left] == s[right]:
                left += 1
                right -= 1
            else:
                return isPalindrome(s[left: right]) or isPalindrome(s[left + 1: right + 1])
        return True

# V1'''
# IDEA : Two Pointers
# https://leetcode.com/problems/valid-palindrome-ii/submissions/
class Solution:
    def validPalindrome(self, s: str) -> bool:
        def check_palindrome(s, i, j):
            while i < j:
                if s[i] != s[j]:
                    return False
                i += 1
                j -= 1
            
            return True

        i = 0
        j = len(s) - 1
        while i < j:
            # Found a mismatched pair - try both deletions
            if s[i] != s[j]:
                return check_palindrome(s, i, j - 1) or check_palindrome(s, i + 1, j)
            i += 1
            j -= 1
        
        return True

# V1''''
# https://leetcode.com/problems/valid-palindrome-ii/discuss/1410354/Recursive-Python
class Solution:
    def validPalindrome(self, s: str) -> bool:
        if not s:
            return True
        def isPalindrome(l, r, edit_used):
            while l <= r and s[l] == s[r]:
                l += 1
                r -= 1
            if l < r and not edit_used:
                return isPalindrome(l+1, r, True) or isPalindrome(l, r-1, True)
            elif l < r:
                return False
            else:
                return True
        
        return isPalindrome(0, len(s)-1, False)

# V1'''''
# https://leetcode.com/problems/valid-palindrome-ii/discuss/219079/Simple-Python-Solution
class Solution:
    def isValid(self, s):
        try:
            while s[-1] == s[0]:
                s.pop(-1)
                s.pop(0)
        except:
            pass
        return len(s) == 0
    def validPalindrome(self, s):
        s = list(s)
        if s[::-1] == s:
            return True
        while s[-1] == s[0]:
            s.pop(-1)
            s.pop(0)
        if len(s) > 1:
            return self.isValid(list(s)[1:]) or self.isValid(list(s)[:-1])
        return True

# V2