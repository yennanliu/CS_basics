package LeetCodeJava.Math;

// https://leetcode.com/problems/reverse-subarray-to-maximize-array-value/description/

/**
 * 1330. Reverse Subarray To Maximize Array Value
 * Hard
 *
 * You are given an integer array nums. The value of this array is defined as the sum of
 * |nums[i] - nums[i + 1]| for all 0 <= i < nums.length - 1.
 *
 * You are allowed to select any subarray of the given array and reverse it. You can perform this
 * operation only once.
 *
 * Find maximum possible value of the final array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,1,5,4]
 * Output: 10
 * Explanation: By reversing the subarray [3,1,5] the array becomes [2,5,1,3,4] whose value is 10.
 *
 * Example 2:
 *
 * Input: nums = [2,4,9,24,2,1,10]
 * Output: 68
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 3 * 10^4
 * -10^5 <= nums[i] <= 10^5
 * The answer is guaranteed to fit in a 32-bit integer.
 *
 */
public class ReverseSubarrayToMaximizeArrayValue {

    // V0
    // IDEA: A REVERSAL ONLY REWIRES ITS TWO SEAMS -> MAXIMISE THE GAIN AT THE SEAMS
    //   reversing nums[i..j] keeps every inner |a - b|; only the pairs at i-1|i and j|j+1 change.
    //   1) prefix reversal (i = 0): the seam at k|k+1 becomes |nums[0] - nums[k+1]|
    //   2) suffix reversal (j = n-1): the seam at k|k+1 becomes |nums[n-1] - nums[k]|
    //   3) interior reversal: expanding the absolute values, the best gain is
    //      2 * (max over adjacent pairs of min(a, b) - min over adjacent pairs of max(a, b)),
    //      which is only useful when positive (one pair lies entirely above another).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxValueAfterReverse(int[] nums) {
        int n = nums.length;

        // value of the array as given
        int base = 0;
        for (int i = 0; i < n - 1; i++) {
            base += Math.abs(nums[i] - nums[i + 1]);
        }
        int best = base;

        // 1) and 2): reversing a prefix or a suffix moves only one seam
        for (int i = 0; i < n - 1; i++) {
            int seam = Math.abs(nums[i] - nums[i + 1]);
            best = Math.max(best, base - seam + Math.abs(nums[0] - nums[i + 1]));
            best = Math.max(best, base - seam + Math.abs(nums[n - 1] - nums[i]));
        }

        // 3) interior reversal: the highest "low end" against the lowest "high end"
        int maxOfPairMin = Integer.MIN_VALUE;
        int minOfPairMax = Integer.MAX_VALUE;
        for (int i = 0; i < n - 1; i++) {
            int low = Math.min(nums[i], nums[i + 1]);
            int high = Math.max(nums[i], nums[i + 1]);
            maxOfPairMin = Math.max(maxOfPairMin, low);
            minOfPairMax = Math.min(minOfPairMax, high);
        }
        best = Math.max(best, base + 2 * (maxOfPairMin - minOfPairMax));

        return best;
    }
}
