package LeetCodeJava.Tree;

// https://leetcode.com/problems/count-good-nodes-in-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */

public class CountGoodNodesInBinaryTree {

    // V0
    // IDEA : DFS + maintain min value
    private int numGoodNodes = 0;

    public int goodNodes(TreeNode root) {
        //dfs(root, Integer.MIN_VALUE);
        findGoodNode(root, root.val); // can use root.val as well
        return numGoodNodes;
    }

    private void findGoodNode(TreeNode node, int maxSoFar) {

        if (maxSoFar <= node.val) {
            numGoodNodes += 1; // found a good node
        }

        if (node.right != null) {
            /** NOTE !!!
             *
             *  maintian the "max so far" value,
             *  so instead of comparing all nodes in path
             *  -> ONLY need to compare current node val with maxSoFar
             */
            findGoodNode(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            /** NOTE !!!
             *
             *  maintian the "max so far" value,
             *  so instead of comparing all nodes in path
             *  -> ONLY need to compare current node val with maxSoFar
             */
            findGoodNode(node.left, Math.max(node.val, maxSoFar));
        }
    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes_1 = 0;

    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes_1;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes_1++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V2
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_3(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }

    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair2 {
        public TreeNode node;
        public int maxSoFar;

        public Pair2(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_4(TreeNode root) {
        int numGoodNodes = 0;
        Queue<Pair> queue = new LinkedList<>();
        queue.offer(new Pair(root, Integer.MIN_VALUE));

        while (queue.size() > 0) {
            Pair curr = queue.poll();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.right != null) {
                queue.offer(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.left != null) {
                queue.offer(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }

}
