"""

2814. Minimum Time Takes to Reach Destination Without Drowning
Hard

You are given an n * m 0-indexed grid of string land. Right now, you are standing at the cell that contains "S", and you want to get to the cell containing "D". There are three other types of cells in this land:

".": These cells are empty.
"X": These cells are stone.
"*": These cells are flooded.

At each second, you can move to a cell that shares a side with your current cell (if it exists). Also, at each second, every empty cell that shares a side with a flooded cell becomes flooded as well.

There are two problems ahead of your journey:

You can't step on stone cells.
You can't step on flooded cells since you will drown (also, you can't step on a cell that will be flooded at the same time as you step on it).

Return the minimum time it takes you to reach the destination in seconds, or -1 if it is impossible.

Note that the destination will never be flooded.


Example 1:

Input: land = [["D",".","*"],[".",".","."],[".","S","."]]
Output: 3
Explanation: The picture below shows the simulation of the land second by second. The blue cells are flooded, and the gray cells are stone.
Picture (0) shows the initial state and picture (3) shows the final state when we reach destination. As you see, it takes us 3 second to reach destination and the answer would be 3.
It can be shown that 3 is the minimum time needed to reach from S to D.

Example 2:

Input: land = [["D","X","*"],[".",".","."],[".",".","S"]]
Output: -1
Explanation: The picture below shows the simulation of the land second by second. The blue cells are flooded, and the gray cells are stone.
Picture (0) shows the initial state. As you see, no matter which paths we choose, we will drown at the 3rd second. Also the minimum path takes us 4 seconds to reach from S to D.
So the answer would be -1.

Example 3:

Input: land = [["D",".",".",".","*","."],[".","X",".","X",".","."],[".",".",".",".","S","."]]
Output: 6
Explanation: It can be shown that we can reach destination in 6 seconds. Also it can be shown that 6 is the minimum seconds one need to reach from S to D.


Constraints:

2 <= n, m <= 100
land consists only of "S", "D", ".", "*" and "X".
Exactly one of the cells is equal to "S".
Exactly one of the cells is equal to "D".

"""

# V0
# IDEA : TWO BFS - FLOOD SPREAD FIRST, THEN THE WALKER
#
#   The water does not care about us, so precompute it once:
#     BFS 1 : multi-source BFS from every "*" cell gives
#             flood[r][c] = the second at which (r, c) becomes flooded
#             (INF if it never does). Stone "X" blocks the water, and "D" is
#             stated to never flood, so both are treated as non-floodable.
#
#     BFS 2 : ordinary shortest-path BFS for the walker starting at "S" with
#             t = 0. Stepping into (x, y) happens at second t + 1 and is legal
#             only when the cell is not stone and flood[x][y] > t + 1.
#
#   NOTE : the inequality is STRICT - the statement forbids stepping onto a
#          cell that floods at the very same second, so flood[x][y] == t + 1
#          is a drowning move.
#   NOTE : "D" is exempt from the flood check entirely.
#   NOTE : BFS 2 visits each cell at most once because the walker's arrival
#          times are non-decreasing; a later arrival is never more permissive
#          than the first one, so no re-visits are needed.
#
# time = O(n * m), space = O(n * m)
from collections import deque


class Solution(object):
    def minimumSeconds(self, land):
        n, m = len(land), len(land[0])
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))
        INF = float('inf')

        # --- BFS 1 : flood spreading ---
        flood = [[INF] * m for _ in range(n)]
        q = deque()
        start = dest = None
        for i in range(n):
            for j in range(m):
                ch = land[i][j]
                if ch == '*':
                    flood[i][j] = 0
                    q.append((i, j))
                elif ch == 'S':
                    start = (i, j)
                elif ch == 'D':
                    dest = (i, j)
        while q:
            i, j = q.popleft()
            t = flood[i][j] + 1
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < m and flood[x][y] == INF:
                    if land[x][y] == 'X' or land[x][y] == 'D':
                        continue
                    flood[x][y] = t
                    q.append((x, y))

        # --- BFS 2 : the walker ---
        seen = [[False] * m for _ in range(n)]
        seen[start[0]][start[1]] = True
        q = deque([(start[0], start[1], 0)])
        while q:
            i, j, t = q.popleft()
            if (i, j) == dest:
                return t
            for di, dj in dirs:
                x, y = i + di, j + dj
                if not (0 <= x < n and 0 <= y < m) or seen[x][y]:
                    continue
                if land[x][y] == 'X':
                    continue
                if land[x][y] != 'D' and flood[x][y] <= t + 1:
                    continue
                seen[x][y] = True
                q.append((x, y, t + 1))
        return -1
