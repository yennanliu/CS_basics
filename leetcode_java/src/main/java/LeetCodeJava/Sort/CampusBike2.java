package LeetCodeJava.Sort;

// https://leetcode.com/problems/campus-bikes-ii/description/
// https://leetcode.ca/2018-10-31-1066-Campus-Bikes-II/#google_vignette

import java.util.Arrays;
import java.util.Comparator;

/**
 *  1066. Campus Bikes II
 * On a campus represented as a 2D grid, there are N workers and M  bikes, with N <= M. Each worker and  bike is a 2D coordinate on this grid.
 *
 * We assign one unique bike to each worker so that the sum of the Manhattan distances between each worker and their assigned bike is minimized.
 *
 * The Manhattan distance between two points p1 and p2 is Manhattan(p1, p2) = |p1.x - p2.x| + |p1.y - p2.y|.
 *
 * Return the minimum possible sum of Manhattan distances between each worker and their assigned bike.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: workers = [[0,0],[2,1]], bikes = [[1,2],[3,3]]
 * Output: 6
 * Explanation:
 * We assign bike 0 to worker 0, bike 1 to worker 1. The Manhattan distance of both assignments is 3, so the output is 6.
 * Example 2:
 *
 *
 *
 * Input: workers = [[0,0],[1,1],[2,0]], bikes = [[1,0],[2,2],[2,1]]
 * Output: 4
 * Explanation:
 * We first assign bike 0 to worker 0, then assign bike 1 to worker 1 or worker 2, bike 2 to worker 2 or worker 1. Both assignments lead to sum of the Manhattan distances as 4.
 *
 *
 * Note:
 *
 * 0 <= workers[i][0], workers[i][1], bikes[i][0], bikes[i][1] < 1000
 * All worker and bike locations are distinct.
 * 1 <= workers.length <= bikes.length <= 10
 *
 */
public class CampusBike2 {

    // V0
    // IDEA : custom sorting (Comparator), already-visited check
    // TODO : validate below
//    public int assignBikes(int[][] workers, int[][] bikes){
//
//        int n = workers.length;
//        int m = bikes.length;
//        int k = 0;
//        // get all possible Manhattan distances
//        int[][] cache = new int[n * m][3];
//        for (int i = 0; i < n; i++){
//            for (int j = 0; j < m; j++){
//                int dist
//                        = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
//                // NOTE !!! here we append 3 var
//                //cache.add(dist, i, j);
//                cache[k] = new int[]{dist, i, j};
//                k += 1;
//            }
//        }
//
//        // sorting
//        Arrays.stream(cache).sorted(new Comparator<int[]>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                // dist
//                if (o1[0] != o2[0]){
//                    return o1[0] - o2[0];
//                }
//                // compare i (worker index)
//                if (o1[1] != o2[1]){
//                    return o1[1] - o2[1];
//                }
//                // compare j (bike index)
////                if (o1[2] != o2[2]){
////                    return o1[2] - o2[2];
////                }
//                // TODO : check
//                // return 0;
//                return o1[2] - o2[2];
//            }
//        });
//
//        int res = 0;
//
//        // prepare final result
//        Boolean[] visited_1 = new Boolean[n];
//        Boolean[] visited_2 = new Boolean[m];
//        for (int[] c : cache){
//            //Object y = x;
//            int dist = c[0];
//            int i = c[1];
//            int j = c[2];
//            if (!visited_1[i] && !visited_2[j]){
//                res += dist;
//                visited_1[i] = true;
//                visited_2[j] = true;
//            }
//        }
//
//        return res;
//    }


    // V1_1
    // https://leetcode.ca/2018-10-31-1066-Campus-Bikes-II/#google_vignette
    public int assignBikes_1_1(int[][] workers, int[][] bikes) {
        int n = workers.length;
        int m = bikes.length;
        int[][] f = new int[n + 1][1 << m];
        for (int[] g : f) {
            Arrays.fill(g, 1 << 30);
        }
        f[0][0] = 0;
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < 1 << m; ++j) {
                for (int k = 0; k < m; ++k) {
                    if ((j >> k & 1) == 1) {
                        int d = Math.abs(workers[i - 1][0] - bikes[k][0])
                                + Math.abs(workers[i - 1][1] - bikes[k][1]);
                        f[i][j] = Math.min(f[i][j], f[i - 1][j ^ (1 << k)] + d);
                    }
                }
            }
        }
        return Arrays.stream(f[n]).min().getAsInt();
    }

    // V2
    // https://github.com/doocs/leetcode/blob/main/solution/1000-1099/1066.Campus%20Bikes%20II/README.md
    // IDEA : ROUTE COMPRESSION + DP
    public int assignBikes_2(int[][] workers, int[][] bikes) {
        int n = workers.length;
        int m = bikes.length;
        int[][] f = new int[n + 1][1 << m];
        for (int[] g : f) {
            Arrays.fill(g, 1 << 30);
        }
        f[0][0] = 0;
        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < 1 << m; ++j) {
                for (int k = 0; k < m; ++k) {
                    if ((j >> k & 1) == 1) {
                        int d = Math.abs(workers[i - 1][0] - bikes[k][0])
                                + Math.abs(workers[i - 1][1] - bikes[k][1]);
                        f[i][j] = Math.min(f[i][j], f[i - 1][j ^ (1 << k)] + d);
                    }
                }
            }
        }
        return Arrays.stream(f[n]).min().getAsInt();
    }


    // V3
    // https://blog.csdn.net/qq_46105170/article/details/119177348

}
