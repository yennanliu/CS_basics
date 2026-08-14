"""

1536. Minimum Swaps to Arrange a Binary Grid
Medium

Given an n x n binary grid, in one step you can choose two adjacent rows of the grid and swap them.

A grid is said to be valid if all the cells above the main diagonal are zeros.

Return the minimum number of steps needed to make the grid valid, or -1 if the grid cannot be valid.

The main diagonal of a grid is the diagonal that starts at cell (1, 1) and ends at cell (n, n).


Example 1:

Input: grid = [[0,0,1],[1,1,0],[1,0,0]]
Output: 3

Example 2:

Input: grid = [[0,1,1,0],[0,1,1,0],[0,1,1,0],[0,1,1,0]]
Output: -1
Explanation: All rows are similar, swaps have no effect on the grid.

Example 3:

Input: grid = [[1,0,0],[1,1,0],[1,1,1]]
Output: 0


Constraints:

n == grid.length == grid[i].length
1 <= n <= 200
grid[i][j] is either 0 or 1


"""

# V0
# IDEA : REDUCE EACH ROW TO ITS TRAILING-ZERO COUNT, THEN GREEDY BUBBLING
#
#   only the length of a row's trailing run of zeros matters : row i is
#   acceptable at position i iff it has at least (n - 1 - i) trailing zeros.
#
#   fill positions top-down. for position i, scan downward for the FIRST
#   row j >= i that qualifies, and bubble it up with (j - i) adjacent swaps.
#
#   NOTE : taking the nearest qualifying row is optimal — any row further
#          down costs strictly more swaps, and moving a row up only ever
#          pushes the skipped rows one slot down, which never hurts them
#          (they were rejected here, so they need a later, looser slot).
#   if no row qualifies for some position -> impossible, return -1.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def minSwaps(self, grid):
        n = len(grid)

        # zeros[i] = number of trailing zeros in row i
        zeros = []
        for row in grid:
            c = 0
            for j in range(n - 1, -1, -1):
                if row[j] != 0:
                    break
                c += 1
            zeros.append(c)

        res = 0
        for i in range(n):
            need = n - 1 - i
            k = -1
            for j in range(i, n):
                if zeros[j] >= need:
                    k = j
                    break
            if k == -1:
                return -1
            res += k - i
            while k > i:
                zeros[k], zeros[k - 1] = zeros[k - 1], zeros[k]
                k -= 1
        return res
