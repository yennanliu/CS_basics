package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/number-of-sub-arrays-of-size-k-and-average-greater-than-or-equal-to-threshold/description/

/**
 * 1343. Number of Sub-arrays of Size K and Average Greater than or Equal to Threshold
 * Medium
 *
 * Given an array of integers arr and two integers k and threshold, return the
 * number of sub-arrays of size k and average greater than or equal to threshold.
 *
 * Example 1:
 *
 * Input: arr = [2,2,2,2,5,5,5,8], k = 3, threshold = 4
 * Output: 3
 * Explanation: Sub-arrays [2,5,5],[5,5,5] and [5,5,8] have averages 4, 5 and 6
 * respectively. All other sub-arrays of size 3 have averages less than 4
 * (the threshold).
 *
 * Example 2:
 *
 * Input: arr = [11,13,17,23,29,31,7,5,2,3], k = 3, threshold = 5
 * Output: 6
 * Explanation: The first 6 sub-arrays of size 3 have averages greater than 5.
 * Note that averages are not integers.
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= arr[i] <= 10^4
 * 1 <= k <= arr.length
 * 0 <= threshold <= 10^4
 *
 */
public class NumberOfSubArraysOfSizeKAndAverageGreaterThanOrEqualToThreshold {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW
    //   compare the window SUM against threshold * k, so no float division is needed
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numOfSubarrays(int[] arr, int k, int threshold) {
        if (arr == null || k <= 0 || arr.length < k) {
            return 0;
        }
        // long: threshold * k and the window sum can reach 10^9
        long targetSum = (long) threshold * k;

        // the first window [0, k - 1]
        long windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }
        int count = 0;
        if (windowSum >= targetSum) {
            count++;
        }

        // slide: add the new right element, drop the old left one
        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k];
            if (windowSum >= targetSum) {
                count++;
            }
        }
        return count;
    }
}
