# V0 
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
