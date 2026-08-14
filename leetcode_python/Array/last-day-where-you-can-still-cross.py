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
