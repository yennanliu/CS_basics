package LeetCodeJava.Sort;

// https://leetcode.com/problems/campus-bikes/description/
// https://leetcode.ca/all/1057.html

import java.util.Arrays;

/**
 *  1057. Campus Bikes
 * On a campus represented as a 2D grid, there are N workers and M bikes, with N <= M. Each worker and bike is a 2D coordinate on this grid.
 *
 * Our goal is to assign a bike to each worker. Among the available bikes and workers, we choose the (worker, bike) pair with the shortest Manhattan distance between each other, and assign the bike to that worker. (If there are multiple (worker, bike) pairs with the same shortest Manhattan distance, we choose the pair with the smallest worker index; if there are multiple ways to do that, we choose the pair with the smallest bike index). We repeat this process until there are no available workers.
 *
 * The Manhattan distance between two points p1 and p2 is Manhattan(p1, p2) = |p1.x - p2.x| + |p1.y - p2.y|.
 *
 * Return a vector ans of length N, where ans[i] is the index (0-indexed) of the bike that the i-th worker is assigned to.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: workers = [[0,0],[2,1]], bikes = [[1,2],[3,3]]
 * Output: [1,0]
 * Explanation:
 * Worker 1 grabs Bike 0 as they are closest (without ties), and Worker 0 is assigned Bike 1. So the output is [1, 0].
 * Example 2:
 *
 *
 *
 * Input: workers = [[0,0],[1,1],[2,0]], bikes = [[1,0],[2,2],[2,1]]
 * Output: [0,2,1]
 * Explanation:
 * Worker 0 grabs Bike 0 at first. Worker 1 and Worker 2 share the same distance to Bike 2, thus Worker 1 is assigned to Bike 2, and Worker 2 will take Bike 1. So the output is [0,2,1].
 *
 *
 * Note:
 *
 * 0 <= workers[i][j], bikes[i][j] < 1000
 * All worker and bike locations are distinct.
 * 1 <= workers.length <= bikes.length <= 1000
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon ByteDance Google
 *
 */
// NOTE !!! If there are multiple (worker, bike) pairs with the same shortest Manhattan distance, we choose the pair with the smallest worker index; if there are multiple ways to do that, we choose the pair with the smallest bike index). We repeat this process until there are no available workers.
public class CampusBike {

    // V0
    // TODO : validate/implement below
//    public int[] assignBikes(int[][] workers, int[][] bikes) {
//        return null;
//    }

    // V1
    // IDEA : sorting
    // https://leetcode.ca/2018-10-22-1057-Campus-Bikes/
    public int[] assignBikes_1(int[][] workers, int[][] bikes) {
        int n = workers.length;
        int m = bikes.length;
        /** NOTE !!!
         *
         *  - we setup array with (m * n, 3) dimension
         *  - m * n : all combination count
         *  - 3 : for saving 3 variables (dist, i, j)
         *
         */
        int[][] arr = new int[m * n][3];
        // NOTE !!! k start from 0
        for (int i = 0, k = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                int dist
                        = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                // NOTE !!! here we append 3 var
                arr[k++] = new int[] {dist, i, j};
            }
        }

        // sorting
        // If there are multiple (worker, bike) pairs with the same shortest Manhattan distance, we choose the pair with the smallest worker index; if there are multiple ways to do that, we choose the pair with the smallest bike index). We repeat this process until there are no available workers.
        Arrays.sort(arr, (a, b) -> {
            // compare dist
            if (a[0] != b[0]) {
                return a[0] - b[0];
            }
            // compare i (worker index)
            if (a[1] != b[1]) {
                return a[1] - b[1];
            }
            // compare j (bike index)
            return a[2] - b[2];
        });

        // prepare final result
        // init vis1, vis2 to record when the coordination is used (added to result)
        boolean[] vis1 = new boolean[n];
        boolean[] vis2 = new boolean[m];
        int[] ans = new int[n];
        for (int[] e : arr) {
            int i = e[1], j = e[2];
            // NOTE !!! to avoid reuse same element
            if (!vis1[i] && !vis2[j]) {
                vis1[i] = true;
                vis2[j] = true;
                ans[i] = j;
            }
        }
        return ans;
    }

    // V2
}
