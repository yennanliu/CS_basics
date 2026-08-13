"""

850. Rectangle Area II
Hard

You are given a 2D array of axis-aligned rectangles. Each rectangle[i] = [xi1, yi1, xi2, yi2]
denotes the ith rectangle where (xi1, yi1) are the coordinates of the bottom-left
corner, and (xi2, yi2) are the coordinates of the top-right corner.

Calculate the total area covered by all rectangles in the plane.
Any area covered by two or more rectangles should only be counted once.

Return the total area. Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
Output: 6
Explanation: A total area of 6 is covered by all three rectangles.
From (1,1) to (2,2), the green and red rectangles overlap.
From (1,0) to (2,3), all three rectangles overlap.

Example 2:

Input: rectangles = [[0,0,1000000000,1000000000]]
Output: 49
Explanation: The answer is 10^18 modulo (10^9 + 7), which is 49.


Constraints:

1 <= rectangles.length <= 200
rectanges[i].length == 4
0 <= xi1, yi1, xi2, yi2 <= 10^9
xi1 <= xi2
yi1 <= yi2
All rectangles have non zero area.

"""

# V0
# IDEA : SWEEP LINE + COORDINATE COMPRESSION
#
#   Sweep a horizontal line upward through all distinct y values.
#   Between two consecutive y values the set of active rectangles never
#   changes, so the covered area of that horizontal slab is
#
#       (covered x length) * (slab height)
#
#   To get the covered x length, compress all distinct x coordinates into
#   at most 2n "columns" and keep a counter per column of how many active
#   rectangles cover it. A column contributes its width when its counter > 0.
#
#   Each rectangle becomes two events:
#       (y1, +1, x1, x2)  bottom edge -> the rectangle becomes active
#       (y2, -1, x1, x2)  top edge    -> the rectangle becomes inactive
#
#   NOTE: take the modulo only at the very end - taking it earlier would
#         corrupt the running width/height arithmetic.
#
# time  = O(n^2)     # 2n events, each rescanning 2n columns
# space = O(n)
class Solution(object):
    def rectangleArea(self, rectangles):
        MOD = 10 ** 9 + 7

        # compress the x axis
        xs = sorted({x for x1, y1, x2, y2 in rectangles for x in (x1, x2)})
        pos = {x: i for i, x in enumerate(xs)}

        # sweep events, sorted by y
        events = []
        for x1, y1, x2, y2 in rectangles:
            events.append((y1, 1, x1, x2))
            events.append((y2, -1, x1, x2))
        events.sort()

        # count[i] = how many active rectangles cover column [xs[i], xs[i+1])
        count = [0] * len(xs)
        area = 0
        prev_y = events[0][0]

        for y, delta, x1, x2 in events:
            # close off the slab between prev_y and y with the CURRENT coverage
            covered = 0
            for i in range(len(xs) - 1):
                if count[i] > 0:
                    covered += xs[i + 1] - xs[i]
            area += covered * (y - prev_y)
            prev_y = y

            # then apply this event
            for i in range(pos[x1], pos[x2]):
                count[i] += delta

        return area % MOD
