package LeetCodeJava.Array;

// https://leetcode.com/problems/best-meeting-point/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 296. Best Meeting Point
 * Hard
 * Lock: Prime
 *
 * Given an m x n binary grid grid where each 1 marks the home of one friend,
 * return the minimal total travel distance.
 *
 * The total travel distance is the sum of the distances between the houses of the friends
 * and the meeting point.
 *
 * The distance is calculated using Manhattan Distance, where
 * distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0,0,1],[0,0,0,0,0],[0,0,1,0,0]]
 * Output: 6
 * Explanation: Given three friends living at (0,0), (0,4), and (2,2).
 * The point (0,2) is an ideal meeting point, as the total travel distance of 2 + 2 + 2 = 6
 * is minimal. So return 6.
 *
 * Example 2:
 *
 * Input: grid = [[1,1]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * grid[i][j] is either 0 or 1.
 * There will be at least two friends in the grid.
 *
 */
public class BestMeetingPoint {

    // V0
    // IDEA: MEDIAN (Manhattan distance decomposes into independent x and y axes)
    /**
     *  |dx| + |dy| means the row coordinate and the column coordinate can be optimized
     *  SEPARATELY. On a single axis, the point minimizing the sum of absolute distances
     *  is the MEDIAN.
     *
     *  Instead of computing the median explicitly, we pair up the OUTERMOST points:
     *  for a sorted list, (arr[-1] - arr[0]) + (arr[-2] - arr[1]) + ... is exactly
     *  the minimal total distance (any point between the innermost pair works).
     *
     *  time  = O(m * n)   // the column list needs a sort: O(m*n*log(m*n)) worst case
     *  space = O(m * n)
     */
    public int minTotalDistance(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }

        /** NOTE !!!
         *
         *  `rows` already comes out sorted (we scan top to bottom),
         *  but `cols` does NOT -> it must be sorted explicitly.
         */
        Collections.sort(cols);

        return minDist(rows) + minDist(cols);
    }

    /** sum of the gaps between symmetric outer pairs */
    private int minDist(List<Integer> arr) {
        int i = 0;
        int j = arr.size() - 1;
        int total = 0;
        while (i < j) {
            total += arr.get(j) - arr.get(i);
            i += 1;
            j -= 1;
        }
        return total;
    }

}
