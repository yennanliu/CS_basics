"""

3200. Maximum Height of a Triangle
Easy

You are given two integers red and blue representing the count of red and blue colored balls. You have to arrange these balls to form a triangle such that the 1st row will have 1 ball, the 2nd row will have 2 balls, the 3rd row will have 3 balls, and so on.

All the balls in a particular row should be the same color, and adjacent rows should have different colors.

Return the maximum height of the triangle that can be achieved.


Example 1:

Input: red = 2, blue = 4
Output: 3
Explanation:
The only possible arrangement is shown above.

Example 2:

Input: red = 2, blue = 1
Output: 2
Explanation:
The only possible arrangement is shown above.

Example 3:

Input: red = 1, blue = 1
Output: 1

Example 4:

Input: red = 10, blue = 1
Output: 2


Constraints:

1 <= red, blue <= 100

"""

# V0
# IDEA : ONLY TWO ARRANGEMENTS EXIST — TRY BOTH AND SIMULATE
#
#   once the colour of row 1 is fixed, every later row's colour is forced by
#   the alternation. so there are exactly two candidate triangles, and each
#   can be grown row by row until the next row's colour runs out of balls.
#
#   row h needs h balls of its colour, so the greedy simulation is exact —
#   there is no choice to make along the way.
#
# time = O(sqrt(red + blue)), space = O(1)
class Solution(object):
    def maxHeightOfTriangle(self, red, blue):

        def grow(first, second):
            """first supplies the odd rows, second the even rows"""
            h = 0
            while True:
                need = h + 1
                if h % 2 == 0:
                    if first < need:
                        return h
                    first -= need
                else:
                    if second < need:
                        return h
                    second -= need
                h += 1

        return max(grow(red, blue), grow(blue, red))
