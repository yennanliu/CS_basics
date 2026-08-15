"""

3360. Stone Removal Game
Easy

Alice and Bob are playing a game where they take turns removing stones from a pile, with Alice going first.

Alice starts by removing exactly 10 stones on her first turn.
For each subsequent turn, each player removes exactly 1 fewer stone than the previous opponent.

The player who cannot make a move loses the game.

Given a positive integer n, return true if Alice wins the game and false otherwise.


Example 1:

Input: n = 12
Output: true
Explanation:
Alice removes 10 stones on her first turn, leaving 2 stones for Bob.
Bob cannot remove 9 stones, so Alice wins.

Example 2:

Input: n = 1
Output: false
Explanation:
Alice cannot remove 10 stones, so Alice loses.


Constraints:

1 <= n <= 50

"""

# V0
# IDEA : THE MOVE SIZES ARE FIXED — 10, 9, 8, ... — SO JUST PLAY IT OUT
#
#   nobody has a choice : the k-th removal always takes 11 - k stones. so
#   simulate until the current player cannot pay, and whoever that is loses.
#
#   n <= 50 means at most a handful of turns.
#
# time = O(1), space = O(1)
class Solution(object):
    def canAliceWin(self, n):
        take = 10
        alice = True
        while n >= take:
            n -= take
            take -= 1
            alice = not alice
        # the player to move cannot pay, so the OTHER one won
        return not alice
