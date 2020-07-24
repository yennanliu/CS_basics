# V0 
# IDEA : DFS + SET
class Solution:
    def pacificAtlantic(self, matrix):
        if not matrix: return []
        pacific,atlantic = set(),set()
        m,n = len(matrix),len(matrix[0])
        for i in range(n):
            self.dfs(0, i, matrix, pacific, 0)
            self.dfs(m - 1, i, matrix, atlantic, 0)
        for i in range(m):
            self.dfs(i, 0, matrix, pacific, -1)
            self.dfs(i, n - 1, matrix, atlantic, 0)
        return list(pacific&atlantic)
        
    def dfs(self, x, y, matrix, visit, height):
        m,n = len(matrix),len(matrix[0])
        if x < 0 or x >= m or y < 0 or y >= n or (x,y) in visit:
            return
        if matrix[x][y] < height:
            return
        visit.add((x,y))
        moves = [[0,1], [0, -1], [1,0], [-1,0]]
        for move in moves:
            self.dfs( x + move[0], y + move[1], matrix, visit, matrix[x][y])

# V0'
class Solution:
    def pacificAtlantic(self, matrix):
        if not matrix: return []
        pacific,atlantic = set(),set()
        m,n = len(matrix),len(matrix[0])
        for i in xrange(n):
            self.dfs(0, i, matrix, pacific, 0)
            self.dfs(m - 1, i, matrix, atlantic, 0)
        for i in xrange(m):
            self.dfs(i, 0, matrix, pacific, -1)
            self.dfs(i, n - 1, matrix, atlantic, 0)
        return list(pacific&atlantic)
        
    def dfs(self, x, y, matrix, visit, height):
        m,n = len(matrix),len(matrix[0])
        if x < 0 or x >= m or y < 0 or y >= n or (x,y) in visit:
            return
        if matrix[x][y] < height:
            return
        visit.add((x,y))
        self.dfs(x - 1, y, matrix, visit, matrix[x][y]) 
        self.dfs(x + 1, y, matrix, visit, matrix[x][y]) 
        self.dfs(x, y - 1, matrix, visit, matrix[x][y]) 
        self.dfs(x, y + 1, matrix, visit, matrix[x][y])

# V1
# https://www.jiuzhang.com/solution/pacific-atlantic-water-flow/#tag-highlight-lang-python
# IDEA : DFS 
class Solution:
    """
    @param matrix: the given matrix
    @return: The list of grid coordinates
    """
    def pacificAtlantic(self, matrix):
        if not matrix: return []
        pacific,atlantic = set(),set()
        m,n = len(matrix),len(matrix[0])
        for i in xrange(n):
            self.dfs(0, i, matrix, pacific, 0)
            self.dfs(m - 1, i, matrix, atlantic, 0)
        for i in xrange(m):
            self.dfs(i, 0, matrix, pacific, -1)
            self.dfs(i, n - 1, matrix, atlantic, 0)
        return list(pacific&atlantic)
        
    def dfs(self, x, y, matrix, visit, height):
        m,n = len(matrix),len(matrix[0])
        if x < 0 or x >= m or y < 0 or y >= n or (x,y) in visit:
            return
        if matrix[x][y] < height:
            return
        visit.add((x,y))
        self.dfs(x - 1, y, matrix, visit, matrix[x][y]) 
        self.dfs(x + 1, y, matrix, visit, matrix[x][y]) 
        self.dfs(x, y - 1, matrix, visit, matrix[x][y]) 
        self.dfs(x, y + 1, matrix, visit, matrix[x][y])

### Test case : dev

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82917037
class Solution(object):
    def pacificAtlantic(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        if not matrix or not matrix[0]: return []
        m, n = len(matrix), len(matrix[0])
        p_visited = [[False] * n for _ in range(m)]
        a_visited = [[False] * n for _ in range(m)]
        for i in range(m):
            self.dfs(p_visited, matrix, m, n, i, 0)
            self.dfs(a_visited, matrix, m, n, i, n -1)
        for j in range(n):
            self.dfs(p_visited, matrix, m, n, 0, j)
            self.dfs(a_visited, matrix, m, n, m - 1, j)
        res = []
        for i in range(m):
            for j in range(n):
                if p_visited[i][j] and a_visited[i][j]:
                    res.append([i, j])
        return res
        
    def dfs(self, visited, matrix, m, n, i, j):
        visited[i][j] = True
        directions = [(-1, 0), (1, 0), (0, 1), (0, -1)]
        for dire in directions:
            x, y = i + dire[0], j + dire[1]
            if x < 0 or x >= m or y < 0 or y >= n or visited[x][y] or matrix[x][y] < matrix[i][j]:
                continue
            self.dfs(visited, matrix, m, n, x, y)

# V1''
# https://leetcode.com/problems/pacific-atlantic-water-flow/discuss/90739/Python-DFS-bests-85.-Tips-for-all-DFS-in-matrix-question.
# IDEA : DFS
class Solution(object):
    def pacificAtlantic(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        if not matrix: return []
        self.directions = [(1,0),(-1,0),(0,1),(0,-1)]
        m = len(matrix)
        n = len(matrix[0])
        p_visited = [[False for _ in range(n)] for _ in range(m)]
        
        a_visited = [[False for _ in range(n)] for _ in range(m)]
        result = []
        
        for i in range(m):
            # p_visited[i][0] = True
            # a_visited[i][n-1] = True
            self.dfs(matrix, i, 0, p_visited, m, n)
            self.dfs(matrix, i, n-1, a_visited, m, n)
        for j in range(n):
            # p_visited[0][j] = True
            # a_visited[m-1][j] = True
            self.dfs(matrix, 0, j, p_visited, m, n)
            self.dfs(matrix, m-1, j, a_visited, m, n)
            
        for i in range(m):
            for j in range(n):
                if p_visited[i][j] and a_visited[i][j]:
                    result.append([i,j])
        return result
                
                
    def dfs(self, matrix, i, j, visited, m, n):
        # when dfs called, meaning its caller already verified this point 
        visited[i][j] = True
        for dir in self.directions:
            x, y = i + dir[0], j + dir[1]
            if x < 0 or x >= m or y < 0 or y >= n or visited[x][y] or matrix[x][y] < matrix[i][j]:
                continue
            self.dfs(matrix, x, y, visited, m, n)

# V1'''
# https://leetcode.com/problems/pacific-atlantic-water-flow/discuss/90739/Python-DFS-bests-85.-Tips-for-all-DFS-in-matrix-question.
class Solution(object):
    def longestIncreasingPath(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        if not matrix: return 0
        self.directions = [(1,0),(-1,0),(0,1),(0,-1)]
        m = len(matrix)
        n = len(matrix[0])
        cache = [[-1 for _ in range(n)] for _ in range(m)]
        res = 0
        for i in range(m):
            for j in range(n):
                cur_len = self.dfs(i, j, matrix, cache, m, n)
                res = max(res, cur_len)
        return res
        
    def dfs(self, i, j, matrix, cache, m, n):
        if cache[i][j] != -1:
            return cache[i][j]
        res = 1
        for direction in self.directions:
            x, y = i + direction[0], j + direction[1]
            if x < 0 or x >= m or y < 0 or y >= n or matrix[x][y] <= matrix[i][j]:
                continue
            length = 1 + self.dfs(x, y, matrix, cache, m, n)
            res = max(length, res)
        cache[i][j] = res
        return res

# V1'''''
# https://leetcode.com/problems/pacific-atlantic-water-flow/discuss/90764/Python-solution-using-bfs-and-sets.
# IDEA : DFS + SET
class Solution(object):
    def pacificAtlantic(self, matrix):
        if not matrix: return []
        m, n = len(matrix), len(matrix[0])
        def bfs(reachable_ocean):
            q = collections.deque(reachable_ocean)
            while q:
                (i, j) = q.popleft()
                for (di, dj) in [(0,1), (0, -1), (1, 0), (-1, 0)]:
                    if 0 <= di+i < m and 0 <= dj+j < n and (di+i, dj+j) not in reachable_ocean \
                        and matrix[di+i][dj+j] >= matrix[i][j]:
                        q.append( (di+i,dj+j) )
                        reachable_ocean.add( (di+i, dj+j) )
            return reachable_ocean         
        pacific  =set ( [ (i, 0) for i in range(m)]   + [(0, j) for j  in range(1, n)]) 
        atlantic =set ( [ (i, n-1) for i in range(m)] + [(m-1, j) for j in range(n-1)]) 
        return list( bfs(pacific) & bfs(atlantic) )

# V2 
# Time:  O(m * n)
# Space: O(m * n)
class Solution(object):
    def pacificAtlantic(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: List[List[int]]
        """
        PACIFIC, ATLANTIC = 1, 2

        def pacificAtlanticHelper(matrix, x, y, prev_height, prev_val, visited, res):
            if (not 0 <= x < len(matrix)) or \
               (not 0 <= y < len(matrix[0])) or \
               matrix[x][y] < prev_height or \
               (visited[x][y] | prev_val) == visited[x][y]:
                return

            visited[x][y] |= prev_val
            if visited[x][y] == (PACIFIC | ATLANTIC):
                res.append((x, y))

            for d in [(0, -1), (0, 1), (-1, 0), (1, 0)]:
                pacificAtlanticHelper(matrix, x + d[0], y + d[1], matrix[x][y], visited[x][y], visited, res)

        if not matrix:
            return []

        res = []
        m, n = len(matrix),len(matrix[0])
        visited = [[0 for _ in range(n)] for _ in range(m)]

        for i in range(m):
            pacificAtlanticHelper(matrix, i, 0, float("-inf"), PACIFIC, visited, res)
            pacificAtlanticHelper(matrix, i, n - 1, float("-inf"), ATLANTIC, visited, res)
        for j in range(n):
            pacificAtlanticHelper(matrix, 0, j, float("-inf"), PACIFIC, visited, res)
            pacificAtlanticHelper(matrix, m - 1, j, float("-inf"), ATLANTIC, visited, res)

        return res