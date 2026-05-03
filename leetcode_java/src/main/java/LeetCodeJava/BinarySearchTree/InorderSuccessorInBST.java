package LeetCodeJava.BinarySearchTree;

// https://leetcode.ca/all/285.html
// https://leetcode.com/problems/inorder-successor-in-bst/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *  285. Inorder Successor in BST
 * Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
 *
 * The successor of a node p is the node with the smallest key greater than p.val.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [2,1,3], p = 1
 * Output: 2
 * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
 * Example 2:
 *
 *
 * Input: root = [5,3,6,2,4,null,null,1], p = 6
 * Output: null
 * Explanation: There is no in-order successor of the current node, so the answer is null.
 *
 *
 * Note:
 *
 * If the given node has no in-order successor in the tree, return null.
 * It's guaranteed that the values of the tree are unique.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Microsoft Palantir Technologies Pocket Gems Quip (Salesforce) Zillow
 * Problem Solution
 * 285-Inorder-Successor-in-BST
 *
 */
public class InorderSuccessorInBST {

    // V0
//    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
//
//    }


    // V0-1
    // IDEA: BST property + DFS (eager traversal) (gpt)
    public TreeNode inorderSuccessor_0_1(TreeNode root, TreeNode p) {
        List<TreeNode> list = new ArrayList<>();
        dfs2(root, list);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == p) {
                return (i + 1 < list.size()) ? list.get(i + 1) : null;
            }
        }
        return null;
    }

    private void dfs2(TreeNode root, List<TreeNode> list){
        if (root == null) return;

        dfs2(root.left, list);
        list.add(root);
        dfs2(root.right, list);
    }


    // V0-2
    // IDEA: BST + lazy traversal (GPT)
    public TreeNode inorderSuccessor_0_2(TreeNode root, TreeNode p) {
        TreeNode successor = null;

        while (root != null) {
            if (p.val < root.val) {
                successor = root;      // potential answer
                root = root.left;
            } else {
                root = root.right;
            }
        }

        return successor;
    }


    // V1
    // https://leetcode.ca/2016-09-10-285-Inorder-Successor-in-BST/
    public TreeNode inorderSuccessor_1(TreeNode root, TreeNode p) {
        TreeNode ans = null;
        while (root != null) {
            if (root.val > p.val) {
                ans = root;
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return ans;
    }


    // V2



    // V3




}
