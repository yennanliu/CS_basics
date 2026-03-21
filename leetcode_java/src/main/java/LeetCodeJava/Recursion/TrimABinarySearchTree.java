package LeetCodeJava.Recursion;

// https://leetcode.com/problems/trim-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  669. Trim a Binary Search Tree
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary search tree and the lowest and highest boundaries as low and high, trim the tree so that all its elements lies in [low, high]. Trimming the tree should not change the relative structure of the elements that will remain in the tree (i.e., any node's descendant should remain a descendant). It can be proven that there is a unique answer.
 *
 * Return the root of the trimmed binary search tree. Note that the root may change depending on the given bounds.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,0,2], low = 1, high = 2
 * Output: [1,null,2]
 * Example 2:
 *
 *
 * Input: root = [3,0,4,null,2,null,null,1], low = 1, high = 3
 * Output: [3,2,null,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 0 <= Node.val <= 104
 * The value of each node in the tree is unique.
 * root is guaranteed to be a valid binary search tree.
 * 0 <= low <= high <= 104
 *
 *
 *
 */
public class TrimABinarySearchTree {

    // V0
//    public TreeNode trimBST(TreeNode root, int low, int high) {
//
//    }

    // V0-1
    // IDEA: `PRE-ORDER` DFS + BST property (fixed by gemini)
    /**  NOTE !!!
     *
     *  1. this is a `pre-order` DFS
     *
     *  2. time = O(N)  -> need to visit all nodes
     *     space = O(H)
     *
     *
     *   --------------
     *
     *   Good questions—this is exactly the kind of nuance interviewers care about for **LeetCode 669 Trim a Binary Search Tree**.
     *
     * ---
     *
     * # 1️⃣ Is this post-order DFS?
     *
     * 👉 **No — it’s *not* post-order. It’s closer to *pre-order*.**
     *
     * ### Why?
     *
     * Look at the structure:
     *
     * ```java
     * if (root.val < low) {
     *     return trimBST(root.right, low, high);
     * }
     *
     * if (root.val > high) {
     *     return trimBST(root.left, low, high);
     * }
     * ```
     *
     * You are:
     *
     * * **processing the current node FIRST**
     * * deciding what to do **before visiting children**
     *
     * That’s the definition of **pre-order behavior**.
     *
     * ---
     *
     * ### But there’s a twist 👇
     *
     * In the “valid node” case:
     *
     * ```java
     * root.left = trimBST(root.left, low, high);
     * root.right = trimBST(root.right, low, high);
     * ```
     *
     * This part *looks like* post-order (children first, then use results).
     *
     * ---
     *
     * ### ✅ Final classification
     *
     * 👉 This is best described as:
     *
     * > **Pre-order DFS with pruning + recursive reconstruction**
     *
     * * Pre-order → because decisions happen at the root first
     * * Not pure post-order → because you sometimes **skip entire subtrees early**
     *
     * ---
     *
     * # 2️⃣ Time & Space Complexity
     *
     * ### ❌ Your guess:
     *
     * > time = O(log N), space = O(N)
     *
     * 👉 Not correct in general.
     *
     * ---
     *
     * ## ✅ Time Complexity
     *
     * ### Worst case:
     *
     * > **O(N)**
     *
     * Why?
     *
     * * You may visit every node once
     * * Example: all nodes are within `[low, high]`
     *
     * ---
     *
     * ### Best case (pruning helps):
     *
     * > **O(H)** (height of tree)
     *
     * * If you keep skipping one side (like a linked list)
     * * For balanced BST: **O(log N)**
     *
     * ---
     *
     * ## ✅ Space Complexity
     *
     * This is about **recursion stack**:
     *
     * ### Worst case:
     *
     * > **O(N)**
     * > (skewed tree)
     *
     * ### Best / average (balanced tree):
     *
     * > **O(log N)**
     *
     * ---
     *
     * ## 📊 Final Answer Summary
     *
     * | Metric | Complexity                           |
     * | ------ | ------------------------------------ |
     * | Time   | **O(N)** worst, **O(log N)** best    |
     * | Space  | **O(N)** worst, **O(log N)** average |
     *
     * ---
     *
     * ## 🧠 Key insight (important for interviews)
     *
     * This problem is fast not because of DFS order, but because of:
     *
     * > **BST property allows pruning entire subtrees**
     *
     * That’s the real optimization—not the traversal type.
     *
     * ---
     *
     * ## 🏁 One-liner takeaway
     *
     * * Traversal: **Pre-order style pruning**
     * * Time: **O(N)** worst
     * * Space: **O(H)** (height of tree)
     *
     */
    /**
     *  time = O(N)
     *  space = O(H)
     */
    public TreeNode trimBST_0_1(TreeNode root, int low, int high) {
        if (root == null) {
            return null;
        }

        // Case 1: Root is too SMALL.
        // The entire left subtree is also too small.
        // We discard root and left, and return whatever remains of the RIGHT subtree.
        if (root.val < low) {
            return trimBST_0_1(root.right, low, high);
        }

        // Case 2: Root is too BIG.
        // The entire right subtree is also too big.
        // We discard root and right, and return whatever remains of the LEFT subtree.
        if (root.val > high) {
            return trimBST_0_1(root.left, low, high);
        }

        // Case 3: Root is in range.
        // We keep the root and recursively trim its children.
        root.left = trimBST_0_1(root.left, low, high);
        root.right = trimBST_0_1(root.right, low, high);

        return root;
    }

    // V0-2
    // IDEA: POST ORDER DFS (gpt)
    public TreeNode trimBST_0_2(TreeNode root, int low, int high) {
        if (root == null)
            return null;

        // process children first
        root.left = trimBST_0_2(root.left, low, high);
        root.right = trimBST_0_2(root.right, low, high);

        // then decide current node
        if (root.val < low) {
            return root.right;
        }

        if (root.val > high) {
            return root.left;
        }

        return root;
    }



    // V0-x
    // IDEA: PRE-ORDER DFS + BST property (fixed by GPT)
    public TreeNode trimBST_0_x(TreeNode root, int low, int high) {
        if (root == null)
            return null;

        // case 1: root too small → discard left subtree
        if (root.val < low) {
            return trimBST_0_x(root.right, low, high);
        }

        // case 2: root too large → discard right subtree
        if (root.val > high) {
            return trimBST_0_x(root.left, low, high);
        }

        // case 3: root is valid → trim children
        root.left = trimBST_0_x(root.left, low, high);
        root.right = trimBST_0_x(root.right, low, high);

        return root;
    }

    // V0-3
    // IDEA: DFS + `parent` + BST (gemini) (NOT recommended)
    // NOTE !!! this version of code is NOT a ideal one,
    //          added here just for learning purpose.
    public TreeNode trimBST_0_3(TreeNode root, int low, int high) {
        TreeNode dummy = new TreeNode(0);
        dummy.left = root;

        trimHelper(root, dummy, true, low, high);

        return dummy.left;
    }

    private void trimHelper(TreeNode node, TreeNode parent, boolean isLeft, int low, int high) {
        if (node == null)
            return;

        // Case 1: node is too small → replace with right subtree
        if (node.val < low) {
            if (isLeft)
                parent.left = node.right;
            else
                parent.right = node.right;

            trimHelper(node.right, parent, isLeft, low, high);
            return;
        }

        // Case 2: node is too large → replace with left subtree
        if (node.val > high) {
            if (isLeft)
                parent.left = node.left;
            else
                parent.right = node.left;

            trimHelper(node.left, parent, isLeft, low, high);
            return;
        }

        // Case 3: node is valid → recurse normally
        trimHelper(node.left, node, true, low, high);
        trimHelper(node.right, node, false, low, high);
    }



    // V1
    // https://leetcode.com/problems/trim-a-binary-search-tree/solutions/1947897/easy-to-understand-with-explanation-0ms-eb6wl/
    public TreeNode trimBST_1(TreeNode root, int L, int R) {
        if (root == null) {
            return null;
        }
        //if the range is correct then checking both root left and right
        if (root.val >= L && root.val <= R) {
            root.left = trimBST_1(root.left, L, R);//Trim the left subtree
            root.right = trimBST_1(root.right, L, R);//Trim the right subtree
        } else if (root.val < L) {
            //if the root val is less than low then getting values from left will be even lower(binary tree rule) so we need to set the root to root.right;
            root = trimBST_1(root.right, L, R);
        } else if (root.val > R) {
            //if the root val is greater than high then getting values from right will be even higher(binary tree rule) so we need to the root to root.left.
            root = trimBST_1(root.left, L, R);
        }
        return root;
    }


    // V2
    // https://leetcode.com/problems/trim-a-binary-search-tree/solutions/1046706/trim-binary-search-tree-detailed-explana-ryxh/
    public TreeNode trimBST_2(TreeNode root, int low, int high) {
        if (root == null)
            return null;
        if (root.val > high)
            return trimBST_2(root.left, low, high);
        if (root.val < low)
            return trimBST_2(root.right, low, high);
        root.left = trimBST_2(root.left, low, high);
        root.right = trimBST_2(root.right, low, high);
        ;
        return root;
    }






}
