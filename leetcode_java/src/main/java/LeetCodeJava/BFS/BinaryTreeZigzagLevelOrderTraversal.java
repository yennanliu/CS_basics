package LeetCodeJava.BFS;

// https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *
 * 103. Binary Tree Zigzag Level Order Traversal
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return the zigzag level order traversal of its nodes' values. (i.e., from left to right, then right to left for the next level and alternate between).
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[3],[20,9],[15,7]]
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
 * -100 <= Node.val <= 100
 *
 */
public class BinaryTreeZigzagLevelOrderTraversal {

    // V0
//    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
//
//    }

    // V1-1
    // https://leetcode.ca/2016-03-12-103-Binary-Tree-Zigzag-Level-Order-Traversal/
    public List<List<Integer>> zigzagLevelOrder_1_1(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Deque<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        boolean left = true;
        while (!q.isEmpty()) {
            List<Integer> t = new ArrayList<>();
            for (int n = q.size(); n > 0; --n) {
                TreeNode node = q.poll();
                t.add(node.val);
                if (node.left != null) {
                    q.offer(node.left);
                }
                if (node.right != null) {
                    q.offer(node.right);
                }
            }
            if (!left) {
                Collections.reverse(t);
            }
            ans.add(t);
            left = !left;
        }
        return ans;
    }

    // V1-2
    // https://leetcode.ca/2016-03-12-103-Binary-Tree-Zigzag-Level-Order-Traversal/
    public List<List<Integer>> zigzagLevelOrder_1_2(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        boolean isLeftToRight = true;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        int currentLevelCount = 1;
        int nextLevelCount = 0;

        List<Integer> one = new ArrayList<>();

        while (!q.isEmpty()) {
            TreeNode current = q.poll();
            currentLevelCount--;

            if (isLeftToRight) {
                one.add(current.val);
            } else {
                one.add(0, current.val);
            }

            if (current.left != null) {
                q.offer(current.left);
                nextLevelCount++;
            }
            if (current.right != null) {
                q.offer(current.right);
                nextLevelCount++;
            }

            if (currentLevelCount == 0) {
                currentLevelCount = nextLevelCount;
                nextLevelCount = 0;

                result.add(one);
                one = new ArrayList<>();

                isLeftToRight = !isLeftToRight;
            }
        }

        return result;
    }

    // V1-3
    // https://leetcode.ca/2016-03-12-103-Binary-Tree-Zigzag-Level-Order-Traversal/
    public List<List<Integer>> zigzagLevelOrder_1_3(TreeNode root) {

        List<List<Integer>> list = new ArrayList<List<Integer>>();

        if (root == null) {
            return list;
        }

        Queue<TreeNode> q = new LinkedList<>();

        q.offer(root);
        q.offer(null);// @note: use null as marker for end of level

        boolean direction = true; // true: left=>right, false: right=>left
        List<Integer> oneLevel = new ArrayList<>();
        while (!q.isEmpty()) {

            TreeNode current = q.poll();

            if (current == null) {
                List<Integer> copy = new ArrayList<>(oneLevel);
                list.add(copy);

                // clean after one level recorded
                oneLevel.clear();// @memorize: this api
                direction = !direction;

                // @note:@memorize: if stack is now empty then DO NOT add null, or else infinite looping
                // sk.offer(null); // add marker
                if (!q.isEmpty()) {
                    q.offer(null); // add marker
                }

                continue;
            }

            if (direction) {
                oneLevel.add(current.val);
            } else {
                oneLevel.add(0, current.val);
            }

            // @note:@memorize: since using null as marker, then must avoid adding null when children are null
            // sk.offer(current.left);
            // sk.offer(current.right);
            if (current.left != null) {
                q.offer(current.left);
            }
            if (current.right != null) {
                q.offer(current.right);
            }

        }

        return list;
    }

    // V2
    // https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/solutions/7262597/non-ai-sol-java-by-aadi_dayal08-e654/
    public List<List<Integer>> zigzagLevelOrder_2(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        List<List<Integer>> list = new ArrayList<>();

        if (root == null) {
            return list;
        }

        q.add(root);

        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> in = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                TreeNode x = q.poll();
                in.add(x.val);

                if (x.left != null)
                    q.add(x.left);
                if (x.right != null)
                    q.add(x.right);
            }
            list.add(in);
        }

        for (int i = 1; i < list.size(); i += 2) {
            Collections.reverse(list.get(i));
        }

        return list;
    }

    // V3
    // https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/solutions/7257957/simple-java-solution-easy-to-understand-kytog/
//    public List<List<Integer>> zigzagLevelOrder_3(TreeNode root) {
//        List<List<Integer>> res = new ArrayList<>();
//        if (root == null)
//            return res;
//        Queue<TreeNode> q = new LinkedList<>();
//        q.offer(root);
//        boolean flag = true;
//
//        while (!q.isEmpty()) {
//            int n = q.size();
//            List<Integer> list = new LinkedList<>();
//            if (flag) {
//                for (int i = 0; i < n; i++) {
//                    TreeNode curr = q.poll();
//                    list.add(curr.val);
//                    if (curr.left != null)
//                        q.offer(curr.left);
//                    if (curr.right != null)
//                        q.offer(curr.right);
//                }
//            } else {
//                for (int i = 0; i < n; i++) {
//                    TreeNode curr = q.poll();
//                    list.addFirst(curr.val);
//                    if (curr.left != null)
//                        q.offer(curr.left);
//                    if (curr.right != null)
//                        q.offer(curr.right);
//                }
//            }
//            flag = !flag;
//            res.add(list);
//        }
//        return res;
//    }



}
