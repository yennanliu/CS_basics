"""

3021. Alice and Bob Playing Flower Game
Medium

Alice and Bob are playing a turn-based game on a circular field surrounded by flowers. The circle represents the field, and there are x flowers in the clockwise direction between Alice and Bob, and y flowers in the anti-clockwise direction between them.

The game proceeds as follows:

1. Alice takes the first turn.
2. In each turn, a player must choose either the clockwise or anti-clockwise direction and pick one flower from that side.
3. At the end of the turn, if there are no flowers left at all, the current player captures their opponent and wins the game.

Given two integers, n and m, the task is to compute the number of possible pairs (x, y) that satisfy the conditions:

Alice must win the game according to the described rules.
The number of flowers x in the clockwise direction must be in the range [1,n].
The number of flowers y in the anti-clockwise direction must be in the range [1,m].

Return the number of possible pairs (x, y) that satisfy the conditions mentioned in the statement.


Example 1:

Input: n = 3, m = 2
Output: 3
Explanation: The following pairs satisfy conditions described in the statement: (1,2), (3,2), (2,1).

Example 2:

Input: n = 1, m = 1
Output: 0
Explanation: No pairs satisfy the conditions described in the statement.


Constraints:

1 <= n, m <= 10^5

"""

# V0
# IDEA : EVERY TURN REMOVES EXACTLY ONE FLOWER — SO ONLY THE PARITY MATTERS
#
#   the game lasts exactly x + y turns no matter what either player picks,
#   and whoever takes the LAST flower wins. Alice moves on turns 1, 3, 5...,
#   so she wins precisely when x + y is ODD.
#
#   counting the pairs is then pure arithmetic :
#       odd x  with even y  ->  ceil(n/2)  * floor(m/2)
#       even x with odd y   ->  floor(n/2) * ceil(m/2)
#
# time = O(1), space = O(1)
class Solution(object):
    def flowerGame(self, n, m):
        odd_n, even_n = (n + 1) // 2, n // 2
        odd_m, even_m = (m + 1) // 2, m // 2
        return odd_n * even_m + even_n * odd_m
