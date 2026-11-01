package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-palindromic-path-in-graph/description/

import java.util.*;

/**
 * 3615. Longest Palindromic Path in Graph
 * Hard
 *
 * You are given an integer n and an undirected graph with n nodes labeled from 0
 * to n - 1 and a 2D array edges, where edges[i] = [ui, vi] indicates an edge
 * between nodes ui and vi.
 *
 * You are also given a string label of length n, where label[i] is the character
 * associated with node i.
 *
 * You may start at any node and move to any adjacent node, visiting each node at
 * most once.
 *
 * Return the maximum possible length of a palindrome that can be formed by
 * visiting a set of unique nodes along a valid path.
 *
 *
 * Example 1:
 *
 * Input: n = 3, edges = [[0,1],[1,2]], label = "aba"
 * Output: 3
 * Explanation:
 * The longest palindromic path is from node 0 to node 2 via node 1, following the
 * path 0 -> 1 -> 2 forming string "aba".
 * This is a valid palindrome of length 3.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1],[0,2]], label = "abc"
 * Output: 1
 * Explanation:
 * No path with more than one node forms a palindrome.
 * The best option is any single node, giving a palindrome of length 1.
 *
 * Example 3:
 *
 * Input: n = 4, edges = [[0,2],[0,3],[3,1]], label = "bbac"
 * Output: 3
 * Explanation:
 * The longest palindromic path is from node 0 to node 1, following the path
 * 0 -> 3 -> 1, forming string "bcb".
 * This is a valid palindrome of length 3.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 14
 * n - 1 <= edges.length <= n * (n - 1) / 2
 * edges[i] == [ui, vi]
 * 0 <= ui, vi <= n - 1
 * ui != vi
 * label.length == n
 * label consists of lowercase English letters.
 * There are no duplicate edges.
 *
 */
public class LongestPalindromicPathInGraph {

    // V0
    // IDEA: BITMASK DP, GROWING THE PALINDROME FROM ITS CENTRE OUTWARD
    //   a palindromic path pairs its first node with its last, its second with its
    //   second-to-last, ... so grow it from the middle: the state is (u, v, mask),
    //   u and v the two current END nodes, mask the nodes already on the path.
    //   start: a single node (odd length) or an edge u - v with equal labels (even length)
    //   step: extend both ends at once to unused neighbours u' of u and v' of v,
    //         u' != v', label[u'] == label[v'] -> (u', v', mask + u' + v')
    //   every reachable state is a palindromic simple path of length popcount(mask).
    //   Steps only add bits, so visiting masks in increasing order is a valid DP order.
    /**
     * time = O(2^N * N^4)
     * space = O(2^N * N^2)
     */
    public int maxLen(int n, int[][] edges, String label) {
        boolean[][] adjacent = new boolean[n][n];
        for (int[] edge : edges) {
            adjacent[edge[0]][edge[1]] = true;
            adjacent[edge[1]][edge[0]] = true;
        }

        int totalStates = 1 << n;
        // reachable[mask][u][v] = a palindromic path over exactly mask ends at u and v
        boolean[][][] reachable = new boolean[totalStates][][];

        for (int u = 0; u < n; u++) {
            // a lone node is a palindrome of length 1
            markReachable(reachable, 1 << u, u, u, n);
            for (int v = u + 1; v < n; v++) {
                // two adjacent equal letters are a palindrome of length 2
                if (adjacent[u][v] && label.charAt(u) == label.charAt(v)) {
                    markReachable(reachable, (1 << u) | (1 << v), u, v, n);
                }
            }
        }

        int best = 1;
        for (int mask = 1; mask < totalStates; mask++) {
            if (reachable[mask] == null) {
                continue;
            }
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    if (!reachable[mask][u][v]) {
                        continue;
                    }
                    best = Math.max(best, Integer.bitCount(mask));
                    // extend both ends by one node each
                    for (int nextU = 0; nextU < n; nextU++) {
                        if (!adjacent[u][nextU] || (mask & (1 << nextU)) != 0) {
                            continue;
                        }
                        for (int nextV = 0; nextV < n; nextV++) {
                            if (nextV == nextU || !adjacent[v][nextV] || (mask & (1 << nextV)) != 0) {
                                continue;
                            }
                            if (label.charAt(nextU) != label.charAt(nextV)) {
                                continue;
                            }
                            int nextMask = mask | (1 << nextU) | (1 << nextV);
                            markReachable(reachable, nextMask, nextU, nextV, n);
                        }
                    }
                }
            }
        }
        return best;
    }

    /** Records state (u, v, mask), allocating the mask's table on first use. */
    private void markReachable(boolean[][][] reachable, int mask, int u, int v, int n) {
        if (reachable[mask] == null) {
            reachable[mask] = new boolean[n][n];
        }
        reachable[mask][u][v] = true;
    }
}
