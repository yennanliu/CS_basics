package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-nodes-with-the-highest-score/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  2049. Count Nodes With the Highest Score
 *  Medium
 *
 *  There is a binary tree rooted at 0 consisting of n nodes. The nodes are labeled
 *  from 0 to n - 1. You are given a 0-indexed integer array parents representing the
 *  tree, where parents[i] is the parent of node i. Since node 0 is the root,
 *  parents[0] == -1.
 *
 *  Each node has a score. To find the score of a node, consider if the node and the
 *  edges connected to it were removed. The tree would become one or more non-empty
 *  subtrees. The size of a subtree is the number of the nodes in it. The score of the
 *  node is the product of the sizes of all those subtrees.
 *
 *  Return the number of nodes that have the highest score.
 *
 *  Example 1:
 *    Input: parents = [-1,2,0,2,0]
 *    Output: 3
 *    (scores: node0 = 3*1 = 3, node1 = 4, node2 = 1*1*2 = 2, node3 = 4, node4 = 4)
 *
 *  Example 2:
 *    Input: parents = [-1,2,0]
 *    Output: 2
 *
 *  Constraints:
 *    n == parents.length
 *    2 <= n <= 10^5
 *    parents[0] == -1
 *    0 <= parents[i] <= n - 1 for i != 0
 *    parents represents a valid binary tree.
 */
public class CountNodesWithTheHighestScore {

    // V0
    // IDEA: build left/right child arrays, get every subtree size with an
    //       ITERATIVE post-order walk (n can be 1e5 -> avoid deep recursion),
    //       then score(i) = size(left) * size(right) * (n - size(i)).
    /**
     * time = O(n)
     * space = O(n)
     */
    public int countHighestScoreNodes(int[] parents) {

        int n = parents.length;

        // binary tree : at most 2 children per node
        int[] left = new int[n];
        int[] right = new int[n];
        for (int i = 0; i < n; i++) {
            left[i] = -1;
            right[i] = -1;
        }

        for (int i = 1; i < n; i++) {
            int p = parents[i];
            if (left[p] == -1) {
                left[p] = i;
            } else {
                right[p] = i;
            }
        }

        // iterative post-order : subtree size of every node
        int[] size = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        Deque<Integer> order = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            order.push(cur);
            if (left[cur] != -1) {
                stack.push(left[cur]);
            }
            if (right[cur] != -1) {
                stack.push(right[cur]);
            }
        }
        // `order` now pops children before their parent
        while (!order.isEmpty()) {
            int cur = order.pop();
            int s = 1;
            if (left[cur] != -1) {
                s += size[left[cur]];
            }
            if (right[cur] != -1) {
                s += size[right[cur]];
            }
            size[cur] = s;
        }

        long best = 0;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            long score = 1L;
            if (left[i] != -1) {
                score *= size[left[i]];
            }
            if (right[i] != -1) {
                score *= size[right[i]];
            }
            int rest = n - size[i]; // the "parent side" component
            if (rest > 0) {
                score *= rest;
            }

            if (score > best) {
                best = score;
                cnt = 1;
            } else if (score == best) {
                cnt++;
            }
        }

        return cnt;
    }
}
