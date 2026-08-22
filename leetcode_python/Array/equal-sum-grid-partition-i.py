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


# V0-1
# IDEA : TWO POINTERS -- GROW THE LIGHTER SIDE UNTIL THE TWO SIDES BALANCE
#
#   collapse the grid to its row sums (and separately its column sums).  every
#   cell is positive, so the prefix grows and the suffix shrinks monotonically
#   as the cut slides right -- the two curves can cross at most once.
#
#   that lets a pair of pointers walk toward each other instead of scanning:
#   whichever side is currently lighter absorbs the next line, because the
#   lighter side can never be fixed by making the heavier side heavier.  when
#   the pointers become adjacent the only candidate cut left is between them,
#   so one equality test finishes it.  no total / 2 division is needed, which
#   also removes the "odd total" special case.
#
# time = O(m * n), space = O(m + n)
class Solution(object):
    def canPartitionGrid(self, grid):
        m, n = len(grid), len(grid[0])
        rows = [sum(r) for r in grid]
        if self._meet(rows):
            return True

        cols = [0] * n
        for r in grid:
            for j, v in enumerate(r):
                cols[j] += v
        return self._meet(cols)

    def _meet(self, arr):
        if len(arr) < 2:                  # a cut needs two non-empty sides
            return False
        lo, hi = 0, len(arr) - 1
        left, right = arr[lo], arr[hi]
        while lo + 1 < hi:
            if left < right:
                lo += 1
                left += arr[lo]
            else:
                hi -= 1
                right += arr[hi]
        return left == right


# V0-2
# IDEA : 2D PREFIX SUM MATRIX -- EVERY CUT BECOMES AN O(1) RECTANGLE QUERY
#
#   build pre[i][j] = sum of the sub-rectangle [0..i) x [0..j).  then the top
#   part of a horizontal cut after row i-1 is pre[i][n] and the left part of a
#   vertical cut after column j-1 is pre[m][j], both read in constant time, so
#   the cut scan is decoupled from how the sums were accumulated.
#
#   heavier on memory than the running-total version, but it is the shape to
#   reach for when the follow-up asks for arbitrary sub-rectangles (two cuts,
#   a cut plus a hole, ...) rather than only full-width splits.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def canPartitionGrid(self, grid):
        m, n = len(grid), len(grid[0])
        pre = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m):
            row, up, cur = grid[i], pre[i], pre[i + 1]
            run = 0
            for j in range(n):
                run += row[j]
                cur[j + 1] = up[j + 1] + run

        total = pre[m][n]
        if total % 2:
            return False
        half = total // 2

        for i in range(1, m):             # horizontal cut below row i-1
            if pre[i][n] == half:
                return True
        for j in range(1, n):             # vertical cut right of column j-1
            if pre[m][j] == half:
                return True
        return False
