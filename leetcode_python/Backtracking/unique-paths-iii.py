"""

980. Unique Paths III
Hard

You are given an m x n integer array grid where grid[i][j] could be:

1 representing the starting square. There is exactly one starting square.
2 representing the ending square. There is exactly one ending square.
0 representing empty squares we can walk over.
-1 representing obstacles that we cannot walk over.
Return the number of 4-directional walks from the starting square to the ending square, that walk over every non-obstacle square exactly once.

 

Example 1:


Input: grid = [[1,0,0,0],[0,0,0,0],[0,0,2,-1]]
Output: 2
Explanation: We have the following two paths: 
1. (0,0),(0,1),(0,2),(0,3),(1,3),(1,2),(1,1),(1,0),(2,0),(2,1),(2,2)
2. (0,0),(1,0),(2,0),(2,1),(1,1),(0,1),(0,2),(0,3),(1,3),(1,2),(2,2)
Example 2:


Input: grid = [[1,0,0,0],[0,0,0,0],[0,0,0,2]]
Output: 4
Explanation: We have the following four paths: 
1. (0,0),(0,1),(0,2),(0,3),(1,3),(1,2),(1,1),(1,0),(2,0),(2,1),(2,2),(2,3)
2. (0,0),(0,1),(1,1),(1,0),(2,0),(2,1),(2,2),(1,2),(0,2),(0,3),(1,3),(2,3)
3. (0,0),(1,0),(2,0),(2,1),(2,2),(1,2),(1,1),(0,1),(0,2),(0,3),(1,3),(2,3)
4. (0,0),(1,0),(2,0),(2,1),(1,1),(0,1),(0,2),(0,3),(1,3),(1,2),(2,2),(2,3)
Example 3:


Input: grid = [[0,1],[2,0]]
Output: 0
Explanation: There is no path that walks over every empty square exactly once.
Note that the starting and ending square can be anywhere in the grid.
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 20
1 <= m * n <= 20
-1 <= grid[i][j] <= 2
There is exactly one starting cell and one ending cell.

"""

# V0

# V1
# IDEA : BACKTRACK
# https://leetcode.com/problems/unique-paths-iii/discuss/221946/JavaPython-Brute-Force-Backtracking
# IDEA :
# First find out where the start and the end is.
# Also We need to know the number of empty cells.
# We we try to explore a cell,
# it will change 0 to -2 and do a dfs in 4 direction.
# If we hit the target and pass all empty cells, increment the result.
# Complexity
# Time complexity is as good as dp,
# but it take less space and easier to implement.
class Solution:
    def uniquePathsIII(self, A):
        self.res = 0
        m, n, empty = len(A), len(A[0]), 1
        for i in range(m):
            for j in range(n):
                if A[i][j] == 1:
                    x, y = (i, j)
                elif A[i][j] == 0:
                    empty += 1

        def dfs(x, y, empty):
            if not (0 <= x < m and 0 <= y < n and A[x][y] >= 0): return
            if A[x][y] == 2:
                self.res += empty == 0
                return
            A[x][y] = -2
            dfs(x + 1, y, empty - 1)
            dfs(x - 1, y, empty - 1)
            dfs(x, y + 1, empty - 1)
            dfs(x, y - 1, empty - 1)
            A[x][y] = 0
        dfs(x, y, empty)
        return self.res

# V1'
# https://zxi.mytechroad.com/blog/searching/leetcode-980-unique-paths-iii/
# C++
# class Solution {
# public:
#   int uniquePathsIII(vector<vector<int>>& grid) {    
#     int sx = -1;
#     int sy = -1;
#     int n = 1;
#     for (int i = 0; i < grid.size(); ++i)
#       for (int j = 0; j < grid[0].size(); ++j)
#         if (grid[i][j] == 0) ++n;
#         else if (grid[i][j] == 1) { sx = j; sy = i; }    
#     return dfs(grid, sx, sy, n);
#   }
# private:
#   int dfs(vector<vector<int>>& grid, int x, int y, int n) {
#     if (x < 0 || x == grid[0].size() || 
#         y < 0 || y == grid.size() || 
#         grid[y][x] == -1) return 0;
#     if (grid[y][x] == 2) return n == 0;    
#     grid[y][x] = -1;
#     int paths = dfs(grid, x + 1, y, n - 1) + 
#                 dfs(grid, x - 1, y, n - 1) +
#                 dfs(grid, x, y + 1, n - 1) + 
#                 dfs(grid, x, y - 1, n - 1);
#     grid[y][x] = 0;
#     return paths;
#   };
# };

# V1''
# IDEA : DFS
# https://leetcode.com/problems/unique-paths-iii/discuss/253343/Python-just-DFS
# IDEA : 
# Just as DFS is a path search, we will do DFS (walk) over non-obstacle squares.
# If we find a path that has walked over every non-obstacle square exactly once, we increase self.walks by 1.
# We ensure this by counting visited squares.
# The key point in this DFS is that we modify every visited square by decreasing square value (grid[i][j] -= 1) and reversing it after each DFS. Therefore, the next DFS will be fresh on unmodified grid.
class Solution:
    def uniquePathsIII(self, grid: List[List[int]]) -> int:
        def dfs(i, j, visited):
            if grid[i][j] == 2:
                self.walks += visited == self.visit
                return
            for x, y in ((i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)):
                if 0 <= x < m and 0 <= y < n and grid[x][y] != -1:
                    grid[i][j] -= 1
                    dfs(x, y, visited + 1)
                    grid[i][j] += 1
        m, n = len(grid), len(grid[0])
        self.visit = m * n - sum(c == -1 for row in grid for c in row)
        self.walks = 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    grid[i][j] -= 1
                    dfs(i, j, 1)
        return self.walks

# V1'''
# IDEA : Backtracking
# https://leetcode.com/problems/unique-paths-iii/solution/
class Solution:
    def uniquePathsIII(self, grid: List[List[int]]) -> int:
        rows, cols = len(grid), len(grid[0])

        # step 1). initialize the conditions for backtracking
        #   i.e. initial state and final state
        non_obstacles = 0
        start_row, start_col = 0, 0
        for row in range(0, rows):
            for col in range(0, cols):
                cell = grid[row][col] 
                if  cell >= 0:
                    non_obstacles += 1
                if cell == 1:
                    start_row, start_col = row, col

        # count of paths as the final result
        path_count = 0

        # step 2). backtrack on the grid
        def backtrack(row, col, remain):
            # we need to modify this external variable
            nonlocal path_count

            # base case for the termination of backtracking
            if grid[row][col] == 2 and remain == 1:
                # reach the destination
                path_count += 1
                return

            # mark the square as visited. case: 0, 1, 2 
            temp = grid[row][col] 
            grid[row][col] = -4
            remain -= 1   # we now have one less square to visit

            # explore the 4 potential directions around
            for ro, co in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                next_row, next_col = row + ro, col + co

                if not (0 <= next_row < rows and 0 <= next_col < cols):
                    # invalid coordinate
                    continue
                if grid[next_row][next_col] < 0:
                    # either obstacle or visited square
                    continue

                backtrack(next_row, next_col, remain)

            # unmark the square after the visit
            grid[row][col] = temp

        backtrack(start_row, start_col, non_obstacles)

        return path_count

# V1''''
# https://leetcode.com/problems/unique-paths-iii/discuss/871046/python-backtracking
class Solution:
	def uniquePathsIII(self, grid: List[List[int]]) -> int:
		r = len(grid)
		c = len(grid[0])
		n = 0
		path = [(1, 0), (-1, 0), (0, 1), (0, -1)]
		seen = set()
		self.res = 0
		for i in range(r):
			for j in range(c):
				if grid[i][j] == 0 or grid[i][j] == 2:
					n += 1
				elif grid[i][j] == 1:
					start = (i, j)
					seen.add((i, j))
		def helper(i, j, cnt):
			if cnt == n and grid[i][j] == 2:
				self.res += 1
			if cnt < n and grid[i][j] == 2:
				return
			for k in path:
				x = i + k[0]
				y = j + k[1]
				if 0 <= x < r and 0 <= y < c and (x, y) not in seen and grid[x][y] != -1:
					seen.add((x, y))
					helper(x, y, cnt + 1)
					seen.remove((x, y))
		helper(start[0], start[1], 0)
		return self.res

# V1''''
# https://leetcode.com/problems/unique-paths-iii/discuss/222122/Python-solution
class Solution:
    def uniquePathsIII(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        def dfs(y, x, count):
            seen[y][x] = 1
            if grid[y][x] == 2:
                seen[y][x] = 0
                if count == non_obs_count:
                    return 1
                else:
                    return 0
            res = []
            if y > 0 and (grid[y-1][x] == 0 or grid[y-1][x] == 2) and seen[y-1][x] == 0:
                res.append(dfs(y-1, x, count+1))
            if y < n-1 and (grid[y+1][x] == 0 or grid[y+1][x] == 2) and seen[y+1][x] == 0:
                res.append(dfs(y+1, x, count+1))
            if x > 0 and (grid[y][x-1] == 0 or grid[y][x-1] == 2) and seen[y][x-1] == 0:
                res.append(dfs(y, x-1, count+1))
            if x < m-1 and (grid[y][x+1] == 0 or grid[y][x+1] == 2) and seen[y][x+1] == 0:
                res.append(dfs(y, x+1, count+1))
            seen[y][x] = 0
            return sum(res)        
        
        
        if not grid or not grid[0]:
            return 0
        n = len(grid)
        m = len(grid[0])
        seen = [[0]*m for _ in range(n)]
        non_obs_count = 0
        for i in range(n):
            for j in range(m):
                if grid[i][j] == 0 or grid[i][j] == 2:
                    non_obs_count += 1
                if grid[i][j] == 1:
                    start = (i, j)
        
        return dfs(start[0], start[1], 0)

# V1'''''
# https://www.cnblogs.com/grandyang/p/14191490.html
# https://github.com/grandyang/leetcode/issues/980
# JAVA
# class Solution {
# public:
#     int uniquePathsIII(vector<vector<int>>& grid) {
#         int m = grid.size(), n = grid[0].size(), x0 = 0, y0 = 0, target = 1, res = 0;
#         for (int i = 0; i < m; ++i) {
#             for (int j = 0; j < n; ++j) {
#                 if (grid[i][j] == 1) {
#                     x0 = i; y0 = j;
#                 } else if (grid[i][j] == 0) {
#                     ++target;
#                 }
#             }
#         }
#         helper(grid, target, x0, y0, res);
#         return res;
#     }
#     void helper(vector<vector<int>>& grid, int& target, int i, int j, int& res) {
#         int m = grid.size(), n = grid[0].size();
#         if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] < 0) return;
#         if (grid[i][j] == 2) {
#             if (target == 0) ++res;
#             return;
#         }
#         grid[i][j] = -2;
#         --target;
#         helper(grid, target, i + 1, j, res);
#         helper(grid, target, i - 1, j, res);
#         helper(grid, target, i, j + 1, res);
#         helper(grid, target, i, j - 1, res);
#         grid[i][j] = 0;
#         ++target;
#     }
# };

# V1'''''''
# https://blog.csdn.net/qq_39378221/article/details/105788102

# V2