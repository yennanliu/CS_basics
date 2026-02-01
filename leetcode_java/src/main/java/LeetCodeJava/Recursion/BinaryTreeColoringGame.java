package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-coloring-game/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;

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

    /**
     *
     * 🧩 Problem Recap
     * 	•	There are n nodes labeled 1..n.
     * 	•	Player 1 colors node x first.
     * 	•	Player 2 colors any other node y.
     * 	•	Then players alternate coloring adjacent uncolored nodes.
     * 	•	The player who colors more nodes wins.
     *
     *   -> We need to check if player 2 can pick a node
     *     so that they can guarantee to win.
     *
     */
    /**
     * 💡 Core Idea
     *
     * When player 1 colors node x, it divides the tree into 3 parts:
     * 	1.	x’s left subtree
     * 	2.	x’s right subtree
     * 	3.	The rest of the tree (the parent side)
     *
     * Player 2 can win if they can choose a node
     * from one of those three parts that has more
     * than half of all nodes (i.e., > n / 2).
     *
     * So the key is:
     * 	•	Use DFS to count the number of nodes in each subtree.
     * 	•	Once we find node x, record:
     * 	•	leftCnt = count(x.left)
     * 	•	rightCnt = count(x.right)
     * 	•	parentCnt = n - (leftCnt + rightCnt + 1)
     * 	•	Player 2 wins if max(leftCnt, rightCnt, parentCnt) > n / 2.
     */
    // V0
    // IDEA : RECURSION + TREE OP (fixed by GPT)
    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean btreeGameWinningMove(TreeNode root, int n, int x) {

        // Get the total count of nodes in the left and right subtree of the node with value x
        // NOTE !!! xNode is the node that player1 color at first move
        TreeNode xNode = findNode(root, x);
        int leftSubtreeCount = getSubNodeCount(xNode.left);
        int rightSubtreeCount = getSubNodeCount(xNode.right);

        // Player 1's count is the sum of nodes in x's left and right subtrees + x itself
        // NOTE !!! below code works as well (V1)
        // V1
        //int player1Cnt = getSubNodeCount(xNode);
        // V2
        int player1Cnt = leftSubtreeCount + rightSubtreeCount + 1;

        /**
         *  NOTE !!! core of the logic:
         *
         *   player 2 can choose either one of below:
         *
         *    1.  The left subtree of x
         *    2.  The right subtree of x
         *    3.  Everything else (which is n - player1Cnt)
         *
         *    -> so for 1, 2, we use `max(leftSubtreeCount, rightSubtreeCount)`
         *    -> for 3, we use `n - player1Cnt`
         *
         *    -> then we get the max val over (1,2) and 3
         *
         *    -> finally check if the `max val` can  > n / 2
         *       (e.g. if player 2 can win)
         *
         */
        // `Player 2` can choose any one of the three options:
        // 1. The left subtree of x
        // 2. The right subtree of x
        // 3. Everything else (which is n - player1Cnt)
        // NOTE !!! : below code get max val from 3 values (e.g. : leftSubtreeCount, rightSubtreeCount, n - player1Cnt)
        int player2MaxCnt = Math.max(
                Math.max(leftSubtreeCount, rightSubtreeCount),
                n - player1Cnt
        );

        /** NOTE !!!
         *
         *   we CAN NOT use `player1MaxCnt < n / 2` to check if player2 can win or not.
         *
         *   -------
         *
         *   Reason:
         *
         *   That's a great question! It highlights the subtle
         *    but crucial difference between Player 1's
         *    starting position and Player 2's winning move.
         *
         *    -> You cannot simply use player1Cnt < n / 2 because
         *    Player 1's initial colored region (player1Cnt) is not fixed.
         *    Player 1's region is whatever remains after Player 2 makes their move.
         *
         *
         *    -> Why player1Cnt < n / 2 is Incorrect ?
         *       The winning condition for Player 2 is that the size of the
         *       region Player 2 claims must be strictly greater than n/2.
         *
         *       Your proposed condition, player1Cnt < n / 2, i
         *       s equivalent to checking if the remaining nodes,
         *       $n - player1Cnt, are greater than n/2.
         *
         *   -> this is wrong because:
         *
         *      1. Player 1's count is only 1 at the start: At the moment
         *         Player 2 chooses a node to sever, Player 1 only controls the node $
         *         x$ itself (size 1). Player 1's region is not the sum of
         *         $x$ and its subtrees until after Player 2 has chosen their region.
         *
         *      2. Player 2 has three choices: Player 2 has the strategic advantage of
         *          breaking the connection to the largest available uncolored region.
         *          These regions are:
         *              -  Left Subtree of $x$ (size: $L$)
         *              - Right Subtree of $x$ (size: $R$)
         *              - Parent Region (size: $P = n - L - R - 1$)
         *
         *     3. The largest region wins:
         *        Player 2 will always choose $\max(L, R, P)$.
         *        Player 2 wins if $\max(L, R, P) > n/2$.
         *
         */
        // Player 2 wins if they can control more than half the nodes
        return player2MaxCnt > n / 2;
    }

    /** NOTE !!!  the helper func find the `1st node player to color` */
    private TreeNode findNode(TreeNode root, int x) {
        if (root == null || root.val == x) return root;
        TreeNode left = findNode(root.left, x);
        if (left != null) return left;
        return findNode(root.right, x);
    }

    /** NOTE !!!  the helper func get the total sub node count` */
    private int getSubNodeCount(TreeNode root) {
        if (root == null) return 0;
        return 1 + getSubNodeCount(root.left) + getSubNodeCount(root.right);
    }



    // V0-1
    // Map to record { node.val : number of nodes in its subtree }
    Map<Integer, Integer> subNodeCntMap = new HashMap<>();

    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean btreeGameWinningMove_0_1(TreeNode root, int n, int x) {
        if (root == null || n == 0) return false;

        // Step 1: build the map {node_val: subtree_count}
        getSubNodeCount_0_1(root);

        // Step 2: get leftCnt, rightCnt, parentCnt for node x
        TreeNode target = findNode_0_1(root, x);
        if (target == null) return false;

        int leftCnt = target.left == null ? 0 : subNodeCntMap.get(target.left.val);
        int rightCnt = target.right == null ? 0 : subNodeCntMap.get(target.right.val);
        int parentCnt = n - (1 + leftCnt + rightCnt);

        // Step 3: if any part > n/2, player 2 can win
        return Math.max(parentCnt, Math.max(leftCnt, rightCnt)) > n / 2;
    }

    /** DFS to compute subtree node count for each node */
    private int getSubNodeCount_0_1(TreeNode node) {
        if (node == null) return 0;
        int left = getSubNodeCount_0_1(node.left);
        int right = getSubNodeCount_0_1(node.right);
        int total = left + right + 1;
        subNodeCntMap.put(node.val, total);
        return total;
    }

    /** Helper to find the node with value x */
    private TreeNode findNode_0_1(TreeNode node, int x) {
        if (node == null) return null;
        if (node.val == x) return node;
        TreeNode left = findNode_0_1(node.left, x);
        if (left != null) return left;
        return findNode_0_1(node.right, x);
    }

    // V0-2
    // IDEA: DFS (gemini)
    // Variables to store the sizes of the Left and Right subtrees of Player 1's node (x).
    private int leftSize;
    private int rightSize;

    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean btreeGameWinningMove_0_2(TreeNode root, int n, int x) {
        // The game requires n >= 3 for a meaningful game, but we handle constraints.
        if (root == null) {
            return false;
        }

        // Step 1: Compute the sizes of the Left and Right subtrees of node x.
        // We use the DFS helper function, starting from the root of the tree.
        // This function will set leftSize and rightSize as a side effect when it hits node x.
        dfsCount(root, x);

        // Step 2: Calculate the size of the three regions Player 2 can sever and claim.

        // Region 1: The Left Subtree of x (leftSize)
        int size1 = leftSize;

        // Region 2: The Right Subtree of x (rightSize)
        int size2 = rightSize;

        // Region 3: The Parent/Upper Region (The rest of the tree)
        // Total nodes (n) - (Size of Left Subtree + Size of Right Subtree + Node x itself)
        int size3 = n - (size1 + size2 + 1);

        // Step 3: Player 2 wins if the largest available region is strictly greater than n/2.
        // Player 2 will greedily choose the region with max size.
        int maxRegionSize = Math.max(size1, Math.max(size2, size3));

        // Note: Integer division n / 2 computes floor(n/2). We need strictly greater than n/2.
        return maxRegionSize > n / 2;
    }

    /**
     * Recursive DFS to count the size of the subtree rooted at 'node'.
     * When node 'x' is found, it updates the global leftSize and rightSize variables.
     * @param node The current node in the traversal.
     * @param x The target node's value (Player 1's choice).
     * @return The total size of the subtree rooted at 'node'.
     */
    private int dfsCount(TreeNode node, int x) {
        if (node == null) {
            return 0;
        }

        // Recursively calculate the size of the left and right subtrees
        int L = dfsCount(node.left, x);
        int R = dfsCount(node.right, x);

        // If the current node is Player 1's chosen node (x), record its subtree sizes.
        if (node.val == x) {
            leftSize = L;
            rightSize = R;
        }

        // Return the total size of the subtree rooted at 'node' (1 + left + right)
        return 1 + L + R;
    }

    // The original getSubNodeCount is now defunct, replaced by the logic in dfsCount.
    // private void getSubNodeCount() { }
    // The original can2ndPlayerWin logic is now handled correctly in btreeGameWinningMove.
    // private boolean can2ndPlayerWin(TreeNode root, TreeNode parant, int n, int x) { }


    // V1
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/367682/simple-clean-java-solution/

    /**
     * When you find the selected node,
     * there are three different paths you can block:
     * left right parent In order to guarantee your win,
     * one of those paths should include more nodes than
     * the sum of other two paths.
     */
    /**
     * time = O(N)
     * space = O(H)
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
    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
    public int countofnodes(TreeNode root){
        if(root==null) return 0;
        return countofnodes(root.left)+countofnodes(root.right)+1;
    }

    // V4
    // https://leetcode.com/problems/binary-tree-coloring-game/solutions/4408523/java-o-n-100-faster-solution/
    /**
     * time = O(N)
     * space = O(H)
     */
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

    // V5
    // IDEA : TREE + RECURSION
    // https://www.youtube.com/watch?v=0MGbvRHYZxc
    // https://zxi.mytechroad.com/blog/tree/leetcode-1145-binary-tree-coloring-game/
    // C++
//    class Solution {
//        public:
//        bool btreeGameWinningMove(TreeNode* root, int n, int x) {
//            int red_left;
//            int red_right;
//            function<int(TreeNode*)> count = [&](TreeNode* node){
//                if (!node) return 0;
//                int l = count(node->left);
//                int r = count(node->right);
//                if (root->val == x) {
//                    red_left = l;
//                    red_right = r;
//                }
//                return 1 + l + r;
//            };
//            count(root);
//            // Color red's parent.
//            if (1 + red_left + red_right <= n / 2) return true;
//            // Color the child that has the larger subtree.
//            return max(red_left, red_right) > n / 2;
//        }
//    };



}
