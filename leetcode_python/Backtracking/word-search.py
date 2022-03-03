"""

Given an m x n grid of characters board and a string word, return true if word exists in the grid.

The word can be constructed from letters of sequentially adjacent cells,
where adjacent cells are horizontally or vertically neighboring. 
The same letter cell may not be used more than once.

 

Example 1:


Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
Output: true

Example 2:


Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
Output: true

Example 3:


Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
Output: false
 

Constraints:

m == board.length
n = board[i].length
1 <= m, n <= 6
1 <= word.length <= 15
board and word consists of only lowercase and uppercase English letters.

"""

# V0
# IDEA : DFS + backtracking
class Solution(object):
 
    def exist(self, board, word):
        ### NOTE : construct the visited matrix
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        """
        NOTE !!!! : we visit every element in board and trigger the dfs
        """
        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.dfs(board, word, 0, i, j, visited):
                    return True

        return False

    def dfs(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        ### NOTE this condition
        # 1) if idx out of range
        # 2) if already visited
        # 3) if board[i][j] != word[cur] -> not possible to be as same as word
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # NOTE THIS !! : mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.dfs(board, word, cur + 1, i + 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i - 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i, j + 1, visited) or\
                 self.dfs(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
       
# V0'
# IDEA : DFS
class Solution(object):
    def exist(self, board, word):
        """
        :type board: List[List[str]]
        :type word: str
        :rtype: bool
        """
        for y in range(len(board)):
            for x in range(len(board[0])):
                if self.dfs(board, word, x, y, 0):
                    return True
        return False
    
    def dfs(self, board, word, x, y, i):
        if i == len(word):
            return True
        if x < 0 or x >= len(board[0]) or y < 0 or y >= len(board):
            return False
        if board[y][x] != word[i]:
            return False
        board[y][x] = board[y][x].swapcase() # to mark if the route already been passed (.swapcase(), e.g. A->a)
        isExit =  self.dfs(board, word, x + 1, y, i + 1) or self.dfs(board, word, x, y + 1, i + 1) or self.dfs(board, word, x - 1, y, i + 1) or self.dfs(board, word, x, y - 1, i + 1)
        board[y][x] = board[y][x].swapcase() # if already visited all possible route within the route collection, then roll back the maked route (.swapcase(), e.g. a->A), and run the other visit again 
        return isExit

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79386066
# IDEA : BACKTRACKING 
# DEMO : 
# In [39]: 'ACV'.swapcase()
# Out[39]: 'acv'
# In [41]: 'wfwwrgergewCEVER'.swapcase()
# Out[41]: 'WFWWRGERGEWcever'
class Solution(object):
    def exist(self, board, word):
        """
        :type board: List[List[str]]
        :type word: str
        :rtype: bool
        """
        for y in range(len(board)):
            for x in range(len(board[0])):
                if self.exit(board, word, x, y, 0):
                    return True
        return False
    
    def exit(self, board, word, x, y, i):
        if i == len(word):
            return True
        if x < 0 or x >= len(board[0]) or y < 0 or y >= len(board):
            return False
        if board[y][x] != word[i]:
            return False
        board[y][x] = board[y][x].swapcase() # to mark if the route already been passed (.swapcase(), e.g. A->a)
        isexit =  self.exit(board, word, x + 1, y, i + 1) or self.exit(board, word, x, y + 1, i + 1) or self.exit(board, word, x - 1, y, i + 1) or self.exit(board, word, x, y - 1, i + 1)
        board[y][x] = board[y][x].swapcase() # if already visited all possible route within the route collection, then roll back the maked route (.swapcase(), e.g. a->A), and run the other visit again 
        return isexit

# V1'
# https://www.cnblogs.com/zuoyuan/p/3769767.html
# IDEA : DFS 
class Solution:
    # @param board, a list of lists of 1 length string
    # @param word, a string
    # @return a boolean
    def exist(self, board, word):
        def dfs(x, y, word):
            if len(word)==0: return True
            #left
            if x>0 and board[x-1][y]==word[0]:
                tmp=board[x][y]; board[x][y]='#'
                if dfs(x-1,y,word[1:]):
                    return True
                board[x][y]=tmp
            #right
            if x<len(board)-1 and board[x+1][y]==word[0]:
                tmp=board[x][y]; board[x][y]='#'
                if dfs(x+1,y,word[1:]):
                    return True
                board[x][y]=tmp
            #down
            if y>0 and board[x][y-1]==word[0]:
                tmp=board[x][y]; board[x][y]='#'
                if dfs(x,y-1,word[1:]):
                    return True
                board[x][y]=tmp
            #up
            if y<len(board[0])-1 and board[x][y+1]==word[0]:
                tmp=board[x][y]; board[x][y]='#'
                if dfs(x,y+1,word[1:]):
                    return True
                board[x][y]=tmp
            return False
                
        for i in range(len(board)):
            for j in range(len(board[0])):
                if board[i][j]==word[0]:
                    if(dfs(i,j,word[1:])):
                        return True
        return False

# V1''
# https://www.jiuzhang.com/solution/word-search/#tag-highlight-lang-python
class Solution:
    # @param board, a list of lists of 1 length string
    # @param word, a string
    # @return a boolean
    def exist(self, board, word):
        # write your code here
        # Boundary Condition
        if word == []:
            return True
        m = len(board)
        if m == 0:
            return False
        n = len(board[0])
        if n == 0:
            return False
        # Visited Matrix
        visited = [[False for j in range(n)] for i in range(m)]
        # DFS
        for i in range(m):
            for j in range(n):
                if self.exist2(board, word, visited, i, j):
                    return True
        return False

    def exist2(self, board, word, visited, row, col):
        if word == '':
            return True
        m, n = len(board), len(board[0])
        if row < 0 or row >= m or col < 0 or col >= n:
            return False
        if board[row][col] == word[0] and not visited[row][col]:
            visited[row][col] = True
            # row - 1, col
            if self.exist2(board, word[1:], visited, row - 1, col) or self.exist2(board, word[1:], visited, row, col - 1) or self.exist2(board, word[1:], visited, row + 1, col) or self.exist2(board, word[1:], visited, row, col + 1):
                return True
            else:
                visited[row][col] = False
        return False

# V2
# Time:  O(m * n * l)
# Space: O(l)
class Solution(object):
    # @param board, a list of lists of 1 length string
    # @param word, a string
    # @return a boolean
    def exist(self, board, word):
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.existRecu(board, word, 0, i, j, visited):
                    return True

        return False

    def existRecu(self, board, word, cur, i, j, visited):
        if cur == len(word):
            return True

        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        visited[i][j] = True
        result = self.existRecu(board, word, cur + 1, i + 1, j, visited) or\
                 self.existRecu(board, word, cur + 1, i - 1, j, visited) or\
                 self.existRecu(board, word, cur + 1, i, j + 1, visited) or\
                 self.existRecu(board, word, cur + 1, i, j - 1, visited)
        visited[i][j] = False

        return result
