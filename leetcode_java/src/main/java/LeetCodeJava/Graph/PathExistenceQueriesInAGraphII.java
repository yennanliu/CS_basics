package LeetCodeJava.Graph;

// https://leetcode.com/problems/path-existence-queries-in-a-graph-ii/description/

import java.util.*;

/**
 * 3534. Path Existence Queries in a Graph II
 * Hard
 *
 * You are given an integer n representing the number of nodes in a graph, labeled
 * from 0 to n - 1.
 *
 * You are also given an integer array nums of length n and an integer maxDiff.
 *
 * An undirected edge exists between nodes i and j if the absolute difference
 * between nums[i] and nums[j] is at most maxDiff (i.e., |nums[i] - nums[j]| <=
 * maxDiff).
 *
 * You are also given a 2D integer array queries. For each queries[i] = [u_i, v_i],
 * find the minimum distance between nodes u_i and v_i_. If no path exists between
 * the two nodes, return -1 for that query.
 *
 * Return an array answer, where answer[i] is the result of the i^th query.
 *
 * Note: The edges between the nodes are unweighted.
 *
 * Example 1:
 *
 * Input: n = 5, nums = [1,8,3,4,2], maxDiff = 3, queries = [[0,3],[2,4]]
 *
 * Output: [1,1]
 *
 * Explanation:
 *
 * The resulting graph is:
 *
 * Query  | Shortest Path | Minimum Distance
 * -------+---------------+-----------------
 * [0, 3] | 0 -> 3        | 1
 * [2, 4] | 2 -> 4        | 1
 *
 * Thus, the output is [1, 1].
 *
 * Example 2:
 *
 * Input: n = 5, nums = [5,3,1,9,10], maxDiff = 2, queries =
 * [[0,1],[0,2],[2,3],[4,3]]
 *
 * Output: [1,2,-1,1]
 *
 * Explanation:
 *
 * The resulting graph is:
 *
 * Query  | Shortest Path | Minimum Distance
 * -------+---------------+-----------------
 * [0, 1] | 0 -> 1        | 1
 * [0, 2] | 0 -> 1 -> 2   | 2
 * [2, 3] | None          | -1
 * [4, 3] | 3 -> 4        | 1
 *
 * Thus, the output is [1, 2, -1, 1].
 *
 * Example 3:
 *
 * Input: n = 3, nums = [3,6,1], maxDiff = 1, queries = [[0,0],[0,1],[1,2]]
 *
 * Output: [0,-1,-1]
 *
 * Explanation:
 *
 * There are no edges between any two nodes because:
 *
 * Nodes 0 and 1: |nums[0] - nums[1]| = |3 - 6| = 3 > 1
 *
 * Nodes 0 and 2: |nums[0] - nums[2]| = |3 - 1| = 2 > 1
 *
 * Nodes 1 and 2: |nums[1] - nums[2]| = |6 - 1| = 5 > 1
 *
 * Thus, no node can reach any other node, and the output is [0, -1, -1].
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 *
 * 0 <= nums[i] <= 10^5
 *
 * 0 <= maxDiff <= 10^5
 *
 * 1 <= queries.length <= 10^5
 *
 * queries[i] == [u_i, v_i]
 *
 * 0 <= u_i, v_i < n
 *
 */
public class PathExistenceQueriesInAGraphII {

    // V0
    // IDEA: SORT BY VALUE -> INTERVAL GRAPH -> GREEDY JUMPS VIA BINARY LIFTING
    //  - in sorted order the neighbours of position p are a contiguous block, and the shortest
    //    path a -> b (a < b) is the jump-game greedy: always hop to far[p], the furthest
    //    position still within maxDiff
    //  - jump[j][p] = position after 2^j greedy hops; count hops by taking the largest jumps
    //    that still land before b, then one more hop reaches b
    //  - two positions are connected iff no gap > maxDiff lies between them (component id)
    /**
     * time = O((n + q) * log n)
     * space = O(n * log n)
     */
    public int[] pathExistenceQueries(int n, int[] nums, int maxDiff, int[][] queries) {
        // order[p] = original node at sorted position p
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(nums[a], nums[b]));

        int[] values = new int[n];
        int[] position = new int[n];   // original node -> sorted position
        for (int p = 0; p < n; p++) {
            values[p] = nums[order[p]];
            position[order[p]] = p;
        }

        // component[p] = connected component of sorted position p
        int[] component = new int[n];
        for (int p = 1; p < n; p++) {
            component[p] = component[p - 1];
            if (values[p] - values[p - 1] > maxDiff) {
                component[p]++;
            }
        }

        // far[p] = furthest position reachable in one hop (two pointers, far is non-decreasing)
        int levels = Math.max(1, 32 - Integer.numberOfLeadingZeros(n));
        int[][] jump = new int[levels][n];
        int reach = 0;
        for (int p = 0; p < n; p++) {
            while (reach + 1 < n && values[reach + 1] - values[p] <= maxDiff) {
                reach++;
            }
            jump[0][p] = reach;
        }
        for (int j = 1; j < levels; j++) {
            for (int p = 0; p < n; p++) {
                jump[j][p] = jump[j - 1][jump[j - 1][p]];
            }
        }

        int[] answer = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int a = Math.min(position[queries[i][0]], position[queries[i][1]]);
            int b = Math.max(position[queries[i][0]], position[queries[i][1]]);
            if (a == b) {
                answer[i] = 0;
                continue;
            }
            if (component[a] != component[b]) {
                answer[i] = -1;
                continue;
            }
            // take every jump that still stops short of b
            int steps = 0;
            int current = a;
            for (int j = levels - 1; j >= 0; j--) {
                if (jump[j][current] < b) {
                    current = jump[j][current];
                    steps += (1 << j);
                }
            }
            // one final hop lands on (or past) b
            answer[i] = steps + 1;
        }
        return answer;
    }
}
