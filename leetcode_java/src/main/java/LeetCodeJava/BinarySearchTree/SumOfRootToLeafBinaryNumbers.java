package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/

import java.util.ArrayDeque;
import java.util.Deque;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1022. Sum of Root To Leaf Binary Numbers
 *  Easy
 *
 *  You are given the root of a binary tree where each node has a value 0 or 1.
 *  Each root-to-leaf path represents a binary number starting with the most
 *  significant bit.
 *
 *  For example, if the path is 0 -> 1 -> 1 -> 0 -> 1, then this could represent
 *  01101 in binary, which is 13.
 *
 *  For all leaves in the tree, consider the numbers represented by the path from
 *  the root to that leaf. Return the sum of these numbers.
 *
 *  Example 1:
 *   Input: root = [1,0,1,0,1,0,1]
 *   Output: 22   ((100) + (101) + (110) + (111) = 4 + 5 + 6 + 7)
 *
 *  Example 2:
 *   Input: root = [0]
 *   Output: 0
 *
 *  Constraints:
 *   The number of nodes in the tree is in the range [1, 1000].
 *   Node.val is 0 or 1.
 */
public class SumOfRootToLeafBinaryNumbers {

    // V0
    // IDEA: DFS carrying the binary number built so far (cur = cur * 2 + val);
    //       add it to the answer when a leaf is reached.
    /**
     * time = O(n)
     * space = O(h)   // h = tree height (recursion stack)
     */
    public int sumRootToLeaf(TreeNode root) {
        return dfs(root, 0);
    }

    private int dfs(TreeNode node, int cur) {
        if (node == null) {
            return 0;
        }
        cur = (cur << 1) | node.val;
        if (node.left == null && node.right == null) {
            return cur;
        }
        return dfs(node.left, cur) + dfs(node.right, cur);
    }

    // V1
    // IDEA: same DFS, but iterative -- an explicit stack holds (node, partial
    //       number) pairs, so there is no recursion depth limit on skewed trees.
    /**
     * time = O(n)
     * space = O(h)
     */
    public int sumRootToLeaf_1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Deque<TreeNode> nodes = new ArrayDeque<>();
        Deque<Integer> partials = new ArrayDeque<>();
        nodes.push(root);
        partials.push(root.val);

        int res = 0;
        while (!nodes.isEmpty()) {
            TreeNode node = nodes.pop();
            int cur = partials.pop();

            if (node.left == null && node.right == null) {
                res += cur;
                continue;
            }
            if (node.left != null) {
                nodes.push(node.left);
                partials.push((cur << 1) | node.left.val);
            }
            if (node.right != null) {
                nodes.push(node.right);
                partials.push((cur << 1) | node.right.val);
            }
        }
        return res;
    }

    // V2
    // IDEA: Morris pre-order traversal -- temporarily thread each node's
    //       in-order predecessor's right pointer back to it, so the whole tree is
    //       walked with NO stack and NO recursion (O(1) extra space). The number
    //       of steps to the predecessor tells us how many bits to unwind.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int sumRootToLeaf_2(TreeNode root) {
        int res = 0;
        int cur = 0;
        TreeNode node = root;

        while (node != null) {
            if (node.left != null) {
                // find the in-order predecessor of `node` inside its left subtree
                TreeNode pred = node.left;
                int steps = 1;
                while (pred.right != null && pred.right != node) {
                    pred = pred.right;
                    steps++;
                }

                if (pred.right == null) {
                    // first visit -> take the bit and thread the link
                    cur = (cur << 1) | node.val;
                    pred.right = node;
                    node = node.left;
                } else {
                    // second visit -> pred is the last node of the left subtree
                    if (pred.left == null) {
                        res += cur;              // pred is a leaf
                    }
                    // NOTE !!! unwind the bits contributed by the left subtree path
                    cur >>= steps;
                    pred.right = null;           // restore the tree
                    node = node.right;
                }
            } else {
                cur = (cur << 1) | node.val;
                if (node.right == null) {
                    res += cur;                  // node is a leaf
                }
                node = node.right;
            }
        }
        return res;
    }
}
