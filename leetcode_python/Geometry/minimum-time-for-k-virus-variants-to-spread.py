"""

1956. Minimum Time For K Virus Variants to Spread
Hard

There are n unique virus variants in an infinite 2D grid. You are given a 2D array points, where points[i] = [xi, yi] represents a virus originating at (xi, yi) on day 0. Note that it is possible for multiple virus variants to originate at the same point.

Every day, each cell infected with a virus variant will spread the virus to all neighboring points in the four cardinal directions (i.e. up, down, left, and right). If a cell has multiple variants, all the variants will spread without interfering with each other.

Given an integer k, return the minimum integer number of days for any point to contain at least k of the unique virus variants.


Example 1:

Input: points = [[1,1],[6,1]], k = 2
Output: 3
Explanation: On day 3, points (3,1) and (4,1) will contain both virus variants. Note that these are not the only points that will contain both virus variants.

Example 2:

Input: points = [[3,3],[1,2],[9,2]], k = 2
Output: 2
Explanation: On day 2, points (1,3), (2,3), (2,2), and (3,2) will contain the first two viruses. Note that these are not the only points that will contain both virus variants.

Example 3:

Input: points = [[3,3],[1,2],[9,2]], k = 3
Output: 4
Explanation: On day 4, the point (5,2) will contain all 3 viruses. Note that this is not the only point that will contain all 3 virus variants.


Constraints:

n == points.length
2 <= n <= 50
points[i].length == 2
1 <= xi, yi <= 100
2 <= k <= n

"""

# V0
# IDEA : ENUMERATE MEETING CELLS + k-TH SMALLEST MANHATTAN DISTANCE
#
#   after d days a variant covers exactly the cells within Manhattan distance d
#   of its origin. so a cell c holds >= k variants on day d iff at least k of
#   the origins are within Manhattan distance d of c.
#
#   the best cell for any subset always lies inside the bounding box of that
#   subset (clamping a coordinate into the box never increases |x - xi|), and
#   all coordinates are in [1, 100] -> only 100 x 100 cells need checking.
#
#   for each candidate cell : sort its distances to the n origins and take the
#   k-th smallest; the answer is the minimum of those over all cells.
#
# time = O(100 * 100 * n log n), space = O(n)
class Solution(object):
    def minDayskVariants(self, points, k):
        lo_x = min(p[0] for p in points)
        hi_x = max(p[0] for p in points)
        lo_y = min(p[1] for p in points)
        hi_y = max(p[1] for p in points)

        res = float('inf')
        for x in range(lo_x, hi_x + 1):
            for y in range(lo_y, hi_y + 1):
                dists = sorted(abs(px - x) + abs(py - y) for px, py in points)
                if dists[k - 1] < res:
                    res = dists[k - 1]
        return res
