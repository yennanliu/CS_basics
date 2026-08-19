package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-number-of-visible-points/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  1610. Maximum Number of Visible Points
 *  Hard
 *
 *  You are given an array points, an integer angle, and your location, where
 *  location = [posx, posy] and points[i] = [xi, yi] both denote integral coordinates on
 *  the X-Y plane.
 *
 *  Initially, you are facing directly east from your position. You cannot move from your
 *  position, but you can rotate. Let d be the amount in degrees that you rotate
 *  counterclockwise. Then, your field of view is the inclusive range of angles
 *  [d - angle/2, d + angle/2].
 *
 *  You can see some set of points if, for each point, the angle formed by the point, your
 *  position, and the immediate east direction from your position is in your field of
 *  view.
 *
 *  There can be multiple points at one coordinate. There may be points at your location,
 *  and you can always see these points regardless of your rotation. Points do not
 *  obstruct your vision to other points.
 *
 *  Return the maximum number of points you can see.
 *
 *  Example 1:
 *    Input: points = [[2,1],[2,2],[3,3]], angle = 90, location = [1,1]
 *    Output: 3
 *
 *  Example 3:
 *    Input: points = [[1,0],[2,1]], angle = 13, location = [1,1]
 *    Output: 1
 *
 *  Constraints:
 *    1 <= points.length <= 10^5
 *    points[i].length == 2
 *    location.length == 2
 *    0 <= angle < 360
 *    0 <= posx, posy, xi, yi <= 100
 */
public class MaximumNumberOfVisiblePoints {

    // V0
    // IDEA: POLAR ANGLES + SLIDING WINDOW ON A CIRCULAR SORTED ARRAY
    //
    //   distance is irrelevant - only each point's bearing matters. compute
    //   atan2(dy, dx) for every point, sort, and the question becomes "the densest
    //   window of width `angle` on a circle".
    //
    //   handle the wrap-around by appending every bearing + 2*pi to the array, then slide
    //   a two-pointer window over that doubled array.
    //
    //   NOTE: points sitting exactly ON your location have no bearing; they are always
    //         visible, so count them separately and add at the end.
    //   NOTE: add a tiny epsilon to the width - the field of view is INCLUSIVE and atan2
    //         arithmetic loses a few ulps.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        int px = location.get(0);
        int py = location.get(1);
        int same = 0;
        List<Double> bearings = new ArrayList<>();
        for (List<Integer> p : points) {
            int x = p.get(0);
            int y = p.get(1);
            if (x == px && y == py) {
                same++;
            } else {
                bearings.add(Math.atan2(y - py, x - px));
            }
        }
        Collections.sort(bearings);
        int n = bearings.size();
        if (n == 0) {
            return same;
        }

        double[] ext = new double[2 * n];
        for (int i = 0; i < n; i++) {
            ext[i] = bearings.get(i);
            ext[i + n] = bearings.get(i) + 2 * Math.PI;
        }
        double width = angle * Math.PI / 180.0 + 1e-9;

        int best = 0;
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (j < i) {
                j = i;
            }
            while (j < 2 * n && ext[j] <= ext[i] + width) {
                j++;
            }
            best = Math.max(best, j - i);
        }
        return best + same;
    }
}
