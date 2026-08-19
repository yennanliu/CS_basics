package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-points-inside-the-square/

/**
 *  3143. Maximum Points Inside the Square
 *  Medium
 *
 *  You are given a 2D array points and a string s where points[i] represents the
 *  coordinates of point i, and s[i] represents the tag of point i.
 *
 *  A valid square is a square centered at the origin (0, 0), has edges parallel to the
 *  axes, and does not contain two points with the same tag.
 *
 *  Return the maximum number of points contained in a valid square.
 *
 *  Note:
 *    A point is considered to be inside the square if it lies on or within the square's
 *    boundaries. The side length of the square can be zero.
 *
 *  Example 1:
 *    Input: points = [[2,2],[-1,-2],[-4,4],[-3,1],[3,-3]], s = "abdca"
 *    Output: 2
 *    Explanation: the square of side length 4 covers points[0] and points[1].
 *
 *  Example 3:
 *    Input: points = [[1,1],[-1,-1],[2,-2]], s = "ccd"
 *    Output: 0
 *
 *  Constraints:
 *    1 <= s.length, points.length <= 10^5
 *    points[i].length == 2
 *    -10^9 <= points[i][0], points[i][1] <= 10^9
 *    s.length == points.length
 *    points consists of distinct coordinates.
 *    s consists only of lowercase English letters.
 */
public class MaximumPointsInsideTheSquare {

    // V0
    // IDEA: A SQUARE IS JUST A CHEBYSHEV RADIUS - FIND WHERE THE FIRST TAG REPEATS
    //
    //   a square of half-side r centred at the origin contains exactly the points with
    //   max(|x|, |y|) <= r. so every point is described by one number, its Chebyshev
    //   radius, and growing the square means sweeping r upward.
    //
    //   a tag becomes a conflict the moment its SECOND-smallest radius is admitted, so
    //   the square must stop strictly before
    //
    //       limit = min over tags of (second smallest radius of that tag)
    //
    //   then the answer counts the points with radius < limit. tracking, per tag, only
    //   the two smallest radii is enough - one pass, 26 pairs of numbers.
    /**
     * time = O(N)
     * space = O(1)   // 26 tags
     */
    public int maxPointsInsideSquare(int[][] points, String s) {
        final long INF = Long.MAX_VALUE;
        long[] best1 = new long[26];        // smallest radius per tag
        long[] best2 = new long[26];        // second smallest
        for (int i = 0; i < 26; i++) {
            best1[i] = INF;
            best2[i] = INF;
        }

        int n = points.length;
        long[] radii = new long[n];
        for (int i = 0; i < n; i++) {
            long r = Math.max(Math.abs((long) points[i][0]), Math.abs((long) points[i][1]));
            radii[i] = r;
            int t = s.charAt(i) - 'a';
            if (r < best1[t]) {
                best2[t] = best1[t];
                best1[t] = r;
            } else if (r < best2[t]) {
                best2[t] = r;
            }
        }

        long limit = INF;                  // first radius that would duplicate a tag
        for (int i = 0; i < 26; i++) {
            limit = Math.min(limit, best2[i]);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (radii[i] < limit) {
                res++;
            }
        }
        return res;
    }
}
