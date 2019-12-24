# V0

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

# 	public TicTacToe() {
# 		board = new char[3][3];
# 		initialize();
# 	}

# 	public char getCurrentPlayer() {
# 		return currentPlayerMark;
# 	}

# 	public void initialize() {
# 		gameEnd = false;
# 		currentPlayerMark = 'x';

# 		for (int i = 0; i < 3; i++) {
# 			for (int j = 0; j < 3; j++) {
# 				board[i][j] = '-';
# 			}
# 		}
# 	}

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

# 	public void changePlayer() {
# 		if (currentPlayerMark == 'x')
# 			currentPlayerMark = 'o';
# 		else
# 			currentPlayerMark = 'x';

# 	}

# 	// true means this move wins the game, false means otherwise
# 	public boolean move(int row, int col) throws AlreadyTakenException, GameEndException {

# 		if (gameEnd) {
# 			throw new GameEndException();
# 		}

# 		if (board[row][col] != '-') {
# 			throw new AlreadyTakenException();
# 		}

# 		board[row][col] = currentPlayerMark;

# 		boolean win;

# 		//check row
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[row][i] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}

# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}

# 		//check column
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][col] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}

# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}

# 		//check back diagonal
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][i] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}

# 		if (win) {
# 			gameEnd = true;
# 			return win;
# 		}

# 		//check forward diagonal
# 		win = true;
# 		for (int i = 0; i < board.length; i++) {
# 			if (board[i][board.length - i - 1] != currentPlayerMark) {
# 				win = false;
# 				break;
# 			}
# 		}

# 		if (win) {
# 			gameEnd = true;
#             return win;
# 		}
# 		changePlayer();
# 		return win;
# 	}
# }


# class GameEndException extends Exception{
# 	public GameEndException()
# 	{
# 		super("Game has been ended, cannot make any more moves");
# 	}
# }

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