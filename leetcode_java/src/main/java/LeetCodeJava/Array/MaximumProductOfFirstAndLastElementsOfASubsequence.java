package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-product-of-first-and-last-elements-of-a-subsequence/description/

/**
 * 3584. Maximum Product of First and Last Elements of a Subsequence
 * Medium
 *
 * You are given an integer array nums and an integer m.
 *
 * Return the maximum product of the first and last elements of any subsequence of
 * nums of size m.
 *
 *
 * Example 1:
 *
 * Input: nums = [-1,-9,2,3,-2,-3,1], m = 1
 * Output: 81
 * Explanation: The subsequence [-9] has the largest product: -9 * -9 = 81.
 *
 * Example 2:
 *
 * Input: nums = [1,3,-5,5,6,-4], m = 3
 * Output: 20
 * Explanation: The subsequence [-5, 6, -4] has the largest product.
 *
 * Example 3:
 *
 * Input: nums = [2,-1,2,-6,5,2,-5,7], m = 2
 * Output: 35
 * Explanation: The subsequence [5, 7] has the largest product.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 * 1 <= m <= nums.length
 *
 */
public class MaximumProductOfFirstAndLastElementsOfASubsequence {

    // V0
    // IDEA: ONLY THE TWO ENDPOINTS MATTER -- RUNNING MAX / MIN AT A GAP OF m-1
    //   a size-m subsequence with endpoints i and j exists exactly when j - i >= m - 1,
    //   so the task is: maximise nums[i] * nums[j] over pairs at least m-1 apart.
    //   with negatives, the best partner of nums[j] is the max OR the min of the
    //   allowed prefix nums[0 .. j-m+1], both kept in O(1) as j advances.
    //   (checking every pair is O(n^2) and times out.)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maximumProduct(int[] nums, int m) {
        long prefixMax = Long.MIN_VALUE;
        long prefixMin = Long.MAX_VALUE;
        long best = Long.MIN_VALUE;

        for (int j = m - 1; j < nums.length; j++) {
            // index j - m + 1 just became a legal first element
            long candidateFirst = nums[j - m + 1];
            prefixMax = Math.max(prefixMax, candidateFirst);
            prefixMin = Math.min(prefixMin, candidateFirst);

            long last = nums[j];
            best = Math.max(best, prefixMax * last);
            best = Math.max(best, prefixMin * last);
        }
        return best;
    }
}
