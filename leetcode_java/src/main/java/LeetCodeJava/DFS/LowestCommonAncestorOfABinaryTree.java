package LeetCodeJava.DFS;

// https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/

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
        if ((root == null) || (root.equals(p) || root.equals(q))){
            return root;
        }


        /**
         *  NOTE !!! we assign sub-tree as left, right
         *  -> in order to do value comparision below
         */
        TreeNode left = this.lowestCommonAncestor(root.left, p, q);
        TreeNode right = this.lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null){
            return root;
        }

        /** NOTE !!!
         *
         *  if left is NOT null, retern left
         *  else, return right
         */
        return (left != null && right == null) ? left : right;
    }

    // V1
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/solutions/65226/my-java-solution-which-is-easy-to-understand/
    public TreeNode lowestCommonAncestor_1(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q)  return root;
        TreeNode left = lowestCommonAncestor_1(root.left, p, q);
        TreeNode right = lowestCommonAncestor_1(root.right, p, q);
        if(left != null && right != null)   return root;
        return left != null ? left : right;
    }

    // V2
    // https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/submissions/1280367287/
    public TreeNode lowestCommonAncestor_2(TreeNode root, TreeNode p, TreeNode q) {
        //base case
        if(root == null || root == p || root == q)
            return root;

        TreeNode left = lowestCommonAncestor_2(root.left,p,q);
        TreeNode right = lowestCommonAncestor_2(root.right,p,q);

        // result
        if(left == null)
            return right;
        else if(right == null)
            return left;
        else
            return root;
    }
    
}
