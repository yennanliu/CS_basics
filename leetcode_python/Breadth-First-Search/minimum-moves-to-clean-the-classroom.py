"""

3568. Minimum Moves to Clean the Classroom
Medium

You are given an m x n grid classroom where a student volunteer is tasked with cleaning up litter scattered around the room. Each cell in the grid is one of the following:

'S': Starting position of the student.
'L': Litter that must be collected (once collected, the cell becomes an empty cell).
'R': Reset area that restores the student's energy to full capacity, regardless of their current energy level (can be used multiple times).
'X': Obstacle the student cannot pass through.
'.': Empty space.

You are also given an integer energy, representing the maximum energy the student can have. The student starts with this energy from the starting position.

Each move to an adjacent cell (up, down, left, or right) costs 1 unit of energy. If the energy reaches zero, the student can only continue if they are on a reset area 'R', which sets the energy back to the maximum value energy.

Return the minimum number of moves required to collect all litter items, or -1 if it's impossible.


Example 1:

Input: classroom = ["S.", "XL"], energy = 2
Output: 2
Explanation:
The student starts at (0, 0) with 2 units of energy.
Move right to (0, 1), costing 1 unit of energy, leaving 1 unit of energy.
Move down to (1, 1) to collect the litter 'L'.
The student collects all the litter using 2 moves. Thus, the output is 2.

Example 2:

Input: classroom = ["LS", "RL"], energy = 4
Output: 3
Explanation:
The student starts at (0, 1) with 4 units of energy.
Move down to (1, 1) to collect the litter 'L'.
Move left to (1, 0) which is a reset area 'R', restoring the energy back to full.
Move up to (0, 0) to collect the remaining litter 'L'.
The student collects all the litter using 3 moves. Thus, the output is 3.

Example 3:

Input: classroom = ["L.S", "RXL"], energy = 3
Output: -1
Explanation:
No valid path collects all 'L'.


Constraints:

1 <= m == classroom.length <= 20
1 <= n == classroom[i].length <= 20
classroom[i][j] is one of 'S', 'L', 'R', 'X', or '.'
1 <= energy <= 50
There is exactly one 'S' in the grid.
There are at most 10 'L' cells in the grid.

"""

# V0
# IDEA : BFS OVER (ROW, COL, COLLECTED-MASK) KEEPING THE BEST ENERGY
#
#   at most 10 litters means the "which litter is already picked up" part of
#   the state is a 10-bit mask. energy is not part of the state key: for the
#   same (cell, mask) more energy is never worse, so we only re-expand a
#   state when we reach it with strictly more energy than before.
#
#   that dominance rule keeps the search finite while plain BFS layers still
#   give the minimum number of moves, because every move costs exactly 1.
#
# time = O(m * n * 2^L * energy), space = O(m * n * 2^L)
from collections import deque


class Solution(object):
    def minMoves(self, classroom, energy):
        m, n = len(classroom), len(classroom[0])
        litter = {}
        sr = sc = 0
        for i in range(m):
            for j in range(n):
                ch = classroom[i][j]
                if ch == 'S':
                    sr, sc = i, j
                elif ch == 'L':
                    litter[(i, j)] = len(litter)
        full = (1 << len(litter)) - 1
        if full == 0:
            return 0

        best = [[[-1] * (full + 1) for _ in range(n)] for _ in range(m)]
        best[sr][sc][0] = energy
        q = deque([(sr, sc, 0, energy)])
        steps = 0
        while q:
            for _ in range(len(q)):
                r, c, mask, e = q.popleft()
                if e == 0:
                    continue
                for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    nr, nc = r + dr, c + dc
                    if nr < 0 or nr >= m or nc < 0 or nc >= n:
                        continue
                    ch = classroom[nr][nc]
                    if ch == 'X':
                        continue
                    ne = e - 1
                    nmask = mask
                    if ch == 'L':
                        nmask = mask | (1 << litter[(nr, nc)])
                    elif ch == 'R':
                        ne = energy
                    if nmask == full:
                        return steps + 1
                    if best[nr][nc][nmask] < ne:
                        best[nr][nc][nmask] = ne
                        q.append((nr, nc, nmask, ne))
            steps += 1
        return -1
