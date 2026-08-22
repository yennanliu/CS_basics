"""

2849. Determine if a Cell Is Reachable at a Given Time
Medium

You are given four integers sx, sy, fx, fy, and a non-negative integer t.

In an infinite 2D grid, you start at the cell (sx, sy). Each second, you must move to any of its adjacent cells.

Return true if you can reach cell (fx, fy) after exactly t seconds, or false otherwise.

A cell's adjacent cells are the 8 cells around it that share at least one corner with it. You can visit the same cell several times.


Example 1:

Input: sx = 2, sy = 4, fx = 7, fy = 7, t = 6
Output: true
Explanation: Starting at cell (2, 4), we can reach cell (7, 7) in exactly 6 seconds by going through the cells depicted in the picture above.

Example 2:

Input: sx = 3, sy = 1, fx = 7, fy = 3, t = 3
Output: false
Explanation: Starting at cell (3, 1), it takes at least 4 seconds to reach cell (7, 3) by going through the cells depicted in the picture above. Hence, we cannot reach cell (7, 3) at the third second.


Constraints:

1 <= sx, sy, fx, fy <= 10^9
0 <= t <= 10^9

"""

# V0
# IDEA : MATH (Chebyshev distance + a single degenerate case)
#
#   with 8-directional moves one step changes x by at most 1 AND y by at
#   most 1 simultaneously, so the minimum number of steps between two cells
#   is the Chebyshev distance max(|dx|, |dy|).
#
#   any surplus time can always be burnt: step off to a neighbour and step
#   back (2 seconds), or if dx != dy simply "waste" a diagonal-vs-straight
#   choice (1 second). Concretely, once t >= max(|dx|, |dy|) and the cells
#   differ, every larger t is reachable too, because the grid is infinite
#   and we can detour by one cell.
#
#   NOTE : the ONLY unreachable-with-slack case is start == finish with
#          t == 1 — we must move every second, so we cannot stand still for
#          exactly one second and end where we began. t == 0 is fine (no
#          move made) and t >= 2 is fine (step away, step back).
#
# time = O(1), space = O(1)
class Solution(object):
    def isReachableAtTime(self, sx, sy, fx, fy, t):
        if sx == fx and sy == fy:
            return t != 1
        dx = abs(sx - fx)
        dy = abs(sy - fy)
        return max(dx, dy) <= t


# V0-1
# IDEA : REACHABLE-REGION CONTAINMENT (the exactly-t reachable set is a square)
#
#   a king move may change x and y at the SAME time, so the two coordinates are
#   completely decoupled: in t seconds x can land anywhere in [sx-t, sx+t] and,
#   independently, y anywhere in [sy-t, sy+t]. so the set of cells reachable in
#   exactly t seconds is that axis-aligned square -- with a single hole at its
#   own centre when t == 1, since we are forced to move every second.
#
#   that turns the whole problem into a bounding-box membership test, with no
#   distance to compute at all.
#
# time = O(1), space = O(1)
class Solution(object):
    def isReachableAtTime(self, sx, sy, fx, fy, t):
        if t == 1 and sx == fx and sy == fy:
            return False
        return sx - t <= fx <= sx + t and sy - t <= fy <= sy + t


# V0-2
# IDEA : GREEDY STEP-BY-STEP SIMULATION (why the closed form is max(|dx|, |dy|))
#
#   actually walk the king: each second nudge x toward fx and y toward fy at
#   the same time, which is a diagonal move while both coordinates are off and
#   a straight move once one of them has landed. count the seconds.
#   abort as soon as the count passes t, so the loop runs at most t + 1 times.
#
#   NOTE : (fx > x) - (fx < x) is the sign of the remaining x gap, i.e. +1 / 0 / -1.
#   NOTE : steps == 0 means we never moved (start == finish) -- reachable for
#          t == 0 and for t >= 2 (step away and back), but not for t == 1.
#
# time = O(min(t, max(|dx|, |dy|))), space = O(1)
class Solution(object):
    def isReachableAtTime(self, sx, sy, fx, fy, t):
        x, y = sx, sy
        steps = 0
        while x != fx or y != fy:
            if steps > t:
                return False
            x += (fx > x) - (fx < x)
            y += (fy > y) - (fy < y)
            steps += 1
        if steps == 0:
            return t != 1
        return steps <= t
