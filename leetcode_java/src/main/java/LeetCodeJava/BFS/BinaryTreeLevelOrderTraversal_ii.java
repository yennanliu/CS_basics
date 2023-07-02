package LeetCodeJava.BFS;

// https://leetcode.com/problems/binary-tree-level-order-traversal-ii/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeLevelOrderTraversal_ii {

    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();

        if (root == null){
            return res;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        int layer = 0;
        queue.add(root);

        while (!queue.isEmpty()){

            int q_size = queue.size();
            for (int i = 0; i < q_size; i++){

//                if (res.size() <= layer){
//                    res.add(new ArrayList<>());
//                }

                res.add(new ArrayList<>());

                TreeNode cur = queue.remove();
                res.get(layer).add(cur.val);

                if (cur.left != null){
                    queue.add(cur.left);
                }

                if (cur.right != null){
                    queue.add(cur.right);
                }
            }

            layer += 1;

        }

        return res;
    }

}
