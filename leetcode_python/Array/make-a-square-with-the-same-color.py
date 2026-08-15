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
