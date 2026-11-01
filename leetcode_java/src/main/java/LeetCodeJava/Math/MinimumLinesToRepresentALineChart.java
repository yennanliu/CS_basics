package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-lines-to-represent-a-line-chart/description/

import java.util.*;

/**
 * 2280. Minimum Lines to Represent a Line Chart
 * Medium
 *
 * You are given a 2D integer array stockPrices where stockPrices[i] = [day_i, price_i] indicates the price of the stock on day day_i is price_i. A line chart is created from the array by plotting the points on an XY plane with the X-axis representing the day and the Y-axis representing the price and connecting adjacent points. One such example is shown below:
 *
 * Return the minimum number of lines needed to represent the line chart.
 *
 *
 * Example 1:
 *
 * Input: stockPrices = [[1,7],[2,6],[3,5],[4,4],[5,4],[6,3],[7,2],[8,1]]
 * Output: 3
 * Explanation:
 * The diagram above represents the input line chart.
 * The blue line represents the line from (1,7) to (4,4), which can be identified with 1 line.
 * The red line represents the line from (4,4) to (5,4), which can be identified with 1 line.
 * The green line represents the line from (5,4) to (8,1), which can be identified with 1 line.
 * It can be shown that it is not possible to represent the line chart using less than 3 lines.
 *
 * Example 2:
 *
 * Input: stockPrices = [[3,4],[1,2],[7,8],[2,3]]
 * Output: 1
 * Explanation:
 * As the diagram above shows, the line chart can be represented with a single line.
 *
 *
 * Constraints:
 *
 * 1 <= stockPrices.length <= 10^5
 * stockPrices[i].length == 2
 * 1 <= day_i, price_i <= 10^9
 * All day_i are distinct.
 *
 */
public class MinimumLinesToRepresentALineChart {

    // V0
    // IDEA: SORT BY DAY, THEN COUNT SLOPE CHANGES WITH CROSS-MULTIPLICATION
    //   comparing slopes as doubles is lossy at 10^9, so compare
    //   (y2 - y1) * (x3 - x2) with (y3 - y2) * (x2 - x1) in exact long arithmetic.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minimumLines(int[][] stockPrices) {
        int n = stockPrices.length;
        // a single point needs no line
        if (n <= 1) {
            return 0;
        }

        // sort a copy by day so the points are in drawing order
        int[][] points = stockPrices.clone();
        Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));

        // the first two points always start one line
        int lines = 1;
        for (int i = 2; i < n; i++) {
            long x1 = points[i - 2][0];
            long y1 = points[i - 2][1];
            long x2 = points[i - 1][0];
            long y2 = points[i - 1][1];
            long x3 = points[i][0];
            long y3 = points[i][1];

            // a different slope means a new line starts at point i - 1
            if ((y2 - y1) * (x3 - x2) != (y3 - y2) * (x2 - x1)) {
                lines++;
            }
        }
        return lines;
    }
}
