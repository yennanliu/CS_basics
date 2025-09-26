package LeetCodeJava.Recursion;

// https://leetcode.com/problems/minimum-depth-of-binary-tree/
/**
 * 111. Minimum Depth of Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given a binary tree, find its minimum depth.
 *
 * The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.
 *
 * Note: A leaf is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 2
 * Example 2:
 *
 * Input: root = [2,null,3,null,4,null,5,null,6]
 * Output: 5
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 105].
 * -1000 <= Node.val <= 1000
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *  The minimum depth is the number of nodes
 *  along the shortest path from the root node down to the nearest leaf node.
 *
 *  -> NOTE !!! depth is node number, not distance
 *  > The minimum depth is the number of nodes ...
 *
 */

public class MinimumDepthOfBinaryTree {

    // V0
//    public int minDepth(TreeNode root) {
//
//    }

    /** NOTE !!! below is WRONG
     *
     *  (reason described in V0-0-1)
     */
//    public int minDepth(TreeNode root) {
//        // edge
//        if(root == null){
//            return 0;
//        }
//
//        // post order traverse
//        // left -> right -> root
//
//        int leftDepth = minDepth(root.left);
//        int rightDepth = minDepth(root.right);
//
//        return 1 + Math.max(leftDepth, rightDepth);
//    }

    // V0-0-1
    // IDEA: DFS (fixed by gpt)
    /**
     *  Max depth VS min depth
     *
     *  - maxDepth can safely use Math.max() with null children.
     * 	- minDepth needs this guard, because null doesn’t count as a valid path.
     *
     */
    public int minDepth_0_0_1(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }

        // if one of the child is null, we must go through the other child
        /**
         *  NOTE !!! below
         *
         *   the ticky part of this problem
         *
         *
         *   1) Why special-case when one child is null?
         *
         *    The minDepth of a tree is the shortest path from root to a leaf.
         *    -> A leaf means a node with no children (left == null && right == null).
         *
         *    -> If we simply take Math.min(leftDepth, rightDepth) without checks,
         *       then a missing child contributes 0, and you’ll incorrectly
         *       think that the null path is valid.
         *
         *     -> e.g. if we go WITHOUT below special checks,
         *             we'll take `null` node as shortest depth (depth=0) by MISTAKE
         *
         *
         *   2) Example where it breaks if you don’t handle null child
         *
         *      input:
         *
         *              1
         *               \
         *                2
         *
         *    ->  - Node 1 has:
         * 	         - left = null
         * 	         - right = 2
         *
         * 	  -> if we did:
         *
         * 	  ```
         * 	  int leftDepth = minDepth(root.left);  // 0
         *    int rightDepth = minDepth(root.right); // 1
         *    return 1 + Math.min(leftDepth, rightDepth); // 1 + 0 = 1 ❌ WRONG
         * `  ```
         *
         *    -> But the true minimum depth is 2 (1 → 2).
         *    -> Why wrong? Because 0 (null child) was treated as a valid path.
         *       But you can’t stop at null, you need a real leaf.
         *
         *   3) Correct handling
         *
         *      ```
         *      if (root.left == null) {
         *     return 1 + minDepth(root.right);
         *     }
         *     if (root.right == null) {
         *     return 1 + minDepth(root.left);
         *     }
         *     ```
         *
         *     -> This ensures:
         * 	    - If left == null, the path must go through right child.
         * 	    - If right == null, the path must go through left child.
         *
         *      -> So you never mistakenly take a 0 from a missing child.
         *
         *
         */
        /**
         *  NOTE !!! below logic ensures
         *
         *  - If left == null, the path MUST go through right child.
         *  - If right == null, the path MUST go through left child.
         */
        if (root.left == null) {
            return 1 + minDepth_0_0_1(root.right);
        }
        if (root.right == null) {
            return 1 + minDepth_0_0_1(root.left);
        }

        // both children exist -> take the smaller depth
        int leftDepth = minDepth_0_0_1(root.left);
        int rightDepth = minDepth_0_0_1(root.right);

        return 1 + Math.min(leftDepth, rightDepth);
    }

    // V0-1
    // IDEA: DFS (fixed by gpt)
    public int minDepth_0_1(TreeNode root) {
        if (root == null)
            return 0;

        // If one of the children is null, don't take min with 0
        /**
         * 	NOTE !!!
         *
         * 	If either left or right is null,
         * 	we must go down the non-null side — minDepth is
         * 	defined as the depth to the nearest leaf,
         * 	so null branches can’t be considered in the Math.min().
         */
        if (root.left == null)
            return minDepth_0_1(root.right) + 1;
        if (root.right == null)
            return minDepth_0_1(root.left) + 1;

        return Math.min(minDepth_0_1(root.left), minDepth_0_1(root.right)) + 1;
    }

    // V0-2
    // IDEA : DFS
    public int minDepth_0_2(TreeNode root) {

        if (root == null){
            return  0;
        }

        //maxDepth = Math.max(getDepth(root.left), getDepth(root.right));
        return getDepth(root);
    }

    private int getDepth(TreeNode root){

        if (root == null){
            return 0;
        }

//        if (root.left != null){
//            return 1 + getDepth(root.left);
//        }
//
//        if (root.right != null){
//            return 1 + getDepth(root.right);
//        }

        /**
         *  NOTE !!! below condition
         *  -> we need to go till meat a node,
         *     then can calculate min depths (number of node)
         *
         *  -> Note: A leaf is a node with no children.
         *
         *  -> plz check below example for idea
         *  example : [2,null,3,null,4,null,5,null,6]
         *
         */
        if (root.left == null) {
            return 1 + getDepth(root.right);
        } else if (root.right == null) {
            return 1 + getDepth(root.left);
        }

        return 1 + Math.min(getDepth(root.left), getDepth(root.right));
    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/minimum-depth-of-binary-tree/editorial/
    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // If only one of child is non-null, then go into that recursion.
        if (root.left == null) {
            return 1 + dfs(root.right);
        } else if (root.right == null) {
            return 1 + dfs(root.left);
        }

        // Both children are non-null, hence call for both childs.
        return 1 + Math.min(dfs(root.left), dfs(root.right));
    }

    public int minDepth_2(TreeNode root) {
        return dfs(root);
    }

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/minimum-depth-of-binary-tree/editorial/
    public int minDepth_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int depth = 1;

        while (q.isEmpty() == false) {
            int qSize = q.size();

            while (qSize > 0) {
                qSize--;

                TreeNode node = q.remove();
                // Since we added nodes without checking null, we need to skip them here.
                if (node == null) {
                    continue;
                }

                // The first leaf would be at minimum depth, hence return it.
                if (node.left == null && node.right == null) {
                    return depth;
                }

                q.add(node.left);
                q.add(node.right);
            }
            depth++;
        }
        return -1;
    }



}
