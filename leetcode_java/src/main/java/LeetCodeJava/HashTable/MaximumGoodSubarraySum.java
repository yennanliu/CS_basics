package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-good-subarray-sum/description/

import java.util.*;

/**
 * 3026. Maximum Good Subarray Sum
 * Medium
 *
 * You are given an array nums of length n and a positive integer k.
 *
 * A subarray of nums is called good if the absolute difference between its first and last
 * element is exactly k, in other words, the subarray nums[i..j] is good if |nums[i] - nums[j]| == k.
 *
 * Return the maximum sum of a good subarray of nums. If there are no good subarrays, return 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,6], k = 1
 * Output: 11
 * Explanation: The absolute difference between the first and last element must be 1 for a good
 * subarray. All the good subarrays are: [1,2], [2,3], [3,4], [4,5], and [5,6]. The maximum
 * subarray sum is 11 for the subarray [5,6].
 *
 * Example 2:
 *
 * Input: nums = [-1,3,2,4,5], k = 3
 * Output: 11
 * Explanation: The absolute difference between the first and last element must be 3 for a good
 * subarray. All the good subarrays are: [-1,3,2], and [2,4,5]. The maximum subarray sum is 11
 * for the subarray [2,4,5].
 *
 * Example 3:
 *
 * Input: nums = [-1,-2,-3,-4], k = 2
 * Output: -6
 * Explanation: The absolute difference between the first and last element must be 2 for a good
 * subarray. All the good subarrays are: [-1,-2,-3], and [-2,-3,-4]. The maximum subarray sum is
 * -6 for the subarray [-1,-2,-3].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 * 1 <= k <= 10^9
 *
 */
public class MaximumGoodSubarraySum {

    // V0
    // IDEA: PREFIX SUM + HASH MAP {value : index whose prefix sum is the smallest}
    //
    //   sum(nums[i..j]) = prefixSum[j + 1] - prefixSum[i], so for a fixed right end j
    //   the best start is the one with the SMALLEST prefixSum[i] -- among the starts
    //   that make the subarray good. "good" pins the start's VALUE:
    //   nums[i] must be nums[j] - k or nums[j] + k.
    //
    //   so keep, per value, the start index with the smallest prefix sum, and at
    //   each j look up the (at most two) qualifying values.
    //
    //   NOTE !!! the answer can be negative (example 3), so track "found any" rather
    //            than starting from 0; fall back to 0 only when nothing is good.
    //   NOTE !!! nums[i] +- k reaches 2 * 10^9 and sums reach 10^14 -> use long.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maximumSubarraySum(int[] nums, int k) {
        // edge case: a good subarray needs at least 2 elements
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int n = nums.length;

        // prefixSum[i] = nums[0] + ... + nums[i - 1]
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        // value -> start index (holding that value) whose prefixSum is the smallest so far
        Map<Long, Integer> bestStartIdx = new HashMap<>();

        long maxSum = Long.MIN_VALUE;
        boolean found = false;

        for (int j = 0; j < n; j++) {
            long val = nums[j];

            // the start must hold val + k or val - k
            long[] targets = { val + k, val - k };
            for (long target : targets) {
                Integer startIdx = bestStartIdx.get(target);
                if (startIdx != null) {
                    // sum of nums[startIdx .. j]
                    long subSum = prefixSum[j + 1] - prefixSum[startIdx];
                    maxSum = Math.max(maxSum, subSum);
                    found = true;
                }
            }

            // nums[j] can start a future subarray; keep the index with the
            // smaller prefix sum BEFORE it, since that makes later sums larger
            Integer oldIdx = bestStartIdx.get(val);
            if (oldIdx == null || prefixSum[j] < prefixSum[oldIdx]) {
                bestStartIdx.put(val, j);
            }
        }

        if (!found) {
            return 0;
        }
        return maxSum;
    }

    // V0-1
    // IDEA: SAME AS V0, BUT STORE THE MIN PREFIX SUM ITSELF (no prefix array)
    //
    //   instead of an index into a prefix array, keep a running prefix and map
    //   each value straight to the smallest prefix sum seen BEFORE it:
    //
    //       minPrefixBefore[v] = min prefix sum at any index i so far with nums[i] == v
    //
    //   then the subarray ending at j and starting at a good value v sums to
    //   (prefix including nums[j]) - minPrefixBefore[v].
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maximumSubarraySum_0_1(int[] nums, int k) {
        // edge case: a good subarray needs at least 2 elements
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // value -> smallest prefix sum (sum of everything BEFORE an occurrence of it)
        Map<Long, Long> minPrefixBefore = new HashMap<>();

        long prefix = 0;            // sum of nums[0 .. j - 1]
        long maxSum = Long.MIN_VALUE;
        boolean found = false;

        for (int num : nums) {
            long val = num;

            // record nums[j] as a start BEFORE adding it to the prefix
            Long oldPrefix = minPrefixBefore.get(val);
            if (oldPrefix == null || prefix < oldPrefix) {
                minPrefixBefore.put(val, prefix);
            }

            // now prefix includes nums[j]: it is the end of the subarray
            prefix += val;

            long[] targets = { val + k, val - k };
            for (long target : targets) {
                Long startPrefix = minPrefixBefore.get(target);
                if (startPrefix != null) {
                    maxSum = Math.max(maxSum, prefix - startPrefix);
                    found = true;
                }
            }
        }

        if (!found) {
            return 0;
        }
        return maxSum;
    }
}
