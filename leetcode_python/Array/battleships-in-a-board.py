# V0

# V1 
# http://bookshadow.com/weblog/2016/10/13/leetcode-battleships-in-a-board/
# IDEA : GREEDY 
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
# Time:  O(m * n)
# Space: O(1)
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