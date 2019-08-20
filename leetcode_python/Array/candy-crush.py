# V0 

# V1 
# http://bookshadow.com/weblog/2017/11/05/leetcode-candy-crush/
class Solution(object):
    def candyCrush(self, board):
        """
        :type board: List[List[int]]
        :rtype: List[List[int]]
        """
        w, h = len(board), len(board[0])
        stop = False
        while not stop:
            stop = True
            for x in range(w):
                y = z = 0
                while y < h:
                    z = y + 1
                    while z < h and board[x][z] and abs(board[x][z]) == abs(board[x][y]):
                        z += 1
                    if z - y > 2:
                        stop = False
                        while y < z:
                            board[x][y] = -abs(board[x][y])
                            y += 1
                    y = z
            for y in range(h):
                x = z= 0
                while x < w:
                    z = x + 1
                    while z < w and board[z][y] and abs(board[z][y]) == abs(board[x][y]):
                        z += 1
                    if z - x > 2:
                        stop = False
                        while x < z:
                            board[x][y] = -abs(board[x][y])
                            x += 1
                    x = z
            for y in range(h):
                x = z = w - 1
                while z >= 0:
                    if board[z][y] > 0:
                        board[x][y] = board[z][y]
                        x -= 1
                    z -= 1
                while x >= 0:
                    board[x][y] = 0
                    x -= 1
        return board
        
# V2 
# Time:  O((R * C)^2)
# Space: O(1)
class Solution(object):
    def candyCrush(self, board):
        """
        :type board: List[List[int]]
        :rtype: List[List[int]]
        """
        R, C = len(board), len(board[0])
        changed = True

        while changed:
            changed = False

            for r in range(R):
                for c in range(C-2):
                    if abs(board[r][c]) == abs(board[r][c+1]) == abs(board[r][c+2]) != 0:
                        board[r][c] = board[r][c+1] = board[r][c+2] = -abs(board[r][c])
                        changed = True

            for r in range(R-2):
                for c in range(C):
                    if abs(board[r][c]) == abs(board[r+1][c]) == abs(board[r+2][c]) != 0:
                        board[r][c] = board[r+1][c] = board[r+2][c] = -abs(board[r][c])
                        changed = True

            for c in range(C):
                i = R-1
                for r in reversed(range(R)):
                    if board[r][c] > 0:
                        board[i][c] = board[r][c]
                        i -= 1
                for r in reversed(range(i+1)):
                    board[r][c] = 0

        return board