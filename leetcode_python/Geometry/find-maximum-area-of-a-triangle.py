"""

3588. Find Maximum Area of a Triangle
Medium

You are given a 2D array coords of size n x 2, representing the coordinates of n points in an infinite Cartesian plane.

Find twice the maximum area of a triangle with its corners at any three elements from coords, such that at least one side of this triangle is parallel to the x-axis or y-axis. Formally, if the maximum area of such a triangle is A, return 2 * A.

If no such triangle exists, return -1.

Note that a triangle cannot have zero area.


Example 1:

Input: coords = [[1,1],[1,2],[3,2],[3,3]]
Output: 2
Explanation:
The triangle with corners (1, 1), (1, 2), and (3, 2) has a base 1 and height 2. Hence its area is 1/2 * base * height = 1.

Example 2:

Input: coords = [[1,1],[2,2],[3,3]]
Output: -1
Explanation:
The only possible triangle has corners (1, 1), (2, 2), and (3, 3). None of its sides are parallel to the x-axis or the y-axis.


Constraints:

1 <= n == coords.length <= 10^5
1 <= coords[i][0], coords[i][1] <= 10^6
All coords[i] are unique.

"""

# V0
# IDEA : THE AXIS-PARALLEL SIDE IS THE BASE; THE APEX IS A GLOBAL EXTREME
#
#   fix the base as two points sharing a y value; the widest such pair is
#   always best because 2*area = base_length * height and the two factors
#   are independent. the height is the vertical distance from that y to the
#   apex, so the apex should be whichever of the globally smallest or
#   largest y is further away.
#
#   running the same sweep with the two axes swapped covers vertical sides.
#   grouping by the shared coordinate and keeping only each group's min/max
#   is all the bookkeeping required.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxArea(self, coords):
        return max(self._scan(coords, 0, 1), self._scan(coords, 1, 0))

    def _scan(self, coords, base_axis, other_axis):
        # best base_length * height over sides parallel to `base_axis`
        groups = {}
        lo = float('inf')
        hi = float('-inf')
        for p in coords:
            b = p[base_axis]
            o = p[other_axis]
            if o < lo:
                lo = o
            if o > hi:
                hi = o
            if o in groups:
                mn, mx = groups[o]
                if b < mn:
                    mn = b
                if b > mx:
                    mx = b
                groups[o] = (mn, mx)
            else:
                groups[o] = (b, b)

        best = -1
        for o, (mn, mx) in groups.items():
            base = mx - mn
            if base == 0:
                continue
            height = max(o - lo, hi - o)
            if height > 0:
                v = base * height
                if v > best:
                    best = v
        return best
