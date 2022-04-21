"""

Given a string s, return the longest palindromic substring in s.

 
Example 1:

Input: s = "babad"
Output: "bab"
Note: "aba" is also a valid answer.

Example 2:

Input: s = "cbbd"
Output: "bb"

Example 3:

Input: s = "a"
Output: "a"

Example 4:

Input: s = "ac"
Output: "a"
 

Constraints:

1 <= s.length <= 1000
s consist of only digits and English letters (lower-case and/or upper-case),

"""

# V0
# IDEA : TWO POINTERS
# -> DEAL WITH odd, even len cases
#  -> step 1) for loop on idx 
#  -> step 2) and start from "center" 
#  -> step 3) and do a while loop
#  -> step 4) check if len of sub str > 1
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025355/Easy-to-understand-solution-with-O(n2)-time-complexity
# Time complexity = best case O(n) to worse case O(n^2)
# Space complexity = O(1) if not considering the space complexity for result, as all the comparison happens in place.
class Solution:
    # The logic I have used is very simple, iterate over each character in the array and assming that its the center of a palindrome step in either direction to see how far you can go by keeping the property of palindrome true. The trick is that the palindrome can be of odd or even length and in each case the center will be different.
    # For odd length palindrome i am considering the index being iterating on is the center, thereby also catching the scenario of a palindrome with a length of 1.
    # For even length palindrome I am considering the index being iterating over and the next element on the left is the center.
    def longestPalindrome(self, s):

        if len(s) <= 1:
            return s

        res = []

        for idx in range(len(s)):
        
            """
            # CASE 1) : odd len
            # Check for odd length palindrome with idx at its center

            -> NOTE : the only difference (between odd, even len)
            
            -> NOTE !!!  : 2 idx : left = right = idx
            """
            left = right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1
              
            """"
            # CASE 2) : even len  
            # Check for even length palindrome with idx and idx-1 as its center

            -> NOTE : the only difference (between odd, even len)

            -> NOTE !!!  : 2 idx : left = idx - 1,  right = idx
            """
            left = idx - 1
            right = idx
            # note the condition !!!
            while left >= 0 and right < len(s) and s[left] == s[right]:
                if right - left + 1 > len(res):
                    res = s[left:right + 1]
                left -= 1
                right += 1

        return res

# V0'
# IDEA : TWO POINTER + RECURSION
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1057629/Python.-Super-simple-and-easy-understanding-solution.-O(n2).
class Solution:
    def longestPalindrome(self, s):
        res = ""
        length = len(s)
        def helper(left, right):
            while left >= 0 and right < length and s[left] == s[right]:
                left -= 1
                right += 1      
            return s[left + 1 : right]
        
        for index in range(len(s)):
            res = max(helper(index, index), helper(index, index + 1), res, key = len)           
        return res

# V0''
# IDEA : TWO POINTERS
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1025496/Python-Clean-and-Simple
class Solution:
    def longestPalindrome(self, s):
        left = 0
        size = 1
        for i in range(len(s)):
            odd = s[i-size-1:i+1]
            even = s[i-size:i+1]
            if 0 <= i-size-1 and odd == odd[::-1]:
                left = i-size-1
                size += 2
            elif 0 <= i-size and even == even[::-1]:
                left = i-size
                size += 1
        return s[left:left+size]

# V0'''
# IDEA : DP
# https://leetcode.com/problems/longest-palindromic-substring/discuss/1194142/Super-Clean-DP-Python-Solution
class Solution:
    def longestPalindrome(self, s):
            dp = [[0]*len(s) for _ in range(len(s))]   

            longest = ""
            for i in range(len(s)):
                for j in range(len(s) - i):
                    if (i == 0) or (i == 1 and s[j] == s[j+i]) or (s[j] == s[j+i] and dp[i-2][j+1]):
                        dp[i][j] = 1
                        longest = s[j:j+i+1]
            return longest

# V0'''
# IDEA : BRUTE FORCE (TIME OUT ERROR)
# brute force
class Solution(object):
    def longestPalindrome(self, s):
        def check(_str):
            return _str == _str[::-1]
        if len(s) == 0:
            return ""
        res = ""
        tmp = ""
        for i in range(len(s)):
            for j in range(i+1, len(s)+1):
                tmp = s[i:j]
                print ("tmp = " + str(tmp) + " check(tmp) = " + str(check(tmp)) )
                if check(tmp):
                    res = tmp if len(tmp) > len(res) else res
        return res

# V1
# IDEA : LOOPING ON "MIDDLE"
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""
            
        longest = ""
        for middle in range(len(s)):
            sub = self.find_palindrome_from(s, middle, middle)
            if len(sub) > len(longest):
                longest = sub
            sub = self.find_palindrome_from(s, middle, middle + 1)
            if len(sub) > len(longest):
                longest = sub

        return longest
        
    def find_palindrome_from(self, string, left, right):
        while left >= 0 and right < len(string) and string[left] == string[right]:
            left -= 1
            right += 1    
        return string[left + 1:right]

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79573621
# https://blog.csdn.net/qqxx6661/article/details/76864410
# IDEA : DP 
# STATUS EQUATION :
# dp[i, j] = 1                                        if i == j
#          = s[i] == s[j]                             if j = i + 1
#          = s[i] == s[j] && dp[i + 1][j - 1]         if j > i + 1      
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        if len(set(s)) == 1: return s
        n = len(s)
        start, end, maxL = 0, 0, 0
        dp = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(i):
                dp[j][i] = (s[j] == s[i]) & ((i - j < 2) | dp[j + 1][i - 1])
                if dp[j][i] and maxL < i - j + 1:
                    maxL = i - j + 1
                    start = j
                    end = i
            dp[i][i] = 1
        return s[start : end + 1]

# V1'
# https://www.jiuzhang.com/solution/longest-palindromic-substring/#tag-highlight-lang-python
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""
            
        n = len(s)
        is_palindrome = [[False] * n for _ in range(n)]
        
        for i in range(n):
            is_palindrome[i][i] = True
        for i in range(1, n):
            is_palindrome[i][i - 1] = True
            
        longest, start, end = 1, 0, 0
        for length in range(1, n):
            for i in range(n - length):
                j = i + length
                is_palindrome[i][j] = s[i] == s[j] and is_palindrome[i + 1][j - 1]
                if is_palindrome[i][j] and length + 1 > longest:
                    longest = length + 1
                    start, end = i, j
                    
        return s[start:end + 1]

# V1''
# https://www.jiuzhang.com/solution/longest-palindromic-substring/#tag-highlight-lang-python
class Solution:
    """
    @param s: input string
    @return: the longest palindromic substring
    """
    def longestPalindrome(self, s):
        if not s:
            return ""
            
        longest = ""
        for middle in range(len(s)):
            sub = self.find_palindrome_from(s, middle, middle)
            if len(sub) > len(longest):
                longest = sub
            sub = self.find_palindrome_from(s, middle, middle + 1)
            if len(sub) > len(longest):
                longest = sub

        return longest
        
    def find_palindrome_from(self, string, left, right):
        while left >= 0 and right < len(string) and string[left] == string[right]:
            left -= 1
            right += 1    
        return string[left + 1:right]

# V2
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        def preProcess(s):
            if not s:
                return ['^', '$']
            T = ['^']
            for c in s:
                T +=  ['#', c]
            T += ['#', '$']
            return T

        T = preProcess(s)
        P = [0] * len(T)
        center, right = 0, 0
        for i in range(1, len(T) - 1):
            i_mirror = 2 * center - i
            if right > i:
                P[i] = min(right - i, P[i_mirror])
            else:
                P[i] = 0

            while T[i + 1 + P[i]] == T[i - 1 - P[i]]:
                P[i] += 1

            if i + P[i] > right:
                center, right = i, i + P[i]

        max_i = 0
        for i in range(1, len(T) - 1):
            if P[i] > P[max_i]:
                max_i = i
        start = (max_i - 1 - P[max_i]) / 2
        return s[start : start + P[max_i]]
