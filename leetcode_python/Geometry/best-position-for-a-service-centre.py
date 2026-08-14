"""

1515. Best Position for a Service Centre
Hard

A delivery company wants to build a new service center in a new city. The company knows the positions of all the customers in this city on a 2D-Map and wants to build the new center in a position such that the sum of the euclidean distances to all customers is minimum.

Given an array positions where positions[i] = [x_i, y_i] is the position of the ith customer on the map, return the minimum sum of the euclidean distances to all customers.

In other words, you need to choose the position of the service center [x_centre, y_centre] such that the sum over all i of sqrt((x_centre - x_i)^2 + (y_centre - y_i)^2) is minimized.

Answers within 10^-5 of the actual value will be accepted.


Example 1:

Input: positions = [[0,1],[1,0],[1,2],[2,1]]
Output: 4.00000
Explanation: As shown, you can see that choosing [x_centre, y_centre] = [1, 1] will make the distance to each customer = 1, the sum of all distances is 4 which is the minimum possible we can achieve.

Example 2:

Input: positions = [[1,1],[3,3]]
Output: 2.82843
Explanation: The minimum possible sum of distances = sqrt(2) + sqrt(2) = 2.82843


Constraints:

1 <= positions.length <= 50
positions[i].length == 2
0 <= x_i, y_i <= 100

"""

# V0
# IDEA : GRADIENT DESCENT (geometric median / Weber point)
#
#   f(x, y) = sum of sqrt((x - xi)^2 + (y - yi)^2) is CONVEX, so it has a
#   single global minimum and plain gradient descent converges to it.
#
#   d f / d x = sum (x - xi) / dist_i      (same shape for y)
#
#   start from the centroid (a good initial guess), step against the
#   gradient with a learning rate that decays each iteration, and stop
#   once the step size falls under eps.
#   NOTE : add a tiny 1e-8 to the denominator — when the current point
#          sits exactly on a customer, dist_i is 0 and would divide by 0.
#
# time = O(iters * n), space = O(1)
from math import sqrt
class Solution(object):
    def getMinDistSum(self, positions):
        n = len(positions)
        x = float(sum(p[0] for p in positions)) / n
        y = float(sum(p[1] for p in positions)) / n

        alpha = 0.5
        decay = 0.999
        eps = 1e-7
        while True:
            gx = gy = 0.0
            total = 0.0
            for px, py in positions:
                a = x - px
                b = y - py
                d = sqrt(a * a + b * b)
                total += d
                gx += a / (d + 1e-8)
                gy += b / (d + 1e-8)

            dx = gx * alpha
            dy = gy * alpha
            x -= dx
            y -= dy
            alpha *= decay

            if abs(dx) <= eps and abs(dy) <= eps:
                return total
