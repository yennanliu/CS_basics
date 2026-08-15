"""

2132. Stamping the Grid
Hard

You are given an m x n binary matrix grid where each cell is either 0 (empty) or 1 (occupied).

You are then given stamps of size stampHeight x stampWidth. We want to fit the stamps such that they follow the given restrictions and requirements:

Cover all the empty cells.
Do not cover any of the occupied cells.
We can put as many stamps as we want.
Stamps can overlap with each other.
Stamps are not allowed to be rotated.
Stamps must stay completely inside the grid.

Return true if it is possible to fit the stamps while following the given restrictions and requirements. Otherwise, return false.


Example 1:

Input: grid = [[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0]], stampHeight = 4, stampWidth = 3
Output: true
Explanation: We have two overlapping stamps (labeled 1 and 2 in the image) that are able to cover all the empty cells.

Example 2:

Input: grid = [[1,0,0,0],[0,1,0,0],[0,0,1,0],[0,0,0,1]], stampHeight = 2, stampWidth = 2
Output: false
Explanation: There is no way to fit the stamps onto all the empty cells without the stamps going outside the grid.


Constraints:

m == grid.length
n == grid[r].length
1 <= m, n <= 10^5
1 <= m * n <= 2 * 10^5
grid[r][c] is either 0 or 1.
1 <= stampHeight, stampWidth <= 10^5

"""

# V0
# IDEA : 2D PREFIX SUM TO TEST A PLACEMENT, 2D DIFFERENCE ARRAY TO PAINT IT
#
#   stamps may overlap freely, so the greedy answer is "place a stamp
#   wherever it legally fits" — nothing is lost by placing one more. two
#   O(m*n) sweeps do the whole job :
#
#   1) prefix sums of grid -> a stamp anchored at (i, j) is legal iff the
#      rectangle it covers sums to 0 (contains no occupied cell).
#
#   2) for every legal anchor, stamp a +1 / -1 pattern into a DIFFERENCE
#      array instead of writing h*w cells. its 2D prefix sum then gives, per
#      cell, how many stamps cover it.
#
#   finally every empty cell must be covered at least once.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def possibleToStamp(self, grid, stampHeight, stampWidth):
        m, n = len(grid), len(grid[0])

        # 1) prefix[i][j] = sum of grid[0..i-1][0..j-1]
        prefix = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m):
            row = grid[i]
            pre_up, pre_cur = prefix[i], prefix[i + 1]
            for j in range(n):
                pre_cur[j + 1] = row[j] + pre_cur[j] + pre_up[j + 1] - pre_up[j]

        # 2) mark every legal stamp into a difference array
        diff = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m - stampHeight + 1):
            for j in range(n - stampWidth + 1):
                bi, bj = i + stampHeight, j + stampWidth
                occupied = (prefix[bi][bj] - prefix[i][bj]
                            - prefix[bi][j] + prefix[i][j])
                if occupied == 0:
                    diff[i][j] += 1
                    diff[i][bj] -= 1
                    diff[bi][j] -= 1
                    diff[bi][bj] += 1

        # 3) prefix-sum the difference array back into coverage counts
        for i in range(m):
            for j in range(n):
                if i:
                    diff[i][j] += diff[i - 1][j]
                if j:
                    diff[i][j] += diff[i][j - 1]
                if i and j:
                    diff[i][j] -= diff[i - 1][j - 1]
                if grid[i][j] == 0 and diff[i][j] == 0:
                    return False
        return True
