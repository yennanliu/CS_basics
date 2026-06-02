"""

36. Valid Sudoku
Medium

Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:

Each row must contain the digits 1-9 without repetition.
Each column must contain the digits 1-9 without repetition.
Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
Note:

A Sudoku board (partially filled) could be valid but is not necessarily solvable.
Only the filled cells need to be validated according to the mentioned rules.
 

Example 1:


Input: board = 
[["5","3",".",".","7",".",".",".","."]
,["6",".",".","1","9","5",".",".","."]
,[".","9","8",".",".",".",".","6","."]
,["8",".",".",".","6",".",".",".","3"]
,["4",".",".","8",".","3",".",".","1"]
,["7",".",".",".","2",".",".",".","6"]
,[".","6",".",".",".",".","2","8","."]
,[".",".",".","4","1","9",".",".","5"]
,[".",".",".",".","8",".",".","7","9"]]
Output: true

Example 2:

Input: board = 
[["8","3",".",".","7",".",".",".","."]
,["6",".",".","1","9","5",".",".","."]
,[".","9","8",".",".",".",".","6","."]
,["8",".",".",".","6",".",".",".","3"]
,["4",".",".","8",".","3",".",".","1"]
,["7",".",".",".","2",".",".",".","6"]
,[".","6",".",".",".",".","2","8","."]
,[".",".",".","4","1","9",".",".","5"]
,[".",".",".",".","8",".",".","7","9"]]
Output: false
Explanation: Same as Example 1, except with the 5 in the top left corner being modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.
 

Constraints:

board.length == 9
board[i].length == 9
board[i][j] is a digit 1-9 or '.'.

"""


# V0
class Solution(object):
    def isValidSudoku(self, board):
        n = 9

        for i in range(n):
            if not self.check_row(board, i):
                return False
            if not self.check_col(board, i):
                return False

        for r in range(0, 9, 3):
            for c in range(0, 9, 3):
                if not self.check_box(board, r, c):
                    return False

        return True

    def check_row(self, board, r):
        s = set()
        for c in range(9):
            val = board[r][c]
            if val == ".":
                continue
            if val in s:
                return False
            s.add(val)
        return True

    def check_col(self, board, c):
        s = set()
        for r in range(9):
            val = board[r][c]
            if val == ".":
                continue
            if val in s:
                return False
            s.add(val)
        return True

    def check_box(self, board, r0, c0):
        s = set()
        for r in range(r0, r0 + 3):
            for c in range(c0, c0 + 3):
                val = board[r][c]
                if val == ".":
                    continue
                if val in s:
                    return False
                s.add(val)
        return True


# V0
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        n = len(board)
        return self.isValidRow(board) and self.isValidCol(board) and self.isValidNineCell(board)
        
    def isValidRow(self, board):
        n = len(board)
        for r in range(n):
            row = [x for x in board[r] if x != '.']
            if len(set(row)) != len(row): # if not repetition 
                return False
        return True

    def isValidCol(self, board):
        n = len(board)
        for c in range(n):
            col = [board[r][c] for r in range(n) if board[r][c] != '.']
            if len(set(col)) != len(col): # if not repetition 
                return False
        return True

    def isValidNineCell(self, board):
        n = len(board)
        for r in range(0, n, 3):
            for c in range(0, n, 3):
                cell = []
                for i in range(3):
                    for j in range(3):
                        num = board[r + i][c + j]
                        if num != '.':
                            cell.append(num)
                if len(set(cell)) != len(cell): # if not repetition 
                    return False
        return True


# V0-1
class Solution(object):
    def isValidSudoku(self, board):
        rows = [set() for _ in range(9)]
        cols = [set() for _ in range(9)]
        boxes = [set() for _ in range(9)]

        for r in range(9):
            for c in range(9):
                val = board[r][c]

                if val == ".":
                    continue

                box_id = (r // 3) * 3 + (c // 3)

                if (val in rows[r] or
                    val in cols[c] or
                    val in boxes[box_id]):
                    return False

                rows[r].add(val)
                cols[c].add(val)
                boxes[box_id].add(val)

        return True


# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82813653
# IDEA : GREEDY 
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        n = len(board)
        return self.isValidRow(board) and self.isValidCol(board) and self.isValidNineCell(board)
        
    def isValidRow(self, board):
        n = len(board)
        for r in range(n):
            row = [x for x in board[r] if x != '.']
            if len(set(row)) != len(row): # if not repetition 
                return False
        return True

    def isValidCol(self, board):
        n = len(board)
        for c in range(n):
            col = [board[r][c] for r in range(n) if board[r][c] != '.']
            if len(set(col)) != len(col): # if not repetition 
                return False
        return True

    def isValidNineCell(self, board):
        n = len(board)
        for r in range(0, n, 3):
            for c in range(0, n, 3):
                cell = []
                for i in range(3):
                    for j in range(3):
                        num = board[r + i][c + j]
                        if num != '.':
                            cell.append(num)
                if len(set(cell)) != len(cell): # if not repetition 
                    return False
        return True

# V1'
# https://blog.csdn.net/coder_orz/article/details/51596499
# IDEA : HASH TABLE 
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        seen = []
        for i, row in enumerate(board):
            for j, c in enumerate(row):
                if c != '.':
                    seen += [(c,j),(i,c),(i/3,j/3,c)]
        return len(seen) == len(set(seen))

# V1''
# https://www.jiuzhang.com/solution/valid-sudoku/#tag-highlight-lang-python
class Solution:
    # @param board, a 9x9 2D array
    # @return a boolean
    def isValidSudoku(self, board):
        row = [set([]) for i in range(9)]
        col = [set([]) for i in range(9)]
        grid = [set([]) for i in range(9)]

        for r in range(9):
            for c in range(9):
                if board[r][c] == '.':
                    continue
                if board[r][c] in row[r]:
                    return False
                if board[r][c] in col[c]:
                    return False

                g = r / 3 * 3 + c / 3
                if board[r][c] in grid[g]:
                    return False
                grid[g].add(board[r][c])
                row[r].add(board[r][c])
                col[c].add(board[r][c])
        return True

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

# if __name__ == "__main__":
#     board = [[1, '.', '.', '.', '.', '.', '.', '.', '.'],
#              ['.', 2, '.', '.', '.', '.', '.', '.', '.'],
#              ['.', '.', 3, '.', '.', '.', '.', '.', '.'],
#              ['.', '.', '.', 4, '.', '.', '.', '.', '.'],
#              ['.', '.', '.', '.', 5, '.', '.', '.', '.'],
#              ['.', '.', '.', '.', '.', 6, '.', '.', '.'],
#              ['.', '.', '.', '.', '.', '.', 7, '.', '.'],
#              ['.', '.', '.', '.', '.', '.', '.', 8, '.'],
#              ['.', '.', '.', '.', '.', '.', '.', '.', 9]]
#     print(Solution().isValidSudoku(board))


# V3
# Time:  O(9^2)
# Space: O(9)
class Solution(object):
    def isValidSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: bool
        """
        for i in xrange(9):
            if not self.isValidList([board[i][j] for j in xrange(9)]) or \
               not self.isValidList([board[j][i] for j in xrange(9)]):
                return False
        for i in xrange(3):
            for j in xrange(3):
                if not self.isValidList([board[m][n] for n in xrange(3 * j, 3 * j + 3) \
                                                     for m in xrange(3 * i, 3 * i + 3)]):
                    return False
        return True

    def isValidList(self, xs):
        xs = filter(lambda x: x != '.', xs)
        return len(set(xs)) == len(xs)