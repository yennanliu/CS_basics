package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-alternating-subarray-sum/description/
// https://leetcode.ca/2021-06-27-2036-Maximum-Alternating-Subarray-Sum/

/**
 * 2036. Maximum Alternating Subarray Sum
 * Medium
 *
 * A subarray of a 0-indexed integer array is a contiguous non-empty sequence of elements within an array.
 *
 * The alternating subarray sum of a subarray that ranges from index i to j (inclusive, 0 <= i <= j <
 * nums.length) is nums[i] - nums[i+1] + nums[i+2] - ... +/- nums[j].
 *
 * Given a 0-indexed integer array nums, return the maximum alternating subarray sum of any subarray of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,-1,1,2]
 * Output: 5
 * Explanation: The subarray [3,-1,1] has the largest alternating subarray sum: 3 - (-1) + 1 = 5.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2,2,2]
 * Output: 2
 *
 * Example 3:
 *
 * Input: nums = [1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 *
 */
public class MaximumAlternatingSubarraySum {

    // V0
    // IDEA: KADANE WITH TWO STATES (THE SIGN THE CURRENT ELEMENT WAS ADDED WITH)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maximumAlternatingSubarraySum(int[] nums) {
        // a sentinel far below any real sum; Long.MIN_VALUE would overflow when added to
        final long NEG_INF = Long.MIN_VALUE / 4;

        // plus  = best sum of a subarray ending at i where nums[i] carries a '+'
        // minus = best sum of a subarray ending at i where nums[i] carries a '-'
        long plus = NEG_INF;
        long minus = NEG_INF;
        long best = NEG_INF;

        for (int x : nums) {
            // '+' : extend a subarray whose last sign was '-', or start fresh here
            long newPlus = Math.max(minus + x, x);
            // '-' : can only extend a subarray whose last sign was '+' (a subarray never starts with '-')
            long newMinus = plus - x;

            plus = newPlus;
            minus = newMinus;
            best = Math.max(best, Math.max(plus, minus));
        }
        return best;
    }
}
