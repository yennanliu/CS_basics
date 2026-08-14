"""

1591. Strange Printer II
Hard

There is a strange printer with the following two special requirements:

On each turn, the printer will print a solid rectangular pattern of a single color on the grid. This will cover up the existing colors in the rectangle.
Once the printer has used a color for the above operation, the same color cannot be used again.

You are given a m x n matrix targetGrid, where targetGrid[row][col] is the color in the position (row, col) of the grid.

Return true if it is possible to print the matrix targetGrid, otherwise, return false.

Example 1:

Input: targetGrid = [[1,1,1,1],[1,2,2,1],[1,2,2,1],[1,1,1,1]]
Output: true

Example 2:

Input: targetGrid = [[1,1,1,1],[1,1,3,3],[1,1,3,4],[5,5,1,4]]
Output: true

Example 3:

Input: targetGrid = [[1,2,1],[2,1,2],[1,2,1]]
Output: false
Explanation: It is impossible to form targetGrid because it is not allowed to print the same color in different turns.

Constraints:

m == targetGrid.length
n == targetGrid[i].length
1 <= m, n <= 60
1 <= targetGrid[row][col] <= 60

"""

# V0
# IDEA : BOUNDING BOX + TOPOLOGICAL SORT (colour a must be printed before b)
#
#   every colour is one rectangle, and its rectangle is exactly the
#   bounding box of its cells (a smaller one would not cover them).
#   any OTHER colour d seen inside colour c's box was printed later
#   (it covered c) -> directed edge c -> d.
#   the grid is printable iff that dependency graph is acyclic, which a
#   Kahn topological sort decides.
#
# time = O(C * m * n), space = O(m * n + C^2), C <= 60 colours
from collections import deque
class Solution(object):
    def isPrintable(self, targetGrid):
        m, n = len(targetGrid), len(targetGrid[0])
        C = 61
        top = [m] * C
        left = [n] * C
        bottom = [-1] * C
        right = [-1] * C
        colors = set()
        for i in range(m):
            for j in range(n):
                c = targetGrid[i][j]
                colors.add(c)
                if i < top[c]:
                    top[c] = i
                if i > bottom[c]:
                    bottom[c] = i
                if j < left[c]:
                    left[c] = j
                if j > right[c]:
                    right[c] = j

        adj = [set() for _ in range(C)]
        indeg = [0] * C
        for c in colors:
            for i in range(top[c], bottom[c] + 1):
                for j in range(left[c], right[c] + 1):
                    d = targetGrid[i][j]
                    if d != c and d not in adj[c]:
                        adj[c].add(d)
                        indeg[d] += 1

        q = deque(c for c in colors if indeg[c] == 0)
        done = 0
        while q:
            c = q.popleft()
            done += 1
            for d in adj[c]:
                indeg[d] -= 1
                if indeg[d] == 0:
                    q.append(d)
        return done == len(colors)
