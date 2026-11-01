package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-even-odd-subarray-with-threshold/description/

/**
 * 2760. Longest Even Odd Subarray With Threshold
 * Easy
 *
 * You are given a 0-indexed integer array nums and an integer threshold.
 *
 * Find the length of the longest subarray of nums starting at index l and ending at index r (0 <= l <= r < nums.length) that satisfies the following conditions:
 *
 * nums[l] % 2 == 0
 * For all indices i in the range [l, r - 1], nums[i] % 2 != nums[i + 1] % 2
 * For all indices i in the range [l, r], nums[i] <= threshold
 *
 * Return an integer denoting the length of the longest such subarray.
 *
 * Note: A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,5,4], threshold = 5
 * Output: 3
 * Explanation: In this example, we can select the subarray that starts at l = 1 and ends at r = 3 => [2,5,4]. This subarray satisfies the conditions.
 * Hence, the answer is the length of the subarray, 3. We can show that 3 is the maximum possible achievable length.
 *
 * Example 2:
 *
 * Input: nums = [1,2], threshold = 2
 * Output: 1
 * Explanation: In this example, we can select the subarray that starts at l = 1 and ends at r = 1 => [2].
 * It satisfies all the conditions and we can show that 1 is the maximum possible achievable length.
 *
 * Example 3:
 *
 * Input: nums = [2,3,4,5], threshold = 4
 * Output: 3
 * Explanation: In this example, we can select the subarray that starts at l = 0 and ends at r = 2 => [2,3,4].
 * It satisfies all the conditions.
 * Hence, the answer is the length of the subarray, 3. We can show that 3 is the maximum possible achievable length.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 * 1 <= threshold <= 100
 *
 */
public class LongestEvenOddSubarrayWithThreshold {

    // V0
    // IDEA: GREEDY SCAN WITHOUT BACKTRACKING (two pointers)
    //       from a valid start (even and <= threshold) extend right while the parity keeps
    //       alternating and values stay <= threshold. once a stretch [left, right) stops, no
    //       index inside it can start a longer one (its stretch would be a suffix), so the
    //       next start jumps straight to right.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestAlternatingSubarray(int[] nums, int threshold) {
        int n = nums.length;
        int longest = 0;
        int left = 0;
        while (left < n) {
            // a start must be even and within the threshold
            if (nums[left] % 2 != 0 || nums[left] > threshold) {
                left++;
                continue;
            }
            int right = left + 1;
            while (right < n && nums[right] <= threshold && nums[right] % 2 != nums[right - 1] % 2) {
                right++;
            }
            longest = Math.max(longest, right - left);
            left = right;
        }
        return longest;
    }
}
