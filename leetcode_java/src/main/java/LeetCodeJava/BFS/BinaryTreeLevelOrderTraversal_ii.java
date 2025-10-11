package LeetCodeJava.BFS;

// https://leetcode.com/problems/binary-tree-level-order-traversal-ii/
/**
 *  107. Binary Tree Level Order Traversal II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given the root of a binary tree, return the bottom-up level order traversal of its nodes' values. (i.e., from left to right, level by level from leaf to root).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[15,7],[9,20],[3]]
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
 * 706.5K
 * Submissions
 * 1.1M
 * Acceptance Rate
 * 65.2%
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class BinaryTreeLevelOrderTraversal_ii {

    // V0
    // IDEA: BFS
    public List<List<Integer>> levelOrderBottom(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();
        List<List<Integer>> cache = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                System.out.println(">>> node.val = " + node.val
                        + ", list = " + list);
                list.add(node.val);
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }
            }
            cache.add(new ArrayList<>(list));
        }

        System.out.println(">>> cache = " + cache);
        for (int i = cache.size() - 1; i >= 0; i--) {
            System.out.println(">>> ");
            List<Integer> _list = cache.get(i);
            System.out.println(">>> i = " + i + ", _list = " + _list);
            res.add(_list);
        }

        return res;
    }

    // V0-1
    // IDEA : BFS
    public List<List<Integer>> levelOrderBottom_0_1(TreeNode root) {

        List<List<Integer>> res = new ArrayList<>();
        List<List<Integer>> resFinal = new ArrayList<>();

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

        for (int j = res.size()-1; j >= 0; j--){
            List<Integer> cur = res.get(j);
            //resFinal.add(new ArrayList<>());
            resFinal.add(cur);
        }

        return resFinal;
    }

    // V1
    // IDEA : BFS
    public List<List<Integer>> levelOrderBottom_2(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<List<Integer>>();
        if (root == null) return levels;

        ArrayDeque<TreeNode> nextLevel = new ArrayDeque() {{ offer(root); }};
        ArrayDeque<TreeNode> currLevel = new ArrayDeque();

        while (!nextLevel.isEmpty()) {
            currLevel = nextLevel.clone();
            nextLevel.clear();
            levels.add(new ArrayList<Integer>());

            for (TreeNode node : currLevel) {
                // append the current node value
                levels.get(levels.size() - 1).add(node.val);

                // process child nodes for the next level
                if (node.left != null)
                    nextLevel.offer(node.left);
                if (node.right != null)
                    nextLevel.offer(node.right);
            }
        }

        /** NOTE !!! we can reverse list via Collections.reverse */
        Collections.reverse(levels);
        return levels;
    }

    // V2
    // IDEA : RECURSION
    // https://leetcode.com/problems/binary-tree-level-order-traversal-ii/editorial/
    List<List<Integer>> levels = new ArrayList<List<Integer>>();

    public void helper(TreeNode node, int level) {
        // start the current level
        if (levels.size() == level)
            levels.add(new ArrayList<Integer>());

        // append the current node value
        levels.get(level).add(node.val);

        // process child nodes for the next level
        if (node.left != null)
            helper(node.left, level + 1);
        if (node.right != null)
            helper(node.right, level + 1);
    }

    public List<List<Integer>> levelOrderBottom_3(TreeNode root) {
        if (root == null) return levels;
        helper(root, 0);
        Collections.reverse(levels);
        return levels;
    }



}
