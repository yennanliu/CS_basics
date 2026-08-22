"""

999. Available Captures for Rook
Easy

You are given an 8 x 8 matrix representing a chessboard. There is exactly one white rook represented by 'R', some number of white bishops 'B', and some number of black pawns 'p'. Empty squares are represented by '.'.

A rook can move any number of squares horizontally or vertically (up, down, left, right) until it reaches another piece or the edge of the board. A rook is attacking a pawn if it can move to the pawn's square in one move.

Note: A rook cannot move through other pieces, such as bishops or pawns. This means a rook cannot attack a pawn if there is another piece blocking the path.

Return the number of pawns the white rook is attacking.

Example 1:

Input: board = [[".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],[".",".",".","R",".",".",".","p"],[".",".",".",".",".",".",".","."],[".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],[".",".",".",".",".",".",".","."],[".",".",".",".",".",".",".","."]]
Output: 3
Explanation: In this example, the rook is attacking all the pawns.

Example 2:

Input: board = [[".",".",".",".",".",".",".","."],[".","p","p","p","p","p",".","."],[".","p","p","B","p","p",".","."],[".","p","B","R","B","p",".","."],[".","p","p","B","p","p",".","."],[".","p","p","p","p","p",".","."],[".",".",".",".",".",".",".","."],[".",".",".",".",".",".",".","."]]
Output: 0
Explanation: The bishops are blocking the rook from attacking any of the pawns.

Example 3:

Input: board = [[".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],[".",".",".","p",".",".",".","."],["p","p",".","R",".","p","B","."],[".",".",".",".",".",".",".","."],[".",".",".","B",".",".",".","."],[".",".",".","p",".",".",".","."],[".",".",".",".",".",".",".","."]]
Output: 3
Explanation: The rook is attacking the pawns at positions b5, d6, and f5.

Constraints:

board.length == 8
board[i].length == 8
board[i][j] is either 'R', '.', 'B', or 'p'
There is exactly one cell with board[i][j] == 'R'

"""

# V0
# IDEA : SIMULATION - ray cast in the 4 rook directions
#
#  Locate 'R', then walk outwards in each of the 4 directions until we
#  either leave the board or hit a piece:
#     - hit 'p' -> that pawn is captured, stop that ray
#     - hit 'B' -> blocked, stop that ray with no capture
#
# time = O(m * n)  # scanning the board to find the rook dominates
# space = O(1)
class Solution(object):
    def numRookCaptures(self, board):
        rows, cols = len(board), len(board[0])

        # locate the rook
        ri = rj = -1
        for i in range(rows):
            for j in range(cols):
                if board[i][j] == 'R':
                    ri, rj = i, j
                    break
            if ri != -1:
                break

        res = 0
        for di, dj in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            x, y = ri + di, rj + dj
            while 0 <= x < rows and 0 <= y < cols:
                if board[x][y] == 'p':
                    res += 1
                    break
                if board[x][y] == 'B':
                    break
                x += di
                y += dj

        return res


# V0-1
# IDEA : COMPRESS THE ROOK'S LINES, LOOK AT ITS NEIGHBOURS
#
#   drop the empty squares from the rook's row and from the rook's column.
#   in the compressed string the characters sitting directly next to 'R' are
#   exactly the first blocker of each ray, so the answer is the number of 'p'
#   among (at most) those 4 characters - no stepwise walk needed.
#
# time = O(m * n)  # the scan that locates the rook dominates
# space = O(m + n)
class Solution(object):
    def numRookCaptures(self, board):
        rows = len(board)
        ri = rj = -1
        for i in range(rows):
            if 'R' in board[i]:
                ri, rj = i, list(board[i]).index('R')
                break

        row_line = ''.join(board[ri]).replace('.', '')
        col_line = ''.join(board[i][rj] for i in range(rows)).replace('.', '')

        res = 0
        for line in (row_line, col_line):
            k = line.index('R')
            if k > 0 and line[k - 1] == 'p':
                res += 1
            if k + 1 < len(line) and line[k + 1] == 'p':
                res += 1
        return res


# V0-2
# IDEA : BFS OVER THE SQUARES THE ROOK CAN SLIDE THROUGH
#
#   treat the board as a graph and push (cell, direction) into the queue : a
#   state only ever expands along the SAME direction, which keeps every walk a
#   legal rook move. an empty square is enqueued, a 'p' scores and stops, a 'B'
#   just stops.
#
# time = O(m * n), space = O(m + n)
class Solution(object):
    def numRookCaptures(self, board):
        import collections
        rows, cols = len(board), len(board[0])
        ri = rj = -1
        for i in range(rows):
            for j in range(cols):
                if board[i][j] == 'R':
                    ri, rj = i, j

        q = collections.deque(
            (ri, rj, d) for d in ((-1, 0), (1, 0), (0, -1), (0, 1))
        )
        res = 0
        while q:
            x, y, (di, dj) = q.popleft()
            nx, ny = x + di, y + dj
            if not (0 <= nx < rows and 0 <= ny < cols):
                continue
            cell = board[nx][ny]
            if cell == 'p':
                res += 1
            elif cell == '.':
                q.append((nx, ny, (di, dj)))
        return res
