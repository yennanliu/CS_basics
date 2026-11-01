package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-smallest-product-of-two-sorted-arrays/description/

/**
 * 2040. Kth Smallest Product of Two Sorted Arrays
 * Hard
 *
 * Given two sorted 0-indexed integer arrays nums1 and nums2 as well as an integer k, return the kth (1-based) smallest product of nums1[i] * nums2[j] where 0 <= i < nums1.length and 0 <= j < nums2.length.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,5], nums2 = [3,4], k = 2
 * Output: 8
 * Explanation: The 2 smallest products are:
 * - nums1[0] * nums2[0] = 2 * 3 = 6
 * - nums1[0] * nums2[1] = 2 * 4 = 8
 * The 2nd smallest product is 8.
 *
 * Example 2:
 *
 * Input: nums1 = [-4,-2,0,3], nums2 = [2,4], k = 6
 * Output: 0
 * Explanation: The 6 smallest products are:
 * - nums1[0] * nums2[1] = (-4) * 4 = -16
 * - nums1[0] * nums2[0] = (-4) * 2 = -8
 * - nums1[1] * nums2[1] = (-2) * 4 = -8
 * - nums1[1] * nums2[0] = (-2) * 2 = -4
 * - nums1[2] * nums2[0] = 0 * 2 = 0
 * - nums1[2] * nums2[1] = 0 * 4 = 0
 * The 6th smallest product is 0.
 *
 * Example 3:
 *
 * Input: nums1 = [-2,-1,0,1,2], nums2 = [-3,-1,2,4,5], k = 3
 * Output: -6
 * Explanation: The 3 smallest products are:
 * - nums1[0] * nums2[4] = (-2) * 5 = -10
 * - nums1[0] * nums2[3] = (-2) * 4 = -8
 * - nums1[4] * nums2[0] = 2 * (-3) = -6
 * The 3rd smallest product is -6.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 5 * 10^4
 * -10^5 <= nums1[i], nums2[j] <= 10^5
 * k >= 1
 * k <= nums1.length * nums2.length
 * nums1 and nums2 are sorted.
 *
 */
public class KthSmallestProductOfTwoSortedArrays {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + COUNT PRODUCTS <= X WITH A BINARY SEARCH PER nums1[i]
    //  count(x) = number of pairs with nums1[i] * nums2[j] <= x; it never decreases as x grows,
    //  so the answer is the smallest x with count(x) >= k.
    //  for a fixed a = nums1[i] (nums2 sorted ascending):
    //    a > 0 : a * b <= x  <=>  b <= floor(x / a)  -> a prefix of nums2
    //    a < 0 : a * b <= x  <=>  b >= ceil(x / a)   -> a suffix of nums2
    //    a == 0: every b works iff x >= 0
    //  integer floor / ceil division keeps it exact (doubles lose precision near 1e10).
    /**
     * time = O(N * log M * log R)   (R = 2 * 10^10, the range of possible products)
     * space = O(1)
     */
    public long kthSmallestProduct(int[] nums1, int[] nums2, long k) {
        // |product| <= 10^5 * 10^5 = 10^10
        long low = -10_000_000_000L;
        long high = 10_000_000_000L;

        // find the smallest product value x with countAtMost(x) >= k
        while (low < high) {
            long mid = low + (high - low) / 2;
            if (countAtMost(nums1, nums2, mid) >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** Number of pairs (i, j) with nums1[i] * nums2[j] <= x. */
    private long countAtMost(int[] nums1, int[] nums2, long x) {
        int m = nums2.length;
        long count = 0;
        for (int a : nums1) {
            if (a > 0) {
                // b <= floor(x / a): every index before the first b > limit
                long limit = Math.floorDiv(x, (long) a);
                count += firstGreater(nums2, limit);
            } else if (a < 0) {
                // b >= ceil(x / a); ceil(p / q) == -floor(-p / q)
                long limit = -Math.floorDiv(-x, (long) a);
                count += m - firstAtLeast(nums2, limit);
            } else {
                // 0 * b == 0, so all of nums2 counts iff x >= 0
                if (x >= 0) {
                    count += m;
                }
            }
        }
        return count;
    }

    /** First index whose value is > target (upper bound); arr.length if none. */
    private int firstGreater(int[] arr, long target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    /** First index whose value is >= target (lower bound); arr.length if none. */
    private int firstAtLeast(int[] arr, long target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
