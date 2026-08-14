"""

2660. Determine the Winner of a Bowling Game
Easy

You are given two 0-indexed integer arrays player1 and player2, representing the number of pins that player 1 and player 2 hit in a bowling game, respectively.

The bowling game consists of n turns, and the number of pins in each turn is exactly 10.

Assume a player hits xi pins in the ith turn. The value of the ith turn for the player is:

2xi if the player hits 10 pins in either (i - 1)th or (i - 2)th turn.
Otherwise, it is xi.

The score of the player is the sum of the values of their n turns.

Return

1 if the score of player 1 is more than the score of player 2,
2 if the score of player 2 is more than the score of player 1, and
0 in case of a draw.


Example 1:

Input: player1 = [5,10,3,2], player2 = [6,5,7,3]
Output: 1
Explanation:
The score of player 1 is 5 + 10 + 2*3 + 2*2 = 25.
The score of player 2 is 6 + 5 + 7 + 3 = 21.

Example 2:

Input: player1 = [3,5,7,6], player2 = [8,10,10,2]
Output: 2
Explanation:
The score of player 1 is 3 + 5 + 7 + 6 = 21.
The score of player 2 is 8 + 10 + 2*10 + 2*2 = 42.

Example 3:

Input: player1 = [2,3], player2 = [4,1]
Output: 0
Explanation:
The score of player1 is 2 + 3 = 5.
The score of player2 is 4 + 1 = 5.

Example 4:

Input: player1 = [1,1,1,10,10,10,10], player2 = [10,10,10,10,1,1,1]
Output: 2
Explanation:
The score of player1 is 1 + 1 + 1 + 10 + 2*10 + 2*10 + 2*10 = 73.
The score of player2 is 10 + 2*10 + 2*10 + 2*10 + 2*1 + 2*1 + 1 = 75.


Constraints:

n == player1.length == player2.length
1 <= n <= 1000
0 <= player1[i], player2[i] <= 10

"""

# V0
# IDEA : SIMULATION (score each player independently, then compare)
#
#   the "double" rule only looks BACKWARD at the two previous turns, so a
#   single left -> right pass is enough: turn i is worth 2*x when
#   arr[i-1] == 10 or arr[i-2] == 10, else x.
#
#   NOTE : the multiplier depends on the ORIGINAL pin counts of the two
#          previous turns, not on their (possibly doubled) values -- so
#          never feed the doubled value back into the check.
#
#   NOTE : the bonus is NOT stacked. Even if both turn i-1 and turn i-2
#          were strikes, turn i is still only doubled once.
#
#   NOTE : guard the indices -- turn 0 has no previous turn, turn 1 has
#          only one.
#
# time = O(n), space = O(1)
class Solution(object):
    def isWinner(self, player1, player2):
        def score(arr):
            total = 0
            for i, x in enumerate(arr):
                if (i >= 1 and arr[i - 1] == 10) or (i >= 2 and arr[i - 2] == 10):
                    total += 2 * x
                else:
                    total += x
            return total

        a, b = score(player1), score(player2)
        if a > b:
            return 1
        if b > a:
            return 2
        return 0
