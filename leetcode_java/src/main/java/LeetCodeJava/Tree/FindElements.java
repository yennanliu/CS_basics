package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-elements-in-a-contaminated-binary-tree/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1261. Find Elements in a Contaminated Binary Tree
 *  Medium
 *
 *  Given a binary tree with the following rules:
 *   - root.val == 0
 *   - For any treeNode:
 *       if treeNode.val has a value x and treeNode.left != null,
 *          then treeNode.left.val == 2 * x + 1
 *       if treeNode.val has a value x and treeNode.right != null,
 *          then treeNode.right.val == 2 * x + 2
 *
 *  Now the binary tree is contaminated, which means all treeNode.val have been
 *  changed to -1.
 *
 *  Implement the FindElements class:
 *   - FindElements(TreeNode root) Initializes the object with a contaminated
 *     binary tree and recovers it.
 *   - boolean find(int target) Returns true if the target value exists in the
 *     recovered binary tree.
 *
 *  Example 1:
 *    Input
 *      ["FindElements","find","find"]
 *      [[[-1,null,-1]],[1],[2]]
 *    Output
 *      [null,false,true]
 *
 *  Example 2:
 *    Input
 *      ["FindElements","find","find","find"]
 *      [[[-1,-1,-1,-1,-1]],[1],[3],[5]]
 *    Output
 *      [null,true,true,false]
 *
 *  Constraints:
 *    TreeNode.val == -1
 *    The height of the binary tree is less than or equal to 20
 *    The total number of nodes is between [1, 10^4]
 *    Total calls of find() is between [1, 10^4]
 *    0 <= target <= 10^6
 */
public class FindElements {

    // V0
    // IDEA: DFS RECOVER + HASH SET
    //       rebuild every value ONCE at construction time and cache them in a
    //       set, so each find() is O(1).
    //       NOTE: iterative DFS, so a 10^4 node tree can NOT blow the stack.
    private final Set<Integer> seen;

    /**
     * time = O(N)
     * space = O(N)
     */
    public FindElements(TreeNode root) {
        this.seen = new HashSet<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root != null) {
            root.val = 0;
            stack.push(root);
        }
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            this.seen.add(node.val);
            if (node.left != null) {
                node.left.val = 2 * node.val + 1;
                stack.push(node.left);
            }
            if (node.right != null) {
                node.right.val = 2 * node.val + 2;
                stack.push(node.right);
            }
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean find(int target) {
        return this.seen.contains(target);
    }

    // V1
    // IDEA: WALK DOWN FROM THE TARGET (no pre-computed set, O(1) init memory)
    //       from `target`, repeatedly step to its parent ((target - 1) / 2) to
    //       recover the root -> target move list, then REPLAY that path on the
    //       real tree. an odd value came from a LEFT child, an even one from a
    //       RIGHT child.
    public static class FindElements2 {

        private final TreeNode root;

        /**
         * time = O(1)
         * space = O(1)
         */
        public FindElements2(TreeNode root) {
            this.root = root;
            if (this.root != null) {
                this.root.val = 0;
            }
        }

        /**
         * time = O(log(target))
         * space = O(log(target))
         */
        public boolean find(int target) {
            if (this.root == null) {
                return false;
            }
            Deque<Integer> path = new ArrayDeque<>();
            while (target > 0) {
                path.push(target % 2 == 1 ? 1 : 2); // 1 = left child, 2 = right child
                target = (target - 1) / 2;
            }
            TreeNode node = this.root;
            while (!path.isEmpty()) {
                int step = path.pop();
                node = (step == 1) ? node.left : node.right;
                if (node == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
