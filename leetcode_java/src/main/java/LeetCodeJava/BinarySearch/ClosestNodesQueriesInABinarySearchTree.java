package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/closest-nodes-queries-in-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 2476. Closest Nodes Queries in a Binary Search Tree
 * Medium
 *
 * You are given the root of a binary search tree and an array queries of size n consisting of positive
 * integers.
 *
 * Find a 2D array answer of size n where answer[i] = [mini, maxi]:
 *
 * mini is the largest value in the tree that is smaller than or equal to queries[i]. If a such value
 * does not exist, add -1 instead.
 * maxi is the smallest value in the tree that is greater than or equal to queries[i]. If a such value
 * does not exist, add -1 instead.
 *
 * Return the array answer.
 *
 *
 * Example 1:
 *
 * Input: root = [6,2,13,1,4,9,15,null,null,null,null,null,null,14], queries = [2,5,16]
 * Output: [[2,2],[4,6],[15,-1]]
 * Explanation: We answer the queries in the following way:
 * - The largest number that is smaller or equal than 2 in the tree is 2, and the smallest number that is
 *   greater or equal than 2 is still 2. So the answer for the first query is [2,2].
 * - The largest number that is smaller or equal than 5 in the tree is 4, and the smallest number that is
 *   greater or equal than 5 is 6. So the answer for the second query is [4,6].
 * - The largest number that is smaller or equal than 16 in the tree is 15, and the smallest number that
 *   is greater or equal than 16 does not exist. So the answer for the third query is [15,-1].
 *
 * Example 2:
 *
 * Input: root = [4,null,9], queries = [3]
 * Output: [[-1,4]]
 * Explanation: The largest number that is smaller or equal to 3 in the tree does not exist, and the
 * smallest number that is greater or equal to 3 is 4. So the answer for the query is [-1,4].
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 10^5].
 * 1 <= Node.val <= 10^6
 * n == queries.length
 * 1 <= n <= 10^5
 * 1 <= queries[i] <= 10^6
 *
 */
public class ClosestNodesQueriesInABinarySearchTree {

    // V0
    // IDEA: IN-ORDER TRAVERSAL -> SORTED LIST + BINARY SEARCH
    /**
     *  The in-order walk of a BST is sorted, so flatten it once and answer each query with two
     *  binary searches:
     *    floor   = the value before the upper bound of x  (largest value <= x)
     *    ceiling = the value at the lower bound of x      (smallest value >= x)
     *  The tree can be a 10^5-node chain, so the traversal is iterative (no deep recursion).
     *  Searching the tree directly per query would be O(height), O(N) on a chain.
     */
    /**
     * time = O(N + Q log N)
     * space = O(N)
     */
    public List<List<Integer>> closestNodes(TreeNode root, List<Integer> queries) {
        // iterative in-order traversal
        List<Integer> sortedValues = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;
        while (current != null || !stack.isEmpty()) {
            // go as far left as possible
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            sortedValues.add(current.val);
            current = current.right;
        }

        int size = sortedValues.size();
        List<List<Integer>> answer = new ArrayList<>();
        for (int query : queries) {
            int floorIndex = upperBound(sortedValues, query) - 1;
            int ceilingIndex = lowerBound(sortedValues, query);

            int floor = -1;
            if (floorIndex >= 0) {
                floor = sortedValues.get(floorIndex);
            }
            int ceiling = -1;
            if (ceilingIndex < size) {
                ceiling = sortedValues.get(ceilingIndex);
            }

            List<Integer> pair = new ArrayList<>();
            pair.add(floor);
            pair.add(ceiling);
            answer.add(pair);
        }
        return answer;
    }

    /** First index whose value is >= target, or list size if none. */
    private int lowerBound(List<Integer> sorted, int target) {
        int left = 0;
        int right = sorted.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** First index whose value is > target, or list size if none. */
    private int upperBound(List<Integer> sorted, int target) {
        int left = 0;
        int right = sorted.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted.get(mid) > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
