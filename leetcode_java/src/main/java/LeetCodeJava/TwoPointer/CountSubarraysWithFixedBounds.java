package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-subarrays-with-fixed-bounds/description/

/**
 * 2444. Count Subarrays With Fixed Bounds
 * Hard
 *
 * You are given an integer array nums and two integers minK and maxK.
 *
 * A fixed-bound subarray of nums is a subarray that satisfies the following conditions:
 *
 * The minimum value in the subarray is equal to minK.
 * The maximum value in the subarray is equal to maxK.
 *
 * Return the number of fixed-bound subarrays.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,2,7,5], minK = 1, maxK = 5
 * Output: 2
 * Explanation: The fixed-bound subarrays are [1,3,5] and [1,3,5,2].
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1], minK = 1, maxK = 1
 * Output: 10
 * Explanation: Every subarray of nums is a fixed-bound subarray. There are 10 possible subarrays.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i], minK, maxK <= 10^6
 *
 */
public class CountSubarraysWithFixedBounds {

    // V0
    // IDEA: THREE POINTERS — LAST OUT-OF-RANGE INDEX, LAST minK INDEX, LAST maxK INDEX
    //  fix the right end i. nums[l .. i] is valid iff
    //    - it holds no value outside [minK, maxK]  -> l > lastBad
    //    - it holds at least one minK              -> l <= lastMin
    //    - it holds at least one maxK              -> l <= lastMax
    //  so the valid starts are (lastBad, min(lastMin, lastMax)], which is
    //  max(0, min(lastMin, lastMax) - lastBad) of them.
    //  all three start at -1, which makes "not seen yet" contribute 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countSubarrays(int[] nums, int minK, int maxK) {
        long count = 0;
        int lastBad = -1;
        int lastMin = -1;
        int lastMax = -1;
        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            if (value < minK || value > maxK) {
                lastBad = i;
            }
            if (value == minK) {
                lastMin = i;
            }
            if (value == maxK) {
                lastMax = i;
            }
            int validStarts = Math.min(lastMin, lastMax) - lastBad;
            if (validStarts > 0) {
                count += validStarts;
            }
        }
        return count;
    }
}
