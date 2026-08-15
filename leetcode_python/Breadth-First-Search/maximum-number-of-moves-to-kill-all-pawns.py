"""

3283. Maximum Number of Moves to Kill All Pawns
Hard

There is a 50 x 50 chessboard with one knight and some pawns on it. You are given two integers kx and ky where (kx, ky) denotes the position of the knight, and a 2D array positions where positions[i] = [x_i, y_i] denotes the position of the pawns on the chessboard.

Alice and Bob play a turn-based game, where Alice goes first. In each player's turn:

The player selects a pawn that still exists on the board and captures it with the knight in the fewest possible moves. Note that the player can select any pawn, it might not be one that can be captured in the least number of moves.
In the above scenario, after capturing the pawn, there might be some other pawns on the board.

Alice is trying to maximize the sum of the number of moves made by both players until there are no more pawns on the board, whereas Bob tries to minimize them.

Return the maximum total number of moves made during the game that Alice can achieve, assuming both players play optimally.

Note that in one move, a chess knight has eight possible positions it can move to.


Example 1:

Input: kx = 1, ky = 1, positions = [[0,0]]
Output: 4
Explanation:
The knight takes 4 moves to reach the pawn at (0, 0).

Example 2:

Input: kx = 0, ky = 2, positions = [[1,1],[2,2],[3,3]]
Output: 8
Explanation:
Alice picks the pawn at (2, 2) and captures it in two moves.
Bob picks the pawn at (3, 3) and captures it in two moves.
Alice picks the pawn at (1, 1) and captures it in four moves.

Example 3:

Input: kx = 0, ky = 0, positions = [[1,2],[2,4]]
Output: 3
Explanation:
Alice picks the pawn at (2, 4) and captures it in two moves.
Bob picks the pawn at (1, 2) and captures it in one move.


Constraints:

0 <= kx, ky <= 49
1 <= positions.length <= 15
positions[i].length == 2
0 <= positions[i][0], positions[i][1] <= 49
All positions[i] are unique.
The input is generated such that positions[i] != [kx, ky].

"""

# V0
# IDEA : PRECOMPUTE KNIGHT DISTANCES, THEN A MINIMAX BITMASK DP
#
#   the capture cost between two squares is the knight's shortest path, which
#   BFS gives — and only 16 sources matter (the knight plus each pawn), so
#   16 BFS runs over a 50x50 board settle every distance.
#
#   after that the game is pure combinatorics : state = (where the knight
#   stands, which pawns remain). the mover picks a pawn, pays its distance,
#   and the knight relocates there.
#
#       Alice's turn -> take the MAXIMUM over the remaining pawns
#       Bob's turn   -> take the MINIMUM
#
#   whose turn it is follows from the parity of how many pawns are gone, so
#   no extra state dimension is needed. 15 pawns give 2^15 * 16 states.
#
# time = O(16 * 50^2 + 2^n * n^2), space = O(2^n * n)
from collections import deque


class Solution(object):
    def maxMoves(self, kx, ky, positions):
        n = len(positions)
        sources = [(kx, ky)] + [tuple(p) for p in positions]
        SIZE = 50
        MOVES = ((1, 2), (2, 1), (-1, 2), (-2, 1),
                 (1, -2), (2, -1), (-1, -2), (-2, -1))

        def bfs(sx, sy):
            dist = [[-1] * SIZE for _ in range(SIZE)]
            dist[sx][sy] = 0
            q = deque([(sx, sy)])
            while q:
                x, y = q.popleft()
                for dx, dy in MOVES:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < SIZE and 0 <= ny < SIZE and dist[nx][ny] == -1:
                        dist[nx][ny] = dist[x][y] + 1
                        q.append((nx, ny))
            return dist

        # cost[a][b] : moves from source a to pawn b
        grids = [bfs(x, y) for x, y in sources]
        cost = [[grids[a][positions[b][0]][positions[b][1]] for b in range(n)]
                for a in range(n + 1)]

        full = (1 << n) - 1
        memo = {}

        def play(at, taken):
            if taken == full:
                return 0
            key = (at, taken)
            if key in memo:
                return memo[key]
            alice = bin(taken).count('1') % 2 == 0
            best = None
            for b in range(n):
                if (taken >> b) & 1:
                    continue
                cand = cost[at][b] + play(b + 1, taken | (1 << b))
                if best is None:
                    best = cand
                elif alice:
                    best = max(best, cand)
                else:
                    best = min(best, cand)
            memo[key] = best
            return best

        return play(0, 0)
