package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/widest-pair-of-indices-with-equal-range-sum/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1983. Widest Pair of Indices With Equal Range Sum
 * Medium
 *
 * You are given two 0-indexed binary arrays nums1 and nums2. Find the widest pair of indices (i, j)
 * such that i <= j and nums1[i] + nums1[i+1] + ... + nums1[j] == nums2[i] + nums2[i+1] + ... + nums2[j].
 *
 * The widest pair of indices is the pair with the largest distance between i and j. The distance
 * between a pair of indices is defined as j - i + 1.
 *
 * Return the distance of the widest pair of indices. If no pair of indices meets the conditions,
 * return 0.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,1,0,1], nums2 = [0,1,1,0]
 * Output: 3
 * Explanation: If i = 1 and j = 3: nums1[1] + nums1[2] + nums1[3] = 1 + 0 + 1 = 2 and
 * nums2[1] + nums2[2] + nums2[3] = 1 + 1 + 0 = 2. The distance between i and j is j - i + 1 = 3.
 *
 * Example 2:
 *
 * Input: nums1 = [0,1], nums2 = [1,1]
 * Output: 1
 *
 * Example 3:
 *
 * Input: nums1 = [0], nums2 = [1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * nums1[i] is either 0 or 1.
 * nums2[i] is either 0 or 1.
 *
 */
public class WidestPairOfIndicesWithEqualRangeSum {

    // V0
    // IDEA: PREFIX SUM OF THE DIFFERENCE + HASH MAP (longest zero-sum subarray)
    // equal range sums <=> the range sum of (nums1[t] - nums2[t]) is 0, i.e. two prefix sums are
    // equal. Keep only the FIRST index of each prefix value (widest window); seed {0: -1} so a window
    // starting at index 0 counts.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int widestPairOfIndices(int[] nums1, int[] nums2) {
        Map<Integer, Integer> firstIndex = new HashMap<>();
        firstIndex.put(0, -1);
        int prefix = 0;
        int widest = 0;
        for (int i = 0; i < nums1.length; i++) {
            prefix += nums1[i] - nums2[i];
            if (firstIndex.containsKey(prefix)) {
                widest = Math.max(widest, i - firstIndex.get(prefix));
            } else {
                firstIndex.put(prefix, i);
            }
        }
        return widest;
    }
}
