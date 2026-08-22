"""

3537. Fill a Special Grid
Medium

You are given a non-negative integer n representing a 2^n x 2^n grid. You must
fill the grid with integers from 0 to 2^2n - 1 to make it special. A grid is
special if it satisfies all the following conditions:

All numbers in the top-right quadrant are smaller than those in the bottom-right
quadrant.

All numbers in the bottom-right quadrant are smaller than those in the
bottom-left quadrant.

All numbers in the bottom-left quadrant are smaller than those in the top-left
quadrant.

Each of its quadrants is also a special grid.

Return the special 2^n x 2^n grid.

Note: Any 1x1 grid is special.

Example 1:

Input: n = 0

Output: [[0]]

Explanation:

The only number that can be placed is 0, and there is only one possible position
in the grid.

Example 2:

Input: n = 1

Output: [[3,0],[2,1]]

Explanation:

The numbers in each quadrant are:

Top-right: 0

Bottom-right: 1

Bottom-left: 2

Top-left: 3

Since 0 < 1 < 2 < 3, this satisfies the given constraints.

Example 3:

Input: n = 2

Output: [[15,12,3,0],[14,13,2,1],[11,8,7,4],[10,9,6,5]]

Explanation:

The numbers in each quadrant are:

Top-right: 3, 0, 2, 1

Bottom-right: 7, 4, 6, 5

Bottom-left: 11, 8, 10, 9

Top-left: 15, 12, 14, 13

max(3, 0, 2, 1) < min(7, 4, 6, 5)

max(7, 4, 6, 5) < min(11, 8, 10, 9)

max(11, 8, 10, 9) < min(15, 12, 14, 13)

This satisfies the first three requirements. Additionally, each quadrant is also
a special grid. Thus, this is a special grid.

Constraints:

0 <= n <= 10

"""

# V0
# IDEA : BUILD BY DOUBLING -- THE FOUR QUADRANTS ARE THE SAME GRID, SHIFTED
#
#   the four conditions force a strict order on the quadrants: top-right holds
#   the smallest block of values, then bottom-right, then bottom-left, then
#   top-left.  each block has exactly s*s of the 4*s*s values, so the offsets
#   are forced too -- 0, s*s, 2*s*s, 3*s*s in that order.
#
#   "each quadrant is also special" then says every quadrant is *the same*
#   grid of half the side, just with its own offset added.  so instead of
#   recursing down to single cells, grow the answer: start from [[0]] and
#   repeatedly paste four offset copies of the current grid together.
#
#   with s doubling each round the total work is s^2 + (s/2)^2 + ... < (4/3)s^2
#   cells, and every row of the new grid is built with two list comprehensions.
#
# time = O(4^n), space = O(4^n)
class Solution(object):
    def specialGrid(self, n):
        grid = [[0]]
        s = 1
        for _ in range(n):
            q = s * s
            top = [[x + 3 * q for x in row] + row for row in grid]
            bottom = [[x + 2 * q for x in row] + [x + q for x in row] for row in grid]
            grid = top + bottom
            s <<= 1
        return grid


# V0-1
# IDEA : TOP-DOWN DIVIDE AND CONQUER -- PAINT EACH QUADRANT WITH ITS OFFSET
#
#   read the four constraints as an ordering of the quadrants: top-right gets
#   the lowest block of values, then bottom-right, then bottom-left, then
#   top-left.  a quadrant of side h owns exactly h*h consecutive values, so its
#   starting value is forced -- base + 0, base + h*h, base + 2h*h, base + 3h*h
#   in that order.
#
#   "each quadrant is also special" is then literally the recursive call, so
#   allocate the 2^n x 2^n board once and paint into it, splitting until a
#   single cell is left and it can only hold `base`.
#
#   the mirror image of the bottom-up doubling above: same recursion tree, but
#   walked from the whole board down instead of from [[0]] up, so it never
#   re-copies an intermediate grid (at the cost of ~4^n Python frames).
#
# time = O(4^n), space = O(4^n)
class Solution(object):
    def specialGrid(self, n):
        side = 1 << n
        grid = [[0] * side for _ in range(side)]

        def fill(r, c, size, base):
            if size == 1:
                grid[r][c] = base
                return
            h = size >> 1
            q = h * h
            fill(r, c + h, h, base)               # top-right    : smallest
            fill(r + h, c + h, h, base + q)       # bottom-right
            fill(r + h, c, h, base + 2 * q)       # bottom-left
            fill(r, c, h, base + 3 * q)           # top-left     : largest

        fill(0, 0, side, 0)
        return grid


# V0-2
# IDEA : CLOSED FORM FROM THE BITS OF (i, j) -- NO RECURSION, NO SHARED STATE
#
#   the recursion above chooses a quadrant at every level purely from one bit
#   of the row index and one bit of the column index, and each choice
#   contributes a fixed base-4 digit:
#
#       (top bit of i, top bit of j) ->  (0,0) top-left     -> digit 3
#                                        (0,1) top-right    -> digit 0
#                                        (1,0) bottom-left  -> digit 2
#                                        (1,1) bottom-right -> digit 1
#
#   which is exactly the n = 1 answer [[3,0],[2,1]] used as a 2x2 lookup table.
#   so grid[i][j] is the base-4 number whose k-th digit is Q[bit_k(i)][bit_k(j)]
#   -- an interleave of the two indices' bits, computable per cell on its own
#   with no recursion and no intermediate grid.
#
# time = O(4^n * n), space = O(4^n)
class Solution(object):
    def specialGrid(self, n):
        Q = ((3, 0), (2, 1))
        side = 1 << n
        out = []
        for i in range(side):
            row = []
            for j in range(side):
                v = 0
                for k in range(n - 1, -1, -1):
                    v = v * 4 + Q[(i >> k) & 1][(j >> k) & 1]
                row.append(v)
            out.append(row)
        return out
