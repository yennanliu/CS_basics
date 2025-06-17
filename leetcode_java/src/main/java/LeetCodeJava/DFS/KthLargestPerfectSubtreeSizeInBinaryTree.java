package LeetCodeJava.DFS;

// https://leetcode.com/problems/k-th-largest-perfect-subtree-size-in-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 3319. K-th Largest Perfect Subtree Size in Binary Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given the root of a binary tree and an integer k.
 *
 * Return an integer denoting the size of the kth largest perfect binary subtree, or -1 if it doesn't exist.
 *
 * A perfect binary tree is a tree where all leaves are on the same level, and every parent has two children.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [5,3,6,5,2,5,7,1,8,null,null,6,8], k = 2
 *
 * Output: 3
 *
 * Explanation:
 *
 *
 *
 * The roots of the perfect binary subtrees are highlighted in black. Their sizes, in non-increasing order are [3, 3, 1, 1, 1, 1, 1, 1].
 * The 2nd largest size is 3.
 *
 * Example 2:
 *
 * Input: root = [1,2,3,4,5,6,7], k = 1
 *
 * Output: 7
 *
 * Explanation:
 *
 *
 *
 * The sizes of the perfect binary subtrees in non-increasing order are [7, 3, 3, 1, 1, 1, 1]. The size of the largest perfect binary subtree is 7.
 *
 * Example 3:
 *
 * Input: root = [1,2,3,null,4], k = 3
 *
 * Output: -1
 *
 * Explanation:
 *
 *
 *
 * The sizes of the perfect binary subtrees in non-increasing order are [1, 1]. There are fewer than 3 perfect binary subtrees.
 *
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 2000].
 * 1 <= Node.val <= 2000
 * 1 <= k <= 1024
 *
 */
public class KthLargestPerfectSubtreeSizeInBinaryTree {
  /**
   * Definition for a binary tree node.
   * public class TreeNode {
   *     int val;
   *     TreeNode left;
   *     TreeNode right;
   *     TreeNode() {}
   *     TreeNode(int val) { this.val = val; }
   *     TreeNode(int val, TreeNode left, TreeNode right) {
   *         this.val = val;
   *         this.left = left;
   *         this.right = right;
   *     }
   * }
   */

  // V0
  //    public int kthLargestPerfectSubtree(TreeNode root, int k) {
  //
  //    }

  // V0-1
  // IDEA: DFS (fixed by gpt)
  //  Time Complexity: O(N log N)
  //  Space Complexity: O(N)
  /**
   *  NOTE !!! the DFS is `post order traverse`
   *           e.g. left -> right -> root
   */
  /**
   *  Objective recap:
   *
   *   We want to:
   * 	•	Find all perfect binary subtrees in the given tree.
   * 	•	A perfect binary tree is one where:
   * 	    •	Every node has 0 or 2 children (i.e., full),
   * 	    •	All leaf nodes are at the `same depth`.
   * 	•	Return the k-th largest size among these perfect subtrees.
   * 	•	If there are fewer than k perfect subtrees, return -1.
   *
   */
  // This is a class-level list that stores the sizes of all perfect subtrees we discover during traversal.
  private List<Integer> perfectSizes = new ArrayList<>();

    public int kthLargestPerfectSubtree_0_1(TreeNode root, int k) {
        dfs(root);
        if (perfectSizes.size() < k)
            return -1;

        Collections.sort(perfectSizes, Collections.reverseOrder());
        return perfectSizes.get(k - 1);
    }

  // Helper class to store information about each subtree
  /**
   *
   * It returns a helper object SubtreeInfo, which contains:
   * 	•	height: depth of the subtree rooted at node.
   * 	•	size: number of nodes in the subtree.
   * 	•	isPerfect: boolean indicating whether this subtree is perfect.
   *
   */
  private static class SubtreeInfo {
        int height;
        int size;
        boolean isPerfect;

        SubtreeInfo(int height, int size, boolean isPerfect) {
            this.height = height;
            this.size = size;
            this.isPerfect = isPerfect;
        }
    }

  /**
   * Inside dfs():
   * 	1.	Base case:
   * 	    •	If node == null, we return a SubtreeInfo with height 0, size 0, and isPerfect = true.
   * 	2.	Recurse on left and right children.
   * 	3.	Check if the subtree rooted at this node is perfect:
   *
   */
    /**
     *  NOTE !!! the DFS is `post order traverse`
     *           e.g. left -> right -> root
     * ---
     *
     * ## ✅ Is this DFS post-order?
     *
     * **Yes, 100% this is a post-order traversal.**
     *
     * ### In post-order DFS, the visiting order is:
     *
     * ```
     * left → right → root
     * ```
     *
     * In your `dfs()`:
     *
     * ```java
     * SubtreeInfo left = dfs(node.left);   // Visit left subtree
     * SubtreeInfo right = dfs(node.right); // Visit right subtree
     * ```
     *
     * Then only **after both left and right are processed**, you evaluate the current root:
     *
     * ```java
     * boolean isPerfect = left.isPerfect && right.isPerfect && (left.height == right.height);
     * ```
     *
     * This ordering is **crucial** for subtree-based logic.
     *
     * ---
     *
     * ## 🧠 Why post-order is needed for this?
     *
     * Because:
     *
     * * You **cannot determine if the current subtree is perfect** without first knowing:
     *
     *   * If its **left and right subtrees** are perfect
     *   * If they have the **same height**
     *
     * So post-order guarantees:
     *
     * * All recursive calls on children are **done** before we evaluate the current node.
     *
     * ---
     *
     * ## ✅ Example Tree
     *
     * ```
     *         1
     *        / \
     *       2   3
     *      / \
     *     4   5
     * ```
     *
     * Traversal sequence in post-order:
     *
     * ```
     * dfs(4) → dfs(5) → dfs(2) → dfs(3) → dfs(1)
     * ```
     *
     * You evaluate subtree rooted at 2 **only after** processing its children.
     *
     * ---
     *
     * ## ✅ Your code and comments are great
     *
     * Your code:
     *
     * ```java
     * boolean isPerfect = left.isPerfect && right.isPerfect
     *         && (left.height == right.height);
     * ```
     *
     * Comment:
     *
     * ```java
     * This ensures:
     *  • Both left and right subtrees are perfect.
     *  • Their heights are the same → leaves are at the same level.
     * ```
     *
     * Perfect explanation. ✅
     *
     * Then:
     *
     * ```java
     * if (isPerfect) {
     *     perfectSizes.add(size);
     * }
     * ```
     *
     * You **collect the size of perfect subtrees** once you've confirmed the conditions.
     *
     * ---
     *
     * ## ✅ Final Notes
     *
     * | Concept           | Status                                          |
     * | ----------------- | ----------------------------------------------- |
     * | DFS Traversal     | ✅ Yes, it's **post-order**                      |
     * | Order logic       | ✅ Needed to confirm perfect subtree before root |
     * | Efficiency        | ✅ Optimal — O(n) for full tree                  |
     * | Clean abstraction | ✅ `SubtreeInfo` is idiomatic and extensible     |
     *
     */
    private SubtreeInfo dfs(TreeNode node) {
        if (node == null) {
            return new SubtreeInfo(0, 0, true);
        }

        SubtreeInfo left = dfs(node.left);
        SubtreeInfo right = dfs(node.right);

    /**  NOTE !!!  below logic:
     *
     * This ensures:
     * 	•	Both left and right subtrees are perfect.
     * 	•	Their `heights` are the same → leaves are at the `same level`.
     */
    boolean isPerfect = left.isPerfect && right.isPerfect
            && (left.height == right.height);


        int size = left.size + right.size + 1;
        int height = Math.max(left.height, right.height) + 1;

        /**
         *  NOTE !!!
         *
         *  If the current subtree is perfect, we record its size:
         *
         */
        if (isPerfect) {
            perfectSizes.add(size);
        }

        return new SubtreeInfo(height, size, isPerfect);
    }


    // V0-2
    // IDEA: MIN HEAP (gpt)


    // V1


    // V2
    // https://leetcode.com/problems/k-th-largest-perfect-subtree-size-in-binary-tree/solutions/5905522/javacpython-dfs-by-lee215-5ejv/
    // IDEA: DFS
    public int kthLargestPerfectSubtree_2(TreeNode root, int k) {
        List<Integer> res = new ArrayList<>();
        dfs(root, res);
        Collections.sort(res);
        if (k <= res.size()) {
            return (1 << res.get(res.size() - k)) - 1;
        }
        return -1;
    }

    private int dfs(TreeNode root, List<Integer> res) {
        if (root == null)
            return 0;
        int l = dfs(root.left, res), r = dfs(root.right, res);
        if (l == r && l >= 0) {
            res.add(l + 1);
            return l + 1;
        }
        return -1;
    }

    // V3
    // https://leetcode.com/problems/k-th-largest-perfect-subtree-size-in-binary-tree/solutions/5913315/python3-10-lines-dfs-ts-82-77-by-spauldi-mfov/
    // IDEA: DFS
    public int kthLargestPerfectSubtree_3(TreeNode root, int k) {
        List<Integer> ans = new ArrayList<>();
        dfs_3(root, ans);

        if (ans.size() < k)
            return -1;

        Collections.sort(ans, Collections.reverseOrder());
        return ans.get(k - 1);
    }

    private int dfs_3(TreeNode node, List<Integer> ans) {

        if (node == null)
            return 0;

        int left = dfs_3(node.left, ans);
        int right = dfs_3(node.right, ans);

        if (left != right)
            return -1;

        ans.add(left + right + 1);
        return left + right + 1;
    }

    // V4
    // IDEA: DFS
    // https://leetcode.com/problems/k-th-largest-perfect-subtree-size-in-binary-tree/solutions/5905494/easy-on-dfs-traversal-solution-by-be_fig-wg2e/
//    List<Integer> ans = new ArrayList<>();
//
//    // Method to recursively check for perfect subtree and calculate its size
//    public Pair<Boolean, Integer> makeTree(TreeNode root) {
//        if (root == null) {
//            return new Pair<>(true, 0);
//        }
//
//        Pair<Boolean, Integer> l = makeTree(root.left);
//        Pair<Boolean, Integer> r = makeTree(root.right);
//
//        if (l.getKey() && r.getKey() && l.getValue().equals(r.getValue())) {
//            int s = l.getValue() + r.getValue() + 1;
//            ans.add(s);
//            return new Pair<>(true, s);
//        }
//
//        return new Pair<>(false, 0);
//    }
//
//    public int kthLargestPerfectSubtree_4(TreeNode root, int k) {
//        if (root == null) {
//            return -1;
//        }
//
//        ans.clear();
//        makeTree(root);
//
//        Collections.sort(ans, Collections.reverseOrder());
//
//        if (ans.size() >= k) {
//            return ans.get(k - 1);
//        }
//
//        return -1;
//    }

    // V5

}
