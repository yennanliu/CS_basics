package LeetCodeJava.Graph;

// https://leetcode.com/problems/min-cost-to-connect-all-points/

import java.util.*;

/**
 * 1584. Min Cost to Connect All Points
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array points representing integer coordinates of some points on a 2D-plane, where points[i] = [xi, yi].
 *
 * The cost of connecting two points [xi, yi] and [xj, yj] is the manhattan distance between them: |xi - xj| + |yi - yj|, where |val| denotes the absolute value of val.
 *
 * Return the minimum cost to make all points connected. All points are connected if there is exactly one simple path between any two points.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
 * Output: 20
 * Explanation:
 *
 * We can connect the points as shown above to get the minimum cost of 20.
 * Notice that there is a unique path between every pair of points.
 * Example 2:
 *
 * Input: points = [[3,12],[-2,5],[-4,1]]
 * Output: 18
 *
 *
 * Constraints:
 *
 * 1 <= points.length <= 1000
 * -106 <= xi, yi <= 106
 * All pairs (xi, yi) are distinct.
 *
 */
public class MinCostToConnectAllPoints {

    // V0
//    public int minCostConnectPoints(int[][] points) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/min-cost-to-connect-points
    // IDEA: Kruskal's Algorithm
    public class DSU {
        int[] Parent, Size;

        public DSU(int n) {
            Parent = new int[n + 1];
            Size = new int[n + 1];
            for (int i = 0; i <= n; i++) Parent[i] = i;
            Arrays.fill(Size, 1);
        }

        public int find(int node) {
            if (Parent[node] != node) {
                Parent[node] = find(Parent[node]);
            }
            return Parent[node];
        }

        public boolean union(int u, int v) {
            int pu = find(u), pv = find(v);
            if (pu == pv) return false;
            if (Size[pu] < Size[pv]) {
                int temp = pu;
                pu = pv;
                pv = temp;
            }
            Size[pu] += Size[pv];
            Parent[pv] = pu;
            return true;
        }
    }

    public int minCostConnectPoints_1_1(int[][] points) {
        int n = points.length;
        DSU dsu = new DSU(n);
        List<int[]> edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Math.abs(points[i][0] - points[j][0]) +
                        Math.abs(points[i][1] - points[j][1]);
                edges.add(new int[] {dist, i, j});
            }
        }

        edges.sort((a, b) -> Integer.compare(a[0], b[0]));
        int res = 0;

        for (int[] edge : edges) {
            if (dsu.union(edge[1], edge[2])) {
                res += edge[0];
            }
        }
        return res;
    }


    // V1-2
    // https://neetcode.io/problems/min-cost-to-connect-points
    // IDEA: Prim's Algorithm
    public int minCostConnectPoints_1_2(int[][] points) {
        int N = points.length;
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int x1 = points[i][0];
            int y1 = points[i][1];
            for (int j = i + 1; j < N; j++) {
                int x2 = points[j][0];
                int y2 = points[j][1];
                int dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                adj.computeIfAbsent(i, k -> new ArrayList<>()).add(new int[]{dist, j});
                adj.computeIfAbsent(j, k -> new ArrayList<>()).add(new int[]{dist, i});
            }
        }

        int res = 0;
        Set<Integer> visit = new HashSet<>();
        PriorityQueue<int[]> minH = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minH.offer(new int[]{0, 0});
        while (visit.size() < N) {
            int[] curr = minH.poll();
            int cost = curr[0];
            int i = curr[1];
            if (visit.contains(i)) {
                continue;
            }
            res += cost;
            visit.add(i);
            for (int[] nei : adj.getOrDefault(i, Collections.emptyList())) {
                int neiCost = nei[0];
                int neiIndex = nei[1];
                if (!visit.contains(neiIndex)) {
                    minH.offer(new int[]{neiCost, neiIndex});
                }
            }
        }
        return res;
    }


    // V2

}
