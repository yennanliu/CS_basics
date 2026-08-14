"""

1861. Rotating the Box
Medium

You are given an m x n matrix of characters boxGrid representing a side-view of a box. Each cell of the box is one of the following:

A stone '#'
A stationary obstacle '*'
Empty '.'

The box is rotated 90 degrees clockwise, causing some of the stones to fall due to gravity. Each stone falls down until it lands on an obstacle, another stone, or the bottom of the box. Gravity does not affect the obstacles' positions, and the inertia from the box's rotation does not affect the stones' horizontal positions.

It is guaranteed that each stone in boxGrid rests on an obstacle, another stone, or the bottom of the box.

Return an n x m matrix representing the box after the rotation described above.


Example 1:

Input: boxGrid = [["#",".","#"]]
Output: [["."],
         ["#"],
         ["#"]]

Example 2:

Input: boxGrid = [["#",".","*","."],
                  ["#","#","*","."]]
Output: [["#","."],
         ["#","#"],
         ["*","*"],
         [".","."]]

Example 3:

Input: boxGrid = [["#","#","*",".","*","."],
                  ["#","#","#","*",".","."],
                  ["#","#","#",".","#","."]]
Output: [[".","#","#"],
         [".","#","#"],
         ["#","#","*"],
         ["#","*","."],
         ["#",".","*"],
         ["#",".","."]]


Constraints:

m == boxGrid.length
n == boxGrid[i].length
1 <= m, n <= 500
boxGrid[i][j] is either '#', '*', or '.'.

"""

# V0
# IDEA : SIMULATE GRAVITY FIRST (in the ORIGINAL frame), THEN ROTATE
#
#   after a 90 deg clockwise rotation, cell (i, j) lands at (j, m-1-i).
#   "falling down" in the rotated box == moving RIGHT inside the original
#   row i. so we never need to touch the rotated matrix while simulating.
#
#   per row, scan right -> left with a write pointer :
#     '*'  -> obstacle : nothing can pass, reset write = idx - 1
#     '#'  -> drop it at write, blank the old cell, write -= 1
#     '.'  -> skip
#
#   NOTE : rows are independent, so this is a single left-to-right pass
#          over each row - no repeated settling loop needed.
#
# time = O(m * n), space = O(m * n) for the output
class Solution(object):
    def rotateTheBox(self, boxGrid):
        m, n = len(boxGrid), len(boxGrid[0])

        for i in range(m):
            row = boxGrid[i]
            write = n - 1
            for j in range(n - 1, -1, -1):
                if row[j] == '*':
                    write = j - 1
                elif row[j] == '#':
                    row[j] = '.'
                    row[write] = '#'
                    write -= 1

        res = [[None] * m for _ in range(n)]
        for i in range(m):
            for j in range(n):
                res[j][m - 1 - i] = boxGrid[i][j]

        return res
