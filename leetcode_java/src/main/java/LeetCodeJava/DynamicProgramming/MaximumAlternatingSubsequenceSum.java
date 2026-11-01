package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-alternating-subsequence-sum/description/

/**
 * 1911. Maximum Alternating Subsequence Sum
 * Medium
 *
 * The alternating sum of a 0-indexed array is defined as the sum of the elements
 * at even indices minus the sum of the elements at odd indices.
 *
 * For example, the alternating sum of [4,2,5,3] is (4 + 5) - (2 + 3) = 4.
 *
 * Given an array nums, return the maximum alternating sum of any subsequence of
 * nums (after reindexing the elements of the subsequence).
 *
 * A subsequence of an array is a new array generated from the original array by
 * deleting some elements (possibly none) without changing the remaining
 * elements' relative order. For example, [2,7,4] is a subsequence of
 * [4,2,3,7,2,1,4], while [2,4,2] is not.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,5,3]
 * Output: 7
 * Explanation: It is optimal to choose the subsequence [4,2,5] with alternating sum (4 + 5) - 2 = 7.
 *
 * Example 2:
 *
 * Input: nums = [5,6,7,8]
 * Output: 8
 * Explanation: It is optimal to choose the subsequence [8] with alternating sum 8.
 *
 * Example 3:
 *
 * Input: nums = [6,2,1,2,4,5]
 * Output: 10
 * Explanation: It is optimal to choose the subsequence [6,1,5] with alternating sum (6 + 5) - 1 = 10.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MaximumAlternatingSubsequenceSum {

    // V0
    // IDEA: DP ON THE PARITY OF THE PICKED LENGTH (2 rolling states, "stock trading" shape)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxAlternatingSum(int[] nums) {
        // oddLength  = best sum of a subsequence of odd length (last pick was ADDED)
        // evenLength = best sum of a subsequence of even length (last pick was SUBTRACTED)
        // sums reach 10^5 * 10^5 = 10^10 -> long
        long oddLength = 0;
        long evenLength = 0;

        for (int x : nums) {
            // both transitions must read the values from before this element
            long nextOdd = Math.max(oddLength, evenLength + x);   // x lands on an even index -> added
            long nextEven = Math.max(evenLength, oddLength - x);  // x lands on an odd index -> subtracted
            oddLength = nextOdd;
            evenLength = nextEven;
        }
        return Math.max(oddLength, evenLength);
    }

    // V1
    // IDEA: GREEDY (sum of every rise, like LC 122 stock II, plus the first element)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxAlternatingSum_1(int[] nums) {
        // take nums[0], then every upward step: buy at a valley (subtract), sell at a peak (add)
        long total = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                total += nums[i] - nums[i - 1];
            }
        }
        return total;
    }
}
