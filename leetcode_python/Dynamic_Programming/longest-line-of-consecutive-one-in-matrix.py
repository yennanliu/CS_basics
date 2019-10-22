# V0


# V1
# http://bookshadow.com/weblog/2017/04/23/leetcode-longest-line-of-consecutive-one-in-matrix/
# IDEA : DP 
# DP EQUATION :
# h[x][y] = M[x][y] * (h[x - 1][y]  + 1)
# v[x][y] = M[x][y] * (v[x][y - 1]  + 1)
# d[x][y] = M[x][y] * (d[x - 1][y - 1]  + 1)
# a[x][y] = M[x][y] * (a[x + 1][y - 1]  + 1)
class Solution(object):
    def longestLine(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        h, w = len(M), len(M) and len(M[0]) or 0
        ans = 0
        #horizontal & diagonal
        diag = [[0] * w for r in range(h)]
        for x in range(h):
            cnt = 0
            for y in range(w):
                cnt = M[x][y] * (cnt + 1)
                diag[x][y] = M[x][y]
                if x > 0 and y > 0 and M[x][y] and diag[x - 1][y - 1]:
                    diag[x][y] += diag[x - 1][y - 1]
                ans = max(ans, cnt, diag[x][y])
        #vertical & anti-diagonal
        adiag = [[0] * w for r in range(h)]
        for x in range(w):
            cnt = 0
            for y in range(h):
                cnt = M[y][x] * (cnt + 1)
                adiag[y][x] = M[y][x]
                if y < h - 1 and x > 0 and M[y][x] and adiag[y + 1][x - 1]:
                    adiag[y][x] += adiag[y + 1][x - 1]
                ans = max(ans, cnt, adiag[y][x])
        return ans

# V1' 
# https://www.jiuzhang.com/solution/longest-line-of-consecutive-one-in-matrix/#tag-highlight-lang-python
class Solution:
    """
    @param M: the 01 matrix
    @return: the longest line of consecutive one in the matrix
    """
    def longestLine(self, M):
        if len(M) == 0:
            return 0
        n = len(M)
        m = len(M[0])
        ans = 0
        dp = [[[0]*4 for i in range(0, m)] for j in range(0, n)]
        for i in range(n):
            for j in range(m):
                if M[i][j] == 0:
                    continue
                for k in range(4):
                    dp[i][j][k] = 1
                if i - 1 >= 0:
                    dp[i][j][0] += dp[i - 1][j][0]
                if j - 1 >= 0:
                    dp[i][j][1] += dp[i][j - 1][1]
                if i - 1 >= 0 and j - 1 >= 0:
                    dp[i][j][2] += dp[i - 1][j - 1][2]
                if i - 1 >= 0 and j + 1 < m:
                    dp[i][j][3] += dp[i - 1][j + 1][3]
                ans = max(ans, max(dp[i][j][0], dp[i][j][1]))
                ans = max(ans, max(dp[i][j][2], dp[i][j][3]))
        return ans

# V2 
# Time:  O(m * n)
# Space: O(n)
class Solution(object):
    def longestLine(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        if not M: return 0
        result = 0
        dp = [[[0] * 4 for _ in range(len(M[0]))] for _ in range(2)]
        for i in range(len(M)):
            for j in range(len(M[0])):
                dp[i % 2][j][:] = [0] * 4
                if M[i][j] == 1:
                    dp[i % 2][j][0] = dp[i % 2][j - 1][0]+1 if j > 0 else 1
                    dp[i % 2][j][1] = dp[(i-1) % 2][j][1]+1 if i > 0 else 1
                    dp[i % 2][j][2] = dp[(i-1) % 2][j-1][2]+1 if (i > 0 and j > 0) else 1
                    dp[i % 2][j][3] = dp[(i-1) % 2][j+1][3]+1 if (i > 0 and j < len(M[0])-1) else 1
                    result = max(result, max(dp[i % 2][j]))
        return result