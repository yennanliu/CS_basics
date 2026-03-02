package LeetCodeJava.Tree;

// https://leetcode.com/problems/insert-into-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  NOTE !!!
 *
 *   what if `duplicated val` in node is allowed ?
 *
 *   e.g.
 *
 *     It is guaranteed that the new value does not exist in the original BST.
 *
 *
 */
public class InsertIntoABinarySearchTreeFollowup {

    // V0
//    public TreeNode insertIntoBST(TreeNode root, int val) {
//
//    }

    // V1
    // IDEA: BST (gemini)
    /**
     * If the BST **could** contain duplicates, you have to decide on a convention: do duplicates go to the **left** or the **right**?
     *
     * Standard BST implementations usually pick one and stick to it. Most commonly, duplicates are placed in the **left** subtree (meaning $node.left \le node.val$).
     *
     * ### 🛠️ Updated Java Solution (Handling Duplicates)
     *
     * ---
     *
     * ### 🔍 Key Changes for Duplicates
     *
     * * **Logic Change**: In your previous code, we assumed `val` was always different. Now, we use `val > root.val` for the right side and `else` (which covers `val <= root.val`) for the left.
     * * **Consistency**: As long as you are consistent (always left or always right), the tree remains a valid BST.
     * * **Search Impact**: If you search for a value that has duplicates, you can no longer stop at the first match if you need to find *all* instances; you would have to keep checking the left subtree.
     *
     * ### 📊 Complexity (Still in terms of $N$)
     *
     * | Complexity | Worst Case | Average Case |
     * | --- | --- | --- |
     * | **Time** | **$O(N)$** | **$O(\log N)$** |
     * | **Space** | **$O(N)$** | **$O(\log N)$** |
     *
     * > **Note**: If you insert a long string of the **exact same value** (e.g., insert `5` five times), the tree will become a "skewed" line to the left, resulting in **$O(N)$** time for the next insertion.
     *
     * ---
     *
     * ### 💡 Alternative: The "Counter" Approach
     *
     * In some production systems (like databases), instead of adding a new node for a duplicate, we add a `count` variable to the `TreeNode` class:
     *
     * ```java
     * class TreeNode {
     *     int val;
     *     int count; // Number of times this value exists
     *     TreeNode left, right;
     * }
     *
     * ```
     *
     */
    public TreeNode insertIntoBST_1(TreeNode root, int val) {
        // 1. Base Case: Found the insertion spot
        if (root == null) {
            return new TreeNode(val);
        }

        // 2. BST Property: If new value is strictly GREATER than current
        if (val > root.val) {
            root.right = insertIntoBST_1(root.right, val);
        }
        // 3. Handling Duplicates: If value is LESS THAN or EQUAL TO current
        // We treat duplicates as part of the left subtree logic
        else {
            root.left = insertIntoBST_1(root.left, val);
        }

        return root;
    }


    // V2-1
    // IDEA: BST (gpt)
    public TreeNode insertIntoBST_2_1(TreeNode root, int val) {

        if (root == null) {
            return new TreeNode(val);
        }

        if (val < root.val) {
            root.left = insertIntoBST_2_1(root.left, val);
        } else { // val >= root.val  (duplicates go right)
            root.right = insertIntoBST_2_1(root.right, val);
        }

        return root;
    }

    // V2-2
    // IDEA: BST (gpt)
    public TreeNode insertIntoBST_2_2(TreeNode root, int val) {

        if (root == null) {
            return new TreeNode(val);
        }

        if (val <= root.val) { // duplicates go left
            root.left = insertIntoBST_2_2(root.left, val);
        } else {
            root.right = insertIntoBST_2_2(root.right, val);
        }

        return root;
    }

    // V2-3
    // IDEA: BST (gpt)
    class TreeNode {
        int val;
        int count;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
            this.count = 1;
        }
    }

    public TreeNode insertIntoBST_2_3(TreeNode root, int val) {

        if (root == null) {
            return new TreeNode(val);
        }

        if (val < root.val) {
            root.left = insertIntoBST_2_3(root.left, val);
        } else if (val > root.val) {
            root.right = insertIntoBST_2_3(root.right, val);
        } else {
            root.count++; // duplicate found
        }

        return root;
    }




}
