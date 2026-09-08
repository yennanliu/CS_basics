"""

51. N-Queens
Hard

The n-queens puzzle is the problem of placing n queens on an n x n
chessboard such that no two queens attack each other.

(two queens attack each other if they share a row, a column,
or a diagonal)

Given an integer n, return all distinct solutions to the n-queens puzzle.
You may return the answer in any order.

Each solution contains a distinct board configuration of the n-queens'
placement, where 'Q' and '.' both indicate a queen and an empty space
respectively.


Example 1:

Input: n = 4

Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]

Explanation:

There exist two distinct solutions to the 4-queens puzzle as shown above.

Example 2:

Input: n = 1

Output: [["Q"]]

Explanation:

A single queen on a 1 x 1 board attacks nothing.


Constraints:

1 <= n <= 9

"""

# V0
# IDEA : BACKTRACK (PLACE ONE QUEEN PER ROW) + 3 "USED" SETS
#
#   place exactly ONE queen per row, so the row conflict is impossible
#   by construction and only 3 things can still clash: the column,
#   the "\" diagonal and the "/" diagonal.
#
#   the trick is that each diagonal has a CONSTANT id, so a clash is a
#   set lookup instead of a scan of the board:
#
#      "\" diagonal -> row - col is the same for every cell on it
#      "/" diagonal -> row + col is the same for every cell on it
#
#   e.g. n = 4, queen at (1,3) -> row-col = -2, row+col = 4
#        -> (2,4) is off board, but (0,2) shares row+col = 2? no -> 0+2 = 2 != 4
#        -> (3,1) has row+col = 4 -> SAME "/" diagonal -> rejected
#
#   `queens[row] = col` is all the state a board needs; the "....Q..."
#   strings are only rendered once a full placement is reached.
#
# time = O(n!), space = O(n^2)   (O(n) aux, the rest is the returned boards)


class Solution(object):
    def solveNQueens(self, n):
        """
        :type n: int
        :rtype: List[List[str]]
        """
        # edge
        if n <= 0:
            return []

        res = []
        # queens[row] = the col the queen of that row sits on
        queens = []
        cols = set()
        diag = set()       # "\" -> row - col
        anti_diag = set()  # "/" -> row + col

        def backtrack(row):
            # all n rows filled -> render the board
            if row == n:
                res.append(["." * c + "Q" + "." * (n - c - 1) for c in queens])
                return

            for col in range(n):
                # NOTE !!! the 2 diagonals are keyed by (row - col) and (row + col)
                if col in cols or (row - col) in diag or (row + col) in anti_diag:
                    continue

                # place
                queens.append(col)
                cols.add(col)
                diag.add(row - col)
                anti_diag.add(row + col)

                backtrack(row + 1)

                # undo
                queens.pop()
                cols.remove(col)
                diag.remove(row - col)
                anti_diag.remove(row + col)

        backtrack(0)
        return res


# V0-1
# IDEA : BACKTRACK + BITMASK
#
#   same search, but the 3 sets become 3 integers, so "is this cell
#   attacked ?" is one AND instead of 3 hash lookups.
#
#   the diagonals are what make bitmask worth it: going down one row
#   SHIFTS a diagonal's threat by exactly one column, so the mask is
#   carried down rather than recomputed:
#
#      cols      -> passed down unchanged
#      "\" diag  -> (diag | bit) << 1
#      "/" diag  -> (anti | bit) >> 1
#
#   e.g. n = 4, queen at col 1 -> bit = 0010
#        next row is blocked at col 1 (cols), col 2 ("\"), col 0 ("/")
#
#   free = ~(cols | diag | anti) & ((1 << n) - 1) is the set of open
#   columns, and `free & -free` pops the lowest one, so this visits the
#   columns in the same order V0 does -> identical output order.
#
# time = O(n!), space = O(n^2)   (O(n) aux, the rest is the returned boards)


class Solution2(object):
    def solveNQueens(self, n):
        """
        :type n: int
        :rtype: List[List[str]]
        """
        # edge
        if n <= 0:
            return []

        res = []
        queens = []
        full = (1 << n) - 1

        def backtrack(row, cols, diag, anti_diag):
            if row == n:
                res.append(["." * c + "Q" + "." * (n - c - 1) for c in queens])
                return

            # NOTE !!! a 1 bit here = a column that is still SAFE
            free = ~(cols | diag | anti_diag) & full
            while free:
                bit = free & -free      # lowest safe column
                free ^= bit             # and drop it from the todo set
                col = bit.bit_length() - 1

                queens.append(col)
                backtrack(row + 1, cols | bit, (diag | bit) << 1, (anti_diag | bit) >> 1)
                queens.pop()

        backtrack(0, 0, 0, 0)
        return res
