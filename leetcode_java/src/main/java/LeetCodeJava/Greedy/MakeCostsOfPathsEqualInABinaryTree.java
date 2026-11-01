package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-costs-of-paths-equal-in-a-binary-tree/description/

import java.util.*;

/**
 * 2673. Make Costs of Paths Equal in a Binary Tree
 * Medium
 *
 * You are given an integer n representing the number of nodes in a perfect binary tree consisting of nodes numbered from 1 to n. The root of the tree is node 1 and each node i in the tree has two children where the left child is the node 2 * i and the right child is 2 * i + 1.
 *
 * Each node in the tree also has a cost represented by a given 0-indexed integer array cost of size n where cost[i] is the cost of node i + 1. You are allowed to increment the cost of any node by 1 any number of times.
 *
 * Return the minimum number of increments you need to make the cost of paths from the root to each leaf node equal.
 *
 * Note:
 *
 * A perfect binary tree is a tree where each node, except the leaf nodes, has exactly 2 children.
 * The cost of a path is the sum of costs of nodes in the path.
 *
 *
 * Example 1:
 *
 * Input: n = 7, cost = [1,5,2,2,3,3,1]
 * Output: 6
 * Explanation: We can do the following increments:
 * - Increase the cost of node 4 one time.
 * - Increase the cost of node 3 three times.
 * - Increase the cost of node 7 two times.
 * Each path from the root to a leaf will have a total cost of 9.
 * The total increments we did is 1 + 3 + 2 = 6.
 * It can be shown that this is the minimum answer we can achieve.
 *
 * Example 2:
 *
 * Input: n = 3, cost = [5,3,3]
 * Output: 0
 * Explanation: The two paths already have equal total costs, so no increments are needed.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^5
 * n + 1 is a power of 2
 * cost.length == n
 * 1 <= cost[i] <= 10^4
 *
 */
public class MakeCostsOfPathsEqualInABinaryTree {

    // V0
    // IDEA: BOTTOM-UP GREEDY ON THE HEAP-INDEXED PERFECT TREE
    //       all root-to-leaf sums are equal iff, at every node, both children's
    //       "node -> leaf" path costs are equal. bottom-up, raise the cheaper child by the
    //       difference (one bump on the child lifts its whole subtree), then
    //       pathCost[v] = cost[v] + max(child path costs).
    //       node v (1-based) has children 2v and 2v + 1; internal nodes are 1 .. n / 2.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minIncrements(int n, int[] cost) {
        // pathCost[v - 1] = max cost from node v down to a leaf, after v's subtree is fixed
        int[] pathCost = Arrays.copyOf(cost, n);
        int increments = 0;

        // n / 2 down to 1 visits every node after both of its children
        for (int v = n / 2; v >= 1; v--) {
            int leftCost = pathCost[2 * v - 1];  // node 2v lives at index 2v - 1
            int rightCost = pathCost[2 * v];     // node 2v + 1 lives at index 2v
            increments += Math.abs(leftCost - rightCost);
            pathCost[v - 1] += Math.max(leftCost, rightCost);
        }
        return increments;
    }
}
