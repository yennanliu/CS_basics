package LeetCodeJava.Greedy;

// https://leetcode.com/problems/number-of-ways-to-divide-a-long-corridor/description/

/**
 * 2147. Number of Ways to Divide a Long Corridor
 * Hard
 *
 * Along a long library corridor, there is a line of seats and decorative plants. You are given a 0-indexed
 * string corridor of length n consisting of letters 'S' and 'P' where each 'S' represents a seat and each
 * 'P' represents a plant.
 *
 * One room divider has already been installed to the left of index 0, and another to the right of index
 * n - 1. Additional room dividers can be installed. For each position between indices i - 1 and i
 * (1 <= i <= n - 1), at most one divider can be installed.
 *
 * Divide the corridor into non-overlapping sections, where each section has exactly two seats with any
 * number of plants. There may be multiple ways to perform the division. Two ways are different if there is
 * a position with a room divider installed in the first way but not in the second way.
 *
 * Return the number of ways to divide the corridor. Since the answer may be very large, return it modulo
 * 10^9 + 7. If there is no way, return 0.
 *
 *
 * Example 1:
 *
 * Input: corridor = "SSPPSPS"
 * Output: 3
 *
 * Example 2:
 *
 * Input: corridor = "PPSPSP"
 * Output: 1
 *
 * Example 3:
 *
 * Input: corridor = "S"
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == corridor.length
 * 1 <= n <= 10^5
 * corridor[i] is either 'S' or 'P'.
 *
 */
public class NumberOfWaysToDivideALongCorridor {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PAIR UP THE SEATS, MULTIPLY THE GAPS BETWEEN CONSECUTIVE PAIRS
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numberOfWays(String corridor) {
        long ways = 1;
        int seatCount = 0;
        // index of the 2nd seat of the most recently completed pair
        int lastPairEnd = -1;

        for (int i = 0; i < corridor.length(); i++) {
            if (corridor.charAt(i) != 'S') {
                continue;
            }
            seatCount++;

            // the 1st seat of a new pair (3rd, 5th, ...): the divider may go in any slot
            // between the previous pair's 2nd seat and this seat -> (i - lastPairEnd) choices
            if (seatCount > 2 && seatCount % 2 == 1) {
                ways = ways * (i - lastPairEnd) % MOD;
            }
            // the 2nd seat of a pair closes it
            if (seatCount % 2 == 0) {
                lastPairEnd = i;
            }
        }

        // zero seats, or an odd number of them, admits no valid division
        if (seatCount == 0 || seatCount % 2 == 1) {
            return 0;
        }
        return (int) ways;
    }
}
