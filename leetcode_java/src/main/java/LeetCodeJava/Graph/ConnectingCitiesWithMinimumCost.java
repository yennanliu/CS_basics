package LeetCodeJava.Graph;

// https://leetcode.ca/all/1135.html

import java.util.Arrays;
import java.util.Comparator;

/**
 *  1135. Connecting Cities With Minimum Cost
 * There are N cities numbered from 1 to N.
 *
 * You are given connections, where each connections[i] = [city1, city2, cost] represents the cost to connect city1 and city2 together.  (A connection is bidirectional: connecting city1 and city2 is the same as connecting city2 and city1.)
 *
 * Return the minimum cost so that for every pair of cities, there exists a path of connections (possibly of length 1) that connects those two cities together.  The cost is the sum of the connection costs used. If the task is impossible, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: N = 3, connections = [[1,2,5],[1,3,6],[2,3,1]]
 * Output: 6
 * Explanation:
 * Choosing any 2 edges will connect all cities so we choose the minimum 2.
 * Example 2:
 *
 *
 *
 * Input: N = 4, connections = [[1,2,3],[3,4,4]]
 * Output: -1
 * Explanation:
 * There is no way to connect all cities even if all edges are used.
 *
 *
 * Note:
 *
 * 1 <= N <= 10000
 * 1 <= connections.length <= 10000
 * 1 <= connections[i][0], connections[i][1] <= N
 * 0 <= connections[i][2] <= 10^5
 * connections[i][0] != connections[i][1]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Uber
 * Problem Solution
 * 1135-Connecting-Cities-With-Minimum-Cost
 *
 *
 */
public class ConnectingCitiesWithMinimumCost {


    // V0
//    public int minimumCost(int n, int[][] connections) {
//
//    }


    // V1



    // V2
    // https://leetcode.ca/2019-01-08-1135-Connecting-Cities-With-Minimum-Cost/
    private int[] p;

    public int minimumCost_2(int n, int[][] connections) {
        Arrays.sort(connections, Comparator.comparingInt(a -> a[2]));
        p = new int[n];
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        int ans = 0;
        for (int[] e : connections) {
            int x = e[0] - 1, y = e[1] - 1, cost = e[2];
            if (find(x) == find(y)) {
                continue;
            }
            p[find(x)] = find(y);
            ans += cost;
            if (--n == 1) {
                return ans;
            }
        }
        return -1;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }



    // V3



}
