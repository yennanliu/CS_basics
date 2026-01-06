package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/balance-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 1382. Balance a Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the root of a binary search tree, return a balanced binary search tree with the same node values. If there is more than one answer, return any of them.
 *
 * A binary search tree is balanced if the depth of the two subtrees of every node never differs by more than 1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,null,2,null,3,null,4,null,null]
 * Output: [2,1,3,null,null,null,4]
 * Explanation: This is not the only correct answer, [3,1,4,null,2] is also correct.
 * Example 2:
 *
 *
 * Input: root = [2,1,3]
 * Output: [2,1,3]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 1 <= Node.val <= 105
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 296,434/349.9K
 * Acceptance Rate
 * 84.7%
 * Topics
 */
public class BalanceABinarySearchTree {

    // V0
    // IDEA: BST + `mid point` built BST + DFS
    // LC 105, 106
    public TreeNode balanceBST(TreeNode root) {

        List<TreeNode> nodes = new ArrayList<>();
        /**  NOTE !!!
         *
         *  we want to get increasing order to list of node
         *  and since it's a BST, so we can do above
         *  by `in order traverse`. e.g. left -> root -> right
         */
        inorder_0(root, nodes);
        /** build tree */
        return rebuildBST(nodes, 0, nodes.size() - 1);
    }

    /**
     * help func get list of node via inrder traverse
     */
    private void inorder_0(TreeNode root, List<TreeNode> nodes) {
        if (root == null) {
            return;
        }
        inorder_0(root.left, nodes);
        nodes.add(root);
        inorder_0(root.right, nodes);
    }

    /**
     *  NOTE !!! the param of this helper func:
     *
     * @nodes: list of nodes (increase order)
     * @l: left boundary of list
     * @r: right boundary of list
     */
    private TreeNode rebuildBST(List<TreeNode> nodes, int l, int r) {
        // edge
        if (nodes == null || nodes.size() == 0) {
            return null;
        }
        /**
         *  NOTE !!!  this edge case
         */
        if (l > r) {
            return null;
        }
        /**
         *  NOTE !!! core of the problem: Rebuild balanced BST
         *
         *  •	Pick the middle node as root.
         * 	•	Recursively build left and right subtrees from halves.
         * 	•	This ensures each subtree size differs by ≤ 1 ⇒ balanced.
         *
         *  refer LC 105, 106
         */
        int mid = (l + r) / 2; // radius
        TreeNode root = nodes.get((mid));
        root.left = rebuildBST(nodes, l, mid - 1); // ???
        root.right = rebuildBST(nodes, mid + 1, r);

        // ???
        return root;
    }

    // V0-1
    // IDEA: BST + DFS (fixed by gpt)
    // Step 1: collect inorder traversal (sorted values)
    public TreeNode balanceBST_0_1(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>();
        inorder(root, nodes);
        // Step 2: rebuild balanced BST
        return buildBalanced(nodes, 0, nodes.size() - 1);
    }

    private void inorder(TreeNode root, List<TreeNode> nodes) {
        if (root == null)
            return;
        inorder(root.left, nodes);
        nodes.add(root);
        inorder(root.right, nodes);
    }

    private TreeNode buildBalanced(List<TreeNode> nodes, int l, int r) {
        if (l > r)
            return null;
        int mid = (l + r) / 2;
        TreeNode root = nodes.get(mid);
        root.left = buildBalanced(nodes, l, mid - 1);
        root.right = buildBalanced(nodes, mid + 1, r);
        return root;
    }

    // V0-2
    // IDEA: in-order traversal + BST + DFS (gemini)
    // List to store the node values in sorted order
    private List<Integer> sortedValues = new ArrayList<>();

    public TreeNode balanceBST_0_2(TreeNode root) {
        if (root == null) {
            return null;
        }

        // 1. Perform in-order traversal to get all values in a sorted list.
        inOrderTraversal(root);

        // 2. Build a new, balanced BST from the sorted list.
        return buildBalancedBST(0, sortedValues.size() - 1);
    }

    /**
     * Helper 1: Performs in-order traversal (Left, Root, Right)
     * to populate the sortedValues list.
     */
    private void inOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left);
        sortedValues.add(node.val); // Add the current node's value
        inOrderTraversal(node.right);
    }

    /**
     * Helper 2: Recursively builds a balanced BST from the sorted list
     * using the indices [start...end].
     */
    private TreeNode buildBalancedBST(int start, int end) {
        // Base case: If the pointers cross, we have no nodes to create.
        if (start > end) {
            return null;
        }

        // Find the middle element of the current list segment.
        // This will become the root of this subtree.
        int mid = start + (end - start) / 2;

        // Create the new root node
        TreeNode root = new TreeNode(sortedValues.get(mid));

        // Recursively build the left subtree from the left half (start to mid-1)
        root.left = buildBalancedBST(start, mid - 1);

        // Recursively build the right subtree from the right half (mid+1 to end)
        root.right = buildBalancedBST(mid + 1, end);

        return root;
    }

    // V0-3
    // IDEA: get all nodes + rebuild BST by taking `mid` node as root
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/64296433
    private List<Integer> v = new ArrayList<>();

    private void dfs_0_3(TreeNode n) {
        if (n == null) {
            return;
        }
        dfs_0_3(n.left);
        v.add(n.val);
        dfs_0_3(n.right);
    }

    private TreeNode build_0_3(int l, int r) {
        if (l > r) {
            return null;
        }
        int mid = (l + r) / 2;
        TreeNode res = new TreeNode(v.get(mid));
        res.left = build_0_3(l, mid - 1);
        res.right = build_0_3(mid + 1, r);
        return res;
    }

    public TreeNode balanceBST_0_3(TreeNode root) {
        dfs_0_3(root);
        return build_0_3(0, v.size() - 1);
    }


    // V1-1
    // IDEA: Inorder Traversal + Recursive Construction
    // https://leetcode.com/problems/balance-a-binary-search-tree/solutions/
    public TreeNode balanceBST_1_1(TreeNode root) {
        // Create a list to store the inorder traversal of the BST
        List<Integer> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);

        // Construct and return the balanced BST
        return createBalancedBST(inorder, 0, inorder.size() - 1);
    }

    private void inorderTraversal(TreeNode root, List<Integer> inorder) {
        // Perform an inorder traversal to store the elements in sorted order
        if (root == null)
            return;
        inorderTraversal(root.left, inorder);
        inorder.add(root.val);
        inorderTraversal(root.right, inorder);
    }

    private TreeNode createBalancedBST(
            List<Integer> inorder,
            int start,
            int end) {
        // Base case: if the start index is greater than the end index, return null
        if (start > end)
            return null;

        // Find the middle element of the current range
        int mid = start + (end - start) / 2;

        // Recursively construct the left and right subtrees
        TreeNode leftSubtree = createBalancedBST(inorder, start, mid - 1);
        TreeNode rightSubtree = createBalancedBST(inorder, mid + 1, end);

        // Create a new node with the middle element and attach the subtrees
        TreeNode node = new TreeNode(
                inorder.get(mid),
                leftSubtree,
                rightSubtree);
        return node;
    }

    // V1-2
    // IDEA: : Day-Stout-Warren Algorithm / In-Place Balancing
    // https://leetcode.com/problems/balance-a-binary-search-tree/solutions/
    public TreeNode balanceBST_1_2(TreeNode root) {
        if (root == null)
            return null;

        // Step 1: Create the backbone (vine)
        // Temporary dummy node
        TreeNode vineHead = new TreeNode(0);
        vineHead.right = root;
        TreeNode current = vineHead;
        while (current.right != null) {
            if (current.right.left != null) {
                rightRotate(current, current.right);
            } else {
                current = current.right;
            }
        }

        // Step 2: Count the nodes
        int nodeCount = 0;
        current = vineHead.right;
        while (current != null) {
            ++nodeCount;
            current = current.right;
        }

        // Step 3: Create a balanced BST
        int m = (int) Math.pow(
                2,
                Math.floor(Math.log(nodeCount + 1) / Math.log(2))) -
                1;
        makeRotations(vineHead, nodeCount - m);
        while (m > 1) {
            m /= 2;
            makeRotations(vineHead, m);
        }

        TreeNode balancedRoot = vineHead.right;
        return balancedRoot;
    }

    // Function to perform a right rotation
    private void rightRotate(TreeNode parent, TreeNode node) {
        TreeNode tmp = node.left;
        node.left = tmp.right;
        tmp.right = node;
        parent.right = tmp;
    }

    // Function to perform a left rotation
    private void leftRotate(TreeNode parent, TreeNode node) {
        TreeNode tmp = node.right;
        node.right = tmp.left;
        tmp.left = node;
        parent.right = tmp;
    }

    // Function to perform a series of left rotations to balance the vine
    private void makeRotations(TreeNode vineHead, int count) {
        TreeNode current = vineHead;
        for (int i = 0; i < count; ++i) {
            TreeNode tmp = current.right;
            leftRotate(current, tmp);
            current = current.right;
        }
    }

    // V2
    // https://leetcode.ca/2019-09-12-1382-Balance-a-Binary-Search-Tree/
    private List<Integer> nums = new ArrayList<>();

    public TreeNode balanceBST_2(TreeNode root) {
        dfs(root);
        return build(0, nums.size() - 1);
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        nums.add(root.val);
        dfs(root.right);
    }

    private TreeNode build(int i, int j) {
        if (i > j) {
            return null;
        }
        int mid = (i + j) >> 1;
        TreeNode left = build(i, mid - 1);
        TreeNode right = build(mid + 1, j);
        return new TreeNode(nums.get(mid), left, right);
    }



}
