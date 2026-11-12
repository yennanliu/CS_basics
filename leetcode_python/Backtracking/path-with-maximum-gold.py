# https://leetcode.com/problems/path-with-maximum-gold/

"""

1219. Path with Maximum Gold
Medium

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


"""
NOTE !!!!


    -> LC 1219 Path with Maximum Gold 不適合使用 Dijkstra。
       這題應該使用 DFS + Backtracking（回溯）。


    -> Reason 1:

        題目要求找出一條路徑，讓沿途收集的 Gold 總和最大，
        而且每個格子最多只能走一次。這不是一般 Dijkstra 的最短路徑問題，
        也不能用 cost_matrix 記錄「到某格的最佳成本」來剪枝，
        因為是否走過某格會影響後續可以走的路徑。


    -> Reason 2:

        為什麼不能用 Dijkstra？ 
        Dijkstra 是用來尋找「起點到終點的`最短或最小`成本路徑」，
        且通常節點或邊權重固定、可以重複造訪。
        而 LC 1219 要求的是在一張網格中尋找「
        不重複造訪節點（Simple Path）且總和最大」的路徑。
        這在圖論中屬於 NP-hard 的最長路徑問題變體，
        必須透過回溯法（Backtracking / DFS）來窮舉所有可能的簡單路徑。

"""

# V0
# IDEA: DFS + BACKTRACK (gpt)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
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

        """
        NOTE !!!


        grid[y][x] = -1 會持續影響目前`這一層 DFS 及其所有子遞迴呼叫`，
        直到這個 DFS 節點的四個方向都執行完畢，
        才會執行 backtrack，將原本的值還原。


        ->

        子 DFS 可以看到父 DFS 修改過的 grid。
        父 DFS 也會看到子 DFS 修改過的 grid。
        每一層 DFS 必須負責還原自己修改的狀態。


        ->

        DFS 往下探索時，修改的狀態會持續保留給所有子遞迴呼叫；
        只有當目前節點的所有遞迴分支都完成後，
        才會執行 backtracking，還原該節點自己的狀態。



        -> backtrack code structure


        ```
        # 1. Save original state
        cache = grid[y][x]

        # 2. Modify state
        grid[y][x] = -1

        # 3. Explore all recursive branches
        dfs(...)
        dfs(...)
        dfs(...)
        dfs(...)

        # 4. Restore original state
        grid[y][x] = cache
        ```

        """
        grid[y][x] = -1

        self.dfs_helper(grid, x + 1, y, cur_gold)
        self.dfs_helper(grid, x - 1, y, cur_gold)
        self.dfs_helper(grid, x, y + 1, cur_gold)
        self.dfs_helper(grid, x, y - 1, cur_gold)


        """
        NOTE !!!


        -> undo the grid `state` (e.g. backtrack) 
        """
        # backtrack
        grid[y][x] = cache


# V0-0-1
# IDEA: DFS + BACKTRACK (GPT)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # Edge case
        if not grid or not grid[0]:
            return 0

        self.max_gold = 0

        l = len(grid)
        w = len(grid[0])

        # Try every non-zero cell as starting point
        for y in range(l):
            for x in range(w):

                if grid[y][x] > 0:
                    cur_gold = 0
                    self.helper(grid, x, y, cur_gold)

        return self.max_gold

    def helper(self, grid, x, y, cur_gold):

        l = len(grid)
        w = len(grid[0])

        # 1. Boundary check
        if x < 0 or x >= w or y < 0 or y >= l:
            return

        # 2. Check whether cell is empty or visited
        if grid[y][x] <= 0:
            return

        # 3. Collect gold
        val = grid[y][x]
        cur_gold += val

        # 4. Update maximum gold
        self.max_gold = max(self.max_gold, cur_gold)

        # 5. Mark as visited
        grid[y][x] = -1

        # 6. Explore four directions
        self.helper(grid, x + 1, y, cur_gold)
        self.helper(grid, x - 1, y, cur_gold)
        self.helper(grid, x, y + 1, cur_gold)
        self.helper(grid, x, y - 1, cur_gold)

        # 7. Backtracking: restore original value
        grid[y][x] = val



# V0-0-2
# IDEA: DFS + BACKTRACK (gemini)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0
            
        rows = len(grid)
        cols = len(grid[0])
        self.max_gold = 0
        
        def dfs(r, c, current_gold):
            # 更新全域最大黃金數
            self.max_gold = max(self.max_gold, current_gold)
            
            # 暫存當前格子的黃金數，並將其標記為已造訪（設為 0）
            temp = grid[r][c]
            grid[r][c] = 0
            
            # 四個方向：上、下、左、右
            directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                # 如果新座標在邊界內，且該格有黃金（> 0）
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] > 0:
                    dfs(nr, nc, current_gold + grid[nr][nc])
                    
            # 回溯（Backtrack）：離開當前節點時，恢復該格子的原值，供其他路徑使用
            grid[r][c] = temp

        # 從每一個大於 0 的格子出發進行 DFS 探索
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] > 0:
                    dfs(r, c, grid[r][c])
                    
        return self.max_gold


# V0-0-3
# IDEA: DFS + BACKTRACK (GPT)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0

        m = len(grid)
        n = len(grid[0])

        self.max_gold = 0

        # Try every non-zero cell as the starting point
        for y in range(m):
            for x in range(n):
                if grid[y][x] > 0:
                    self.dfs(grid, x, y, 0)

        return self.max_gold

    def dfs(self, grid, x, y, current_gold):
        m = len(grid)
        n = len(grid[0])

        # 1. Boundary check
        if x < 0 or x >= n or y < 0 or y >= m:
            return

        # 2. Cannot visit empty cells or already visited cells
        if grid[y][x] == 0:
            return

        # 3. Collect gold from current cell
        gold = grid[y][x]
        current_gold += gold

        # 4. Update global maximum
        self.max_gold = max(self.max_gold, current_gold)

        # 5. Mark current cell as visited
        grid[y][x] = 0

        # 6. Explore four directions
        self.dfs(grid, x + 1, y, current_gold)  # Right
        self.dfs(grid, x - 1, y, current_gold)  # Left
        self.dfs(grid, x, y + 1, current_gold)  # Down
        self.dfs(grid, x, y - 1, current_gold)  # Up

        # 7. Backtracking: restore original gold
        grid[y][x] = gold


# V0-1
# IDEA: DFS + BACKTRACK (gemini)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
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



# V0-2
# IDEA: DFS (for loop in moves V1) + BACKTRACK (gemini)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.max_gold = 0
        l = len(grid)
        w = len(grid[0])
        
        for y in range(l):
            for x in range(w):
                if grid[y][x] > 0:
                    # Pass the initial grid[y][x] value as the starting cur_gold
                    self.dfs_helper(grid, x, y, grid[y][x])
                    
        return self.max_gold

    def dfs_helper(self, grid, x, y, cur_gold):
        l = len(grid)
        w = len(grid[0])
        
        # Update the global maximum with our current valid accumulated path total
        self.max_gold = max(self.max_gold, cur_gold)
        
        # Mark current cell as visited in-place
        cache = grid[y][x]
        grid[y][x] = -1
        
        # Explore neighbors using your specific move matrix layout
        moves = [[-1, 0], [1, 0], [0, 1], [0, -1]]
        for m in moves:
            x_ = x + m[0]
            y_ = y + m[1]
            
            # Inline boundary check and validation before making the call
            if 0 <= x_ < w and 0 <= y_ < l and grid[y_][x_] > 0:
                self.dfs_helper(grid, x_, y_, cur_gold + grid[y_][x_])
                
        # Backtrack: Restore the cell value for other search paths
        grid[y][x] = cache



# V0-3
# IDEA: DFS (for loop in moves V2) + BACKTRACK (gemini)
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
class Solution(object):
    def getMaximumGold(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.max_gold = 0
        l = len(grid)
        w = len(grid[0])
        
        for y in range(l):
            for x in range(w):
                if grid[y][x] > 0:
                    # Initialize max_gold with the starting cell's value 
                    # to cover single-cell configurations safely
                    self.max_gold = max(self.max_gold, grid[y][x])
                    self.dfs_helper(grid, x, y, grid[y][x])
                    
        return self.max_gold

    def dfs_helper(self, grid, x, y, cur_gold):
        l = len(grid)
        w = len(grid[0])
        
        # Mark current cell as visited in-place
        cache = grid[y][x]
        grid[y][x] = -1
        
        # Explore neighbors 
        moves = [[-1, 0], [1, 0], [0, 1], [0, -1]]
        for m in moves:
            x_ = x + m[0]
            y_ = y + m[1]
            
            if 0 <= x_ < w and 0 <= y_ < l and grid[y_][x_] > 0:

                """
                NOTE !!!


                we DON'T do below in `for m in moves` loop,

                    ```
                    cache = grid[y][x]
                    grid[y][x] = -1
                    ```
                since:

                    -> same cell CAN be visited serverl times
                       by different DFS recursion calls

                """
                next_gold = cur_gold + grid[y_][x_]
                
                # Update global tracking directly inside the loop step
                self.max_gold = max(self.max_gold, next_gold)
                
                self.dfs_helper(grid, x_, y_, next_gold)
                
        # Backtrack: Restore the cell value 
        grid[y][x] = cache



# V1



# V2
# IDEA: Depth-First Search with Backtracking
# https://leetcode.com/problems/path-with-maximum-gold/editorial/
# time = O(4^(m*n)), m = rows, n = cols
# space = O(m*n)
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





# V2'
# IDEA: bfs with Backtracking
# https://leetcode.com/problems/path-with-maximum-gold/editorial/
# time = O(4^(m*n) * m*n), m = rows, n = cols (queue states each copy visited set)
# space = O(4^(m*n) * m*n)
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
