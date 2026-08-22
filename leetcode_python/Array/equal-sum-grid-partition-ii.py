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


# V0-1
# IDEA : BRUTE FORCE -- REBUILD BOTH PIECES AT EVERY CUT AND HUNT THE CELL
#
#   the reference version of the sweep, written so the rule being tested is
#   visible: for each of the m-1 + n-1 cuts, add up the two pieces from
#   scratch, and if they differ by d then look for a cell worth exactly d in
#   the heavier piece that may be punched out without breaking it.
#
#   removability is the whole subtlety, so it lives in one helper.  a piece is
#   always a full-width band of rows, so it is a rectangle: with at least two
#   rows AND at least two columns any single cell can go and the rest is still
#   connected, but a one-cell-wide strip only survives losing one of its two
#   end cells.
#
#   O((m + n) * m * n) -- too slow for the real limits, kept as the statement
#   of intent that the hash-map sweep above optimises.
#
# time = O((m + n) * m * n), space = O(m * n)
class Solution(object):
    def canPartitionGrid(self, grid):
        if self._brute(grid):
            return True
        m, n = len(grid), len(grid[0])
        return self._brute([[grid[i][j] for i in range(m)] for j in range(n)])

    def _brute(self, g):
        rows, cols = len(g), len(g[0])
        for i in range(rows - 1):
            s_top = sum(sum(g[r]) for r in range(i + 1))
            s_bot = sum(sum(g[r]) for r in range(i + 1, rows))
            if s_top == s_bot:
                return True
            if s_top > s_bot:
                if self._removable(g, 0, i, cols, s_top - s_bot):
                    return True
            elif self._removable(g, i + 1, rows - 1, cols, s_bot - s_top):
                return True
        return False

    def _removable(self, g, r0, r1, cols, need):
        # rows r0..r1 x every column: is there a cell == need whose removal
        # leaves the rest of that rectangle connected?
        if r1 > r0 and cols >= 2:                 # both sides >= 2 : any cell
            for r in range(r0, r1 + 1):
                for v in g[r]:
                    if v == need:
                        return True
            return False
        # a strip (one row, or one column) : only its two ends can be dropped
        return need == g[r0][0] or need == g[r1][cols - 1]


# V0-2
# IDEA : BIG-INT BITMASK MEMBERSHIP + ONE FORWARD AND ONE BACKWARD PASS
#
#   the sweep only ever asks "does this piece contain the value d", and cell
#   values are bounded by 10^5, so the value set fits in a single Python
#   integer used as a bitset: bit v is on iff v is present.  membership is then
#   a shift and a mask instead of a dict probe, and no per-cut bookkeeping of
#   *removals* is needed at all.
#
#   that is the point of splitting the walk in two: the forward pass grows the
#   top piece and answers only the cuts where the top is heavier, the backward
#   pass grows the bottom piece and answers only the cuts where the bottom is
#   heavier.  each pass adds bits and never clears them, so no complement
#   structure has to be maintained.
#
#   V = 10^5 is the value ceiling; the bitset holds V bits, i.e. V / 64 words.
#
# time = O(m * n * V / 64), space = O(V / 64 + m + n)
class Solution(object):
    def canPartitionGrid(self, grid):
        if self._sweep(grid):
            return True
        m, n = len(grid), len(grid[0])
        return self._sweep([[grid[i][j] for i in range(m)] for j in range(n)])

    def _sweep(self, g):
        rows, cols = len(g), len(g[0])
        rs = [sum(r) for r in g]
        total = sum(rs)

        # forward : discount one cell of the top piece (rows 0..i)
        mask, s = 0, 0
        for i in range(rows - 1):
            for v in g[i]:
                mask |= 1 << v
            s += rs[i]
            diff = 2 * s - total
            if diff == 0:
                return True
            if diff > 0:
                if i >= 1 and cols >= 2:
                    if (mask >> diff) & 1:
                        return True
                elif diff == g[0][0] or diff == g[i][cols - 1]:
                    return True

        # backward : discount one cell of the bottom piece (rows i+1..rows-1)
        mask, s = 0, 0
        for i in range(rows - 2, -1, -1):
            for v in g[i + 1]:
                mask |= 1 << v
            s += rs[i + 1]
            need = 2 * s - total                  # bottom - top
            if need > 0:
                if rows - i >= 3 and cols >= 2:
                    if (mask >> need) & 1:
                        return True
                elif need == g[i + 1][0] or need == g[rows - 1][cols - 1]:
                    return True
        return False
