package LeetCodeJava.Array;

// https://leetcode.com/problems/adjacent-increasing-subarrays-detection-ii/description/

import java.util.List;

/**
 * 3350. Adjacent Increasing Subarrays Detection II
 * Medium
 *
 * Given an array nums of n integers, find the maximum value of k for which there exist two adjacent subarrays of length k each, such that both subarrays are strictly increasing. Specifically, check if there are two subarrays of length k starting at indices a and b (a < b), where:
 *
 * Both subarrays nums[a..a + k - 1] and nums[b..b + k - 1] are strictly increasing.
 * The subarrays must be adjacent, meaning b = a + k.
 *
 * Return the maximum possible value of k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,7,8,9,2,3,4,3,1]
 * Output: 3
 * Explanation:
 * The subarray starting at index 2 is [7, 8, 9], which is strictly increasing.
 * The subarray starting at index 5 is [2, 3, 4], which is also strictly increasing.
 * These two subarrays are adjacent, and 3 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,4,4,4,5,6,7]
 * Output: 2
 * Explanation:
 * The subarray starting at index 0 is [1, 2], which is strictly increasing.
 * The subarray starting at index 2 is [3, 4], which is also strictly increasing.
 * These two subarrays are adjacent, and 2 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 2 * 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class AdjacentIncreasingSubarraysDetectionII {

    // V0
    // IDEA: MAXIMAL RISING RUNS (k fits inside one run as run / 2, or across a boundary as min(prev, cur))
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxIncreasingSubarrays(List<Integer> nums) {
        int n = nums.size();
        int best = 0;
        int previousRun = 0;  // length of the run just before the current one
        int currentRun = 1;   // length of the strictly increasing run ending at i

        for (int i = 1; i < n; i++) {
            if (nums.get(i) > nums.get(i - 1)) {
                currentRun++;
            } else {
                // the current run closed: score it, then start a new one
                best = Math.max(best, scoreRuns(previousRun, currentRun));
                previousRun = currentRun;
                currentRun = 1;
            }
        }
        // score the last run, which never hit a break
        best = Math.max(best, scoreRuns(previousRun, currentRun));
        return best;
    }

    /** Best k using one run alone (run / 2) or the boundary between two runs (the shorter one). */
    private int scoreRuns(int previousRun, int currentRun) {
        return Math.max(currentRun / 2, Math.min(previousRun, currentRun));
    }
}
