# https://leetcode.com/problems/path-with-maximum-gold/

"""

1219. Path with Maximum Gold
Solved
Medium
Topics
premium lock icon
Companies
Hint
In a gold mine grid of size m x n, each cell in this mine has an integer representing the amount of gold in that cell, 0 if it is empty.

Return the maximum amount of gold you can collect under the conditions:

Every time you are located in a cell you will collect all the gold in that cell.
From your position, you can walk one step to the left, right, up, or down.
You can't visit the same cell more than once.
Never visit a cell with 0 gold.
You can start and stop collecting gold from any position in the grid that has some gold.
 

Example 1:

Input: grid = [[0,6,0],[5,8,7],[0,9,0]]
Output: 24
Explanation:
[[0,6,0],
 [5,8,7],
 [0,9,0]]
Path to get the maximum gold, 9 -> 8 -> 7.
Example 2:

Input: grid = [[1,0,7],[2,0,6],[3,4,5],[0,3,0],[9,0,20]]
Output: 28
Explanation:
[[1,0,7],
 [2,0,6],
 [3,4,5],
 [0,3,0],
 [9,0,20]]
Path to get the maximum gold, 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7.
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 15
0 <= grid[i][j] <= 100
There are at most 25 cells containing gold.


"""


# V0
# IDEA: DFS + BACKTRACK (gpt)
class Solution(object):
    def getMaximumGold(self, grid):
        l = len(grid)
        w = len(grid[0])

        self.max_gold = 0

        for y in range(l):
            for x in range(w):
                if grid[y][x] > 0:
                    self.dfs_helper(grid, x, y, 0)

        return self.max_gold

    def dfs_helper(self, grid, x, y, cur_gold):
        l = len(grid)
        w = len(grid[0])

        if x < 0 or x >= w or y < 0 or y >= l:
            return

        if grid[y][x] <= 0:
            return

        cur_gold += grid[y][x]
        self.max_gold = max(self.max_gold, cur_gold)

        # mark visited
        cache = grid[y][x]
        grid[y][x] = -1

        self.dfs_helper(grid, x + 1, y, cur_gold)
        self.dfs_helper(grid, x - 1, y, cur_gold)
        self.dfs_helper(grid, x, y + 1, cur_gold)
        self.dfs_helper(grid, x, y - 1, cur_gold)

        # backtrack
        grid[y][x] = cache


# V0-1
# IDEA: DFS + BACKTRACK (gemini)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.max_gold = 0
        rows = len(grid)
        cols = len(grid[0])
        
        # Try starting the path from every valid cell containing gold
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] > 0:
                    # FIX 1: Initialize current gold path tracking at 0
                    self.dfs_helper(grid, r, c, 0)
                    
        return self.max_gold

    def dfs_helper(self, grid, r, c, cur_gold):
        rows = len(grid)
        cols = len(grid[0])
        
        # FIX 2: Validate boundaries and check if cell is empty (0) or visited (-1)
        # BEFORE modifying cur_gold or proceeding.
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] <= 0:
            return
            
        # Accumulate the gold from the current cell
        cache = grid[r][c]
        cur_gold += cache
        
        # Update the maximum global gold found so far
        self.max_gold = max(self.max_gold, cur_gold)
        
        # Mark as visited in-place
        grid[r][c] = -1
        
        # Explore all 4 adjacent directions recursively
        self.dfs_helper(grid, r + 1, c, cur_gold)
        self.dfs_helper(grid, r - 1, c, cur_gold)
        self.dfs_helper(grid, r, c + 1, cur_gold)
        self.dfs_helper(grid, r, c - 1, cur_gold)
        
        # Backtrack: Restore original grid value for other search paths
        grid[r][c] = cache


# V1



# V2-1
# IDEA: Depth-First Search with Backtracking
# https://leetcode.com/problems/path-with-maximum-gold/editorial/
class Solution:
    def getMaximumGold(self, grid: List[List[int]]) -> int:
        DIRECTIONS = [0, 1, 0, -1, 0]
        rows = len(grid)
        cols = len(grid[0])
        max_gold = 0

        def dfs_backtrack(grid, rows, cols, row, col):
            # Base case: this cell is not in the matrix or has no gold
            if row < 0 or col < 0 or row == rows or col == cols or \
                    grid[row][col] == 0:
                return 0
            max_gold = 0

            # Mark the cell as visited and save the value
            original_val = grid[row][col]
            grid[row][col] = 0

            # Backtrack in each of the four directions
            for direction in range(4):
                max_gold = max(max_gold,
                               dfs_backtrack(grid, rows, cols, 
                                             DIRECTIONS[direction] + row,
                                             DIRECTIONS[direction + 1] + col))

            # Set the cell back to its original value
            grid[row][col] = original_val
            return max_gold + original_val

        # Search for the path with the maximum gold starting from each cell
        for row in range(rows):
            for col in range(cols):
                max_gold = max(max_gold, dfs_backtrack(grid, rows, cols, row, 
                                                       col))
        return max_gold





# V2-2
# IDEA: bfs with Backtracking
# https://leetcode.com/problems/path-with-maximum-gold/editorial/
class Solution:
    def getMaximumGold(self, grid: List[List[int]]) -> int:
        DIRECTIONS = [0, 1, 0, -1, 0]
        rows = len(grid)
        cols = len(grid[0])
        
        def bfs_backtrack(row: int, col: int) -> int:
            queue = deque()
            visited = set()
            max_gold = 0
            visited.add((row, col))
            queue.append((row, col, grid[row][col], visited))
            while queue:
                curr_row, curr_col, curr_gold, curr_vis = queue.popleft()
                max_gold = max(max_gold, curr_gold)

                # Search for gold in each of the 4 neighbor cells
                for direction in range(4):
                    next_row = curr_row + DIRECTIONS[direction]
                    next_col = curr_col + DIRECTIONS[direction + 1]

                    # If the next cell is in the matrix, has gold, 
                    # and has not been visited, add it to the queue
                    if 0 <= next_row < rows and 0 <= next_col < cols and \
                            grid[next_row][next_col] != 0 and \
                            (next_row, next_col) not in curr_vis:
                        curr_vis.add((next_row, next_col))
                        queue.append((next_row, next_col, 
                                      curr_gold + grid[next_row][next_col], 
                                      curr_vis.copy()))
                        curr_vis.remove((next_row, next_col))
            return max_gold

        # Find the total amount of gold in the grid
        total_gold = sum(sum(row) for row in grid)
        
        # Search for the path with the maximum gold starting from each cell
        max_gold = 0
        for row in range(rows):
            for col in range(cols):
                if grid[row][col] != 0:
                    max_gold = max(max_gold, bfs_backtrack(row, col))
                    # If we found a path with the total gold, it's the max gold
                    if max_gold == total_gold:
                        return total_gold
        return max_gold



        

