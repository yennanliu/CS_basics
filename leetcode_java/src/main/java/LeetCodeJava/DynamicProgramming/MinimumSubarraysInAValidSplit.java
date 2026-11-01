package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-subarrays-in-a-valid-split/description/

import java.util.*;

/**
 * 2464. Minimum Subarrays in a Valid Split
 * Medium
 *
 * You are given an integer array nums.
 *
 * Splitting of an integer array nums into subarrays is valid if:
 *
 * the greatest common divisor of the first and last elements of each subarray is greater than 1, and
 * each element of nums belongs to exactly one subarray.
 *
 * Return the minimum number of subarrays in a valid subarray splitting of nums. If a valid
 * subarray splitting is not possible, return -1.
 *
 * Note that:
 *
 * The greatest common divisor of two numbers is the largest positive integer that evenly divides both numbers.
 * A subarray is a contiguous non-empty part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,6,3,4,3]
 * Output: 2
 * Explanation: We can create a valid split in the following way: [2,6] | [3,4,3].
 *
 * Example 2:
 *
 * Input: nums = [3,5]
 * Output: 2
 * Explanation: We can create a valid split in the following way: [3] | [5].
 *
 * Example 3:
 *
 * Input: nums = [1,2,1]
 * Output: -1
 * Explanation: It is impossible to create a valid split.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 10^5
 *
 */
public class MinimumSubarraysInAValidSplit {

    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: PREFIX DP OVER CUT POSITIONS - dp[i] = min(dp[j] + 1) OVER j WITH gcd(nums[j], nums[i-1]) > 1
    /**
     * time = O(N^2 * log(max))
     * space = O(N)
     */
    public int validSubarraySplit(int[] nums) {
        int n = nums.length;

        // dp[i] = fewest subarrays covering nums[0 .. i-1]
        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            // the last subarray is nums[j .. i-1]; it is valid if its two ends share a factor
            for (int j = i - 1; j >= 0; j--) {
                if (dp[j] != INF && gcd(nums[j], nums[i - 1]) > 1) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }

        if (dp[n] == INF) {
            return -1;
        }
        return dp[n];
    }

    /** Euclid's algorithm. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
