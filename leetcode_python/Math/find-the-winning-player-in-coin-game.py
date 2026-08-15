"""

3222. Find the Winning Player in Coin Game
Easy

You are given two integers x and y, denoting the number of coins with values 75 and 10 respectively.

Alice and Bob are playing a game. Each turn, starting with Alice, the player must pick up coins with a total value 115. If the player is unable to do so, they lose the game.

Return the name of the player who wins the game if both players play optimally.


Example 1:

Input: x = 2, y = 7
Output: "Alice"
Explanation:
The game ends in a single turn:
Alice picks 1 coin with a value of 75 and 4 coins with a value of 10.

Example 2:

Input: x = 4, y = 11
Output: "Bob"
Explanation:
The game ends in 2 turns:
Alice picks 1 coin with a value of 75 and 4 coins with a value of 10.
Bob picks 1 coin with a value of 75 and 4 coins with a value of 10.


Constraints:

1 <= x, y <= 100

"""

# V0
# IDEA : 115 HAS EXACTLY ONE DECOMPOSITION — 75 + 4 x 10
#
#   with only 75s and 10s available, 115 can only be formed as one 75 plus
#   four 10s (two 75s already overshoot, and 10s alone never end in 5). so
#   every turn consumes the same fixed bundle and there is no strategy at
#   all.
#
#   the number of turns is min(x, y // 4), and Alice moves on the odd ones,
#   so she wins exactly when that count is odd.
#
# time = O(1), space = O(1)
class Solution(object):
    def losingPlayer(self, x, y):
        turns = min(x, y // 4)
        return "Alice" if turns % 2 else "Bob"
