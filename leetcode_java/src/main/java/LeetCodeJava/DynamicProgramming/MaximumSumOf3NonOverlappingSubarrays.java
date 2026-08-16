package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/description/
/**
 * 689. Maximum Sum of 3 Non-Overlapping Subarrays
 * Hard
 *
 * Given an integer array nums and an integer k, find three non-overlapping subarrays of
 * length k with maximum sum and return them.
 *
 * Return the result as a list of indices representing the starting position of each
 * interval (0-indexed). If there are multiple answers, return the lexicographically
 * smallest one.
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,2,6,7,5,1], k = 2
 * Output: [0,3,5]
 * Explanation: Subarrays [1, 2], [2, 6], [7, 5] correspond to the starting indices
 * [0, 3, 5].
 * We could have also taken [2, 1], but an answer of [1, 3, 5] would be lexicographically
 * larger.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,2,1,2,1,2,1], k = 2
 * Output: [0,2,4]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= nums[i] < 2^16
 * 1 <= k <= floor(nums.length / 3)
 *
 */
public class MaximumSumOf3NonOverlappingSubarrays {

    // V0
    // IDEA: PREFIX SUM + BEST-ON-THE-LEFT / BEST-ON-THE-RIGHT ARRAYS
    /**
     *   Step 1: COLLAPSE each length-k window into ONE number
     *             w[i] = sum(nums[i, i+k))
     *
     *   Step 2: FIX the MIDDLE window at index j. The other two are then INDEPENDENT:
     *             - best window entirely to the LEFT  -> any start in [0, j-k]
     *             - best window entirely to the RIGHT -> any start in [j+k, m-1]
     *           Precompute those with two sweeps:
     *             left[i]  = argmax of w over [0, i]
     *             right[i] = argmax of w over [i, m-1]
     *
     *   Step 3: scan j and keep the best triple.
     *
     *   NOTE !!! LEXICOGRAPHIC tie-breaking -- all three comparisons must prefer the
     *            SMALLER index on a tie:
     *     - left  sweeps FORWARD  with strict `>`  -> keeps the earliest maximum
     *     - right sweeps BACKWARD with `>=`        -> keeps the earliest maximum
     *     - the j loop runs ASCENDING with strict `>` -> keeps the earliest triple
     *
     *   time  = O(n)
     *   space = O(n)
     */
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int n = nums.length;

        // prefix[i] = sum of nums[0, i)
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        // w[i] = sum of the length-k window starting at i
        int m = n - k + 1;
        long[] w = new long[m];
        for (int i = 0; i < m; i++) {
            w[i] = prefix[i + k] - prefix[i];
        }

        // left[i] = index of the best window within w[0..i] (EARLIEST on tie)
        int[] left = new int[m];
        int best = 0;
        for (int i = 0; i < m; i++) {
            if (w[i] > w[best]) {
                best = i;
            }
            left[i] = best;
        }

        // right[i] = index of the best window within w[i..m-1] (EARLIEST on tie)
        int[] right = new int[m];
        best = m - 1;
        for (int i = m - 1; i >= 0; i--) {
            if (w[i] >= w[best]) {
                best = i;
            }
            right[i] = best;
        }

        int[] res = new int[0];
        long bestTotal = -1;
        for (int j = k; j < m - k; j++) {
            int i = left[j - k];
            int l = right[j + k];
            long total = w[i] + w[j] + w[l];
            if (total > bestTotal) {
                bestTotal = total;
                res = new int[] { i, j, l };
            }
        }

        return res;
    }

}
