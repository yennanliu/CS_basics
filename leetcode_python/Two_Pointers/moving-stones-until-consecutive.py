"""

1033. Moving Stones Until Consecutive
Medium

There are three stones in different positions on the X-axis. You are given three
integers a, b, and c, the positions of the stones.

In one move, you pick up a stone at an endpoint (i.e., either the lowest or highest
position stone), and move it to an unoccupied position between those endpoints.
Formally, let's say the stones are currently at positions x, y, and z with x < y < z.
You pick up the stone at either position x or position z, and move that stone to an
integer position k, with x < k < z and k != y.

The game ends when you cannot make any more moves (i.e., the stones are in three
consecutive positions).

Return an integer array answer of length 2 where:

answer[0] is the minimum number of moves you can play, and
answer[1] is the maximum number of moves you can play.


Example 1:

Input: a = 1, b = 2, c = 5
Output: [1,2]
Explanation: Move the stone from 5 to 3, or move the stone from 5 to 4 to 3.

Example 2:

Input: a = 4, b = 3, c = 2
Output: [0,0]
Explanation: We cannot make any moves.

Example 3:

Input: a = 3, b = 5, c = 1
Output: [1,2]
Explanation: Move the stone from 1 to 4; or move the stone from 1 to 2 to 4.


Constraints:

1 <= a, b, c <= 100
a, b, and c have different values.

"""

# V0
# IDEA : MATH / CASE ANALYSIS
#
#  sort the 3 positions -> x < y < z
#   - already consecutive (z - x == 2)  -> [0, 0]
#   - one gap has size <= 1 (y - x == 2 or z - y == 2), or one side already
#     adjacent -> we can finish in 1 move
#   - otherwise -> 2 moves
#   - max moves = shrink the span by exactly 1 each move -> (z - x - 2)
#
# time = O(1)
# space = O(1)
class Solution(object):
    def numMovesStones(self, a, b, c):
        x, z = min(a, b, c), max(a, b, c)
        y = a + b + c - x - z

        # already consecutive -> no move possible
        if z - x == 2:
            return [0, 0]

        # if either gap is small enough (<= 2 spacing), 1 move is enough
        if y - x <= 2 or z - y <= 2:
            min_move = 1
        else:
            min_move = 2

        max_move = z - x - 2
        return [min_move, max_move]
