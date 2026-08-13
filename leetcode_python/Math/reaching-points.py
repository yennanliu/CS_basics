"""

780. Reaching Points
Hard

Given four integers sx, sy, tx, and ty, return true if it is possible to convert the
point (sx, sy) to the point (tx, ty) through some operations, or false otherwise.

The allowed operation on some point (x, y) is to convert it to either (x, x + y)
or (x + y, y).


Example 1:

Input: sx = 1, sy = 1, tx = 3, ty = 5
Output: true
Explanation:
One series of moves that transforms the starting point to the target is:
(1, 1) -> (1, 2)
(1, 2) -> (3, 2)
(3, 2) -> (3, 5)

Example 2:

Input: sx = 1, sy = 1, tx = 2, ty = 2
Output: false

Example 3:

Input: sx = 1, sy = 1, tx = 1, ty = 1
Output: true


Constraints:

1 <= sx, sy, tx, ty <= 10^9

"""

# V0
# IDEA : WORK BACKWARDS WITH MODULO
#
#   Forwards, (x, y) branches in two directions — too slow. Backwards it is
#   DETERMINISTIC: since all values stay positive, the last move must have grown the
#   larger coordinate, so (tx, ty) came from (tx - ty, ty) when tx > ty, else from
#   (tx, ty - tx).
#
#   Repeatedly subtracting is O(value), so use modulo to do all the subtractions of
#   one coordinate in a single step (a Euclidean-algorithm style reduction).
#
#   Stop as soon as a coordinate drops to or below its start value, then handle the
#   tail: if one coordinate already matches, the other only had that fixed value
#   added repeatedly -> a divisibility check.
#
# time = O(log(max(tx, ty)))
# space = O(1)
class Solution(object):
    def reachingPoints(self, sx, sy, tx, ty):
        # both coordinates still above the start -> keep reducing the larger one
        while tx > sx and ty > sy:
            if tx > ty:
                tx %= ty
            else:
                ty %= tx

        if tx == sx and ty == sy:
            return True

        # only ty still needs shrinking: it grew by repeatedly adding tx (== sx)
        if tx == sx and ty > sy:
            return (ty - sy) % tx == 0

        # only tx still needs shrinking: it grew by repeatedly adding ty (== sy)
        if ty == sy and tx > sx:
            return (tx - sx) % ty == 0

        return False
