package LeetCodeJava.DFS;

// https://leetcode.com/problems/longest-path-with-different-adjacent-characters/

import java.util.ArrayList;
import java.util.List;

/**
 *  2246. Longest Path With Different Adjacent Characters
 *  Hard
 *
 *  You are given a tree (i.e. a connected, undirected graph that has no cycles)
 *  rooted at node 0 consisting of n nodes numbered from 0 to n - 1. The tree is
 *  represented by a 0-indexed array parent of size n, where parent[i] is the
 *  parent of node i. Since node 0 is the root, parent[0] == -1.
 *
 *  You are also given a string s of length n, where s[i] is the character
 *  assigned to node i.
 *
 *  Return the length of the longest path in the tree such that no pair of
 *  adjacent nodes on the path have the same character assigned to them.
 *
 *  Example 1:
 *    Input: parent = [-1,0,0,1,1,2], s = "abacbe"
 *    Output: 3
 *    Explanation: The longest path where each two adjacent nodes have different
 *                 characters is the path 0 -> 1 -> 3, of length 3.
 *
 *  Example 2:
 *    Input: parent = [-1,0,0,0], s = "aabc"
 *    Output: 3
 *    Explanation: The path is 2 -> 0 -> 3.
 *
 *  Constraints:
 *    n == parent.length == s.length
 *    1 <= n <= 10^5
 *    0 <= parent[i] <= n - 1 for all i >= 1
 *    parent[0] == -1
 *    parent represents a valid tree.
 *    s consists of only lowercase English letters.
 */
public class LongestPathWithDifferentAdjacentCharacters {

    // V0
    // IDEA: TREE DP - AT EACH NODE, JOIN ITS TWO BEST DOWNWARD CHAINS
    //       down[u] = longest valid chain starting at u going strictly down.
    //       a child v may extend it only when s[v] != s[u], so
    //           down[u] = 1 + max(down[v] over usable children, or 0)
    //       the longest path THROUGH u bends at u and uses its two best usable
    //       children: 1 + best1 + best2. every path has a unique highest node,
    //       so the max of that over all u is the answer.
    //       n is up to 10^5 -> the traversal is ITERATIVE (reverse BFS order
    //       guarantees children are settled before their parent).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPath(int[] parent, String s) {
        int n = parent.length;

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            children.get(parent[i]).add(i);
        }

        // BFS order from the root -> parents always come before children
        int[] order = new int[n];
        int head = 0, tail = 0;
        order[tail++] = 0;
        while (head < tail) {
            int u = order[head++];
            for (int v : children.get(u)) {
                order[tail++] = v;
            }
        }

        int[] down = new int[n];
        int res = 1;

        // reverse BFS order -> children before parents
        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];
            int best1 = 0, best2 = 0;
            for (int v : children.get(u)) {
                if (s.charAt(v) == s.charAt(u)) {
                    continue;
                }
                int cand = down[v];
                if (cand > best1) {
                    best2 = best1;
                    best1 = cand;
                } else if (cand > best2) {
                    best2 = cand;
                }
            }
            down[u] = 1 + best1;
            res = Math.max(res, 1 + best1 + best2);
        }

        return res;
    }
}
