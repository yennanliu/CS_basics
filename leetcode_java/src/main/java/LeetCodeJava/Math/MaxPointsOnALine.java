package LeetCodeJava.Math;

// https://leetcode.com/problems/max-points-on-a-line/description/

import DataStructure.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 *  149. Max Points on a Line
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: points = [[1,1],[2,2],[3,3]]
 * Output: 3
 * Example 2:
 *
 *
 * Input: points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= points.length <= 300
 * points[i].length == 2
 * -104 <= xi, yi <= 104
 * All the points are unique.
 *
 *
 */
public class MaxPointsOnALine {

    // V0
//    public int maxPoints(int[][] points) {
//
//    }

    // V1
    // https://leetcode.ca/2016-04-27-149-Max-Points-on-a-Line/
    public int maxPoints_1(int[][] points) {
        int n = points.length;
        int ans = 1;
        for (int i = 0; i < n; ++i) {
            int x1 = points[i][0], y1 = points[i][1];
            Map<String, Integer> cnt = new HashMap<>();
            for (int j = i + 1; j < n; ++j) {
                int x2 = points[j][0], y2 = points[j][1];
                int dx = x2 - x1, dy = y2 - y1;
                int g = gcd(dx, dy);
                String k = (dx / g) + "." + (dy / g);
                cnt.put(k, cnt.getOrDefault(k, 0) + 1);
                ans = Math.max(ans, cnt.get(k) + 1);
            }
        }
        return ans;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // V2
    // https://leetcode.com/problems/max-points-on-a-line/solutions/3017341/java-solution-with-explantation-by-sarth-bwrz/
    // This method returns the maximum number of points that lie on the same line
    // given a set of points represented by the 2D array points
    public int maxPoints_2(int[][] points) {
        // n is the number of points in the array
        int n = points.length;

        // If there are 0 or 1 points, there is at most one line that can be formed
        // (i.e., the line formed by the single point, or no line if there are no points)
        if(n <= 2) return n;

        // Initialize the maximum number of points on a line to 2, since there must be at least 2 points to form a line
        int ans = 2;

        // Iterate through all pairs of points
        for(int i = 0 ;i < n; i++){
            for(int j = i+1; j < n ; j++){
                // temp is the number of points on the line formed by point i and point j
                int temp = 2;
                // Check if any other points are on the same line as point i and point j
                for(int k = j+1 ; k<n ; k++ ){
                    // Check if point k is on the same line as point i and point j
                    // This is done by checking if the slope between point i and point k is equal to the slope between point i and point j
                    int x = (points[j][1] - points[i][1]) * (points[k][0] - points[i][0]);
                    int y = (points[k][1] - points[i][1]) * (points[j][0] - points[i][0]);
                    if(x == y){
                        // If the slopes are equal, point k is on the same line as point i and point j
                        temp++;
                    }
                }
                // Update the maximum number of points on a line if necessary
                if(temp > ans){
                    ans = temp;
                }
            }
        }
        // Return the maximum number of points on a line
        return ans;
    }


    // V3
    public int maxPoints_3(int[][] points) {
        if (points.length <= 2) return points.length;

        int ans = 0;

        for (int i = 0; i < points.length; ++i) {
            Map<Pair<Integer, Integer>, Integer> slopeCount = new HashMap<>();
            int samePoints = 1; // Count of identical points to point i
            int maxPoints = 0;  // Max points with same slope from point i

            for (int j = i + 1; j < points.length; ++j) {
                int[] p1 = points[i];
                int[] p2 = points[j];

                if (p1[0] == p2[0] && p1[1] == p2[1]) {
                    ++samePoints;
                } else {
                    Pair<Integer, Integer> slope = getSlope(p1, p2);
                    slopeCount.merge(slope, 1, Integer::sum);
                    maxPoints = Math.max(maxPoints, slopeCount.get(slope));
                }
            }

            ans = Math.max(ans, samePoints + maxPoints);
        }

        return ans;
    }

    private Pair<Integer, Integer> getSlope(int[] p, int[] q) {
        int dx = p[0] - q[0];
        int dy = p[1] - q[1];

        // Handle vertical line (dx = 0)
        if (dx == 0) {
            return new Pair<>(0, 1); // Represent vertical as (0,1)
        }
        // Handle horizontal line (dy = 0)
        if (dy == 0) {
            return new Pair<>(1, 0); // Represent horizontal as (1,0)
        }

        // Reduce fraction using GCD
        int g = gcd_3(Math.abs(dx), Math.abs(dy));
        dx /= g;
        dy /= g;

        // Ensure consistent sign: make sure dx is positive, or if dx is negative, flip signs
        if (dx < 0) {
            dx = -dx;
            dy = -dy;
        }

        return new Pair<>(dx, dy);
    }

    private int gcd_3(int a, int b) {
        return b == 0 ? a : gcd_3(b, a % b);
    }


}
