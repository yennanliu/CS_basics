package LeetCodeJava.HashTable;

// https://leetcode.com/problems/binary-tree-vertical-order-traversal/description/
// https://www.youtube.com/watch?v=-JFngYs21Y8

import DataStructure.Pair;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *  314. Binary Tree Vertical Order Traversal
 * Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by column).
 *
 * If two nodes are in the same row and column, the order should be from left to right.
 *
 * Examples 1:
 *
 * Input: [3,9,20,null,null,15,7]
 *
 *    3
 *   /\
 *  /  \
 *  9  20
 *     /\
 *    /  \
 *   15   7
 *
 * Output:
 *
 * [
 *   [9],
 *   [3,15],
 *   [20],
 *   [7]
 * ]
 * Examples 2:
 *
 * Input: [3,9,8,4,0,1,7]
 *
 *      3
 *     /\
 *    /  \
 *    9   8
 *   /\  /\
 *  /  \/  \
 *  4  01   7
 *
 * Output:
 *
 * [
 *   [4],
 *   [9],
 *   [3,0,1],
 *   [8],
 *   [7]
 * ]
 * Examples 3:
 *
 * Input: [3,9,8,4,0,1,7,null,null,null,2,5] (0's right child is 2 and 1's left child is 5)
 *
 *      3
 *     /\
 *    /  \
 *    9   8
 *   /\  /\
 *  /  \/  \
 *  4  01   7
 *     /\
 *    /  \
 *    5   2
 *
 * Output:
 *
 * [
 *   [4],
 *   [9,5],
 *   [3,0,1],
 *   [8,2],
 *   [7]
 * ]
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Adobe Amazon Bloomberg ByteDance Databricks Expedia Facebook Google Mathworks Microsoft Oracle Snapchat
 * Problem Solution
 * 314-Binary-Tree-Vertical-Order-Traversal
 * All Problems:
 */
public class BinaryTreeVerticalOrderTraversal {

    // custom helper class (GPT)
//    class Pair<K, V> {
//        private K key;
//        private V value;
//
//        public Pair(K key, V value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        public K getKey() {
//            return key;
//        }
//
//        public V getValue() {
//            return value;
//        }
//    }

    // V0
//    public List<List<Integer>> verticalOrder(TreeNode root) {
//    }

    // V0-1
    // IDEA: BFS + CUSTOM CLASS (fixed by gpt)
    class Solution {
        public class NodeIdx {
            int idx;
            TreeNode node;
            public NodeIdx(int idx, TreeNode node) {
                this.idx = idx;
                this.node = node;
            }
        }

        public List<List<Integer>> verticalOrder_0_1(TreeNode root) {
            List<List<Integer>> res = new ArrayList<>();
            if (root == null) return res;

            // BFS queue
            Queue<NodeIdx> q = new LinkedList<>();
            q.add(new NodeIdx(0, root));

            // map: { column index -> list of node values }
            Map<Integer, List<Integer>> map = new HashMap<>();
            int minCol = 0, maxCol = 0;

            while (!q.isEmpty()) {
                NodeIdx cur = q.poll();
                int idx = cur.idx;
                TreeNode node = cur.node;

                // add node value to map
                map.computeIfAbsent(idx, k -> new ArrayList<>()).add(node.val);

                /**
                 *  we maintain the min, max val of idx
                 *  so we can easily go through in the correct order and range
                 *  when collect result
                 */
                // track range of column indices
                minCol = Math.min(minCol, idx);
                maxCol = Math.max(maxCol, idx);

                // add children with updated column index
                if (node.left != null) q.add(new NodeIdx(idx - 1, node.left));
                if (node.right != null) q.add(new NodeIdx(idx + 1, node.right));
            }

            // build result list from minCol → maxCol
            for (int i = minCol; i <= maxCol; i++) {
                res.add(map.get(i));
            }

            return res;
        }
    }


    // V1-1
    // IDEA: BFS + SORT
    // https://neetcode.io/solutions/binary-tree-vertical-order-traversal
    public List<List<Integer>> verticalOrder_1_1(TreeNode root) {
        if (root == null) return new ArrayList<>();

        Map<Integer, List<Integer>> cols = new TreeMap<>();
        Queue<Pair<TreeNode, Integer>> queue = new LinkedList<>();
        queue.offer(new Pair<>(root, 0));

        while (!queue.isEmpty()) {
            Pair<TreeNode, Integer> p = queue.poll();
            TreeNode node = p.getKey();
            int pos = p.getValue();

            cols.computeIfAbsent(pos, k -> new ArrayList<>()).add(node.val);

            if (node.left != null) queue.offer(new Pair<>(node.left, pos - 1));
            if (node.right != null) queue.offer(new Pair<>(node.right, pos + 1));
        }

        return new ArrayList<>(cols.values());
    }

    // V1-2
    // IDEA: DFS + SORT
    // https://neetcode.io/solutions/binary-tree-vertical-order-traversal
    private Map<Integer, List<int[]>> cols = new TreeMap<>();

    public List<List<Integer>> verticalOrder_1_2(TreeNode root) {
        dfs(root, 0, 0);
        List<List<Integer>> res = new ArrayList<>();

        for (List<int[]> list : cols.values()) {
            list.sort(Comparator.comparingInt(a -> a[0]));
            List<Integer> colVals = new ArrayList<>();
            for (int[] p : list) colVals.add(p[1]);
            res.add(colVals);
        }

        return res;
    }

    /** NOTE !!!
     *
     *  the param of this dfs helper func
     *
     *    -> int row, int col
     */
    private void dfs(TreeNode node, int row, int col) {
        if (node == null) return;
        cols.computeIfAbsent(col, k -> new ArrayList<>()).add(new int[]{row, node.val});
        /** NOTE !!!
         *
         *  how we recursively call the dfs func
         */
        dfs(node.left, row + 1, col - 1);
        dfs(node.right, row + 1, col + 1);
    }

    // V1-3
    // IDEA: Breadth First Search (Optimal)
    // https://neetcode.io/solutions/binary-tree-vertical-order-traversal
    public List<List<Integer>> verticalOrder_1_3(TreeNode root) {
        if (root == null) return new ArrayList<>();

        Map<Integer, List<Integer>> cols = new HashMap<>();
        Queue<Pair<TreeNode, Integer>> queue = new LinkedList<>();
        queue.offer(new Pair<>(root, 0));
        int minCol = 0, maxCol = 0;

        while (!queue.isEmpty()) {
            Pair<TreeNode, Integer> p = queue.poll();
            TreeNode node = p.getKey();
            int col = p.getValue();

            cols.computeIfAbsent(col, x -> new ArrayList<>()).add(node.val);
            minCol = Math.min(minCol, col);
            maxCol = Math.max(maxCol, col);

            if (node.left != null) queue.offer(new Pair<>(node.left, col - 1));
            if (node.right != null) queue.offer(new Pair<>(node.right, col + 1));
        }

        List<List<Integer>> res = new ArrayList<>();
        for (int c = minCol; c <= maxCol; c++) {
            res.add(cols.get(c));
        }
        return res;
    }

    // V1-4
    // IDEA: DFS (OPTIMAL)
    // https://neetcode.io/solutions/binary-tree-vertical-order-traversal
    private Map<Integer, List<int[]>> cols_1_4 = new HashMap<>();
    private int minCol = 0, maxCol = 0;

    public List<List<Integer>> verticalOrder_1_4(TreeNode root) {
        if (root == null) return new ArrayList<>();
        dfs_1_4(root, 0, 0);

        List<List<Integer>> res = new ArrayList<>();
        for (int c = minCol; c <= maxCol; c++) {
            List<int[]> list = cols_1_4.getOrDefault(c, new ArrayList<>());
            list.sort(Comparator.comparingInt(a -> a[0])); // sort by row
            List<Integer> colVals = new ArrayList<>();
            for (int[] p : list) colVals.add(p[1]);
            res.add(colVals);
        }
        return res;
    }

    private void dfs_1_4(TreeNode node, int row, int col) {
        if (node == null) return;
        cols_1_4.computeIfAbsent(col, k -> new ArrayList<>()).add(new int[]{row, node.val});
        minCol = Math.min(minCol, col);
        maxCol = Math.max(maxCol, col);
        dfs_1_4(node.left, row + 1, col - 1);
        dfs_1_4(node.right, row + 1, col + 1);
    }

    // V2-1
    // IDEA: DFS
    // https://leetcode.ca/2016-10-09-314-Binary-Tree-Vertical-Order-Traversal/

    // V2-2
    // IDEA: BFS
    // https://leetcode.ca/2016-10-09-314-Binary-Tree-Vertical-Order-Traversal/
    public List<List<Integer>> verticalOrder_2_2(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Deque<Pair<TreeNode, Integer>> q = new ArrayDeque<>();
        q.offer(new Pair<>(root, 0));
        TreeMap<Integer, List<Integer>> d = new TreeMap<>();
        while (!q.isEmpty()) {
            for (int n = q.size(); n > 0; --n) {
                Pair<TreeNode, Integer> p = q.pollFirst();
                root = p.getKey();
                int offset = p.getValue();
                d.computeIfAbsent(offset, k -> new ArrayList()).add(root.val);
                if (root.left != null) {
                    q.offer(new Pair<>(root.left, offset - 1));
                }
                if (root.right != null) {
                    q.offer(new Pair<>(root.right, offset + 1));
                }
            }
        }
        return new ArrayList<>(d.values());
    }


    // V3


}
