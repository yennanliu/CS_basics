package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/all-possible-full-binary-trees/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  894. All Possible Full Binary Trees
 *  Medium
 *
 *  Given an integer n, return a list of all possible full binary trees with n
 *  nodes. Each node of each tree in the answer must have Node.val == 0.
 *
 *  Each element of the answer is the root node of one possible tree.
 *  You may return the final list of trees in any order.
 *
 *  A full binary tree is a binary tree where each node has exactly 0 or 2 children.
 *
 *  Example 1:
 *
 *  Input: n = 7
 *  Output: [[0,0,0,null,null,0,0,null,null,0,0],
 *           [0,0,0,null,null,0,0,0,0],
 *           [0,0,0,0,0,0,0],
 *           [0,0,0,0,0,null,null,null,null,0,0],
 *           [0,0,0,0,0,null,null,0,0]]
 *
 *  Example 2:
 *
 *  Input: n = 3
 *  Output: [[0,0,0]]
 *
 *  Constraints:
 *
 *  1 <= n <= 20
 */
public class AllPossibleFullBinaryTrees {

    private final Map<Integer, List<TreeNode>> memo = new HashMap<>();

    // V0
    // IDEA: recursion + memo. A full binary tree needs an odd node count; split
    //       the remaining (n - 1) nodes between left / right subtree.
    /**
     * time = O(2^n)   // ~ Catalan number of trees, each built once thanks to memo
     * space = O(2^n)
     */
    public List<TreeNode> allPossibleFBT(int n) {
        if (n % 2 == 0) {
            return new ArrayList<>();
        }
        if (this.memo.containsKey(n)) {
            return this.memo.get(n);
        }
        List<TreeNode> res = new ArrayList<>();
        if (n == 1) {
            res.add(new TreeNode(0));
            this.memo.put(n, res);
            return res;
        }
        // NOTE !!! left sub tree size must be odd, and right gets (n - 1 - left)
        for (int leftCnt = 1; leftCnt < n - 1; leftCnt += 2) {
            List<TreeNode> lefts = allPossibleFBT(leftCnt);
            List<TreeNode> rights = allPossibleFBT(n - 1 - leftCnt);
            for (TreeNode left : lefts) {
                for (TreeNode right : rights) {
                    TreeNode root = new TreeNode(0);
                    root.left = left;
                    root.right = right;
                    res.add(root);
                }
            }
        }
        this.memo.put(n, res);
        return res;
    }

    // V1
    // IDEA: bottom-up DP (tabulation). Build dp[i] = every full binary tree with
    //       i nodes from the already built smaller odd sizes, no recursion.
    /**
     * time = O(2^n)   // ~ Catalan number of trees
     * space = O(2^n)
     */
    public List<TreeNode> allPossibleFBT_1(int n) {
        if (n % 2 == 0) {
            return new ArrayList<>();
        }
        // dp[i] = all full binary trees with i nodes (empty for even i)
        List<List<TreeNode>> dp = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            dp.add(new ArrayList<>());
        }
        dp.get(1).add(new TreeNode(0));

        for (int size = 3; size <= n; size += 2) {
            List<TreeNode> cur = dp.get(size);
            for (int leftCnt = 1; leftCnt < size - 1; leftCnt += 2) {
                for (TreeNode left : dp.get(leftCnt)) {
                    for (TreeNode right : dp.get(size - 1 - leftCnt)) {
                        cur.add(new TreeNode(0, left, right));
                    }
                }
            }
        }
        return dp.get(n);
    }

    // V2
    // IDEA: brute force plain recursion with NO memo - every subtree list is
    //       rebuilt from scratch. Kept as a readable correctness reference.
    /**
     * time = O(2^n * n)  // exponential, sub-results recomputed
     * space = O(2^n)
     */
    public List<TreeNode> allPossibleFBT_2(int n) {
        List<TreeNode> res = new ArrayList<>();
        if (n % 2 == 0) {
            return res;
        }
        if (n == 1) {
            res.add(new TreeNode(0));
            return res;
        }
        for (int leftCnt = 1; leftCnt < n - 1; leftCnt += 2) {
            List<TreeNode> lefts = allPossibleFBT_2(leftCnt);
            List<TreeNode> rights = allPossibleFBT_2(n - 1 - leftCnt);
            for (TreeNode left : lefts) {
                for (TreeNode right : rights) {
                    res.add(new TreeNode(0, left, right));
                }
            }
        }
        return res;
    }
}
