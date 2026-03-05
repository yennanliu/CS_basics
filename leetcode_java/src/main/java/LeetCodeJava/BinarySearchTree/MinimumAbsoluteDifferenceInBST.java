package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/minimum-absolute-difference-in-bst/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 530. Minimum Absolute Difference in BST
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a Binary Search Tree (BST), return the minimum absolute difference between the values of any two different nodes in the tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,6,1,3]
 * Output: 1
 * Example 2:
 *
 *
 * Input: root = [1,0,48,null,null,12,49]
 * Output: 1
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 104].
 * 0 <= Node.val <= 105
 *
 *
 * Note: This question is the same as 783: https://leetcode.com/problems/minimum-distance-between-bst-nodes/
 *
 *
 */
public class MinimumAbsoluteDifferenceInBST {

    // V0
    // IDEA: DFS
    List<Integer> nodes = new ArrayList<>();

    /**

     * time = O(N)

     * space = O(N)
     */


    public int getMinimumDifference(TreeNode root) {
        // edge
        if (root == null) {
            return -1;
        }
        if (root.left == null && root.right == null) {
            return -1;
        }

        getNodesDFS(root);

        // sort (small -> big)
        Collections.sort(nodes);
        //System.out.println(">>> nodes = " + nodes);

        int minDiff = Integer.MAX_VALUE;

        for (int i = 0; i < nodes.size() - 1; i++) {
            int diff = Math.abs(nodes.get(i + 1) - nodes.get(i));
            minDiff = Math.min(minDiff, diff);
        }

        return minDiff;
    }

    private void getNodesDFS(TreeNode root) {
        if (root == null) {
            return;
        }
        nodes.add(root.val);
        getNodesDFS(root.left);
        getNodesDFS(root.right);
    }


    // V0-0-1
    // IDEA: BST PROPERTY (gemini)
    // Member variables to track the previous value and the result
    private Integer prev = null;
    private int minDiff = Integer.MAX_VALUE;

    public int getMinimumDifference_0_0_1(TreeNode root) {
        // Reset variables for each call (important for LeetCode's persistence)
        prev = null;
        minDiff = Integer.MAX_VALUE;

        inOrder_0_0_1(root);
        return minDiff;
    }

    /** NOTE !!!
     *
     *   1. we use `in-order` traverse,  (pre, post order may work, but NOT straightforward)
     *   2. we get `diff` via cur node and prev node
     *   3. we can also do it in `right -> cur -> left ` node order
     *
     */
    private void inOrder_0_0_1(TreeNode node) {
        if (node == null) {
            return;
        }

        // 1. Left subtree
        inOrder_0_0_1(node.left);

        // 2. Current node
        if (prev != null) {
            // Since it's a BST, in-order values are sorted
            minDiff = Math.min(minDiff, node.val - prev);
        }
        prev = node.val;

        // 3. Right subtree
        inOrder_0_0_1(node.right);
    }


    // V0-1
    // IDEA: BFS + SORT
    /**
     * time = O(N)
     * space = O(N)
     */

    public int getMinimumDifference_0_1(TreeNode root) {
        if (root == null) {
            return -1; // ????
        }
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        List<Integer> list = new ArrayList<>();
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            list.add(node.val);
            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }
        //System.out.println(">>> list = " + list);
        // sort
        Collections.sort(list);
        int minDiff = Integer.MAX_VALUE;
        for (int i = 1; i < list.size(); i++) {
            minDiff = Math.min(minDiff, Math.abs(list.get(i) - list.get(i - 1)));
        }

        return minDiff;
    }


    // V0-3
    // IDEA: BFS + SORT
    // time: O(N + N * logN)
    // space: O(N)
    public int getMinimumDifference_0_3(TreeNode root) {
        // edge
        if (root == null) {
            return -1; // ???
        }
        if (root.right == null && root.left == null) {
            return 0;
        }
        // BFS
        List<Integer> list = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode cur = q.poll();
                list.add(cur.val);
                if (cur.left != null) {
                    q.add(cur.left);
                }
                if (cur.right != null) {
                    q.add(cur.right);
                }
            }
        }

        // sort
        Collections.sort(list);
        int midDiff = Integer.MAX_VALUE;
        for (int i = 1; i < list.size(); i++) {
            midDiff = Math.min(list.get(i) - list.get(i - 1), midDiff);
        }

        return midDiff;
    }



    // V1
    // https://leetcode.ca/2017-05-13-530-Minimum-Absolute-Difference-in-BST/
    // IDEA: DFS
    private int ans;
    private int prev_1;
    private int inf = Integer.MAX_VALUE;

    /**

     * time = O(N)

     * space = O(H)
     */


    public int getMinimumDifference_1(TreeNode root) {
        ans = inf;
        prev_1 = inf;
        dfs(root);
        return ans;
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        ans = Math.min(ans, Math.abs(root.val - prev_1));
        prev_1 = root.val;
        dfs(root.right);
    }

    // V2
    // https://leetcode.com/problems/minimum-absolute-difference-in-bst/solutions/7207279/minimum-absolute-difference-in-bst-2ms-j-pxeo/
    List<Integer> list;
    /**
     * time = O(N)
     * space = O(N)
     */

    public int getMinimumDifference_2(TreeNode root) {
        list = new ArrayList<>();
        inOrder(root, list);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < list.size() - 1; i = i + 1) {
            int diff = Math.abs(list.get(i + 1) - list.get(i));
            min = Math.min(diff, min);
        }
        return min;
    }

    /**

     * time = O(N)

     * space = O(H)
     */


    public void inOrder(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        inOrder(node.left, list);
        list.add(node.val);
        inOrder(node.right, list);
    }

    // V3
    // https://leetcode.com/problems/minimum-absolute-difference-in-bst/solutions/6961212/minimum-absolute-difference-in-bst-by-sh-xlgt/
    TreeNode pre = null;
    int min = Integer.MAX_VALUE;
    /**

     * time = O(N)

     * space = O(H)
     */

    public int getMinimumDifference_3(TreeNode root) {
        if (root == null)
            return 0;
        inOrder(root);
        return min;
    }

    private void inOrder(TreeNode root) {
        if (root == null)
            return;

        // Visit left subtree
        inOrder(root.left);

        // Process current node
        if (pre != null) {
            min = Math.min(min, Math.abs(pre.val - root.val));
        }
        pre = root;

        // Visit right subtree
        inOrder(root.right);
    }




}
