package LeetCodeJava.DFS;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/
/**
 *  LC 235
 *
 Code
 Testcase
 Test Result
 Test Result
 235. Lowest Common Ancestor of a Binary Search Tree
 Solved
 Medium
 Topics
 Companies
 Given a binary search tree (BST), find the lowest common ancestor (LCA) node of two given nodes in the BST.

 According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node in T that has both p and q as descendants (where we allow a node to be a descendant of itself).”



 Example 1:


 Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
 Output: 6
 Explanation: The LCA of nodes 2 and 8 is 6.
 Example 2:


 Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
 Output: 2
 Explanation: The LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself according to the LCA definition.
 Example 3:

 Input: root = [2,1], p = 2, q = 1
 Output: 2


 Constraints:

 The number of nodes in the tree is in the range [2, 105].
 -109 <= Node.val <= 109
 All Node.val are unique.
 p != q
 p and q will exist in the BST.

 Seen this question in a real interview before?
 1/5
 Yes

 *
 */
import LeetCodeJava.DataStructure.TreeNode;

public class LowestCommonAncestorOfABinaryTree {

    // V0
    // IDEA : DFS (RECURSION + POST ORDER TRANSVERSAL)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/lowest-common-ancestor-of-a-binary-tree.py#L42
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {

        // since p != q
//        if (root.equals(p) && root.equals(q)){
//            return root;
//        }

        /**
         *  NOTE !!!  below condition
         *
         *  -> we put "root == null" to avoid null pointer error
         *  -> if (root.equals(p) or (root.equals(q) : return root directly
         */
        /**
         * This is a base case for recursion:
         *
         * - If root is null, we return null (reached the end of a branch).
         *
         * - If root is either p or q, we return root because:
         *
         *    - That means we’ve found one of the nodes we’re looking for.
         *    - The ancestor of a node can be the node itself.
         */
        if ((root == null) || (root.equals(p) || root.equals(q))){
            return root;
        }


        /**
         *  NOTE !!! we assign sub-tree as left, right
         *  -> in order to do `comparison` below
         *
         */
        /**
         *  - We recursively search for p and q in the
         *    left and right subtrees.
         *
         *  - These calls will bubble up either null or
         *    the node (if found in that subtree).
         *
         */
        TreeNode left = this.lowestCommonAncestor(root.left, p, q);
        TreeNode right = this.lowestCommonAncestor(root.right, p, q);

        /**
         * If both left and right are not null, it means:
         *
         *   - p is found in one subtree
         *   - q is found in the other subtree
         *
         *  -> Hence, the current root is their lowest common ancestor,
         *     so we return it.
         *
         *
         */
        if (left != null && right != null){
            return root;
        }

        /** NOTE !!!
         *
         *  if left is NOT null, return left
         *  otherwise, return right
         */
        /**
         *  - If only one side is non-null,
         *     it means both nodes are located in the same subtree,
         *     and that non-null result is potentially the LCA.
         *
         *
         *   - So, return whichever side is not null.
         */
        if (left != null) {
            return left;
        }
        return right;
        // return (left != null && right == null) ? left : right;
    }

    // V1-1
    // https://neetcode.io/problems/lowest-common-ancestor-in-binary-search-tree
    // IDEA: RECURSION
    public TreeNode lowestCommonAncestor_1_1(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        if (Math.max(p.val, q.val) < root.val) {
            return lowestCommonAncestor_1_1(root.left, p, q);
        } else if (Math.min(p.val, q.val) > root.val) {
            return lowestCommonAncestor_1_1(root.right, p, q);
        } else {
            return root;
        }
    }


    // V1-2
    // https://neetcode.io/problems/lowest-common-ancestor-in-binary-search-tree
    // IDEA: Iteration
    public TreeNode lowestCommonAncestor_1_2(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode cur = root;

        while (cur != null) {
            if (p.val > cur.val && q.val > cur.val) {
                cur = cur.right;
            } else if (p.val < cur.val && q.val < cur.val) {
                cur = cur.left;
            } else {
                return cur;
            }
        }
        return null;
    }


    // V2
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/solutions/65226/my-java-solution-which-is-easy-to-understand/
    public TreeNode lowestCommonAncestor_2(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q)  return root;
        TreeNode left = lowestCommonAncestor_2(root.left, p, q);
        TreeNode right = lowestCommonAncestor_2(root.right, p, q);
        if(left != null && right != null)   return root;
        return left != null ? left : right;
    }

    // V3
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/submissions/1280367287/
    public TreeNode lowestCommonAncestor_3(TreeNode root, TreeNode p, TreeNode q) {
        //base case
        if(root == null || root == p || root == q)
            return root;

        TreeNode left = lowestCommonAncestor_3(root.left,p,q);
        TreeNode right = lowestCommonAncestor_3(root.right,p,q);

        // result
        if(left == null)
            return right;
        else if(right == null)
            return left;
        else
            return root;
    }

    // V4
    // IDEA: DFS (GPT)
    public TreeNode lowestCommonAncestor_4(TreeNode root, TreeNode p, TreeNode q) {
        // Keep traversing the tree
        while (root != null) {
            // If both p and q are less than root, LCA must be in left subtree
            if (p.val < root.val && q.val < root.val) {
                root = root.left;
            }
            // If both p and q are greater than root, LCA must be in right subtree
            else if (p.val > root.val && q.val > root.val) {
                root = root.right;
            }
            // If p and q lie on either side of root, or one is the root, then root is LCA
            else {
                return root;
            }
        }
        return null; // This should never be hit as per problem constraints
    }

}
