"""

348. Design Tic-Tac-Toe
Medium

Assume the following rules are for the tic-tac-toe game on an n x n board between two players:

A move is guaranteed to be valid and is placed on an empty block.
Once a winning condition is reached, no more moves are allowed.
A player who succeeds in placing n of their marks in a horizontal, vertical, or diagonal row wins the game.
Implement the TicTacToe class:

TicTacToe(int n) Initializes the object the size of the board n.
int move(int row, int col, int player) Indicates that the player with id player plays at the cell (row, col) of the board. The move is guaranteed to be a valid move.
 

Example 1:

Input
["TicTacToe", "move", "move", "move", "move", "move", "move", "move"]
[[3], [0, 0, 1], [0, 2, 2], [2, 2, 1], [1, 1, 2], [2, 0, 1], [1, 0, 2], [2, 1, 1]]
Output
[null, 0, 0, 0, 0, 0, 0, 1]

Explanation
TicTacToe ticTacToe = new TicTacToe(3);
Assume that player 1 is "X" and player 2 is "O" in the board.
ticTacToe.move(0, 0, 1); // return 0 (no one wins)
|X| | |
| | | |    // Player 1 makes a move at (0, 0).
| | | |

ticTacToe.move(0, 2, 2); // return 0 (no one wins)
|X| |O|
| | | |    // Player 2 makes a move at (0, 2).
| | | |

ticTacToe.move(2, 2, 1); // return 0 (no one wins)
|X| |O|
| | | |    // Player 1 makes a move at (2, 2).
| | |X|

ticTacToe.move(1, 1, 2); // return 0 (no one wins)
|X| |O|
| |O| |    // Player 2 makes a move at (1, 1).
| | |X|

ticTacToe.move(2, 0, 1); // return 0 (no one wins)
|X| |O|
| |O| |    // Player 1 makes a move at (2, 0).
|X| |X|

ticTacToe.move(1, 0, 2); // return 0 (no one wins)
|X| |O|
|O|O| |    // Player 2 makes a move at (1, 0).
|X| |X|

ticTacToe.move(2, 1, 1); // return 1 (player 1 wins)
|X| |O|
|O|O| |    // Player 1 makes a move at (2, 1).
|X|X|X|
 

Constraints:

2 <= n <= 100
player is 1 or 2.
0 <= row, col < n
(row, col) are unique for each different call to move.
At most n2 calls will be made to move.
 

Follow-up: Could you do better than O(n2) per move() operation?

"""

# V0
# https://github.com/yennanliu/utility_Python/blob/master/game/tic_tac_toe.py
class TicTacToe:

    def __init__(self, n):
        """
        Initialize your data structure here.
        :type n: int
        """
        self.grid = [[' ']*n for i in range(n)]       
        

    def move(self, row, col, player):
        """
        Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins.
        :type row: int
        :type col: int
        :type player: int
        :rtype: int
        """
        if player == 1:
            mark = 'X'
        else:
            mark = 'O'
            
        self.grid[row][col] = mark
        # check wining condition
        # check if the row has the same mark
        n = len(self.grid)
        sum_of_row = sum([self.grid[row][c] == mark for c in range(n)])
        sum_of_col = sum([self.grid[r][col]== mark for r in range(n)])
        sum_of_left_d = sum([self.grid[i][i] == mark for i in range(n)])
        sum_of_right_d = sum([self.grid[i][n-1-i] == mark for i in range(n)])
        if sum_of_row == n or sum_of_col == n or sum_of_left_d== n or sum_of_right_d == n:
            return player        
        else:
            return 0
            
# t_game = TicTacToe()
# t_game.operate_game()

# V1
# https://blog.csdn.net/danspace1/article/details/86616981
class TicTacToe:

    def __init__(self, n):
        """
        Initialize your data structure here.
        :type n: int
        """
        self.grid = [[' ']*n for i in range(n)]       
        

    def move(self, row, col, player):
        """
        Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins.
        :type row: int
        :type col: int
        :type player: int
        :rtype: int
        """
        if player == 1:
            mark = 'X'
        else:
            mark = 'O'
            
        self.grid[row][col] = mark
        # check wining condition
        # check if the row has the same mark
        n = len(self.grid)
        sum_of_row = sum([self.grid[row][c] == mark for c in range(n)])
        sum_of_col = sum([self.grid[r][col]== mark for r in range(n)])
        sum_of_left_d = sum([self.grid[i][i] == mark for i in range(n)])
        sum_of_right_d = sum([self.grid[i][n-1-i] == mark for i in range(n)])
        if sum_of_row == n or sum_of_col == n or sum_of_left_d== n or sum_of_right_d == n:
            return player        
        else:
            return 0


# V1
# IDEA : Optimized Brute Force
# https://leetcode.com/problems/design-tic-tac-toe/solution/
# JAVA
# class TicTacToe {
#
#     private int[][] board;
#     private int n;
#
#     public TicTacToe(int n) {
#         board = new int[n][n];
#         this.n = n;
#     }
#
#     public int move(int row, int col, int player) {
#         board[row][col] = player;
#         // check if the player wins
#         if ((checkRow(row, player)) ||
#             (checkColumn(col, player)) ||
#             (row == col && checkDiagonal(player)) ||
#             (col == n - row - 1 && checkAntiDiagonal(player))) {
#             return player;
#         }
#         // No one wins
#         return 0;
#     }
#
#     private boolean checkDiagonal(int player) {
#         for (int row = 0; row < n; row++) {
#             if (board[row][row] != player) {
#                 return false;
#             }
#         }
#         return true;
#     }
#
#     private boolean checkAntiDiagonal(int player) {
#         for (int row = 0; row < n; row++) {
#             if (board[row][n - row - 1] != player) {
#                 return false;
#             }
#         }
#         return true;
#     }
#
#     private boolean checkColumn(int col, int player) {
#         for (int row = 0; row < n; row++) {
#             if (board[row][col] != player) {
#                 return false;
#             }
#         }
#         return true;
#     }
#
#     private boolean checkRow(int row, int player) {
#         for (int col = 0; col < n; col++) {
#             if (board[row][col] != player) {
#                 return false;
#             }
#         }
#         return true;
#     }
# }


# V1
# IDEA : Optimised Approach
# https://leetcode.com/problems/design-tic-tac-toe/solution/
# public class TicTacToe {
#     int[] rows;
#     int[] cols;
#     int diagonal;
#     int antiDiagonal;
#
#     public TicTacToe(int n) {
#         rows = new int[n];
#         cols = new int[n];
#     }
#
#     public int move(int row, int col, int player) {
#         int currentPlayer = (player == 1) ? 1 : -1;
#         // update currentPlayer in rows and cols arrays
#         rows[row] += currentPlayer;
#         cols[col] += currentPlayer;
#         // update diagonal
#         if (row == col) {
#             diagonal += currentPlayer;
#         }
#         //update anti diagonal
#         if (col == (cols.length - row - 1)) {
#             antiDiagonal += currentPlayer;
#         }
#         int n = rows.length;
#         // check if the current player wins
#         if (Math.abs(rows[row]) == n ||
#                 Math.abs(cols[col]) == n ||
#                 Math.abs(diagonal) == n ||
#                 Math.abs(antiDiagonal) == n) {
#             return player;
#         }
#         // No one wins
#         return 0;
#     }
# }


# V1''
# https://blog.csdn.net/danspace1/article/details/86616981
class TicTacToe:

    def __init__(self, n):
        """
        Initialize your data structure here.
        :type n: int
        """
        self.row, self.col, self.diag1, self.diag2, self.n = [0]*n, [0]*n, 0, 0, n 
        

    def move(self, row, col, player):
        """
        Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins.
        :type row: int
        :type col: int
        :type player: int
        :rtype: int
        """
        if player == 1:
            offset = 1
        else:
            offset = -1
            
        self.row[row] += offset
        self.col[col] += offset
        if row == col:
            self.diag1 += offset
        if row + col == self.n-1:
            self.diag2 += offset
            
        if self.n in [self.row[row], self.col[col], self.diag1, self.diag2]:
            return 1
        if -self.n in [self.row[row], self.col[col], self.diag1, self.diag2]:
            return 2
        return 0

# V1'
# https://www.jiuzhang.com/solution/design-tic-tac-toe/
# JAVA
# public class TicTacToe {
#     private char[][] board;
# 	private char currentPlayerMark;
# 	private boolean gameEnd;
#
# 	public TicTacToe() {
# 		board = new char[3][3];
# 		initialize();
# 	}
#
# 	public char getCurrentPlayer() {
# 		return currentPlayerMark;
# 	}
#
# 	public void initialize() {
# 		gameEnd = false;
# 		currentPlayerMark = 'x';
#
# 		for (int i = 0; i < 3; i++) {
# 			for (int j = 0; j < 3; j++) {
# 				board[i][j] = '-';
# 			}
# 		}
# 	}
#
# 	public boolean isBoardFull() {
# 		for (int i = 0; i < 3; i++) {
# 			for (int j = 0; j < 3; j++) {
# 				if (board[i][j] == '-') {
# 					return false;
# 				}
# 			}
# 		}
# 		gameEnd = true;
# 		return true;
# 	}
#
# 	public void changePlayer() {
# 		if (currentPlayerMark == 'x')
# 			currentPlayerMark = 'o';
# 		else
# 			currentPlayerMark = 'x';
#
# 	}
#
# 	// true means this move wins the game, false means otherwise
# 	public boolean move(int row, int col) throws AlreadyTakenException, GameEndException {
#
# 		if (gameEnd) {
# 			throw new GameEndException();
# 		}
#
# 		if (board[row][col] != '-') {
# 			throw new AlreadyTakenException();
# 		}
#
# 		board[row][col] = currentPlayerMark;
#
# 		boolean win;
#
# 		//check row
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[row][i] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}
#
# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}
#
# 		//check column
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][col] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}
#
# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}
#
# 		//check back diagonal
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][i] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}
#
# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}
#
# 		//check forward diagonal
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][board.length - i - 1] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}
#
# 		if (win) {
# 			gameEnd = true;
#             return win;
# 		}
# 		changePlayer();
# 		return win;
# 	}
# }
#
#
# class GameEndException extends Exception{
# 	public GameEndException()
# 	{
# 		super("Game has been ended, cannot make any more moves");
# 	}
# }
#
# class AlreadyTakenException extends Exception {
# 	public AlreadyTakenException()
# 	{
# 		super("This place has been taken");
# 	}
# }

# V2
# Time:  O(1), per move.
# Space: O(n^2)
class TicTacToe(object):

    def __init__(self, n):
        """
        Initialize your data structure here.
        :type n: int
        """
        self.__size = n
        self.__rows = [[0, 0] for _ in xrange(n)]
        self.__cols = [[0, 0] for _ in xrange(n)]
        self.__diagonal = [0, 0]
        self.__anti_diagonal = [0, 0]

    def move(self, row, col, player):
        """
        Player {player} makes a move at ({row}, {col}).
        @param row The row of the board.
        @param col The column of the board.
        @param player The player, can be either 1 or 2.
        @return The current winning condition, can be either:
                0: No one wins.
                1: Player 1 wins.
                2: Player 2 wins.
        :type row: int
        :type col: int
        :type player: int
        :rtype: int
        """
        i = player - 1
        self.__rows[row][i] += 1
        self.__cols[col][i] += 1
        if row == col:
            self.__diagonal[i] += 1
        if col == len(self.__rows) - row - 1:
            self.__anti_diagonal[i] += 1
        if any(self.__rows[row][i] == self.__size,
               self.__cols[col][i] == self.__size,
               self.__diagonal[i] == self.__size,
               self.__anti_diagonal[i] == self.__size):
            return player
        return 0