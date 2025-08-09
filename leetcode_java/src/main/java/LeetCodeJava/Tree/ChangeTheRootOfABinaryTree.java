package LeetCodeJava.Tree;

// https://leetcode.com/problems/change-the-root-of-a-binary-tree/
// https://leetcode.ca/all/1666.html
// https://leetcode.cn/problems/change-the-root-of-a-binary-tree/

/**
 * 1666. Change the Root of a Binary Tree
 * Given the root of a binary tree and a leaf node, reroot the tree so that the leaf is the new root.
 *
 * You can reroot the tree with the following steps for each node cur on the path starting from the leaf up to the root​​​ excluding the root:
 *
 * If cur has a left child, then that child becomes cur's right child. Note that it is guaranteed that cur will have at most one child.
 * cur's original parent becomes cur's left child.
 * Return the new root of the rerooted tree.
 *
 * Note: Ensure that your solution sets the Node.parent pointers correctly after rerooting or you will receive "Wrong Answer".
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], leaf = 7
 * Output: [7,2,null,5,4,3,6,null,null,null,1,null,null,0,8]
 * Example 2:
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], leaf = 0
 * Output: [0,1,null,3,8,5,null,null,null,6,2,null,null,7,4]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 100].
 * -109 <= Node.val <= 109
 * All Node.val are unique.
 * leaf exist in the tree.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Google
 *
 */
public class ChangeTheRootOfABinaryTree {

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent; // NOTE !!! this is attr
    };

  // V0
  //    public Node flipBinaryTree(Node root, Node leaf) {
  //
  //    }

  // V1
  // IDEA: TREE traverse + `pointer` op (gpt)
  // TODO: validate
  // Complexity
  //	•	Time Complexity: O(h), where h is height of tree (worst-case O(n) for skewed tree).
  //	•	Space Complexity: O(1) — in-place.
  /**
   *  IDEA:
   *
   *  Let’s first restate the key rules:
   *
   * 	•	You’re given a root and a leaf node
   *        (which is guaranteed to be in the tree).
   *
   * 	•	You have to re-root the tree so that leaf becomes the new root.
   *
   *    •	While walking from leaf to root:
   * 	        1.	If cur has a left child, make that child the right child.
   * 	        2.	The original parent of cur becomes cur.left.
   *
   * 	•	You must update both left, right, and parent pointers correctly.
   *
   */
  /**
   *  Example:
   *
   *           3
   *         /   \
   *       5       1
   *     /   \    / \
   *    6     2  0   8
   *         / \
   *        7   4
   *
   * root = 3
   * leaf = 7
   *
   *  ----------------------
   *
   *  ->
   *
   *  	•	Start at leaf = 7.
   * 	•	First step:
   * 	    •	next = 2 (parent of 7).
   * 	    •	7.left = 2 (old parent).
   * 	    •	Detach 2.left (to avoid cycles).
   * 	    •	7.parent = null (it will become new root).
   * 	•	Second step: curr = 2, next = 5 (parent).
   * 	    •	If 2.left != null (but now it’s null), so skip.
   * 	    •	2.left = 5 (old parent).
   * 	    •	Detach 5.right (was 2).
   * 	    •	Update 2.parent = 7.
   *
   * Continue until curr reaches original root.
   * At the end, leaf is new root, and all
   * pointers are correctly set.
   *
   *
   *
   */
  /**
   *  Alright — let’s go end-to-end for the example
   *
   * root = [3,5,1,6,2,0,8,null,null,7,4]
   * leaf = 7
   *
   * The tree with parent pointers looks like:
   *
   *           3
   *         /   \
   *        5     1
   *      /   \   / \
   *     6     2 0   8
   *          / \
   *         7   4
   *
   *
   * ⸻
   *
   * Step 0 – Initial setup
   * 	•	curr = 7
   * 	•	prev = null
   * 	•	next = null (will set later in loop)
   *
   * ⸻
   *
   * Step 1 – Handle node 7
   * 	•	next = curr.parent = 2
   * 	•	curr.left = next → 7.left = 2
   * 	•	Detach from parent:
   * 	•	2.left == curr → set 2.left = null
   * 	•	curr.right = curr.left? No — old 7.left was null, so skip Rule #1.
   * 	•	Set curr.parent = prev → 7.parent = null (new root candidate)
   * 	•	Move:
   * 	•	prev = 7
   * 	•	curr = 2
   *
   * Tree after Step 1:
   *
   *     7
   *    /
   *   2
   *    \
   *     4
   * parent: 2.parent = 7
   *
   * (detached from 5, but 4 remains as 2’s right child)
   *
   * ⸻
   *
   * Step 2 – Handle node 2
   * 	•	next = curr.parent = 5
   * 	•	Rule #1: curr.left is null → skip.
   * 	•	Rule #2: curr.left = next → 2.left = 5
   * 	•	Detach from parent:
   * 	•	5.right == curr → set 5.right = null
   * 	•	curr.parent = prev → 2.parent = 7
   * 	•	Move:
   * 	•	prev = 2
   * 	•	curr = 5
   *
   * Tree after Step 2:
   *
   *       7
   *      /
   *     2
   *    / \
   *   5   4
   * parent: 5.parent = 2
   *
   * (5 still has left child 6, right child null)
   *
   * ⸻
   *
   * Step 3 – Handle node 5
   * 	•	next = curr.parent = 3
   * 	•	Rule #1: curr.left != null (6 exists), so move it to right:
   * 	•	curr.right = curr.left → 5.right = 6
   * 	•	Rule #2: curr.left = next → 5.left = 3
   * 	•	Detach from parent:
   * 	•	3.left == curr → set 3.left = null
   * 	•	curr.parent = prev → 5.parent = 2
   * 	•	Move:
   * 	•	prev = 5
   * 	•	curr = 3
   *
   * Tree after Step 3:
   *
   *       7
   *      /
   *     2
   *    / \
   *   5   4
   *  / \
   * 3   6
   *
   *
   * ⸻
   *
   * Step 4 – Handle node 3 (old root)
   * 	•	next = curr.parent = null → this is the final step.
   * 	•	Rule #1: curr.left is null → skip.
   * 	•	Rule #2: curr.left = next → 3.left = null (no effect).
   * 	•	No detachment needed (no parent).
   * 	•	curr.parent = prev → 3.parent = 5
   * 	•	Move:
   * 	•	prev = 3
   * 	•	curr = null (loop ends)
   *
   * ⸻
   *
   * Final Tree
   *
   *         7
   *        /
   *       2
   *      / \
   *     5   4
   *    / \
   *   3   6
   *    \
   *     1
   *    / \
   *   0   8
   *
   * (with correct parent pointers)
   *
   */
  public Node flipBinaryTree_1(Node root, Node leaf) {
        Node curr = leaf;
        Node prev = null;
        Node next;

        while (curr != null) {
            next = curr.parent; // store original parent

            // Rule 1: If curr has a left child, make it the right child
            if (curr.left != null) {
                curr.right = curr.left;
            }

            // Rule 2: original parent becomes curr.left
            curr.left = next;

            // Prevent cycle: if parent’s left is curr, null it
            if (next != null) {
                if (next.left == curr) {
                    next.left = null;
                } else {
                    next.right = null;
                }
            }

            // Fix parent pointer
            curr.parent = prev;

            // Move up
            prev = curr;
            curr = next;
        }

        return leaf; // new root
    }

    // V2
    // https://leetcode.ca/2020-06-22-1666-Change-the-Root-of-a-Binary-Tree/
    public Node flipBinaryTree_2(Node root, Node leaf) {
        Node cur = leaf;
        Node p = cur.parent;
        while (cur != root) {
            Node gp = p.parent;
            if (cur.left != null) {
                cur.right = cur.left;
            }
            cur.left = p;
            p.parent = cur;
            if (p.left == cur) {
                p.left = null;
            } else if (p.right == cur) {
                p.right = null;
            }
            cur = p;
            p = gp;
        }
        leaf.parent = null;
        return leaf;
    }

    // V3
    // https://github.com/doocs/leetcode/blob/main/solution/1600-1699/1666.Change%20the%20Root%20of%20a%20Binary%20Tree/README_EN.md

}
