package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/kth-smallest-element-in-a-bst/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

public class KthSmallestElementInABST {

    // V0
    // IDEA : BFS + PQ
    public int kthSmallest(TreeNode root, int k) {

        if (root.left == null && root.right == null){
            return root.val;
        }

        /** NOTE !!!
         *
         *   we use PQ (priority queue) for getting k-th small element
         *
         *   In java, default PQ is small PQ, if we need big PQ, can use update comparator
         *
         *
         *    // Small PQ (default min-heap)
         *    PriorityQueue<Integer> smallPQ = new PriorityQueue<>();
         *
         *    // Big PQ (max-heap)
         *    PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Comparator.reverseOrder());
         *
         */
        PriorityQueue<Integer> pq = new PriorityQueue();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        // bfs
        while(!queue.isEmpty()){
            TreeNode node = queue.poll();
            pq.add(node.val);
            if (node.left != null){
                queue.add(node.left);
            }
            if (node.right != null){
                queue.add(node.right);
            }
        }

        // 1-bases k-th small element
        while (k > 1){
            pq.poll();
            k -= 1;
        }

        return pq.poll();
    }

    // V0'
    // IDEA : DFS
    List<Integer> cache = new ArrayList();
    public int kthSmallest_0(TreeNode root, int k) {

        if (root.left == null && root.right == null){
            return k;
        }

        dfs(root);
        List<Integer> cache_sorted = cache.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < cache_sorted.size(); i++){
            if (i == k-1){
                return cache_sorted.get(i);
            }
        }

        return 0;
    }

    private void dfs(TreeNode root){

        if (root == null){
            return;
        }

        this.cache.add(root.val);

        if (root.left != null){
            dfs(root.left);
        }

        if (root.right != null){
            dfs(root.right);
        }
    }

    // V1
    // IDEA : Recursive Inorder Traversal + STACK
    // https://leetcode.com/problems/kth-smallest-element-in-a-bst/editorial/
    public int kthSmallest_2(TreeNode root, int k) {
        LinkedList<TreeNode> stack = new LinkedList<>();

        while (true) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            if (--k == 0) return root.val;
            root = root.right;
        }
    }

}
