"""

64. Minimum Path Sum
Medium

Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right, which minimizes the sum of all numbers along its path.

Note: You can only move either down or right at any point in time.

 

Example 1:


Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
Output: 7
Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
Example 2:

Input: grid = [[1,2,3],[4,5,6]]
Output: 12
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 200
0 <= grid[i][j] <= 100

"""

# V0
class Solution(object):
    def minPathSum(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        pass


# V0-1
# IDEA: Dijkstra (gemini)
import heapq

class Solution(object):
    def minPathSum(self, grid):

        # Edge case
        if not grid or not grid[0]:
            return 0
            
        m = len(grid)
        n = len(grid[0])
        
        """
        NOTE !!!

        1. 

        since PQ compares first element by default,
        so we need to insert in below ordering:


         [current_cost, row, col]


        2. 

        the cost for `starting point` is grid[0][0],
        so we use it in our PQ init
        """
        # pq stores: [current_cost, row, col]
        # Start with the cost of the very first cell
        pq = [[grid[0][0], 0, 0]]
        

        """
        NOTE !!!


        1. 

        `cost_grid` for saving `cost` in grid

        -> so we know whether should proceed with new proposed paths


        2. syntax: `[val]...`

        [[val] * n for _ in range(m)]

        """
        # Properly initialize the 2D cost array
        cost_grid = [[float('inf')] * n for _ in range(m)]
        # DON'T forget to update init cost to cost_grid
        cost_grid[0][0] = grid[0][0]
        
        # CAN ONLY move right, down
        # [d_row, d_col]: right, down
        moves = [[0, 1], [1, 0]] 
        
        while pq:
            # Always pop the path with the lowest cost so far
            curr_cost, r, c = heapq.heappop(pq)
            
            # If we reached the bottom-right corner, we are done!
            if r == m - 1 and c == n - 1:
                return curr_cost
                
            """
            NOTE !!!


            we skip if we already has a better path (low cost) VS
            current path
            """
            # If we already found a better path to this cell earlier, skip it
            if curr_cost > cost_grid[r][c]:
                continue
                
            for dr, dc in moves:
                nr, nc = r + dr, c + dc
                
                # Check bounds
                if 0 <= nr < m and 0 <= nc < n:
                    new_cost = curr_cost + grid[nr][nc]
                    
                    # If this new path is cheaper, record it and push to heap
                    if new_cost < cost_grid[nr][nc]:
                        cost_grid[nr][nc] = new_cost
                        heapq.heappush(pq, [new_cost, nr, nc])
                        
        return -1



# V0-2
# IDEA: Dijkstra (gpt)
import heapq

# Dijkstra = BFS + Priority Queue
class Solution(object):
    def minPathSum(self, grid):
        if not grid or not grid[0]:
            return 0

        m = len(grid)
        n = len(grid[0])

        # (cost, x, y)
        pq = [(grid[0][0], 0, 0)]

        moves = [(0, 1), (1, 0)]

        cost_grid = [[float("inf")] * n for _ in range(m)]
        cost_grid[0][0] = grid[0][0]

        while pq:
            cost, x, y = heapq.heappop(pq)

            if x == n - 1 and y == m - 1:
                return cost

            # Skip outdated entries
            if cost > cost_grid[y][x]:
                continue

            for dx, dy in moves:
                nx = x + dx
                ny = y + dy

                if 0 <= nx < n and 0 <= ny < m:
                    new_cost = cost + grid[ny][nx]

                    if new_cost < cost_grid[ny][nx]:
                        cost_grid[ny][nx] = new_cost
                        heapq.heappush(pq, (new_cost, nx, ny))

        return -1



# V0-3
# IDEA: DP (gpt)
class Solution(object):
    def minPathSum(self, grid):
        if not grid or not grid[0]:
            return 0

        m = len(grid)
        n = len(grid[0])

        dp = [[0] * n for _ in range(m)]

        dp[0][0] = grid[0][0]

        # First row
        for j in range(1, n):
            dp[0][j] = dp[0][j - 1] + grid[0][j]

        # First column
        for i in range(1, m):
            dp[i][0] = dp[i - 1][0] + grid[i][0]

        # Remaining cells
        for i in range(1, m):
            for j in range(1, n):
                dp[i][j] = min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]

        return dp[m - 1][n - 1]



# V0-3
# IDEA: DP (gemini)
class Solution(object):
    def minPathSum(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0
            
        m = len(grid)
        n = len(grid[0])
        
        # 1. Initialize the first row (can only arrive from the left)
        for c in range(1, n):
            grid[0][c] += grid[0][c-1]
            
        # 2. Initialize the first column (can only arrive from above)
        for r in range(1, m):
            grid[r][0] += grid[r-1][0]
            
        # 3. Fill the rest of the grid
        for r in range(1, m):
            for c in range(1, n):
                # The minimum cost to reach this cell is its own cost PLUS 
                # the cheaper of the two cells we could have come from.
                grid[r][c] += min(grid[r-1][c], grid[r][c-1])
                
        # The bottom-right cell now contains the absolute minimum path sum
        return grid[-1][-1]


# V1
# https://blog.csdn.net/xx_123_1_rj/article/details/84571645
# IDEA : 
# DP : dp[i][j] = dp[i][j] + min(dp[i-1][j], dp[i][j-1])
# DEMO 
#    ...: grid = [
#    ...:     [1, 3, 1],
#    ...:     [1, 5, 1],
#    ...:     [4, 2, 1]]
#    ...: solu = Solution()
#    ...: print(solu.minPathSum(grid))
#    ...:     
# grid : [[1, 4, 5], [2, 7, 6], [6, 8, 7]]
# 7
# time = O(m * n)
# space = O(1)
class Solution:
    def minPathSum(self, grid):

        if not grid: return None
        m, n = len(grid), len(grid[0])  # row, column 

        for i in range(1, m):  ###  deal with boundary (→)  -- the 1st row, since the 1st row can only be visited via →. so we can fill them first at beginning 
            grid[i][0] += grid[i-1][0]

        for j in range(1, n):  ### deal with boundary (↓) -- the 1st column, since the 1st column can only be visited via ↓. so we can fill them first at beginning  
            grid[0][j] += grid[0][j-1]

        for i in range(1, m):  # get dp[i][j] += min(dp[i-1][j], dp[i][j-1])
            for j in range(1, n):
                grid[i][j] += min(grid[i-1][j], grid[i][j-1])
        return grid[-1][-1]

# V1'
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
# public class Solution {
#     public int calculate(int[][] grid, int i, int j) {
#         if (i == grid.length || j == grid[0].length) return Integer.MAX_VALUE;
#         if (i == grid.length - 1 && j == grid[0].length - 1) return grid[i][j];
#         return grid[i][j] + Math.min(calculate(grid, i + 1, j), calculate(grid, i, j + 1));
#     }
#     public int minPathSum(int[][] grid) {
#         return calculate(grid, 0, 0);
#     }
# }

# V1''
# IDEA : DP 2D
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
# public class Solution {
#     public int minPathSum(int[][] grid) {
#         int[][] dp = new int[grid.length][grid[0].length];
#         for (int i = grid.length - 1; i >= 0; i--) {
#             for (int j = grid[0].length - 1; j >= 0; j--) {
#                 if(i == grid.length - 1 && j != grid[0].length - 1)
#                     dp[i][j] = grid[i][j] +  dp[i][j + 1];
#                 else if(j == grid[0].length - 1 && i != grid.length - 1)
#                     dp[i][j] = grid[i][j] + dp[i + 1][j];
#                 else if(j != grid[0].length - 1 && i != grid.length - 1)
#                     dp[i][j] = grid[i][j] + Math.min(dp[i + 1][j], dp[i][j + 1]);
#                 else
#                     dp[i][j] = grid[i][j];
#             }
#         }
#         return dp[0][0];
#     }
# }

# V1'''
# IDEA : DP 1D
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
#  public class Solution {
#     public int minPathSum(int[][] grid) {
#         int[] dp = new int[grid[0].length];
#         for (int i = grid.length - 1; i >= 0; i--) {
#             for (int j = grid[0].length - 1; j >= 0; j--) {
#                 if(i == grid.length - 1 && j != grid[0].length - 1)
#                     dp[j] = grid[i][j] +  dp[j + 1];
#                 else if(j == grid[0].length - 1 && i != grid.length - 1)
#                     dp[j] = grid[i][j] + dp[j];
#                 else if(j != grid[0].length - 1 && i != grid.length - 1)
#                     dp[j] = grid[i][j] + Math.min(dp[j], dp[j + 1]);
#                 else
#                     dp[j] = grid[i][j];
#             }
#         }
#         return dp[0];
#     }
# }

# V2
# time = O(m * n)
# space = O(m + n)
class Solution(object):
    # @param grid, a list of lists of integers
    # @return an integer
    def minPathSum(self, grid):
        sum = list(grid[0])
        for j in range(1, len(grid[0])):
            sum[j] = sum[j - 1] + grid[0][j]

        for i in range(1, len(grid)):
            sum[0] += grid[i][0]
            for j in range(1, len(grid[0])):
                sum[j] = min(sum[j - 1], sum[j]) + grid[i][j]

        return sum[-1]
