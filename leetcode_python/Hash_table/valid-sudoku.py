# Time:  O(9^2)
# Space: O(9)

# Determine if a Sudoku is valid,
# according to: Sudoku Puzzles - The Rules.
#
# The Sudoku board could be partially filled,
# where empty cells are filled with the character '.'.
#
# A partially filled sudoku which is valid.
#
# Note:
# A valid Sudoku board (partially filled) is not necessarily solvable.
# Only the filled cells need to be validated.


# V1 : dev 




# V2 
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        for i in range(9):
            if not self.isValidList([board[i][j] for j in range(9)]) or \
               not self.isValidList([board[j][i] for j in range(9)]):
                return False
        for i in range(3):
            for j in range(3):
                if not self.isValidList([board[m][n] for n in range(3 * j, 3 * j + 3) \
                                                     for m in range(3 * i, 3 * i + 3)]):
                    return False
        return True

    def isValidList(self, xs):
        xs = [x for x in xs if x != '.']
        return len(set(xs)) == len(xs)


if __name__ == "__main__":
    board = [[1, '.', '.', '.', '.', '.', '.', '.', '.'],
             ['.', 2, '.', '.', '.', '.', '.', '.', '.'],
             ['.', '.', 3, '.', '.', '.', '.', '.', '.'],
             ['.', '.', '.', 4, '.', '.', '.', '.', '.'],
             ['.', '.', '.', '.', 5, '.', '.', '.', '.'],
             ['.', '.', '.', '.', '.', 6, '.', '.', '.'],
             ['.', '.', '.', '.', '.', '.', 7, '.', '.'],
             ['.', '.', '.', '.', '.', '.', '.', 8, '.'],
             ['.', '.', '.', '.', '.', '.', '.', '.', 9]]
    print(Solution().isValidSudoku(board))

    