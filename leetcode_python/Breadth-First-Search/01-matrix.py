# V0 

# V1 
# http://bookshadow.com/weblog/2017/03/19/leetcode-01-matrix/
# IDEA : QUEUE 
class Solution(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        h, w = len(matrix), len(matrix[0])
        ans = [[0] * w for x in range(h)]
        queue = [(x, y) for x in range(h) for y in range(w) if matrix[x][y]]
        step = 0
        while queue:
            step += 1
            nqueue, mqueue = [], []
            for x, y in queue:
                zero = 0
                #for dx, dy in zip((1, 0, -1, 0), (0, 1, 0, -1)):
                for dx, dy in [[1,0], [0,1], [-1,0], [0,-1]]:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < h and 0 <= ny < w and matrix[nx][ny] == 0:
                        zero += 1
                if zero:
                    ans[x][y] = step
                    mqueue.append((x, y))
                else: 
                    nqueue.append((x, y))
            for x, y in mqueue:
                matrix[x][y] = 0
            queue = nqueue
        return ans

# V1'
# https://www.jiuzhang.com/solution/01-matrix/#tag-highlight-lang-python
class Solution:
    """
    @param matrix: a 0-1 matrix
    @return: return a matrix
    """
    def updateMatrix(self, matrix):
        # write your code here
        n, m = len(matrix), len(matrix[0])
        dp = [[100000 for j in range(m)] for i in range(n)]
        for i in range(n):
            for j in range(m):
                if (matrix[i][j] == 0):
                    dp[i][j] = 0
                if (i > 0):
                    dp[i][j] = min(dp[i][j], dp[i - 1][j] + 1)
                if (j > 0):
                    dp[i][j] = min(dp[i][j], dp[i][j - 1] + 1)
        for i in range(n - 1, -1, -1):
            for j in range(m - 1, -1, -1):
                if (dp[i][j] > 0):
                    if (i < n - 1):
                        dp[i][j] = min(dp[i][j], dp[i + 1][j] + 1)
                    if (j < m - 1):
                        dp[i][j] = min(dp[i][j], dp[i][j + 1] + 1)
        return dp

# V2 
# Time:  O(m * n)
# Space: O(m * n)
import collections
class Solution(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        queue = collections.deque()
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    queue.append((i, j))
                else:
                    matrix[i][j] = float("inf")

        dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        while queue:
            cell = queue.popleft()
            for dir in dirs:
                i, j = cell[0]+dir[0], cell[1]+dir[1]
                if not (0 <= i < len(matrix)) or not (0 <= j < len(matrix[0])) or \
                   matrix[i][j] <= matrix[cell[0]][cell[1]]+1:
                        continue
                queue.append((i, j))
                matrix[i][j] = matrix[cell[0]][cell[1]]+1

        return matrix


# Time:  O(m * n)
# Space: O(m * n)
# dp solution
class Solution2(object):
    def updateMatrix(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        dp = [[float("inf")]*len(matrix[0]) for _ in range(len(matrix))]
        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] == 0:
                    dp[i][j] = 0
                else:
                    if i > 0:
                        dp[i][j] = min(dp[i][j], dp[i-1][j]+1)
                    if j > 0:
                        dp[i][j] = min(dp[i][j], dp[i][j-1]+1)
        for i in reversed(range(len(matrix))):
            for j in reversed(range(len(matrix[i]))):
                if matrix[i][j] == 0:
                    dp[i][j] = 0
                else:
                    if i < len(matrix)-1:
                        dp[i][j] = min(dp[i][j], dp[i+1][j]+1)
                    if j < len(matrix[i])-1:
                        dp[i][j] = min(dp[i][j], dp[i][j+1]+1)
        return dp
