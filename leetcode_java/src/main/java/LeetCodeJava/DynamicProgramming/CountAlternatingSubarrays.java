package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-alternating-subarrays/description/

/**
 * 3101. Count Alternating Subarrays
 * Medium
 *
 * You are given a binary array nums.
 *
 * We call a subarray alternating if no two adjacent elements in the subarray have the same value.
 *
 * Return the number of alternating subarrays in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,1,1]
 * Output: 5
 * Explanation:
 * The following subarrays are alternating: [0], [1], [1], [1], and [0,1].
 *
 * Example 2:
 *
 * Input: nums = [1,0,1,0]
 * Output: 10
 * Explanation:
 * Every subarray of the array is alternating. There are 10 possible subarrays that we can choose.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class CountAlternatingSubarrays {

    // V0
    // IDEA: COUNT ALTERNATING SUBARRAYS BY THEIR RIGHT END
    //   run = length of the longest alternating stretch ending at i. Every suffix of it is
    //   alternating and ends at i, so index i contributes exactly `run` subarrays.
    //   run grows by 1 when nums[i] != nums[i-1], otherwise it restarts at 1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countAlternatingSubarrays(int[] nums) {
        long total = 0;
        int run = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] != nums[i - 1]) {
                run++;
            } else {
                run = 1;
            }
            total += run;
        }
        return total;
    }
}
