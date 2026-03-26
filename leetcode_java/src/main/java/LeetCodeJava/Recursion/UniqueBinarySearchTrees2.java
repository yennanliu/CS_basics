package LeetCodeJava.Recursion;

import DataStructure.Pair;
import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;


// https://leetcode.com/problems/unique-binary-search-trees-ii/description/
/**
 *   95. Unique Binary Search Trees II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer n, return all the structurally unique BST's (binary search trees), which has exactly n nodes of unique values from 1 to n. Return the answer in any order.
 *
 *  Example 1:
 *
 *
 * Input: n = 3
 * Output: [[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
 * Example 2:
 *
 * Input: n = 1
 * Output: [[1]]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 8
 *
 */
public class UniqueBinarySearchTrees2 {

    // V0
//    public List<TreeNode> generateTrees(int n) {
//
//    }


    // V1-1
    // IDEA: Recursive Dynamic Programming
    // https://leetcode.com/problems/unique-binary-search-trees-ii/editorial/
    public List<TreeNode> allPossibleBST_1_1(
            int start,
            int end,
            Map<Pair<Integer, Integer>, List<TreeNode>> memo) {
        List<TreeNode> res = new ArrayList<>();
        if (start > end) {
            res.add(null);
            return res;
        }
        if (memo.containsKey(new Pair<>(start, end))) {
            return memo.get(new Pair<>(start, end));
        }
        // Iterate through all values from start to end to construct left and right subtree recursively.
        for (int i = start; i <= end; ++i) {
            List<TreeNode> leftSubTrees = allPossibleBST_1_1(start, i - 1, memo);
            List<TreeNode> rightSubTrees = allPossibleBST_1_1(i + 1, end, memo);

            // Loop through all left and right subtrees and connect them to ith root.
            for (TreeNode left : leftSubTrees) {
                for (TreeNode right : rightSubTrees) {
                    TreeNode root = new TreeNode(i, left, right);
                    res.add(root);
                }
            }
        }
        memo.put(new Pair<>(start, end), res);
        return res;
    }

    public List<TreeNode> generateTrees_1_1(int n) {
        Map<Pair<Integer, Integer>, List<TreeNode>> memo = new HashMap<>();
        return allPossibleBST_1_1(1, n, memo);
    }


    // V1-2
    // IDEA:  Iterative Dynamic Programming
    // https://leetcode.com/problems/unique-binary-search-trees-ii/editorial/
    public List<TreeNode> generateTrees_1_2(int n) {
        List<List<List<TreeNode>>> dp = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            List<List<TreeNode>> innerList = new ArrayList<>(n + 1);
            for (int j = 0; j <= n; j++) {
                innerList.add(new ArrayList<>());
            }
            dp.add(innerList);
        }

        for (int i = 1; i <= n; i++) {
            dp.get(i).get(i).add(new TreeNode(i));
        }

        for (int numberOfNodes = 2; numberOfNodes <= n; numberOfNodes++) {
            for (int start = 1; start <= n - numberOfNodes + 1; start++) {
                int end = start + numberOfNodes - 1;
                for (int i = start; i <= end; i++) {
                    List<TreeNode> leftSubtrees = (i != start)
                            ? dp.get(start).get(i - 1)
                            : new ArrayList<>();
                    if (leftSubtrees.isEmpty()) {
                        leftSubtrees.add(null);
                    }
                    List<TreeNode> rightSubtrees = (i != end)
                            ? dp.get(i + 1).get(end)
                            : new ArrayList<>();
                    if (rightSubtrees.isEmpty()) {
                        rightSubtrees.add(null);
                    }

                    for (TreeNode left : leftSubtrees) {
                        for (TreeNode right : rightSubtrees) {
                            TreeNode root = new TreeNode(i, left, right);
                            dp.get(start).get(end).add(root);
                        }
                    }
                }
            }
        }
        return dp.get(1).get(n);
    }



    // V1-3
    // IDEA: Dynamic Programming with Space Optimization
    // https://leetcode.com/problems/unique-binary-search-trees-ii/editorial/
    public List<TreeNode> generateTrees_1_3(int n) {
        List<List<TreeNode>> dp = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            dp.add(new ArrayList<>());
        }
        dp.get(0).add(null);

        for (int numberOfNodes = 1; numberOfNodes <= n; numberOfNodes++) {
            for (int i = 1; i <= numberOfNodes; i++) {
                int j = numberOfNodes - i;
                for (TreeNode left : dp.get(i - 1)) {
                    for (TreeNode right : dp.get(j)) {
                        TreeNode root = new TreeNode(i, left, clone(right, i));
                        dp.get(numberOfNodes).add(root);
                    }
                }
            }
        }
        return dp.get(n);
    }

    private TreeNode clone(TreeNode node, int offset) {
        if (node == null) {
            return null;
        }
        TreeNode clonedNode = new TreeNode(node.val + offset);
        clonedNode.left = clone(node.left, offset);
        clonedNode.right = clone(node.right, offset);
        return clonedNode;
    }





    // V2





}
