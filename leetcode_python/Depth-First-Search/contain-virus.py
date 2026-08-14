"""

749. Contain Virus
Hard

A virus is spreading rapidly, and your task is to quarantine the infected area by
installing walls.

The world is modeled as an m x n binary grid isInfected, where isInfected[i][j] == 0
represents uninfected cells, and isInfected[i][j] == 1 represents cells contaminated
with the virus. A wall (and only one wall) can be installed between any two
4-directionally adjacent cells, on the shared boundary.

Every night, the virus spreads to all neighboring cells in all four directions unless
blocked by a wall. Resources are limited. Each day, you can install walls around only
one region (i.e., the affected area (continuous block of infected cells) that threatens
the most uninfected cells the following night). There will never be a tie.

Return the number of walls used to quarantine all the infected regions.
If the world will become fully infected, return the number of walls used.


Example 1:

Input: isInfected = [[0,1,0,0,0,0,0,1],[0,1,0,0,0,0,0,1],[0,0,0,0,0,0,0,1],[0,0,0,0,0,0,0,0]]
Output: 10
Explanation: There are 2 contaminated regions.
On the first day, add 5 walls to quarantine the viral region on the left.
On the second day, add 5 walls to quarantine the viral region on the right.
The virus is fully contained.

Example 2:

Input: isInfected = [[1,1,1],[1,0,1],[1,1,1]]
Output: 4
Explanation: Even though there is only one cell saved, there are 4 walls built.
Notice that walls are only built on the shared boundary of two different cells.

Example 3:

Input: isInfected = [[1,1,1,0,0,0,0,0,0],[1,0,1,0,1,1,1,1,1],[1,1,1,0,0,0,0,0,0]]
Output: 13
Explanation: The region on the left only builds two new walls.


Constraints:

m == isInfected.length
n == isInfected[i].length
1 <= m, n <= 50
isInfected[i][j] is either 0 or 1.
There is always a contiguous viral region throughout the described process that will
infect strictly more uncontaminated squares in the next round.

"""

# V0
# IDEA : DFS (FLOOD FILL) PER REGION + ROUND BY ROUND SIMULATION
#
#   Each day:
#     1) flood fill every connected region of 1s, recording for each region
#          - its cells
#          - the SET of distinct uninfected neighbours it threatens (frontier)
#          - the COUNT of infected/uninfected adjacencies (= walls needed;
#            one 0-cell touched from 3 sides needs 3 walls)
#     2) quarantine the region with the largest frontier: add its wall count and
#        mark its cells 2 (walled off forever, and 2 is neither 0 nor 1 so it
#        naturally blocks later flood fills)
#     3) every OTHER region spreads: each of its frontier cells becomes infected
#   Stop once no region threatens any uninfected cell.
#
# time = O((m*n)^2) — at most O(m*n) rounds, each scanning the whole grid
# space = O(m*n)
class Solution(object):
    def containVirus(self, isInfected):
        m, n = len(isInfected), len(isInfected[0])
        DIRS = ((-1, 0), (1, 0), (0, -1), (0, 1))
        total_walls = 0

        while True:
            seen = [[False] * n for _ in range(m)]
            regions = []  # (cells, frontier_set, wall_count)

            for i in range(m):
                for j in range(n):
                    if isInfected[i][j] != 1 or seen[i][j]:
                        continue

                    cells = []
                    frontier = set()
                    walls = 0

                    # iterative DFS over one contaminated region
                    stack = [(i, j)]
                    seen[i][j] = True
                    while stack:
                        r, c = stack.pop()
                        cells.append((r, c))
                        for dr, dc in DIRS:
                            nr, nc = r + dr, c + dc
                            if not (0 <= nr < m and 0 <= nc < n):
                                continue
                            if isInfected[nr][nc] == 1 and not seen[nr][nc]:
                                seen[nr][nc] = True
                                stack.append((nr, nc))
                            elif isInfected[nr][nc] == 0:
                                frontier.add((nr, nc))  # distinct cells threatened
                                walls += 1              # one wall per shared boundary

                    regions.append((cells, frontier, walls))

            # a region with no frontier is already sealed in -> nothing left to do
            regions = [reg for reg in regions if reg[1]]
            if not regions:
                break

            # quarantine the region threatening the most uninfected cells
            target = max(regions, key=lambda reg: len(reg[1]))
            total_walls += target[2]
            for r, c in target[0]:
                isInfected[r][c] = 2  # 2 = walled off, blocks neighbours forever

            # all remaining regions spread by one cell overnight
            for reg in regions:
                if reg is target:
                    continue
                for r, c in reg[1]:
                    isInfected[r][c] = 1

        return total_walls
