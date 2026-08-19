package LeetCodeJava.Math;

// https://leetcode.com/problems/best-position-for-a-service-centre/

/**
 *  1515. Best Position for a Service Centre
 *  Hard
 *
 *  A delivery company wants to build a new service center in a new city. The company
 *  knows the positions of all the customers in this city on a 2D-Map and wants to build
 *  the new center in a position such that the sum of the euclidean distances to all
 *  customers is minimum.
 *
 *  Given an array positions where positions[i] = [x_i, y_i] is the position of the ith
 *  customer on the map, return the minimum sum of the euclidean distances to all customers.
 *
 *  In other words, you need to choose the position of the service center
 *  [x_centre, y_centre] such that the sum over all i of
 *  sqrt((x_centre - x_i)^2 + (y_centre - y_i)^2) is minimized.
 *
 *  Answers within 10^-5 of the actual value will be accepted.
 *
 *  Example 1:
 *    Input: positions = [[0,1],[1,0],[1,2],[2,1]]
 *    Output: 4.00000
 *    Explanation: choosing [x_centre, y_centre] = [1, 1] makes the distance to each
 *                 customer = 1, so the sum of all distances is 4.
 *
 *  Example 2:
 *    Input: positions = [[1,1],[3,3]]
 *    Output: 2.82843
 *    Explanation: the minimum possible sum of distances = sqrt(2) + sqrt(2) = 2.82843
 *
 *  Constraints:
 *    1 <= positions.length <= 50
 *    positions[i].length == 2
 *    0 <= x_i, y_i <= 100
 */
public class BestPositionForAServiceCentre {

    // V0
    // IDEA: GRADIENT DESCENT ON THE GEOMETRIC MEDIAN (WEBER POINT)
    //
    //   f(x, y) = sum sqrt((x - xi)^2 + (y - yi)^2) is CONVEX, so it has a single
    //   global minimum and plain gradient descent converges to it.
    //
    //       df/dx = sum (x - xi) / dist_i          (same shape for y)
    //
    //   start from the centroid (a good initial guess), step against the gradient
    //   with a learning rate that decays each iteration, and stop once the step
    //   size falls under eps.
    //   NOTE: add a tiny 1e-8 to the denominator - when the current point sits
    //         exactly on a customer, dist_i is 0 and would divide by 0.
    /**
     * time = O(iters * N)
     * space = O(1)
     */
    public double getMinDistSum(int[][] positions) {
        int n = positions.length;
        double x = 0.0;
        double y = 0.0;
        for (int[] p : positions) {
            x += p[0];
            y += p[1];
        }
        x /= n;
        y /= n;

        double alpha = 0.5;
        double decay = 0.999;
        double eps = 1e-7;
        double total = 0.0;

        for (int it = 0; it < 200000; it++) {
            double gx = 0.0;
            double gy = 0.0;
            total = 0.0;
            for (int[] p : positions) {
                double a = x - p[0];
                double b = y - p[1];
                double d = Math.sqrt(a * a + b * b);
                total += d;
                gx += a / (d + 1e-8);
                gy += b / (d + 1e-8);
            }

            double dx = gx * alpha;
            double dy = gy * alpha;
            x -= dx;
            y -= dy;
            alpha *= decay;

            if (Math.abs(dx) <= eps && Math.abs(dy) <= eps) {
                break;
            }
        }
        return total;
    }
}
