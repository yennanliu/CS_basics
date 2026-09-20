"""

44. Wildcard Matching
Hard

Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*' where:

'?' Matches any single character.
'*' Matches any sequence of characters (including the empty sequence).
The matching should cover the entire input string (not partial).

Example 1:

Input: s = "aa", p = "a"
Output: false
Explanation: "a" does not match the entire string "aa".

Example 2:

Input: s = "aa", p = "*"
Output: true
Explanation: '*' matches any sequence.

Example 3:

Input: s = "cb", p = "?a"
Output: false
Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.

Constraints:

0 <= s.length, p.length <= 2000
s contains only lowercase English letters.
p contains only lowercase English letters, '?' or '*'.

"""

# V0
# IDEA : 2D DP
#
#   dp[i][j] = True iff s[:i] is fully matched by p[:j]
#
#   Transitions:
#     p[j-1] == '*'  -> dp[i][j] = dp[i-1][j]   (* consumes one s-char)
#                                 | dp[i][j-1]   (* consumes nothing)
#     p[j-1] == '?' or p[j-1] == s[i-1]
#                    -> dp[i][j] = dp[i-1][j-1]  (both advance by 1)
#
#   Init: dp[0][0] = True; dp[0][j] = True iff p[:j] is all '*'s
#
#   e.g. s="aa", p="*"
#        dp[0][1]=True (p[0]='*' matches empty)
#        dp[1][1]=dp[0][1]=True, dp[2][1]=dp[1][1]=True -> True
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def isMatch(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: bool
        """
        m, n = len(s), len(p)
        dp = [[False] * (n + 1) for _ in range(m + 1)]

        # empty string matches empty pattern
        dp[0][0] = True

        # empty string can only be matched by a run of '*'s
        for j in range(1, n + 1):
            if p[j - 1] == '*':
                dp[0][j] = dp[0][j - 1]
            else:
                break

        for i in range(1, m + 1):
            for j in range(1, n + 1):


                """
                NOTE !!!

                the "*"  case, 2 cases

                -> 

                    1. match `0` char  (匹配 0 個 character)

                        - dp[i][j - 1]
                        - (* 完全不吃任何 character)



                    2. match `1+` char  (匹配 1 個以上 character)

                        - dp[i-1][j]
                        - (* 不能被消耗掉)


                ->

                            pattern
                                *
                                │
                        ┌───────┴───────┐
                        │               │
                     match 0         match 1+
                        │               │
                        ↓               ↓
                   dp[i][j-1]      dp[i-1][j]


                """
                if p[j - 1] == '*':
                    # NOTE !!! '*' can match zero chars (dp[i][j-1]) or one more char (dp[i-1][j])
                    """

                    dp[i][j] = dp[i][j - 1] or dp[i - 1][j]
                              ^^^^^^^^^^^    ^^^^^^^^^^^^^
                              0 chars        1+ chars

                    """
                    dp[i][j] = dp[i][j - 1] or dp[i - 1][j]
                

                elif p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                    dp[i][j] = dp[i - 1][j - 1]

        return dp[m][n]


# V1
# IDEA : GREEDY (two pointers)
#
#   Track the last '*' position in p and the s-position when it was seen.
#   If a mismatch occurs, backtrack: let '*' consume one more s-char.
#
# time = O(m * n) worst case, O(m + n) average, space = O(1)
class Solution2(object):
    def isMatch(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: bool
        """
        si = pi = 0
        star_pi = star_si = -1

        while si < len(s):
            if pi < len(p) and (p[pi] == s[si] or p[pi] == '?'):
                si += 1
                pi += 1
            elif pi < len(p) and p[pi] == '*':
                star_pi = pi
                star_si = si
                pi += 1
            elif star_pi != -1:
                # backtrack: let '*' eat one more s-char
                star_si += 1
                si = star_si
                pi = star_pi + 1
            else:
                return False

        # remaining pattern chars must all be '*'
        while pi < len(p) and p[pi] == '*':
            pi += 1

        return pi == len(p)
