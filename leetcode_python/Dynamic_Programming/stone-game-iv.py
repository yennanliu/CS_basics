"""

1510. Stone Game IV
Hard

Alice and Bob take turns playing a game, with Alice starting first.

Initially, there are n stones in a pile. On each player's turn, that player makes a move consisting of removing any non-zero square number of stones in the pile.

Also, if a player cannot make a move, he/she loses the game.

Given a positive integer n, return true if and only if Alice wins the game otherwise return false, assuming both players play optimally.


Example 1:

Input: n = 1
Output: true
Explanation: Alice can remove 1 stone winning the game because Bob doesn't have any moves.

Example 2:

Input: n = 2
Output: false
Explanation: Alice can only remove 1 stone, after that Bob removes the last one winning the game (2 -> 1 -> 0).

Example 3:

Input: n = 4
Output: true
Explanation: n is already a perfect square, Alice can win with one move, removing 4 stones (4 -> 0).


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : GAME DP (win / lose states, bottom up)
#
#   f[i] = True if the player TO MOVE with i stones left wins.
#
#     f[0] = False        (no move possible -> current player loses)
#     f[i] = True  iff  there exists a square j*j <= i with f[i - j*j] == False
#
#   i.e. a position wins if it can hand the opponent any losing position.
#   NOTE : this is a symmetric game, so the same table serves both players;
#          the answer for Alice is simply f[n].
#
"""

DP def
    (GAME DP - win / lose states)

    f[i]: True if the player TO MOVE with i stones left WINS

DP eq

     f[0] = False        # no move possible -> the current player loses

     f[i] = True   iff   there exists a square j*j <= i with f[i - j*j] == False


    -> e.g. a position WINS if it can hand the opponent ANY losing position

     the game is symmetric, so one table serves both players

     ans = f[n]        # Alice moves first

"""
# time = O(n * sqrt(n)), space = O(n)
class Solution(object):
    def winnerSquareGame(self, n):
        f = [False] * (n + 1)
        for i in range(1, n + 1):
            j = 1
            while j * j <= i:
                if not f[i - j * j]:
                    f[i] = True
                    break
                j += 1
        return f[n]
