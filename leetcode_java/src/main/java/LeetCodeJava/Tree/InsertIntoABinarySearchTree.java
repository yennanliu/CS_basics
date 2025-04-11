package LeetCodeJava.Tree;

// https://leetcode.com/problems/insert-into-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 701. Insert into a Binary Search Tree
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given the root node of a binary search tree (BST) and a value to insert into the tree. Return the root node of the BST after the insertion. It is guaranteed that the new value does not exist in the original BST.
 *
 * Notice that there may exist multiple valid ways for the insertion, as long as the tree remains a BST after insertion. You can return any of them.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,7,1,3], val = 5
 * Output: [4,2,7,1,3,5]
 * Explanation: Another accepted tree is:
 *
 * Example 2:
 *
 * Input: root = [40,20,60,10,30,50,70], val = 25
 * Output: [40,20,60,10,30,50,70,null,null,25]
 * Example 3:
 *
 * Input: root = [4,2,7,1,3,null,null,null,null,null,null], val = 5
 * Output: [4,2,7,1,3,5]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree will be in the range [0, 104].
 * -108 <= Node.val <= 108
 * All the values Node.val are unique.
 * -108 <= val <= 108
 * It's guaranteed that val does not exist in the original BST.
 *
 */
public class InsertIntoABinarySearchTree {

    // V0
//    public TreeNode insertIntoBST(TreeNode root, int val) {
//
//    }

    // V0-1
    // IDEA: RECURSION (fixed by gpt)
    public TreeNode insertIntoBST_0_1(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        /**
         *  NOTE !!!
         *
         *   via below, we can still `MODIFY root value`,
         *   even it's not declared as a global variable
         *
         *   -> e.g. we have root as input,
         *      within `insertNodeHelper` method,
         *      we append `new sub tree` to root as its left, right sub tree
         *
         */
        /**
         * It works by modifying the tree in-place
         * (i.e., directly changing the .left or .right
         *  references of the existing nodes).
         *
         */
        insertNodeHelper(root, val); // helper modifies the tree in-place
        return root;
    }

    public void insertNodeHelper(TreeNode root, int val) {
        if (val < root.val) {
            if (root.left == null) {
                root.left = new TreeNode(val);
            } else {
                /** NOTE !!!
                 *
                 *  no need to return val,
                 *  since we `append sub tree` to root directly
                 *  in the method (e.g. root.left == ..., root.right = ...)
                 */
                insertNodeHelper(root.left, val);
            }
        } else {
            if (root.right == null) {
                root.right = new TreeNode(val);
            } else {
                insertNodeHelper(root.right, val);
            }
        }
    }

    // V0-2
    // IDEA: RECURSION V2 (fixed by gpt)
    public TreeNode insertIntoBST_0_2(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        if (val < root.val) {
            root.left = insertIntoBST_0_2(root.left, val);
        } else {
            root.right = insertIntoBST_0_2(root.right, val);
        }

        return root;
    }


    // V1
    // https://youtu.be/Cpg8f79luEA?feature=shared

    // V2-1
    // IDEA: RECURSION
    // https://leetcode.com/problems/insert-into-a-binary-search-tree/solutions/1683942/well-detailed-explaination-java-c-python-3o3n/
    public TreeNode insertIntoBST_2_1(TreeNode root, int val) {
        if (root == null)
            return new TreeNode(val);
        if (root.val > val)
            root.left = insertIntoBST_2_1(root.left, val);
        else
            root.right = insertIntoBST_2_1(root.right, val);
        return root;
    }

    // V2-2
    // IDEA: RECURSION
    // https://leetcode.com/problems/insert-into-a-binary-search-tree/submissions/1603221927/
    public TreeNode insertIntoBST_2_2(TreeNode root, int val) {
        if (root == null)
            return new TreeNode(val);

        TreeNode curr = root;

        while (true) {
            if (curr.val < val) {
                if (curr.right != null)
                    curr = curr.right;
                else {
                    curr.right = new TreeNode(val);
                    break;
                }
            } else {
                if (curr.left != null)
                    curr = curr.left;
                else {
                    curr.left = new TreeNode(val);
                    break;
                }
            }
        }
        return root;
    }

    // V3-1
    // IDEA: RECURSION
    // https://leetcode.com/problems/insert-into-a-binary-search-tree/solutions/6586549/100-acceptance-0ms-very-easy-to-understa-hq6b/
    public TreeNode insertIntoBST_3_1(TreeNode root, int val) {
        if (root == null)
            return new TreeNode(val);
        if (root.val > val)
            root.left = insertIntoBST_3_1(root.left, val);
        if (root.val < val)
            root.right = insertIntoBST_3_1(root.right, val);
        return root;
    }


    // V4
    // https://leetcode.com/problems/insert-into-a-binary-search-tree/solutions/6519231/well-detailed-explanation-java-easy-to-u-mpdh/
    public TreeNode insertIntoBST_4(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val); // Insert as root if tree is empty.
        }

        TreeNode parent = null;
        TreeNode current = root;

        while (current != null) {
            parent = current;
            if (val < current.val) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // Insert the new node as the child of the parent node.
        if (val < parent.val) {
            parent.left = new TreeNode(val);
        } else {
            parent.right = new TreeNode(val);
        }

        return root;
    }


}
