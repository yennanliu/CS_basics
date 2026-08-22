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


# V0-1
# IDEA : 4 LINE SWEEPS WITH A "LIT" FLAG (NO PER-GUARD RAYS AT ALL)
#
#   instead of walking outward from every guard, walk each line ONCE in each
#   of the 4 directions carrying a single boolean `lit`:
#     - hitting a guard  -> lit = True   (everything after it is watched)
#     - hitting a wall   -> lit = False  (sight is cut)
#     - a free cell      -> mark it seen when lit
#
#   two passes per row (left->right, right->left) and two per column
#   (top->bottom, bottom->top) cover all four viewing directions, so the total
#   work is a fixed 4 * m * n regardless of how many guards there are.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countUnguarded(self, m, n, guards, walls):
        GUARD, WALL = 1, 2
        blocked = [[0] * n for _ in range(m)]
        for r, c in guards:
            blocked[r][c] = GUARD
        for r, c in walls:
            blocked[r][c] = WALL
        seen = [[False] * n for _ in range(m)]

        for r in range(m):
            lit = False
            for c in range(n):
                b = blocked[r][c]
                if b:
                    lit = (b == GUARD)
                elif lit:
                    seen[r][c] = True
            lit = False
            for c in range(n - 1, -1, -1):
                b = blocked[r][c]
                if b:
                    lit = (b == GUARD)
                elif lit:
                    seen[r][c] = True

        for c in range(n):
            lit = False
            for r in range(m):
                b = blocked[r][c]
                if b:
                    lit = (b == GUARD)
                elif lit:
                    seen[r][c] = True
            lit = False
            for r in range(m - 1, -1, -1):
                b = blocked[r][c]
                if b:
                    lit = (b == GUARD)
                elif lit:
                    seen[r][c] = True

        return sum(1 for r in range(m) for c in range(n)
                   if not blocked[r][c] and not seen[r][c])


# V0-2
# IDEA : BRUTE FORCE, ASKED PER CELL — LOOK BACK ALONG THE 4 DIRECTIONS
#
#   turn the question around: for each unoccupied cell, walk outward in the
#   4 directions and see WHO is the first occupied cell met. a guard means the
#   cell is watched, a wall means that direction is dead. the cell survives
#   only if all 4 walks end on a wall or on the border.
#
#   obstacles live in a dict, so no m x n grid is allocated — the price is
#   O(m + n) probing per cell.
#
# time = O(m * n * (m + n)), space = O(guards + walls)
class Solution(object):
    def countUnguarded(self, m, n, guards, walls):
        GUARD, WALL = 1, 2
        occupied = {}
        for r, c in guards:
            occupied[(r, c)] = GUARD
        for r, c in walls:
            occupied[(r, c)] = WALL

        res = 0
        for r in range(m):
            for c in range(n):
                if (r, c) in occupied:
                    continue
                watched = False
                for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    x, y = r + dr, c + dc
                    while 0 <= x < m and 0 <= y < n:
                        hit = occupied.get((x, y), 0)
                        if hit:
                            watched = (hit == GUARD)
                            break
                        x += dr
                        y += dc
                    if watched:
                        break
                if not watched:
                    res += 1
        return res
