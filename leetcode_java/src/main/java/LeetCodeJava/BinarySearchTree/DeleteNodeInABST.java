package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/delete-node-in-a-bst/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  450. Delete Node in a BST
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a root node reference of a BST and a key, delete the node with the given key in the BST. Return the root node reference (possibly updated) of the BST.
 *
 * Basically, the deletion can be divided into two stages:
 *
 * Search for a node to remove.
 * If the node is found, delete the node.
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,3,6,2,4,null,7], key = 3
 * Output: [5,4,6,2,null,null,7]
 * Explanation: Given key to delete is 3. So we find the node with value 3 and delete it.
 * One valid answer is [5,4,6,2,null,null,7], shown in the above BST.
 * Please notice that another valid answer is [5,2,6,null,4,null,7] and it's also accepted.
 *
 * Example 2:
 *
 * Input: root = [5,3,6,2,4,null,7], key = 0
 * Output: [5,3,6,2,4,null,7]
 * Explanation: The tree does not contain a node with value = 0.
 * Example 3:
 *
 * Input: root = [], key = 0
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 104].
 * -105 <= Node.val <= 105
 * Each node has a unique value.
 * root is a valid binary search tree.
 * -105 <= key <= 105
 *
 *
 * Follow up: Could you solve it with time complexity O(height of tree)?
 *
 */
public class DeleteNodeInABST {

  // V0
  //    public TreeNode deleteNode(TreeNode root, int key) {
  //
  //    }

  // V0-1
  // IDEA: DFS + BST property
  /**
   *
   *  (for key val found)
   *
   *  // Case 1: No children
   *
   *  // Case 2: One child
   *
   *  // Case 3: Two children
   *
   */
  public TreeNode deleteNode_0_1(TreeNode root, int key) {
      // edge
      if (root == null)
          return root;

      // NOTE !!!  root.val too big, find left
      if (key < root.val)
          root.left = deleteNode_0_1(root.left, key);
      // NOTE !!!  root.val too small, find right
      else if (key > root.val)
          root.right = deleteNode_0_1(root.right, key);
      // NOTE !!! if `found`
      else {
          // Case 1: No children
          if (root.left == null && root.right == null)
              return null;

          // Case 2: One child
          if (root.left == null || root.right == null)
              return (root.left != null) ? root.left : root.right;

          // Case 3: Two children
          /**
           *  NOTE !!!
           *
           *   step 1) move `1 step` to sub left
           *   step 2) move `till the right most` (move to sub right till reach the `end`)
           */
          TreeNode temp = root.left;
          while (temp.right != null){
              temp = temp.right;
          }
          /**
           *  NOTE !!!
           *
           *  re-assign root val as tmp.val
           */
          root.val = temp.val;
          /**
           *  NOTE !!!!!!
           *
           *   for root.left,  we update it via recursion call
           *
           *   -> NOTE !!!  pass `root.left` as node parameter
           *                pass `tmp.val` as `to-delete target` parameter
           *
           */
          root.left = deleteNode_0_1(root.left, temp.val);
      }
      return root;
  }

  // V0-2
  // IDEA: DFS + BST property (gpt)
  public TreeNode deleteNode_0_2(TreeNode root, int key) {
      return deleteNodeHelper(root, key);
  }

    private TreeNode deleteNodeHelper(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        if (key < root.val) {
            // search in left subtree
            root.left = deleteNodeHelper(root.left, key);
        } else if (key > root.val) {
            // search in right subtree
            root.right = deleteNodeHelper(root.right, key);
        } else {
            // Found the node to delete

            // Case 1: No left child
            if (root.left == null) {
                return root.right;
            }

            // Case 2: No right child
            if (root.right == null) {
                return root.left;
            }

            // Case 3: Two children → find inorder successor
            TreeNode minNode = findMin(root.right);
            root.val = minNode.val; // copy value
            root.right = deleteNodeHelper(root.right, minNode.val); // delete successor
        }

        return root;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

  // V1-1
  // https://youtu.be/LFzAoJJt92M?feature=shared
  // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0450-delete-node-in-a-bst.java
  /**
   *
   *  Summary of Deletion Strategy:
   *  
   *
   *  | Case         | Description        | What Happens                                  |
   * |--------------|--------------------|-----------------------------------------------|
   * | Leaf         | No children         | Return `null`                                 |
   * | One Child    | One child           | Replace node with its child                   |
   * | Two Children | Both children       | Replace with in-order successor, then delete the successor |
   *
   */
  public TreeNode deleteNode_1_1(TreeNode root, int key) {
        // edge (base case)
        if (root == null)
            return null;

        // case 1) key > root.val
        /** NOTE !!!
         *
         *  if `key > root.val`
         *   -> move to `right` node, and keep running the `delete node op`
         *   -> NEED to connect root.right to the recursive call result
         *      e.g. root.right = deleteNode_1_1(..)
         */
        if (key > root.val) {
            root.right = deleteNode_1_1(root.right, key);
        }
        // case 2) key < root.val
        /** NOTE !!!
         *
         *  if `key < root.val`
         *   -> move to `left` node, and keep running the `delete node op`
         *   -> NEED to connect root.left to the recursive call result
         *      e.g. root.left = deleteNode_1_1(..)
         */
        else if (key < root.val) {
            root.left = deleteNode_1_1(root.left, key);
        }
        // case 3) key == root.val (the `to_delete` node is found)
        else {
            // case 3-1) left sub node is null
            if (root.left == null) {
                return root.right;
            }
            // case 3-2) right sub node is null
            else if (root.right == null) {
                return root.left;
      }
      // case 3-3) BOTH sub left, right sub node are NOT null
      /**
       *  	•	When a node has both children, we:
       *
       * 	  •	Find the in-order successor (the `smallest` node in the right subtree).
       * 	  •	`Copy` its value to the `current node`.
       * 	  •	`Recursively` DELETE that `successor` in the right subtree.
       *
       *   ->  This preserves the BST property because the in-order successor
       *       is always the next-larger node.
       *
       */
      else {
                TreeNode minVal = minimumVal(root.right);
                root.val = minVal.val;
                root.right = deleteNode_1_1(root.right, minVal.val);
            }
        }

        /** NOTE !!! DON'T forget to return root as final result */
        return root;
    }

    // helper func
    public TreeNode minimumVal(TreeNode root) {
        TreeNode curr = root;
        while (curr != null && curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    // V2
    // https://leetcode.com/problems/delete-node-in-a-bst/solutions/6532656/simple-solution-in-java-baet-100-users-b-70nb/
    public TreeNode deleteNode_2(TreeNode root, int key) {
        if (root == null)
            return null;
        if (root.val == key)
            return linker(root);
        TreeNode dummy = root;
        while (root != null) {
            if (root.val > key) {
                if (root.left != null && root.left.val == key) {
                    root.left = linker(root.left);
                    break;
                } else
                    root = root.left;
            } else {
                if (root.right != null && root.right.val == key) {
                    root.right = linker(root.right);
                    break;
                } else
                    root = root.right;
            }
        }
        return dummy;
    }

    public TreeNode linker(TreeNode root) {
        if (root.left == null)
            return root.right;
        if (root.right == null)
            return root.left;
        TreeNode rightChild = root.right;
        TreeNode extremeRight = f(root.left);
        extremeRight.right = rightChild;
        return root.left;
    }

    public TreeNode f(TreeNode root) {
        if (root.right == null)
            return root;
        return f(root.right);
    }

    // V3
    public TreeNode deleteNode_3(TreeNode root, int key) {
        //search
        if (root == null) {
            return null;
        }
        if (root.val < key) {
            root.right = deleteNode_3(root.right, key);
        } else if (root.val > key) {
            root.left = deleteNode_3(root.left, key);
        } else {
            //case 1: leaf node
            if (root.left == null && root.right == null) {
                return null;
            }
            //case 2 : one child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            //case 3 : two children
            TreeNode IS = findInOrderSuccessor(root.right);
            root.val = IS.val;

            root.right = deleteNode_3(root.right, IS.val);
        }
        return root;

    }

    private TreeNode findInOrderSuccessor(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    // V4
    // https://leetcode.com/problems/delete-node-in-a-bst/solutions/6504555/full-explanation-100-beat-python-c-java-1kcmz/
    public TreeNode deleteNode_4(TreeNode root, int key) {
        if (root == null)
            return root;

        if (key < root.val)
            root.left = deleteNode_4(root.left, key);
        else if (key > root.val)
            root.right = deleteNode_4(root.right, key);
        else {
            // Case 1: No children
            if (root.left == null && root.right == null)
                return null;

            // Case 2: One child
            if (root.left == null || root.right == null)
                return (root.left != null) ? root.left : root.right;

            // Case 3: Two children
            TreeNode temp = root.left;
            while (temp.right != null)
                temp = temp.right;
            root.val = temp.val;
            root.left = deleteNode_4(root.left, temp.val);
        }
        return root;
    }


}
