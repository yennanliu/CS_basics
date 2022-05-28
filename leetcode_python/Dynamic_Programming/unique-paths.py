"""

62. Unique Paths
Medium

There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.

Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the bottom-right corner.

The test cases are generated so that the answer will be less than or equal to 2 * 109.

 

Example 1:


Input: m = 3, n = 7
Output: 28
Example 2:

Input: m = 3, n = 2
Output: 3
Explanation: From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
1. Right -> Down -> Down
2. Down -> Down -> Right
3. Down -> Right -> Down
 

Constraints:

1 <= m, n <= 100

"""

# V0
# IDEA : BFS + dp (memory)
class Solution:
    def uniquePaths(self, m, n):

        # NOTE !!! we init paths as below
        paths = [[1]*n for _ in range(m)]
        
        q = deque()
        q.append((0,0))
        
        while q:
            row, col = q.popleft()
            
            if row == m or col == n or paths[row][col] > 1:
                continue 
            
            if row-1 >= 0 and col-1 >= 0:
                paths[row][col] = paths[row-1][col] + paths[row][col-1]
            
            q.append((row+1, col))
            q.append((row, col+1))
        
        return paths[-1][-1]

# V0'
# IDEA : DP
class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        d = [[1] * n for _ in range(m)]

        for col in range(1, m):
            for row in range(1, n):
                d[col][row] = d[col - 1][row] + d[col][row - 1]

        return d[m - 1][n - 1]

# V1
# IDEA : BFS + dp (memory)
# https://leetcode.com/problems/unique-paths/discuss/1145339/Pyhon-Beats-98-BFS
class Solution:
    def uniquePaths(self, m, n):

        # NOTE !!! we init paths as below
        paths = [[1]*n for _ in range(m)]
        
        q = deque()
        q.append((0,0))
        
        while q:
            row, col = q.popleft()
            
            if row == m or col == n or paths[row][col] > 1:
                continue 
            
            if row-1 >= 0 and col-1 >= 0:
                paths[row][col] = paths[row-1][col] + paths[row][col-1]
            
            q.append((row+1, col))
            q.append((row, col+1))
        
        return paths[-1][-1]

# V1'
# IDEA : DP
# https://leetcode.com/problems/unique-paths/solution/
class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        d = [[1] * n for _ in range(m)]

        for col in range(1, m):
            for row in range(1, n):
                d[col][row] = d[col - 1][row] + d[col][row - 1]

        return d[m - 1][n - 1]

# V1''
# IDEA : MATH
# https://leetcode.com/problems/unique-paths/solution/
from math import factorial
class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        return factorial(m + n - 2) // factorial(n - 1) // factorial(m - 1)

# V1'''
# IDEA : DP
# https://leetcode.com/problems/unique-paths/discuss/162333/Python-solution
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        table = [[0]*n for _ in range(m)]
        for i in range(n):
            table[0][i] = 1
        for j in range(m):
            table[j][0] = 1
        for i in range(1,m):
            for j in range(1,n):
                table[i][j] = table[i-1][j] + table[i][j-1]
        return table[m-1][n-1]

# V1'''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79337352
# IDEA : DP 
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[0] * n for _ in range(m)]
        for i in range(m):
            dp[i][0] = 1
        for i in range(n):
            dp[0][i] = 1
        for i in range(1, m):
            for j in range(1, n):
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j]
        return dp[m - 1][n - 1]

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79337352
# IDEA : DP 
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        dp = [[1] * n for _ in range(m)]
        for i in range(m):
            for j in range(n):
                if i == 0 or j == 0:
                    continue
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j]
        return dp[m - 1][n - 1]

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79337352
# IDEA : Permutations
class Solution(object):
    def uniquePaths(self, m, n):
        total = m + n - 2
        v = n - 1
        def permutation(m, n):
            son = 1
            for i in range(m, m - n, -1):
                son *= i
            mom = 1
            for i in range(n, 0, -1):
                mom *= i
            return son / mom
        return permutation(total, min(v, total -v))

# V1'''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79337352
# IDEA : MEMORY SEARCH
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        memo = [[0] * n for _ in range(m)]
        return self.dfs(m - 1, n - 1, memo)
        
    def dfs(self, m, n, memo):
        if m == 0 or n == 0:
            return 1
        if memo[m][n]:
            return memo[m][n]
        up = self.dfs(m - 1, n, memo)
        left = self.dfs(m, n - 1, memo)
        memo[m][n] = up + left
        return memo[m][n]

# V2 
# https://blog.csdn.net/Lu_gee/article/details/76938597
# DP status equation :
# matrix[i][j] = matrix[i - 1][j] + matrix[i][j - 1]
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
        matrix = [[0] * n] * m
        for i in range(m):
            for j in range(n):
                if j == 0 or i == 0:
                    matrix[i][j] = 1
                else:
                    matrix[i][j] = matrix[i - 1][j] + matrix[i][j - 1]
        return matrix[m - 1][n - 1]

# V3 
# Time:  O(m * n)
# Space: O(m + n)
class Solution(object):
    # @return an integer
    def uniquePaths(self, m, n):
        if m < n:
            return self.uniquePaths(n, m)
        ways = [1] * n
        for i in range(1, m):
            for j in range(1, n):
                ways[j] += ways[j - 1]
        return ways[n - 1]