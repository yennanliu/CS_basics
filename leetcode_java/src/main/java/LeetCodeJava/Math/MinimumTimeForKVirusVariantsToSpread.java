package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-time-for-k-virus-variants-to-spread/

import java.util.Arrays;

/**
 *  1956. Minimum Time For K Virus Variants to Spread
 *  Hard
 *
 *  There are n unique virus variants in an infinite 2D grid. You are given a 2D array
 *  points, where points[i] = [xi, yi] represents a virus originating at (xi, yi) on day
 *  0. Note that it is possible for multiple virus variants to originate at the same point.
 *
 *  Every day, each cell infected with a virus variant will spread the virus to all
 *  neighboring points in the four cardinal directions (i.e. up, down, left, and right).
 *  If a cell has multiple variants, all the variants will spread without interfering with
 *  each other.
 *
 *  Given an integer k, return the minimum integer number of days for any point to contain
 *  at least k of the unique virus variants.
 *
 *  Example 1:
 *    Input: points = [[1,1],[6,1]], k = 2
 *    Output: 3
 *    Explanation: on day 3, (3,1) and (4,1) contain both variants.
 *
 *  Example 3:
 *    Input: points = [[3,3],[1,2],[9,2]], k = 3
 *    Output: 4
 *    Explanation: on day 4, (5,2) contains all 3 variants.
 *
 *  Constraints:
 *    n == points.length
 *    2 <= n <= 50
 *    points[i].length == 2
 *    1 <= xi, yi <= 100
 *    2 <= k <= n
 */
public class MinimumTimeForKVirusVariantsToSpread {

    // V0
    // IDEA: ENUMERATE MEETING CELLS + k-TH SMALLEST MANHATTAN DISTANCE
    //
    //   after d days a variant covers exactly the cells within Manhattan distance d of
    //   its origin. so a cell c holds >= k variants on day d iff at least k of the
    //   origins are within Manhattan distance d of c.
    //
    //   the best cell for any subset always lies inside the bounding box of that subset
    //   (clamping a coordinate into the box never increases |x - xi|), and all coordinates
    //   are in [1, 100] -> only 100 x 100 cells need checking.
    //
    //   for each candidate cell: sort its distances to the n origins and take the k-th
    //   smallest; the answer is the minimum of those over all cells.
    /**
     * time = O(100 * 100 * N log N)
     * space = O(N)
     */
    public int minDayskVariants(int[][] points, int k) {
        int loX = Integer.MAX_VALUE, hiX = Integer.MIN_VALUE;
        int loY = Integer.MAX_VALUE, hiY = Integer.MIN_VALUE;
        for (int[] p : points) {
            loX = Math.min(loX, p[0]);
            hiX = Math.max(hiX, p[0]);
            loY = Math.min(loY, p[1]);
            hiY = Math.max(hiY, p[1]);
        }

        int n = points.length;
        int[] dists = new int[n];
        int res = Integer.MAX_VALUE;
        for (int x = loX; x <= hiX; x++) {
            for (int y = loY; y <= hiY; y++) {
                for (int i = 0; i < n; i++) {
                    dists[i] = Math.abs(points[i][0] - x) + Math.abs(points[i][1] - y);
                }
                Arrays.sort(dists);
                if (dists[k - 1] < res) {
                    res = dists[k - 1];
                }
            }
        }
        return res;
    }
}
