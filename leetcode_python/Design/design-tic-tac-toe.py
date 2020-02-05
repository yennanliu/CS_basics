# V0
# https://github.com/yennanliu/utility_Python/blob/master/game/tic_tac_toe.py
class TicTacToe:

    def __init__(self):
        self.matrix = [ [None]*3 for _ in range(3) ]

    def check_x_axis(self):
        # x axis
        for i in range(len(self.matrix)):
            if (self.matrix[i].count("x") == 3 ):
                print (" {} win the game".format("x"))
                return True, "x"

            elif (self.matrix[i].count("y") == 3 ):
                print (" {} win the game".format("y"))
                return True, "y"

    def check_y_axis(self):
        # y axis
        for j in range(len(self.matrix[1])):
            count_x, count_y = 0, 0 
            for i_ in range(len(self.matrix)):
                if self.matrix[i_][j] == 'x':
                    count_x += 1 
                elif self.matrix[i_][j] == 'y':
                    count_y += 1 
                if count_x == 3:
                    return True, "x"
                elif count_y == 3:
                    return True, "y"

    def check_left_diagonal(self):
        # left diagonal  
        sum_of_left_d_X = sum([self.matrix[i][i] == "x" for i in range(len(self.matrix))])
        sum_of_left_d_y = sum([self.matrix[i][i] == "y" for i in range(len(self.matrix))])

        if sum_of_left_d_X == 3:
            return True, "x"
        elif sum_of_left_d_y == 3:
            return True, "y"

    def check_right_diagonal(self):
        # right diagonal
        sum_of_left_d_X = sum([self.matrix[i][len(self.matrix)-i-1] == "x" for i in range(len(self.matrix))])
        sum_of_left_d_y = sum([self.matrix[i][len(self.matrix)-i-1] == "y" for i in range(len(self.matrix))])

        if sum_of_left_d_X == 3:
            return True, "x"
        elif sum_of_left_d_y == 3:
            return True, "y"

    def check_win(self):
        if self.check_x_axis():
            return self.check_x_axis()

        elif self.check_y_axis():
            return self.check_y_axis()

        elif self.check_left_diagonal():
            return self.check_left_diagonal()

        elif self.check_right_diagonal():
            return self.check_right_diagonal()

        #elif self.check_occupied():
        #    return (False, "even")

    def check_occupied(self):
        for i in range(len(self.matrix)):
            if None in self.matrix[i]:
                return False
        return True

    def operate_game(self):
        # start from "x", then "o". so the process is : x -> o -> x....
        while ( not self.check_win()) and ( not self.check_occupied() ):

            input_x = input("plz input x : ")
            input_y = input("plz input y : ")
            x, y = int(input_x), int(input_y)

            print ("x, y = ", x, y) 
        
            round_ = 1 
            if x < 0 or x > 3 or y < 0 or y > 3:
                print ("out of range, plz select x, y again")

            if round_ % 2 == 1:
                icon = "x"
                round_ += 1 

            elif round_ % 2 == 1:
                icon = "o"
                round_ += 1 

            print ("player put {} at [{}, {}]".format(icon, x, y))
            self.matrix[x][y] = icon
            print ("current matrix :", self.matrix)
            print ("self.check_win() ", self.check_win())

        if self.check_win():
            result =  self.check_win()
            print (" {} win the game".format(result[1]))
            return "{} win!".format(result[1])
        else:
            return "even"
            
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