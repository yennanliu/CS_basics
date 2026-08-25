"""

2128. Remove All Ones With Row and Column Flips
Medium
(premium / locked problem)

You are given an m x n binary matrix grid.

In one operation, you can choose any row or column and flip each value in that row or column (i.e., changing all 0's to 1's, and all 1's to 0's).

Return true if it is possible to remove all 1's from grid using any number of operations or false otherwise.


Example 1:

Input: grid = [[0,1,0],[1,0,1],[0,1,0]]
Output: true
Explanation: One possible way to remove all 1's from grid is to:
- Flip the middle row
- Flip the middle column

Example 2:

Input: grid = [[1,1,0],[0,0,0],[0,0,0]]
Output: false
Explanation: It is impossible to remove all 1's from grid.

Example 3:

Input: grid = [[0]]
Output: true
Explanation: There are no 1's in grid and it is already all 0's.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : EVERY ROW MUST EQUAL ROW 0 OR ITS EXACT COMPLEMENT
#
#   fix the column flips first : whatever they are, they turn row 0 into all
#   zeros, and that choice is forced (flip column j iff grid[0][j] == 1).
#
#   after those column flips, each remaining row is now either all 0s
#   (nothing left to do) or all 1s (one row flip clears it). "all 0s or all
#   1s after that transformation" is exactly the condition
#       row == row0   or   row == complement(row0)
#
#   so one pass comparing every row against row 0 settles it.
#
# time = O(m * n), space = O(n)
class Solution(object):
    def removeOnes(self, grid):
        first = grid[0]
        flipped = [1 - x for x in first]
        return all(row == first or row == flipped for row in grid)


# V0-1
# IDEA : CONSTRUCTIVE SIMULATION — ACTUALLY PERFORM THE FORCED FLIPS
#
#   instead of reasoning about the invariant, just do the flips the greedy
#   argument forces and look at what is left :
#
#     1) flip every column j with grid[0][j] == 1  -> row 0 becomes all zeros
#     2) flip every row i whose first cell is now 1
#     3) the grid is clearable iff it is now entirely zeros
#
#   step 1 is forced (row 0 has to be cleared somehow, and a row flip on row 0
#   only swaps which columns need flipping), and once the columns are fixed the
#   row flips are forced too — so if this greedy plan fails, no plan works.
#
#   NOTE : works on a copy, so the caller's grid is not mutated.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def removeOnes(self, grid):
        m, n = len(grid), len(grid[0])
        g = [row[:] for row in grid]

        for j in range(n):
            if g[0][j] == 1:
                for i in range(m):
                    g[i][j] ^= 1

        for i in range(m):
            if g[i][0] == 1:
                for j in range(n):
                    g[i][j] ^= 1

        return all(v == 0 for row in g for v in row)


# V0-2
# IDEA : BITMASK — PACK EACH ROW INTO AN INTEGER
#
#   a row of n binary cells is just an n-bit integer, and "flip this whole row"
#   is a single XOR with the all-ones mask.  the condition of V0 then becomes
#
#       mask_i == mask_0   or   mask_i == mask_0 ^ full
#
#   which is one XOR + one compare per row instead of an element-wise list
#   comparison, and the whole grid is stored in m machine-word-ish ints.
#
# time = O(m * n), space = O(m)  (m packed ints, O(1) machine words if n <= 64)
class Solution(object):
    def removeOnes(self, grid):
        n = len(grid[0])
        full = (1 << n) - 1

        masks = []
        for row in grid:
            v = 0
            for bit in row:
                v = (v << 1) | bit
            masks.append(v)

        base = masks[0]
        return all(v == base or v == (base ^ full) for v in masks)
