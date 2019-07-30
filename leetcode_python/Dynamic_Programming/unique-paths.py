# V0 : DEV 


# V1'' 
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

# V1'''
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

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79337352
# IDEA : Permutations
class Solution(object):
    def uniquePaths(self, m, n):
        """
        :type m: int
        :type n: int
        :rtype: int
        """
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

# V1''''
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
