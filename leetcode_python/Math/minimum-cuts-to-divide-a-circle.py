"""

2481. Minimum Cuts to Divide a Circle
Easy

A valid cut in a circle can be:

A cut that is represented by a straight line that touches two points on the edge of the circle and passes through its center, or
A cut that is represented by a straight line that touches one point on the edge of the circle and its center.

Some valid and invalid cuts are shown in the figures below.

Given the integer n, return the minimum number of cuts needed to divide a circle into n equal slices.


Example 1:

Input: n = 4
Output: 2
Explanation:
The above figure shows how cutting the circle twice through the middle divides it into 4 equal slices.

Example 2:

Input: n = 3
Output: 3
Explanation:
At least 3 cuts are needed to divide the circle into 3 equal slices.
It can be shown that less than 3 cuts cannot result in 3 slices of equal size and shape.
Also note that the first cut will not divide the circle into distinct parts.


Constraints:

1 <= n <= 100

"""

# V0
# IDEA : MATH / CASE SPLIT
#
#   a full diameter cut makes 2 slices, a radius cut makes 1.
#   - n == 1 : the circle is already one slice -> 0 cuts.
#   - n even : pair the n radii into n / 2 diameters -> n / 2 cuts.
#   - n odd (> 1) : no two of the n radii are collinear -> n cuts.
#
# time = O(1), space = O(1)
class Solution(object):
    def numberOfCuts(self, n):
        if n > 1 and n % 2 == 1:
            return n
        return n // 2
