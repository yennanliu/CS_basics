package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-penalty-for-a-shop/description/

import java.util.*;

/**
 * 2483. Minimum Penalty for a Shop
 * Medium
 *
 * You are given the customer visit log of a shop represented by a 0-indexed string customers consisting only of characters 'N' and 'Y':
 *
 * if the ith character is 'Y', it means that customers come at the ith hour
 * whereas 'N' indicates that no customers come at the ith hour.
 *
 * If the shop closes at the jth hour (0 <= j <= n), the penalty is calculated as follows:
 *
 * For every hour when the shop is open and no customers come, the penalty increases by 1.
 * For every hour when the shop is closed and customers come, the penalty increases by 1.
 *
 * Return the earliest hour at which the shop must be closed to incur a minimum penalty.
 *
 * Note that if a shop closes at the jth hour, it means the shop is closed at the hour j.
 *
 *
 * Example 1:
 *
 * Input: customers = "YYNY"
 * Output: 2
 * Explanation:
 * - Closing the shop at the 0th hour incurs in 1+1+0+1 = 3 penalty.
 * - Closing the shop at the 1st hour incurs in 0+1+0+1 = 2 penalty.
 * - Closing the shop at the 2nd hour incurs in 0+0+0+1 = 1 penalty.
 * - Closing the shop at the 3rd hour incurs in 0+0+1+1 = 2 penalty.
 * - Closing the shop at the 4th hour incurs in 0+0+1+0 = 1 penalty.
 * Closing the shop at 2nd or 4th hour gives a minimum penalty. Since 2 is earlier, the answer is 2.
 *
 * Example 2:
 *
 * Input: customers = "NNNNN"
 * Output: 0
 * Explanation: It is best to close the shop at the 0th hour as no customers arrive.
 *
 * Example 3:
 *
 * Input: customers = "YYYY"
 * Output: 4
 * Explanation: It is best to close the shop at the 4th hour as customers arrive at each hour.
 *
 *
 * Constraints:
 *
 * 1 <= customers.length <= 10^5
 * customers consists only of characters 'Y' and 'N'.
 *
 */
public class MinimumPenaltyForAShop {

    // V0
    // IDEA: SWEEP THE CLOSING HOUR, UPDATING THE PENALTY INCREMENTALLY (PREFIX SUM)
    //       closing at hour 0 costs the number of 'Y's. Moving the close from j to j + 1:
    //       a 'Y' at j is now served (-1), an 'N' at j is now wasted (+1).
    //       Updating only on a strict improvement keeps the earliest best hour.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int bestClosingTime(String customers) {
        int penalty = 0;
        for (char c : customers.toCharArray()) {
            if (c == 'Y') {
                penalty++;
            }
        }

        int bestPenalty = penalty;
        int bestHour = 0;
        for (int j = 0; j < customers.length(); j++) {
            if (customers.charAt(j) == 'Y') {
                penalty--;
            } else {
                penalty++;
            }
            if (penalty < bestPenalty) {
                bestPenalty = penalty;
                bestHour = j + 1;
            }
        }
        return bestHour;
    }
}
