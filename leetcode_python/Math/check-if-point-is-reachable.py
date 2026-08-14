"""

2543. Check if Point Is Reachable
Hard

There exists an infinitely large grid. You are currently at point (1, 1), and you need to reach the point (targetX, targetY) using a finite number of steps.

In one step, you can move from point (x, y) to any one of the following points:

(x, y - x)
(x - y, y)
(2 * x, y)
(x, 2 * y)

Given two integers targetX and targetY representing the X-coordinate and Y-coordinate of your final position, return true if you can reach the point from (1, 1) using some number of steps, and false otherwise.


Example 1:

Input: targetX = 6, targetY = 9
Output: false
Explanation: It is impossible to reach (6,9) from (1,1) using any sequence of moves, so false is returned.

Example 2:

Input: targetX = 4, targetY = 7
Output: true
Explanation: You can follow the path (1,1) -> (1,2) -> (1,4) -> (1,8) -> (1,7) -> (2,7) -> (4,7).


Constraints:

1 <= targetX, targetY <= 10^9

"""

# V0
# IDEA : MATH / NUMBER THEORY (gcd is invariant up to powers of 2)
#
#   look at what each move does to g = gcd(x, y):
#     (x, y-x) and (x-y, y)  -> subtraction never changes gcd  => g stays g
#     (2x, y) and (x, 2y)    -> can only multiply g by 2 (or leave it)
#   we start at (1, 1) with g = 1, so any reachable point must have a gcd
#   that is a power of 2.
#
#   the converse also holds: running the moves backwards, while x or y is even
#   we can halve it; once both are odd and differ we can do (x, y) -> (x+y, y)
#   -> ((x+y)/2, y), which strictly shrinks the larger coordinate. so we can
#   always drive any gcd == 2^k point back down to (1, 1).
#
#   NOTE : "is a power of 2" is the classic bit trick g & (g - 1) == 0, which
#          is safe here because g >= 1 always (never zero).
#
# time = O(log(min(targetX, targetY))), space = O(1)
class Solution(object):
    def isReachable(self, targetX, targetY):
        a, b = targetX, targetY
        while b:
            a, b = b, a % b
        # a is now gcd(targetX, targetY); reachable iff it is a power of 2
        return a & (a - 1) == 0
