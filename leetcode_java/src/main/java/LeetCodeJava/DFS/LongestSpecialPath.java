package LeetCodeJava.DFS;

// https://leetcode.com/problems/longest-special-path/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 3425. Longest Special Path
 * Hard
 *
 * You are given an undirected tree rooted at node 0 with n nodes numbered from 0 to n - 1,
 * represented by a 2D array edges of length n - 1, where edges[i] = [u_i, v_i, length_i]
 * indicates an edge between nodes u_i and v_i with length length_i. You are also given an
 * integer array nums, where nums[i] represents the value at node i.
 *
 * A special path is defined as a downward path from an ancestor node to a descendant node such
 * that all the values of the nodes in that path are unique.
 *
 * Note that a path may start and end at the same node.
 *
 * Return an array result of size 2, where result[0] is the length of the longest special path,
 * and result[1] is the minimum number of nodes in all possible longest special paths.
 *
 *
 * Example 1:
 *
 * Input: edges = [[0,1,2],[1,2,3],[1,3,5],[1,4,4],[2,5,6]], nums = [2,1,2,1,3,1]
 * Output: [6,2]
 * Explanation:
 * The longest special paths are 2 -> 5 and 0 -> 1 -> 4, both having a length of 6.
 * The minimum number of nodes across all longest special paths is 2.
 *
 * Example 2:
 *
 * Input: edges = [[1,0,8]], nums = [2,2]
 * Output: [0,1]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 10^4
 * edges.length == n - 1
 * edges[i].length == 3
 * 0 <= u_i, v_i < n
 * 1 <= length_i <= 10^3
 * nums.length == n
 * 0 <= nums[i] <= 5 * 10^4
 * The input is generated such that edges represents a valid tree.
 *
 */
public class LongestSpecialPath {

    // V0
    // IDEA: DFS CARRYING A "NO REPEATED VALUE" SLIDING WINDOW ALONG THE ROOT PATH
    //  the root-to-node path plays the role of the string in "longest substring without
    //  repeating characters": entering a node of value v whose last occurrence on the path is at
    //  depth d0 moves the window's left edge (a depth) to max(left, d0 + 1). Edge lengths are
    //  >= 1, so the longest window ending at a node starts exactly at the left edge.
    //  On backtrack the left edge and the value's last depth are restored.
    //  Iterative DFS, because n = 5 * 10^4 can overflow the call stack.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] longestSpecialPath(int[][] edges, int[] nums) {
        int n = nums.length;

        // adjacency: node -> {neighbour, edge length}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            graph.get(edge[1]).add(new int[] {edge[0], edge[2]});
        }

        int maxValue = 0;
        for (int v : nums) {
            maxValue = Math.max(maxValue, v);
        }
        // lastDepth[v] = depth of the deepest node on the current path with value v, -1 if none
        int[] lastDepth = new int[maxValue + 1];
        Arrays.fill(lastDepth, -1);
        // rootDistance[d] = distance from the root to the path's node at depth d
        long[] rootDistance = new long[n + 1];

        long bestLength = 0;
        int bestNodes = 1;
        int left = 0;

        // frame = {node, parent, depth, next adjacency index, saved left, saved lastDepth, entered}
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, -1, 0, 0, 0, 0, 0});

        while (!stack.isEmpty()) {
            int[] frame = stack.peek();
            int node = frame[0];
            int depth = frame[2];
            int value = nums[node];

            if (frame[6] == 0) {
                // first visit: push node onto the path and slide the window's left edge
                frame[6] = 1;
                frame[4] = left;
                frame[5] = lastDepth[value];
                left = Math.max(left, lastDepth[value] + 1);
                lastDepth[value] = depth;

                long length = rootDistance[depth] - rootDistance[left];
                int nodes = depth - left + 1;
                if (length > bestLength) {
                    bestLength = length;
                    bestNodes = nodes;
                } else if (length == bestLength && nodes < bestNodes) {
                    bestNodes = nodes;
                }
            }

            // find the next child (skip the parent)
            List<int[]> neighbours = graph.get(node);
            int[] child = null;
            while (frame[3] < neighbours.size()) {
                int[] candidate = neighbours.get(frame[3]);
                frame[3]++;
                if (candidate[0] != frame[1]) {
                    child = candidate;
                    break;
                }
            }

            if (child != null) {
                rootDistance[depth + 1] = rootDistance[depth] + child[1];
                stack.push(new int[] {child[0], node, depth + 1, 0, 0, 0, 0});
            } else {
                // all children done: pop node off the path and restore the window
                lastDepth[value] = frame[5];
                left = frame[4];
                stack.pop();
            }
        }
        return new int[] {(int) bestLength, bestNodes};
    }
}
