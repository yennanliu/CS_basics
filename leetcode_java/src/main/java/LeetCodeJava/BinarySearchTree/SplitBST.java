package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/split-bst/description/
// https://leetcode.ca/all/776.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 776. Split BST
 * Given a Binary Search Tree (BST) with root node root, and a target value V, split the tree into two subtrees where one subtree has nodes that are all smaller or equal to the target value, while the other subtree has all nodes that are greater than the target value.  It's not necessarily the case that the tree contains a node with value V.
 *
 * Additionally, most of the structure of the original tree should remain.  Formally, for any child C with parent P in the original tree, if they are both in the same subtree after the split, then node C should still have the parent P.
 *
 * You should output the root TreeNode of both subtrees after splitting, in any order.
 *
 * Example 1:
 *
 * Input: root = [4,2,6,1,3,5,7], V = 2
 * Output: [[2,1],[4,3,6,null,null,5,7]]
 * Explanation:
 * Note that root, output[0], and output[1] are TreeNode objects, not arrays.
 *
 * The given tree [4,2,6,1,3,5,7] is represented by the following diagram:
 *
 *           4
 *         /   \
 *       2      6
 *      / \    / \
 *     1   3  5   7
 *
 * while the diagrams for the outputs are:
 *
 *           4
 *         /   \
 *       3      6      and    2
 *             / \           /
 *            5   7         1
 * Note:
 *
 * The size of the BST will not exceed 50.
 * The BST is always valid and each node's value is different.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Coupang Google
 *
 */
public class SplitBST {

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

    /** NOTE !!!
     *
     *  this is a `split` BST problem, NOT `delete`,
     *  (e.g. CAN'T reuse LC 450 Delete Node in a BST  logic)
     *  so we CAN'T use `delete BST` approach.
     *
     *  -> we should implement a `split` logic instead
     */


    // V0
//    public TreeNode[] splitBST(TreeNode root, int target) {
//    }

    // V0-1
    // IDEA: DFS (PRE-ORDER traverse) (gemini)
    // TODO : validate
    /**  NOTE !!!
     *
     *
     * # 📌 Definition of the return value
     *
     * ```java
     * TreeNode[] res = new TreeNode[2];
     * ```
     *
     * | Index    | Meaning                                        |
     * | -------- | ---------------------------------------------- |
     * | `res[0]` | root of BST containing **all values ≤ target** |
     * | `res[1]` | root of BST containing **all values > target** |
     *
     * So conceptually:
     *
     * ```java
     * return [ smallerTree , largerTree ]
     * ```
     *
     * or
     *
     * ```java
     * return [ <= target , > target ]
     * ```
     *
     * ---
     *
     * # 🌳 Example
     *
     * Original BST
     *
     * ```
     *         4
     *        / \
     *       2   6
     *      / \ / \
     *     1  3 5  7
     * ```
     *
     * target = `2`
     *
     * ---
     *
     * ### Result
     *
     * Tree 1 (`res[0]`) → **≤ 2**
     *
     * ```
     *     2
     *    /
     *   1
     * ```
     *
     * Tree 2 (`res[1]`) → **> 2**
     *
     * ```
     *         4
     *        / \
     *       3   6
     *          / \
     *         5   7
     * ```
     *
     * So the function returns:
     *
     * ```java
     * TreeNode[] result
     * result[0] = root of <=2 tree
     * result[1] = root of >2 tree
     * ```
     *
     * ---
     *
     * # 🧠 Why an array?
     *
     * Because the recursion **needs to return two trees at the same time**.
     *
     * Example recursive call:
     *
     * ```java
     * TreeNode[] res = splitBST(root.right, target);
     * ```
     *
     * Now:
     *
     * ```java
     * res[0] → nodes <= target
     * res[1] → nodes > target
     * ```
     *
     * Then we reconnect nodes accordingly.
     *
     * ---
     *
     * # ⚠️ Important clarification
     *
     * Your guess:
     *
     * ```
     * split tree < target
     * split tree >= target
     * ```
     *
     * is **slightly wrong**.
     *
     * Correct definition in the problem:
     *
     * ```id="oj1lq2"
     * res[0] = nodes <= target
     * res[1] = nodes > target
     * ```
     *
     * This ensures **no nodes are lost**.
     *
     * ---
     *
     * # 🪄 Mental Model (very helpful)
     *
     * Think of the return value like:
     *
     * ```
     * return {
     *    leftPartition,
     *    rightPartition
     * }
     * ```
     *
     * or
     *
     * ```
     * [ allowedSide , overflowSide ]
     * ```
     *
     * ---
     *
     * ✅ **One-line summary**
     *
     * ```
     * TreeNode[0] → BST with values ≤ target
     * TreeNode[1] → BST with values > target
     * ```
     *
     */
    /**  NOTE !!!
     *
     *  res definition:
     *    [ smallerTree , largerTree ]
     *
     *     * TreeNode[0] → BST with values ≤ target
     *     * TreeNode[1] → BST with values > target
     *
     */
    public TreeNode[] splitBST_0_1(TreeNode root, int target) {
        // Base case: an empty tree splits into two empty trees
        if (root == null) {
            /** NOTE !!! the return val */
            return new TreeNode[]{null, null};
        }

        
        /** NOTE !!!
         *
         *   2 cases
         *
         *   - root.val <= target
         *   - root.val > target
         */

        /**  DEMO (dry run)
         *
         * Let’s do a **step-by-step dry run** for LeetCode 776: Split BST.
         *
         * Input:
         *
         * ```
         * root = [4,2,6,1,3,5,7]
         * V = 2
         * ```
         *
         * Tree:
         *
         * ```
         *         4
         *        / \
         *       2   6
         *      / \ / \
         *     1  3 5  7
         * ```
         *
         * Goal:
         *
         * ```
         * res[0] → nodes ≤ 2
         * res[1] → nodes > 2
         * ```
         *
         * ---
         *
         * # Step 1 — Start at root (4)
         *
         * ```
         * splitBST(4,2)
         * ```
         *
         * Check:
         *
         * ```
         * 4 > 2
         * ```
         *
         * So **4 belongs to the right tree**.
         *
         * We must split the **left subtree**.
         *
         * ```
         * res = splitBST(2,2)
         * ```
         *
         * Tree view:
         *
         * ```
         *         4
         *        /
         *       2
         * ```
         *
         * ---
         *
         * # Step 2 — Node 2
         *
         * ```
         * splitBST(2,2)
         * ```
         *
         * Check:
         *
         * ```
         * 2 ≤ 2
         * ```
         *
         * So **2 belongs to the left tree**.
         *
         * Now split the **right subtree of 2**.
         *
         * ```
         * res = splitBST(3,2)
         * ```
         *
         * Tree:
         *
         * ```
         *       2
         *      / \
         *     1   3
         * ```
         *
         * ---
         *
         * # Step 3 — Node 3
         *
         * ```
         * splitBST(3,2)
         * ```
         *
         * Check:
         *
         * ```
         * 3 > 2
         * ```
         *
         * So **3 belongs to the right tree**.
         *
         * Split its **left subtree**.
         *
         * ```
         * res = splitBST(null,2)
         * ```
         *
         * ---
         *
         * # Step 4 — Base case
         *
         * ```
         * splitBST(null,2)
         * ```
         *
         * Return:
         *
         * ```
         * [null , null]
         * ```
         *
         * Meaning:
         *
         * ```
         * <=2 : null
         * >2  : null
         * ```
         *
         * ---
         *
         * # Step 5 — Return to node 3
         *
         * We had:
         *
         * ```
         * res = [null , null]
         * ```
         *
         * Reconnect:
         *
         * ```
         * root.left = res[1]
         * ```
         *
         * So:
         *
         * ```
         * 3.left = null
         * ```
         *
         * Return:
         *
         * ```
         * [ null , 3 ]
         * ```
         *
         * Meaning:
         *
         * ```
         * <=2 tree : null
         * >2 tree  : 3
         * ```
         *
         * ---
         *
         * # Step 6 — Return to node 2
         *
         * We received:
         *
         * ```
         * res = [null , 3]
         * ```
         *
         * Reconnect:
         *
         * ```
         * 2.right = res[0]
         * ```
         *
         * So:
         *
         * ```
         * 2.right = null
         * ```
         *
         * Now:
         *
         * ```
         * <=2 tree root = 2
         * >2 tree root = 3
         * ```
         *
         * Return:
         *
         * ```
         * [2 , 3]
         * ```
         *
         * Tree states:
         *
         * Left tree:
         *
         * ```
         *     2
         *    /
         *   1
         * ```
         *
         * Right tree:
         *
         * ```
         * 3
         * ```
         *
         * ---
         *
         * # Step 7 — Return to node 4
         *
         * We received:
         *
         * ```
         * res = [2 , 3]
         * ```
         *
         * Reconnect:
         *
         * ```
         * 4.left = res[1]
         * ```
         *
         * So:
         *
         * ```
         * 4.left = 3
         * ```
         *
         * Tree becomes:
         *
         * ```
         *         4
         *        / \
         *       3   6
         *          / \
         *         5   7
         * ```
         *
         * Return:
         *
         * ```
         * [2 , 4]
         * ```
         *
         * ---
         *
         * # Final Result
         *
         * ### res[0]  (≤2)
         *
         * ```
         *     2
         *    /
         *   1
         * ```
         *
         * Level order:
         *
         * ```
         * [2,1]
         * ```
         *
         * ---
         *
         * ### res[1]  (>2)
         *
         * ```
         *         4
         *        / \
         *       3   6
         *          / \
         *         5   7
         * ```
         *
         * Level order:
         *
         * ```
         * [4,3,6,null,null,5,7]
         * ```
         *
         * ---
         *
         * # Final Output
         *
         * ```
         * [[2,1],[4,3,6,null,null,5,7]]
         * ```
         *
         * ---
         *
         * # Key Insight
         *
         * The algorithm **does not rebuild the tree**.
         *
         * It only **cuts one edge per recursion**:
         *
         * ```
         * root.right = res[0]
         * or
         * root.left  = res[1]
         * ```
         *
         * This is why the complexity is only:
         *
         * ```
         * O(height of BST)
         * ```
         *
         *
         */
        if (root.val <= target) {
            // Root belongs to the "Small" side.
            // But some of its RIGHT children might still be > target.
            // So we split the right subtree.
            TreeNode[] split = splitBST_0_1(root.right, target);

            // The root's new right child is the "Small" part of the split
            root.right = split[0];

            /**  NOTE !!!
             *
             *  res definition:
             *    [ smallerTree , largerTree ]
             *
             *     * TreeNode[0] → BST with values ≤ target
             *     * TreeNode[1] → BST with values > target
             *
             */
            // Return [current_root_with_updated_right, the_Big_part_from_the_split]
            return new TreeNode[]{root, split[1]};

        } else {
            // Root belongs to the "Big" side.
            // But some of its LEFT children might still be <= target.
            // So we split the left subtree.
            TreeNode[] split = splitBST_0_1(root.left, target);

            // The root's new left child is the "Big" part of the split
            root.left = split[1];

            /**  NOTE !!!
             *
             *  res definition:
             *    [ smallerTree , largerTree ]
             *
             *     * TreeNode[0] → BST with values ≤ target
             *     * TreeNode[1] → BST with values > target
             *
             */
            // Return [the_Small_part_from_the_split, current_root_with_updated_left]
            return new TreeNode[]{split[0], root};
        }
    }


    // V0-2
    // IDEA: (GPT)
    public TreeNode[] splitBST_0_2(TreeNode root, int target) {
        if (root == null) {
            return new TreeNode[]{null, null};
        }

        if (root.val <= target) {
            TreeNode[] res = splitBST_0_2(root.right, target);
            root.right = res[0];
            res[0] = root;
            return res;
        } else {
            TreeNode[] res = splitBST_0_2(root.left, target);
            root.left = res[1];
            res[1] = root;
            return res;
        }
    }



    // V1
    // IDEA : DFS
    // https://leetcode.ca/2018-01-14-776-Split-BST/
    private int t;

    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode[] splitBST_1(TreeNode root, int target) {
        t = target;
        return dfs(root);
    }

    /**
     * time = O(H)
     * space = O(H)
     */
    private TreeNode[] dfs(TreeNode root) {
        if (root == null) {
            return new TreeNode[] {null, null};
        }
        if (root.val <= t) {
            TreeNode[] ans = dfs(root.right);
            root.right = ans[0];
            ans[0] = root;
            return ans;
        } else {
            TreeNode[] ans = dfs(root.left);
            root.left = ans[1];
            ans[1] = root;
            return ans;
        }
    }

    // V2-1
    // IDEA: ITERATIVE
    // https://youtu.be/Qz_I0CqMy_Q?si=XmxkWyjJzRLa475f
    /**  IDEA:
     *
     *  1. setup 2 dummy nodes
     *  2. traverse nodes via `4 -> 2 -> 3 `  way
     *      - root -> left -> right ???
     *
     *  3. `cut tree`
     *     - via remove `root pointer`
     *
     *  // T = O(h)
     *  // S = O(n)
     *
     */
    public TreeNode[] splitBST_2_1(TreeNode root, int V) {
        TreeNode pre = new TreeNode(-1), preP = pre;
        TreeNode succ = new TreeNode(-1), succP = succ;
        while (root != null) {
            if (root.val > V) {
                succP.left = root;
                succP = succP.left;
                root = root.left;
                succP.left = null;
            } else {
                preP.right = root;
                preP = preP.right;
                root = root.right;
                preP.right = null;
            }
        }
        return new TreeNode[]{pre.right, succ.left};
    }


    // V2-1
    // IDEA: RECURSIVE
    // https://youtu.be/Qz_I0CqMy_Q?si=XmxkWyjJzRLa475f
    /**
     *  IDEA:
     *
     * // recursive:
     * // 假设已處理好
     * // splitBST root.left - [2], [3]
     * // splitBST root.right - null, [6]
     * // 把部分subres安插到root上，返回subres
     * // T = O(h)
     * // S = O(n)
     *
     */
    public TreeNode[] splitBST_2_2(TreeNode root, int V) {
        if (root == null) return new TreeNode[]{null, null};
        TreeNode[] res;
        if (root.val <= V) {
            res = splitBST_2_2(root.right, V);
            root.right = res[0];
            res[0] = root;
        } else {
            res = splitBST_2_2(root.left, V);
            root.left = res[1];
            res[1] = root;
        }
        return res;
    }



    // V3
    // IDEA : DFS (gpt)
    // TODO : validate
    /**
     * Explanation
     * 	1.	Base Case:
     * 	    •	If the root is null, return two null subtrees because there's nothing to split.
     * 	2.	Recursive Case:
     * 	    •	If the root's value is  <= V :
     * 	            •	Keep the root as part of the left subtree.
     * 	            •	Recursively split the right child since nodes greater than  V  might be in the right subtree.
     * 	    •	If the root's value is  > V :
     * 	            •	Keep the root as part of the right subtree.
     * 	            •	Recursively split the left child since nodes  <= V  might be in the left subtree.
     * 	3.	Reassign Children:
     * 	    •	After the recursive split, adjust the child pointers of the root node to maintain the BST structure.
     * 	4.	Result:
     * 	    •	The first element in the result array is the root of the subtree  <= V .
     * 	    •	The second element is the root of the subtree  > V .
     *      Time Complexity
     * 	        •	Each node is visited exactly once, so the time complexity is  O(n) , where  n  is the number of nodes in the BST.
     *      Space Complexity
     * 	        •	The space complexity is  O(h) , where  h  is the height of the tree, due to the recursive stack.
     * time = O(H)
     * space = O(H)
     */
    public TreeNode[] splitBST_3(TreeNode root, int V) {
        // Base case: If the root is null, both subtrees are null
        if (root == null) {
            return new TreeNode[]{null, null};
        }

        // Array to hold the two resulting subtrees
        TreeNode[] result = new TreeNode[2];

        // If the root's value is less than or equal to V
        if (root.val <= V) {
            // Recursively split the right subtree
            TreeNode[] splitRight = splitBST_3(root.right, V);

            // Root becomes the head of the left subtree
            result[0] = root;
            root.right = splitRight[0]; // Reassign the right child to the left subtree
            result[1] = splitRight[1]; // Right subtree from the split becomes the second result
        } else {
            // If root's value is greater than V, recursively split the left subtree
            TreeNode[] splitLeft = splitBST_3(root.left, V);

            // Root becomes the head of the right subtree
            result[1] = root;
            root.left = splitLeft[1]; // Reassign the left child to the right subtree
            result[0] = splitLeft[0]; // Left subtree from the split becomes the first result
        }

        return result;
    }


    // V4
    // https://www.cnblogs.com/grandyang/p/8993143.html




}
