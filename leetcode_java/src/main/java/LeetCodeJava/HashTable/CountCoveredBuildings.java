package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-covered-buildings/description/

import java.util.*;

/**
 * 3531. Count Covered Buildings
 * Medium
 *
 * You are given a positive integer n, representing an n x n city. You are also
 * given a 2D grid buildings, where buildings[i] = [x, y] denotes a unique building
 * located at coordinates [x, y].
 *
 * A building is covered if there is at least one building in all four directions:
 * left, right, above, and below.
 *
 * Return the number of covered buildings.
 *
 * Example 1:
 *
 * Input: n = 3, buildings = [[1,2],[2,2],[3,2],[2,1],[2,3]]
 *
 * Output: 1
 *
 * Explanation:
 *
 * Only building [2,2] is covered as it has at least one building:
 *
 * above ([1,2])
 *
 * below ([3,2])
 *
 * left ([2,1])
 *
 * right ([2,3])
 *
 * Thus, the count of covered buildings is 1.
 *
 * Example 2:
 *
 * Input: n = 3, buildings = [[1,1],[1,2],[2,1],[2,2]]
 *
 * Output: 0
 *
 * Explanation:
 *
 * No building has at least one building in all four directions.
 *
 * Example 3:
 *
 * Input: n = 5, buildings = [[1,3],[3,2],[3,3],[3,5],[5,3]]
 *
 * Output: 1
 *
 * Explanation:
 *
 * Only building [3,3] is covered as it has at least one building:
 *
 * above ([1,3])
 *
 * below ([5,3])
 *
 * left ([3,2])
 *
 * right ([3,5])
 *
 * Thus, the count of covered buildings is 1.
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 *
 * 1 <= buildings.length <= 10^5
 *
 * buildings[i] = [x, y]
 *
 * 1 <= x, y <= n
 *
 * All coordinates of buildings are unique.
 *
 */
public class CountCoveredBuildings {

    // V0
    // IDEA: ONLY THE EXTREMES OF EACH ROW AND COLUMN CAN FAIL
    //   "has a building above" means the building is not the minimum-x entry of
    //   its column; likewise below / left / right with max-x, min-y, max-y. so
    //   record per column the min and max x, per row the min and max y; a
    //   building is covered exactly when it lies strictly inside all four.
    /**
     * time = O(M)
     * space = O(N)
     */
    public int countCoveredBuildings(int n, int[][] buildings) {
        // coordinates run 1..n; index y holds the column's extremes, index x the row's
        int[] colMinX = new int[n + 1];
        int[] colMaxX = new int[n + 1];
        int[] rowMinY = new int[n + 1];
        int[] rowMaxY = new int[n + 1];
        Arrays.fill(colMinX, Integer.MAX_VALUE);
        Arrays.fill(rowMinY, Integer.MAX_VALUE);

        for (int[] building : buildings) {
            int x = building[0];
            int y = building[1];
            colMinX[y] = Math.min(colMinX[y], x);
            colMaxX[y] = Math.max(colMaxX[y], x);
            rowMinY[x] = Math.min(rowMinY[x], y);
            rowMaxY[x] = Math.max(rowMaxY[x], y);
        }

        int covered = 0;
        for (int[] building : buildings) {
            int x = building[0];
            int y = building[1];
            boolean hasAboveAndBelow = colMinX[y] < x && x < colMaxX[y];
            boolean hasLeftAndRight = rowMinY[x] < y && y < rowMaxY[x];
            if (hasAboveAndBelow && hasLeftAndRight) {
                covered++;
            }
        }
        return covered;
    }
}
