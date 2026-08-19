package LeetCodeJava.Math;

// https://leetcode.com/problems/minimize-manhattan-distances/

/**
 *  3102. Minimize Manhattan Distances
 *  Hard
 *
 *  You are given an array points representing integer coordinates of some points on a 2D
 *  plane, where points[i] = [xi, yi].
 *
 *  The distance between two points is defined as their Manhattan distance.
 *
 *  Return the minimum possible value for maximum distance between any two points by
 *  removing exactly one point.
 *
 *  Example 1:
 *    Input: points = [[3,10],[5,15],[10,2],[4,4]]
 *    Output: 12
 *    Explanation: removing the 2nd point leaves a max distance of 12, between (5,15) and
 *                 (4,4) -> |5-4| + |15-4| = 12, which is the smallest achievable.
 *
 *  Example 2:
 *    Input: points = [[1,1],[1,1],[1,1]]
 *    Output: 0
 *
 *  Constraints:
 *    3 <= points.length <= 10^5
 *    points[i].length == 2
 *    1 <= points[i][0], points[i][1] <= 10^8
 */
public class MinimizeManhattanDistances {

    // V0
    // IDEA: ROTATE TO CHEBYSHEV - MANHATTAN MAX BECOMES TWO INDEPENDENT SPREADS
    //
    //   with u = x + y and v = x - y,
    //       |x1-x2| + |y1-y2| = max(|u1-u2|, |v1-v2|)
    //   so the largest pairwise Manhattan distance in a set is simply
    //       max(spread of u, spread of v)      where spread = max - min
    //   no pair enumeration needed.
    //
    //   removing one point can only change a spread if that point is the current extreme,
    //   so keeping the index of the TWO largest and TWO smallest of u and of v is enough:
    //   dropping point i falls back to the runner-up exactly when i held the record.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumDistance(int[][] points) {
        int n = points.length;
        int[] u = new int[n];
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            u[i] = points[i][0] + points[i][1];
            v[i] = points[i][0] - points[i][1];
        }

        int[] uMax = top2(u, true);
        int[] uMin = top2(u, false);
        int[] vMax = top2(v, true);
        int[] vMin = top2(v, false);

        int res = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int hiU = u[uMax[0] == i ? uMax[1] : uMax[0]];
            int loU = u[uMin[0] == i ? uMin[1] : uMin[0]];
            int hiV = v[vMax[0] == i ? vMax[1] : vMax[0]];
            int loV = v[vMin[0] == i ? vMin[1] : vMin[0]];
            res = Math.min(res, Math.max(hiU - loU, hiV - loV));
        }
        return res;
    }

    // indices of the best and the runner-up value (max when `wantMax`, else min)
    private int[] top2(int[] arr, boolean wantMax) {
        int first = -1;
        int second = -1;
        for (int i = 0; i < arr.length; i++) {
            if (first == -1 || better(arr[i], arr[first], wantMax)) {
                second = first;
                first = i;
            } else if (second == -1 || better(arr[i], arr[second], wantMax)) {
                second = i;
            }
        }
        return new int[]{first, second};
    }

    private boolean better(int a, int b, boolean wantMax) {
        return wantMax ? a > b : a < b;
    }
}
