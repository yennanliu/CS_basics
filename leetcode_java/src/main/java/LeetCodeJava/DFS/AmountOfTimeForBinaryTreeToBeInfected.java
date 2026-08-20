package LeetCodeJava.DFS;

// https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2385. Amount of Time for Binary Tree to Be Infected
 *  Medium
 *
 *  You are given the root of a binary tree with unique values, and an integer start.
 *  At minute 0, an infection starts from the node with value start.
 *
 *  Each minute, a node becomes infected if:
 *    - The node is currently uninfected.
 *    - The node is adjacent to an infected node.
 *
 *  Return the number of minutes needed for the entire tree to be infected.
 *
 *  Example 1:
 *    Input: root = [1,5,3,null,4,10,6,9,2], start = 3
 *    Output: 4
 *    Explanation: minute 0 -> {3}, minute 1 -> {1,10,6}, minute 2 -> {5},
 *                 minute 3 -> {4}, minute 4 -> {9,2}
 *
 *  Example 2:
 *    Input: root = [1], start = 1
 *    Output: 0
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^5].
 *    1 <= Node.val <= 10^5
 *    Each node has a unique value.
 *    A node is adjacent to another node if it has a direct edge to it.
 *    start exists in the tree.
 */
public class AmountOfTimeForBinaryTreeToBeInfected {

    // V0
    // IDEA: THE INFECTION IGNORES THE PARENT/CHILD DIRECTION -> MAKE IT A GRAPH
    //       spreading to "adjacent" nodes walks UP to the parent as well as down to
    //       the children, so the tree is really an undirected graph here.
    //       one pass builds an adjacency map (val -> neighbour vals), then a plain
    //       BFS from `start`; the answer is the depth of the last BFS layer, i.e.
    //       the eccentricity of `start`.
    //       both passes are ITERATIVE - the tree can hold 10^5 nodes shaped as a path.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int amountOfTime(TreeNode root, int start) {

        if (root == null) {
            return 0;
        }

        // build undirected adjacency over node values
        Map<Integer, List<Integer>> adj = new HashMap<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode cur = stack.pop();
            if (!adj.containsKey(cur.val)) {
                adj.put(cur.val, new ArrayList<Integer>());
            }
            if (cur.left != null) {
                link(adj, cur.val, cur.left.val);
                stack.push(cur.left);
            }
            if (cur.right != null) {
                link(adj, cur.val, cur.right.val);
                stack.push(cur.right);
            }
        }

        // BFS from start, count the layers
        java.util.Set<Integer> visited = new java.util.HashSet<>();
        Deque<Integer> q = new ArrayDeque<>();
        q.add(start);
        visited.add(start);

        int minutes = -1;
        while (!q.isEmpty()) {
            int size = q.size();
            minutes++;
            for (int i = 0; i < size; i++) {
                int cur = q.poll();
                for (Integer nxt : adj.get(cur)) {
                    if (visited.add(nxt)) {
                        q.add(nxt);
                    }
                }
            }
        }

        return minutes;
    }

    private void link(Map<Integer, List<Integer>> adj, int a, int b) {
        if (!adj.containsKey(a)) {
            adj.put(a, new ArrayList<Integer>());
        }
        if (!adj.containsKey(b)) {
            adj.put(b, new ArrayList<Integer>());
        }
        adj.get(a).add(b);
        adj.get(b).add(a);
    }
}
