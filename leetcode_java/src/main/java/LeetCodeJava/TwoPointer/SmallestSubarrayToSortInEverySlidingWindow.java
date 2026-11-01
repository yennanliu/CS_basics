package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/smallest-subarray-to-sort-in-every-sliding-window/description/

/**
 * 3555. Smallest Subarray to Sort in Every Sliding Window
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * For each contiguous subarray of length k, determine the minimum length of a continuous segment
 * that must be sorted so that the entire window becomes non-decreasing; if the window is already
 * sorted, its required length is zero.
 *
 * Return an array of length n - k + 1 where each element corresponds to the answer for its
 * window.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,4,5], k = 3
 * Output: [2,2,0]
 * Explanation:
 * nums[0...2] = [1, 3, 2]. Sort [3, 2] to get [1, 2, 3], the answer is 2.
 * nums[1...3] = [3, 2, 4]. Sort [3, 2] to get [2, 3, 4], the answer is 2.
 * nums[2...4] = [2, 4, 5] is already sorted, so the answer is 0.
 *
 * Example 2:
 *
 * Input: nums = [5,4,3,2,1], k = 4
 * Output: [4,4]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= k <= nums.length
 * 1 <= nums[i] <= 10^6
 *
 */
public class SmallestSubarrayToSortInEverySlidingWindow {

    // V0
    // IDEA: PER WINDOW, TWO SCANS (SAME AS LC 581 "SHORTEST UNSORTED CONTINUOUS SUBARRAY")
    //  left to right with the running max: any i with w[i] < max(before) must be inside the
    //  segment -> the last such i is the segment's right end. Right to left with the running
    //  min: any i with w[i] > min(after) must be inside -> the last such i is the left end.
    //  n <= 1000, so each window can afford its own O(k) scans.
    /**
     * time = O(N * K)
     * space = O(1) besides the output
     */
    public int[] minSubarraySort(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];

        for (int start = 0; start + k <= n; start++) {
            int end = start + k - 1;

            // right end of the segment: last position smaller than something before it
            int segmentEnd = -1;
            int runningMax = nums[start];
            for (int i = start + 1; i <= end; i++) {
                if (nums[i] < runningMax) {
                    segmentEnd = i;
                } else {
                    runningMax = nums[i];
                }
            }
            if (segmentEnd == -1) {
                // window already sorted
                result[start] = 0;
                continue;
            }

            // left end of the segment: last position (scanning leftward) larger than something after it
            int segmentStart = end;
            int runningMin = nums[end];
            for (int i = end - 1; i >= start; i--) {
                if (nums[i] > runningMin) {
                    segmentStart = i;
                } else {
                    runningMin = nums[i];
                }
            }
            result[start] = segmentEnd - segmentStart + 1;
        }
        return result;
    }
}
