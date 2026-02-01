package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/kth-smallest-element-in-a-bst/
/**
 * 230. Kth Smallest Element in a BST
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,1,4,null,2], k = 1
 * Output: 1
 * Example 2:
 *
 *
 * Input: root = [5,3,6,2,4,null,null,1], k = 3
 * Output: 3
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is n.
 * 1 <= k <= n <= 104
 * 0 <= Node.val <= 104
 *
 *
 * Follow up: If the BST is modified often (i.e., we can do insert and delete operations) and you need to find the kth smallest frequently, how would you optimize?
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KthSmallestElementInABST {

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
    // V0
    // IDEA : BFS + PQ
    /**
     * time = O(N log N)
     * space = O(N)
     */
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

    // V-0-1
    List<Integer> treeVal = new ArrayList<>();
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int kthSmallest_0_1(TreeNode root, int k) {

        //edge
        if(root == null){
            return -1; // ?
        }
        if(root.left == null &&  root.right == null){
            return root.val;
        }

        //System.out.println(">>> (before) treeVal = " + treeVal);
        getTreeVal(root);
        //System.out.println(">>> (after) treeVal = " + treeVal);
        // sorting (decreasing order)
        Collections.sort(treeVal);

        return treeVal.get(k-1);
    }

    private void getTreeVal(TreeNode root){
        if(root == null){
            return; // ??
        }
        // pre-order traversal
        treeVal.add(root.val);
        getTreeVal(root.left);
        getTreeVal(root.right);
    }

    // V0-2
    // IDEA : DFS
    List<Integer> cache = new ArrayList();
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int kthSmallest_0_2(TreeNode root, int k) {

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
    /**
     * time = O(H + K)
     * space = O(H)
     */
    public int kthSmallest_1(TreeNode root, int k) {
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
