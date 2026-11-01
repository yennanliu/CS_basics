package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-score-of-spliced-array/description/

import java.util.*;

/**
 * 2321. Maximum Score Of Spliced Array
 * Hard
 *
 * You are given two 0-indexed integer arrays nums1 and nums2, both of length n.
 *
 * You can choose two integers left and right where 0 <= left <= right < n and swap the subarray nums1[left...right] with the subarray nums2[left...right].
 *
 * For example, if nums1 = [1,2,3,4,5] and nums2 = [11,12,13,14,15] and you choose left = 1 and right = 2, nums1 becomes [1,12,13,4,5] and nums2 becomes [11,2,3,14,15].
 *
 * You may choose to apply the mentioned operation once or not do anything.
 *
 * The score of the arrays is the maximum of sum(nums1) and sum(nums2), where sum(arr) is the sum of all the elements in the array arr.
 *
 * Return the maximum possible score.
 *
 * A subarray is a contiguous sequence of elements within an array. arr[left...right] denotes the subarray that contains the elements of nums starting at index left and ending at index right (inclusive).
 *
 *
 * Example 1:
 *
 * Input: nums1 = [60,60,60], nums2 = [10,90,10]
 * Output: 210
 * Explanation: Choosing left = 1 and right = 1, we have nums1 = [60,90,60] and nums2 = [10,60,10].
 * The score is max(sum(nums1), sum(nums2)) = max(210, 80) = 210.
 *
 * Example 2:
 *
 * Input: nums1 = [20,40,20,70,30], nums2 = [50,20,50,40,20]
 * Output: 220
 * Explanation: Choosing left = 3, right = 4, we have nums1 = [20,40,20,40,20] and nums2 = [50,20,50,70,30].
 * The score is max(sum(nums1), sum(nums2)) = max(140, 220) = 220.
 *
 * Example 3:
 *
 * Input: nums1 = [7,11,13], nums2 = [1,1,1]
 * Output: 31
 * Explanation: We choose not to swap any subarray.
 * The score is max(sum(nums1), sum(nums2)) = max(31, 3) = 31.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * 1 <= nums1[i], nums2[i] <= 10^5
 *
 */
public class MaximumScoreOfSplicedArray {

    // V0
    // IDEA: KADANE ON THE DIFFERENCE ARRAY
    //       swapping [l, r] changes sum(nums1) by sum(nums2[i] - nums1[i]) over the window,
    //       so the best nums1 total is sum(nums1) + max subarray sum of (nums2 - nums1),
    //       clamped at 0 because the swap is optional. Same for nums2, then take the larger.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumsSplicedArray(int[] nums1, int[] nums2) {
        long sum1 = 0;
        long sum2 = 0;
        for (int i = 0; i < nums1.length; i++) {
            sum1 += nums1[i];
            sum2 += nums2[i];
        }

        long bestForNums1 = sum1 + bestGain(nums1, nums2);
        long bestForNums2 = sum2 + bestGain(nums2, nums1);
        return (int) Math.max(bestForNums1, bestForNums2);
    }

    /** Max subarray sum of (to[i] - from[i]), at least 0 (the empty window). */
    private long bestGain(int[] from, int[] to) {
        long best = 0;
        long current = 0;
        for (int i = 0; i < from.length; i++) {
            // extend the window, or restart it if it has gone negative
            current = Math.max(0, current + to[i] - from[i]);
            best = Math.max(best, current);
        }
        return best;
    }
}
