"""

3548. Equal Sum Grid Partition II
Hard

You are given an m x n matrix grid of positive integers. Your task is to
determine if it is possible to make either one horizontal or one vertical cut on
the grid such that:

Each of the two resulting sections formed by the cut is non-empty.

The sum of elements in both sections is equal, or can be made equal by
discounting at most one single cell in total (from either section).

If a cell is discounted, the rest of the section must remain connected.

Return true if such a partition exists; otherwise, return false.

Note: A section is connected if every cell in it can be reached from any other
cell by moving up, down, left, or right through other cells in the section.

Example 1:

Input: grid = [[1,4],[2,3]]

Output: true

Explanation:

A horizontal cut after the first row gives sums 1 + 4 = 5 and 2 + 3 = 5, which
are equal. Thus, the answer is true.

Example 2:

Input: grid = [[1,2],[3,4]]

Output: true

Explanation:

A vertical cut after the first column gives sums 1 + 3 = 4 and 2 + 4 = 6.

By discounting 2 from the right section (6 - 2 = 4), both sections have equal
sums and remain connected. Thus, the answer is true.

Example 3:

Input: grid = [[1,2,4],[2,3,5]]

Output: false

Explanation:

A horizontal cut after the first row gives 1 + 2 + 4 = 7 and 2 + 3 + 5 = 10.

By discounting 3 from the bottom section (10 - 3 = 7), both sections have equal
sums, but they do not remain connected as it splits the bottom section into two
parts ([2] and [5]). Thus, the answer is false.

Example 4:

Input: grid = [[4,1,8],[3,2,6]]

Output: false

Explanation:

No valid cut exists, so the answer is false.

Constraints:

1 <= m == grid.length <= 10^5

1 <= n == grid[i].length <= 10^5

2 <= m * n <= 10^5

1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : SWEEP EVERY CUT, THEN ASK WHETHER THE EXCESS SITS IN A REMOVABLE CELL
#
#   after a cut the two pieces are fixed rectangles, so the only freedom left
#   is which single cell to discount.  dropping a cell of value x from the
#   heavier piece moves the balance by exactly x, so with
#   diff = heavy - light the discount works if and only if the heavier piece
#   holds a cell whose value is diff -- one membership test per cut.
#
#   connectivity is what makes this more than a lookup.  a full rectangle with
#   at least two rows *and* at least two columns stays connected whichever cell
#   is punched out, so there the test really is just "is diff among that
#   piece's values".  a piece that is a single row (or a single column) is a
#   strip, and a strip only survives losing one of its two end cells -- so only
#   two specific values are usable.
#
#   the sweep keeps a value->count map for the growing side and its complement
#   for the shrinking side, so each cut costs O(1) once its row has been folded
#   in.  vertical cuts are the identical walk over the transposed grid.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def canPartitionGrid(self, grid):
        if self._scan(grid):
            return True
        m, n = len(grid), len(grid[0])
        return self._scan([[grid[i][j] for i in range(m)] for j in range(n)])

    def _scan(self, g):
        rows, cols = len(g), len(g[0])
        total = sum(map(sum, g))

        top = {}                              # values of rows 0..i
        bot = {}                              # values of rows i+1..rows-1
        for row in g:
            for v in row:
                bot[v] = bot.get(v, 0) + 1

        s_top = 0
        for i in range(rows - 1):
            for v in g[i]:
                s_top += v
                top[v] = top.get(v, 0) + 1
                c = bot[v] - 1
                if c:
                    bot[v] = c
                else:
                    del bot[v]
            diff = 2 * s_top - total
            if diff == 0:
                return True
            if diff > 0:                      # discount inside rows 0..i
                if i >= 1 and cols >= 2:
                    if diff in top:
                        return True
                elif diff == g[0][0] or diff == g[i][cols - 1]:
                    return True
            else:                             # discount inside rows i+1..
                need = -diff
                if rows - i >= 3 and cols >= 2:
                    if need in bot:
                        return True
                elif need == g[i + 1][0] or need == g[rows - 1][cols - 1]:
                    return True
        return False
