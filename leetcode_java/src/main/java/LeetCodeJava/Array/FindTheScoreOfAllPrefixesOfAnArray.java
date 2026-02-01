package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-score-of-all-prefixes-of-an-array/description/

import java.util.Arrays;

/**
 * 2640. Find the Score of All Prefixes of an Array
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * We define the conversion array conver of an array arr as follows:
 *
 * conver[i] = arr[i] + max(arr[0..i]) where max(arr[0..i]) is the maximum value of arr[j] over 0 <= j <= i.
 * We also define the score of an array arr as the sum of the values of the conversion array of arr.
 *
 * Given a 0-indexed integer array nums of length n, return an array ans of length n where ans[i] is the score of the prefix nums[0..i].
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,7,5,10]
 * Output: [4,10,24,36,56]
 * Explanation:
 * For the prefix [2], the conversion array is [4] hence the score is 4
 * For the prefix [2, 3], the conversion array is [4, 6] hence the score is 10
 * For the prefix [2, 3, 7], the conversion array is [4, 6, 14] hence the score is 24
 * For the prefix [2, 3, 7, 5], the conversion array is [4, 6, 14, 12] hence the score is 36
 * For the prefix [2, 3, 7, 5, 10], the conversion array is [4, 6, 14, 12, 20] hence the score is 56
 * Example 2:
 *
 * Input: nums = [1,1,2,4,8,16]
 * Output: [2,4,8,16,32,64]
 * Explanation:
 * For the prefix [1], the conversion array is [2] hence the score is 2
 * For the prefix [1, 1], the conversion array is [2, 2] hence the score is 4
 * For the prefix [1, 1, 2], the conversion array is [2, 2, 4] hence the score is 8
 * For the prefix [1, 1, 2, 4], the conversion array is [2, 2, 4, 8] hence the score is 16
 * For the prefix [1, 1, 2, 4, 8], the conversion array is [2, 2, 4, 8, 16] hence the score is 32
 * For the prefix [1, 1, 2, 4, 8, 16], the conversion array is [2, 2, 4, 8, 16, 32] hence the score is 64
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
public class FindTheScoreOfAllPrefixesOfAnArray {

    // V0
    // IDEA: ARRAY OP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long[] findPrefixScore(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        int n = nums.length;
        /** NOTE !!!
         *
         *  we use long type, to avoid overflow error
         */
        long[] cache = new long[n];
        long[] res = new long[n];
        int maxTillNow = 0;

        // Step 1: compute conversion array
        for (int i = 0; i < n; i++) {
            maxTillNow = Math.max(maxTillNow, nums[i]);
            cache[i] = nums[i] + (long) maxTillNow;
        }

        // Step 2: compute prefix sum of conversion array
        long accSum = 0;
        for (int i = 0; i < n; i++) {
            accSum += cache[i];
            res[i] = accSum;
        }

        return res;
    }

    // V1
    // IDEA: ARRAY OP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long[] findPrefixScore_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        long[] converArr = new long[nums.length];
        long[] ans = new long[nums.length];

        // Keep track of running maximum
        int maxSoFar = Integer.MIN_VALUE;
        long accSum = 0L;

        for (int i = 0; i < nums.length; i++) {
            maxSoFar = Math.max(maxSoFar, nums[i]);
            converArr[i] = (long) nums[i] + maxSoFar;
            accSum += converArr[i];
            ans[i] = accSum;
        }

        // Debug print (optional)
        System.out.println(">>> converArr = " + Arrays.toString(converArr) +
                ", ans = " + Arrays.toString(ans));

        return ans;
    }

    // V2
    // IDEA: ARRAY OP (fix by gemini)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long[] findPrefixScore_2(int[] nums) {
        // Edge case: Handle null or empty array
        if (nums == null || nums.length == 0) {
            return new long[0];
        }

        int n = nums.length;
        long[] ans = new long[n]; // The final prefix score array

        // Variable to track the running maximum value encountered so far (max(nums[0...i]))
        long runningMax = 0;

        // --- FIX 2: Use a 'long' for the accumulator to prevent overflow ---
        long currentPrefixScoreSum = 0;

        // Single pass to calculate the runningMax, conver value, and final prefix sum
        for (int i = 0; i < n; i++) {

            // Cast the current number to long for safe calculations
            long currentNum = (long) nums[i];

            // --- FIX 1: Correctly update the running maximum ---
            // The max of the prefix [0...i] is the max of the previous max and the current number.
            runningMax = Math.max(runningMax, currentNum);

            // Calculate conver[i] = nums[i] + max(nums[0...i])
            long conver_i = currentNum + runningMax;

            // Update the accumulated score sum
            currentPrefixScoreSum += conver_i;

            // Store the result in the final array
            ans[i] = currentPrefixScoreSum;
        }

        // Remove the System.out.println() for production code submission.
        // System.out.println(">>> ans = " + Arrays.toString(ans));

        return ans;
    }


    // V3

    // V4

}
