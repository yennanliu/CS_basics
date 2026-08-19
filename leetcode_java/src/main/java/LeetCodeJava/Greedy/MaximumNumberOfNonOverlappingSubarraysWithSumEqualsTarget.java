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
}
