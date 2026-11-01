package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-index-sum-of-common-elements/description/
// https://leetcode.ca/2025-10-22-3682-Minimum-Index-Sum-of-Common-Elements/

import java.util.*;

/**
 * 3682. Minimum Index Sum of Common Elements
 * Medium
 *
 * You are given two integer arrays nums1 and nums2 of equal length n.
 *
 * We define a pair of indices (i, j) as a good pair if nums1[i] == nums2[j].
 *
 * Return the minimum index sum i + j among all possible good pairs. If no
 * such pairs exist, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [3,2,1], nums2 = [1,3,1]
 * Output: 1
 * Explanation:
 * Common elements between nums1 and nums2 are 1 and 3.
 * For 3, [i, j] = [0, 1], giving an index sum of i + j = 1.
 * For 1, [i, j] = [2, 0], giving an index sum of i + j = 2.
 * The minimum index sum is 1.
 *
 * Example 2:
 *
 * Input: nums1 = [5,1,2], nums2 = [2,1,3]
 * Output: 2
 * Explanation:
 * Common elements between nums1 and nums2 are 1 and 2.
 * For 1, [i, j] = [1, 1], giving an index sum of i + j = 2.
 * For 2, [i, j] = [2, 0], giving an index sum of i + j = 2.
 * The minimum index sum is 2.
 *
 * Example 3:
 *
 * Input: nums1 = [6,4], nums2 = [7,8]
 * Output: -1
 * Explanation:
 * Since no common elements between nums1 and nums2, the output is -1.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length == nums2.length <= 10^5
 * -10^5 <= nums1[i], nums2[i] <= 10^5
 *
 */
public class MinimumIndexSumOfCommonElements {

    // V0
    // IDEA: EARLIEST INDEX OF EACH VALUE IN nums2, THEN ONE PASS OVER nums1
    //   once a value v is fixed the best i and j are its earliest positions in
    //   each array, chosen independently. store the first index of each value in
    //   nums2; scanning nums1 left to right evaluates i + first2[v] at every i,
    //   which covers each value's best pair (later duplicates only give larger sums).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumSum(int[] nums1, int[] nums2) {
        Map<Integer, Integer> firstIndexIn2 = new HashMap<>();
        for (int j = 0; j < nums2.length; j++) {
            if (!firstIndexIn2.containsKey(nums2[j])) {
                firstIndexIn2.put(nums2[j], j);
            }
        }

        int best = -1;
        for (int i = 0; i < nums1.length; i++) {
            Integer j = firstIndexIn2.get(nums1[i]);
            if (j == null) {
                continue;
            }
            int indexSum = i + j;
            if (best == -1 || indexSum < best) {
                best = indexSum;
            }
        }
        return best;
    }
}
