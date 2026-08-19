package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/maximum-length-of-repeated-subarray/

/**
 *  718. Maximum Length of Repeated Subarray
 *  Medium
 *
 *  Given two integer arrays nums1 and nums2, return the maximum length of a
 *  subarray that appears in both arrays.
 *
 *  Example 1:
 *
 *  Input: nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7]
 *  Output: 3
 *  Explanation: The repeated subarray with maximum length is [3,2,1].
 *
 *  Example 2:
 *
 *  Input: nums1 = [0,0,0,0,0], nums2 = [0,0,0,0,0]
 *  Output: 5
 *
 *  Constraints:
 *
 *  1 <= nums1.length, nums2.length <= 1000
 *  0 <= nums1[i], nums2[i] <= 100
 */
public class MaximumLengthOfRepeatedSubarray {

    // V0
    // IDEA: 2D DP, dp[i][j] = length of common suffix ending at nums1[i-1], nums2[j-1]
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int findLength(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) {
            return 0;
        }
        int m = nums1.length;
        int n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];
        int res = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    res = Math.max(res, dp[i][j]);
                }
            }
        }
        return res;
    }

    // V1
    // IDEA: same DP but rolling 1D array (space optimized), iterate j backward
    /**
     * time = O(m * n)
     * space = O(n)
     */
    public int findLength_1(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) {
            return 0;
        }
        int m = nums1.length;
        int n = nums2.length;
        int[] dp = new int[n + 1];
        int res = 0;
        for (int i = 1; i <= m; i++) {
            // NOTE !!! go backward so dp[j-1] is still the "previous row" value
            for (int j = n; j >= 1; j--) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[j] = dp[j - 1] + 1;
                    res = Math.max(res, dp[j]);
                } else {
                    dp[j] = 0;
                }
            }
        }
        return res;
    }
}
