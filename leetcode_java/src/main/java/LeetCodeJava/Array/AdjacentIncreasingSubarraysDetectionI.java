package LeetCodeJava.Array;

// https://leetcode.com/problems/adjacent-increasing-subarrays-detection-i/description/

import java.util.List;

/**
 * 3349. Adjacent Increasing Subarrays Detection I
 * Easy
 *
 * Given an array nums of n integers and an integer k, determine whether there exist two adjacent subarrays of length k such that both subarrays are strictly increasing. Specifically, check if there are two subarrays starting at indices a and b (a < b), where:
 *
 * Both subarrays nums[a..a + k - 1] and nums[b..b + k - 1] are strictly increasing.
 * The subarrays must be adjacent, meaning b = a + k.
 *
 * Return true if it is possible to find two such subarrays, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,7,8,9,2,3,4,3,1], k = 3
 * Output: true
 * Explanation:
 * The subarray starting at index 2 is [7, 8, 9], which is strictly increasing.
 * The subarray starting at index 5 is [2, 3, 4], which is also strictly increasing.
 * These two subarrays are adjacent, so the result is true.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,4,4,4,5,6,7], k = 5
 * Output: false
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 < 2 * k <= nums.length
 * -1000 <= nums[i] <= 1000
 *
 */
public class AdjacentIncreasingSubarraysDetectionI {

    // V0
    // IDEA: CHECK EVERY START a (both nums[a..a+k-1] and nums[a+k..a+2k-1] must rise strictly)
    /**
     * time = O(N * K)
     * space = O(1)
     */
    public boolean hasIncreasingSubarrays(List<Integer> nums, int k) {
        int n = nums.size();
        for (int a = 0; a + 2 * k <= n; a++) {
            if (isStrictlyIncreasing(nums, a, k) && isStrictlyIncreasing(nums, a + k, k)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true if nums[start .. start + length - 1] is strictly increasing. */
    private boolean isStrictlyIncreasing(List<Integer> nums, int start, int length) {
        for (int i = start; i < start + length - 1; i++) {
            if (nums.get(i) >= nums.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: RUN LENGTHS OF STRICTLY INCREASING STRETCHES (answer is true iff the best k from LC 3350 >= k)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasIncreasingSubarrays_1(List<Integer> nums, int k) {
        int previousRun = 0;
        int currentRun = 1;
        int bestK = 0;
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i) > nums.get(i - 1)) {
                currentRun++;
            } else {
                previousRun = currentRun;
                currentRun = 1;
            }
            // both blocks inside the current run, or straddling the previous / current boundary
            bestK = Math.max(bestK, currentRun / 2);
            bestK = Math.max(bestK, Math.min(previousRun, currentRun));
        }
        return bestK >= k;
    }
}
