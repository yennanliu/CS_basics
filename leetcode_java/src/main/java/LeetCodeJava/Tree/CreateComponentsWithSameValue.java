package LeetCodeJava.Tree;

// https://leetcode.com/problems/create-components-with-same-value/description/

import java.util.*;

/**
 * 2440. Create Components With Same Value
 * Hard
 *
 * There is an undirected tree with n nodes labeled from 0 to n - 1.
 *
 * You are given a 0-indexed integer array nums of length n where nums[i] represents the value of the ith node. You are also given a 2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.
 *
 * You are allowed to delete some edges, splitting the tree into multiple connected components. Let the value of a component be the sum of all nums[i] for which node i is in the component.
 *
 * Return the maximum number of edges you can delete, such that every connected component in the tree has the same value.
 *
 *
 * Example 1:
 *
 * Input: nums = [6,2,2,2,6], edges = [[0,1],[1,2],[1,3],[3,4]]
 * Output: 2
 * Explanation: The above figure shows how we can delete the edges [0,1] and [3,4]. The created components are nodes [0], [1,2,3] and [4]. The sum of the values in each component equals 6. It can be proven that no better deletion exists, so the answer is 2.
 *
 * Example 2:
 *
 * Input: nums = [2], edges = []
 * Output: 0
 * Explanation: There are no edges to be deleted.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 10^4
 * nums.length == n
 * 1 <= nums[i] <= 50
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= edges[i][0], edges[i][1] <= n - 1
 * edges represents a valid tree.
 *
 */
public class CreateComponentsWithSameValue {

    // V0
    // IDEA: ENUMERATE DIVISORS OF THE TOTAL + GREEDY BOTTOM-UP PACKING
    //       k components of value t means k * t = total, so t divides total and t >= max(nums).
    //       For a candidate t, post-order the tree accumulating subtree sums:
    //       sum == t closes a component (send 0 up), sum > t fails, sum < t flows to the parent.
    //       Try the largest k first; the answer is k - 1 deleted edges.
    //       Traversal is iterative because the tree can be a 2 * 10^4 long chain.
    /**
     * time = O(N * d(S)), d(S) = number of divisors of the total
     * space = O(N)
     */
    public int componentValue(int[] nums, int[][] edges) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }

        // adjacency list: node = tree vertex, edge = undirected tree edge
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // iterative DFS from 0: parent of each node + a pre-order
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int orderSize = 0;
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        visited[0] = true;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            order[orderSize++] = node;
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    parent[next] = node;
                    stack.push(next);
                }
            }
        }

        int total = 0;
        int maxValue = 0;
        for (int num : nums) {
            total += num;
            maxValue = Math.max(maxValue, num);
        }

        // more components = more deleted edges, so try the largest count first
        for (int k = Math.min(n, total / maxValue); k > 1; k--) {
            if (total % k == 0 && canSplit(nums, parent, order, total / k)) {
                return k - 1;
            }
        }
        return 0;
    }

    /** True if the tree splits into components each summing to exactly target. */
    private boolean canSplit(int[] nums, int[] parent, int[] order, int target) {
        int[] accumulated = nums.clone();
        // reverse pre-order visits children before their parents
        for (int i = order.length - 1; i >= 0; i--) {
            int node = order[i];
            if (accumulated[node] > target) {
                return false;
            }
            if (accumulated[node] == target) {
                // this component closes here; nothing flows upward
                accumulated[node] = 0;
            } else if (node != 0) {
                // the partial component must extend through the parent edge
                accumulated[parent[node]] += accumulated[node];
            } else {
                // the root is left holding a partial component
                return false;
            }
        }
        return true;
    }
}
