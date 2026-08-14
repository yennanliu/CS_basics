r"""

959. Regions Cut By Slashes
Medium

An n x n grid is composed of 1 x 1 squares where each 1 x 1 square consists of a '/', '\', or blank space ' '. These characters divide the square into contiguous regions.

Given the grid grid represented as a string array, return the number of regions.

Note that backslash characters are escaped, so a '\' is represented as '\\'.

Example 1:

Input: grid = [" /","/ "]
Output: 2

Example 2:

Input: grid = [" /","  "]
Output: 1

Example 3:

Input: grid = ["/\\","\\/"]
Output: 5
Explanation: Recall that because \ characters are escaped, "\\/" refers to \/, and "/\\" refers to /\.

Constraints:

n == grid.length == grid[i].length
1 <= n <= 30
grid[i][j] is either '/', '\', or ' '.

"""

# V0
# IDEA : UNION FIND on 4 TRIANGLES per CELL
#
#  Split every 1x1 cell into 4 triangles, indexed clockwise:
#
#        \  0  /
#         \   /
#      3    X    1
#         /   \
#        /  2  \
#
#    0 = top, 1 = right, 2 = bottom, 3 = left
#
#  Inside a cell:
#     '/'   -> merge (0,3) and (1,2)
#     '\'   -> merge (0,1) and (2,3)
#     ' '   -> merge all four
#
#  Between cells:
#     right neighbour  -> this cell's 1  with neighbour's 3
#     bottom neighbour -> this cell's 2  with neighbour's 0
#
#  Start with 4*n*n components and decrement on every successful union;
#  what is left is the number of regions.
#
# time = O(n^2 * a(n^2))
# space = O(n^2)
class Solution(object):
    def regionsBySlashes(self, grid):
        n = len(grid)
        parent = list(range(4 * n * n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return False
            parent[ra] = rb
            return True

        count = 4 * n * n
        for i in range(n):
            for j in range(n):
                base = 4 * (i * n + j)
                ch = grid[i][j]

                if ch == '/':
                    if union(base + 0, base + 3):
                        count -= 1
                    if union(base + 1, base + 2):
                        count -= 1
                elif ch == '\\':
                    if union(base + 0, base + 1):
                        count -= 1
                    if union(base + 2, base + 3):
                        count -= 1
                else:
                    if union(base + 0, base + 1):
                        count -= 1
                    if union(base + 1, base + 2):
                        count -= 1
                    if union(base + 2, base + 3):
                        count -= 1

                # stitch to the cell on the right
                if j + 1 < n:
                    if union(base + 1, 4 * (i * n + j + 1) + 3):
                        count -= 1
                # stitch to the cell below
                if i + 1 < n:
                    if union(base + 2, 4 * ((i + 1) * n + j) + 0):
                        count -= 1

        return count
