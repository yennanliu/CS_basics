package LeetCodeJava.HashTable;

// https://leetcode.ca/all/356.html
// https://leetcode.com/problems/line-reflection/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *356. Line Reflection
 * Given n points on a 2D plane, find if there is such a line parallel to y-axis that reflect the given points.
 *
 * Example 1:
 *
 * Input: [[1,1],[-1,1]]
 * Output: true
 * Example 2:
 *
 * Input: [[1,1],[-1,-1]]
 * Output: false
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Google
 * Problem Solution
 * 356-Line-Reflection
 *
 */
public class LineReflection {

    // V0
//    public boolean isReflected(int[][] points) {
//    }

    // V1
    // https://leetcode.ca/2016-11-20-356-Line-Reflection/
    public boolean isReflected_1(int[][] points) {
        final int inf = 1 << 30;
        int minX = inf, maxX = -inf;
        Set<List<Integer>> pointSet = new HashSet<>();
        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);

            //pointSet.add(List.of(p[0], p[1]));
            List<Integer> tmp = new ArrayList<>();
            tmp.add(p[0]);
            tmp.add(p[1]);
            pointSet.add(tmp);
        }
        int s = minX + maxX;
        for (int[] p : points) {
            // TODO: check if below are equals
//            if (!pointSet.contains(List.of(s - p[0], p[1]))) {
//                return false;
//            }
            if (!pointSet.contains(new int[]{s - p[0], p[1]})) {
                return false;
            }
        }
        return true;
    }

    // V2

}
