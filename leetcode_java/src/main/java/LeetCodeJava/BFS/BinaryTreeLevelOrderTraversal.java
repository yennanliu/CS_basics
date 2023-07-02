package LeetCodeJava.BFS;

// https://leetcode.com/problems/binary-tree-level-order-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class BinaryTreeLevelOrderTraversal {

    // V0
    // IDEA : BFS
//    public List<List<Integer>> levelOrder(TreeNode root) {
//
//        List<List<Integer>> res = new ArrayList<>();
//
//        if (root == null){
//            return res;
//        }
//
//        //List<String> res = Arrays.asList();
//        Queue<TreeNode> queue = new LinkedList<TreeNode>();
//        queue.add(root);
//        int layer = 0;
//
//        while (!queue.isEmpty()){
//
//            //List<Integer> tmpArray = new ArrayList<>();
//            for (int i = 0; i < queue.size(); i++){
//                TreeNode cur = queue.poll();
//                //tmpArray.add(cur.val);
//                res.get(layer).add(cur.val);
//
//                if (cur.left != null){
//                    queue.add(cur.left);
//                }
//
//                if (cur.right != null){
//                    queue.add(cur.right);
//                }
//            }
//            //res.get(layer).add(1);
//            layer += 1;
//            //tmpArray = new ArrayList<>();
//        }
//        return res;
//    }

    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/binary-tree-level-order-traversal/editorial/
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<List<Integer>>();
        if (root == null) return levels;

        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        int level = 0;
        while ( !queue.isEmpty() ) {
            // start the current level
            levels.add(new ArrayList<Integer>());

            // number of elements in the current level
            int level_length = queue.size();
            for(int i = 0; i < level_length; ++i) {
                TreeNode node = queue.remove();

                // fulfill the current level
                // NOTE !!! this trick
                levels.get(level).add(node.val);

                // add child nodes of the current level
                // in the queue for the next level
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            // go to next level
            level++;
        }
        return levels;
    }

    // V2
    // IDEA : RECURSION
    // https://leetcode.com/problems/binary-tree-level-order-traversal/editorial/
    List<List<Integer>> levels = new ArrayList<List<Integer>>();

    public void helper(TreeNode node, int level) {
        // start the current level
        if (levels.size() == level)
            levels.add(new ArrayList<Integer>());

        // fulfil the current level
        levels.get(level).add(node.val);

        // process child nodes for the next level
        if (node.left != null)
            helper(node.left, level + 1);
        if (node.right != null)
            helper(node.right, level + 1);
    }

    public List<List<Integer>> levelOrder_2(TreeNode root) {
        if (root == null) return levels;
        helper(root, 0);
        return levels;
    }

}
