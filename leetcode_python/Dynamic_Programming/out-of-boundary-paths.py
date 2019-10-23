# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83447155
# http://bookshadow.com/weblog/2017/05/07/leetcode-out-of-boundary-paths/
# IDEA : DP 
class Solution(object):
    def findPaths(self, m, n, N, i, j):
        """
        :type m: int
        :type n: int
        :type N: int
        :type i: int
        :type j: int
        :rtype: int
        """
        dp = [[[0] * n for _ in range(m)] for _ in range(N + 1)]
        for s in range(1, N + 1):
            for x in range(m):
                for y in range(n):
                    v1 = 1 if x == 0 else dp[s - 1][x - 1][y]
                    v2 = 1 if x == m - 1 else dp[s - 1][x + 1][y]
                    v3 = 1 if y == 0 else dp[s - 1][x][y - 1]
                    v4 = 1 if y == n - 1 else dp[s - 1][x][y + 1]
                    dp[s][x][y] = (v1 + v2 + v3 + v4) % (10**9 + 7)
        return dp[N][i][j]

# V1'
# http://bookshadow.com/weblog/2017/05/07/leetcode-out-of-boundary-paths/
# IDEA : DP 
# DP EQUATION :
# dp[t + 1][x + dx][y + dy] += dp[t][x][y]
# t means the # of moves, dx, dy = [(1,0), (-1,0), (0,1), (0,-1)]
    def findPaths(self, m, n, N, i, j):
        """
        :type m: int
        :type n: int
        :type N: int
        :type i: int
        :type j: int
        :rtype: int
        """
        MOD = 10**9 + 7
        dz = zip((1, 0, -1, 0), (0, 1, 0, -1))
        dp = [[0] *n for x in range(m)]
        dp[i][j] = 1
        ans = 0
        for t in range(N):
            ndp = [[0] *n for x in range(m)]
            for x in range(m):
                for y in range(n):
                    for dx, dy in dz:
                        nx, ny = x + dx, y + dy
                        if 0 <= nx < m and 0 <= ny < n:
                            ndp[nx][ny] = (ndp[nx][ny] + dp[x][y]) % MOD
                        else:
                            ans = (ans + dp[x][y]) % MOD
            dp = ndp
        return ans

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83447155
# IDEA : BFS, DFS  

# V1'''
# https://www.jiuzhang.com/solution/out-of-boundary-paths/#tag-highlight-lang-python
class Solution:
    def findPaths(self, m: int, n: int, N: int, i: int, j: int) -> int:
        if 0 == N:
            return 0

        def dfs(N, step, rows, cols, y, x):
            if (y, x, step) in mem:
                return mem[(y, x, step)]
            if not (0 <= y <= rows-1 and 0 <= x <= cols-1):
                return 1
            if step == N:
                return 0
            res = 0
            for oy, ox in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                res += dfs(N, step+1, rows, cols, y+oy, x+ox)
            mem[(y, x, step)] = res
            return res

        mem = {}
        ret = dfs(N, 0, m, n, i, j)
        return ret % (10 ** 9 + 7)

# V1''
# https://www.jiuzhang.com/solution/out-of-boundary-paths/#tag-highlight-lang-python
class Solution:
       def findPaths(self, m, n, N, i, j):
        """
        :type m: int
        :type n: int
        :type N: int
        :type i: int
        :type j: int
        :rtype: int
        """
        MOD = 10**9 + 7
        dz = zip((1, 0, -1, 0), (0, 1, 0, -1))
        dp = [[0] *n for x in range(m)]
        dp[i][j] = 1
        ans = 0
        for t in range(N):
            ndp = [[0] *n for x in range(m)]
            for x in range(m):
                for y in range(n):
                    for dx, dy in dz:
                        nx, ny = x + dx, y + dy
                        if 0 <= nx < m and 0 <= ny < n:
                            ndp[nx][ny] = (ndp[nx][ny] + dp[x][y]) % MOD
                        else:
                            ans = (ans + dp[x][y]) % MOD
            dp = ndp
        return ans

# V2
# Time:  O(N * m * n)
# Space: O(m * n)
class Solution(object):
    def findPaths(self, m, n, N, x, y):
        """
        :type m: int
        :type n: int
        :type N: int
        :type x: int
        :type y: int
        :rtype: int
        """
        M = 1000000000 + 7
        dp = [[[0 for _ in range(n)] for _ in range(m)] for _ in range(2)]
        for moves in range(N):
            for i in range(m):
                for j in range(n):
                    dp[(moves + 1) % 2][i][j] = (((1 if (i == 0) else dp[moves % 2][i - 1][j]) + \
                                                  (1 if (i == m - 1) else dp[moves % 2][i + 1][j])) % M + \
                                                 ((1 if (j == 0) else dp[moves % 2][i][j - 1]) + \
                                                  (1 if (j == n - 1) else dp[moves % 2][i][j + 1])) % M) % M
        return dp[N % 2][x][y]
