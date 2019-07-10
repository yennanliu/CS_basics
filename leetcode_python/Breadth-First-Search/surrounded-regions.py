# V0 

# V1 
# https://blog.csdn.net/danspace1/article/details/88010210
class Solution(object):
    def solve(self, board):
        """
        :type board: List[List[str]]
        :rtype: None Do not return anything, modify board in-place instead.
        """
        if not board: return
        row, col = len(board), len(board[0])
        q = collections.deque()
        
        # get the index of all O on the boarder
        for r in range(row):
            for c in range(col):
                if r in [0, row-1] or c in [0, col-1] and board[r][c]=='O':
                    q.append((r,c))
                    
        # bfs, make the adjacent O into D
        while q:
            x, y = q.popleft()
            if 0 <= x < row and 0 <= y < col and board[x][y] == 'O':
                board[x][y] = 'D'              
                for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:                
                    q.append((x+dx, y+dy))
                    
        # make the rest of O into X and make the D into 0
        for r in range(row):
            for c in range(col):
                if board[r][c] == 'O':
                    board[r][c] = 'X'
                elif board[r][c] == 'D':
                    board[r][c] = 'O'

# V2 
# Time:  O(m * n)
# Space: O(m + n)
import collections
class Solution(object):
    def solve(self, board):
        """
        :type board: List[List[str]]
        :rtype: void Do not return anything, modify board in-place instead.
        """
        if not board:
            return

        q = collections.deque()

        for i in range(len(board)):
            if board[i][0] == 'O':
                board[i][0] = 'V'
                q.append((i, 0))
            if board[i][len(board[0])-1] == 'O':
                board[i][len(board[0])-1] = 'V'
                q.append((i, len(board[0])-1))

        for j in range(1, len(board[0])-1):
            if board[0][j] == 'O':
                board[0][j] = 'V'
                q.append((0, j))
            if board[len(board)-1][j] == 'O':
                board[len(board)-1][j] = 'V'
                q.append((len(board)-1, j))

        while q:
            i, j = q.popleft()
            for x, y in [(i+1, j), (i-1, j), (i, j+1), (i, j-1)]:
                if 0 <= x < len(board) and 0 <= y < len(board[0]) and \
                   board[x][y] == 'O':
                    board[x][y] = 'V'
                    q.append((x, y))

        for i in range(len(board)):
            for j in range(len(board[0])):
                if board[i][j] != 'V':
                    board[i][j] = 'X'
                else:
                    board[i][j] = 'O'