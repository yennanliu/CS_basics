package LeetCodeJava.Sort;

// https://leetcode.com/problems/merge-sorted-array/

import java.util.Arrays;

public class MergeSortedArray {

    // V0

    // V1
    // IDEA : MERGE AND SORT
    // https://leetcode.com/problems/merge-sorted-array/editorial/
    public void merge_2(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            nums1[i + m] = nums2[i];
        }
        Arrays.sort(nums1);
    }


    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/merge-sorted-array/editorial/
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
