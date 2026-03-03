package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/delete-node-in-a-bst/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 450. Delete Node in a BST
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
 * Example 1:
 * Input: root = [5,3,6,2,4,null,7], key = 3
 * Output: [5,4,6,2,null,null,7]
 *
 * Example 2:
 * Input: root = [5,3,6,2,4,null,7], key = 0
 * Output: [5,3,6,2,4,null,7]
 *
 * Example 3:
 * Input: root = [], key = 0
 * Output: []
 *
 * Constraints:
 * The number of nodes in the tree is in the range [0, 104].
 * -105 <= Node.val <= 105
 * Each node has a unique value.
 * root is a valid binary search tree.
 * -105 <= key <= 105
 *
 * Follow up: Could you solve it with time complexity O(height of tree)?
 */
public class DeleteNodeInABST {

    // V0
    // IDEA: DFS + BST property
    /**
     * (when found a node to delete)
     * // Case 1: No children
     * // Case 2: One child
     * // Case 3: Two children
     *
     * Summary of Deletion Strategy:
     * | Case         | Description        | What Happens                                  |
     * |--------------|--------------------|-----------------------------------------------|
     * | Leaf         | No children         | Return `null`                                 |
     * | One Child    | One child           | Replace node with its child                   |
     * | Two Children | Both children       | Replace with in-order successor, then delete the successor |
     *
     * `in-order successor`: Left → root → Right
     *    time = O(H)
     *    space = O(H)
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        return deleteNodeHelper_0(root, key);
    }

    private TreeNode deleteNodeHelper_0(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        /**
         * CASE 1) NOT found a node to delete
         */
        if (key < root.val) {
            // search in left subtree
            /**
             * NOTE !!!
             * we assign `left sub tree` as res from deleteNodeHelper_0(root.left, key)
             * -> NOT return `deleteNodeHelper_0(root.left, key)`
             * as res directly, since it deleteNodeHelper_0
             * could NOT be a null val, we need it to assign root.left,
             * so we can keep `whole BST info`
             */
            root.left = deleteNodeHelper_0(root.left, key);
        } else if (key > root.val) {
            // search in right subtree
            root.right = deleteNodeHelper_0(root.right, key);
        }
        /**
         * CASE 2) Found a node to delete
         */
        else {
            // Case 1: No left child
            if (root.left == null) {
                return root.right;
            }

            // Case 2: No right child
            if (root.right == null) {
                return root.left;
            }

            /**
             * NOTE !!!! below
             * step 1) find `min` val (`sub right tree`)
             * step 2) set root val as min val
             * step 3) delete the `min` val node from sub right tree
             * - `recursively` call `deleteNodeHelper`
             */
            // Case 3: Two children → find inorder successor
            TreeNode minNode = findMin_0(root.right);
            root.val = minNode.val; // copy value
            root.right = deleteNodeHelper_0(root.right, minNode.val); // delete successor
        }
        return root;
    }

    private TreeNode findMin_0(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    // V0-0-1
    // IDEA: BST + DFS (fixed by gemini)
    /**
     * ### 📊 Complexity Analysis
     *
     * #### **Time Complexity: $O(H)$**
     *
     * * $H$ is the height of the tree.
     * * In the **average case** (balanced tree), this is **$O(\log N)$**.
     * * In the **worst case** (skewed tree), this is **$O(N)$**.
     * * **Why?** We first spend $O(H)$ to find the node. If it has two children, we spend another $O(H)$ to find the successor and delete it. Since these are sequential or nested within the same path, the total time remains proportional to the height.
     *
     * #### **Space Complexity: $O(H)$**
     *
     * * This is the space used by the **recursion stack**.
     * * **Average Case**: $O(\log N)$.
     * * **Worst Case**: $O(N)$ for a skewed tree.
     * * **Why?** Each recursive call adds a frame to the stack until we reach the node to be deleted or a leaf.
     *
     *
     */
    public TreeNode deleteNode_0_0_1(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        if (root.val == key) {
            // Case 1: No children or Case 2: One child (Right)
            if (root.left == null) {
                return root.right;
            }
            // Case 2: One child (Left)
            else if (root.right == null) {
                return root.left;
            }
            /** NOTE !!!  below */
            // Case 3: Two children
            else {
                /** NOTE !!!
                 *
                 *   1 go right sub node, and find `left most` sub left node
                 *   2. `swap` -> NOTE !!! swap node val
                 *   3. run delete op on sub right node (delete the `leftMostNode` node)
                 */
                // 1. Find the in-order successor (leftmost node in the right subtree)
                TreeNode leftMostNode = findLeftMostNode(root.right);

                /** NOTE !!!  how we `swap` node */
                // 2. Overwrite current root's value with successor's value
                root.val = leftMostNode.val;

                // 3. Delete the successor node from the right subtree
                root.right = deleteNode_0_0_1(root.right, leftMostNode.val);
            }
        } else {
            if (root.val < key) {
                root.right = deleteNode_0_0_1(root.right, key);
            } else {
                root.left = deleteNode_0_0_1(root.left, key);
            }
        }

        return root;
    }

    private TreeNode findLeftMostNode(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    // V0-1
    // IDEA: DFS + BST property
    /**
     * time = O(H)
     * space = O(H)
     */
    /**
     * (when found a node to delete)
     *  // Case 1: No children
     *  // Case 2: One child
     *  // Case 3: Two children
     * time = O(H)
     * space = O(H)
     */
    public TreeNode deleteNode_0_1(TreeNode root, int key) {
        if (root == null)
            return root;

        if (key < root.val)
            root.left = deleteNode_0_1(root.left, key);
        else if (key > root.val)
            root.right = deleteNode_0_1(root.right, key);
        else {
            // Case 1: No children
            if (root.left == null && root.right == null)
                return null;

            // Case 2: One child
            if (root.left == null || root.right == null)
                return (root.left != null) ? root.left : root.right;

            // Case 3: Two children
            /**
             * step 1) move `1 step` to sub left
             * step 2) move `till the right most`
             */
            TreeNode temp = root.left;
            while (temp.right != null) {
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
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode deleteNode_0_2(TreeNode root, int key) {
        return deleteNodeHelper(root, key);
    }

    private TreeNode deleteNodeHelper(TreeNode root, int key) {
        if (root == null) {
            return null;
        }

        if (key < root.val) {
            root.left = deleteNodeHelper(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNodeHelper(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            TreeNode minNode = findMin(root.right);
            root.val = minNode.val;
            root.right = deleteNodeHelper(root.right, minNode.val);
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
    /**
     * time = O(H)
     * space = O(H)
     */
    // https://youtu.be/LFzAoJJt92M?feature=shared
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0450-delete-node-in-a-bst.java
    /**
     *  Summary of Deletion Strategy:
     *  | Case         | Description        | What Happens                                  |
     * |--------------|--------------------|-----------------------------------------------|
     * | Leaf         | No children         | Return `null`                                 |
     * | One Child    | One child           | Replace node with its child                   |
     * | Two Children | Both children       | Replace with in-order successor, then delete the successor |
     * time = O(H)
     * space = O(H)
     */
    public TreeNode deleteNode_1_1(TreeNode root, int key) {
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
             *    •   When a node has both children, we:
             *
             *      • Find the in-order successor (the `smallest` node in the right subtree).
             *      • `Copy` its value to the `current node`.
             *      • `Recursively` DELETE that `successor` in the right subtree.
             *
             *   ->  This preserves the BST property because the in-order successor
             *       is always the next-larger node.
             *
             */
            else {
                TreeNode minValNode = minimumVal(root.right);
                root.val = minValNode.val;
                root.right = deleteNode_1_1(root.right, minValNode.val);
            }
        }
        return root;
    }

    private TreeNode minimumVal(TreeNode root) {
        TreeNode curr = root;
        while (curr != null && curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    // V2
    /**
     * time = O(H)
     * space = O(1)
     */
    public TreeNode deleteNode_2(TreeNode root, int key) {
        if (root == null)
            return null;
        if (root.val == key)
            return linker(root);
        TreeNode dummy = root;
        TreeNode current = root;
        while (current != null) {
            if (current.val > key) {
                if (current.left != null && current.left.val == key) {
                    current.left = linker(current.left);
                    break;
                } else
                    current = current.left;
            } else {
                if (current.right != null && current.right.val == key) {
                    current.right = linker(current.right);
                    break;
                } else
                    current = current.right;
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
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode deleteNode_3(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        if (root.val < key) {
            root.right = deleteNode_3(root.right, key);
        } else if (root.val > key) {
            root.left = deleteNode_3(root.left, key);
        } else {
            if (root.left == null && root.right == null) {
                return null;
            }
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
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
    /**
     * time = O(H)
     * space = O(H)
     */
    public TreeNode deleteNode_4(TreeNode root, int key) {
        if (root == null)
            return root;

        if (key < root.val)
            root.left = deleteNode_4(root.left, key);
        else if (key > root.val)
            root.right = deleteNode_4(root.right, key);
        else {
            if (root.left == null && root.right == null)
                return null;
            if (root.left == null || root.right == null)
                return (root.left != null) ? root.left : root.right;
            TreeNode temp = root.left;
            while (temp.right != null)
                temp = temp.right;
            root.val = temp.val;
            root.left = deleteNode_4(root.left, temp.val);
        }
        return root;
    }




}