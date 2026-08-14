"""

1954. Minimum Garden Perimeter to Collect Enough Apples
Medium

In a garden represented as an infinite 2D grid, there is an apple tree planted at every integer coordinate. The apple tree planted at an integer coordinate (i, j) has |i| + |j| apples growing on it.

You will buy an axis-aligned square plot of land that is centered at (0, 0).

Given an integer neededApples, return the minimum perimeter of a plot such that at least neededApples apples are inside or on the perimeter of that plot.

The value of |x| is defined as:

x if x >= 0
-x if x < 0


Example 1:

Input: neededApples = 1
Output: 8
Explanation: A square plot of side length 1 does not contain any apples.
However, a square plot of side length 2 has 12 apples inside (as depicted in the image above).
The perimeter is 2 * 4 = 8.

Example 2:

Input: neededApples = 13
Output: 16

Example 3:

Input: neededApples = 1000000000
Output: 5040


Constraints:

1 <= neededApples <= 10^15

"""

# V0
# IDEA : CLOSED-FORM APPLE COUNT + BINARY SEARCH ON THE HALF-SIDE
#
#   let the plot span [-x, x] in both axes (side 2x, perimeter 8x). summing
#   |i| + |j| over that square telescopes to
#
#         apples(x) = 2 * x * (x + 1) * (2x + 1)
#
#   (check : x = 1 -> 2*1*2*3 = 12, which matches the picture in example 1).
#
#   apples(x) is strictly increasing, so binary search the smallest x with
#   apples(x) >= neededApples and return 8 * x.
#
#   NOTE : neededApples <= 10^15 and apples(10^5) ~ 2*10^15, so x = 10^5 is a
#          safe upper bound for the search.
#
# time = O(log X), space = O(1)
class Solution(object):
    def minimumPerimeter(self, neededApples):
        def apples(x):
            return 2 * x * (x + 1) * (2 * x + 1)

        lo, hi = 1, 100000
        while lo < hi:
            mid = (lo + hi) // 2
            if apples(mid) >= neededApples:
                hi = mid
            else:
                lo = mid + 1
        return 8 * lo
