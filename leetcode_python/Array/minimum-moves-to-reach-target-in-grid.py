"""

3609. Minimum Moves to Reach Target in Grid
Hard

You are given four integers sx, sy, tx, and ty, representing two points (sx, sy)
and (tx, ty) on an infinitely large 2D grid.

You start at (sx, sy).

At any point (x, y), define m = max(x, y). You can either:

Move to (x + m, y), or
Move to (x, y + m).

Return the minimum number of moves required to reach (tx, ty). If it is
impossible to reach the target, return -1.


Example 1:

Input: sx = 1, sy = 2, tx = 5, ty = 4
Output: 2
Explanation:
The optimal path is:
Move 1: max(1, 2) = 2. Increase the y-coordinate by 2, moving from (1, 2) to
(1, 2 + 2) = (1, 4).
Move 2: max(1, 4) = 4. Increase the x-coordinate by 4, moving from (1, 4) to
(1 + 4, 4) = (5, 4).
Thus, the minimum number of moves to reach (5, 4) is 2.

Example 2:

Input: sx = 0, sy = 1, tx = 2, ty = 3
Output: 3
Explanation:
The optimal path is:
Move 1: max(0, 1) = 1. Increase the x-coordinate by 1, moving from (0, 1) to
(0 + 1, 1) = (1, 1).
Move 2: max(1, 1) = 1. Increase the x-coordinate by 1, moving from (1, 1) to
(1 + 1, 1) = (2, 1).
Move 3: max(2, 1) = 2. Increase the y-coordinate by 2, moving from (2, 1) to
(2, 1 + 2) = (2, 3).
Thus, the minimum number of moves to reach (2, 3) is 3.

Example 3:

Input: sx = 1, sy = 1, tx = 2, ty = 2
Output: -1
Explanation:
It is impossible to reach (2, 2) from (1, 1) using the allowed moves. Thus, the
answer is -1.


Constraints:

0 <= sx <= tx <= 10^9
0 <= sy <= ty <= 10^9
"""

# V0
# IDEA : BACKWARD WALK — THE PREDECESSOR IS FORCED
#
#   forward the branching factor is 2, but backwards it collapses. suppose we
#   stand at (X, Y) with X > Y. the last move cannot have been a y-step: a
#   y-step lands on y + max(X, y), which is >= X either way (it is y + X if
#   X >= y, and 2y > X if y > X), contradicting X > Y. so the last move grew x,
#   and the only question is which coordinate was the max back then.
#
#   both readings are pinned down. if the predecessor had x <= Y then
#   X = x + Y, giving (X - Y, Y), which is legal exactly when X <= 2Y. if it
#   had x >= Y then X = 2x, giving (X/2, Y), legal exactly when X >= 2Y and X
#   is even. the two windows only overlap at X = 2Y where they name the same
#   point, so the predecessor is unique — there is nothing to search, and an
#   odd X with X > 2Y simply has no predecessor at all.
#
#   the tie X == Y > 0 is the one fork: x + max(x, X) = X forces x = 0, so the
#   predecessors are (0, X) and (X, 0). from either one a coordinate is stuck
#   at 0 forever afterwards, so the fork is decided by the start, not searched:
#   take (0, X) if sx == 0, (X, 0) if sy == 0, and otherwise the target is
#   unreachable. (both zero means the start is (0, 0), from which every move is
#   a self-loop.)
#
#   the descent alternates euclidean subtraction with halving, so it burns
#   through the coordinates in logarithmic time, and since the chain is unique
#   the step count it reports is not just minimal but the only possible one.
#
# time = O(log(tx) + log(ty)), space = O(1)
class Solution(object):
    def minMoves(self, sx, sy, tx, ty):
        steps = 0
        while (tx, ty) != (sx, sy):
            # forward moves never decrease a coordinate
            if tx < sx or ty < sy:
                return -1
            if tx > ty:
                if tx > 2 * ty:
                    if tx % 2:
                        return -1
                    tx //= 2
                else:
                    tx -= ty
            elif ty > tx:
                if ty > 2 * tx:
                    if ty % 2:
                        return -1
                    ty //= 2
                else:
                    ty -= tx
            else:
                if sx == 0:
                    tx = 0
                elif sy == 0:
                    ty = 0
                else:
                    return -1
            steps += 1
        return steps
