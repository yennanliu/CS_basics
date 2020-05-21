# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79462285
# IDEA : DFS 
class Solution(object):
    def updateBoard(self, board, click):
        """
        :type board: List[List[str]]
        :type click: List[int]
        :rtype: List[List[str]]
        """
        (row, col), directions = click, ((-1, 0), (1, 0), (0, 1), (0, -1), (-1, 1), (-1, -1), (1, 1), (1, -1))
        if 0 <= row < len(board) and 0 <= col < len(board[0]):
            if board[row][col] == 'M':
                board[row][col] = 'X'
            elif board[row][col] == 'E':
                n = sum([board[row + r][col + c] == 'M' for r, c in directions if 0 <= row + r < len(board) and 0 <= col +c < len(board[0])])
                board[row][col] = str(n if n else 'B')
                if not n:
                    for r, c in directions:
                        self.updateBoard(board, [row + r, col + c])
        return board

# V1'
# http://bookshadow.com/weblog/2017/02/26/leetcode-minesweeper/
# IDEA : BFS 
class Solution(object):
    def updateBoard(self, board, click):
        """
        :type board: List[List[str]]
        :type click: List[int]
        :rtype: List[List[str]]
        """
        w, h = len(board), len(board[0])
        def countBoard(i, j):
            cnt = 0
            for di in (-1, 0, 1):
                for dj in (-1, 0, 1):
                    ni, nj = i + di, j + dj
                    if ni < 0 or ni >= w or nj < 0 or nj >= h:
                        continue
                    if board[ni][nj] == 'M':
                        cnt += 1
            return str(cnt) if cnt else 'B'
        cx, cy = click
        if board[cx][cy] == 'M':
            board[cx][cy] = 'X'
            return board
        q = [click]
        board[cx][cy] = countBoard(cx, cy)
        if board[cx][cy] != 'B':
            return board
        while q:
            ti, tj = q.pop(0)
            for di in (-1, 0, 1):
                for dj in (-1, 0, 1):
                    ni, nj = ti + di, tj + dj
                    if ni < 0 or ni >= w or nj < 0 or nj >= h:
                        continue
                    if board[ni][nj] == 'E':
                        board[ni][nj] = countBoard(ni, nj)
                        if board[ni][nj] == 'B':
                            q.append((ni, nj))
        return board

# V1''
# IDEA : BFS 
# https://leetcode.com/problems/minesweeper/discuss/284461/Python-BFS
class Solution(object):
    def updateBoard(self, board, click):
        """
        :type board: List[List[str]]
        :type click: List[int]
        :rtype: List[List[str]]
        """
        R, C = len(board), len(board[0])
        if board[click[0]][click[1]] == "M": board[click[0]][click[1]] = "X"
        dir = [1,0], [0,1], [-1,0],[0,-1],[1,1],[-1,-1],[1,-1],[-1,1]
        q = collections.deque()
        q.append(click)
        seen = set(click)
        
        def numBombsTangent(board, i, j):
            count = 0 
            for x, y in dir: 
                if 0 <= i + x < R and 0 <= j + y < C and board[i+x][y+j] == "M": count += 1 
            return count 

        while q: 
            for tup in range(len(q)):
                x, y = q.popleft()
                if board[x][y] == "E":
                    bombsNextTo = numBombsTangent(board, x, y)
                    board[x][y] = "B" if bombsNextTo == 0 else str(bombsNextTo)
                    if bombsNextTo == 0:
                        for a, b in dir:
                            if 0 <= a + x < R and 0 <= b + y < C and (a+x,b+y) not in seen:
                                q.append((a+x, b+y))
                                seen.add((a+x, b+y))
        return board 

 # V1'''
 # https://leetcode.com/problems/minesweeper/discuss/284461/Python-BFS
 # IDEA : DFS 
 class Solution:
    def updateBoard(self, board: List[List[str]], click: List[int]) -> List[List[str]]:
        x, y = click
        surround = [(-1, 0), (1, 0), (0, 1), (0, -1), (1, -1), (1, 1), (-1, 1), (-1, -1)]
        
        def available(x, y):
            return 0 <= x < len(board) and 0 <= y < len(board[0])
        
        def reveal(board, x, y):
            # reveal blank cell with dfs
            if not available(x, y) or board[x][y] != "E":
                return
            # count adjacent mines
            mine_count = 0
            for dx, dy in surround:
                if available(dx+x, dy+y) and board[dx+x][dy+y] == "M":
                    mine_count += 1
            if mine_count:
                # have mines in adjacent cells
                board[x][y] = str(mine_count)
            else:
                # not adjacent mines 
                board[x][y] = "B"
                for dx, dy in surround:
                    reveal(board, dx+x, dy+y)

        if board[x][y] == "M":
            board[x][y] = "X"
        elif board[x][y] == "E":
            reveal(board, x, y)
        return board

# V1''''
# https://leetcode.com/problems/minesweeper/discuss/144746/Python-BFS-%2B-DFS-with-comments
# IDEA : DFS, BFS 
class Solution:
    
    directions = [(-1, 0), (-1, -1), (-1, 1), (0,-1), (0, 1), (1, -1), (1, 0), (1, 1)]
    
    def updateBoard(self, board, click):
        """
        :type board: List[List[str]]
        :type click: List[int]
        :rtype: List[List[str]]
        """
        # return self.bfs(board, click)
        return self.dfs(board, click)
    
    def dfs(self, board, click):
        stack = [(click[0], click[1])]
        m, n = len(board), len(board[0])
        while stack:
            r, c = stack.pop() # last inserted element
            
            if board[r][c] == 'M':
                board[r][c] = 'X'
                break
            
            # check for adjacent mines
            mines = 0
            for i, j in self.directions:
                dr = r + i
                dc = c + j
                if 0 <= dr < m and 0 <= dc < n and board[dr][dc] == 'M':
                    mines += 1
            board[r][c] = str(mines) if mines else 'B'
            
            # add neighbors
            for i, j in self.directions:
                dr = r + i
                dc = c + j
                if 0 <= dr < m and 0 <= dc < n and board[r][c] == 'B' and board[dr][dc] == 'E':
                    stack.append((dr, dc))
            
        return board
        
    
    def bfs(self, board, click):
        queue = [(click[0], click[1])]
        m, n = len(board), len(board[0])
        while queue:
            r, c = queue.pop(0)
            
            if board[r][c] == 'M':
                board[r][c] = 'X'
                break
            
            # check for adjacent mines
            mines = 0
            for i, j in self.directions:
                dr = r + i
                dc = c + j
                if 0 <= dr < m and 0 <= dc < n and board[dr][dc] == 'M':
                    mines += 1
            board[r][c] = str(mines) if mines else 'B'
                
            # add neighbors
            for i, j in self.directions:
                dr = r + i
                dc = c + j
                # BFS could potentially add duplicate (i,j) to the queue so we check that (i,j) is not already in the queue
                if 0 <= dr < m and 0 <= dc < n and (dr,dc) not in queue and board[r][c] == 'B' and board[dr][dc] == 'E':
                    queue.append((dr, dc))
                
        return board
        
# V2