"""

3531. Count Covered Buildings
Medium

You are given a positive integer n, representing an n x n city. You are also
given a 2D grid buildings, where buildings[i] = [x, y] denotes a unique building
located at coordinates [x, y].

A building is covered if there is at least one building in all four directions:
left, right, above, and below.

Return the number of covered buildings.

Example 1:

Input: n = 3, buildings = [[1,2],[2,2],[3,2],[2,1],[2,3]]

Output: 1

Explanation:

Only building [2,2] is covered as it has at least one building:

above ([1,2])

below ([3,2])

left ([2,1])

right ([2,3])

Thus, the count of covered buildings is 1.

Example 2:

Input: n = 3, buildings = [[1,1],[1,2],[2,1],[2,2]]

Output: 0

Explanation:

No building has at least one building in all four directions.

Example 3:

Input: n = 5, buildings = [[1,3],[3,2],[3,3],[3,5],[5,3]]

Output: 1

Explanation:

Only building [3,3] is covered as it has at least one building:

above ([1,3])

below ([5,3])

left ([3,2])

right ([3,5])

Thus, the count of covered buildings is 1.

Constraints:

2 <= n <= 10^5

1 <= buildings.length <= 10^5

buildings[i] = [x, y]

1 <= x, y <= n

All coordinates of buildings are unique.

"""

# V0
# IDEA : ONLY THE EXTREMES OF EACH ROW AND COLUMN CAN FAIL
#
#   "has a building above" just means the building is not the minimum-x entry
#   of its column; likewise for below/left/right with max-x, min-y, max-y.
#
#   so instead of scanning neighbours we record, per column, the smallest and
#   largest x, and per row the smallest and largest y — then a building is
#   covered exactly when it is strictly inside all four of those ranges.
#
# time = O(m), space = O(m)
class Solution(object):
    def countCoveredBuildings(self, n, buildings):
        col_min, col_max = {}, {}
        row_min, row_max = {}, {}
        for x, y in buildings:
            if y not in col_min or x < col_min[y]:
                col_min[y] = x
            if y not in col_max or x > col_max[y]:
                col_max[y] = x
            if x not in row_min or y < row_min[x]:
                row_min[x] = y
            if x not in row_max or y > row_max[x]:
                row_max[x] = y

        res = 0
        for x, y in buildings:
            if col_min[y] < x < col_max[y] and row_min[x] < y < row_max[x]:
                res += 1
        return res
