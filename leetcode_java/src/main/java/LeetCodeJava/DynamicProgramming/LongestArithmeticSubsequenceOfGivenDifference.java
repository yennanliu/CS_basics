package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-arithmetic-subsequence-of-given-difference/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1218. Longest Arithmetic Subsequence of Given Difference
 * Medium
 *
 * Given an integer array arr and an integer difference, return the length of the longest
 * subsequence in arr which is an arithmetic sequence such that the difference between
 * adjacent elements in the subsequence equals difference.
 *
 * A subsequence is a sequence that can be derived from arr by deleting some or no elements
 * without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3,4], difference = 1
 * Output: 4
 * Explanation: The longest arithmetic subsequence is [1,2,3,4].
 *
 * Example 2:
 *
 * Input: arr = [1,3,5,7], difference = 1
 * Output: 1
 * Explanation: The longest arithmetic subsequence is any single element.
 *
 * Example 3:
 *
 * Input: arr = [1,5,7,8,5,3,4,2,1], difference = -2
 * Output: 4
 * Explanation: The longest arithmetic subsequence is [7,5,3,1].
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * -10^4 <= arr[i], difference <= 10^4
 *
 */
public class LongestArithmeticSubsequenceOfGivenDifference {

    // V0
    // IDEA: HASH MAP DP — dp[value] = longest arithmetic subsequence ENDING with `value`
    //       dp[val] = dp[val - difference] + 1  (the previous term is fixed by the difference)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestSubsequence(int[] arr, int difference) {
        // edge case: no elements, no subsequence
        if (arr == null || arr.length == 0) {
            return 0;
        }

        // dp key: a value seen so far; dp value: best length of a subsequence ending with it.
        // keyed by VALUE rather than index, so the predecessor lookup is O(1)
        Map<Integer, Integer> dp = new HashMap<>();
        int maxLength = 0;

        for (int val : arr) {
            // to append val, the previous term of the subsequence must be val - difference
            int prev = val - difference;

            // extend the subsequence ending at prev, or start a new one of length 1.
            // a later occurrence of the same value overwrites the earlier one, which is
            // right: it can extend everything the earlier one could, and more
            int length = dp.getOrDefault(prev, 0) + 1;
            dp.put(val, length);

            maxLength = Math.max(maxLength, length);
        }

        return maxLength;
    }
}
