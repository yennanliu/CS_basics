package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/allocate-mailboxes/description/

import java.util.Arrays;

/**
 * 1478. Allocate Mailboxes
 * Hard
 *
 * Given the array houses where houses[i] is the location of the ith house along a street and an integer k, allocate k mailboxes in the street.
 *
 * Return the minimum total distance between each house and its nearest mailbox.
 *
 * The test cases are generated so that the answer fits in a 32-bit integer.
 *
 *
 * Example 1:
 *
 * Input: houses = [1,4,8,10,20], k = 3
 * Output: 5
 * Explanation: Allocate mailboxes in position 3, 9 and 20.
 * Minimum total distance from each houses to nearest mailboxes is |3-1| + |4-3| + |9-8| + |10-9| + |20-20| = 5
 *
 * Example 2:
 *
 * Input: houses = [2,3,5,12,18], k = 2
 * Output: 9
 * Explanation: Allocate mailboxes in position 3 and 14.
 * Minimum total distance from each houses to nearest mailboxes is |2-3| + |3-3| + |5-3| + |12-14| + |18-14| = 9.
 *
 *
 * Constraints:
 *
 * 1 <= k <= houses.length <= 100
 * 1 <= houses[i] <= 10^4
 * All the integers of houses are unique.
 *
 */
public class AllocateMailboxes {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: INTERVAL COST + PARTITION DP
    //       after sorting, each mailbox serves a CONTIGUOUS block of houses, so the task is
    //       "cut the sorted houses into k blocks, minimise the total cost".
    //       one block's mailbox goes at its median; pairing the outermost houses gives
    //         blockCost[i][j] = blockCost[i+1][j-1] + houses[j] - houses[i]
    //       dp[i][j] = min cost for houses 0..i with j mailboxes
    //                = min over split p of dp[p][j-1] + blockCost[p+1][i]
    /**
     * time = O(N^2 * K)
     * space = O(N^2)
     */
    public int minDistance(int[] houses, int k) {
        int n = houses.length;
        // the input is not ordered; sort a copy so the caller's array is untouched
        int[] sorted = houses.clone();
        Arrays.sort(sorted);

        // blockCost[i][j] = cost of serving houses i..j with ONE mailbox at their median
        int[][] blockCost = new int[n][n];
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                blockCost[i][j] = blockCost[i + 1][j - 1] + sorted[j] - sorted[i];
            }
        }

        // dp[i][j] = min cost of houses 0..i using j mailboxes
        int[][] dp = new int[n][k + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < n; i++) {
            dp[i][1] = blockCost[0][i];
            // more mailboxes than houses never helps
            int maxBoxes = Math.min(k, i + 1);
            for (int j = 2; j <= maxBoxes; j++) {
                // the last mailbox serves houses p+1..i
                for (int p = 0; p < i; p++) {
                    if (dp[p][j - 1] < INF) {
                        dp[i][j] = Math.min(dp[i][j], dp[p][j - 1] + blockCost[p + 1][i]);
                    }
                }
            }
        }
        return dp[n - 1][k];
    }
}
