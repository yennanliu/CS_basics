# V0

# V1
# https://www.jiuzhang.com/solution/longest-palindromic-subsequence/#tag-highlight-lang-python
# IDEA : DP
# DP EQUATION
# dp[i][j]=dp[i+1][j−1]+2if(str[i]==str[j])
# dp[i][j]=max(dp[i+1][j],dp[i][j-1])\quad if(str[i]!=str[j])dp[i][j]=max(dp[i+1][j],dp[i][j−1])if(str[i]!=str[j])
class Solution:
    # @param {string} s the maximum length of s is 1000
    # @return {int} the longest palindromic subsequence's length
    def longestPalindromeSubseq(self, s):
        # Write your code here
        length = len(s)
        if length == 0:
            return 0
        dp = [[0 for _ in range(length)] for __ in range(length)]
        for i in range(length - 1, -1, -1):
            dp[i][i] = 1
            for j in xrange(i + 1, length):
                if s[i] == s[j]:
                    dp[i][j] = dp[i + 1][j - 1] + 2
                else:
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
        return dp[0][length - 1]

# V1'
# http://bookshadow.com/weblog/2017/02/12/leetcode-longest-palindromic-subsequence/
# JAVA
# IDEA : DP
# DP EQUATION : 
# dp[i][j] = dp[i + 1][j - 1] + 2           if s[i] == s[j]
# dp[i][j] = max(dp[i][j - 1], dp[i + 1][j])    otherwise
# public class Solution {
#     public int longestPalindromeSubseq(String s) {
#         int size = s.length();
#         int[][] dp = new int[size][size];
#         for (int i = size - 1; i >= 0; i--) {
#             dp[i][i] = 1;
#             for (int j = i + 1; j < size; j++) {
#                 if (s.charAt(i) == s.charAt(j)) {
#                     dp[i][j] = dp[i + 1][j - 1] + 2;
#                 } else {
#                     dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
#                 }
#             }
#         }
#         return dp[0][size - 1];
#     }
# }

# V1''
# http://bookshadow.com/weblog/2017/02/12/leetcode-longest-palindromic-subsequence/
# JAVA
# IDEA : DP 
# public class Solution {
#     public int longestPalindromeSubseq(String s) {
#         int size = s.length();
#         int[][] dp = new int[size + 1][size + 1];
#         for (int i = 1; i <= size; i++) {
#             for (int j = 1; j <= size; j++) {
#                 if (s.charAt(i - 1) == s.charAt(size - j)) {
#                     dp[i][j] = dp[i - 1][j - 1] + 1;
#                 } else {
#                     dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
#                 }
#             }
#         }
#         int ans = s.length() > 0 ? 1 : 0;
#         for (int m = 0; m < size; m++) {
#             ans = Math.max(dp[m][size - m] * 2, ans);
#             if (m > 0) ans = Math.max(dp[m - 1][size - m] * 2 + 1, ans);
#         }
#         return ans;
#     }
# }

# V2
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def longestPalindromeSubseq(self, s):
        """
        :type s: str
        :rtype: int
        """
        if s == s[::-1]:  # optional, to optimize special case
            return len(s)

        dp = [[1] * len(s) for _ in range(2)]
        for i in reversed(range(len(s))):
            for j in range(i+1, len(s)):
                if s[i] == s[j]:
                    dp[i%2][j] = 2 + dp[(i+1)%2][j-1] if i+1 <= j-1 else 2
                else:
                    dp[i%2][j] = max(dp[(i+1)%2][j], dp[i%2][j-1])
        return dp[0][-1]
