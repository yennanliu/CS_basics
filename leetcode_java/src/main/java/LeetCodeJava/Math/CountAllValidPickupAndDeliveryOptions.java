package LeetCodeJava.Math;

// https://leetcode.com/problems/count-all-valid-pickup-and-delivery-options/description/

/**
 * 1359. Count All Valid Pickup and Delivery Options
 * Hard
 *
 * Given n orders, each order consists of a pickup and a delivery service.
 *
 * Count all valid pickup/delivery possible sequences such that delivery(i) is always after of pickup(i).
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 1
 * Explanation: Unique order (P1, D1), Delivery 1 always is after of Pickup 1.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 6
 * Explanation: All possible orders:
 * (P1,P2,D1,D2), (P1,P2,D2,D1), (P1,D1,P2,D2), (P2,P1,D1,D2), (P2,P1,D2,D1) and (P2,D2,P1,D1).
 * This is an invalid order (P1,D2,P2,D1) because Pickup 2 is after of Delivery 2.
 *
 * Example 3:
 *
 * Input: n = 3
 * Output: 90
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 *
 */
public class CountAllValidPickupAndDeliveryOptions {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMBINATORICS / 1D DP
    //   f[i] = number of valid sequences of i orders.
    //   Insert the pair (Pi, Di) into a valid sequence of i - 1 orders (length 2i - 2):
    //   choose 2 of the 2i slots for it, Pi first -> C(2i, 2) = i * (2i - 1) ways.
    //   f[i] = i * (2i - 1) * f[i - 1],  f[1] = 1
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countOrders(int n) {
        // only f[i - 1] is needed, so one variable is enough
        long ways = 1;
        for (int i = 2; i <= n; i++) {
            long placements = (long) i * (2L * i - 1);
            ways = ways * placements % MOD;
        }
        return (int) ways;
    }
}
