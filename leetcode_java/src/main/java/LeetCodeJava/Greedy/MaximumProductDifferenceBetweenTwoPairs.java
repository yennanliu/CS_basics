package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-product-difference-between-two-pairs/description/

import java.util.*;

/**
 * 1913. Maximum Product Difference Between Two Pairs
 * Easy
 *
 * The product difference between two pairs (a, b) and (c, d) is defined as
 * (a * b) - (c * d).
 *
 * For example, the product difference between (5, 6) and (2, 7) is
 * (5 * 6) - (2 * 7) = 16.
 *
 * Given an integer array nums, choose four distinct indices w, x, y, and z such
 * that the product difference between pairs (nums[w], nums[x]) and
 * (nums[y], nums[z]) is maximized.
 *
 * Return the maximum such product difference.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,6,2,7,4]
 * Output: 34
 * Explanation: We can choose indices 1 and 3 for the first pair (6, 7) and indices
 * 2 and 4 for the second pair (2, 4). The product difference is (6 * 7) - (2 * 4) = 34.
 *
 * Example 2:
 *
 * Input: nums = [4,2,5,9,7,4,8]
 * Output: 64
 * Explanation: We can choose indices 3 and 6 for the first pair (9, 8) and indices
 * 1 and 5 for the second pair (2, 4). The product difference is (9 * 8) - (2 * 4) = 64.
 *
 *
 * Constraints:
 *
 * 4 <= nums.length <= 10^4
 * 1 <= nums[i] <= 10^4
 *
 */
public class MaximumProductDifferenceBetweenTwoPairs {

    // V0
    // IDEA: ONE PASS TRACKING THE TWO LARGEST AND TWO SMALLEST (all values are positive)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProductDifference(int[] nums) {
        // largest >= secondLargest, smallest <= secondSmallest
        int largest = 0;
        int secondLargest = 0;
        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;

        for (int x : nums) {
            if (x > largest) {
                secondLargest = largest;
                largest = x;
            } else if (x > secondLargest) {
                secondLargest = x;
            }

            if (x < smallest) {
                secondSmallest = smallest;
                smallest = x;
            } else if (x < secondSmallest) {
                secondSmallest = x;
            }
        }

        // max product minus min product; values are positive so these are the extremes
        return largest * secondLargest - smallest * secondSmallest;
    }

    // V1
    // IDEA: SORT, THEN USE BOTH ENDS
    /**
     * time = O(N log N)
     * space = O(N) for the sorted copy
     */
    public int maxProductDifference_1(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        return sorted[n - 1] * sorted[n - 2] - sorted[0] * sorted[1];
    }
}
