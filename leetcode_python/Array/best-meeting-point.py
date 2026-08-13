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
