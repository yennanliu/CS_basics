package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/range-sum-of-bst/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

/**
 * 938. Range Sum of BST
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root node of a binary search tree and two integers low and high, return the sum of values of all nodes with a value in the inclusive range [low, high].
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [10,5,15,3,7,null,18], low = 7, high = 15
 * Output: 32
 * Explanation: Nodes 7, 10, and 15 are in the range [7, 15]. 7 + 10 + 15 = 32.
 * Example 2:
 *
 *
 * Input: root = [10,5,15,3,7,13,18,1,null,6], low = 6, high = 10
 * Output: 23
 * Explanation: Nodes 6, 7, and 10 are in the range [6, 10]. 6 + 7 + 10 = 23.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 2 * 104].
 * 1 <= Node.val <= 105
 * 1 <= low <= high <= 105
 * All Node.val are unique.
 *
 *
 *
 */
public class RangeSumOfBST {

    // V0
//    public int rangeSumBST(TreeNode root, int low, int high) {
//
//    }

    // V1
    // https://leetcode.com/problems/range-sum-of-bst/solutions/4525606/9943easy-solutionwith-explanation-by-mra-ecci/
    public int rangeSumBST_1(TreeNode root, int low, int high) {
        if (root == null) {
            return 0;
        }

        int currentVal = (root.val >= low && root.val <= high) ? root.val : 0;

        int leftSum = rangeSumBST_1(root.left, low, high);
        int rightSum = rangeSumBST_1(root.right, low, high);

        return currentVal + leftSum + rightSum;
    }

    // V2-1
    // https://leetcode.com/problems/range-sum-of-bst/solutions/192019/javapython-3-3-similar-recursive-and-1-i-t3a8/
    public int rangeSumBST_2_1(TreeNode root, int L, int R) {
        if (root == null)
            return 0; // base case.
        if (root.val < L)
            return rangeSumBST_2_1(root.right, L, R); // left branch excluded.
        if (root.val > R)
            return rangeSumBST_2_1(root.left, L, R); // right branch excluded.
        return root.val + rangeSumBST_2_1(root.right, L, R) + rangeSumBST_2_1(root.left, L, R); // count in both children.
    }


    // V2-2
    // https://leetcode.com/problems/range-sum-of-bst/solutions/192019/javapython-3-3-similar-recursive-and-1-i-t3a8/
    public int rangeSumBST_2_2(TreeNode root, int L, int R) {
        if (root == null)
            return 0; // base case.
        return (L <= root.val && root.val <= R ? root.val : 0) + rangeSumBST_2_2(root.right, L, R)
                + rangeSumBST_2_2(root.left, L, R);
    }



    // V2-3
    // https://leetcode.com/problems/range-sum-of-bst/solutions/192019/javapython-3-3-similar-recursive-and-1-i-t3a8/
    public int rangeSumBST_2_3(TreeNode root, int L, int R) {
        if (root == null) {
            return 0;
        }
        int sum = 0;
        if (root.val > L) {
            sum += rangeSumBST_2_3(root.left, L, R);
        } // left child is a possible candidate.
        if (root.val < R) {
            sum += rangeSumBST_2_3(root.right, L, R);
        } // right child is a possible candidate.
        if (root.val >= L && root.val <= R) {
            sum += root.val;
        } // count root in.
        return sum;
    }

    // V2-4
    // https://leetcode.com/problems/range-sum-of-bst/solutions/192019/javapython-3-3-similar-recursive-and-1-i-t3a8/
    public int rangeSumBST_2_4(TreeNode root, int L, int R) {
        Stack<TreeNode> stk = new Stack<>();
        stk.push(root);
        int sum = 0;
        while (!stk.isEmpty()) {
            TreeNode n = stk.pop();
            if (n == null) {
                continue;
            }
            if (n.val > L) {
                stk.push(n.left);
            } // left child is a possible candidate.
            if (n.val < R) {
                stk.push(n.right);
            } // right child is a possible candidate.
            if (L <= n.val && n.val <= R) {
                sum += n.val;
            }
        }
        return sum;
    }


    // V3



}
