package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-power-of-k-size-subarrays-i/description/

/**
 * 3254. Find the Power of K-Size Subarrays I
 * Medium
 *
 * You are given an array of integers nums of length n and a positive integer k.
 *
 * The power of an array is defined as:
 *
 * Its maximum element if all of its elements are consecutive and sorted in ascending order.
 * -1 otherwise.
 *
 * You need to find the power of all subarrays of nums of size k.
 *
 * Return an integer array results of size n - k + 1, where results[i] is the power of nums[i..(i + k - 1)].
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,3,2,5], k = 3
 * Output: [3,4,-1,-1,-1]
 * Explanation:
 * There are 5 subarrays of nums of size 3:
 * [1, 2, 3] with the maximum element 3.
 * [2, 3, 4] with the maximum element 4.
 * [3, 4, 3] whose elements are not consecutive.
 * [4, 3, 2] whose elements are not sorted.
 * [3, 2, 5] whose elements are not consecutive.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2,2,2], k = 4
 * Output: [-1,-1]
 *
 * Example 3:
 *
 * Input: nums = [3,2,3,2,3,2], k = 2
 * Output: [-1,3,-1,3,-1]
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 500
 * 1 <= nums[i] <= 10^5
 * 1 <= k <= n
 *
 */
public class FindThePowerOfKSizeSubarraysI {

    // V0
    // IDEA: BRUTE FORCE PER WINDOW ("consecutive and ascending" == every step is exactly +1)
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public int[] resultsArray(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];

        for (int start = 0; start + k <= n; start++) {
            boolean consecutive = true;
            for (int t = start; t < start + k - 1; t++) {
                if (nums[t + 1] != nums[t] + 1) {
                    consecutive = false;
                    break;
                }
            }
            // a valid window is ascending, so its maximum is its last element
            result[start] = consecutive ? nums[start + k - 1] : -1;
        }
        return result;
    }

    // V1
    // IDEA: RUNNING STREAK LENGTH (one pass; the approach the sequel LC 3255 needs)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] resultsArray_1(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];

        // streak = length of the "+1 each step" run ending at index i
        int streak = 0;
        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i] == nums[i - 1] + 1) {
                streak++;
            } else {
                streak = 1;
            }

            // window ending at i starts at i - k + 1
            int start = i - k + 1;
            if (start >= 0) {
                result[start] = streak >= k ? nums[i] : -1;
            }
        }
        return result;
    }
}
