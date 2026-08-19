package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-non-overlapping-subarrays-with-sum-equals-target/

import java.util.*;

/**
 *  1546. Maximum Number of Non-Overlapping Subarrays With Sum Equals Target
 *  Medium
 *
 *  Given an array nums and an integer target, return the maximum number of
 *  non-empty non-overlapping subarrays such that the sum of values in each
 *  subarray is equal to target.
 *
 *  Example 1:
 *  Input: nums = [1,1,1,1,1], target = 2
 *  Output: 2
 *  Explanation: There are 2 non-overlapping subarrays with sum equals to 2.
 *
 *  Example 2:
 *  Input: nums = [-1,3,5,1,4,2,-9], target = 6
 *  Output: 2
 *  Explanation: There are 3 subarrays with sum equal to 6
 *  ([5,1], [4,2], [3,5,1,4,2,-9]) but only the first 2 are non-overlapping.
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - -10^4 <= nums[i] <= 10^4
 *   - 0 <= target <= 10^6
 */
public class MaximumNumberOfNonOverlappingSubarraysWithSumEqualsTarget {

    // V0
    // IDEA: PREFIX SUM + SET + GREEDY (whenever a valid subarray ends here, take it and reset)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int maxNonOverlapping(int[] nums, int target) {

        if (nums == null || nums.length == 0) {
            return 0;
        }

        int res = 0;
        long prefix = 0;

        /**
         *  NOTE !!!
         *
         *  seen = prefix sums met since the LAST accepted subarray.
         *  init with 0 : the "empty prefix" is always seen.
         */
        Set<Long> seen = new HashSet<>();
        seen.add(0L);

        for (int x : nums) {
            prefix += x;

            // prefix - previousPrefix == target  ->  a subarray ending here sums to target
            if (seen.contains(prefix - target)) {
                res += 1;
                // greedily cut here, restart the search for the next (non overlapping) subarray
                prefix = 0;
                seen = new HashSet<>();
                seen.add(0L);
            } else {
                seen.add(prefix);
            }
        }

        return res;
    }

    // V1
    // IDEA: DP OVER PREFIXES (no greedy cut/reset argument needed).
    //       dp[i] = best answer for nums[0..i-1];
    //       dp[i] = max(dp[i-1], dp[j] + 1) where j is the LATEST index with
    //       prefix[j] == prefix[i] - target (dp is non decreasing, so latest is best).
    /**
     * time = O(n)
     * space = O(n)
     */
    public int maxNonOverlapping_1(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] dp = new int[n + 1];

        Map<Long, Integer> lastIdx = new HashMap<>();   // prefix sum -> largest i with that prefix
        lastIdx.put(0L, 0);

        long prefix = 0;
        for (int i = 1; i <= n; i++) {
            prefix += nums[i - 1];
            dp[i] = dp[i - 1];
            Integer j = lastIdx.get(prefix - target);
            if (j != null) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
            lastIdx.put(prefix, i);
        }
        return dp[n];
    }

    // V2
    // IDEA: brute force O(n^2) DP - for every end, scan backwards for a subarray summing
    //       to target. Kept as a readable correctness reference for the two O(n) versions.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int maxNonOverlapping_2(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            dp[i] = dp[i - 1];
            long sum = 0;
            for (int j = i; j >= 1; j--) {
                sum += nums[j - 1];
                if (sum == target) {
                    dp[i] = Math.max(dp[i], dp[j - 1] + 1);
                }
            }
        }
        return dp[n];
    }
}
