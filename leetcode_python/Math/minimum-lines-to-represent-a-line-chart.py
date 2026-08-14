"""

2280. Minimum Lines to Represent a Line Chart
Medium

You are given a 2D integer array stockPrices where stockPrices[i] = [day_i, price_i] indicates the price of the stock on day day_i is price_i. A line chart is created from the array by plotting the points on an XY plane with the X-axis representing the day and the Y-axis representing the price and connecting adjacent points. One such example is shown below:

Return the minimum number of lines needed to represent the line chart.


Example 1:

Input: stockPrices = [[1,7],[2,6],[3,5],[4,4],[5,4],[6,3],[7,2],[8,1]]
Output: 3
Explanation:
The diagram above represents the input line chart.
The blue line represents the line from (1,7) to (4,4), which can be identified with 1 line.
The red line represents the line from (4,4) to (5,4), which can be identified with 1 line.
The green line represents the line from (5,4) to (8,1), which can be identified with 1 line.
It can be shown that it is not possible to represent the line chart using less than 3 lines.

Example 2:

Input: stockPrices = [[3,4],[1,2],[7,8],[2,3]]
Output: 1
Explanation:
As the diagram above shows, the line chart can be represented with a single line.


Constraints:

1 <= stockPrices.length <= 10^5
stockPrices[i].length == 2
1 <= day_i, price_i <= 10^9
All day_i are distinct.

"""

# V0
# IDEA : SORT BY DAY, THEN COUNT SLOPE CHANGES USING CROSS PRODUCTS
#
#   consecutive points share a line segment exactly while the slope stays the
#   same. comparing slopes as fractions would need floats (lossy at 10^9), so
#   compare them by CROSS-MULTIPLYING instead :
#
#       (y2 - y1) * (x3 - x2)  ==  (y3 - y2) * (x2 - x1)
#
#   which is exact integer arithmetic and never divides by zero.
#
#   the answer starts at 1 (the first segment) and increments on every slope
#   change; 1 point alone needs 0 lines.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minimumLines(self, stockPrices):
        pts = sorted(stockPrices)
        n = len(pts)
        if n <= 1:
            return 0

        res = 1
        for i in range(2, n):
            x1, y1 = pts[i - 2]
            x2, y2 = pts[i - 1]
            x3, y3 = pts[i]
            if (y2 - y1) * (x3 - x2) != (y3 - y2) * (x2 - x1):
                res += 1
        return res
