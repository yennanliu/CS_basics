# V0 
# IDEA : BFS
class Solution(object):
    def solve(self, board):
        import collections 
        if not board: return 
        l, w = len(board), len(board[0])
        q=collections.deque()

        # get the index of all O on the boarder
        for i in range(l):
            for j in range(w):
                if i in [0, l-1] or j in [0, w-1] and board[i][j] == "O":
                    q.append((i,j))

        # bfs, make the adjacent O into D
        while q:
            x,y = q.popleft()
            if  0 <= x < l and 0 <= y < w and board[x][y] == "O":
                board[x][y] = 'D' 
                for dx, dy in [(0,1), (0,-1),(1,0),(-1,0)]:
                    q.append((x+dx,y+dy))

        # make the rest of O into X and make the D into 0
        for i in range(l):
            for j in range(w):
                if board[i][j] == "O":
                    board[i][j] = "X"
                elif board[i][j] == "D":
                    board[i][j] = "O"

        return board

# V1 
# https://blog.csdn.net/danspace1/article/details/88010210
# IDEA : BFS
# PROCESS : 3 STEPS 
# STEP 1) FIND ALL "O" AT BOUNDARY, COLLECT THEIR INDEX(x,y)
# STEP 2) GO THROUGH (bfs) ALL BOUNDARY "0" MAKE THEM AND ALL THEIR CONNECTION "O" AS "D". i.e. "O" -> "D"
# STEP 3) GO THROUGH ALL REST OF "O" (NOT EQUAL TO "D" yet) AS "X"
# STEP 4) GO THROUGH ALL "D" AND MAKE THEM TO "O"
class Solution(object):
    def solve(self, board):
        import collections 
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
        # for testing
        return board

### Test case
s=Solution()
assert s.solve([[]]) == [[]]
assert s.solve([["X"]]) == [["X"]]
assert s.solve([["O"]]) == [["O"]]
assert s.solve([["O","O","O"]]) == [["O","O","O"]]
assert s.solve([["X","O","O"]]) == [["X","O","O"]]
assert s.solve([["X","X","O"]]) == [["X","X","O"]]
assert s.solve([["X","X","O"],["X","X","X"]]) == [["X","X","O"],["X","X","X"]]
assert s.solve([["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]) ==  [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
assert s.solve([["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","X","X","X"]]) ==  [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","X","X","X"]]

# V1'
# https://leetcode.com/problems/surrounded-regions/discuss/475014/python3-BFS-and-DFS
# IDEA : DFS
class Solution:
    #recursion, dfs
    def solve(self, board: List[List[str]]) -> None:
        """
        Do not return anything, modify board in-place instead.
        """
        if not board or not board[0]:
            return
        R, C = len(board), len(board[0])
        if R <= 2 or C <= 2:
            return
        
        # start from the boarder and replace all O to N
        # put all the boarder value into queue.
        for r in range(R):
            self.dfs(board, r, 0, R, C)
            self.dfs(board, r, C-1, R, C)

        for c in range(C):
            self.dfs(board, 0, c, R, C)
            self.dfs(board, R-1, c, R, C)

        # replace all the O to X, then replace all the N to O
        for r in range(R):
            for c in range(C):
                if board[r][c] == "O":
                    board[r][c] = "X"
                if board[r][c] == "N":
                    board[r][c] = "O"
        
                    
    def dfs(self, board, r, c, R, C):
        if 0<=r<R and 0<=c<C and board[r][c] == "O":
            board[r][c] = "N"
            self.dfs(board, r, c+1, R, C)
            self.dfs(board, r, c-1, R, C)            
            self.dfs(board, r-1, c, R, C)            
            self.dfs(board, r+1, c, R, C)  

# V1''
# https://leetcode.com/problems/surrounded-regions/discuss/475014/python3-BFS-and-DFS
# IDEA : BFS 
class Solution:
    '''
    Time complexity : O(MXN)
    Space complexity : O(1)

    First, check the four border of the matrix. If there is a element is
    'O', alter it and all its neighbor 'O' elements to 'N'.

    Then ,alter all the 'O' to 'X'

    At last,alter all the 'N' to 'O'

    example: 

    X X X X           X X X X             X X X X
    X X O X  ->       X X O X    ->       X X X X
    X O X X           X N X X             X O X X
    X O X X           X N X X             X O X X
    '''
    def solve(self, board: List[List[str]]) -> None:
        """
        Do not return anything, modify board in-place instead.
        """
        if not board or not board[0]:
            return
        R, C = len(board), len(board[0])
        if R <= 2 or C <= 2:
            return
        
        # queue for bfs
        q = deque()
        
        # start from the boarder and replace all O to N
        # put all the boarder value into queue.
        for r in range(R):
            q.append((r, 0))
            q.append((r, C-1))

        for c in range(C):
            q.append((0, c))
            q.append((R-1, c))
        
        while q:
            r, c = q.popleft()
            if 0<=r<R and 0<=c<C and board[r][c] == "O":
                # modify the value from O to N
                board[r][c] = "N"
                # append the surrouding cells to queue.
                q.append((r, c+1))
                q.append((r, c-1))
                q.append((r-1, c))
                q.append((r+1, c))
        
        # replace all the O to X, then replace all the N to O
        for r in range(R):
            for c in range(C):
                if board[r][c] == "O":
                    board[r][c] = "X"
                if board[r][c] == "N":
                    board[r][c] = "O"

# V1'''
# https://www.jiuzhang.com/solution/surrounded-regions/#tag-highlight-lang-python
class Solution:
    # @param {list[list[str]]} board a 2D board containing 'X' and 'O'
    # @return nothing 
    def surroundedRegions(self, board):
        # Write your code here
        if not any(board):
            return

        n, m = len(board), len(board[0])
        q = [ij for k in range(max(n,m)) for ij in ((0, k), (n-1, k), (k, 0), (k, m-1))]
        while q:
            i, j = q.pop()
            if 0 <= i < n and 0 <= j < m and board[i][j] == 'O':
                board[i][j] = 'W'
                q += (i, j-1), (i, j+1), (i-1, j), (i+1, j)

        board[:] = [['XO'[c == 'W'] for c in row] for row in board]

# V1''''
# https://www.jiuzhang.com/solution/surrounded-regions/#tag-highlight-lang-python
class Solution:
    # @param {list[list[str]]} board a 2D board containing 'X' and 'O'
    # @return nothing
    def surroundedRegions(self, board):
        def fill(x, y):
            if x < 0 or x > m-1 or y < 0 or y > n-1 or board[x][y] != 'O':
                return
            queue.append((x, y))
            board[x][y] = 'D'

        def bfs(x, y):
            if board[x][y] == 'O':
                queue.append((x, y))
                fill(x, y)

            while queue:
                curr = queue.pop(0)
                i, j = curr[0], curr[1]
                fill(i+1, j)
                fill(i-1, j)
                fill(i, j+1)
                fill(i, j-1)

        if len(board) == 0:
            return
        m, n, queue = len(board), len(board[0]), []
        for i in range(n):
            bfs(0, i)
            bfs(m-1, i)

        for j in range(1, m-1):
            bfs(j, 0)
            bfs(j, n-1)

        for i in range(m):
            for j in range(n):
                if board[i][j] == 'D':
                    board[i][j] = 'O'
                elif board[i][j] == 'O':
                    board[i][j] = 'X'
                    
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