"""

2056. Number of Valid Move Combinations On Chessboard
Hard

There is an 8 x 8 chessboard containing n pieces (rooks, queens, or bishops). You are given a string array pieces of length n, where pieces[i] describes the type (rook, queen, or bishop) of the ith piece. In addition, you are given a 2D integer array positions also of length n, where positions[i] = [r_i, c_i] indicates that the ith piece is currently at the 1-based coordinate (r_i, c_i) on the chessboard.

When making a move for a piece, you choose a destination square that the piece will travel toward and stop on.

A rook can only travel horizontally or vertically from (r, c) to the direction of (r+1, c), (r-1, c), (r, c+1), or (r, c-1).
A queen can only travel horizontally, vertically, or diagonally from (r, c) to the direction of (r+1, c), (r-1, c), (r, c+1), (r, c-1), (r+1, c+1), (r+1, c-1), (r-1, c+1), (r-1, c-1).
A bishop can only travel diagonally from (r, c) to the direction of (r+1, c+1), (r+1, c-1), (r-1, c+1), (r-1, c-1).

You must make a move for every piece on the board simultaneously. A move combination consists of all the moves performed on all the given pieces. Every second, each piece will instantaneously travel one square towards their destination if they are not already at it. All pieces start traveling at the 0th second. A move combination is invalid if, at a given time, two or more pieces occupy the same square.

Return the number of valid move combinations.

Notes:

No two pieces will start in the same square.
You may choose the square a piece is already on as its destination.
If two pieces are directly adjacent to each other, it is valid for them to move past each other and swap positions in one second.


Example 1:

Input: pieces = ["rook"], positions = [[1,1]]
Output: 15
Explanation: The image above shows the possible squares the piece can move to.

Example 2:

Input: pieces = ["queen"], positions = [[1,1]]
Output: 22
Explanation: The image above shows the possible squares the piece can move to.

Example 3:

Input: pieces = ["bishop"], positions = [[4,3]]
Output: 12
Explanation: The image above shows the possible squares the piece can move to.


Constraints:

n == pieces.length
n == positions.length
1 <= n <= 4
pieces only contains the strings "rook", "queen", and "bishop".
There will be at most one queen on the chessboard.
1 <= r_i, c_i <= 8
Each positions[i] is distinct.

"""

# V0
# IDEA : BACKTRACKING PIECE BY PIECE, RECORDING EACH PIECE'S TIMELINE
#
#   n <= 4 and every piece walks at most 7 squares in one of <= 8 directions,
#   so brute forcing (direction, stop-square) per piece is tiny.
#
#   dist[i][x][y] = the second at which piece i sits on (x, y), else -1
#   end[i]        = (x, y, t) where piece i finally parks (and stays forever)
#
#   when placing piece i we only need to check it against pieces 0..i-1:
#     - checkPass(x, y, t): nobody earlier is on (x, y) at exactly second t,
#       and nobody earlier has already PARKED on (x, y) at a second <= t.
#     - checkStop(x, y, t): to park here, every earlier piece must have left
#       (x, y) strictly before t (dist[j][x][y] < t).
#
#   NOTE : passing THROUGH each other (swapping) is legal, which is why the
#          collision test compares equal timestamps rather than segments.
#
# time = O((8 * 8)^n) with n <= 4, space = O(n * 81)
class Solution(object):
    def countCombinations(self, pieces, positions):
        ROOK = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        BISHOP = [(1, 1), (1, -1), (-1, 1), (-1, -1)]
        QUEEN = ROOK + BISHOP

        n = len(pieces)
        m = 9
        dist = [[[-1] * m for _ in range(m)] for _ in range(n)]
        end = [(0, 0, 0)] * n
        res = [0]

        def dirs_of(p):
            if p[0] == 'r':
                return ROOK
            if p[0] == 'b':
                return BISHOP
            return QUEEN

        def reset(i):
            for a in range(m):
                row = dist[i][a]
                for b in range(m):
                    row[b] = -1

        def check_stop(i, x, y, t):
            for j in range(i):
                if dist[j][x][y] >= t:
                    return False
            return True

        def check_pass(i, x, y, t):
            for j in range(i):
                if dist[j][x][y] == t:
                    return False
                if end[j][0] == x and end[j][1] == y and end[j][2] <= t:
                    return False
            return True

        def dfs(i):
            if i == n:
                res[0] += 1
                return
            x, y = positions[i]
            # option 1 : stay put
            reset(i)
            dist[i][x][y] = 0
            end[i] = (x, y, 0)
            if check_stop(i, x, y, 0):
                dfs(i + 1)
            # option 2 : walk along one direction and stop somewhere
            for dx, dy in dirs_of(pieces[i]):
                reset(i)
                dist[i][x][y] = 0
                nx, ny, nt = x + dx, y + dy, 1
                while 1 <= nx < m and 1 <= ny < m and check_pass(i, nx, ny, nt):
                    dist[i][nx][ny] = nt
                    end[i] = (nx, ny, nt)
                    if check_stop(i, nx, ny, nt):
                        dfs(i + 1)
                    nx += dx
                    ny += dy
                    nt += 1

        dfs(0)
        return res[0]
