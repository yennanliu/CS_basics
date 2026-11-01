package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-reorder-array-to-get-same-bst/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1569. Number of Ways to Reorder Array to Get Same BST
 * Hard
 *
 * Given an array nums that represents a permutation of integers from 1 to n. We are going to construct a binary search tree (BST) by inserting the elements of nums in order into an initially empty BST. Find the number of different ways to reorder nums so that the constructed BST is identical to that formed from the original array nums.
 *
 * For example, given nums = [2,1,3], we will have 2 as the root, 1 as a left child, and 3 as a right child. The array [2,3,1] also yields the same BST but [3,2,1] yields a different BST.
 *
 * Return the number of ways to reorder nums such that the BST formed is identical to the original BST formed from nums.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: nums = [2,1,3]
 * Output: 1
 * Explanation: We can reorder nums to be [2,3,1] which will yield the same BST. There are no other ways to reorder nums which will yield the same BST.
 *
 * Example 2:
 *
 * Input: nums = [3,4,5,1,2]
 * Output: 5
 * Explanation: The following 5 arrays will yield the same BST:
 * [3,1,2,4,5]
 * [3,1,4,2,5]
 * [3,1,4,5,2]
 * [3,4,1,2,5]
 * [3,4,1,5,2]
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: 0
 * Explanation: There are no other orderings of nums that will yield the same BST.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= nums.length
 * All integers in nums are distinct.
 *
 */
public class NumberOfWaysToReorderArrayToGetSameBST {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIVIDE BY ROOT + COMBINATORICS (PASCAL'S TRIANGLE)
    //       the first element is the root; the rest split into `left` (< root) and `right`
    //       (> root). Only the relative order INSIDE each side matters, so the two sides can be
    //       interleaved in C(left + right, left) ways:
    //         ways(seq) = C(|seq| - 1, |left|) * ways(left) * ways(right)
    //       the answer excludes the original ordering itself -> ways(nums) - 1.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int numOfWays(int[] nums) {
        int n = nums.length;

        // pascal[i][j] = C(i, j) mod MOD
        long[][] pascal = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            pascal[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                pascal[i][j] = (pascal[i - 1][j - 1] + pascal[i - 1][j]) % MOD;
            }
        }

        List<Integer> all = new ArrayList<>();
        for (int value : nums) {
            all.add(value);
        }
        long ways = countWays(all, pascal);
        // remove the original ordering; + MOD keeps the result non-negative
        return (int) ((ways - 1 + MOD) % MOD);
    }

    /** Orderings of `seq` (root first) that build the same BST, mod MOD. */
    private long countWays(List<Integer> seq, long[][] pascal) {
        // 0, 1 or 2 elements: only one order builds that tree
        if (seq.size() <= 2) {
            return 1;
        }
        int root = seq.get(0);
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int i = 1; i < seq.size(); i++) {
            if (seq.get(i) < root) {
                left.add(seq.get(i));
            } else {
                right.add(seq.get(i));
            }
        }

        // choose which of the |seq| - 1 slots after the root hold the left subtree's values
        long interleavings = pascal[seq.size() - 1][left.size()];
        long leftWays = countWays(left, pascal);
        long rightWays = countWays(right, pascal);
        return interleavings * leftWays % MOD * rightWays % MOD;
    }
}
