package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-arithmetic-subsequence/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1027. Longest Arithmetic Subsequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array nums of integers, return the length of the longest arithmetic subsequence in nums.
 *
 * Note that:
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 * A sequence seq is arithmetic if seq[i + 1] - seq[i] are all the same value (for 0 <= i < seq.length - 1).
 *
 *
 * Example 1:
 *
 * Input: nums = [3,6,9,12]
 * Output: 4
 * Explanation:  The whole array is an arithmetic sequence with steps of length = 3.
 * Example 2:
 *
 * Input: nums = [9,4,7,2,10]
 * Output: 3
 * Explanation:  The longest arithmetic subsequence is [4,7,10].
 * Example 3:
 *
 * Input: nums = [20,1,15,3,10,5,8]
 * Output: 4
 * Explanation:  The longest arithmetic subsequence is [20,15,10,5].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 1000
 * 0 <= nums[i] <= 500
 *
 *
 */
public class LongestArithmeticSubsequence {

    // V0
//    public int longestArithSeqLength(int[] nums) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=-NIlLdVKBFs


    // V2
    // https://leetcode.com/problems/longest-arithmetic-subsequence/solutions/3671662/beats-100-c-java-python-beginner-friendl-c1gm/
    // time: O(N^2), space: O(N^2)
    public int longestArithSeqLength_2(int[] nums) {
        int n = nums.length;
        if (n <= 2)
            return n;

        int longest = 2;
        Map<Integer, Integer>[] dp = new HashMap[n];

        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                dp[i].put(diff, dp[j].getOrDefault(diff, 1) + 1);
                longest = Math.max(longest, dp[i].get(diff));
            }
        }

        return longest;
    }

    // V3
    // https://leetcode.com/problems/longest-arithmetic-subsequence/solutions/595478/java-dp-solution-using-hashmap-on2-with-92d0r/
    // IDEA: DP
    // time: O(N^2), space: O(N^2)
    public int longestArithSeqLength_3(int[] A) {
        int ansMax = 0, n = A.length, was, diff;
        HashMap<Integer, Integer> tillI[] = new HashMap[n];
        // initialising HashMap for all indices
        for (int i = 0; i < n; ++i)
            tillI[i] = new HashMap<>();

        // iterating over all non- zero length sequence forming indices
        for (int i = 1; i < n; ++i)
            // iterating over all previous values to get all possible solutions
            for (int j = 0; j < i; ++j) {
                diff = A[i] - A[j];
                was = tillI[j].getOrDefault(diff, 1); // what was longest till j with difference diff
                ansMax = Math.max(was + 1, ansMax);
                tillI[i].put(diff, was + 1); // length found till i with difference diff
            }
        return ansMax;
    }


}
