"""

419. Battleships in a Board
Medium

Given an m x n matrix board where each cell is a battleship 'X' or empty '.', return the number of the battleships on board.

Battleships can only be placed horizontally or vertically on board. In other words, they can only be made of the shape 1 x k (1 row, k columns) or k x 1 (k rows, 1 column), where k can be of any size. At least one horizontal or vertical cell separates between two battleships (i.e., there are no adjacent battleships).

Example 1:

https://assets.leetcode.com/uploads/2024/06/21/image.png

Input: board = [["X",".",".","X"],[".",".",".","X"],[".",".",".","X"]]
Output: 2

Example 2:

Input: board = [["."]]
Output: 0

Constraints:

m == board.length
n == board[i].length
1 <= m, n <= 200
board[i][j] is either '.' or 'X'.

Follow up: Could you do it in one-pass, using only O(1) extra memory and without modifying the values board?

"""

# V0

# V1 
# http://bookshadow.com/weblog/2016/10/13/leetcode-battleships-in-a-board/
# IDEA : GREEDY 
# time = O(m * n)
# space = O(1)
class Solution(object):
    def countBattleships(self, board):
        """
        :type board: List[List[str]]
        :rtype: int
        """
        h = len(board)
        w = len(board[0]) if h else 0

        ans = 0
        for x in range(h):
            for y in range(w):
                if board[x][y] == 'X':
                    if x > 0 and board[x - 1][y] == 'X':
                        continue
                    if y > 0 and board[x][y - 1] == 'X':
                        continue
                    ans += 1
        return ans

# V1' 
# http://bookshadow.com/weblog/2016/10/13/leetcode-battleships-in-a-board/
# IDEA : DFS
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def countBattleships(self, board):
        """
        :type board: List[List[str]]
        :rtype: int
        """
        vs = set()
        h = len(board)
        w = len(board[0]) if h else 0

        def dfs(x, y):
            for dx, dy in zip((1, 0, -1, 0), (0, 1, 0, -1)):
                nx, ny = x + dx, y + dy
                if 0 <= nx < h and 0 <= ny < w:
                    if (nx, ny) not in vs and board[nx][ny] == 'X':
                        vs.add((nx, ny))
                        dfs(nx, ny)

        ans = 0
        for x in range(h):
            for y in range(w):
                if (x, y) not in vs and board[x][y] == 'X':
                    ans += 1
                    vs.add((x, y))
                    dfs(x, y)
        return ans 
        
# V2
# time = O(m * n)
# space = O(1)
class Solution(object):
    def countBattleships(self, board):
        """
        :type board: List[List[str]]
        :rtype: int
        """
        if not board or not board[0]:
            return 0

        cnt = 0
        for i in range(len(board)):
            for j in range(len(board[0])):
                cnt += int(board[i][j] == 'X' and
                           (i == 0 or board[i - 1][j] != 'X') and
                           (j == 0 or board[i][j - 1] != 'X'))
        return cnt