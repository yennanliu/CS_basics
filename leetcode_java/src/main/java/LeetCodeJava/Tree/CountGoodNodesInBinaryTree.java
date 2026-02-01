package LeetCodeJava.Tree;

// https://leetcode.com/problems/count-good-nodes-in-binary-tree/
/**
 * 1448. Count Good Nodes in Binary Tree
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a binary tree root, a node X in the tree is named good if in the path from root to X there are no nodes with a value greater than X.
 *
 * Return the number of good nodes in the binary tree.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: root = [3,1,4,3,null,1,5]
 * Output: 4
 * Explanation: Nodes in blue are good.
 * Root Node (3) is always a good node.
 * Node 4 -> (3,4) is the maximum value in the path starting from the root.
 * Node 5 -> (3,4,5) is the maximum value in the path
 * Node 3 -> (3,1,3) is the maximum value in the path.
 * Example 2:
 *
 *
 *
 * Input: root = [3,3,null,4,2]
 * Output: 3
 * Explanation: Node 2 -> (3, 3, 2) is not good, because "3" is higher than it.
 * Example 3:
 *
 * Input: root = [1]
 * Output: 1
 * Explanation: Root is considered as good.
 *
 *
 * Constraints:
 *
 * The number of nodes in the binary tree is in the range [1, 10^5].
 * Each node's value is between [-10^4, 10^4].
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class CountGoodNodesInBinaryTree {

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
    // IDEA : DFS + maintain min value
    /**
     *  NOTE !!!
     *
     *   `good node` is there is NO node val GREATER than such node
     *    BETWEEN ROOT and NODE
     *
     *
     *    > is  good if in the path from root to X there are no nodes with a value greater than X.
     */
    private int numGoodNodes = 0;
    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes(TreeNode root) {
        //dfs(root, Integer.MIN_VALUE);
        findGoodNode(root, root.val); // can use root.val as well
        return numGoodNodes;
    }

    private void findGoodNode(TreeNode node, int maxSoFar) {

        if (maxSoFar <= node.val) {
            numGoodNodes += 1; // found a good node
        }

        /**
         *  NOTE !!!
         *
         *   below structure can be modified as below (check `V0-3` solution below)
         *
         *   ```
         *   if(root == null){
         *       return null;
         *   }
         *
         *    findGoodNode(node.right, Math.max(node.val, maxSoFar));
         *    findGoodNode(node.left, Math.max(node.val, maxSoFar));
         *
         *   ```
         *
         *
         *   -> so we have logic handle if `root is null` beforehand
         *      then we can just call method recursively with node.left, root.right
         *
         */
        if (node.right != null) {
            /** NOTE !!!
             *
             *  maintain the "max so far" value,
             *  so instead of comparing all nodes in path
             *  -> ONLY need to compare current node val with maxSoFar
             */
            findGoodNode(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            /** NOTE !!!
             *
             *  maintain the "max so far" value,
             *  so instead of comparing all nodes in path
             *  -> ONLY need to compare current node val with maxSoFar
             */
            findGoodNode(node.left, Math.max(node.val, maxSoFar));
        }
    }

    // V0-0-1
    // IDEA: DFS
    int goodNodeCnt = 0;
    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_0_0_1(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 1;
        }

        countHelper(root, root.val);

        return goodNodeCnt;
    }

    private void countHelper(TreeNode root, int biggestTillNow){
        // edge
        if(root == null){
            return;
        }

        if(root.val >= biggestTillNow){
            biggestTillNow = root.val;
            goodNodeCnt += 1;
        }

        countHelper(root.left, biggestTillNow);
        countHelper(root.right, biggestTillNow);
    }

    // V0-1
    // IDEA: DFS + maintain max value (fixed by gpt)
//    class TreeNode {
//        int val;
//        TreeNode left, right;
//
//        TreeNode(int val) {
//            this.val = val;
//        }
//    }

    private int res = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_0_1(TreeNode root) {
        if (root == null)
            return 0;
        dfsCheckGoodNode(root, root.val);
        return res;
    }

    private void dfsCheckGoodNode(TreeNode node, int maxSoFar) {
        if (node == null)
            return;

        // Check if the current node is good
        if (node.val >= maxSoFar) {
            res++;
            maxSoFar = node.val; // Update max value seen so far
        }

        // Recur for left and right children
        dfsCheckGoodNode(node.left, maxSoFar);
        dfsCheckGoodNode(node.right, maxSoFar);
    }

    // V0-2
    // IDEA: DFS + MAINTAIN `big so far` value
    /**
     *  NOTE !!!
     *
     *   `good node` is there is NO node val GREATER than such node
     *    BETWEEN ROOT and NODE
     *
     *
     *    > is  good if in the path from root to X there are no nodes with a value greater than X.
     */
    int goodNodeCnt_2 = 0;
    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_0_2(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        // dfs
        this.checkGoodNode(root, root.val);
        return goodNodeCnt_2;
    }

    /** NOTE !!! below helper func NOT return anything */
    private void checkGoodNode(TreeNode root, int maxSoFar) {
        if (root == null) {
            return;
        }
        if (root.val >= maxSoFar) {
            goodNodeCnt_2 += 1;
        }

        if (root.left != null) {
            this.checkGoodNode(
                    root.left,
                    Math.max(root.val, maxSoFar));
        }

        if (root.right != null) {
            this.checkGoodNode(
                    root.right,
                    Math.max(root.val, maxSoFar));
        }
    }

    // V0-3
    // IDEA: DFS + `max val so far` (fixed by gpt)
    int goodNodeCnt_3 = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_0_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        getGoodCnt(root, root.val);
        return goodNodeCnt_3;
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public void getGoodCnt(TreeNode root, int maxValTillNow) {
        if (root == null) {
            return;
        }

        if (root.val >= maxValTillNow) {
            goodNodeCnt_3++;
        }

        // Update max value seen so far
        int newMax = Math.max(maxValTillNow, root.val);
        getGoodCnt(root.left, newMax);
        getGoodCnt(root.right, newMax);
    }


    // V1-1
    // https://neetcode.io/problems/count-good-nodes-in-binary-tree
    // IDEA: DFS
    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_1_1(TreeNode root) {
        return dfs_1_1(root, root.val);
    }

    private int dfs_1_1(TreeNode node, int maxVal) {
        if (node == null) {
            return 0;
        }

        int res = (node.val >= maxVal) ? 1 : 0;
        maxVal = Math.max(maxVal, node.val);
        res += dfs_1_1(node.left, maxVal);
        res += dfs_1_1(node.right, maxVal);
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/count-good-nodes-in-binary-tree
    // IDEA: BFS
//    public int goodNodes_1_2(TreeNode root) {
//        int res = 0;
//        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();
//        q.offer(new Pair<>(root, Integer.MIN_VALUE));
//
//        while (!q.isEmpty()) {
//            Pair<TreeNode, Integer> pair = q.poll();
//            TreeNode node = pair.getKey();
//            int maxval = pair.getValue();
//            if (node.val >= maxval) {
//                res++;
//            }
//            if (node.left != null) {
//                q.offer(new Pair<>(node.left, Math.max(maxval, node.val)));
//            }
//            if (node.right != null) {
//                q.offer(new Pair<>(node.right, Math.max(maxval, node.val)));
//            }
//        }
//        return res;
//    }


    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    private int numGoodNodes_1 = 0;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int goodNodes_2(TreeNode root) {
        dfs(root, Integer.MIN_VALUE);
        return numGoodNodes_1;
    }

    private void dfs(TreeNode node, int maxSoFar) {
        if (maxSoFar <= node.val) {
            numGoodNodes_1++;
        }

        if (node.right != null) {
            dfs(node.right, Math.max(node.val, maxSoFar));
        }

        if (node.left != null) {
            dfs(node.left, Math.max(node.val, maxSoFar));
        }
    }


    // V3
    // IDEA : DFS + Iterative
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair {

        public TreeNode node;
        public int maxSoFar;

        /**
         * time = O(N)
         * space = O(H)
         */
        public Pair(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    /**
     * time = O(N)
     * space = O(H)
     */
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

    // V4
    // IDEA : BFS
    // https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
    class Pair2 {
        public TreeNode node;
        public int maxSoFar;

        /**
         * time = O(N)
         * space = O(H)
         */
        public Pair2(TreeNode node, int maxSoFar) {
            this.node = node;
            this.maxSoFar = maxSoFar;
        }
    }

    /**
     * time = O(N)
     * space = O(H)
     */
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
