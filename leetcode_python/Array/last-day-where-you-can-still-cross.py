"""

1970. Last Day Where You Can Still Cross
Hard

There is a 1-based binary matrix where 0 represents land and 1 represents water. You are given integers row and col representing the number of rows and columns in the matrix, respectively.

Initially on day 0, the entire matrix is land. However, each day a new cell becomes flooded with water. You are given a 1-based 2D array cells, where cells[i] = [ri, ci] represents that on the ith day, the cell on the rith row and cith column (1-based coordinates) will be covered with water (i.e., changed to 1).

You want to find the last day that it is possible to walk from the top to the bottom by only walking on land cells. You can start from any cell in the top row and end at any cell in the bottom row. You can only travel in the four cardinal directions (left, right, up, and down).

Return the last day where it is possible to walk from the top to the bottom by only walking on land cells.


Example 1:

Input: row = 2, col = 2, cells = [[1,1],[2,1],[1,2],[2,2]]
Output: 2
Explanation: The last day where it is possible to cross from top to bottom is on day 2.

Example 2:

Input: row = 2, col = 2, cells = [[1,1],[1,2],[2,1],[2,2]]
Output: 1
Explanation: The last day where it is possible to cross from top to bottom is on day 1.

Example 3:

Input: row = 3, col = 3, cells = [[1,2],[2,1],[3,3],[2,2],[1,1],[1,3],[2,3],[3,2],[3,1]]
Output: 3
Explanation: The last day where it is possible to cross from top to bottom is on day 3.


Constraints:

2 <= row, col <= 2 * 10^4
4 <= row * col <= 2 * 10^4
cells.length == row * col
1 <= ri <= row
1 <= ci <= col
All the values of cells are unique.

"""

# V0
# IDEA : REVERSE TIME + UNION FIND (flooding is hard to undo, un-flooding is easy)
#
#   going forward, land only disappears -> union-find cannot delete edges.
#   so replay time BACKWARDS : start from the fully flooded grid and turn
#   cells[i] back into land for i = n-1, n-2, ...
#
#   two virtual nodes : s joined to every land cell in the TOP row,
#                       t joined to every land cell in the BOTTOM row.
#   the first (largest) i at which find(s) == find(t) is the answer - at that
#   moment the grid equals "after the first i floods", i.e. day i.
#
#   NOTE : we return i (not i+1) because restoring cells[i] means cells[i]
#          is still land, which is exactly the state of day i.
#
# time = O(m*n * alpha(m*n)), space = O(m*n)
class Solution(object):
    def latestDayToCross(self, row, col, cells):
        n = row * col
        parent = list(range(n + 2))
        size = [1] * (n + 2)
        s, t = n, n + 1

        def find(x):
            # iterative path compression (grid can be 2*10^4 cells deep)
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        def union(a, b):
            pa, pb = find(a), find(b)
            if pa == pb:
                return
            if size[pa] < size[pb]:
                pa, pb = pb, pa
            parent[pb] = pa
            size[pa] += size[pb]

        land = [[False] * col for _ in range(row)]
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        for i in range(len(cells) - 1, -1, -1):
            x, y = cells[i][0] - 1, cells[i][1] - 1
            land[x][y] = True
            cur = x * col + y
            for dx, dy in dirs:
                nx, ny = x + dx, y + dy
                if 0 <= nx < row and 0 <= ny < col and land[nx][ny]:
                    union(cur, nx * col + ny)
            if x == 0:
                union(cur, s)
            if x == row - 1:
                union(cur, t)
            if find(s) == find(t):
                return i
        return 0


# V0-1
# IDEA : BINARY SEARCH THE DAY + BFS ON THE LAND OF THAT DAY
#
#   "can I still cross after d floods ?" is MONOTONE in d : once a day fails,
#   every later day fails too (water only ever grows). So binary search the
#   largest d that still answers yes, and answer each question independently
#   with a plain top-row -> bottom-row BFS over the land cells.
#
#   day 0 is always crossable (the whole grid is land), so the search range
#   is [0, len(cells)] and the loop always converges.
#
# time = O(m * n * log(m * n)), space = O(m * n)
from collections import deque
class Solution(object):
    def latestDayToCross(self, row, col, cells):
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        def can(day):
            water = [[False] * col for _ in range(row)]
            for i in range(day):
                water[cells[i][0] - 1][cells[i][1] - 1] = True
            q = deque()
            for j in range(col):
                if not water[0][j]:
                    water[0][j] = True     # flooding == marking as visited
                    q.append((0, j))
            while q:
                x, y = q.popleft()
                if x == row - 1:
                    return True
                for dx, dy in dirs:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < row and 0 <= ny < col and not water[nx][ny]:
                        water[nx][ny] = True
                        q.append((nx, ny))
            return False

        lo, hi = 0, len(cells)
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if can(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo


# V0-2
# IDEA : FORWARD UNION FIND ON THE *WATER*, 8-DIRECTIONALLY
#
#   dual / "blocking wall" view : a top-to-bottom land path exists exactly
#   until the WATER forms a left-wall-to-right-wall barrier - and for a
#   barrier the water cells may touch DIAGONALLY (a diagonal water pair
#   already blocks every 4-directional land step through it).
#
#   so just flood forward in the given order, unioning each new water cell
#   with its 8 neighbours plus the virtual nodes L (column 0) and R (last
#   column). The first index i where L and R meet is the flood that kills the
#   crossing, i.e. the last good day is i (days are 1-based, cells[i] is the
#   flood of day i+1).
#
#   no reverse replay needed, and it stops as soon as the barrier appears.
#
# time = O(m * n * alpha(m * n)), space = O(m * n)
class Solution(object):
    def latestDayToCross(self, row, col, cells):
        n = row * col
        parent = list(range(n + 2))
        size = [1] * (n + 2)
        L, R = n, n + 1

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        def union(a, b):
            pa, pb = find(a), find(b)
            if pa == pb:
                return
            if size[pa] < size[pb]:
                pa, pb = pb, pa
            parent[pb] = pa
            size[pa] += size[pb]

        water = [[False] * col for _ in range(row)]
        dirs8 = ((-1, -1), (-1, 0), (-1, 1), (0, -1),
                 (0, 1), (1, -1), (1, 0), (1, 1))

        for i in range(len(cells)):
            x, y = cells[i][0] - 1, cells[i][1] - 1
            water[x][y] = True
            cur = x * col + y
            for dx, dy in dirs8:
                nx, ny = x + dx, y + dy
                if 0 <= nx < row and 0 <= ny < col and water[nx][ny]:
                    union(cur, nx * col + ny)
            if y == 0:
                union(cur, L)
            if y == col - 1:
                union(cur, R)
            if find(L) == find(R):
                return i
        return len(cells)
