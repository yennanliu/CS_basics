package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/k-radius-subarray-averages/description/

import java.util.Arrays;

/**
 * 2090. K Radius Subarray Averages
 * Medium
 *
 * You are given a 0-indexed array nums of n integers, and an integer k.
 *
 * The k-radius average for a subarray of nums centered at some index i with the radius k is the average of all elements in nums between the indices i - k and i + k (inclusive). If there are less than k elements before or after the index i, then the k-radius average is -1.
 *
 * Build and return an array avgs of length n where avgs[i] is the k-radius average for the subarray centered at index i.
 *
 * The average of x elements is the sum of the x elements divided by x, using integer division. The integer division truncates toward zero, which means losing its fractional part.
 *
 * For example, the average of four elements 2, 3, 1, and 5 is (2 + 3 + 1 + 5) / 4 = 11 / 4 = 2.75, which truncates to 2.
 *
 *
 * Example 1:
 *
 * Input: nums = [7,4,3,9,1,8,5,2,6], k = 3
 * Output: [-1,-1,-1,5,4,4,-1,-1,-1]
 * Explanation:
 * - avg[0], avg[1], and avg[2] are -1 because there are less than k elements before each index.
 * - The sum of the subarray centered at index 3 with radius 3 is: 7 + 4 + 3 + 9 + 1 + 8 + 5 = 37.
 *   Using integer division, avg[3] = 37 / 7 = 5.
 * - For the subarray centered at index 4, avg[4] = (4 + 3 + 9 + 1 + 8 + 5 + 2) / 7 = 4.
 * - For the subarray centered at index 5, avg[5] = (3 + 9 + 1 + 8 + 5 + 2 + 6) / 7 = 4.
 * - avg[6], avg[7], and avg[8] are -1 because there are less than k elements after each index.
 *
 * Example 2:
 *
 * Input: nums = [100000], k = 0
 * Output: [100000]
 * Explanation:
 * - The sum of the subarray centered at index 0 with radius 0 is: 100000.
 *   avg[0] = 100000 / 1 = 100000.
 *
 * Example 3:
 *
 * Input: nums = [8], k = 100000
 * Output: [-1]
 * Explanation:
 * - avg[0] is -1 because there are less than k elements before and after index 0.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^5
 * 0 <= nums[i], k <= 10^5
 *
 */
public class KRadiusSubarrayAverages {

    // V0
    // IDEA: PREFIX SUM OVER A FIXED WINDOW OF WIDTH 2k + 1
    //  index i has a full window iff k <= i <= n - 1 - k; its sum is
    //  prefix[i + k + 1] - prefix[i - k]. every other index stays -1.
    //  NOTE: 2k + 1 can exceed n (k up to 1e5 with n == 1), then the answer is all -1.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] getAverages(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        // long: the window width and its sum can both pass Integer.MAX_VALUE
        long width = 2L * k + 1;
        if (width > n) {
            return result;
        }

        // prefix[i] = nums[0] + ... + nums[i - 1]
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        for (int i = k; i < n - k; i++) {
            long windowSum = prefix[i + k + 1] - prefix[i - k];
            result[i] = (int) (windowSum / width);
        }
        return result;
    }

    // V1
    // IDEA: SLIDING WINDOW WITH A RUNNING SUM (no prefix array)
    /**
     * time = O(N)
     * space = O(1)   (besides the output)
     */
    public int[] getAverages_1(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        long width = 2L * k + 1;
        if (width > n) {
            return result;
        }

        long windowSum = 0;
        for (int right = 0; right < n; right++) {
            windowSum += nums[right];
            // once the window holds 2k + 1 elements, drop the one that fell off the left
            if (right >= width) {
                windowSum -= nums[right - (int) width];
            }
            // the full window nums[right - 2k .. right] is centred at right - k
            if (right >= width - 1) {
                result[right - k] = (int) (windowSum / width);
            }
        }
        return result;
    }
}
