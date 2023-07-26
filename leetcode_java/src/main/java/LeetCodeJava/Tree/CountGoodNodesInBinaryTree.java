package LeetCodeJava.Tree;

// https://leetcode.com/problems/count-good-nodes-in-binary-tree/

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

public class CountGoodNodesInBinaryTree {

//    int cnt = 1;
//    // V0
//    // IDEA : DFS
//    public int goodNodes(TreeNode root) {
//
//        int ans = 1;
//
//        if (root.left == null && root.right == null){
//            return ans;
//        }
//
//        // BFS
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.add(root);
//        int size = 0;
//        //HashMap<Integer, List<Integer>> map = new HashMap<>();
//        List<List<Integer>> res = new LinkedList<>();
//        while (!queue.isEmpty()){
//           size = queue.size();
//           for (int i = 0 ; i < size; i++){
//
//               TreeNode tmpNode = queue.remove();
//               // get path and check if it's good node
//               List<Integer> cache = new ArrayList<>();
//               List<Integer> path = getPath(tmpNode, root, cache);
//               res.add(path);
//               if (tmpNode.left != null){
//                   queue.add(tmpNode.left);
//               }
//
//               if (tmpNode.right != null){
//                   queue.add(tmpNode.right);
//               }
//            }
//        }
//
//        System.out.println("-->");
//        res.stream().forEach(System.out::println);
//        System.out.println("-->");
//        return 0;
//    }
//
//    private List<Integer> getPath(TreeNode node, TreeNode root, List<Integer> path) {
//
//        if (node == null) {
//            return path;
//        }
//
//        if (root.equals(node)) {
//            return path;
//        }
//
//        path.add(node.val);
//
//        if (node.left != null) {
//            return getPath(node.left, root, path);
//        }
//
//        if (node.right != null) {
//            return getPath(node.right, root, path);
//
//        }
//
//        return path;
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes = 0;

    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V2
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_3(TreeNode root) {
        int numGoodNodes = 0;
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Integer.MIN_VALUE));

        while (stack.size() > 0) {
            Pair curr = stack.pop();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.left != null) {
                stack.push(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.right != null) {
                stack.push(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }

    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair2 {
        public TreeNode node;
        public int maxSoFar;

        public Pair2(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    public int goodNodes_4(TreeNode root) {
        int numGoodNodes = 0;
        Queue<Pair> queue = new LinkedList<>();
        queue.offer(new Pair(root, Integer.MIN_VALUE));

        while (queue.size() > 0) {
            Pair curr = queue.poll();
            if (curr.maxSoFar <= curr.node.val) {
                numGoodNodes++;
            }

            if (curr.node.right != null) {
                queue.offer(new Pair(curr.node.right, Math.max(curr.node.val, curr.maxSoFar)));
            }

            if (curr.node.left != null) {
                queue.offer(new Pair(curr.node.left, Math.max(curr.node.val, curr.maxSoFar)));
            }
        }

        return numGoodNodes;
    }

}
