"""

3238. Find the Number of Winning Players
Easy

You are given an integer n representing the number of players in a game and a 2D array pick where pick[i] = [x_i, y_i] represents that the player x_i picked a ball of color y_i.

Player i wins the game if they pick strictly more than i balls of the same color. In other words,

Player 0 wins if they pick any ball.
Player 1 wins if they pick at least two balls of the same color.
...
Player i wins if they pick at least i + 1 balls of the same color.

Return the number of players who win the game.

Note that multiple players can win the game.


Example 1:

Input: n = 4, pick = [[0,0],[1,0],[1,0],[2,1],[2,1],[2,0]]
Output: 2
Explanation:
Player 0 and player 1 win the game, while players 2 and 3 do not win.

Example 2:

Input: n = 5, pick = [[1,1],[1,2],[1,3],[1,4]]
Output: 0
Explanation:
Player 1 and others do not win the game.

Example 3:

Input: n = 5, pick = [[1,1],[2,4],[2,4],[2,4]]
Output: 1
Explanation:
Player 2 wins the game by picking 3 balls with color 4.


Constraints:

2 <= n <= 10
1 <= pick.length <= 100
pick[i].length == 2
0 <= x_i <= n - 1
0 <= y_i <= 10

"""

# V0
# IDEA : TALLY (PLAYER, COLOR) PAIRS, THEN COMPARE EACH PLAYER'S BEST COLOR
#
#   player i needs i + 1 balls of ONE colour, so only the largest per-colour
#   count matters. with at most 10 players and 11 colours, a small table is
#   enough.
#
# time = O(len(pick) + n * colors), space = O(n * colors)
from collections import defaultdict


class Solution(object):
    def winningPlayerCount(self, n, pick):
        cnt = defaultdict(int)
        for x, y in pick:
            cnt[(x, y)] += 1

        best = [0] * n
        for (x, _), c in cnt.items():
            if c > best[x]:
                best[x] = c
        return sum(1 for i in range(n) if best[i] >= i + 1)
