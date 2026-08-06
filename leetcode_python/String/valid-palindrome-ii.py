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
# IDEA: BRUTE FORCE + is_palindrome (gpt)
# time: O(N)
# space: O(N)
"""
Core idea:


Instead of trying every deletion, 
 ->  `ONLY delete one of the two mismatched characters `
     when the first mismatch occurs.
"""
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        l, r = 0, len(s) - 1

        while l < r:
            # NOTE !!!
            # -> ONLY call the helper func when `s[l] != s[r]`
            # -> otherwise, we keep moving (l+=1, r-=1)
            if s[l] != s[r]:
                return (self.is_palindrome(s, l + 1, r) or
                        self.is_palindrome(s, l, r - 1))
            l += 1
            r -= 1

        return True

    def is_palindrome(self, s, l, r):
        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1
        return True


# V0-0-0-1
# time = O(n)
# space = O(n)
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


# V0-1
# IDEA: 2 POINTERS (gemini)
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        l, r = 0, len(s) - 1
        
        while l < r:
            if s[l] != s[r]:

                # `Mismatch found!` We have one chance to delete a character.
                # Try deleting the `left` character (s[l+1 : r+1])
                skip_l = s[l+1 : r+1]
                
                # Try deleting the `right` character (s[l : r])
                skip_r = s[l : r]
                
                # Check if either of these remaining substrings is a palindrome.
                # [::-1] is the fast, Pythonic way to reverse a string.
                return skip_l == skip_l[::-1] or skip_r == skip_r[::-1]
            
            l += 1
            r -= 1
            
        return True


# V0-0-1
# IDEA: BRUTE FORCE + is_palindrome (TLE) (gpt)
# time: O(N ^ 2)
# space: O(N)
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        if self.is_palindrome(s):
            return True

        for i in range(len(s)):
            _s = s[:i] + s[i+1:]
            if self.is_palindrome(_s):
                return True

        return False

    def is_palindrome(self, s):
        l = 0
        r = len(s) - 1

        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1

        return True


# V0-0-2
# IDEA: BRUTE FORCE + is_palindrome (gpt)
# time: O(N)
# space: O(N)
"""
Core idea:


Instead of trying every deletion, 
 ->  `ONLY delete one of the two mismatched characters `
     when the first mismatch occurs.
"""
class Solution(object):
    def validPalindrome(self, s):
        """
        :type s: str
        :rtype: bool
        """
        l, r = 0, len(s) - 1

        while l < r:
            # NOTE !!!
            # -> ONLY call the helper func when `s[l] != s[r]`
            # -> otherwise, we keep moving (l+=1, r-=1)
            if s[l] != s[r]:
                return (self.is_palindrome(s, l + 1, r) or
                        self.is_palindrome(s, l, r - 1))
            l += 1
            r -= 1

        return True

    def is_palindrome(self, s, l, r):
        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1
        return True


# V1
# IDEA : 2 pointers + array op
# https://leetcode.com/problems/valid-palindrome-ii/discuss/469677/JavaScript-and-Python-Solution
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(n)
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
# time = O(n^2)
# space = O(n)
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