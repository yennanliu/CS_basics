package LeetCodeJava.Tree;

// https://leetcode.com/problems/complete-binary-tree-inserter/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  919. Complete Binary Tree Inserter
 *  Medium
 *
 *  A complete binary tree is a binary tree in which every level, except
 *  possibly the last, is completely filled, and all the nodes are as far left
 *  as possible.
 *
 *  Design an algorithm to insert a new node to a complete binary tree keeping
 *  it complete after the insertion. Implement the CBTInserter class:
 *
 *   - CBTInserter(TreeNode root) initializes the data structure with the root
 *     of the complete binary tree.
 *   - int insert(int val) inserts a TreeNode into the tree with value
 *     Node.val == val so that the tree remains complete, and returns the value
 *     of the parent of the inserted TreeNode.
 *   - TreeNode get_root() returns the root node of the tree.
 *
 *  Example:
 *
 *  Input: ["CBTInserter","insert","insert","get_root"]
 *         [[[1,2]],[3],[4],[]]
 *  Output: [null,1,2,[1,2,3,4]]
 *
 *  Constraints:
 *
 *  The number of nodes in the tree will be in the range [1, 1000].
 *  0 <= Node.val <= 5000
 *  root is a complete binary tree.
 *  0 <= val <= 5000
 *  At most 10^4 calls will be made to insert and get_root.
 */
public class CompleteBinaryTreeInserter {

    // BFS (level order) list of the nodes -> index i has children 2i+1 / 2i+2
    private final List<TreeNode> nodes = new ArrayList<>();

    // V0
    // IDEA: keep the tree flattened in level order. The next insertion slot is
    //       index n = nodes.size(), whose parent is at index (n - 1) / 2; it is
    //       a left child when n is odd, a right child when n is even.
    /**
     * time = O(n) for the constructor, O(1) for insert / get_root
     * space = O(n)
     */
    public CompleteBinaryTreeInserter(TreeNode root) {
        if (root == null) {
            return;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            this.nodes.add(cur);
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }
    }

    public int insert(int val) {
        TreeNode node = new TreeNode(val);
        int idx = this.nodes.size();          // index the new node will take
        TreeNode parent = this.nodes.get((idx - 1) / 2);
        if (idx % 2 == 1) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        this.nodes.add(node);
        return parent.val;
    }

    public TreeNode get_root() {
        return this.nodes.get(0);
    }
}
