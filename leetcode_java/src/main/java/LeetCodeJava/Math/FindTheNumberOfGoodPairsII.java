package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-number-of-good-pairs-ii/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3164. Find the Number of Good Pairs II
 * Medium
 *
 * You are given 2 integer arrays nums1 and nums2 of lengths n and m respectively. You are also given a
 * positive integer k.
 *
 * A pair (i, j) is called good if nums1[i] is divisible by nums2[j] * k (0 <= i <= n - 1, 0 <= j <= m - 1).
 *
 * Return the total number of good pairs.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,3,4], nums2 = [1,3,4], k = 1
 * Output: 5
 * Explanation:
 * The 5 good pairs are (0, 0), (1, 0), (1, 1), (2, 0), and (2, 2).
 *
 * Example 2:
 *
 * Input: nums1 = [1,2,4,12], nums2 = [2,4], k = 3
 * Output: 2
 * Explanation:
 * The 2 good pairs are (3, 0) and (3, 1).
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 10^5
 * 1 <= nums1[i], nums2[j] <= 10^6
 * 1 <= k <= 10^3
 *
 */
public class FindTheNumberOfGoodPairsII {

    // V0
    // IDEA: DIVIDE OUT k, THEN FOR EACH DISTINCT nums2 VALUE b SUM THE COUNTS AT b, 2b, 3b, ... (HARMONIC SWEEP)
    //   nums1[i] % (b * k) == 0  <=>  k | nums1[i]  and  b | nums1[i] / k
    /**
     * time = O(N + M + V log V)
     * space = O(V)
     */
    public long numberOfPairs(int[] nums1, int[] nums2, int k) {
        // quotientCount[a] = how many nums1 values equal a * k
        int maxQuotient = 0;
        for (int value : nums1) {
            if (value % k == 0) {
                maxQuotient = Math.max(maxQuotient, value / k);
            }
        }
        if (maxQuotient == 0) {
            return 0;
        }
        int[] quotientCount = new int[maxQuotient + 1];
        for (int value : nums1) {
            if (value % k == 0) {
                quotientCount[value / k]++;
            }
        }

        // distinct divisor candidates from nums2, with multiplicity
        Map<Integer, Integer> divisorFrequency = new HashMap<>();
        for (int b : nums2) {
            divisorFrequency.put(b, divisorFrequency.getOrDefault(b, 0) + 1);
        }

        long goodPairs = 0;
        for (Map.Entry<Integer, Integer> entry : divisorFrequency.entrySet()) {
            int b = entry.getKey();
            int frequency = entry.getValue();
            // visit every multiple of b; each distinct b costs maxQuotient / b steps
            long hits = 0;
            for (int multiple = b; multiple <= maxQuotient; multiple += b) {
                hits += quotientCount[multiple];
            }
            goodPairs += hits * frequency;
        }
        return goodPairs;
    }
}
