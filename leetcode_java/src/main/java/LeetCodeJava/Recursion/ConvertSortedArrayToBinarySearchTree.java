package LeetCodeJava.Recursion;

// https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 108. Convert Sorted Array to Binary Search Tree
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: nums = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5]
 * Explanation: [0,-10,5,null,-3,null,9] is also accepted:
 *
 * Example 2:
 *
 *
 * Input: nums = [1,3]
 * Output: [3,1]
 * Explanation: [1,null,3] and [3,1] are both height-balanced BSTs.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * nums is sorted in a strictly increasing order.
 *
 *
 */
public class ConvertSortedArrayToBinarySearchTree {

    // V0
//    public TreeNode sortedArrayToBST(int[] nums) {
//
//    }

    // V0-0-1
    // IDEA: DFS build tree + BST property (fixed by gemini)
    public TreeNode sortedArrayToBST_0_0_1(int[] nums) {
        // Corrected edge case handling
        if (nums == null || nums.length == 0) {
            return null;
        }

        // Start the recursive process covering the entire array from index 0 to length - 1.
        return buildBST(nums, 0, nums.length - 1);
    }

    /**
     * Recursive helper function to build the Balanced BST (Correctly named buildBST).
     * * Strategy: Divide and Conquer
     * 1. Find the middle element (root).
     * 2. Recursively build the left subtree from the left half.
     * 3. Recursively build the right subtree from the right half.
     */
    public TreeNode buildBST(int[] nums, int leftIdx, int rightIdx) {

        /** NOTE !!!
         *
         *  edge case
         */
        // Base Case: If the subarray is invalid (left pointer crosses right pointer), return null.
        if (leftIdx > rightIdx) {
            return null;
        }

        // 1. Find the correct index of the root (middle element).
        // This standard formula prevents integer overflow and correctly finds the midpoint.
        int midIdx = leftIdx + (rightIdx - leftIdx) / 2;

        // 2. Create the root node with the value at the middle index.
        TreeNode root = new TreeNode(nums[midIdx]);

        /** NOTE !!!
         *
         *  left sub root:  [leftIdx, midIdx - 1]
         */
        // 3. Recursively build the left subtree.
        // The left child covers the range from leftIdx up to (midIdx - 1).
        root.left = buildBST(nums, leftIdx, midIdx - 1);

        /** NOTE !!!
         *
         *  right sub root:  [midIdx + 1, rightIdx]
         */
        // 4. Recursively build the right subtree.
        // The right child covers the range from (midIdx + 1) up to rightIdx.
        root.right = buildBST(nums, midIdx + 1, rightIdx);

        return root;
    }


    // V0-1
    // IDEA: DFS build tree + BST property (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *  key:
     *
     *   1.	Root selection: use the middle element nums[mid], not nums[start].
     *       → ensures the BST is height-balanced.
     * 	 2.	Base case: return null when start > end.
     * 	 3.	Left subtree: use [start, mid-1].
     * 	 4.	Right subtree: use [mid+1, end].
     *
     *
     * 	NOTE !!!
     *
     * 	 for BST, the in-order traverse is an ascending array (small -> big)
     * 	 e.g.
     * 	   input  = [0,1,2]
     * 	   node:
     * 	         0
     * 	       1   2
     *
     * 	   so, we can get the `width` of BST via (right_idx - left_idx) / 2
     * 	   e.g.
     * 	    width = (right_idx - left_idx) / 2
     *
     * 	    or, we can go further, get the idx of root (mid) via below
     *
     * 	    int mid = left + (right - left) / 2;
     *
     */
    /**
     *  Example:
     *
     *  Perfect 👍 Let’s walk step-by-step through the recursion of the fixed code on
     *
     * nums = [-10, -3, 0, 5, 9].
     *
     * ⸻
     *
     * Step 1: Initial call
     *
     * bstBuilder(nums, 0, 4)
     *
     * 	•	start=0, end=4
     * 	•	mid = 0 + (4-0)/2 = 2
     * 	•	root = nums[2] = 0
     *
     * So root node is:
     *
     *    0
     *   / \
     *  ?   ?
     *
     *
     * ⸻
     *
     * Step 2: Build left subtree
     *
     * bstBuilder(nums, 0, 1)
     *
     * 	•	start=0, end=1
     * 	•	mid = 0 + (1-0)/2 = 0
     * 	•	node = nums[0] = -10
     *
     * So left child is -10:
     *
     *     0
     *    / \
     *  -10  ?
     *
     * Build left subtree of -10:
     *
     * bstBuilder(nums, 0, -1)  // start > end → null
     *
     * => left = null
     *
     * Build right subtree of -10:
     *
     * bstBuilder(nums, 1, 1)
     *
     * 	•	start=1, end=1
     * 	•	mid=1
     * 	•	node = nums[1] = -3
     *
     * So:
     *
     *     0
     *    / \
     *  -10  ?
     *     \
     *     -3
     *
     *
     * ⸻
     *
     * Step 3: Build right subtree
     *
     * bstBuilder(nums, 3, 4)
     *
     * 	•	start=3, end=4
     * 	•	mid = 3 + (4-3)/2 = 3
     * 	•	node = nums[3] = 5
     *
     * So right child is 5:
     *
     *         0
     *        / \
     *     -10   5
     *       \
     *       -3
     *
     * Build left subtree of 5:
     *
     * bstBuilder(nums, 3, 2)  // start > end → null
     *
     * => left = null
     *
     * Build right subtree of 5:
     *
     * bstBuilder(nums, 4, 4)
     *
     * 	•	start=4, end=4
     * 	•	mid=4
     * 	•	node = nums[4] = 9
     *
     * So:
     *
     *         0
     *        / \
     *     -10   5
     *       \     \
     *       -3     9
     *
     *
     * ⸻
     *
     * ✅ Final Balanced BST
     *
     *         0
     *        / \
     *     -10   5
     *       \     \
     *       -3     9
     *
     *
     * ⸻
     *
     * 👉 Notice:
     * 	•	0 is root (middle of array).
     * 	•	Left subtree [-10, -3] is balanced under -10.
     * 	•	Right subtree [5, 9] is balanced under 5.
     * 	•	Depth difference between left/right is ≤ 1 everywhere → height-balanced BST.
     *
     * ⸻
     *
     */
    public TreeNode sortedArrayToBST_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return null;
        }
        return bstBuilder(nums, 0, nums.length - 1);
    }

    /** NOTE !!!
     *
     *  help func:
     *
     *   start: start_idx  in nums
     *   end: end_idx in nums
     */
    private TreeNode bstBuilder(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }

        // pick mid as root to ensure balance
        int mid = start + (end - start) / 2;
        TreeNode node = new TreeNode(nums[mid]);

        // build left and right subtrees recursively
        node.left = bstBuilder(nums, start, mid - 1);
        node.right = bstBuilder(nums, mid + 1, end);

        return node;
    }

    // V0-2
    // IDEA: ITERATIVE version (BFS like) (gpt)
    public TreeNode sortedArrayToBST_0_2(int[] nums) {
        if (nums == null || nums.length == 0)
            return null;

        int n = nums.length;
        int mid = (0 + n - 1) / 2;

        TreeNode root = new TreeNode(nums[mid]);

        // queue stores: node + start + end
        Queue<int[]> rangeQ = new LinkedList<>();
        Queue<TreeNode> nodeQ = new LinkedList<>();

        nodeQ.offer(root);
        rangeQ.offer(new int[] { 0, n - 1 });

        while (!nodeQ.isEmpty()) {
            TreeNode curNode = nodeQ.poll();
            int[] range = rangeQ.poll();
            int start = range[0], end = range[1];
            int midIdx = (start + end) / 2;

            // left subtree
            if (start <= midIdx - 1) {
                int leftMid = (start + (midIdx - 1)) / 2;
                curNode.left = new TreeNode(nums[leftMid]);
                nodeQ.offer(curNode.left);
                rangeQ.offer(new int[] { start, midIdx - 1 });
            }

            // right subtree
            if (midIdx + 1 <= end) {
                int rightMid = ((midIdx + 1) + end) / 2;
                curNode.right = new TreeNode(nums[rightMid]);
                nodeQ.offer(curNode.right);
                rangeQ.offer(new int[] { midIdx + 1, end });
            }
        }

        return root;
    }

    // V0-3
    // IDEA: DFS build tree + BST property (gpt)
    public TreeNode sortedArrayToBST_0_3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        return build(nums, 0, nums.length - 1);
    }

    private TreeNode build(int[] nums, int leftIdx, int rightIdx) {
        if (leftIdx > rightIdx) {
            return null;
        }

        int mid = leftIdx + (rightIdx - leftIdx) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        root.left = build(nums, leftIdx, mid - 1);
        root.right = build(nums, mid + 1, rightIdx);

        return root;
    }


    // V1
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6892739/video-find-middle-of-tree-or-subtree-by-94thd/
    public TreeNode sortedArrayToBST_1(int[] nums) {
        return convert(nums, 0, nums.length - 1);
    }

    private TreeNode convert(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        /** NOTE !!!
         *
         *
         *  get `mid` (e.g. the idx of `root`) via
         *
         *   int mid = left + (right - left) / 2;
         */
        int mid = left + (right - left) / 2;

        TreeNode node = new TreeNode(nums[mid]);

        node.left = convert(nums, left, mid - 1);
        node.right = convert(nums, mid + 1, right);

        return node;
    }

    // V2
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6025974/0-ms-runtime-beats-100-user-step-by-step-88p3/
    public TreeNode sortedArrayToBST_2(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    private TreeNode helper(int[] nums, int left, int right) {
        if (left > right)
            return null;
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }


    // V3
    // https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/solutions/6907442/beats-100-beginner-friendly-explanation-dna9u/
    public TreeNode sortedArrayToBST_3(int[] nums) {
        return insert(nums, 0, nums.length);
    }

    // Recursive helper to build BST
    TreeNode insert(int[] nums, int start, int end) {
        if (start == end)
            return null;

        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = insert(nums, start, mid);
        node.right = insert(nums, mid + 1, end);
        return node;
    }



}
