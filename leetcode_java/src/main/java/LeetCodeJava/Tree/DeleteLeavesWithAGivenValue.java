package LeetCodeJava.Tree;

// https://leetcode.com/problems/delete-leaves-with-a-given-value/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

/**
 * 1325. Delete Leaves With a Given Value
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a binary tree root and an integer target, delete all the leaf nodes with value target.
 *
 * Note that once you delete a leaf node with value target, if its parent node becomes a leaf node and has the value target, it should also be deleted (you need to continue doing that until you cannot).
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: root = [1,2,3,2,null,2,4], target = 2
 * Output: [1,null,3,null,4]
 * Explanation: Leaf nodes in green with value (target = 2) are removed (Picture in left).
 * After removing, new nodes become leaf nodes with value (target = 2) (Picture in center).
 * Example 2:
 *
 *
 *
 * Input: root = [1,3,3,3,2], target = 3
 * Output: [1,3,null,null,2]
 * Example 3:
 *
 *
 *
 * Input: root = [1,2,null,2,null,2], target = 2
 * Output: [1]
 * Explanation: Leaf nodes in green with value (target = 2) are removed at each step.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 3000].
 * 1 <= Node.val, target <= 1000
 *
 */
public class DeleteLeavesWithAGivenValue {

  // V0
  // IDEA: DFS (POST ORDER traverse) (fixed by gpt)
  /**
   *  IDEA:
   *
   * 	- Post-order traversal:
   *        	`children` are processed before checking the current node.
   *
   * 	- No duplicate logic —
   *        	all leaf removal happens in one place (helper).
   *
   * 	- removeLeafNodes just calls the helper so the public method stays clean.
   *
   */
  // https://youtu.be/FqAoYAwbwV8?si=mOdW8Wgj3TJ4AyRL
  /**
   * time = O(N)
   * space = O(H)
   */
  public TreeNode removeLeafNodes(TreeNode root, int target) {
        // edge
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
            if (root.val == target) {
                return null;
            }
            return root;
        }

        // below is OK as well
        //return deleteLeafHelper(root, target);

        TreeNode res = deleteLeafHelper_0(root, target);
        return res;
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode deleteLeafHelper_0(TreeNode root, int target) {
        // edge
        if (root == null) {
            return null;
        }

        /**
         *  NOTE !!!
         *
         *   Post-order traversal
         *
         *   -> so the `op order` is:
         *      left -> right -> root
         */
        /**
         *  NOTE !!!
         *
         * We need to recurse into `the left and right children` first
         * so we can determine if after deleting leaves in those subtrees,
         * the current node becomes a new leaf.
         * Only after we've processed children should we check if this node should be deleted.
         *
         * We'd only ever delete original leaves (nodes that were leaves before recursion),
         * but we'd miss nodes that became leaves as a result of deleting their children.
         *
         */
        /**
         *  EXAMPLE :
         *
         *    input:
         *
         *         1
         *       /
         *      2
         *    /
         *   2
         *
         *
         *
         *  Step-by-step with correct order:
         *
         *   step 1) Call deleteLeafHelper(2) on the lowest 2:
         *
         *      - It's a leaf and equals target → delete → return null
         *
         *   step 2) Move to middle 2:
         *
         *      - After the recursive call, its left child is now null,
         *         so it's a new leaf
         *
         *      - It equals target → delete → return null
         *
         *   step 3) Move to root 1:
         *
         *     - Its left child is now null, so nothing else to delete.
         *
         *
         *
         *  -> ❌ If you check the condition before recursion:
         *     The middle 2 still had a left child when it was checked —
         *     it wouldn't be considered a leaf at that point.
         *     So it wouldn’t be deleted, even though it should’ve been.
         */
        // if current is now a leaf and matches target → delete it
        root.left = deleteLeafHelper_0(root.left, target);
        root.right = deleteLeafHelper_0(root.right, target);

        if (root.val == target && root.left == null && root.right == null) {
            return null;
        }

        /**
         *  NOTE !!!
         *
         *  we need to put below logic (recursively traverse sub tree)
         *  BEFORE `delete logic` (e.g.  `if (root.val == target && root.left == null && root.right == null) ..`)
         *
         *  (reason explained above)
         */
        //        root.left = deleteLeafHelper(root.left, target);
        //        root.right = deleteLeafHelper(root.right, target);

        return root;
    }

    // V0-1
    // IDEA: DFS (fixed by gpt)
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode removeLeafNodes_0_1(TreeNode root, int target) {
        return deleteLeafHelper(root, target);
    }

    private TreeNode deleteLeafHelper(TreeNode root, int target) {
        if (root == null) {
            return null;
        }

        // Recursively process left and right children
        root.left = deleteLeafHelper(root.left, target);
        root.right = deleteLeafHelper(root.right, target);

        // After processing children, check if current node is a target leaf
        if (root.left == null && root.right == null && root.val == target) {
            return null; // remove this leaf
        }

        return root; // keep current node
    }

    // V0-2
    // IDEA: DFS (gpt)
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode removeLeafNodes_0_2(TreeNode root, int target) {
        // edge
        if (root == null) {
            return null;
        }

        // recursively fix children first
        root.left = removeLeafNodes_0_2(root.left, target);
        root.right = removeLeafNodes_0_2(root.right, target);

        // after fixing children, check if current is now a leaf and matches target
        if (root.left == null && root.right == null && root.val == target) {
            return null;
        }

        return root;
    }

    // V1
    // https://youtu.be/FqAoYAwbwV8?si=mOdW8Wgj3TJ4AyRL


    // V2-1
    // https://leetcode.com/problems/delete-leaves-with-a-given-value/editorial/
    // IDEA:  Recursion (Postorder Traversal)
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode removeLeafNodes_2_1(TreeNode root, int target) {
        // Base case
        if (root == null) {
            return null;
        }

        // 1. Visit the left children
        root.left = removeLeafNodes_2_1(root.left, target);

        // 2. Visit the right children
        root.right = removeLeafNodes_2_1(root.right, target);

        // 3. Check if the current node is a leaf node and its value equals target
        if (root.left == null && root.right == null && root.val == target) {
            // Delete the node by returning null to the parent, effectively disconnecting it
            return null;
        }

        // Keep it untouched otherwise
        return root;
    }


    // V2-2
    // https://leetcode.com/problems/delete-leaves-with-a-given-value/editorial/
    // IDEA:  Iterative (PostOrder Traversal)
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode removeLeafNodes_2_2(TreeNode root, int target) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode currentNode = root, lastRightNode = null;

        while (!stack.isEmpty() || currentNode != null) {
            // Push left nodes to the stack until reaching the leftmost node.
            while (currentNode != null) {
                stack.push(currentNode);
                currentNode = currentNode.left;
            }

            // Access the top node on the stack without removing it.
            // This node is the current candidate for processing.
            currentNode = stack.peek();

            // Check if the current node has an unexplored right subtree.
            // If so, shift to the right subtree unless it's the subtree we just visited.
            if (currentNode.right != lastRightNode && currentNode.right != null) {
                currentNode = currentNode.right;
                continue; // Continue in the while loop to push this new subtree's leftmost nodes.
            }

            // Remove the node from the stack, since we're about to process it.
            stack.pop();

            // If the node has no right subtree or the right subtree has already been visited,
            // then check if it is a leaf node with the target value.
            if (currentNode.right == null &&
                    currentNode.left == null &&
                    currentNode.val == target) {
                // If the stack is empty after popping, it means the root was a target leaf node.
                if (stack.isEmpty()) {
                    return null; // The tree becomes empty as the root itself is removed.
                }

                // Identify the parent of the current node.
                TreeNode parent = stack.isEmpty() ? null : stack.peek();

                // Disconnect the current node from its parent.
                if (parent != null) {
                    if (parent.left == currentNode) {
                        parent.left = null; // If current is a left child, set the left pointer to null.
                    } else {
                        parent.right = null; // If current is a right child, set the right pointer to null.
                    }
                }
            }

            // Mark this node as visited by setting 'lastRightNode' to 'currentNode' before moving to the next iteration.
            lastRightNode = currentNode;
            // Reset 'currentNode' to null to ensure the next node from the stack is processed.
            currentNode = null;
        }
        return root; // Return the modified tree.
    }

}
