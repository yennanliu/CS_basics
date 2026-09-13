"""

794. Valid Tic-Tac-Toe State
Medium

Given a Tic-Tac-Toe board as a string array board, return true if and only if it is possible to reach this board position during the course of a valid tic-tac-toe game.

The board is a 3 x 3 array that consists of characters ' ', 'X', and 'O'. The ' ' character represents an empty square.

Here are the rules of Tic-Tac-Toe:

Players take turns placing characters into empty squares ' '.
The first player always places 'X' characters, while the second player always places 'O' characters.
'X' and 'O' characters are always placed into empty squares, never filled ones.
The game ends when there are three of the same (non-empty) character filling any row, column, or diagonal.
The game also ends if all squares are non-empty.
No more moves can be played if the game is over.

Example 1:

https://assets.leetcode.com/uploads/2021/05/15/tictactoe1-grid.jpg

Input: board = ["O  ","   ","   "]
Output: false
Explanation: The first player always plays "X".

Example 2:

https://assets.leetcode.com/uploads/2021/05/15/tictactoe2-grid.jpg

Input: board = ["XOX"," X ","   "]
Output: false
Explanation: Players take turns making moves.

Example 3:

https://assets.leetcode.com/uploads/2021/05/15/tictactoe4-grid.jpg

Input: board = ["XOX","O O","XOX"]
Output: true

Constraints:

board.length == 3
board[i].length == 3
board[i][j] is either 'X', 'O', or ' '.

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2018/03/04/leetcode-valid-tic-tac-toe-state/
# time = O(1)
# space = O(1)
class Solution(object):
    def validTicTacToe(self, board):
        """
        :type board: List[str]
        :rtype: bool
        """
        nx = ''.join(board).count('X')
        no = ''.join(board).count('O')
        wx, wo = self.isWin(board, 'X'), self.isWin(board, 'O')
        if wx: return nx == no + 1 and not wo
        if wo: return nx == no
        return nx - 1 <= no <= nx

    def isWin(self, board, pc):
        if any(r == pc * 3 for r in board): return True
        if any(c == pc * 3 for c in zip(*board)): return True
        if board[0][0] == board[1][1] == board[2][2] == pc: return True
        if board[0][2] == board[1][1] == board[2][0] == pc: return True
        return False
        
# V2 
# time = O(1)
# space = O(1)
class Solution(object):
    def validTicTacToe(self, board):
        """
        :type board: List[str]
        :rtype: bool
        """
        def win(board, player):
            for i in range(3):
                if all(board[i][j] == player for j in range(3)):
                    return True
                if all(board[j][i] == player for j in range(3)):
                    return True

            return (player == board[1][1] == board[0][0] == board[2][2] or \
                    player == board[1][1] == board[0][2] == board[2][0])

        FIRST, SECOND = ('X', 'O')
        x_count = sum(row.count(FIRST) for row in board)
        o_count = sum(row.count(SECOND) for row in board)
        if o_count not in {x_count-1, x_count}: return False
        if win(board, FIRST) and x_count-1 != o_count: return False
        if win(board, SECOND) and x_count != o_count: return False
        return True
