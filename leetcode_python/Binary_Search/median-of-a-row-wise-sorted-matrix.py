"""

2387. Median of a Row Wise Sorted Matrix
Medium
(premium / locked problem)

Given an m x n matrix grid containing an odd number of integers where each row is sorted in non-decreasing order, return the median of the matrix.

You must solve the problem in less than O(m * n) time complexity.


Example 1:

Input: grid = [[1,1,2],[2,3,3],[1,3,4]]
Output: 2
Explanation: The sorted array is [1,1,1,2,2,3,3,3,4]. The median is 2.

Example 2:

Input: grid = [[1,1,3,3,4]]
Output: 3
Explanation: The sorted array is [1,1,3,3,4]. The median is 3.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 500
m and n are both odd.
1 <= grid[i][j] <= 10^6
grid[i][j] is sorted in non-decreasing order.

"""

# V0
# IDEA : BINARY SEARCH ON THE VALUE, COUNTING WITH ONE BISECT PER ROW
#
#   the median of an odd-sized collection is the smallest value x such that
#       #{ elements <= x } >= (m * n + 1) / 2
#   and that count is monotone in x, so binary search x over the value range.
#
#   counting is cheap because every row is already sorted : bisect_right per
#   row gives that row's contribution in O(log n).
#
#   this is O(m log n log(max value)), comfortably under the required
#   sub-O(m * n) bound.
#
# time = O(m log n log(max value)), space = O(1)
import bisect


class Solution(object):
    def matrixMedian(self, grid):
        m, n = len(grid), len(grid[0])
        target = (m * n + 1) // 2

        lo = min(row[0] for row in grid)
        hi = max(row[-1] for row in grid)
        while lo < hi:
            mid = (lo + hi) // 2
            count = sum(bisect.bisect_right(row, mid) for row in grid)
            if count >= target:
                hi = mid
            else:
                lo = mid + 1
        return lo
