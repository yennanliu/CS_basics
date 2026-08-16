"""

3643. Flip Square Submatrix Vertically
Easy

You are given an m x n integer matrix grid, and three integers x, y, and k, where (x, y) denotes the top-left corner of a k x k submatrix, and k is its side length.

Your task is to flip the submatrix by reversing the order of its rows vertically.

Return the updated grid.


Example 1:

Input: grid = [[1,2,3],[4,5,6],[7,8,9]], x = 1, y = 0, k = 2
Output: [[1,2,3],[7,8,6],[4,5,9]]
Explanation:
The 2 x 2 submatrix with top-left corner (1, 0) is [[4,5],[7,8]].
Reversing the order of its rows gives [[7,8],[4,5]], which produces the returned grid.

Example 2:

Input: grid = [[3,4,2,3],[2,3,4,2]], x = 0, y = 2, k = 2
Output: [[3,4,4,2],[2,3,2,3]]
Explanation:
The 2 x 2 submatrix with top-left corner (0, 2) is [[2,3],[4,2]].
Reversing the order of its rows gives [[4,2],[2,3]], which produces the returned grid.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 100
1 <= k <= min(m - x, n - y)
0 <= x < m
0 <= y < n
-10^3 <= grid[i][j] <= 10^3

"""

# V0
# IDEA : SWAP MIRRORED ROW SLICES IN PLACE
#
#   a vertical flip only permutes whole rows of the window: row x+i trades
#   places with row x+k-1-i, and the columns [y, y+k) travel unchanged.
#
#   so walk i over the top half only and swap the two k-wide slices; the
#   middle row of an odd-sized window is its own mirror and stays put.
#
# time = O(k^2), space = O(1)
class Solution(object):
    def reverseSubmatrix(self, grid, x, y, k):
        for i in range(k // 2):
            top = x + i
            bot = x + k - 1 - i
            for j in range(y, y + k):
                grid[top][j], grid[bot][j] = grid[bot][j], grid[top][j]
        return grid
