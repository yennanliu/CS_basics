package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-coloring-game/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *
 * 1145. Binary Tree Coloring Game
 * Medium
 * Topics
 * Companies
 * Hint
 * Two players play a turn based game on a binary tree. We are given the root of this binary tree, and the number of nodes n in the tree. n is odd, and each node has a distinct value from 1 to n.
 *
 * Initially, the first player names a value x with 1 <= x <= n, and the second player names a value y with 1 <= y <= n and y != x. The first player colors the node with value x red, and the second player colors the node with value y blue.
 *
 * Then, the players take turns starting with the first player. In each turn, that player chooses a node of their color (red if player 1, blue if player 2) and colors an uncolored neighbor of the chosen node (either the left child, right child, or parent of the chosen node.)
 *
 * If (and only if) a player cannot choose such a node in this way, they must pass their turn. If both players pass their turn, the game ends, and the winner is the player that colored more nodes.
 *
 * You are the second player. If it is possible to choose such a y to ensure you win the game, return true. If it is not possible, return false.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5,6,7,8,9,10,11], n = 11, x = 3
 * Output: true
 * Explanation: The second player can choose the node with value 2.
 * Example 2:
 *
 * Input: root = [1,2,3], n = 3, x = 1
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is n.
 * 1 <= x <= n <= 100
 * n is odd.
 * 1 <= Node.val <= n
 * All the values of the tree are unique.
 */
public class BinaryTreeColoringGame {

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
    // TODO : implement
//    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {
//
//    }

    // V1
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/367682/simple-clean-java-solution/
    /**
     * When you find the selected node,
     * there are three different paths you can block:
     * left right parent In order to guarantee your win,
     * one of those paths should include more nodes than
     * the sum of other two paths.
     */
    public boolean btreeGameWinningMove_1(TreeNode root, int n, int x) {
        if(root == null) return false;

        if(root.val == x){
            int left = count(root.left);
            int right = count(root.right);
            int parent = n - (left+right+1);

            return parent > (left + right) || left > (parent + right) || right > (left + parent);
        }

        return btreeGameWinningMove_1(root.left, n, x) || btreeGameWinningMove_1(root.right, n, x);
    }

    private int count(TreeNode node){
        if(node == null) return 0;
        return count(node.left) + count(node.right) + 1;
    }

    // V2
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/491887/java-solution-beats-100-time-break-the-problem-down-into-two-easier-problems/
    // When you find the selected node, there are three different paths you can block: left ,right or parent
    // if i color left(a) node blue, then red person can color all right and parent nodes red
    // if i color right (b) node blue, then red person can color all left and parent nodes red
    // if i color parent(c) node blue, then red person can color all right and left nodes red
    // so i can only win if i choose a node which contains elements greater than the count of elements in otther 2 nodes i.e a>b+c i color a node blue, or b>a+c i color b node blue , or c>a+b i color c node blue
    // if none of these 3 conditions hold true then red person wins bcoz he has more nodes to color
    int count;
    public boolean btreeGameWinningMove_2(TreeNode root, int n, int x) {
        if (root == null)
            return false;

        // find the node with value x
        TreeNode target = search(root, x);

        // separate the tree into three parts around the target: left subtrees, right subtrees, and others
        int left = subtreeCount(target.left);
        int right = subtreeCount(target.right);
        int remain = n - left - right - 1;

        // return true if the count of any two part is greater than the count of the third part
        return (remain > left + right + 1) || (left > remain + right + 1) || (right > left + remain + 1);
    }

    private TreeNode search(TreeNode root, int x) {
        if (root == null)
            return null;

        if (root.val == x)
            return root;

        TreeNode left = search(root.left, x);
        TreeNode right = search(root.right, x);
        if (left == null && right == null)
            return null;
        else if (left != null)
            return left;
        return right;
    }

    private int subtreeCount(TreeNode root) {
        if (root == null)
            return 0;

        int leftNodes = subtreeCount(root.left);
        int rightNodes = subtreeCount(root.right);
        return leftNodes + rightNodes + 1;
    }

    // V3
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/1700137/java-explained-beginner-friendly/
    public boolean btreeGameWinningMove_3(TreeNode root, int n, int x) {
        if(root==null) return false;
        if(root.val==x){
            int a=countofnodes(root.left);
            int b=countofnodes(root.right);
            int c=n-a-b-1; // 1 for root
            if(a>b+c || b>c+a || c>a+b) return true;
            return false;
        }
        boolean left=btreeGameWinningMove_3(root.left,n,x);
        if(left) return true;
        boolean right=btreeGameWinningMove_3(root.right,n,x);
        if(right) return true;

        return false;
    }
    public int countofnodes(TreeNode root){
        if(root==null) return 0;
        return countofnodes(root.left)+countofnodes(root.right)+1;
    }

    // V4
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/4408523/java-o-n-100-faster-solution/
    public boolean btreeGameWinningMove_4(TreeNode root, int n, int x) {
        TreeNode[] red = new TreeNode[1];
        findNode(root, x, red);
        int[] countLeft = new int[1], countRight = new int[1];
        countNodes(red[0].left, countLeft);
        countNodes(red[0].right, countRight);

        int l = countLeft[0], r = countRight[0];

        return l > n - l || r > n - r || l + r + 1 < n - l - r - 1;
    }

    private void countNodes(TreeNode node, int[] count) {
        if (node == null) return;

        count[0]++;
        countNodes(node.left, count);
        countNodes(node.right, count);
    }

    private void findNode(TreeNode node, int x, TreeNode[] res) {
        if (node == null) return;

        if (node.val == x) {
            res[0] = node;
            return;
        }

        findNode(node.left, x, res);
        findNode(node.right, x, res);
    }
    
}
