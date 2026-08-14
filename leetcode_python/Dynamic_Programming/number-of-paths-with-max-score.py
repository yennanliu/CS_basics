"""

1301. Number of Paths with Max Score
Hard

You are given a square board of characters. You can move on the board starting at the bottom
right square marked with the character 'S'.

You need to reach the top left square marked with the character 'E'. The rest of the squares
are labeled either with a numeric character 1, 2, ..., 9 or with an obstacle 'X'.
In one move you can go up, left or up-left (diagonally) only if there is no obstacle there.

Return a list of two integers: the first integer is the maximum sum of numeric characters you
can collect, and the second is the number of such paths that you can take to get that maximum sum,
taken modulo 10^9 + 7.

In case there is no path, return [0, 0].


Example 1:

Input: board = ["E23","2X2","12S"]

Output: [7,1]

Example 2:

Input: board = ["E12","1X1","21S"]

Output: [4,2]

Example 3:

Input: board = ["E11","XXX","11S"]

Output: [0,0]


Constraints:

2 <= board.length == board[i].length <= 100

"""

# V0
# IDEA : 2D DP (max score + path count, filled bottom-right -> top-left)
#
#   walking backwards turns the moves (up / left / up-left) into
#   (down / right / down-right), so cell (i, j) reads from
#   (i+1, j), (i, j+1), (i+1, j+1).
#
#   carry TWO tables :
#     f[i][j] = best score reachable from 'S' to (i, j)   (-1 = unreachable)
#     g[i][j] = how many paths achieve f[i][j]
#
#   merging a neighbour :
#     strictly better -> overwrite score AND replace the count
#     equal           -> keep the score, ADD the counts
#
#   NOTE : 'E' and 'S' contribute 0, only digit cells add to the score.
#   NOTE : take the modulo while accumulating g, not only at the end.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def pathsWithMaxScore(self, board):
        n = len(board)
        MOD = 10 ** 9 + 7

        f = [[-1] * n for _ in range(n)]
        g = [[0] * n for _ in range(n)]
        f[n - 1][n - 1] = 0
        g[n - 1][n - 1] = 1

        for i in range(n - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                if i == n - 1 and j == n - 1:
                    continue
                if board[i][j] == 'X':
                    continue

                best, cnt = -1, 0
                for x, y in ((i + 1, j), (i, j + 1), (i + 1, j + 1)):
                    if x >= n or y >= n or f[x][y] == -1:
                        continue
                    if f[x][y] > best:
                        best, cnt = f[x][y], g[x][y]
                    elif f[x][y] == best:
                        cnt = (cnt + g[x][y]) % MOD

                if best == -1:
                    continue
                if board[i][j].isdigit():
                    best += int(board[i][j])
                f[i][j] = best
                g[i][j] = cnt

        if f[0][0] == -1:
            return [0, 0]
        return [f[0][0], g[0][0] % MOD]
