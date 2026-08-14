"""

2257. Count Unguarded Cells in the Grid
Medium

You are given two integers m and n representing a 0-indexed m x n grid. You are also given two 2D integer arrays guards and walls where guards[i] = [rowi, coli] and walls[j] = [rowj, colj] represent the positions of the ith guard and jth wall respectively.

A guard can see every cell in the four cardinal directions (north, east, south, or west) starting from their position unless obstructed by a wall or another guard. A cell is guarded if there is at least one guard that can see it.

Return the number of unoccupied cells that are not guarded.


Example 1:

Input: m = 4, n = 6, guards = [[0,0],[1,1],[2,3]], walls = [[0,1],[2,2],[1,4]]
Output: 7
Explanation: The guarded and unguarded cells are shown in red and green respectively in the above diagram.
There are a total of 7 unguarded cells, so we return 7.

Example 2:

Input: m = 3, n = 3, guards = [[1,1]], walls = [[0,1],[1,2],[2,1]]
Output: 4
Explanation: The unguarded cells are shown in green in the above diagram.
There are a total of 4 unguarded cells, so we return 4.


Constraints:

1 <= m, n <= 10^5
2 <= m * n <= 10^5
1 <= guards.length, walls.length <= 5 * 10^4
2 <= guards.length + walls.length <= m * n
guards[j].length == walls[j].length == 2
0 <= rowi, rowj < m
0 <= coli, colj < n
All the positions in guards and walls are unique.

"""

# V0
# IDEA : RAY-CAST FROM EACH GUARD, STOPPING AT ANY OCCUPIED CELL
#
#   mark the grid with G (guard), W (wall), S (seen) and 0 (free). from every
#   guard walk the four directions, marking S until a G or a W blocks the
#   line of sight. an already-seen S does NOT block, so the walk continues
#   through it.
#
#   the answer is the count of cells still 0 at the end.
#
#   NOTE : total work is bounded because each guard's rays are cut short by
#          the first obstruction; m * n <= 10^5 keeps the grid itself small
#          even when one dimension is 10^5.
#
# time = O(m * n + guards * (m + n)), space = O(m * n)
class Solution(object):
    def countUnguarded(self, m, n, guards, walls):
        GUARD, WALL, SEEN, FREE = 1, 2, 3, 0
        grid = [[FREE] * n for _ in range(m)]
        for r, c in guards:
            grid[r][c] = GUARD
        for r, c in walls:
            grid[r][c] = WALL

        for r, c in guards:
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = r + dr, c + dc
                while 0 <= x < m and 0 <= y < n and grid[x][y] not in (GUARD, WALL):
                    grid[x][y] = SEEN
                    x += dr
                    y += dc

        return sum(1 for i in range(m) for j in range(n) if grid[i][j] == FREE)
