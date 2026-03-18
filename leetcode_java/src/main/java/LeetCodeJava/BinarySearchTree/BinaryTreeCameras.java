package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/binary-tree-cameras/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  968. Binary Tree Cameras
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary tree. We install cameras on the tree nodes where each camera at a node can monitor its parent, itself, and its immediate children.
 *
 * Return the minimum number of cameras needed to monitor all nodes of the tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [0,0,null,0,0]
 * Output: 1
 * Explanation: One camera is enough to monitor all nodes if placed as shown.
 * Example 2:
 *
 *
 * Input: root = [0,0,null,0,null,0,null,null,0]
 * Output: 2
 * Explanation: At least two cameras are needed to monitor all nodes of the tree. The above image shows one of the valid configurations of camera placement.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * Node.val == 0
 *
 *
 */
public class BinaryTreeCameras {

    // V0
//    public int minCameraCover(TreeNode root) {
//
//    }


    // V0-1
    // IDEA: BOTTOM UP DFS + 3 states (fixed by gemini)
    /** NOTE !!
     *
     *  need to use `node state` idea:
     *
     *    0: Node is NOT covered. (It needs its parent to put a camera).
     *
     *    1: Node HAS a camera.
     *
     *    2: Node is COVERED (but has no camera).
     *
     */
    /**
     * Metric,Complexity,Explanation
     *
     * Time,O(N),Every node is visited once.
     * Space,O(H),Recursion stack depth (H = height of tree).
     *
     *
     */
    private int cameraCnt = 0;

    public int minCameraCover_0_1(TreeNode root) {
        // If the root returns 0, it means the root itself is not covered
        if (dfs_0_1(root) == 0) {
            cameraCnt++;
        }
        return cameraCnt;
    }

    private int dfs_0_1(TreeNode node) {
        // Base case: null nodes are considered "covered" (State 2)
        // so they don't force a camera on their parents (leaves).
        if (node == null) {
            return 2;
        }

        int left = dfs_0_1(node.left);
        int right = dfs_0_1(node.right);

        // 1. If any child is NOT covered, this node MUST have a camera.
        if (left == 0 || right == 0) {
            cameraCnt++;
            return 1;
        }

        // 2. If any child HAS a camera, this node is now COVERED.
        if (left == 1 || right == 1) {
            return 2;
        }

        // 3. Otherwise (children are covered but have no cameras),
        // this node is NOT covered and relies on its parent.
        return 0;
    }


    // V0-2
    // IDEA: post-order + 3 states + DFS (fixed by gpt)
    /**
     *
     * | State | Meaning     |
     * | ----- | ----------- |
     * | 0     | NOT covered |
     * | 1     | Has camera  |
     * | 2     | Covered     |
     */
    int cameraCnt_0_2 = 0;

    public int minCameraCover_0_2(TreeNode root) {
        if (dfs_0_2(root) == 0) { // root not covered
            cameraCnt_0_2++;
        }
        return cameraCnt_0_2;
    }

    private int dfs_0_2(TreeNode node) {
        if (node == null)
            return 2; // null = covered

        int left = dfs_0_2(node.left);
        int right = dfs_0_2(node.right);

        // if any child is NOT covered → place camera here
        if (left == 0 || right == 0) {
            cameraCnt_0_2++;
            return 1;
        }

        // if any child has camera → this node is covered
        if (left == 1 || right == 1) {
            return 2;
        }

        // otherwise, this node is NOT covered
        return 0;
    }

    // V0-3
    // IDEA: DP (gemini)
    public int minCameraCover_0_3(TreeNode root) {
        int[] ans = solve_0_3(root);
        return Math.min(ans[0], ans[1]);
    }

    // Returns [Strict, Covered, SubtreeCovered]
    private int[] solve_0_3(TreeNode node) {
        if (node == null) {
            return new int[] { 10000, 0, 0 }; // Use a large number for null "camera" state
        }

        int[] L = solve_0_3(node.left);
        int[] R = solve_0_3(node.right);

        // ML = min(L[0], L[1])
        int mL12 = Math.min(L[0], L[1]);
        int mR12 = Math.min(R[0], R[1]);

        // State 0: This node has a camera
        int d0 = 1 + Math.min(L[2], mL12) + Math.min(R[2], mR12);

        // State 1: This node is covered by a child
        int d1 = Math.min(L[0] + mR12, R[0] + mL12);

        // State 2: Everything below is covered, node is NOT covered
        int d2 = L[1] + R[1];

        return new int[] { d0, d1, d2 };
    }



    // V1
    // IDEA: DP
    // https://leetcode.com/problems/binary-tree-cameras/editorial/
    public int minCameraCover_1(TreeNode root) {
        int[] ans = solve(root);
        return Math.min(ans[1], ans[2]);
    }

    // 0: Strict ST; All nodes below this are covered, but not this one
    // 1: Normal ST; All nodes below and incl this are covered - no camera
    // 2: Placed camera; All nodes below this are covered, plus camera here
    public int[] solve(TreeNode node) {
        if (node == null)
            return new int[] { 0, 0, 99999 };

        int[] L = solve(node.left);
        int[] R = solve(node.right);
        int mL12 = Math.min(L[1], L[2]);
        int mR12 = Math.min(R[1], R[2]);

        int d0 = L[1] + R[1];
        int d1 = Math.min(L[2] + mR12, R[2] + mL12);
        int d2 = 1 + Math.min(L[0], mL12) + Math.min(R[0], mR12);
        return new int[] { d0, d1, d2 };
    }



    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/binary-tree-cameras/solutions/7645359/java-recursion-100-beats-0ms-by-kirpa_05-bo9z/
    int cameras = 0;

    public int minCameraCover_2(TreeNode root) {
        if (dfs(root) == 0) {
            cameras++;
        }
        return cameras;
    }

    private int dfs(TreeNode node) {
        if (node == null)
            return 2;

        int left = dfs(node.left);
        int right = dfs(node.right);

        if (left == 0 || right == 0) {
            cameras++;
            return 1;
        }

        if (left == 1 || right == 1) {
            return 2;
        }
        return 0;
    }




    // V3
    // https://leetcode.com/problems/binary-tree-cameras/solutions/7640271/binary-tree-camera-java-by-appledog6113-792r/
    // I have used greedy for this question
    // 3 states of camera
    // 0 represent not covered
    // 1 represent that camera is placed on that certain node
    // 2 represent that its covered by another camera
    static int camera;

    public int minCameraCover_3(TreeNode root) {
        camera = 0;
        if (helper(root) == 0) {
            camera++;
        }
        return camera;
    }

    public int helper(TreeNode root) {
        if (root == null) {
            return 2; // null will already be marked as covered
        }
        int left = helper(root.left);
        int right = helper(root.right);
        if (left == 0 || right == 0) { // if any of the children is not covered then have to place camera on that node..hence we return 1
            camera++;
            return 1;
        }
        if (left == 1 || right == 1) { // if any of the node has camera placed on left or right child..then that node is already covered by its child so we can mark it as covered and skip
            return 2;
        }
        return 0;
    }


    // V4
    // IDEA: PRE ORDER DFS
    // https://leetcode.com/problems/binary-tree-cameras/solutions/7640257/post-order-dfs-approach-by-adityaup43-2why/
    int cam = 0;

    public int minCameraCover_4(TreeNode root) {
        if (helper_4(root) == 0)
            cam++;
        return cam;
    }

    public int helper_4(TreeNode node) {
        if (node == null)
            return 2;//covered
        int left = helper_4(node.left);
        int right = helper_4(node.right);
        if (left == 0 || right == 0) {//not covered
            cam++;
            return 1;
        }
        if (left == 1 || right == 1)
            return 2; //placed
        return 0;
    }






}
