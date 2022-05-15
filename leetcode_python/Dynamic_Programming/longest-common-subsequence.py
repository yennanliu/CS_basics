"""

1143. Longest Common Subsequence
Medium

Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common subsequence, return 0.

A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.

For example, "ace" is a subsequence of "abcde".
A common subsequence of two strings is a subsequence that is common to both strings.

 

Example 1:

Input: text1 = "abcde", text2 = "ace" 
Output: 3  
Explanation: The longest common subsequence is "ace" and its length is 3.
Example 2:

Input: text1 = "abc", text2 = "abc"
Output: 3
Explanation: The longest common subsequence is "abc" and its length is 3.
Example 3:

Input: text1 = "abc", text2 = "def"
Output: 0
Explanation: There is no such common subsequence, so the result is 0.
 

Constraints:

1 <= text1.length, text2.length <= 1000
text1 and text2 consist of only lowercase English characters.

"""

# V0

# V1
# IDEA : DP
# https://leetcode.com/problems/longest-common-subsequence/discuss/794472/Simple-python
class Solution:
      def longestCommonSubsequence(self, s1: str, s2: str) -> int:
            n1, n2 = len(s1), len(s2)
            dp = [[0] * n2 for _ in range(n1)]
            for i in range(n1):
                for j in range(n2):
                    if s1[i] == s2[j]:
                        dp[i][j] = 1 + (dp[i-1][j-1] if i > 0 and j > 0 else 0)
                    else:
                        dp[i][j] = max(dp[i][j-1] if j > 0 else 0, dp[i-1][j] if i > 0 else 0)
            return dp[-1][-1]

# V1'
# IDEA : Memoization
# https://leetcode.com/problems/longest-common-subsequence/discuss/598739/Memoization-Python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        self.cache = [[-1 for i in range(len(text2)+1)] for j in range(len(text1)+1)]
        def LCS(i,j):
            if i == len(text1) or j == len(text2):
                return 0
            if self.cache[i][j] != -1:
                return self.cache[i][j]
            if text1[i] == text2[j]:
                self.cache[i][j] =  1 + LCS(i+1,j+1)
            else:
                self.cache[i][j] = max(LCS(i+1,j),LCS(i,j+1))
            return self.cache[i][j]
        return LCS(0,0)

# V1''
# IDEA : 2D dynamic programming:
# https://leetcode.com/problems/longest-common-subsequence/discuss/598687/Python-O(-m*n-)-2D-DP.-85%2B-w-Hint
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        
        # padding one space for empty string representation
        text1 = ' ' + text1
        text2 = ' ' + text2

        w, h = len(text1), len(text2)

        dp_table = [ [ 0 for x in range(w) ] for y in range(h) ]

        # update dynamic programming table with optimal substructure
        for y in range(1, h):
            for x in range(1, w):

                if text1[x] == text2[y]:
                    # with the same character
                    # extend the length of common subsequence
                    dp_table[y][x] = dp_table[y-1][x-1] + 1
                
                else:
                    # with different characters
                    # choose the optimal subsequence
                    dp_table[y][x] = max( dp_table[y-1][x], dp_table[y][x-1] )

        return dp_table[-1][-1]

# V1'''
# IDEA : Memoization
# https://leetcode.com/problems/longest-common-subsequence/solution/
from functools import lru_cache
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        
        @lru_cache(maxsize=None)
        def memo_solve(p1, p2):
            
            # Base case: If either string is now empty, we can't match
            # up anymore characters.
            if p1 == len(text1) or p2 == len(text2):
                return 0
            
            # Option 1: We don't include text1[p1] in the solution.
            option_1 = memo_solve(p1 + 1, p2)
            
            # Option 2: We include text1[p1] in the solution, as long as
            # a match for it in text2 at or after p2 exists.
            first_occurence = text2.find(text1[p1], p2)
            option_2 = 0
            if first_occurence != -1:
                option_2 = 1 + memo_solve(p1 + 1, first_occurence + 1)
            
            # Return the best option.
            return max(option_1, option_2)
                
        return memo_solve(0, 0)

# V1'''''
# IDEA : Improved Memoization
# https://leetcode.com/problems/longest-common-subsequence/solution/
from functools import lru_cache
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        
        @lru_cache(maxsize=None)
        def memo_solve(p1, p2):
            
            # Base case: If either string is now empty, we can't match
            # up anymore characters.
            if p1 == len(text1) or p2 == len(text2):
                return 0
            
            # Recursive case 1.
            if text1[p1] == text2[p2]:
                return 1 + memo_solve(p1 + 1, p2 + 1)
            
            # Recursive case 2.
            else:
                return max(memo_solve(p1, p2 + 1), memo_solve(p1 + 1, p2))
            
        return memo_solve(0, 0)

# V1''''''
# IDEA : DP
# https://leetcode.com/problems/longest-common-subsequence/solution/
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        
        # Make a grid of 0's with len(text2) + 1 columns 
        # and len(text1) + 1 rows.
        dp_grid = [[0] * (len(text2) + 1) for _ in range(len(text1) + 1)]
        
        # Iterate up each column, starting from the last one.
        for col in reversed(range(len(text2))):
            for row in reversed(range(len(text1))):
                # If the corresponding characters for this cell are the same...
                if text2[col] == text1[row]:
                    dp_grid[row][col] = 1 + dp_grid[row + 1][col + 1]
                # Otherwise they must be different...
                else:
                    dp_grid[row][col] = max(dp_grid[row + 1][col], dp_grid[row][col + 1])
        
        # The original problem's answer is in dp_grid[0][0]. Return it.
        return dp_grid[0][0]

# V1'''''''
# IDEA : DP WITH SPACE OPTIMIZATION
# https://leetcode.com/problems/longest-common-subsequence/solution/
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        
        # If text1 doesn't reference the shortest string, swap them.
        if len(text2) < len(text1):
            text1, text2 = text2, text1
        
        
        # The previous column starts with all 0's and like before is 1
        # more than the length of the first word.
        previous = [0] * (len(text1) + 1)
        
        # Iterate up each column, starting from the last one.
        for col in reversed(range(len(text2))):
            # Create a new array to represent the current column.
            current = [0] * (len(text1) + 1)
            for row in reversed(range(len(text1))):
                if text2[col] == text1[row]:
                    current[row] = 1 + previous[row + 1]
                else:
                    current[row] = max(previous[row], current[row + 1])
            # The current column becomes the previous one.
            previous = current
        
        # The original problem's answer is in previous[0]. Return it.
        return previous[0]

# V1'''''''''
# IDEA : DP
# https://leetcode.com/problems/longest-common-subsequence/discuss/1496789/python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
            n=len(text1)
            m=len(text2)
            dp={}
            def helper(i,j):
                if i==n or j==m:
                    return 0
                if (i,j) in dp:
                    return dp[(i,j)]
                elif text1[i]==text2[j]:
                    temp=1+helper(i+1,j+1)
                    dp[(i,j)]=temp
                else:
                    temp=max(helper(i,j+1),helper(i+1,j))
                    dp[(i,j)]=temp
                return dp[(i,j)]
            return helper(0,0)

# V2