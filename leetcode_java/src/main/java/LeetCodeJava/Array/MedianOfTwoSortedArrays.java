package LeetCodeJava.Array;

// https://leetcode.com/problems/median-of-two-sorted-arrays/description/

import java.util.*;

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

    // V0-1
    // IDEA: BRUTE FORCE (sort + even/odd size median number)
    // time: O((M+N) log(M+N)), space: O(M+N)
    public double findMedianSortedArrays_0_1(int[] nums1, int[] nums2) {
        // edge
        if (nums1.length == 0 && nums2.length == 0) {
            return 0.0; // ?
        }
        if (nums1.length == 0 || nums2.length == 0) {
            if (nums1.length == 0) {
                return getMedian(nums2);
            }
            return getMedian(nums1);
        }

        List<Integer> cache = new ArrayList<>();
        for (int i = 0; i < nums1.length; i++) {
            cache.add(nums1[i]);
        }

        for (int i = 0; i < nums2.length; i++) {
            cache.add(nums2[i]);
        }

        // sort (small -> big)
        Collections.sort(cache, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        // size is even
        /**
         *  NOTE !!!
         *
         *   if array size is even
         *
         *   -> median = ( array[ array.size() / 2 ] + array[ (array.size() / 2) - 1 ] ) / 2
         *
         */
        if (cache.size() % 2 == 0) {
            double val1 = cache.get((cache.size() / 2));
            double val2 = cache.get((cache.size() / 2) - 1);
            // [1,2,3,4]
            // [1,2,3,4,5,6]
            return (val1 + val2) / 2;
        }
        // size is odd
        return cache.get(cache.size() / 2);
    }

    public double getMedian(int[] num) {
        int size = num.length;
        if (size % 2 == 0) {
            double val1 = num[num.length / 2];
            double val2 = num[(num.length / 2) - 1];
            return (val1 + val2) / 2;
        }
        return num[(num.length / 2)];
    }

    // V1-1
    // https://neetcode.io/problems/median-of-two-sorted-arrays
    // IDEA: BRUTE FORCE
    // time: O((M+N) log(M+N)), space: O(M+N)
    public double findMedianSortedArrays_1_1(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int[] merged = new int[len1 + len2];
        System.arraycopy(nums1, 0, merged, 0, len1);
        System.arraycopy(nums2, 0, merged, len1, len2);
        Arrays.sort(merged);

        int totalLen = merged.length;
        if (totalLen % 2 == 0) {
            return (merged[totalLen / 2 - 1] + merged[totalLen / 2]) / 2.0;
        } else {
            return merged[totalLen / 2];
        }
    }

    // V1-2
    // https://neetcode.io/problems/median-of-two-sorted-arrays
    // IDEA: 2 POINTERS
    // time: O(M+N), space: O(1)
    public double findMedianSortedArrays_1_2(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int i = 0, j = 0;
        int median1 = 0, median2 = 0;

        for (int count = 0; count < (len1 + len2) / 2 + 1; count++) {
            median2 = median1;
            if (i < len1 && j < len2) {
                if (nums1[i] > nums2[j]) {
                    median1 = nums2[j];
                    j++;
                } else {
                    median1 = nums1[i];
                    i++;
                }
            } else if (i < len1) {
                median1 = nums1[i];
                i++;
            } else {
                median1 = nums2[j];
                j++;
            }
        }

        if ((len1 + len2) % 2 == 1) {
            return (double) median1;
        } else {
            return (median1 + median2) / 2.0;
        }
    }

    // V1-3
    // https://neetcode.io/problems/median-of-two-sorted-arrays
    // IDEA: Binary Search
    // time: O(log(min(M,N))), space: O(log(min(M,N)))
    public double findMedianSortedArrays_1_3(int[] nums1, int[] nums2) {
        int left = (nums1.length + nums2.length + 1) / 2;
        int right = (nums1.length + nums2.length + 2) / 2;
        return (getKth(nums1, nums1.length, nums2, nums2.length, left, 0, 0) +
                getKth(nums1, nums1.length, nums2, nums2.length, right, 0, 0)) / 2.0;
    }

    public int getKth(int[] a, int m, int[] b, int n, int k, int aStart, int bStart) {
        if (m > n) {
            return getKth(b, n, a, m, k, bStart, aStart);
        }
        if (m == 0) {
            return b[bStart + k - 1];
        }
        if (k == 1) {
            return Math.min(a[aStart], b[bStart]);
        }

        int i = Math.min(m, k / 2);
        int j = Math.min(n, k / 2);

        if (a[aStart + i - 1] > b[bStart + j - 1]) {
            return getKth(a, m, b, n - j, k - j, aStart, bStart + j);
        } else {
            return getKth(a, m - i, b, n, k - i, aStart + i, bStart);
        }
    }

    // V1-4
    // https://neetcode.io/problems/median-of-two-sorted-arrays
    // IDEA: Binary Search (Optimal)
    // time: O(log(min(M,N))), space: O(1)
    public double findMedianSortedArrays_1_4(int[] nums1, int[] nums2) {
        int[] A = nums1;
        int[] B = nums2;
        int total = A.length + B.length;
        int half = (total + 1) / 2;

        if (B.length < A.length) {
            int[] temp = A;
            A = B;
            B = temp;
        }

        int l = 0;
        int r = A.length;
        while (l <= r) {
            int i = (l + r) / 2;
            int j = half - i;

            int Aleft = i > 0 ? A[i - 1] : Integer.MIN_VALUE;
            int Aright = i < A.length ? A[i] : Integer.MAX_VALUE;
            int Bleft = j > 0 ? B[j - 1] : Integer.MIN_VALUE;
            int Bright = j < B.length ? B[j] : Integer.MAX_VALUE;

            if (Aleft <= Bright && Bleft <= Aright) {
                if (total % 2 != 0) {
                    return Math.max(Aleft, Bleft);
                }
                return (Math.max(Aleft, Bleft) + Math.min(Aright, Bright)) / 2.0;
            } else if (Aleft > Bright) {
                r = i - 1;
            } else {
                l = i + 1;
            }
        }
        return -1;
    }


    // V2
    // https://leetcode.com/problems/median-of-two-sorted-arrays/solutions/4070500/99journey-from-brute-force-to-most-optim-z3k8/

    // V3
    // https://leetcode.com/problems/median-of-two-sorted-arrays/solutions/6401742/1ms-100-beats-single-loop-beginner-frien-6bqd/

    // V4
    // time: O(log(min(M,N))), space: O(1)
    public double findMedianSortedArrays_4(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays_4(nums2, nums1); // Ensure nums1 is the smaller array
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

    // V5

}
