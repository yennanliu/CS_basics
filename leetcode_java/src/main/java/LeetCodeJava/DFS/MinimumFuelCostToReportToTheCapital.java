package LeetCodeJava.DFS;

// https://leetcode.com/problems/minimum-fuel-cost-to-report-to-the-capital/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  2477. Minimum Fuel Cost to Report to the Capital
 *  Medium
 *
 *  There is a tree (i.e., a connected, undirected graph with no cycles)
 *  structure country network consisting of n cities numbered from 0 to n - 1
 *  and exactly n - 1 roads. The capital city is city 0. You are given a 2D
 *  integer array roads where roads[i] = [ai, bi] denotes that there exists a
 *  bidirectional road connecting cities ai and bi.
 *
 *  There is a meeting for the representatives of each city, in the capital.
 *  There is a car in each city; seats is the number of seats in each car. A
 *  representative can use the car in their city to travel or change the car and
 *  ride with another representative. The cost of traveling between two cities is
 *  one liter of fuel.
 *
 *  Return the minimum number of liters of fuel to reach the capital city.
 *
 *  Example 1:
 *    Input: roads = [[0,1],[0,2],[0,3]], seats = 5
 *    Output: 3
 *    Explanation: each of the 3 representatives drives directly to the capital.
 *
 *  Example 2:
 *    Input: roads = [[3,1],[3,2],[1,0],[0,4],[0,5],[4,6]], seats = 2
 *    Output: 7
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    roads.length == n - 1
 *    roads[i].length == 2
 *    0 <= ai, bi < n
 *    ai != bi
 *    roads represents a valid tree.
 *    1 <= seats <= 10^5
 */
public class MinimumFuelCostToReportToTheCapital {

    // V0
    // IDEA: GREEDY ON SUBTREE SIZES (every edge is crossed ceil(sz / seats) times)
    //       all cars only ever drive towards the capital (node 0). let sz[v] be
    //       the number of representatives in v's subtree: they all have to cross
    //       the single edge (v -> parent(v)), and packing them as tightly as
    //       possible costs ceil(sz[v] / seats) liters on that edge.
    //       summing that over every non-root node is the answer.
    //       the traversal is ITERATIVE (n up to 10^5, the tree may be a chain).
    /**
     * time = O(N)
     * space = O(N)
     */
    public long minimumFuelCost(int[][] roads, int seats) {
        int n = roads.length + 1;
        if (n == 1) {
            return 0;
        }

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] r : roads) {
            adj.get(r[0]).add(r[1]);
            adj.get(r[1]).add(r[0]);
        }

        // BFS from the capital -> parent array + "parents before children" order
        int[] parent = new int[n];
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int head = 0, tail = 0;
        order[tail++] = 0;
        seen[0] = true;
        while (head < tail) {
            int u = order[head++];
            for (int v : adj.get(u)) {
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    order[tail++] = v;
                }
            }
        }

        long[] size = new long[n];
        Arrays.fill(size, 1L);

        long res = 0;
        // reverse BFS order -> a node is finished before its parent is used
        for (int idx = n - 1; idx >= 1; idx--) {
            int v = order[idx];
            size[parent[v]] += size[v];
            // ceil(size[v] / seats)
            res += (size[v] + seats - 1) / seats;
        }

        return res;
    }
}
