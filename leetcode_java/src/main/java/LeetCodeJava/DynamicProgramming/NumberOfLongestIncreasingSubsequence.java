package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-longest-increasing-subsequence/

/**
 *  673. Number of Longest Increasing Subsequence
 *  Medium
 *
 *  Given an integer array nums, return the number of longest increasing subsequences.
 *
 *  Notice that the sequence has to be strictly increasing.
 *
 *  Example 1:
 *    Input: nums = [1,3,5,4,7]
 *    Output: 2
 *    Explanation: The two longest increasing subsequences are [1,3,4,7] and [1,3,5,7].
 *
 *  Example 2:
 *    Input: nums = [2,2,2,2,2]
 *    Output: 5
 *    Explanation: The length of the longest increasing subsequence is 1,
 *                 and there are 5 increasing subsequences of length 1,
 *                 so output 5.
 *
 *  Constraints:
 *    - 1 <= nums.length <= 2000
 *    - -10^6 <= nums[i] <= 10^6
 *    - The answer is guaranteed to fit inside a 32-bit integer.
 */
public class NumberOfLongestIncreasingSubsequence {

    // V0
    // IDEA: PAIRED DP - len[i] = LIS length ending at i, cnt[i] = how many such LIS
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findNumberOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] len = new int[n];   // longest increasing subsequence ending at i
        int[] cnt = new int[n];   // number of such subsequences

        int best = 0;
        for (int i = 0; i < n; i++) {
            len[i] = 1;
            cnt[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (len[j] + 1 > len[i]) {
                        len[i] = len[j] + 1;
                        cnt[i] = cnt[j];
                    } else if (len[j] + 1 == len[i]) {
                        cnt[i] += cnt[j];
                    }
                }
            }
            best = Math.max(best, len[i]);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (len[i] == best) {
                res += cnt[i];
            }
        }
        return res;
    }
}
