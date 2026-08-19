package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-boomerangs/

import java.util.HashMap;
import java.util.Map;

/**
 *  447. Number of Boomerangs
 *  Medium
 *
 *  You are given n points in the plane that are all pairwise distinct.
 *
 *  A boomerang is a tuple of points (i, j, k) such that the distance between i and j
 *  equals the distance between i and k (the order of the tuple matters).
 *
 *  Return the number of boomerangs.
 *
 *  Example 1:
 *  Input: points = [[0,0],[1,0],[2,0]]
 *  Output: 2
 *  Explanation: The two boomerangs are [[1,0],[0,0],[2,0]] and [[1,0],[2,0],[0,0]].
 *
 *  Example 2:
 *  Input: points = [[1,1],[2,2],[3,3]]
 *  Output: 2
 *
 *  Constraints:
 *  n == points.length, 1 <= n <= 500
 *  points[i].length == 2, -10^4 <= xi, yi <= 10^4
 *  All the points are unique.
 */
public class NumberOfBoomerangs {

    // V0
    // IDEA: for each pivot i, count how many points share the same squared distance;
    //       a group of size m contributes m * (m - 1) ordered pairs
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int numberOfBoomerangs(int[][] points) {
        if (points == null || points.length < 3) {
            return 0;
        }
        int res = 0;
        for (int[] p : points) {
            Map<Integer, Integer> distCnt = new HashMap<>();
            for (int[] q : points) {
                int dx = p[0] - q[0];
                int dy = p[1] - q[1];
                int d = dx * dx + dy * dy;
                distCnt.put(d, distCnt.getOrDefault(d, 0) + 1);
            }
            for (int m : distCnt.values()) {
                res += m * (m - 1);
            }
        }
        return res;
    }
}
