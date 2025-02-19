package LeetCodeJava.Array;

// https://leetcode.com/problems/median-of-two-sorted-arrays/description/
/**
 * 4. Median of Two Sorted Arrays
 * Solved
 * Hard
 * Topics
 * Companies
 * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
 *
 * The overall run time complexity should be O(log (m+n)).
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,3], nums2 = [2]
 * Output: 2.00000
 * Explanation: merged array = [1,2,3] and median is 2.
 * Example 2:
 *
 * Input: nums1 = [1,2], nums2 = [3,4]
 * Output: 2.50000
 * Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 *
 *
 * Constraints:
 *
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -106 <= nums1[i], nums2[i] <= 106
 *
 */
public class MedianOfTwoSortedArrays {

    // V0
//    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
//
//    }

    // V1
    // https://leetcode.com/problems/median-of-two-sorted-arrays/solutions/4070500/99journey-from-brute-force-to-most-optim-z3k8/

    // V2
    // https://leetcode.com/problems/median-of-two-sorted-arrays/solutions/6401742/1ms-100-beats-single-loop-beginner-frien-6bqd/

    // V3
    public double findMedianSortedArrays_3(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays_3(nums2, nums1); // Ensure nums1 is the smaller array
        }

        int len1 = nums1.length;
        int len2 = nums2.length;
        int left = 0, right = len1;

        while (left <= right) {
            int partition1 = (left + right) / 2;
            int partition2 = (len1 + len2 + 1) / 2 - partition1;

            int maxLeft1 = (partition1 == 0) ? Integer.MIN_VALUE : nums1[partition1 - 1];
            int minRight1 = (partition1 == len1) ? Integer.MAX_VALUE : nums1[partition1];

            int maxLeft2 = (partition2 == 0) ? Integer.MIN_VALUE : nums2[partition2 - 1];
            int minRight2 = (partition2 == len2) ? Integer.MAX_VALUE : nums2[partition2];

            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // Correct partition found
                if ((len1 + len2) % 2 == 0) {
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                } else {
                    return Math.max(maxLeft1, maxLeft2);
                }
            } else if (maxLeft1 > minRight2) {
                // Move partition1 to the left
                right = partition1 - 1;
            } else {
                // Move partition1 to the right
                left = partition1 + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted.");
    }

    // V4
}
