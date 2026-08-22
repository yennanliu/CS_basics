"""

1351. Count Negative Numbers in a Sorted Matrix
Easy

Given a m x n matrix grid which is sorted in non-increasing order both row-wise and column-wise,
return the number of negative numbers in grid.


Example 1:

Input: grid = [[4,3,2,-1],[3,2,1,-1],[1,1,-1,-2],[-1,-1,-2,-3]]
Output: 8
Explanation: There are 8 negatives number in the matrix.

Example 2:

Input: grid = [[3,2],[1,0]]
Output: 0


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 100
-100 <= grid[i][j] <= 100


Follow up: Could you find an O(n + m) solution?

"""

# V0
# IDEA : STAIRCASE SEARCH from the bottom-left corner
#
#   rows and columns both decrease left->right and top->bottom, so the
#   bottom-left cell is the "pivot" : it is the smallest of its row and the
#   largest of its column.
#
#   stand at (m-1, 0) and walk :
#     grid[i][j] >= 0 -> everything left of j in this row is >= 0 too,
#                        move right (j += 1)
#     grid[i][j] <  0 -> everything from j to the end of this row is < 0,
#                        add (n - j) and move up (i -= 1)
#
#   NOTE : each step consumes one row or one column, hence O(m + n) total --
#          this is the follow-up's answer, no binary search needed.
#
# time = O(m + n), space = O(1)
class Solution(object):
    def countNegatives(self, grid):
        m, n = len(grid), len(grid[0])
        i, j = m - 1, 0
        res = 0
        while i >= 0 and j < n:
            if grid[i][j] >= 0:
                j += 1
            else:
                res += n - j
                i -= 1
        return res


# V0-1
# IDEA : BINARY SEARCH THE FIRST NEGATIVE OF EACH ROW
#
#   every row is non-increasing, so it has the shape
#   [ >= 0, ..., >= 0, < 0, ..., < 0 ]. "is negative" is therefore a monotone
#   predicate along the row and its switch point can be found by bisection
#   instead of by walking.
#
#   once the first negative index is known, the whole tail (n - idx cells) of
#   that row is negative.
#
# time = O(m * log n), space = O(1)
class Solution(object):
    def countNegatives(self, grid):
        n = len(grid[0])
        res = 0
        for row in grid:
            lo, hi = 0, n
            while lo < hi:
                mid = (lo + hi) // 2
                if row[mid] < 0:
                    hi = mid
                else:
                    lo = mid + 1
            res += n - lo
        return res


# V0-2
# IDEA : BRUTE FORCE — IGNORE THE ORDERING AND TEST EVERY CELL
#
#   the baseline any counting question admits: look at all m * n values and
#   tally the negative ones. m, n <= 100 so 10^4 cells is nothing, and it is
#   the reference the two order-exploiting versions are checked against.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def countNegatives(self, grid):
        return sum(1 for row in grid for v in row if v < 0)
