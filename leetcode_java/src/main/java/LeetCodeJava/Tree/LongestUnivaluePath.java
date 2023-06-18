package LeetCodeJava.Tree;

// https://leetcode.com/problems/longest-univalue-path/

import LeetCodeJava.DataStructure.TreeNode;

public class LongestUnivaluePath {

    // V0
//    public int longestUnivaluePath(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.add(root);
//
//        HashMap<Integer, Integer> dict = new HashMap<>();
//
//        int ans = 0;
//        getSubLength(root, )
//        return ans;
//    }
//
//    // dfs
//    private TreeNode getSubLength(TreeNode root, List<Integer> tmp){
//
//        if (root == null){
//            //return tmp;
//        }
//
//        if (root.left != null && root.left.val == root.val){
//            tmp.add(root.left.val);
//            return getSubLength(root.left, tmp);
//        }
//
//        if (root.right != null && root.right.val == root.val){
//            tmp.add(root.right.val);
//            return getSubLength(root.right, tmp);
//        }
//
//        root.left = getSubLength(root.left, tmp);
//        root.right = getSubLength(root.right, tmp);
//
//        return root;
//    }

    // V1
    // IDEA : Recursion
    // https://leetcode.com/problems/longest-univalue-path/editorial/
    int ans;
    public int longestUnivaluePath(TreeNode root) {
        ans = 0;
        arrowLength(root);
        return ans;
    }
    public int arrowLength(TreeNode node) {
        if (node == null) return 0;
        int left = arrowLength(node.left);
        int right = arrowLength(node.right);
        int arrowLeft = 0, arrowRight = 0;
        if (node.left != null && node.left.val == node.val) {
            arrowLeft += left + 1;
        }
        if (node.right != null && node.right.val == node.val) {
            arrowRight += right + 1;
        }
        ans = Math.max(ans, arrowLeft + arrowRight);
        return Math.max(arrowLeft, arrowRight);
    }

    // V2
    // https://leetcode.com/problems/longest-univalue-path/solutions/246059/python-java-simple-dfs-solution/

    // V3
    // https://leetcode.com/problems/longest-univalue-path/solutions/1427807/java-easy-approach-with-explanation-postorder/
    private int Lpath= 0;//maximum univalued path length
    public int longestUnivaluePath_3(TreeNode root)
    {
        pathCalculator(root);//helps to calculate the univalued path
        return  Lpath;
    }
    private int pathCalculator(TreeNode root)
    {//postorder traversal DFS
        if(root == null)//base case, we we hit null node
            return 0;//we tell the parent that there is no node ahed of this point and no need to traverse

        int left= pathCalculator(root.left);//recursing down the left subtree and knowing about the left child
        int right= pathCalculator(root.right);//recursing down the right subtree and knowing about the right child

        int Tleft= 0, Tright= 0;//total path achived in left and right path

        //Root or Parent
        if(root.left != null && root.left.val == root.val)
            Tleft+= left + 1;//increasing the edge count, if this two end vertex are same

        if(root.right != null && root.right.val == root.val)
            Tright+= right+ 1;//increasing the edge count, if this two end vertex are same

        Lpath= Math.max(Lpath, Tleft + Tright);//current maximum path achived

        return Math.max(Tleft, Tright);//moving forward, we only choose one max(frequency count high) path to move ahed
    }

}
