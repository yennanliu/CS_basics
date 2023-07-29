package LeetCodeJava.DFS;

// https://leetcode.com/problems/binary-tree-right-side-view/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */

public class BinaryTreeRightSideView {

    // VO
    // IDEA : BFS
//    public List<Integer> rightSideView(TreeNode root) {
//
//        List<Integer> res = new ArrayList<>();
//
//        if (root == null){
//            return res;
//        }
//
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.add(root);
//
//        List<List<Integer>> cache = new ArrayList<>();
//
//        while (!queue.isEmpty()){
//
//            int layer = 0;
//            int size = queue.size();
//            for (int i = 0; i < size; i++){
//
//                System.out.println("size = " + size);
//
//                TreeNode node = queue.remove();
//                if (node.equals(null)){
//                    break;
//                }
//
//                //res.add(node.val);
//                cache.get(layer).add(node.val);
//
//                if (node.right != null){
//                    queue.add(node.right);
//                }
//                if (node.left != null){
//                    queue.add(node.left);
//                }
//            }
//
//            layer += 1;
//        }
//
//        System.out.println(">>>");
//        for (List<Integer> x : cache){
//            x.stream().forEach(System.out::println);
//        }
//        System.out.println(">>>");
//
//        for (List<Integer> x : cache){
//            int toAdd = x.get(0);
//            res.add(toAdd);
//        }
//
//        return res;
//    }


    // VO'
    // IDEA : BFS + LC 102
    public List<Integer> rightSideView(TreeNode root) {

        List<Integer> res = new ArrayList<>();

        if (root == null){
            return res;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        int layer = 0;
        queue.add(root);

        List<List<Integer>> cache = new ArrayList<>();

        while (!queue.isEmpty()){

            /** NOTE !!! we add new null array before for loop */
            cache.add(new ArrayList<>());

            /** NOTE !!! we get queue size before for loop */
            int q_size = queue.size();
            for (int i = 0; i < q_size; i++){

                TreeNode cur = queue.remove();
                cache.get(layer).add(cur.val);

                // NOTE !!! cur.left != null
                if (cur.left != null){
                    queue.add(cur.left);
                }

                // NOTE !!! cur.left != null
                if (cur.right != null){
                    queue.add(cur.right);
                }
            }

            layer += 1;

        }

//        System.out.println(">>>");
//        cache.stream().forEach(System.out::println);
//        System.out.println(">>>");

        for (List<Integer> x : cache){
            int toAdd = x.get(x.size()-1);
            res.add(toAdd);
        }

        return res;
    }

    // V1
    // IDEA: BFS: Two Queues
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_2(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        ArrayDeque<TreeNode> nextLevel = new ArrayDeque() {{ offer(root); }};
        ArrayDeque<TreeNode> currLevel = new ArrayDeque();
        List<Integer> rightside = new ArrayList();

        TreeNode node = null;
        while (!nextLevel.isEmpty()) {
            // prepare for the next level
            currLevel = nextLevel;
            nextLevel = new ArrayDeque<>();

            while (! currLevel.isEmpty()) {
                node = currLevel.poll();

                // add child nodes of the current level
                // in the queue for the next level
                if (node.left != null)
                    nextLevel.offer(node.left);
                if (node.right != null)
                    nextLevel.offer(node.right);
            }

            // The current level is finished.
            // Its last element is the rightmost one.
            if (currLevel.isEmpty())
                rightside.add(node.val);
        }
        return rightside;
    }

    // V2
    // IDEA:  BFS: One Queue + Sentinel
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_3(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        Queue<TreeNode> queue = new LinkedList(){{ offer(root); offer(null); }};
        TreeNode prev, curr = root;
        List<Integer> rightside = new ArrayList();

        while (!queue.isEmpty()) {
            prev = curr;
            curr = queue.poll();

            while (curr != null) {
                // add child nodes in the queue
                if (curr.left != null) {
                    queue.offer(curr.left);
                }
                if (curr.right != null) {
                    queue.offer(curr.right);
                }

                prev = curr;
                curr = queue.poll();
            }

            // the current level is finished
            // and prev is its rightmost element
            rightside.add(prev.val);
            // add a sentinel to mark the end
            // of the next level
            if (!queue.isEmpty())
                queue.offer(null);
        }
        return rightside;
    }

    // V3
    // IDEA: BFS: One Queue + Level Size Measurements
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    public List<Integer> rightSideView_4(TreeNode root) {
        if (root == null) return new ArrayList<Integer>();

        ArrayDeque<TreeNode> queue = new ArrayDeque(){{ offer(root); }};
        List<Integer> rightside = new ArrayList();

        while (!queue.isEmpty()) {
            int levelLength = queue.size();

            for(int i = 0; i < levelLength; ++i) {
                TreeNode node = queue.poll();
                // if it's the rightmost element
                if (i == levelLength - 1) {
                    rightside.add(node.val);
                }

                // add child nodes in the queue
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return rightside;
    }
    
    // V4
    // IDEA: Recursive DFS
    // https://leetcode.com/problems/binary-tree-right-side-view/editorial/
    List<Integer> rightside = new ArrayList();
    public void helper(TreeNode node, int level) {
        if (level == rightside.size())
            rightside.add(node.val);

        if (node.right != null)
            helper(node.right, level + 1);
        if (node.left != null)
            helper(node.left, level + 1);
    }

    public List<Integer> rightSideView_5(TreeNode root) {
        if (root == null) return rightside;

        helper(root, 0);
        return rightside;
    }

}
