"""

52. N-Queens II
Hard

The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.

Given an integer n, return the number of distinct solutions to the n-queens puzzle.


Example 1:


Input: n = 4
Output: 2
Explanation: There are two distinct solutions to the 4-queens puzzle as shown.
Example 2:

Input: n = 1
Output: 1
 

Constraints:

1 <= n <= 9

"""

# V0

# V1
# IDEA : BACKTRACK
# https://leetcode.com/problems/n-queens-ii/solutions/1146740/n-queens-ii/
class Solution:
    def totalNQueens(self, n):
        def backtrack(row, diagonals, anti_diagonals, cols):
            # Base case - N queens have been placed
            if row == n:
                return 1

            solutions = 0
            for col in range(n):
                curr_diagonal = row - col
                curr_anti_diagonal = row + col
                # If the queen is not placeable
                if (col in cols 
                      or curr_diagonal in diagonals 
                      or curr_anti_diagonal in anti_diagonals):
                    continue

                # "Add" the queen to the board
                cols.add(col)
                diagonals.add(curr_diagonal)
                anti_diagonals.add(curr_anti_diagonal)

                # Move on to the next row with the updated board state
                solutions += backtrack(row + 1, diagonals, anti_diagonals, cols)

                # "Remove" the queen from the board since we have already
                # explored all valid paths using the above function call
                cols.remove(col)
                diagonals.remove(curr_diagonal)
                anti_diagonals.remove(curr_anti_diagonal)

            return solutions

        return backtrack(0, set(), set(), set())

# V1
# IDEA : BACKTRACK
# https://leetcode.com/problems/n-queens-ii/solutions/243444/python-solution/
class Solution:
    def totalNQueens(self, n: int) -> int:
        def backtrack(i):
            if i == n:
                return 1
            res = 0
            for j in range(n):
                if j not in cols and i-j not in diag and i+j not in off_diag:
                    cols.add(j)
                    diag.add(i-j)
                    off_diag.add(i+j)
                    res += backtrack(i+1)
                    off_diag.remove(i+j)
                    diag.remove(i-j)
                    cols.remove(j)
            return res
       
        cols = set()
        diag = set()
        off_diag = set()
        return backtrack(0)

# V1''
# IDEA : BACKTRACK
# https://leetcode.com/problems/n-queens-ii/submissions/812441270/
class Solution:
    def totalNQueens(self, n: int) -> int: 
        def backtrack(row, col, diag, adiag):
            if row == n: 
                return 1 
            
            solutions = 0  

            for i in range(n):
                if i in col or (row-i) in diag or (row+i) in adiag: 
                    continue
                
                col.add(i)
                diag.add(row-i)
                adiag.add(row+i)
                solutions += backtrack(row+1, col, diag, adiag)
                col.remove(i)
                diag.remove(row-i)
                adiag.remove(row+i)
            
            return solutions
                
        return backtrack(0, set(), set(), set())

# V2