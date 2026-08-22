"""

1992. Find All Groups of Farmland
Medium

You are given a 0-indexed m x n binary matrix land where a 0 represents a hectare of forested land and a 1 represents a hectare of farmland.

To keep the land organized, there are designated rectangular areas of hectares that consist entirely of farmland. These rectangular areas are called groups. No two groups are adjacent, meaning farmland in one group is not four-directionally adjacent to another farmland in a different group.

land can be represented by a coordinate system where the top left corner of land is (0, 0) and the bottom right corner of land is (m-1, n-1). Find the coordinates of the top left and bottom right corner of each group of farmland. A group of farmland with a top left corner at (r1, c1) and a bottom right corner at (r2, c2) is represented by the 4-length array [r1, c1, r2, c2].

Return a 2D array containing the 4-length arrays described above for each group of farmland in land. If there are no groups of farmland, return an empty array. You may return the answer in any order.


Example 1:

Input: land = [[1,0,0],[0,1,1],[0,1,1]]
Output: [[0,0,0,0],[1,1,2,2]]
Explanation:
The first group has a top left corner at land[0][0] and a bottom right corner at land[0][0].
The second group has a top left corner at land[1][1] and a bottom right corner at land[2][2].

Example 2:

Input: land = [[1,1],[1,1]]
Output: [[0,0,1,1]]
Explanation:
The first group has a top left corner at land[0][0] and a bottom right corner at land[1][1].

Example 3:

Input: land = [[0]]
Output: []
Explanation:
There are no groups of farmland.


Constraints:

m == land.length
n == land[i].length
1 <= m, n <= 300
land consists of only 0's and 1's.
Groups of farmland are rectangular in shape.

"""

# V0
# IDEA : SCAN FOR TOP-LEFT CORNERS, THEN WALK THE RECTANGLE EDGES
#
#   because every group is a solid rectangle and groups never touch, a cell
#   (i, j) is the TOP-LEFT corner of a group iff
#       land[i][j] == 1  and  land[i][j-1] != 1  and  land[i-1][j] != 1
#
#   from such a corner just walk down column j while cells stay 1 to find r2,
#   then walk right along row r2 to find c2 - no flood fill needed.
#
#   NOTE : the "no two groups are adjacent" guarantee is what makes the
#          corner test and the two straight walks sufficient.
#
# time = O(m * n), space = O(1) besides the output
class Solution(object):
    def findFarmland(self, land):
        m, n = len(land), len(land[0])
        res = []
        for i in range(m):
            for j in range(n):
                if land[i][j] == 0:
                    continue
                if j > 0 and land[i][j - 1] == 1:
                    continue
                if i > 0 and land[i - 1][j] == 1:
                    continue
                x, y = i, j
                while x + 1 < m and land[x + 1][j] == 1:
                    x += 1
                while y + 1 < n and land[x][y + 1] == 1:
                    y += 1
                res.append([i, j, x, y])
        return res


# V0-1
# IDEA : BFS FLOOD FILL, TRACKING THE BOUNDING BOX OF EACH COMPONENT
#
#   ignore the "every group is a rectangle" guarantee entirely: treat the 1s as
#   a 4-directional graph, BFS out of every unvisited 1, and record the min/max
#   row and column reached. for a solid rectangle that bounding box IS the
#   group, so the same answer falls out of a generic connected-component walk.
#
#   NOTE : a cell is marked visited when it is PUSHED, so nothing can enter the
#          queue twice.
#
# time = O(m * n)
# space = O(m * n)
import collections
class Solution(object):
    def findFarmland(self, land):
        m, n = len(land), len(land[0])
        seen = [[False] * n for _ in range(m)]
        res = []
        for i in range(m):
            for j in range(n):
                if land[i][j] == 0 or seen[i][j]:
                    continue
                seen[i][j] = True
                q = collections.deque([(i, j)])
                r1 = r2 = i
                c1 = c2 = j
                while q:
                    x, y = q.popleft()
                    r1, r2 = min(r1, x), max(r2, x)
                    c1, c2 = min(c1, y), max(c2, y)
                    for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        a, b = x + dx, y + dy
                        if 0 <= a < m and 0 <= b < n and \
                                land[a][b] == 1 and not seen[a][b]:
                            seen[a][b] = True
                            q.append((a, b))
                res.append([r1, c1, r2, c2])
        return res


# V0-2
# IDEA : UNION-FIND OVER THE 1 CELLS, THEN ONE BOUNDING BOX PER ROOT
#
#   flatten (i, j) to i * n + j, union each 1 with its RIGHT and DOWN neighbour
#   when that neighbour is also a 1 (left/up are the same edges seen from the
#   other end), then bucket the cells by root and reduce each bucket to
#   [min row, min col, max row, max col].
#
#   NOTE : no recursion and no queue - the grouping is done by the disjoint-set
#          structure instead of by a traversal order.
#
# time = O(m * n * alpha(m * n))
# space = O(m * n)
class Solution(object):
    def findFarmland(self, land):
        m, n = len(land), len(land[0])
        par = list(range(m * n))

        def find(x):
            while par[x] != x:
                par[x] = par[par[x]]
                x = par[x]
            return x

        for i in range(m):
            for j in range(n):
                if land[i][j] == 0:
                    continue
                for a, b in ((i + 1, j), (i, j + 1)):
                    if a < m and b < n and land[a][b] == 1:
                        ra, rb = find(i * n + j), find(a * n + b)
                        if ra != rb:
                            par[ra] = rb
        box = {}
        for i in range(m):
            for j in range(n):
                if land[i][j] == 0:
                    continue
                r = find(i * n + j)
                if r not in box:
                    box[r] = [i, j, i, j]
                else:
                    cur = box[r]
                    cur[0] = min(cur[0], i)
                    cur[1] = min(cur[1], j)
                    cur[2] = max(cur[2], i)
                    cur[3] = max(cur[3], j)
        return list(box.values())
