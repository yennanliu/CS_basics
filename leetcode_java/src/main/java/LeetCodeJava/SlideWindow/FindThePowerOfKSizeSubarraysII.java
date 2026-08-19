package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/find-the-power-of-k-size-subarrays-ii/

/**
 *  3255. Find the Power of K-Size Subarrays II
 *  Medium
 *
 *  You are given an array of integers nums of length n and a positive integer k.
 *
 *  The power of an array is defined as:
 *   - Its maximum element if all of its elements are consecutive and sorted in
 *     ascending order.
 *   - -1 otherwise.
 *
 *  You need to find the power of all subarrays of nums of size k.
 *
 *  Return an integer array results of size n - k + 1, where results[i] is the
 *  power of nums[i..(i + k - 1)].
 *
 *  Example 1:
 *    Input: nums = [1,2,3,4,3,2,5], k = 3
 *    Output: [3,4,-1,-1,-1]
 *
 *  Example 2:
 *    Input: nums = [2,2,2,2,2], k = 4
 *    Output: [-1,-1]
 *
 *  Example 3:
 *    Input: nums = [3,2,3,2,3,2], k = 2
 *    Output: [-1,3,-1,3,-1]
 *
 *  Constraints:
 *    1 <= n == nums.length <= 10^5
 *    1 <= nums[i] <= 10^6
 *    1 <= k <= n
 */
public class FindThePowerOfKSizeSubarraysII {

    // V0
    // IDEA: ONE RUNNING STREAK OF "+1 STEPS" ANSWERS EVERY WINDOW
    //       track `run` = the length of the longest consecutive-ascending
    //       stretch ending at the current index. it extends when
    //       nums[i] == nums[i-1] + 1 and resets to 1 otherwise.
    //       the window ending at i lies fully inside that stretch exactly when
    //       run >= k, in which case its power is nums[i]; otherwise -1.
    //       this is LC 3254 at 10^5 elements, where re-scanning each window
    //       would be O(N * K).
    /**
     * time = O(N)
     * space = O(1)   // excluding the output array
     */
    public int[] resultsArray(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        int run = 1;
        for (int i = 0; i < n; i++) {
            if (i > 0 && nums[i] == nums[i - 1] + 1) {
                run++;
            } else {
                run = 1;
            }
            if (i >= k - 1) {
                res[i - k + 1] = (run >= k) ? nums[i] : -1;
            }
        }
        return res;
    }
}
