"""

10. Regular Expression Matching
Hard

Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:

'.' Matches any single character.​​​​
'*' Matches zero or more of the preceding element.
The matching should cover the entire input string (not partial).

 

Example 1:

Input: s = "aa", p = "a"
Output: false
Explanation: "a" does not match the entire string "aa".
Example 2:

Input: s = "aa", p = "a*"
Output: true
Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
Example 3:

Input: s = "ab", p = ".*"
Output: true
Explanation: ".*" means "zero or more (*) of any character (.)".
 

Constraints:

1 <= s.length <= 20
1 <= p.length <= 30
s contains only lowercase English letters.
p contains only lowercase English letters, '.', and '*'.
It is guaranteed for each appearance of the character '*', there will be a previous valid character to match.

"""

# V0

# V1
# IDEA : RECURSIVE
# https://leetcode.com/problems/regular-expression-matching/solution/
class Solution(object):
    def isMatch(self, text, pattern):
        if not pattern:
            return not text

        first_match = bool(text) and pattern[0] in {text[0], '.'}

        if len(pattern) >= 2 and pattern[1] == '*':
            return (self.isMatch(text, pattern[2:]) or
                    first_match and self.isMatch(text[1:], pattern))
        else:
            return first_match and self.isMatch(text[1:], pattern[1:])

# V1'
# IDEA : DP (TOP - DOWN)
# https://leetcode.com/problems/regular-expression-matching/solution/
class Solution(object):
    def isMatch(self, text, pattern):
        memo = {}
        def dp(i, j):
            if (i, j) not in memo:
                if j == len(pattern):
                    ans = i == len(text)
                else:
                    first_match = i < len(text) and pattern[j] in {text[i], '.'}
                    if j+1 < len(pattern) and pattern[j+1] == '*':
                        ans = dp(i, j+2) or first_match and dp(i+1, j)
                    else:
                        ans = first_match and dp(i+1, j+1)

                memo[i, j] = ans
            return memo[i, j]

        return dp(0, 0)

# V1''
# IDEA : DP (BOTTOM UP)
# https://leetcode.com/problems/regular-expression-matching/solution/
class Solution(object):
    def isMatch(self, text, pattern):
        dp = [[False] * (len(pattern) + 1) for _ in range(len(text) + 1)]

        dp[-1][-1] = True
        for i in range(len(text), -1, -1):
            for j in range(len(pattern) - 1, -1, -1):
                first_match = i < len(text) and pattern[j] in {text[i], '.'}
                if j+1 < len(pattern) and pattern[j+1] == '*':
                    dp[i][j] = dp[i][j+2] or first_match and dp[i+1][j]
                else:
                    dp[i][j] = first_match and dp[i+1][j+1]

        return dp[0][0]

# V1'''
# IDEA : DP
# https://leetcode.com/problems/regular-expression-matching/discuss/239391/Python-solution
class Solution(object):
    def isMatch(self, s, p):
        n = len(s)
        m = len(p)
        dp = [[False]*(n+1) for _ in range(m+1)]
        dp[0][0] = True
        for i in range(m):
            for j in range(-1, n):
                if j == -1:
                    if p[i] == "*":
                        dp[i+1][j+1] = dp[i-1][j+1]
                    continue
                if p[i].isalpha():
                    if p[i] == s[j]:
                        dp[i+1][j+1] = dp[i][j]
                elif p[i] == ".":
                    dp[i+1][j+1] = dp[i][j]
                else:
                    dp[i+1][j+1] = dp[i-1][j+1] or (dp[i+1][j] and (p[i-1] == s[j] or p[i-1] == "."))
        return dp[-1][-1]

# V1''''
# IDEA : REGULAR EXPRESSION
# https://leetcode.com/problems/regular-expression-matching/discuss/126284/Python-one-liner-cheats
class Solution:
    def isMatch(self, s, p):
        return True if re.match(r"%s" %p, s) and re.match(r"%s" %p, s).group(0) == s else False

# V1'''''
# IDEA : REGULAR EXPRESSION
# https://leetcode.com/problems/regular-expression-matching/discuss/126284/Python-one-liner-cheats
class Solution:
    def isMatch(self, s, p):
        return s in re.findall(p, s)

# V1''''''
# IDEA : REGULAR EXPRESSION
# https://leetcode.com/problems/regular-expression-matching/discuss/126284/Python-one-liner-cheats
class Solution:
    def isMatch(self, s, p):
        return bool(re.match(r"%s" %p, s)) and re.match(r"%s" %p, s).group(0) == s 

# V1'''''''
# IDEA : DP
# https://leetcode.com/problems/regular-expression-matching/discuss/5677/Python-7-liner
class Solution(object):
    def isMatch(self, s, p):
        ls, lp = len(s), len(p)
        dp = [[False] * (ls+1) for _ in range(lp+1)]
        dp[0][0] = True
        for i in range(1, lp+1):
            for j in range(ls+1):
                dp[i][j] = ((i>1 and dp[i-2][j]) or dp[i-1][j] or dp[i][j] or (j>0 and dp[i][j-1] and (s[j-1]==p[i-2] or p[i-2]=='.'))) if p[i-1]=='*' else (j>0 and dp[i-1][j-1] and (p[i-1]==s[j-1] or p[i-1]=='.'))
        return dp[-1][-1]

# V2