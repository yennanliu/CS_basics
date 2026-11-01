package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-subarray-sum-after-one-operation/description/
/**
 * 1746. Maximum Subarray Sum After One Operation
 * Medium
 *
 * You are given an integer array nums. You must perform exactly one operation where you
 * can replace one element nums[i] with nums[i] * nums[i].
 *
 * Return the maximum possible subarray sum after exactly one operation. The subarray
 * must be non-empty.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,-1,-4,-3]
 * Output: 17
 * Explanation: You can perform the operation on index 2 (0-indexed) to make
 * nums = [2,-1,16,-3]. Now, the maximum subarray sum is 2 + -1 + 16 = 17.
 *
 * Example 2:
 *
 * Input: nums = [1,-1,1,1,-1,-1,1]
 * Output: 4
 * Explanation: You can perform the operation on index 1 (0-indexed) to make
 * nums = [1,1,1,1,-1,-1,1], Now, the maximum subarray sum is 1 + 1 + 1 + 1 = 4.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 *
 */
public class MaximumSubarraySumAfterOneOperation {

    // V0
    // IDEA: KADANE WITH TWO STATES (operation not used yet / already used)
    /**
     *  DP def (for subarrays ENDING at the current index):
     *     - noOp   = best sum with no element squared
     *     - withOp = best sum with exactly one element squared
     *
     *  DP eq:
     *     - noOp'   = max(noOp, 0) + x
     *     - withOp' = max( max(noOp, 0) + x * x,   // square x itself
     *                      withOp + x )            // squared earlier, extend
     *
     *  Answer: max withOp over all positions.
     *
     *  time = O(N)
     *  space = O(1)
     */
    public int maxSumAfterOperation(int[] nums) {
        int noOp = 0;
        int withOp = 0;
        int best = Integer.MIN_VALUE;

        for (int x : nums) {
            // a negative prefix never helps, so restart from 0
            int startFromNoOp = Math.max(noOp, 0);

            int nextNoOp = startFromNoOp + x;
            int nextWithOp = Math.max(startFromNoOp + x * x, withOp + x);

            noOp = nextNoOp;
            withOp = nextWithOp;
            best = Math.max(best, withOp);
        }
        return best;
    }
}
