package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-strictly-increasing-subarrays/description/

/**
 * 2393. Count Strictly Increasing Subarrays
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * Return the number of subarrays of nums that are in strictly increasing order.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,4,4,6]
 * Output: 10
 * Explanation: The strictly increasing subarrays are the following:
 * - Subarrays of length 1: [1], [3], [5], [4], [4], [6].
 * - Subarrays of length 2: [1,3], [3,5], [4,6].
 * - Subarrays of length 3: [1,3,5].
 * The total number of subarrays is 6 + 3 + 1 = 10.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 15
 * Explanation: Every subarray is strictly increasing. There are 15 possible subarrays that we can take.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class CountStrictlyIncreasingSubarrays {

    // V0
    // IDEA: LENGTH OF THE INCREASING RUN ENDING AT EACH INDEX
    //  a subarray is strictly increasing iff it lies inside one maximal increasing run.
    //  if the run ending at i has length `run`, exactly `run` increasing subarrays end at i,
    //  so summing `run` over every i counts each of them once (a run of length L adds
    //  L * (L + 1) / 2 in total).
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countSubarrays(int[] nums) {
        long count = 0;
        long run = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i - 1] < nums[i]) {
                // nums[i] extends the run ending at i - 1
                run++;
            } else {
                // a new run starts at i
                run = 1;
            }
            count += run;
        }
        return count;
    }
}
