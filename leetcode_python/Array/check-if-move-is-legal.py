"""

1958. Check if Move is Legal
Medium

You are given a 0-indexed 8 x 8 grid board, where board[r][c] represents the cell (r, c) on a game board. On the board, free cells are represented by '.', white cells are represented by 'W', and black cells are represented by 'B'.

Each move in this game consists of choosing a free cell and changing it to the color you are playing as (either white or black). However, a move is only legal if, after changing it, the cell becomes the endpoint of a good line (horizontal, vertical, or diagonal).

A good line is a line of three or more cells (including the endpoints) where the endpoints of the line are one color, and the remaining cells in the middle are the opposite color (no cells in the line are free).

Given two integers rMove and cMove and a character color representing the color you are playing as (white or black), return true if changing cell (rMove, cMove) to color color is a legal move, or false if it is not legal.


Example 1:

Input: board = [[".",".",".","B",".",".",".","."],[".",".",".","W",".",".",".","."],[".",".",".","W",".",".",".","."],[".",".",".","W",".",".",".","."],["W","B","B",".","W","W","W","B"],[".",".",".","B",".",".",".","."],[".",".",".","B",".",".",".","."],[".",".",".","W",".",".",".","."]], rMove = 4, cMove = 3, color = "B"
Output: true
Explanation: '.', 'W', and 'B' are represented by the colors blue, white, and black respectively, and cell (rMove, cMove) is marked with an 'X'.
The two good lines with the chosen cell as an endpoint are annotated above with the red rectangles.

Example 2:

Input: board = [[".",".",".",".",".",".",".","."],[".","B",".",".","W",".",".","."],[".",".","W",".",".",".",".","."],[".",".",".","W","B",".",".","."],[".",".",".",".",".",".",".","."],[".",".",".",".","B","W",".","."],[".",".",".",".",".",".","W","."],[".",".",".",".",".",".",".","B"]], rMove = 4, cMove = 4, color = "W"
Output: false
Explanation: While there are good lines with the chosen cell as a middle cell, there are no good lines with the chosen cell as an endpoint.


Constraints:

board.length == board[r].length == 8
0 <= rMove, cMove < 8
board[rMove][cMove] == '.'
color is either 'B' or 'W'.

"""

# V0
# IDEA : ENUMERATE 8 DIRECTIONS (walk out from the placed cell)
#
#   from (rMove, cMove) shoot a ray in each of the 8 directions and count
#   how many cells we stepped over (cnt).
#     - the FIRST step must land on the opposite color, otherwise the ray dies
#     - keep walking while we see the opposite color
#     - if we then hit `color` after >= 2 steps -> good line (len >= 3) -> True
#     - hitting '.' (or running off the board) kills the ray
#
#   NOTE : cnt > 1 is the "3+ cells" check - endpoint + >=1 middle + endpoint.
#
# time = O(8 * 8) = O(1), space = O(1)
class Solution(object):
    def checkMove(self, board, rMove, cMove, color):
        for da in (-1, 0, 1):
            for db in (-1, 0, 1):
                if da == 0 and db == 0:
                    continue
                i, j = rMove, cMove
                cnt = 0
                while 0 <= i + da < 8 and 0 <= j + db < 8:
                    i += da
                    j += db
                    cnt += 1
                    if cnt > 1 and board[i][j] == color:
                        return True
                    # '.' kills the line, and `color` too early kills it
                    if board[i][j] == color or board[i][j] == ".":
                        break
        return False


# V0-1
# IDEA : BUILD THE 4 LINES THROUGH THE CELL, THEN ONE REGEX
#
#   the cell lies on exactly 4 lines : its row, its column and the two
#   diagonals. Render each line as a string with the move already applied and
#   remember the index the placed cell sits at. Reading OUTWARD from that index
#   (forwards, then the reversed prefix) a good line is literally the pattern
#       color  opposite+  color
#   so 8 tiny regex tests decide the whole question - no manual stepping.
#
# time = O(1) (4 lines of length <= 8 on a fixed 8x8 board)
# space = O(1)
class Solution(object):
    def checkMove(self, board, rMove, cMove, color):
        import re
        opp = 'B' if color == 'W' else 'W'
        pat = re.compile('^' + color + opp + '+' + color)

        lines = []
        lines.append([(rMove, c) for c in range(8)])                  # row
        lines.append([(r, cMove) for r in range(8)])                  # column
        lines.append([(r, r - rMove + cMove) for r in range(8)        # diag
                      if 0 <= r - rMove + cMove < 8])
        lines.append([(r, rMove + cMove - r) for r in range(8)        # anti
                      if 0 <= rMove + cMove - r < 8])

        for cells in lines:
            p = cells.index((rMove, cMove))
            s = ''.join(color if (r, c) == (rMove, cMove) else board[r][c]
                        for r, c in cells)
            if pat.match(s[p:]) or pat.match(s[p::-1]):
                return True
        return False


# V0-2
# IDEA : BRUTE FORCE - ENUMERATE EVERY CANDIDATE GOOD LINE
#
#   a good line having the move as one endpoint is fully determined by
#   (direction, length) : 8 directions x lengths 3..8 = 48 candidates.
#   For each candidate simply validate the WHOLE segment up front -
#   the far endpoint must be `color` and every cell strictly between the two
#   endpoints must be the opposite color. No early-exit reasoning is needed,
#   which makes this the easiest version to trust.
#
# time = O(8 * 8 * 8) = O(1)
# space = O(1)
class Solution(object):
    def checkMove(self, board, rMove, cMove, color):
        opp = 'B' if color == 'W' else 'W'
        dirs = [(-1, -1), (-1, 0), (-1, 1), (0, -1),
                (0, 1), (1, -1), (1, 0), (1, 1)]
        for da, db in dirs:
            for length in range(3, 9):
                cells = [(rMove + da * k, cMove + db * k)
                         for k in range(length)]
                if not all(0 <= r < 8 and 0 <= c < 8 for r, c in cells):
                    continue
                er, ec = cells[-1]
                if board[er][ec] != color:
                    continue
                if all(board[r][c] == opp for r, c in cells[1:-1]):
                    return True
        return False
