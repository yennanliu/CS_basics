"""

2033. Minimum Operations to Make a Uni-Value Grid
Medium

You are given a 2D integer grid of size m x n and an integer x. In one operation, you can add x to or subtract x from any element in the grid.

A uni-value grid is a grid where all the elements of it are equal.

Return the minimum number of operations to make the grid uni-value. If it is not possible, return -1.


Example 1:

Input: grid = [[2,4],[6,8]], x = 2
Output: 4
Explanation: We can make every element equal to 4 by doing the following:
- Add x to 2 once.
- Subtract x from 6 once.
- Subtract x from 8 twice.
A total of 4 operations were used.

Example 2:

Input: grid = [[1,5],[2,3]], x = 1
Output: 5
Explanation: We can make every element equal to 3.

Example 3:

Input: grid = [[1,2],[3,4]], x = 2
Output: -1
Explanation: It is impossible to make every element equal.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 10^5
1 <= m * n <= 10^5
1 <= x, grid[i][j] <= 10^4

"""

# V0
# IDEA : FEASIBILITY BY REMAINDER mod x, THEN THE MEDIAN MINIMIZES THE COST
#
#   every operation moves a value by exactly x, so two cells can ever meet
#   only if they share the same remainder mod x. check that first -> else -1.
#
#   given they all agree mod x, the cost of targeting value t is
#       sum( abs(v - t) ) / x
#   and sum of absolute deviations is minimized at the MEDIAN of the values.
#
#   NOTE : the target must be one of the existing values (any other value with
#          the same remainder is worse), so picking the sorted middle element
#          is enough.
#
# time = O(m * n * log(m * n)), space = O(m * n)
class Solution(object):
    def minOperations(self, grid, x):
        vals = [v for row in grid for v in row]
        r = vals[0] % x
        if any(v % x != r for v in vals):
            return -1
        vals.sort()
        median = vals[len(vals) // 2]
        return sum(abs(v - median) // x for v in vals)
