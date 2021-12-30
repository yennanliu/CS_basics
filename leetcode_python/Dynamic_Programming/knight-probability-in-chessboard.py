"""

688. Knight Probability in Chessboard
Medium

On an n x n chessboard, a knight starts at the cell (row, column) and attempts to make exactly k moves. The rows and columns are 0-indexed, so the top-left cell is (0, 0), and the bottom-right cell is (n - 1, n - 1).

A chess knight has eight possible moves it can make, as illustrated below. Each move is two cells in a cardinal direction, then one cell in an orthogonal direction.


Each time the knight is to move, it chooses one of eight possible moves uniformly at random (even if the piece would go off the chessboard) and moves there.

The knight continues moving until it has made exactly k moves or has moved off the chessboard.

Return the probability that the knight remains on the board after it has stopped moving.

 

Example 1:

Input: n = 3, k = 2, row = 0, column = 0
Output: 0.06250
Explanation: There are two moves (to (1,2), (2,1)) that will keep the knight on the board.
From each of those positions, there are also two moves that will keep the knight on the board.
The total probability the knight stays on the board is 0.0625.
Example 2:

Input: n = 1, k = 0, row = 0, column = 0
Output: 1.00000
 

Constraints:

1 <= n <= 25
0 <= k <= 100
0 <= row, column <= n

"""

# V0
# IDEA : DP
class Solution(object):
    def knightProbability(self, N, K, r, c):
        dp = [[0 for i in range(N)] for j in range(N)]
        dp[r][c] = 1
        directions = [(1, 2), (1, -2), (2, 1), (2, -1), (-2, 1), (-2, -1), (-1, 2), (-1, -2)]
        for k in range(K):
            new_dp = [[0 for i in range(N)] for j in range(N)]
            for i in range(N):
                for j in range(N):
                    for d in directions:
                        x, y = i + d[0], j + d[1]
                        if x < 0 or x >= N or y < 0 or y >= N:
                            continue
                        new_dp[i][j] += dp[x][y]
            dp = new_dp
        return sum(map(sum, dp)) / float(8 ** K)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82747623
# IDEA : DP
# DP (dp[i][j]) hear means "how many times the horse can get to this place (i,j) in this round"
class Solution(object):
    def knightProbability(self, N, K, r, c):
        """
        :type N: int
        :type K: int
        :type r: int
        :type c: int
        :rtype: float
        """
        dp = [[0 for i in range(N)] for j in range(N)]
        dp[r][c] = 1
        directions = [(1, 2), (1, -2), (2, 1), (2, -1), (-2, 1), (-2, -1), (-1, 2), (-1, -2)]
        for k in range(K):
            new_dp = [[0 for i in range(N)] for j in range(N)]
            for i in range(N):
                for j in range(N):
                    for d in directions:
                        x, y = i + d[0], j + d[1]
                        if x < 0 or x >= N or y < 0 or y >= N:
                            continue
                        new_dp[i][j] += dp[x][y]
            dp = new_dp
        return sum(map(sum, dp)) / float(8 ** K)
        
# V1'
# https://www.jiuzhang.com/solution/knight-probability-in-chessboard/#tag-highlight-lang-python
class Solution:
    """
    @param N: int
    @param K: int
    @param r: int
    @param c: int
    @return: the probability
    """
    def knightProbability(self, N, K, r, c):
        # Write your code here.
        next = [[-1, -2], [1, -2], [2, -1], [2, 1], [1, 2], [-1, 2], [-2, 1], [-2, -1]]
        dp = [[0 for i in range(N)] for j in range(N)]
        dp[r][c] = 1
        for step in range(1, K + 1):
            dpTemp = [[0 for i in range(N)] for j in range(N)]
            for i in range(N):
                for j in range(N):
                    for direction in next:
                        lastR, lastC = i - direction[0], j - direction[1]
                        if all([lastC >= 0, lastR >= 0, lastC < N, lastR < N]):
                            dpTemp[i][j] += dp[lastR][lastC] * 0.125
            dp = dpTemp
        res = 0.0
        for i in range(N):
            for j in range(N):
                res += dp[i][j]
        return res

# V2
# Time:  O(k * n^2)
# Space: O(n^2)
class Solution(object):
    def knightProbability(self, N, K, r, c):
        """
        :type N: int
        :type K: int
        :type r: int
        :type c: int
        :rtype: float
        """
        directions = \
            [[ 1, 2], [ 1, -2], [ 2, 1], [ 2, -1], \
             [-1, 2], [-1, -2], [-2, 1], [-2, -1]]
        dp = [[[1 for _ in range(N)] for _ in range(N)] for _ in range(2)]
        for step in range(1, K+1):
            for i in range(N):
                for j in range(N):
                    dp[step%2][i][j] = 0
                    for direction in directions:
                        rr, cc = i+direction[0], j+direction[1]
                        if 0 <= cc < N and 0 <= rr < N:
                            dp[step%2][i][j] += 0.125 * dp[(step-1)%2][rr][cc]
        return dp[K%2][r][c]