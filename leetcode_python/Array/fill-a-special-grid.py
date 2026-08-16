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
