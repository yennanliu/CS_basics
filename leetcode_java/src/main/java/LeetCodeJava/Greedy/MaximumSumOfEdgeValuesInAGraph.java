package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-sum-of-edge-values-in-a-graph/description/

import java.util.*;

/**
 * 3547. Maximum Sum of Edge Values in a Graph
 * Hard
 *
 * You are given an undirected connected graph of n nodes, numbered from 0 to n -
 * 1. Each node is connected to at most 2 other nodes.
 *
 * The graph consists of m edges, represented by a 2D array edges, where edges[i] =
 * [a_i, b_i] indicates that there is an edge between nodes a_i and b_i.
 *
 * You have to assign a unique value from 1 to n to each node. The value of an edge
 * will be the product of the values assigned to the two nodes it connects.
 *
 * Your score is the sum of the values of all edges in the graph.
 *
 * Return the maximum score you can achieve.
 *
 * Example 1:
 *
 * Input: n = 4, edges = [[0,1],[1,2],[2,3]]
 *
 * Output: 23
 *
 * Explanation:
 *
 * The diagram above illustrates an optimal assignment of values to nodes. The sum
 * of the values of the edges is: (1 * 3) + (3 * 4) + (4 * 2) = 23.
 *
 * Example 2:
 *
 * Input: n = 6, edges = [[0,3],[4,5],[2,0],[1,3],[2,4],[1,5]]
 *
 * Output: 82
 *
 * Explanation:
 *
 * The diagram above illustrates an optimal assignment of values to nodes. The sum
 * of the values of the edges is: (1 * 2) + (2 * 4) + (4 * 6) + (6 * 5) + (5 * 3) +
 * (3 * 1) = 82.
 *
 * Constraints:
 *
 * 1 <= n <= 5 * 10^4
 *
 * m == edges.length
 *
 * 1 <= m <= n
 *
 * edges[i].length == 2
 *
 * 0 <= a_i, b_i < n
 *
 * a_i != b_i
 *
 * There are no repeated edges.
 *
 * The graph is connected.
 *
 * Each node is connected to at most 2 other nodes.
 *
 */
public class MaximumSumOfEdgeValuesInAGraph {

    // V0
    // IDEA: THE GRAPH IS ONE PATH OR ONE CYCLE -- ZIGZAG THE LABELS AROUND n (greedy)
    //  - every degree <= 2 and connected: n - 1 edges = a path, n edges = a cycle
    //  - large labels must be adjacent, so place n in the middle and keep hanging the next
    //    largest value on alternating ends: ... 5, 3, 1 | 2, 4, 6 ... with n, n-1 in the centre
    //  - olderEnd / newerEnd are the two current ends; c is glued to the older one
    //  - a cycle adds one extra edge joining the two outermost labels
    /**
     * time = O(n)
     * space = O(1)
     */
    public long maxScore(int n, int[][] edges) {
        boolean isCycle = edges.length == n;

        long olderEnd = n;
        long newerEnd = n;
        long total = 0;
        for (long label = n - 1; label >= 1; label--) {
            // attach the next largest label to the end that has waited longer
            total += olderEnd * label;
            olderEnd = newerEnd;
            newerEnd = label;
        }
        if (isCycle) {
            // close the cycle between the two outermost labels
            total += olderEnd * newerEnd;
        }
        return total;
    }
}
