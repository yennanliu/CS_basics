package LeetCodeJava.Math;

// https://leetcode.com/problems/airplane-seat-assignment-probability/description/

/**
 * 1227. Airplane Seat Assignment Probability
 * Medium
 *
 * n passengers board an airplane with exactly n seats. The first passenger has lost the ticket
 * and picks a seat randomly. But after that, the rest of the passengers will:
 *
 * Take their own seat if it is still available, and
 * Pick other seats randomly when they find their seat occupied
 *
 * Return the probability that the nth person gets his own seat.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 1.00000
 * Explanation: The first person can only get the first seat.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 0.50000
 * Explanation: The second person has a probability of 0.5 to get the second seat (when first
 * person gets the first seat).
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class AirplaneSeatAssignmentProbability {

    // V0
    // IDEA: MATH / BRAINTEASER
    //   f(n) = probability passenger n gets seat n. passenger 1 picks:
    //     seat 1 (prob 1/n)         -> everyone sits correctly         -> 1
    //     seat n (prob 1/n)         -> passenger n is displaced        -> 0
    //     seat i, 1 < i < n (1/n)   -> passenger i becomes the "lost ticket" passenger
    //                                  over the remaining n - i + 1 seats -> f(n - i + 1)
    //   f(n) = (1 + f(n-1) + ... + f(2)) / n, and with f(2) = 0.5 induction gives
    //   f(n) = 0.5 for every n >= 2.
    /**
     * time = O(1)
     * space = O(1)
     */
    public double nthPersonGetsNthSeat(int n) {
        if (n == 1) {
            return 1.0;
        }
        return 0.5;
    }

    // V1
    // IDEA: DP ON THE LITERAL RECURRENCE (useful to convince yourself of V0)
    //   f[k] = (1 + f[2] + ... + f[k-1]) / k, keeping a running sum of f[2..k-1]
    /**
     * time = O(N)
     * space = O(N)
     */
    public double nthPersonGetsNthSeat_1(int n) {
        if (n == 1) {
            return 1.0;
        }
        double[] probability = new double[n + 1];
        probability[1] = 1.0;
        probability[2] = 0.5;
        // sum of probability[2..k-1]
        double runningSum = 0.0;
        for (int k = 3; k <= n; k++) {
            runningSum += probability[k - 1];
            probability[k] = (1.0 + runningSum) / k;
        }
        return probability[n];
    }
}
