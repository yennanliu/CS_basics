package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-closest-node-to-given-two-nodes/description/

import java.util.*;

/**
 * 2359. Find Closest Node to Given Two Nodes
 * Medium
 *
 * You are given a directed graph of n nodes numbered from 0 to n - 1, where each node has at most one outgoing edge.
 *
 * The graph is represented with a given 0-indexed array edges of size n, indicating that there is a directed edge from node i to node edges[i]. If there is no outgoing edge from i, then edges[i] == -1.
 *
 * You are also given two integers node1 and node2.
 *
 * Return the index of the node that can be reached from both node1 and node2, such that the maximum between the distance from node1 to that node, and from node2 to that node is minimized. If there are multiple answers, return the node with the smallest index, and if no possible answer exists, return -1.
 *
 * Note that edges may contain cycles.
 *
 *
 * Example 1:
 *
 * Input: edges = [2,2,3,-1], node1 = 0, node2 = 1
 * Output: 2
 * Explanation: The distance from node 0 to node 2 is 1, and the distance from node 1 to node 2 is 1.
 * The maximum of those two distances is 1. It can be proven that we cannot get a node with a smaller maximum distance than 1, so we return node 2.
 *
 * Example 2:
 *
 * Input: edges = [1,2,-1], node1 = 0, node2 = 2
 * Output: 2
 * Explanation: The distance from node 0 to node 2 is 2, and the distance from node 2 to itself is 0.
 * The maximum of those two distances is 2. It can be proven that we cannot get a node with a smaller maximum distance than 2, so we return node 2.
 *
 *
 * Constraints:
 *
 * n == edges.length
 * 2 <= n <= 10^5
 * -1 <= edges[i] < n
 * edges[i] != i
 * 0 <= node1, node2 < n
 *
 */
public class FindClosestNodeToGivenTwoNodes {

    // V0
    // IDEA: FUNCTIONAL GRAPH WALK (out-degree <= 1, so "BFS" is just following edges[])
    //  - walk from node1 and from node2, recording the distance to every node reached
    //  - scan nodes left to right and keep the one minimising max(dist1, dist2);
    //    scanning in index order breaks ties by the smallest index for free
    /**
     * time = O(n)
     * space = O(n)
     */
    public int closestMeetingNode(int[] edges, int node1, int node2) {
        int n = edges.length;
        int[] dist1 = walk(edges, node1);
        int[] dist2 = walk(edges, node2);

        int answer = -1;
        int bestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            // node must be reachable from both
            if (dist1[i] == -1 || dist2[i] == -1) {
                continue;
            }
            int farther = Math.max(dist1[i], dist2[i]);
            if (farther < bestDistance) {
                bestDistance = farther;
                answer = i;
            }
        }
        return answer;
    }

    /** Follows the single outgoing edge from start until it falls off or revisits; -1 = unreached. */
    private int[] walk(int[] edges, int start) {
        int[] dist = new int[edges.length];
        Arrays.fill(dist, -1);
        int steps = 0;
        int current = start;
        // stop at a dead end (-1) or when the chain enters a cycle already walked
        while (current != -1 && dist[current] == -1) {
            dist[current] = steps;
            steps++;
            current = edges[current];
        }
        return dist;
    }
}
