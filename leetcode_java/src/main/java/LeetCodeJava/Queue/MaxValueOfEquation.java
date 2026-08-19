package LeetCodeJava.Queue;

// https://leetcode.com/problems/max-value-of-equation/

/**
 *  1499. Max Value of Equation
 *  Hard
 *
 *  You are given an array points containing the coordinates of points on a 2D
 *  plane, sorted by the x-values, where points[i] = [xi, yi] such that xi < xj
 *  for all 1 <= i < j <= points.length. You are also given an integer k.
 *
 *  Return the maximum value of the equation yi + yj + |xi - xj| where
 *  |xi - xj| <= k and 1 <= i < j <= points.length.
 *
 *  It is guaranteed that there exists at least one pair of points that satisfy
 *  the constraint |xi - xj| <= k.
 *
 *  Example 1:
 *    Input: points = [[1,3],[2,0],[5,10],[6,-10]], k = 1
 *    Output: 4
 *    Explanation: The first two points satisfy |xi - xj| <= 1 and give
 *                 3 + 0 + |1 - 2| = 4. The third and fourth points give
 *                 10 + -10 + |5 - 6| = 1. So the answer is max(4, 1) = 4.
 *
 *  Example 2:
 *    Input: points = [[0,0],[3,0],[9,2]], k = 3
 *    Output: 3
 *    Explanation: Only the first two points are within k in x, giving
 *                 0 + 0 + |0 - 3| = 3.
 *
 *  Constraints:
 *    2 <= points.length <= 10^5
 *    points[i].length == 2
 *    -10^8 <= xi, yi <= 10^8
 *    0 <= k <= 2 * 10^8
 *    xi < xj for all 1 <= i < j <= points.length
 */
public class MaxValueOfEquation {

    // V0
    // IDEA: MONOTONIC DEQUE (SLIDING WINDOW MAXIMUM)
    //       x is strictly increasing, so for i < j we have xi < xj and therefore
    //           yi + yj + |xi - xj| = (yi - xi) + (yj + xj)
    //       -> fix j and maximize (yi - xi) over all i < j with xj - xi <= k
    //       -> classic sliding window maximum: keep a deque of candidate i,
    //          decreasing in (y - x), popping the front once it leaves the window.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findMaxValueOfEquation(int[][] points, int k) {
        int n = points.length;
        // deque of indices, decreasing on (y - x)
        int[] dq = new int[n];
        int head = 0;
        int tail = 0;
        long res = Long.MIN_VALUE;

        for (int j = 0; j < n; j++) {
            int x = points[j][0];
            int y = points[j][1];

            // NOTE !!! drop the candidates that are too far away (x - xi > k)
            while (head < tail && (long) x - points[dq[head]][0] > k) {
                head++;
            }
            if (head < tail) {
                int i = dq[head];
                long cand = (long) y + x + (points[i][1] - (long) points[i][0]);
                if (cand > res) {
                    res = cand;
                }
            }

            // NOTE !!! keep the deque decreasing on (y - x)
            while (head < tail
                    && points[dq[tail - 1]][1] - (long) points[dq[tail - 1]][0] <= y - (long) x) {
                tail--;
            }
            dq[tail++] = j;
        }
        return (int) res;
    }
}
