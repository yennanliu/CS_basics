package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-distance-in-a-binary-tree/description/
// https://leetcode.ca/all/1740.html

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1740. Find Distance in a Binary Tree
 * Given the root of a binary tree and two integers p and q, return the distance between the nodes of value p and value q in the tree.
 *
 * The distance between two nodes is the number of edges on the path from one to the other.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 0
 * Output: 3
 * Explanation: There are 3 edges between 5 and 0: 5-3-1-0.
 * Example 2:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 7
 * Output: 2
 * Explanation: There are 2 edges between 5 and 7: 5-2-7.
 * Example 3:
 *
 *
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 5
 * Output: 0
 * Explanation: The distance between a node and itself is 0.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 0 <= Node.val <= 109
 * All Node.val are unique.
 * p and q are values in the tree.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon
 */
public class FindDistanceInABinaryTree {

    // V0
//    public int findDistance(TreeNode root, int p, int q) {
//    }

    // V0-1
    // IDEA: LCA + NODE DIST + DFS (fixed by gpt)
    // TODO: validate
    public int findDistance_0_1(TreeNode root, int p, int q) {
        // edge
        if (root == null) {
            return -1;
        }
        if (p == q) {
            return 0;
        }

        TreeNode nodeLCA = getLCA(root, p, q);
        int len1 = getPathLen(nodeLCA, p, 0);
        int len2 = getPathLen(nodeLCA, q, 0);

        return len1 + len2;
    }

    private TreeNode getLCA(TreeNode root, int p, int q) {
        if (root == null) {
            return null;
        }
        if (root.val == p || root.val == q) {
            return root;
        }

        TreeNode left = getLCA(root.left, p, q);
        TreeNode right = getLCA(root.right, p, q);

        if (left != null && right != null) {
            return root;
        }

        return left != null ? left : right;
    }

    /** NOTE !!!
     *
     *  help func: get dist between `root` and the node with `target` val
     *
     *  1) This is a helper function that:
     * 	  - Starts at root
     * 	  - Tries to find a node whose value = target
     * 	  - Returns the distance (number of edges) from root to target.
     *
     */
    /**
     *  IDEA of `getPathLen` help func:
     *
     *  🧠 Summary of Logic Flow
     * 	1.	Stop when null (return -1) or when target is found (return distance).
     * 	2.	Search left first. If found, return immediately.
     * 	3.	Otherwise, search right.
     * 	4.	If neither side contains the target, the function will bubble up -1.
     */
    private int getPathLen(TreeNode root, int target, int dist) {
        /** NOTE !!!
         *
         *   base case:
         *
         *  •	Base case #1:
         *        if we hit a null node,
         *        -> the target DOES NOT exist in this branch.
         *
         * 	•	Returning -1 is a sentinel value
         * 	    meaning “not found in this subtree”
         */
        if (root == null) {
            return -1;  // not found
        }
        /**
         *  NOTE !!!
         *
         *  •	Base case #2: if the current node matches the target, return dist, which is the current number of edges from the starting node (typically the LCA) to this node.
         * 	•	This is the successful termination of the recursion.
         */
        if (root.val == target) {
            return dist;
        }

        /**
         *  NOTE !!!
         *
         *  •	Recurse into the left subtree.
         * 	•	Increment dist by 1 because we moved down one level.
         * 	•	Store the result in left.
         * 	      - If target is in this subtree,
         * 	        left will contain the distance.
         * 	     - Otherwise, left will be -1.
         */
        int left = getPathLen(root.left, target, dist + 1);
        /**
         * 	•	If we found the target in the left subtree,
         * 	    return that distance immediately.
         * 	•	This avoids unnecessary searching in the right subtree.
         */
        if (left != -1) {
            return left;
        }

        /**
         *  NOTE !!!
         *
         *  •	If not found on the left, search the right subtree with dist + 1.
         * 	•	Return the result directly:
         * 	     - Either a valid distance if found,
         * 	     - Or -1 if not found in right subtree either.
         *
         */
        int right = getPathLen(root.right, target, dist + 1);
        return right;
    }

    // V0-2
    // IDEA: LCA (fixed by gemini)
    // TODO: validate
    public int findDistance_0_2(TreeNode root, int p, int q) {
        // Edge case check.
        if (root == null) {
            return -1; // Or throw an error, depending on problem constraints.
        }
        if (p == q) {
            return 0;
        }

        // 1. Find the LCA of nodes p and q.
        TreeNode nodeLCA = getLCA_0_2(root, p, q);

        // 2. Calculate the distance from the LCA to each node.
        // We use a helper that returns the distance from the target node to the current root.
        int len1 = getDistanceToTarget(nodeLCA, p);
        int len2 = getDistanceToTarget(nodeLCA, q);

        // 3. The total distance is the sum of the two distances.
        return len1 + len2;
    }

    // --- Helper 1: Find the Lowest Common Ancestor (LCA) ---
    private TreeNode getLCA_0_2(TreeNode root, int p, int q) {
        // Base case: If we hit null or find one of the targets, return the current node.
        if (root == null || root.val == p || root.val == q) {
            return root;
        }

        // Search the left and right subtrees.
        TreeNode left = getLCA_0_2(root.left, p, q);
        TreeNode right = getLCA_0_2(root.right, p, q);

        // Case 1: If both left and right return non-null, the current root is the LCA.
        if (left != null && right != null) {
            return root;
        }

        // Case 2: If only one side is non-null, pass that result up.
        // The ternary operator is a concise way to return the non-null result.
        return left != null ? left : right;
    }

    // --- Helper 2: Find the Distance from a Root (LCA) to a Target Node ---
    // Returns the number of edges from 'root' down to the node with value 'targetVal'.
    // Returns -1 if the target is not found in the subtree (shouldn't happen if LCA is correct).
    private int getDistanceToTarget(TreeNode root, int targetVal) {
        if (root == null) {
            return -1; // Target not found in this subtree.
        }

        if (root.val == targetVal) {
            return 0; // Found the target, distance is 0 from here.
        }

        // Search the left subtree.
        int leftDist = getDistanceToTarget(root.left, targetVal);

        // If found on the left, return 1 + the distance from the left child.
        if (leftDist != -1) {
            return leftDist + 1;
        }

        // Search the right subtree.
        int rightDist = getDistanceToTarget(root.right, targetVal);

        // If found on the right, return 1 + the distance from the right child.
        if (rightDist != -1) {
            return rightDist + 1;
        }

        // Target not found in either subtree.
        return -1;
    }


    // V1-1
    // IDEA: alternative: hashmap to store distance of every node-pair during finding LCA, then just map look up
    // https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
    public int findDistance_1_1(TreeNode root, int p, int q) {
        if (p == q)
            return 0;
        TreeNode ancestor = lowestCommonAncestor(root, p, q);
        return getDistance(ancestor, p) + getDistance(ancestor, q);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, int p, int q) {
        if (root == null)
            return null;
        if (root.val == p || root.val == q)
            return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null)
            return root;
        return left == null ? right : left;
    }

    public int getDistance(TreeNode node, int val) {
        if (node == null)
            return -1;
        if (node.val == val)
            return 0;
        int leftDistance = getDistance(node.left, val);
        int rightDistance = getDistance(node.right, val);
        int subDistance = Math.max(leftDistance, rightDistance);
        return subDistance >= 0 ? subDistance + 1 : -1;
    }

    // V1-2
    // IDEA: DFS
    // https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
    public int findDistance_1_2(TreeNode root, int p, int q) {
        TreeNode g = lca(root, p, q);
        return dfs(g, p) + dfs(g, q);
    }

    private int dfs(TreeNode root, int v) {
        if (root == null) {
            return -1;
        }
        if (root.val == v) {
            return 0;
        }
        int left = dfs(root.left, v);
        int right = dfs(root.right, v);
        if (left == -1 && right == -1) {
            return -1;
        }
        return 1 + Math.max(left, right);
    }

    private TreeNode lca(TreeNode root, int p, int q) {
        if (root == null || root.val == p || root.val == q) {
            return root;
        }
        TreeNode left = lca(root.left, p, q);
        TreeNode right = lca(root.right, p, q);
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return root;
    }

    // V2

    // V3



}
