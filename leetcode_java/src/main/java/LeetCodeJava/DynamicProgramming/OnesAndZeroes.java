package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/ones-and-zeroes/

/**
 *  474. Ones and Zeroes
 *  Medium
 *
 *  You are given an array of binary strings strs and two integers m and n.
 *
 *  Return the size of the largest subset of strs such that there are at most
 *  m 0's and n 1's in the subset.
 *
 *  A set x is a subset of a set y if all elements of x are also elements of y.
 *
 *  Example 1:
 *
 *  Input: strs = ["10","0001","111001","1","0"], m = 5, n = 3
 *  Output: 4
 *  Explanation: The largest subset with at most 5 0's and 3 1's is
 *  {"10", "0001", "1", "0"}, so the answer is 4.
 *
 *  Example 2:
 *
 *  Input: strs = ["10","0","1"], m = 1, n = 1
 *  Output: 2
 *  Explanation: The largest subset is {"0", "1"}, so the answer is 2.
 *
 *  Constraints:
 *
 *  1 <= strs.length <= 600
 *  1 <= strs[i].length <= 100
 *  strs[i] consists only of digits '0' and '1'.
 *  1 <= m, n <= 100
 */
public class OnesAndZeroes {

    // V0
    // IDEA: 0/1 KNAPSACK WITH 2 CAPACITIES (m zeros, n ones)
    //  dp[i][j] = max subset size using at most i zeros and j ones
    //  iterate capacities backwards so each string is used at most once
    /**
     * time = O(s * m * n), s = strs.length
     * space = O(m * n)
     */
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        for (String s : strs) {
            int zeros = 0;
            int ones = 0;
            for (int k = 0; k < s.length(); k++) {
                if (s.charAt(k) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            // NOTE !!! go backwards (0/1 knapsack)
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        return dp[m][n];
    }
}
