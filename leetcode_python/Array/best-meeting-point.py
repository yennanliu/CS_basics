"""

296. Best Meeting Point
Hard
(premium)

Given an m x n binary grid grid where each 1 marks the home of one friend,
return the minimal total travel distance.

The total travel distance is the sum of the distances between the houses of the friends
and the meeting point.

The distance is calculated using Manhattan Distance, where
distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.


Example 1:

Input: grid = [[1,0,0,0,1],[0,0,0,0,0],[0,0,1,0,0]]
Output: 6
Explanation: Given three friends living at (0,0), (0,4), and (2,2).
The point (0,2) is an ideal meeting point, as the total travel distance of 2 + 2 + 2 = 6
is minimal. So return 6.

Example 2:

Input: grid = [[1,1]]
Output: 1


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 200
grid[i][j] is either 0 or 1.
There will be at least two friends in the grid.

"""

# V0
# IDEA : MEDIAN (Manhattan distance decomposes into independent x and y axes)
#
#  |dx| + |dy| means the row coordinate and the column coordinate can be optimized
#  SEPARATELY. On a single axis, the point minimizing the sum of absolute distances
#  is the MEDIAN.
#
#  Instead of computing the median explicitly, pair up the outermost points:
#  for a sorted list, sum(arr[-1] - arr[0]) + (arr[-2] - arr[1]) + ... is exactly
#  the minimal total distance (any point between the innermost pair works).
#
# time  = O(m * n)  # the column list needs a sort: O(m*n*log(m*n)) worst case
# space = O(m * n)
class Solution(object):
    def minTotalDistance(self, grid):
        rows, cols = [], []
        for i, row in enumerate(grid):
            for j, v in enumerate(row):
                if v == 1:
                    rows.append(i)
                    cols.append(j)

        # rows already comes out sorted (we scan top to bottom); cols does not
        cols.sort()

        def min_dist(arr):
            # sum of the gaps between symmetric outer pairs
            i, j, total = 0, len(arr) - 1, 0
            while i < j:
                total += arr[j] - arr[i]
                i += 1
                j -= 1
            return total

        return min_dist(rows) + min_dist(cols)


# V0-1
# IDEA : BRUTE FORCE - TRY EVERY CELL AS THE MEETING POINT
#
#   collect the friends once, then score all m * n candidate cells against them.
#   the meeting point does NOT have to be a house, so every cell must be tried;
#   this is the version to check the clever ones against.
#
# time = O(m * n * k), k = number of friends  # O((m*n)^2) worst case
# space = O(k)
class Solution(object):
    def minTotalDistance(self, grid):
        homes = [(i, j)
                 for i, row in enumerate(grid)
                 for j, v in enumerate(row) if v == 1]
        rows, cols = len(grid), len(grid[0])
        best = None
        for i in range(rows):
            for j in range(cols):
                total = sum(abs(i - x) + abs(j - y) for x, y in homes)
                if best is None or total < best:
                    best = total
        return best


# V0-2
# IDEA : PER-AXIS COUNTING + INCREMENTAL SWEEP (no sorting)
#
#   |dx| + |dy| separates, so handle one axis at a time. bucket the friends by
#   coordinate (the buckets come out ordered, so no sort is needed), then sweep
#   the coordinate upwards keeping
#       left  = friends already behind,   right = friends still ahead
#   stepping the meeting point one cell right costs +left and saves -right, so
#   each next cost is O(1) from the previous one.
#
# time = O(m * n)
# space = O(m + n)
class Solution(object):
    def minTotalDistance(self, grid):
        rows, cols = len(grid), len(grid[0])
        row_cnt = [0] * rows
        col_cnt = [0] * cols
        for i in range(rows):
            for j in range(cols):
                if grid[i][j] == 1:
                    row_cnt[i] += 1
                    col_cnt[j] += 1

        def sweep(cnt):
            total = sum(k * c for k, c in enumerate(cnt))   # cost at coord 0
            left, right = 0, sum(cnt)
            best = total
            for k in range(1, len(cnt)):
                left += cnt[k - 1]
                right -= cnt[k - 1]
                total += left - right
                if total < best:
                    best = total
            return best

        return sweep(row_cnt) + sweep(col_cnt)
