"""

1905. Count Sub Islands
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given two m x n binary matrices grid1 and grid2 containing only 0's (representing water) and 1's (representing land). An island is a group of 1's connected 4-directionally (horizontal or vertical). Any cells outside of the grid are considered water cells.

An island in grid2 is considered a sub-island if there is an island in grid1 that contains all the cells that make up this island in grid2.

Return the number of islands in grid2 that are considered sub-islands.

 

Example 1:


Input: grid1 = [[1,1,1,0,0],[0,1,1,1,1],[0,0,0,0,0],[1,0,0,0,0],[1,1,0,1,1]], grid2 = [[1,1,1,0,0],[0,0,1,1,1],[0,1,0,0,0],[1,0,1,1,0],[0,1,0,1,0]]
Output: 3
Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
The 1s colored red in grid2 are those considered to be part of a sub-island. There are three sub-islands.
Example 2:


Input: grid1 = [[1,0,1,0,1],[1,1,1,1,1],[0,0,0,0,0],[1,1,1,1,1],[1,0,1,0,1]], grid2 = [[0,0,0,0,0],[1,1,1,1,1],[0,1,0,1,0],[0,1,0,1,0],[1,0,0,0,1]]
Output: 2 
Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
The 1s colored red in grid2 are those considered to be part of a sub-island. There are two sub-islands.
 

Constraints:

m == grid1.length == grid2.length
n == grid1[i].length == grid2[i].length
1 <= m, n <= 500
grid1[i][j] and grid2[i][j] are either 0 or 1.

"""


# V0
# IDEA: 2 PASS DFS (gpt)
class Solution(object):
    def countSubIslands(self, grid1, grid2):
        rows = len(grid1)
        cols = len(grid1[0])

        # ---------------------------------------------------
        # 1st pass:
        # Remove every island in grid2 that overlaps water in grid1.
        # ---------------------------------------------------
        for r in range(rows):
            for c in range(cols):
                if grid2[r][c] == 1 and grid1[r][c] == 0:
                    self.dfs(grid2, r, c)

        count = 0

        # ---------------------------------------------------
        # 2nd pass:
        # Count the remaining islands.
        # ---------------------------------------------------
        for r in range(rows):
            for c in range(cols):
                if grid2[r][c] == 1:
                    count += 1
                    self.dfs(grid2, r, c)

        return count

    def dfs(self, grid, r, c):
        rows = len(grid)
        cols = len(grid[0])

        # Out of bounds
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return

        # Already water
        if grid[r][c] == 0:
            return

        # Sink this land
        grid[r][c] = 0

        self.dfs(grid, r - 1, c)
        self.dfs(grid, r + 1, c)
        self.dfs(grid, r, c - 1)
        self.dfs(grid, r, c + 1)




# V0-1
# IDEA: 2 PASS DFS (gemini)
class Solution(object):
    def countSubIslands(self, grid1, grid2):
        """
        :type grid1: List[List[int]]
        :type grid2: List[List[int]]
        :rtype: int
        """
        l = len(grid1)
        w = len(grid1[0])
        
        # 1st Pass: Eliminate all grid2 islands that can NEVER be sub-islands
        # If grid2 has land (1) but grid1 has water (0), sink that entire grid2 island.
        for y in range(l):
            for x in range(w):
                if grid2[y][x] == 1 and grid1[y][x] == 0:
                    self.dfs_sink(grid2, x, y)
                    
        cnt = 0
        
        # 2nd Pass: Count the remaining valid sub-islands left in grid2
        for y in range(l):
            for x in range(w):
                if grid2[y][x] == 1:
                    self.dfs_sink(grid2, x, y)
                    cnt += 1
                    
        return cnt
    
    def dfs_sink(self, grid2, x, y):
        l = len(grid2)
        w = len(grid2[0])
        
        # Out of bounds or already water/visited -> stop traversal
        if x < 0 or x >= w or y < 0 or y >= l or grid2[y][x] == 0:
            return
            
        # Sink the land cell to 0 to mark it as visited (no extra space needed!)
        grid2[y][x] = 0
        
        # Radiate out to all 4 neighbors using self.
        self.dfs_sink(grid2, x - 1, y)
        self.dfs_sink(grid2, x + 1, y)
        self.dfs_sink(grid2, x, y - 1)
        self.dfs_sink(grid2, x, y + 1)



# V2-1
# IDEA: BFS
# https://leetcode.com/problems/count-sub-islands/editorial/
class Solution:
    # Directions in which we can traverse inside the grids.
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

    # Helper method to check if the cell at the position (x, y) in the 'grid'
    # is a land cell.
    def is_cell_land(self, x, y, grid):
        return grid[x][y] == 1

    # Traverse all cells of island starting at position (x, y) in 'grid2',
    # and check this island is a sub-island in 'grid1'.
    def is_sub_island(self, x, y, grid1, grid2, visited):
        total_rows = len(grid2)
        total_cols = len(grid2[0])

        is_sub_island = True

        pending_cells = deque()
        # Push the starting cell in the queue and mark it as visited.
        pending_cells.append((x, y))
        visited[x][y] = True

        # Traverse on all cells using the breadth-first search method.
        while pending_cells:
            curr_x, curr_y = pending_cells.popleft()

            # If the current position cell is not a land cell in 'grid1',
            # then the current island can't be a sub-island.
            if not self.is_cell_land(curr_x, curr_y, grid1):
                is_sub_island = False

            for direction in self.directions:
                next_x = curr_x + direction[0]
                next_y = curr_y + direction[1]
                # If the next cell is inside 'grid2', is never visited and
                # is a land cell, then we traverse to the next cell.
                if (
                    0 <= next_x < total_rows
                    and 0 <= next_y < total_cols
                    and not visited[next_x][next_y]
                    and self.is_cell_land(next_x, next_y, grid2)
                ):
                    # Push the next cell in the queue and mark it as visited.
                    pending_cells.append((next_x, next_y))
                    visited[next_x][next_y] = True
        return is_sub_island

    def countSubIslands(
        self, grid1: List[List[int]], grid2: List[List[int]]
    ) -> int:
        total_rows = len(grid2)
        total_cols = len(grid2[0])

        visited = [[False] * total_cols for _ in range(total_rows)]
        sub_island_counts = 0

        # Iterate on each cell in 'grid2'
        for x in range(total_rows):
            for y in range(total_cols):
                # If cell at the position (x, y) in the 'grid2' is not visited,
                # is a land cell in 'grid2', and the island
                # starting from this cell is a sub-island in 'grid1', then we
                # increment the count of sub-islands.
                if (
                    not visited[x][y]
                    and self.is_cell_land(x, y, grid2)
                    and self.is_sub_island(x, y, grid1, grid2, visited)
                ):
                    sub_island_counts += 1

        # Return total count of sub-islands.
        return sub_island_counts




# V2-2
# IDEA: DFS
# https://leetcode.com/problems/count-sub-islands/editorial/
class Solution:
    # Directions in which we can traverse inside the grids.
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

    # Helper method to check if the cell at the position (x, y) in the 'grid'
    # is a land cell.
    def is_cell_land(self, x, y, grid):
        return grid[x][y] == 1

    # Traverse all cells of island starting at position (x, y) in 'grid2',
    # and check if this island is a sub-island in 'grid1'.
    def is_sub_island(self, x, y, grid1, grid2, visited):
        total_rows = len(grid2)
        total_cols = len(grid2[0])
        # Traverse on all cells using the depth-first search method.
        is_sub_island = True

        # If the current cell is not a land cell in 'grid1', then the current island can't be a sub-island.
        if not self.is_cell_land(x, y, grid1):
            is_sub_island = False

        # Traverse on all adjacent cells.
        for direction in self.directions:
            next_x = x + direction[0]
            next_y = y + direction[1]
            # If the next cell is inside 'grid2', is not visited, and is a land cell,
            # then we traverse to the next cell.
            if (
                0 <= next_x < total_rows
                and 0 <= next_y < total_cols
                and not visited[next_x][next_y]
                and self.is_cell_land(next_x, next_y, grid2)
            ):
                # Mark the next cell as visited.
                visited[next_x][next_y] = True
                next_cell_is_part_of_sub_island = self.is_sub_island(
                    next_x, next_y, grid1, grid2, visited
                )
                is_sub_island = (
                    is_sub_island and next_cell_is_part_of_sub_island
                )
        return is_sub_island

    def countSubIslands(
        self, grid1: List[List[int]], grid2: List[List[int]]
    ) -> int:
        total_rows = len(grid2)
        total_cols = len(grid2[0])

        visited = [[False] * total_cols for _ in range(total_rows)]
        sub_island_counts = 0

        # Iterate over each cell in 'grid2'.
        for x in range(total_rows):
            for y in range(total_cols):
                # If the cell at position (x, y) in 'grid2' is not visited,
                # is a land cell in 'grid2', and the island starting from this cell is a sub-island in 'grid1',
                # then increment the count of sub-islands.
                if not visited[x][y] and self.is_cell_land(x, y, grid2):
                    visited[x][y] = True
                    if self.is_sub_island(x, y, grid1, grid2, visited):
                        sub_island_counts += 1

        # Return total count of sub-islands.
        return sub_island_counts



# V2-3
# IDEA: UNION FIND
# https://leetcode.com/problems/count-sub-islands/editorial/
class Solution:
    # Directions in which we can traverse inside the grids.
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

    # Helper method to check if the cell at the position (x, y) in the 'grid'
    # is a land cell.
    def is_cell_land(self, x, y, grid):
        return grid[x][y] == 1

    # Union-Find class.
    class UnionFind:
        def __init__(self, n):
            self.parent = list(range(n))
            self.rank = [0] * n

        # Find the root of element 'u', using the path-compression technique.
        def find(self, u):
            if self.parent[u] != u:
                self.parent[u] = self.find(self.parent[u])
            return self.parent[u]

        # Union two components of elements 'u' and 'v' respectively based on their ranks.
        def union_sets(self, u, v):
            root_u = self.find(u)
            root_v = self.find(v)
            if root_u != root_v:
                if self.rank[root_u] > self.rank[root_v]:
                    self.parent[root_v] = root_u
                elif self.rank[root_u] < self.rank[root_v]:
                    self.parent[root_u] = root_v
                else:
                    self.parent[root_v] = root_u
                    self.rank[root_u] += 1

    # Helper method to convert (x, y) position to a 1-dimensional index.
    def convert_to_index(self, x, y, total_cols):
        return x * total_cols + y

    def countSubIslands(
        self, grid1: List[List[int]], grid2: List[List[int]]
    ) -> int:
        total_rows = len(grid2)
        total_cols = len(grid2[0])
        uf = self.UnionFind(total_rows * total_cols)

        # Traverse each land cell of 'grid2'.
        for x in range(total_rows):
            for y in range(total_cols):
                if self.is_cell_land(x, y, grid2):
                    # Union adjacent land cells with the current land cell.
                    for direction in self.directions:
                        next_x = x + direction[0]
                        next_y = y + direction[1]
                        if (
                            0 <= next_x < total_rows
                            and 0 <= next_y < total_cols
                            and self.is_cell_land(next_x, next_y, grid2)
                        ):
                            uf.union_sets(
                                self.convert_to_index(x, y, total_cols),
                                self.convert_to_index(
                                    next_x, next_y, total_cols
                                ),
                            )

        # Traverse 'grid2' land cells and mark that cell's root as not a sub-island
        # if the land cell is not present at the respective position in 'grid1'.
        is_sub_island = [True] * (total_rows * total_cols)
        for x in range(total_rows):
            for y in range(total_cols):
                if self.is_cell_land(x, y, grid2) and not self.is_cell_land(
                    x, y, grid1
                ):
                    root = uf.find(self.convert_to_index(x, y, total_cols))
                    is_sub_island[root] = False

        # Count all the sub-islands.
        sub_island_counts = 0
        for x in range(total_rows):
            for y in range(total_cols):
                if self.is_cell_land(x, y, grid2):
                    root = uf.find(self.convert_to_index(x, y, total_cols))
                    if is_sub_island[root]:
                        sub_island_counts += 1
                        # One cell can be the root of multiple land cells, so to
                        # avoid counting the same island multiple times, mark it as false.
                        is_sub_island[root] = False

        return sub_island_counts
