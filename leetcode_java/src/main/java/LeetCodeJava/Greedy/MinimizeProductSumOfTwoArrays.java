package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimize-product-sum-of-two-arrays/description/

import java.util.*;

/**
 * 1874. Minimize Product Sum of Two Arrays
 * Medium
 *
 * The product sum of two equal-length arrays a and b is equal to the sum of
 * a[i] * b[i] for all 0 <= i < a.length (0-indexed).
 *
 * For example, if a = [1,2,3,4] and b = [5,2,3,1], the product sum would be
 * 1*5 + 2*2 + 3*3 + 4*1 = 22.
 *
 * Given two arrays nums1 and nums2 of length n, return the minimum product sum if
 * you are allowed to rearrange the order of the elements in nums1.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [5,3,4,2], nums2 = [4,2,2,5]
 * Output: 40
 * Explanation: We can rearrange nums1 to become [3,5,4,2]. The product sum of
 * [3,5,4,2] and [4,2,2,5] is 3*4 + 5*2 + 4*2 + 2*5 = 40.
 *
 * Example 2:
 *
 * Input: nums1 = [2,1,4,5,7], nums2 = [3,2,4,8,6]
 * Output: 65
 * Explanation: We can rearrange nums1 to become [5,7,4,1,2]. The product sum of
 * [5,7,4,1,2] and [3,2,4,8,6] is 5*3 + 7*2 + 4*4 + 1*8 + 2*6 = 65.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * 1 <= nums1[i], nums2[i] <= 100
 *
 */
public class MinimizeProductSumOfTwoArrays {

    // V0
    // IDEA: GREEDY / REARRANGEMENT INEQUALITY (pair the smallest with the largest)
    /**
     * time = O(N log N)
     * space = O(N) for the sorted copies
     */
    public int minProductSum(int[] nums1, int[] nums2) {
        int[] ascending = nums1.clone();
        int[] sorted2 = nums2.clone();
        Arrays.sort(ascending);
        Arrays.sort(sorted2);

        int n = ascending.length;
        int total = 0;
        for (int i = 0; i < n; i++) {
            // read sorted2 from the back so it runs in descending order
            total += ascending[i] * sorted2[n - 1 - i];
        }
        return total;
    }

    // V1
    // IDEA: COUNTING SORT (values are in [1, 100]) -> two pointers over the buckets
    /**
     * time = O(N + V), V = 100
     * space = O(V)
     */
    public int minProductSum_1(int[] nums1, int[] nums2) {
        final int maxValue = 100;
        int[] count1 = new int[maxValue + 1];
        int[] count2 = new int[maxValue + 1];
        for (int v : nums1) {
            count1[v]++;
        }
        for (int v : nums2) {
            count2[v]++;
        }

        int total = 0;
        // smallest value of nums1 meets largest value of nums2
        int small = 1;
        int large = maxValue;
        while (small <= maxValue && large >= 1) {
            if (count1[small] == 0) {
                small++;
            } else if (count2[large] == 0) {
                large--;
            } else {
                // pair as many copies as both buckets allow
                int pairs = Math.min(count1[small], count2[large]);
                total += pairs * small * large;
                count1[small] -= pairs;
                count2[large] -= pairs;
            }
        }
        return total;
    }
}
