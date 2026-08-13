"""

782. Transform to Chessboard
Hard

You are given an n x n binary grid board. In each move, you can swap any two rows
with each other, or any two columns with each other.

Return the minimum number of moves to transform the board into a chessboard board.
If the task is impossible, return -1.

A chessboard board is a board where no 0's and no 1's are 4-directionally adjacent.


Example 1:

Input: board = [[0,1,1,0],[0,1,1,0],[1,0,0,1],[1,0,0,1]]
Output: 2
Explanation: One potential sequence of moves is shown.
The first move swaps the first and second column.
The second move swaps the second and third row.

Example 2:

Input: board = [[0,1],[1,0]]
Output: 0
Explanation: Also note that the board with 0 in the top left corner,
is also a valid chessboard.

Example 3:

Input: board = [[1,0],[1,0]]
Output: -1
Explanation: No matter what sequence of moves you make,
you cannot end with a valid chessboard.


Constraints:

n == board.length
n == board[i].length
2 <= n <= 30
board[i][j] is either 0 or 1.

"""

# V0
# IDEA : MATH / PATTERN OBSERVATION
#
#  Key observations:
#
#   1) A board is fixable ONLY IF every row is either identical to row 0
#      or the exact complement of row 0 (same for columns).
#      That is equivalent to:
#          board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j] == 0
#      for every (i, j)  -> the whole board is determined by its
#      first row and first column.
#
#   2) Row / column swaps never change the multiset of rows, so the
#      number of 1s in row 0 (and col 0) must be n//2 or (n+1)//2.
#
#   3) Once valid, the answer is decided purely by how many entries of
#      the first column (resp. first row) already sit on the "right"
#      parity. Each swap fixes 2 misplaced entries, so we divide by 2.
#      - n even : the board may start with 0 or 1, take the cheaper one.
#      - n odd  : only one starting bit is possible, the odd count is invalid.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def movesToChessboard(self, board):
        n = len(board)

        # 1) every 2x2 sub-rectangle formed with (0,0) must XOR to 0
        for i in range(n):
            for j in range(n):
                if board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j] != 0:
                    return -1

        # 2) the first row / first column must be (almost) balanced
        row_ones = sum(board[0])
        col_ones = sum(board[i][0] for i in range(n))
        if not (n // 2 <= row_ones <= (n + 1) // 2):
            return -1
        if not (n // 2 <= col_ones <= (n + 1) // 2):
            return -1

        # 3) count entries already matching the "starts with 0" pattern
        #    i.e. board[i][0] == i % 2  ->  0,1,0,1,...
        row_swap = sum(board[i][0] == i % 2 for i in range(n))
        col_swap = sum(board[0][j] == j % 2 for j in range(n))

        if n % 2 == 1:
            # odd n: only one of the two patterns is reachable,
            # the reachable one always leaves an even number of mismatches
            if row_swap % 2:
                row_swap = n - row_swap
            if col_swap % 2:
                col_swap = n - col_swap
        else:
            # even n: both patterns are reachable, pick the cheaper one
            row_swap = min(row_swap, n - row_swap)
            col_swap = min(col_swap, n - col_swap)

        # each swap puts 2 lines in place
        return (row_swap + col_swap) // 2
