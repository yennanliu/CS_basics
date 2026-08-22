"""

3127. Make a Square with the Same Color
Easy

You are given a 2D matrix grid of size 3 x 3 consisting only of characters 'B' and 'W'. Character 'W' represents the white color, and character 'B' represents the black color.

Your task is to change the color of at most one cell so that the matrix has a 2 x 2 square where all cells are of the same color.

Return true if it is possible to create a 2 x 2 square of the same color, otherwise, return false.


Example 1:

Input: grid = [["B","W","B"],["B","W","W"],["B","W","B"]]
Output: true
Explanation:
It can be done by changing the color of the grid[0][2].

Example 2:

Input: grid = [["B","W","B"],["W","B","W"],["B","W","B"]]
Output: false
Explanation:
It cannot be done by changing at most one cell.

Example 3:

Input: grid = [["B","W","B"],["B","W","W"],["B","W","W"]]
Output: true
Explanation:
The grid already contains a 2 x 2 square of the same color.


Constraints:

grid.length == 3
grid[i].length == 3
grid[i][j] is either 'W' or 'B'.

"""

# V0
# IDEA : A 2x2 BLOCK IS FIXABLE IFF IT IS NOT ALREADY 2-2 SPLIT
#
#   with one change allowed, a block of four cells can be made uniform
#   whenever at least three of them already agree — i.e. its count of 'B'
#   is 0, 1, 3 or 4. only an even 2-2 split needs two changes.
#
#   there are just four 2x2 blocks in a 3x3 grid, so check them all.
#
# time = O(1), space = O(1)
class Solution(object):
    def canMakeSquare(self, grid):
        for r in range(2):
            for c in range(2):
                blacks = sum(1
                             for dr in range(2)
                             for dc in range(2)
                             if grid[r + dr][c + dc] == 'B')
                if blacks != 2:
                    return True
        return False


# V0-1
# IDEA : BRUTE FORCE — TRY THE 9 SINGLE FLIPS (AND NO FLIP AT ALL)
#
#   "at most one change" is small enough to enumerate literally : the grid
#   as given plus the 9 one-cell mutations, each tested for a uniform 2x2
#   block. no insight about counts needed.
#
# time = O(1), space = O(1)   (10 candidate grids * 4 blocks)
class Solution(object):
    def canMakeSquare(self, grid):
        def has_square(g):
            for r in range(2):
                for c in range(2):
                    cell = g[r][c]
                    if (g[r][c + 1] == cell and g[r + 1][c] == cell
                            and g[r + 1][c + 1] == cell):
                        return True
            return False

        if has_square(grid):
            return True
        for r in range(3):
            for c in range(3):
                g = [list(row) for row in grid]
                g[r][c] = 'W' if g[r][c] == 'B' else 'B'
                if has_square(g):
                    return True
        return False


# V0-2
# IDEA : PRECOMPUTED 4-BIT LOOKUP TABLE OVER THE 16 POSSIBLE BLOCKS
#
#   read a 2x2 block as a 4-bit mask (bit set = 'B'). all 16 masks are
#   enumerated ONCE at import time, and a mask is recorded as fixable when
#   it is already uniform or becomes uniform after flipping one of its bits.
#   the grid scan then answers each block with a single set lookup, doing no
#   counting or reasoning of its own.
#
# time = O(1), space = O(1)
def _build_fixable_masks():
    good = set()
    for mask in range(16):
        for cand in [mask] + [mask ^ (1 << b) for b in range(4)]:
            if cand == 0b0000 or cand == 0b1111:
                good.add(mask)
                break
    return good
_FIXABLE_MASKS = _build_fixable_masks()
class Solution(object):
    def canMakeSquare(self, grid):
        offsets = ((0, 0), (0, 1), (1, 0), (1, 1))
        for r in range(2):
            for c in range(2):
                mask = 0
                for bit, (dr, dc) in enumerate(offsets):
                    if grid[r + dr][c + dc] == 'B':
                        mask |= 1 << bit
                if mask in _FIXABLE_MASKS:
                    return True
        return False
