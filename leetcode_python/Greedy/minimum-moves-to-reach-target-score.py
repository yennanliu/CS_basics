"""

2139. Minimum Moves to Reach Target Score
Medium

You are playing a game with integers. You start with the integer 1 and you want to reach the integer target.

In one move, you can either:

Increment the current integer by one (i.e., x = x + 1).
Double the current integer (i.e., x = 2 * x).

You can use the increment operation any number of times, however, you can only use the double operation at most maxDoubles times.

Given the two integers target and maxDoubles, return the minimum number of moves needed to reach target.


Example 1:

Input: target = 5, maxDoubles = 0
Output: 4
Explanation: Keep incrementing by 1 until you reach target.

Example 2:

Input: target = 19, maxDoubles = 2
Output: 7
Explanation: Initially, x = 1
Increment 3 times so x = 4
Double once so x = 8
Increment once so x = 9
Double again so x = 18
Increment once so x = 19

Example 3:

Input: target = 10, maxDoubles = 4
Output: 4
Explanation: Initially, x = 1
Increment once so x = 2
Double once so x = 4
Double again so x = 8
Increment once so x = 10


Constraints:

1 <= target <= 10^9
0 <= maxDoubles <= 100

"""

# V0
# IDEA : WORK BACKWARDS FROM target — HALVE WHENEVER YOU CAN
#
#   reversing the moves turns "double" into "halve" and "increment" into
#   "decrement". halving cuts the remaining distance in half, so it is always
#   worth doing as early as possible :
#       target odd  -> one decrement to make it even
#       target even -> halve, spending one of the maxDoubles
#
#   once the doubles run out (or target reaches 1) the rest is pure
#   decrements : target - 1 more moves.
#
# time = O(log target), space = O(1)
class Solution(object):
    def minMoves(self, target, maxDoubles):
        moves = 0
        while target > 1 and maxDoubles > 0:
            if target % 2:
                target -= 1
            else:
                target //= 2
                maxDoubles -= 1
            moves += 1
        return moves + (target - 1)
