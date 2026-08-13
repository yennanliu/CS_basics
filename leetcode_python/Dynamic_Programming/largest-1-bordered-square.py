"""

1139. Largest 1-Bordered Square
Medium

Given a 2D grid of 0s and 1s, return the number of elements in the largest square subgrid
that has all 1s on its border, or 0 if such a subgrid doesn't exist in the grid.


Example 1:

Input: grid = [[1,1,1],[1,0,1],[1,1,1]]
Output: 9

Example 2:

Input: grid = [[1,1,0,0]]
Output: 1


Constraints:

1 <= grid.length <= 100
1 <= grid[0].length <= 100
grid[i][j] is 0 or 1

"""

# V0
# IDEA : DP (suffix counts) + ENUMERATION
#
#  DP def:
#    - down[i][j]  = how many consecutive 1s go DOWN  from (i, j)  (including itself)
#    - right[i][j] = how many consecutive 1s go RIGHT from (i, j)  (including itself)
#
#  DP eq (scan bottom-right -> top-left):
#    - down[i][j]  = down[i+1][j] + 1   if grid[i][j] == 1 else 0
#    - right[i][j] = right[i][j+1] + 1  if grid[i][j] == 1 else 0
#
#  then a square with top-left (i, j) and side k is valid iff
#    right[i][j] >= k and down[i][j] >= k          (top edge + left edge)
#    and right[i+k-1][j] >= k and down[i][j+k-1] >= k  (bottom edge + right edge)
# time = O(m * n * min(m, n))
# space = O(m * n)
class Solution(object):
    def largest1BorderedSquare(self, grid):
        m, n = len(grid), len(grid[0])
        down = [[0] * n for _ in range(m + 1)]
        right = [[0] * (n + 1) for _ in range(m)]

        for i in range(m - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                if grid[i][j] == 1:
                    down[i][j] = down[i + 1][j] + 1
                    right[i][j] = right[i][j + 1] + 1

        for k in range(min(m, n), 0, -1):
            for i in range(m - k + 1):
                for j in range(n - k + 1):
                    if (right[i][j] >= k and down[i][j] >= k
                            and right[i + k - 1][j] >= k
                            and down[i][j + k - 1] >= k):
                        return k * k
        return 0
