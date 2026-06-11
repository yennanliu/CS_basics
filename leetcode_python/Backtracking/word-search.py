"""

79. Word Search
Medium

Given an m x n grid of characters board and a string word, return true if word exists in the grid.

The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once.

 

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
 

Follow up: Could you use search pruning to make your solution faster with a larger board?


"""

# V0
# IDEA : DFS + backtracking
class Solution(object):
    def exist(self, board, word):
        # Edge case
        if not board or not board[0]:
            return False

        self.rows = len(board)
        self.cols = len(board[0])

        # Try every cell as a starting point
        for r in range(self.rows):
            for c in range(self.cols):
                if self.dfs(board, word, r, c, 0):
                    return True

        return False

    def dfs(self, board, word, r, c, idx):
        # Out of bounds
        if (
            r < 0 or r >= self.rows or
            c < 0 or c >= self.cols
        ):
            return False

        # Current cell does not match current character
        if board[r][c] != word[idx]:
            return False

        # We matched the last character of the word
        if idx == len(word) - 1:
            return True

        # Mark current cell as visited
        temp = board[r][c]
        board[r][c] = "#"

        # Explore 4 directions
        found = (
            self.dfs(board, word, r + 1, c, idx + 1) or
            self.dfs(board, word, r - 1, c, idx + 1) or
            self.dfs(board, word, r, c + 1, idx + 1) or
            self.dfs(board, word, r, c - 1, idx + 1)
        )

        """
        NOTE !!!

         -> need `undo` the change on martrix val
         (Backtrack)
        """
        # Backtrack: restore original value
        board[r][c] = temp

        """
        NOTE !!!

         -> return the state here !!!
         (but NOT not above, since we still need `backtrack` the state)



        -> e.g.  (below is WRONG)

        board[r][c] = "#"

        return (
            self.dfs(...) or
            self.dfs(...) or
            self.dfs(...) or
            self.dfs(...)
        )

        board[r][c] = temp   # NEVER REACHED


        """
        return found



# V0
# IDEA : DFS + backtracking
class Solution(object):
    def exist(self, board, word):
        if not board or not word:
            return False
            
        self.rows = len(board)       
        self.cols = len(board[0])
        
        # Start a search from every single cell on the board
        for r in range(self.rows):
            for c in range(self.cols):
                # If the first character matches, kick off the backtracking depth search
                if board[r][c] == word[0]:
                    if self.dfs(board, word, r, c, 0):
                        return True
        return False
        
    def dfs(self, board, word, r, c, idx):
        # Base Case: If our index matches the word length, we successfully matched everything!
        if idx == len(word):
            return True
            
        # Out of bounds check OR character mismatch check
        if (r < 0 or r >= self.rows or 
            c < 0 or c >= self.cols or 
            board[r][c] != word[idx]):
            return False
            
        # 1. Action: Save the character
        # and mask the cell to mark it as visited
        temp = board[r][c]
        board[r][c] = "#"
        
        # 2. Recurse: Search all 4 adjacent directions
        # If ANY path returns True, we bubble that True all the way up immediately!
        found = (self.dfs(board, word, r + 1, c, idx + 1) or
                 self.dfs(board, word, r - 1, c, idx + 1) or
                 self.dfs(board, word, r, c + 1, idx + 1) or
                 self.dfs(board, word, r, c - 1, idx + 1))
                 
        # 3. Backtrack: Restore the original character so other paths can use it
        board[r][c] = temp
        
        return found


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
        # if "not false" till cur == len(word), 
        # means we already found the wprd in board
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
   

# V0-1
class Solution(object):
    def exist(self, board, word):
        if not board and word:
            return False

        if not board and not word:
            return True

        l = len(board)
        w = len(board[0])

        for y in range(l):
            for x in range(w):
                if board[y][x] == word[0]:
                    visited = [[False] * w for _ in range(l)]
                    visited[y][x] = True

                    if self.helper(board, word, x, y, visited, 0):
                        return True

        return False

    def helper(self, board, word, x, y, visited, idx):
        l = len(board)
        w = len(board[0])

        if board[y][x] != word[idx]:
            return False

        if idx == len(word) - 1:
            return True

        dirs = [[0,1], [0,-1], [1,0], [-1,0]]

        for d in dirs:
            _x = x + d[0]
            _y = y + d[1]

            if 0 <= _x < w and 0 <= _y < l:
                if not visited[_y][_x]:
                    visited[_y][_x] = True

                    if self.helper(
                        board,
                        word,
                        _x,
                        _y,
                        visited,
                        idx + 1
                    ):
                        return True

                    visited[_y][_x] = False

        return False

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
# IDEA : BACKTEACKING
# https://leetcode.com/problems/word-search/solution/
class Solution(object):
    def exist(self, board, word):
        """
        :type board: List[List[str]]
        :type word: str
        :rtype: bool
        """
        self.ROWS = len(board)
        self.COLS = len(board[0])
        self.board = board

        for row in range(self.ROWS):
            for col in range(self.COLS):
                if self.backtrack(row, col, word):
                    return True

        # no match found after all exploration
        return False


    def backtrack(self, row, col, suffix):
        # bottom case: we find match for each letter in the word
        if len(suffix) == 0:
            return True

        # Check the current status, before jumping into backtracking
        if row < 0 or row == self.ROWS or col < 0 or col == self.COLS \
                or self.board[row][col] != suffix[0]:
            return False

        ret = False
        # mark the choice before exploring further.
        self.board[row][col] = '#'
        # explore the 4 neighbor directions
        for rowOffset, colOffset in [(0, 1), (1, 0), (0, -1), (-1, 0)]:
            ret = self.backtrack(row + rowOffset, col + colOffset, suffix[1:])
            # break instead of return directly to do some cleanup afterwards
            if ret: break

        # revert the change, a clean slate and no side-effect
        self.board[row][col] = suffix[0]

        # Tried all directions, and did not find any match
        return ret

# V1
# IDEA : BACKTEACKING
# https://leetcode.com/problems/word-search/solution/

    def backtrack(self, row, col, suffix):
        """
            backtracking with side-effect,
               the matched letter in the board would be marked with "#".
        """
        # bottom case: we find match for each letter in the word
        if len(suffix) == 0:
            return True

        # Check the current status, before jumping into backtracking
        if row < 0 or row == self.ROWS or col < 0 or col == self.COLS \
                or self.board[row][col] != suffix[0]:
            return False

        # mark the choice before exploring further.
        self.board[row][col] = '#'
        # explore the 4 neighbor directions
        for rowOffset, colOffset in [(0, 1), (-1, 0), (0, -1), (1, 0)]:
            # sudden-death return, no cleanup.
            if self.backtrack(row + rowOffset, col + colOffset, suffix[1:]):
                return True

        # revert the marking
        self.board[row][col] = suffix[0]

        # Tried all directions, and did not find any match
        return False

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