package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-subarray-sum-with-one-deletion/description/

/**
 * 1186. Maximum Subarray Sum with One Deletion
 * Medium
 *
 * Given an array of integers, return the maximum sum for a non-empty subarray
 * (contiguous elements) with at most one element deletion. In other words, you want
 * to choose a subarray and optionally delete one element from it so that there is
 * still at least one element left and the sum of the remaining elements is maximum
 * possible.
 *
 * Note that the subarray needs to be non-empty after deleting one element.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,-2,0,3]
 * Output: 4
 * Explanation: Because we can choose [1, -2, 0, 3] and drop -2, thus the subarray
 * [1, 0, 3] becomes the maximum value.
 *
 * Example 2:
 *
 * Input: arr = [1,-2,-2,3]
 * Output: 3
 * Explanation: We just choose [3] and it's the maximum sum.
 *
 * Example 3:
 *
 * Input: arr = [-1,-1,-1,-1]
 * Output: -1
 * Explanation: The final subarray needs to be non-empty. You can't choose [-1] and
 * delete -1 from it, then get an empty subarray to make the sum equals to 0.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * -10^4 <= arr[i] <= 10^4
 *
 */
public class MaximumSubarraySumWithOneDeletion {

    // V0
    // IDEA: KADANE WITH 2 STATES (DP)
    /**
     * noDelete  = max subarray sum ending at i, with NO deletion used
     * oneDelete = max subarray sum ending at i, with EXACTLY one deletion used
     *             (still non-empty: it is only ever built from a previous non-empty noDelete)
     *
     * oneDelete = max(oneDelete_prev + x,   // keep x, the deletion was spent earlier
     *                 noDelete_prev)        // delete x itself
     * noDelete  = max(noDelete_prev + x, x) // plain Kadane
     *
     * NOTE: oneDelete is updated BEFORE noDelete, since it needs the previous noDelete
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumSum(int[] arr) {
        int noDelete = arr[0];
        // no deletion is possible yet with a single element; a very small sentinel
        // (not MIN_VALUE, so adding x cannot overflow)
        int oneDelete = Integer.MIN_VALUE / 2;
        int best = arr[0];

        for (int i = 1; i < arr.length; i++) {
            int x = arr[i];
            oneDelete = Math.max(oneDelete + x, noDelete);
            noDelete = Math.max(noDelete + x, x);
            best = Math.max(best, Math.max(noDelete, oneDelete));
        }
        return best;
    }

    // V1
    // IDEA: PREFIX (ENDING AT i) + SUFFIX (STARTING AT i) MAX SUBARRAY SUMS
    /**
     * endingAt[i]   = max subarray sum ending at i
     * startingAt[i] = max subarray sum starting at i
     * deleting arr[i] joins the best run ending at i-1 with the best run starting at i+1:
     *   endingAt[i-1] + startingAt[i+1]
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumSum_1(int[] arr) {
        int n = arr.length;

        int[] endingAt = new int[n];
        endingAt[0] = arr[0];
        for (int i = 1; i < n; i++) {
            endingAt[i] = Math.max(endingAt[i - 1], 0) + arr[i];
        }

        int[] startingAt = new int[n];
        startingAt[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            startingAt[i] = Math.max(startingAt[i + 1], 0) + arr[i];
        }

        // no deletion: the best plain subarray
        int best = endingAt[0];
        for (int i = 1; i < n; i++) {
            best = Math.max(best, endingAt[i]);
        }

        // delete arr[i] for an interior i
        for (int i = 1; i < n - 1; i++) {
            best = Math.max(best, endingAt[i - 1] + startingAt[i + 1]);
        }
        return best;
    }
}
