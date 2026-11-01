package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/shortest-distance-to-target-color/description/
// https://leetcode.ca/2019-02-24-1182-Shortest-Distance-to-Target-Color/

import java.util.ArrayList;
import java.util.List;

/**
 * 1182. Shortest Distance to Target Color
 * Medium
 *
 * You are given an array colors, in which there are three colors: 1, 2 and 3.
 *
 * You are also given some queries. Each query consists of two integers i and c,
 * return the shortest distance between the given index i and the target color c.
 * If there is no solution return -1.
 *
 *
 * Example 1:
 *
 * Input: colors = [1,1,2,1,3,2,2,3,3], queries = [[1,3],[2,2],[6,1]]
 * Output: [3,0,3]
 * Explanation:
 * The nearest 3 from index 1 is at index 4 (3 steps away).
 * The nearest 2 from index 2 is at index 2 itself (0 steps away).
 * The nearest 1 from index 6 is at index 3 (3 steps away).
 *
 * Example 2:
 *
 * Input: colors = [1,2], queries = [[0,3]]
 * Output: [-1]
 * Explanation: There is no 3 in the array.
 *
 *
 * Constraints:
 *
 * 1 <= colors.length <= 5*10^4
 * 1 <= colors[i] <= 3
 * 1 <= queries.length <= 5*10^4
 * queries[i].length == 2
 * 0 <= queries[i][0] < colors.length
 * 1 <= queries[i][1] <= 3
 *
 */
public class ShortestDistanceToTargetColor {

    private static final int COLOR_COUNT = 3;

    // V0
    // IDEA: PREFIX / SUFFIX DP (NEAREST OCCURRENCE OF EACH COLOR ON EACH SIDE)
    /**
     * left[i][c]  = largest index <= i holding color c+1   (-1 if none)
     * right[i][c] = smallest index >= i holding color c+1  (n if none)
     *
     * left[i]  = left[i-1],  then left[i][colors[i]-1]  = i
     * right[i] = right[i+1], then right[i][colors[i]-1] = i
     *
     * a query (i, c) is then O(1): min(i - left, right - i), skipping a side that missed
     */
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public List<Integer> shortestDistanceColor(int[] colors, int[][] queries) {
        int n = colors.length;
        int[][] left = new int[n][COLOR_COUNT];
        int[][] right = new int[n][COLOR_COUNT];

        // left-to-right: inherit the previous row, then record this index's color
        for (int i = 0; i < n; i++) {
            for (int c = 0; c < COLOR_COUNT; c++) {
                left[i][c] = (i > 0) ? left[i - 1][c] : -1;
            }
            left[i][colors[i] - 1] = i;
        }

        // right-to-left: same, from the other side
        for (int i = n - 1; i >= 0; i--) {
            for (int c = 0; c < COLOR_COUNT; c++) {
                right[i][c] = (i < n - 1) ? right[i + 1][c] : n;
            }
            right[i][colors[i] - 1] = i;
        }

        List<Integer> result = new ArrayList<>();
        for (int[] query : queries) {
            int index = query[0];
            int color = query[1] - 1;
            int nearestLeft = left[index][color];
            int nearestRight = right[index][color];

            // a missing side keeps best at n, which no real distance can reach
            int best = n;
            if (nearestLeft != -1) {
                best = Math.min(best, index - nearestLeft);
            }
            if (nearestRight != n) {
                best = Math.min(best, nearestRight - index);
            }
            result.add(best == n ? -1 : best);
        }
        return result;
    }
}
