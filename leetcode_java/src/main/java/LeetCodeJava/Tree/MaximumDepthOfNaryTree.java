package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-depth-of-n-ary-tree/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * 559. Maximum Depth of N-ary Tree
 * Easy
 *
 * Given a n-ary tree, find its maximum depth.
 *
 * The maximum depth is the number of nodes along the longest path from the root node
 * down to the farthest leaf node.
 *
 * Nary-Tree input serialization is represented in their level order traversal,
 * each group of children is separated by the null value (See examples).
 *
 * Example 1:
 *
 * Input: root = [1,null,3,2,4,null,5,6]
 * Output: 3
 *
 * Example 2:
 *
 * Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,
 *                null,13,null,null,14]
 * Output: 5
 *
 *
 * Constraints:
 *
 * The total number of nodes is in the range [0, 10^4].
 * The depth of the n-ary tree is less than or equal to 1000.
 *
 */
public class MaximumDepthOfNaryTree {

    // Definition for a Node (offered by LC platform)
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    // V0
    // IDEA: DFS (recursion)
    /**
     *  depth(node) = 1 + max(depth(child) for child in children), 0 for a null node
     *
     *  time  = O(n)
     *  space = O(h)  // h = tree height (recursion stack)
     */
    public int maxDepth(Node root) {
        if (root == null) {
            return 0;
        }

        // a LEAF (no children) has depth 1
        if (root.children == null || root.children.isEmpty()) {
            return 1;
        }

        int best = 0;
        for (Node child : root.children) {
            best = Math.max(best, maxDepth(child));
        }
        return 1 + best;
    }

    // V0-1
    // IDEA: BFS (level order), avoids deep recursion
    /**
     *  the tree can be 1000 deep, so the iterative version is the safer one
     *  when the recursion stack is a concern
     *
     *  time  = O(n)
     *  space = O(w)  // w = max width of the tree
     */
    public int maxDepth_0_1(Node root) {
        if (root == null) {
            return 0;
        }

        int depth = 0;
        Deque<Node> q = new ArrayDeque<>();
        q.offer(root);

        while (!q.isEmpty()) {
            depth += 1;
            // drain exactly ONE level per outer iteration
            int levelSize = q.size();
            for (int i = 0; i < levelSize; i++) {
                Node node = q.poll();
                if (node.children != null) {
                    for (Node child : node.children) {
                        q.offer(child);
                    }
                }
            }
        }

        return depth;
    }

}
