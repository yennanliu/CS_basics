"""

1312. Minimum Insertion Steps to Make a String Palindrome
Hard

Given a string s. In one step you can insert any character at any index of the string.

Return the minimum number of steps to make s palindrome.

A Palindrome String is one that reads the same backward as well as forward.

 

Example 1:

Input: s = "zzazz"
Output: 0
Explanation: The string "zzazz" is already palindrome we don't need any insertions.
Example 2:

Input: s = "mbadm"
Output: 2
Explanation: String can be "mbdadbm" or "mdbabdm".
Example 3:

Input: s = "leetcode"
Output: 5
Explanation: Inserting 5 characters the string becomes "leetcodocteel".
 

Constraints:

1 <= s.length <= 500
s consists of lowercase English letters.

"""

# V0

# V1
# IDEA : DP
# https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/discuss/470706/JavaC%2B%2BPython-Longest-Common-Sequence
# IDEA :
# Intuition
# Split the string s into to two parts,
# and we try to make them symmetrical by adding letters.
#
# The more common symmetrical subsequence they have,
# the less letters we need to add.
#
# Now we change the problem to find the length of longest common sequence.
# This is a typical dynamic problem.
#
#
# Explanation
# Step1.
# Initialize dp[n+1][n+1],
# wheredp[i][j] means the length of longest common sequence between
# i first letters in s1 and j first letters in s2.
#
# Step2.
# Find the the longest common sequence between s1 and s2,
# where s1 = s and s2 = reversed(s)
#
# Step3.
# return n - dp[n][n]
class Solution(object):
     def minInsertions(self, s):
            n = len(s)
            dp = [[0] * (n + 1) for i in range(n + 1)]
            for i in xrange(n):
                for j in range(n):
                    dp[i + 1][j + 1] = dp[i][j] + 1 if s[i] == s[~j] else max(dp[i][j + 1], dp[i + 1][j])
            return n - dp[n][n]

# V1'
# IDEA : DP
# https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/discuss/476662/Python-Clean-DP
class Solution(object):
    def minInsertions(self, s):
        n = len(s)
        dp = [[0] * n for _ in range(n)]
        for j in range(n):
            for i in range(j-1,-1,-1):
                dp[i][j] = dp[i+1][j-1] if s[i] == s[j] else min(dp[i+1][j], dp[i][j-1]) + 1
        return dp[0][n-1]

# V1''
# IDEA : DP
# https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/discuss/1630626/Python-or-DP-or-Simplest-Solution
class Solution:
    def minInsertions(self, s: str, lookup={}) -> int:
        if len(s) <= 1:
            return 0
        if s not in lookup:
            if s[0] == s[-1]:
                lookup[s] = self.minInsertions(s[1:-1], lookup)
            else:
                lookup[s] = 1 + min(self.minInsertions(s[1:], lookup), self.minInsertions(s[:-1], lookup))
        return lookup[s]

# V1'''
# IDEA : DP
# https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/discuss/470733/Python-concise-dp-solution
class Solution:
    def minInsertions(self, s: str) -> int:
        N=len(s)
        dp=[[0]*(N+1) for _ in range(N+1)]
        for gap in range(N):
            for left in range(N-gap):
                right=left+gap
                if s[left]==s[right]:
                    dp[left][right]=dp[left+1][right-1]
                else:
                    dp[left][right]=min(dp[left+1][right],dp[left][right-1])+1
        return dp[0][N-1]

# V2