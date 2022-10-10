"""

37. Sudoku Solver
Hard

Write a program to solve a Sudoku puzzle by filling the empty cells.

A sudoku solution must satisfy all of the following rules:

Each of the digits 1-9 must occur exactly once in each row.
Each of the digits 1-9 must occur exactly once in each column.
Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
The '.' character indicates empty cells.

 

Example 1:


Input: board = [["5","3",".",".","7",".",".",".","."],["6",".",".","1","9","5",".",".","."],[".","9","8",".",".",".",".","6","."],["8",".",".",".","6",".",".",".","3"],["4",".",".","8",".","3",".",".","1"],["7",".",".",".","2",".",".",".","6"],[".","6",".",".",".",".","2","8","."],[".",".",".","4","1","9",".",".","5"],[".",".",".",".","8",".",".","7","9"]]
Output: [["5","3","4","6","7","8","9","1","2"],["6","7","2","1","9","5","3","4","8"],["1","9","8","3","4","2","5","6","7"],["8","5","9","7","6","1","4","2","3"],["4","2","6","8","5","3","7","9","1"],["7","1","3","9","2","4","8","5","6"],["9","6","1","5","3","7","2","8","4"],["2","8","7","4","1","9","6","3","5"],["3","4","5","2","8","6","1","7","9"]]
Explanation: The input board is shown above and the only valid solution is shown below:


 

Constraints:

board.length == 9
board[i].length == 9
board[i][j] is a digit or '.'.
It is guaranteed that the input board has only one solution.

"""

# V0

# V1
# IDEA : BACKTRACK
# https://leetcode.com/problems/sudoku-solver/solutions/259057/sudoku-solver/
from collections import defaultdict
class Solution:
    def solveSudoku(self, board):
        """
        :type board: List[List[str]]
        :rtype: void Do not return anything, modify board in-place instead.
        """
        def could_place(d, row, col):
            """
            Check if one could place a number d in (row, col) cell
            """
            return not (d in rows[row] or d in columns[col] or \
                    d in boxes[box_index(row, col)])
        
        def place_number(d, row, col):
            """
            Place a number d in (row, col) cell
            """
            rows[row][d] += 1
            columns[col][d] += 1
            boxes[box_index(row, col)][d] += 1
            board[row][col] = str(d)
            
        def remove_number(d, row, col):
            """
            Remove a number which didn't lead 
            to a solution
            """
            del rows[row][d]
            del columns[col][d]
            del boxes[box_index(row, col)][d]
            board[row][col] = '.'    
            
        def place_next_numbers(row, col):
            """
            Call backtrack function in recursion
            to continue to place numbers
            till the moment we have a solution
            """
            # if we're in the last cell
            # that means we have the solution
            if col == N - 1 and row == N - 1:
                nonlocal sudoku_solved
                sudoku_solved = True
            #if not yet    
            else:
                # if we're in the end of the row
                # go to the next row
                if col == N - 1:
                    backtrack(row + 1, 0)
                # go to the next column
                else:
                    backtrack(row, col + 1)
                
                
        def backtrack(row = 0, col = 0):
            """
            Backtracking
            """
            # if the cell is empty
            if board[row][col] == '.':
                # iterate over all numbers from 1 to 9
                for d in range(1, 10):
                    if could_place(d, row, col):
                        place_number(d, row, col)
                        place_next_numbers(row, col)
                        # if sudoku is solved, there is no need to backtrack
                        # since the single unique solution is promised
                        if not sudoku_solved:
                            remove_number(d, row, col)
            else:
                place_next_numbers(row, col)
                    
        # box size
        n = 3
        # row size
        N = n * n
        # lambda function to compute box index
        box_index = lambda row, col: (row // n ) * n + col // n
        
        # init rows, columns and boxes
        rows = [defaultdict(int) for i in range(N)]
        columns = [defaultdict(int) for i in range(N)]
        boxes = [defaultdict(int) for i in range(N)]
        for i in range(N):
            for j in range(N):
                if board[i][j] != '.': 
                    d = int(board[i][j])
                    place_number(d, i, j)
        
        sudoku_solved = False
        backtrack()

# V1'
# IDEA : DFS
# https://leetcode.com/problems/sudoku-solver/solutions/1995505/very-short-python-dfs-solution-with-notes/
# IDEA :
# DFS is used as a recursive function to try out all possible cases of a problem and find the case(s) that works and return it to you. In this case, it checks if it follows all 3 rules and if it does it edits the board until it finds a case that works.
class Solution:
    def solveSudoku(self, board: List[List[str]]) -> None:
        spaces = [] # create an empty list
        for i in range(9):
            for j in range(9): # create a 9x9 matrix (same dimensions as board)
                if board[i][j] == '.':
                    spaces.append((i,j)) # append to spaces if coordinate in board is empty
        
        def dfs(idx) -> bool: # dfs function defined
            if idx == len(spaces):
                return True # stop the dfs function if all empty coordinates was filled with nums, terminate DFS.
            
            i,j = spaces[idx] # get x, y coordinate from spaces
            for fill in range(1,10): # Get numbers 1-9 to try and fill
                s = str(fill)
                if s in board[i]: # check rule 1
                    continue  # check 1 fail

                if any(s == board[cell][j] for cell in range(9)): # check rule 2, if any fails then continue
                    continue  # check 2 fail

                # check rule 3:

                row = i // 3 * 3
                col = j // 3 * 3 # find the box in the sudoku
                if any(s == board[r][c] for r in range(row,row+3) for c in range(col,col+3)): # find the other coordinates in the box, and see if it fits the rules
                    continue  # check 3 fail

                board[i][j] = s # change the board coordinates that are empty with the correct nums
                if dfs(idx+1): # add 1 to the idx
                    return True
                board[i][j] = '.'

            return False
        dfs(0) # run the dfs function with idx with 0

# V1''
# IDEA : BACKTRACKING
# https://leetcode.com/problems/sudoku-solver/solutions/1418166/python-backtracking/
class Solution:
    def solveSudoku(self, board: List[List[str]]) -> None:
        self.b = board
        self.emptyCells = []
        
        for i in range(9):
            for j in range(9):
                if board[i][j] == '.':
                    self.emptyCells.append((i, j))
                    
        self.backTrack()
    
    def backTrack(self) -> None:
        if not self.emptyCells: return True
        
        x, y = self.emptyCells.pop(0)
        
        for val in range(1, 10):
            if self.isValid(x, y, str(val)):
                self.b[x][y] = str(val)
                if self.backTrack():
                    return True
                else:
                    self.b[x][y] = '.'
            
        self.emptyCells.insert(0, (x, y))
        return False
                    
    def isValid(self, row: int, col: int, c: str) -> bool:
        for i in range(9):
            if self.b[i][col] != '.' and self.b[i][col] == c: return False
            if self.b[row][i] != '.' and self.b[row][i] == c: return False
            if self.b[3 * (row // 3) + i // 3][ 3 * (col // 3) + i % 3] != '.' and self.b[3 * (row // 3) + i // 3][3 * (col // 3) + i % 3] == c: return False
        
        return True

# V1'''
# IDEA : DFS
# https://leetcode.com/problems/sudoku-solver/solutions/659100/concise-python-solution/
# Trick :
# 	1)'list(zip(*A))' is the transpose of A
# 	2) for a general matrix, the nth element is at row n // len(col) and col n % len(col)
# 	3) in python, setA - setB is equivalent to setA.difference(setB)
#  	4) string.digits is equivalent to '0123456789'
class Solution:
    def solveSudoku(self, board: List[List[str]]) -> None:
        def unique_vals(row, col):
            transpose = list(map(list, zip(*board)))
            colstart, rowstart = (col // 3) * 3, (row // 3) * 3 # topleft corner of each 3 by 3 square
            three_by_three = [board[i][j] 
							  for i in range(rowstart, rowstart + 3) 
							  for j in range(colstart, colstart + 3)]
            return set(string.digits[1:]) - set(board[row] + transpose[col] + three_by_three) - set('.')
            
        def solve(i):
            if i == 81:
                return True
            row, col = i // 9, i % 9
            if board[row][col] == '.':
                for val in unique_vals(row, col):
                    board[row][col] = val
                    if solve(i + 1):
                        return True
                    board[row][col] = '.'
            else:
                 if solve(i + 1):
                    return True
            return False

        solve(0)

# V1'''''
# IDEA : BACKTRACKING
# https://leetcode.com/problems/sudoku-solver/solutions/2683628/python/
class Solution:
    def solveSudoku(self, board: List[List[str]]) -> None:
        """
        Do not return anything, modify board in-place instead.
        """
        def is_valid( row: int, col: int, val: int) -> bool:
            # 判断同一行是否冲突
            for i in range(9):
                if board[row][i] == str(val):
                    return False
            # 判断同一列是否冲突
            for j in range(9):
                if board[j][col] == str(val):
                    return False
            # 判断同一九宫格是否有冲突
            start_row = (row // 3) * 3
            start_col = (col // 3) * 3
            for i in range(start_row, start_row + 3):
                for j in range(start_col, start_col + 3):
                    if board[i][j] == str(val):
                        return False
            return True
        
        def solve():    
            for row in range(9):
                for col in range(9):
                    if board[row][col] != '.':
                            continue
                    for i in range(1, 10):   
                        if is_valid(row, col, i):                                
                            board[row][col] = str(i) 
                            if solve():
                                return True
                            board[row][col] = '.'
                         
                    return False
            return True
        solve()
        return board

# V2