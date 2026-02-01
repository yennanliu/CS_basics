package LeetCodeJava.Sort;

import java.util.Arrays;

// https://leetcode.com/problems/merge-sorted-array/
/**
 * 88. Merge Sorted Array
 * Solved
 * Easy
 * Topics
 * Companies
 * Hint
 * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.
 *
 * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
 *
 * The final sorted array should not be returned by the function, but instead be stored inside the array nums1. To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 * Output: [1,2,2,3,5,6]
 * Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
 * The result of the merge is [1,2,2,3,5,6] with the underlined elements coming from nums1.
 * Example 2:
 *
 * Input: nums1 = [1], m = 1, nums2 = [], n = 0
 * Output: [1]
 * Explanation: The arrays we are merging are [1] and [].
 * The result of the merge is [1].
 * Example 3:
 *
 * Input: nums1 = [0], m = 0, nums2 = [1], n = 1
 * Output: [1]
 * Explanation: The arrays we are merging are [] and [1].
 * The result of the merge is [1].
 * Note that because m = 0, there are no elements in nums1. The 0 is only there to ensure the merge result can fit in nums1.
 *
 *
 * Constraints:
 *
 * nums1.length == m + n
 * nums2.length == n
 * 0 <= m, n <= 200
 * 1 <= m + n <= 200
 * -109 <= nums1[i], nums2[j] <= 109
 *
 *
 * Follow up: Can you come up with an algorithm that runs in O(m + n) time?
 *
 *
 */
public class MergeSortedArray {

    // V0
    // IDEA: ARRAY OP + SORT
    /**
     * time = O(N)
     * space = O(N)
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        /**
         *   NOTE !!! we loop from 0  to `n`
         */
        for (int i = 0; i < n; i++) {
            /**
             *   NOTE !!! we assign nums2[i] val to `i+m` idx at nums1 (nums1[i + m] )
             */
            nums1[i + m] = nums2[i];
        }
        // NOTE: we sort merged array
        Arrays.sort(nums1);
    }

    // V1
    // IDEA : MERGE AND SORT
    // https://leetcode.com/problems/merge-sorted-array/editorial/
    /**
     *   NOTE !!!
     *
     *  two integers m and n,
     *  representing the number of elements in nums1 and nums2 respectively.
     *
     *  -> m : nums1 element cnt
     *  -> n : nums2 element cnt
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public void merge_2(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            nums1[i + m] = nums2[i];
        }
        Arrays.sort(nums1);
    }


    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/merge-sorted-array/editorial/
    /**
     * time = O(N)
     * space = O(N)
     */
    public void merge_3(int[] nums1, int m, int[] nums2, int n) {
        // Make a copy of the first m elements of nums1.
        int[] nums1Copy = new int[m];
        for (int i = 0; i < m; i++) {
            nums1Copy[i] = nums1[i];
        }

        // Read pointers for nums1Copy and nums2 respectively.
        int p1 = 0;
        int p2 = 0;

        // Compare elements from nums1Copy and nums2 and write the smallest to nums1.
        for (int p = 0; p < m + n; p++) {
            // We also need to ensure that p1 and p2 aren't over the boundaries
            // of their respective arrays.
            if (p2 >= n || (p1 < m && nums1Copy[p1] < nums2[p2])) {
                nums1[p] = nums1Copy[p1];
                p1 += 1;
            } else {
                nums1[p] = nums2[p2];
                p2 += 1;
            }
        }
    }

}
