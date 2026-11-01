package LeetCodeJava.Graph;

// https://leetcode.com/problems/longest-cycle-in-a-graph/description/

import java.util.*;

/**
 * 2360. Longest Cycle in a Graph
 * Hard
 *
 * You are given a directed graph of n nodes numbered from 0 to n - 1, where each node has at most one outgoing edge.
 *
 * The graph is represented with a given 0-indexed array edges of size n, indicating that there is a directed edge from node i to node edges[i]. If there is no outgoing edge from node i, then edges[i] == -1.
 *
 * Return the length of the longest cycle in the graph. If no cycle exists, return -1.
 *
 * A cycle is a path that starts and ends at the same node.
 *
 *
 * Example 1:
 *
 * Input: edges = [3,3,4,2,3]
 * Output: 3
 * Explanation: The longest cycle in the graph is the cycle: 2 -> 4 -> 3 -> 2.
 * The length of this cycle is 3, so 3 is returned.
 *
 * Example 2:
 *
 * Input: edges = [2,-1,3,1]
 * Output: -1
 * Explanation: There are no cycles in this graph.
 *
 *
 * Constraints:
 *
 * n == edges.length
 * 2 <= n <= 10^5
 * -1 <= edges[i] < n
 * edges[i] != i
 *
 */
public class LongestCycleInAGraph {

    // V0
    // IDEA: FUNCTIONAL GRAPH + VISIT TIMESTAMPS (each node is walked once overall)
    //  - from any start the walk is a single chain; stamp each node with a global step counter
    //    and the id of the walk that reached it
    //  - re-entering a node of THIS walk closes a cycle of length step - stamp[node];
    //    re-entering a node of an EARLIER walk means the rest was already processed
    /**
     * time = O(n)
     * space = O(n)
     */
    public int longestCycle(int[] edges) {
        int n = edges.length;
        int[] owner = new int[n];   // which walk (start node) visited this node, -1 = none
        int[] stamp = new int[n];   // global step counter when the node was visited
        Arrays.fill(owner, -1);

        int longest = -1;
        int step = 0;
        for (int start = 0; start < n; start++) {
            if (owner[start] != -1) {
                continue;
            }
            int node = start;
            while (node != -1) {
                if (owner[node] != -1) {
                    // closed a cycle inside the current walk
                    if (owner[node] == start) {
                        longest = Math.max(longest, step - stamp[node]);
                    }
                    break;
                }
                owner[node] = start;
                stamp[node] = step;
                step++;
                node = edges[node];
            }
        }
        return longest;
    }
}
