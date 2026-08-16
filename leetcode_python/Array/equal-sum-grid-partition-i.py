"""

3546. Equal Sum Grid Partition I
Medium

You are given an m x n matrix grid of positive integers. Your task is to
determine if it is possible to make either one horizontal or one vertical cut on
the grid such that:

Each of the two resulting sections formed by the cut is non-empty.

The sum of the elements in both sections is equal.

Return true if such a partition exists; otherwise return false.

Example 1:

Input: grid = [[1,4],[2,3]]

Output: true

Explanation:

A horizontal cut between row 0 and row 1 results in two non-empty sections, each
with a sum of 5. Thus, the answer is true.

Example 2:

Input: grid = [[1,3],[2,4]]

Output: false

Explanation:

No horizontal or vertical cut results in two non-empty sections with equal sums.
Thus, the answer is false.

Constraints:

1 <= m == grid.length <= 10^5

1 <= n == grid[i].length <= 10^5

2 <= m * n <= 10^5

1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : A CUT IS JUST A PREFIX -- CHECK EVERY PREFIX SUM AGAINST HALF THE TOTAL
#
#   a horizontal cut after row i puts rows 0..i on one side, so its sum is the
#   prefix sum of the row sums; the other side is whatever is left.  the two
#   halves are therefore equal exactly when that prefix equals total / 2, and
#   the same statement holds column-wise.
#
#   so the whole problem is one pass over the row sums and one pass over the
#   column sums, comparing a running total against half.  if the total is odd
#   no cut can ever work, which is a free early exit.
#
#   both cuts must leave two non-empty pieces, so the last prefix (the entire
#   grid) is excluded from the scan.
#
# time = O(m * n), space = O(n)
class Solution(object):
    def canPartitionGrid(self, grid):
        m, n = len(grid), len(grid[0])
        rows = [sum(r) for r in grid]
        total = sum(rows)
        if total % 2:
            return False
        half = total // 2

        run = 0
        for i in range(m - 1):
            run += rows[i]
            if run == half:
                return True

        cols = [0] * n
        for r in grid:
            for j, v in enumerate(r):
                cols[j] += v
        run = 0
        for j in range(n - 1):
            run += cols[j]
            if run == half:
                return True
        return False
