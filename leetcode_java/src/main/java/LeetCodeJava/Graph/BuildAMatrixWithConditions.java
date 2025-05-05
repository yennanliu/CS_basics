package LeetCodeJava.Graph;

// https://leetcode.com/problems/build-a-matrix-with-conditions/description/

import java.util.*;

/**
 * 2392. Build a Matrix With Conditions
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given a positive integer k. You are also given:
 *
 * a 2D integer array rowConditions of size n where rowConditions[i] = [abovei, belowi], and
 * a 2D integer array colConditions of size m where colConditions[i] = [lefti, righti].
 * The two arrays contain integers from 1 to k.
 *
 * You have to build a k x k matrix that contains each of the numbers from 1 to k exactly once. The remaining cells should have the value 0.
 *
 * The matrix should also satisfy the following conditions:
 *
 * The number abovei should appear in a row that is strictly above the row at which the number belowi appears for all i from 0 to n - 1.
 * The number lefti should appear in a column that is strictly left of the column at which the number righti appears for all i from 0 to m - 1.
 * Return any matrix that satisfies the conditions. If no answer exists, return an empty matrix.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: k = 3, rowConditions = [[1,2],[3,2]], colConditions = [[2,1],[3,2]]
 * Output: [[3,0,0],[0,0,1],[0,2,0]]
 * Explanation: The diagram above shows a valid example of a matrix that satisfies all the conditions.
 * The row conditions are the following:
 * - Number 1 is in row 1, and number 2 is in row 2, so 1 is above 2 in the matrix.
 * - Number 3 is in row 0, and number 2 is in row 2, so 3 is above 2 in the matrix.
 * The column conditions are the following:
 * - Number 2 is in column 1, and number 1 is in column 2, so 2 is left of 1 in the matrix.
 * - Number 3 is in column 0, and number 2 is in column 1, so 3 is left of 2 in the matrix.
 * Note that there may be multiple correct answers.
 * Example 2:
 *
 * Input: k = 3, rowConditions = [[1,2],[2,3],[3,1],[2,3]], colConditions = [[2,1]]
 * Output: []
 * Explanation: From the first two conditions, 3 has to be below 1 but the third conditions needs 3 to be above 1 to be satisfied.
 * No matrix can satisfy all the conditions, so we return the empty matrix.
 *
 *
 * Constraints:
 *
 * 2 <= k <= 400
 * 1 <= rowConditions.length, colConditions.length <= 104
 * rowConditions[i].length == colConditions[i].length == 2
 * 1 <= abovei, belowi, lefti, righti <= k
 * abovei != belowi
 * lefti != righti
 *
 *
 */
public class BuildAMatrixWithConditions {

    // V0
//    public int[][] buildMatrix(int k, int[][] rowConditions, int[][] colConditions) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=khTKB1PzCuw

    // V2-1
    // https://leetcode.com/problems/build-a-matrix-with-conditions/editorial/
    // IDEA:  DFS
    public int[][] buildMatrix_2_1(
            int k,
            int[][] rowConditions,
            int[][] colConditions) {
        // Store the topologically sorted sequences.
        List<Integer> orderRows = topoSort(rowConditions, k);
        List<Integer> orderColumns = topoSort(colConditions, k);

        // If no topological sort exists, return empty array.
        if (orderRows.isEmpty() || orderColumns.isEmpty())
            return new int[0][0];

        int[][] matrix = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (orderRows.get(i).equals(orderColumns.get(j))) {
                    matrix[i][j] = orderRows.get(i);
                }
            }
        }
        return matrix;
    }

    private List<Integer> topoSort(int[][] edges, int n) {
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        List<Integer> order = new ArrayList<>();
        // 0: not visited, 1: visiting, 2: visited
        int[] visited = new int[n + 1];
        boolean hasCycle = false;

        // Perform DFS for each node
        for (int i = 1; i <= n; i++) {
            if (visited[i] == 0) {
                hasCycle = dfs(i, adj, visited, order);
                // Return empty if cycle detected
                if (hasCycle)
                    return new ArrayList<>();
            }
        }

        // Reverse to get the correct order
        Collections.reverse(order);
        return order;
    }

    private boolean dfs(
            int node,
            List<List<Integer>> adj,
            int[] visited,
            List<Integer> order) {
        visited[node] = 1; // Mark node as visiting
        for (int neighbor : adj.get(node)) {
            if (visited[neighbor] == 0) {
                if (dfs(neighbor, adj, visited, order)) {
                    return true; // Cycle detected
                }
            } else if (visited[neighbor] == 1) {
                return true; // Cycle detected
            }
        }
        // Mark node as visited
        visited[node] = 2;
        // Add node to the order
        order.add(node);
        return false;
    }

    // V2-2
    // https://leetcode.com/problems/build-a-matrix-with-conditions/editorial/
    // IDEA: Kahn's Algorithm

    public int[][] buildMatrix_2_2(
            int k,
            int[][] rowConditions,
            int[][] colConditions) {
        int[] orderRows = topoSort_2_2(rowConditions, k);
        int[] orderColumns = topoSort_2_2(colConditions, k);
        if (orderRows.length == 0 || orderColumns.length == 0)
            return new int[0][0];
        int[][] matrix = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (orderRows[i] == orderColumns[j]) {
                    matrix[i][j] = orderRows[i];
                }
            }
        }
        return matrix;
    }

    private int[] topoSort_2_2(int[][] edges, int n) {
        List<Integer>[] adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        int[] deg = new int[n + 1], order = new int[n];
        int idx = 0;
        for (int[] x : edges) {
            adj[x[0]].add(x[1]);
            deg[x[1]]++;
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (deg[i] == 0)
                q.offer(i);
        }
        while (!q.isEmpty()) {
            int f = q.poll();
            order[idx++] = f;
            n--;
            for (int v : adj[f]) {
                if (--deg[v] == 0)
                    q.offer(v);
            }
        }
        if (n != 0)
            return new int[0];
        return order;
    }


}
