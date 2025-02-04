package LeetCodeJava.BFS;

// https://leetcode.com/problems/binary-tree-level-order-traversal/
/**
 *  102. Binary Tree Level Order Traversal
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[3],[9,20],[15,7]]
 * Example 2:
 *
 * Input: root = [1]
 * Output: [[1]]
 * Example 3:
 *
 * Input: root = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 2000].
 * -1000 <= Node.val <= 1000
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 2.6M
 * Submissions
 * 3.8M
 * Acceptance Rate
 * 69.5%
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class BinaryTreeLevelOrderTraversal {

    // V0
    // IDEA : BFS
    public List<List<Integer>> levelOrder(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();

        if (root == null){
            return res;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        int layer = 0;
        queue.add(root);

        while (!queue.isEmpty()){

            /** NOTE !!! we add new null array before for loop */
            res.add(new ArrayList<>());
            /** NOTE !!! we get queue size before for loop */
            int q_size = queue.size();
            for (int i = 0; i < q_size; i++){

                TreeNode cur = queue.poll();
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

    // V0-1
    // IDEA : BFS + custom class (save layer info, and TreeNode)
    public List<List<Integer>> levelOrder_0_1(TreeNode root) {

        if (root == null){
            return new ArrayList();
        }

        List<List<Integer>> res = new ArrayList<>();
        Queue<MyQueue> queue = new LinkedList<>(); // double check
        int layer = 0;
        queue.add(new MyQueue(layer, root));

        while (!queue.isEmpty()){
            MyQueue cur = queue.poll();
            int curLayer = cur.layer;
            if (curLayer >= res.size()){
                res.add(new ArrayList());
            }
            List<Integer> collected = res.get(curLayer);
            res.set(curLayer, collected);
            collected.add(cur.root.val);
            if (cur.root.left != null){
                queue.add(new MyQueue(curLayer+1, cur.root.left));
            }
            if (cur.root.right != null){
                queue.add(new MyQueue(curLayer+1, cur.root.right));
                new MyQueue(curLayer+1, cur.root.right);
            }
        }

        return res;
    }

    public class MyQueue {
        public int layer;
        public TreeNode root;

        MyQueue() {

        }

        MyQueue(int layer, TreeNode root) {
            this.layer = layer;
            this.root = root;
        }
    }

    // V0-2
    // IDEA: BFS + CUSTOM CLASS
    public class MyTreeLayer {
        // attr
        int layer;
        TreeNode root;

        // constructor
        MyTreeLayer(int layer, TreeNode root) {
            this.layer = layer;
            this.root = root;
        }

        // getter, setter
        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public TreeNode getRoot() {
            return root;
        }

        public void setRoot(TreeNode root) {
            this.root = root;
        }
    }

    public List<List<Integer>> levelOrder_0_2(TreeNode root) {
        // edge
        if (root == null) {
            return new ArrayList<>();
        }
        if (root.left == null && root.right == null) {
            List<Integer> tmp = new ArrayList<>();
            tmp.add(root.val);
            List<List<Integer>> res = new ArrayList<>();
            res.add(tmp);
            return res;
        }

        List<List<Integer>> res = new ArrayList<>();
        // bfs
        Queue<MyTreeLayer> q = new LinkedList<>();
        q.add(new MyTreeLayer(0, root));
        while (!q.isEmpty()) {

            MyTreeLayer myTreeLayer = q.poll();
            int depth = myTreeLayer.getLayer();
            TreeNode curRoot = myTreeLayer.getRoot();

            if (res.size() < depth + 1) {
                res.add(new ArrayList<>());
            }

      // NOTE !!! below is wrong !!!!
      // List<Integer> tmp = res.get(depth);
      // tmp.add(curRoot.val);
      // res.add(depth, tmp); // /??

      /**
       *  NOTE !!!
       *
       *   via below, we can retrieve List val by idx,
       *   append new val to the val (with the same idx)
       *
       *
       *   code breakdown:
       *
       *   	•	res is a List<List<Integer>>, where each inner list represents a level of the tree.
       * 	•	res.get(depth) retrieves the list at the given depth.
       * 	•	.add(curRoot.val) adds the current node’s value to the corresponding depth level.
       *
       */
      res.get(depth).add(curRoot.val);

            if (curRoot.left != null) {
                q.add(new MyTreeLayer(depth + 1, curRoot.left));
            }
            if (curRoot.right != null) {
                q.add(new MyTreeLayer(depth + 1, curRoot.right));
            }

        }

        //System.out.println(">>> res = " + res);
        return res;
    }


    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/binary-tree-level-order-traversal/editorial/
    public List<List<Integer>> levelOrder_2(TreeNode root) {
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

    public List<List<Integer>> levelOrder_3(TreeNode root) {
        if (root == null) return levels;
        helper(root, 0);
        return levels;
    }

}
