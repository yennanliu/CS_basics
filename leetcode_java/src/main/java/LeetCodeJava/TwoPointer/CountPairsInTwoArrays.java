package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-pairs-in-two-arrays/description/
// https://leetcode.ca/2021-01-27-1885-Count-Pairs-in-Two-Arrays/

import java.util.Arrays;

/**
 * 1885. Count Pairs in Two Arrays
 * Medium
 *
 * Given two integer arrays nums1 and nums2 of length n, count the pairs of indices (i, j) such that i < j and nums1[i] + nums1[j] > nums2[i] + nums2[j].
 *
 * Return the number of pairs satisfying the condition.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,1,2,1], nums2 = [1,2,1,2]
 * Output: 1
 * Explanation: The pairs satisfying the condition are:
 * - (0, 2) where 2 + 2 > 1 + 1.
 *
 * Example 2:
 *
 * Input: nums1 = [1,10,6,2], nums2 = [1,4,1,5]
 * Output: 5
 * Explanation: The pairs satisfying the condition are:
 * - (0, 1) where 1 + 10 > 1 + 4.
 * - (0, 2) where 1 + 6 > 1 + 1.
 * - (1, 2) where 10 + 6 > 4 + 1.
 * - (1, 3) where 10 + 2 > 4 + 5.
 * - (2, 3) where 6 + 2 > 1 + 5.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * 1 <= nums1[i], nums2[i] <= 10^5
 *
 */
public class CountPairsInTwoArrays {

    // V0
    // IDEA: REWRITE THE INEQUALITY, THEN SORT + TWO POINTERS
    //       nums1[i] + nums1[j] > nums2[i] + nums2[j]
    //         <=> diff[i] + diff[j] > 0   with diff[k] = nums1[k] - nums2[k]
    //       the pair is unordered, so diff can be sorted freely. on sorted diff:
    //       if diff[left] + diff[right] > 0, every index in [left, right-1] also
    //       pairs with right -> add (right - left) and shrink right; otherwise
    //       diff[left] is too small for any partner -> advance left.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long countPairs(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int[] diff = new int[n];
        for (int i = 0; i < n; i++) {
            diff[i] = nums1[i] - nums2[i];
        }
        Arrays.sort(diff);

        // long : up to n^2 / 2 pairs
        long pairs = 0;
        int left = 0;
        int right = n - 1;
        while (left < right) {
            if (diff[left] + diff[right] > 0) {
                pairs += right - left;
                right--;
            } else {
                left++;
            }
        }
        return pairs;
    }
}
