"""

1036. Escape a Large Maze
Hard

There is a 1 million by 1 million grid on an XY-plane, and the coordinates of each
grid square are (x, y).

We start at the source = [sx, sy] square and want to reach the target = [tx, ty]
square. There is also an array of blocked squares, where each blocked[i] = [xi, yi]
represents a blocked square with coordinates (xi, yi).

Each move, we can walk one square north, east, south, or west if the square is not
in the array of blocked squares. We are also not allowed to walk outside of the grid.

Return true if and only if it is possible to reach the target square from the source
square through a sequence of valid moves.


Example 1:

Input: blocked = [[0,1],[1,0]], source = [0,0], target = [0,2]
Output: false
Explanation: The target square is inaccessible starting from the source square
because we cannot move.
We cannot move north or east because those squares are blocked.
We cannot move south or west because we cannot go outside of the grid.

Example 2:

Input: blocked = [], source = [0,0], target = [999999,999999]
Output: true
Explanation: Because there are no blocked cells, it is possible to reach the
target square.


Constraints:

0 <= blocked.length <= 200
blocked[i].length == 2
0 <= xi, yi < 10^6
source.length == target.length == 2
0 <= sx, sy, tx, ty < 10^6
source != target
It is guaranteed that source and target are not blocked.

"""

# V0
# IDEA : BOUNDED BFS (the key trick)
#
#  The grid is 10^6 x 10^6, so a plain BFS is impossible.
#  BUT there are at most 200 blocked cells, and with m blocked cells the
#  LARGEST region they can seal off is a "staircase" hugging a corner,
#  whose area is at most m * (m - 1) / 2.
#
#  -> run a BFS from source, and a BFS from target.
#     If a BFS visits MORE than m*(m-1)/2 cells without being stopped,
#     that side is definitely NOT enclosed (it escaped into open space).
#     Only if BOTH sides escape (or one directly reaches the other)
#     can source reach target.
#
# time = O(m^2), m = len(blocked)
# space = O(m^2)
from collections import deque
class Solution(object):
    def isEscapePossible(self, blocked, source, target):
        N = 10 ** 6
        block = set(map(tuple, blocked))
        m = len(block)

        # max area that m blocked cells can enclose
        limit = m * (m - 1) // 2

        def bfs(src, dst):
            """
            return True if we escaped (visited > limit cells)
            or we directly reached dst
            """
            dst = tuple(dst)
            start = tuple(src)
            visited = set([start])
            q = deque([start])
            while q and len(visited) <= limit:
                x, y = q.popleft()
                for dx, dy in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < N and 0 <= ny < N:
                        if (nx, ny) in block or (nx, ny) in visited:
                            continue
                        if (nx, ny) == dst:
                            return True
                        visited.add((nx, ny))
                        q.append((nx, ny))
            # ran out of cells -> enclosed ; else -> escaped
            return len(visited) > limit

        return bfs(source, target) and bfs(target, source)
