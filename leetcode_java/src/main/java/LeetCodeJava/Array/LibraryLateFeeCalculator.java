package LeetCodeJava.Array;

// https://leetcode.com/problems/library-late-fee-calculator/description/

/**
 * 3687. Library Late Fee Calculator
 * Easy
 *
 * You are given an integer array daysLate where daysLate[i] indicates how many
 * days late the ith book was returned.
 *
 * The penalty is calculated as follows:
 *
 * If daysLate[i] == 1, penalty is 1.
 * If 2 <= daysLate[i] <= 5, penalty is 2 * daysLate[i].
 * If daysLate[i] > 5, penalty is 3 * daysLate[i].
 *
 * Return the total penalty for all books.
 *
 *
 * Example 1:
 *
 * Input: daysLate = [5,1,7]
 * Output: 32
 *
 * Example 2:
 *
 * Input: daysLate = [1,1]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= daysLate.length <= 100
 * 1 <= daysLate[i] <= 100
 *
 */
public class LibraryLateFeeCalculator {

    // V0
    // IDEA: PER-BOOK PIECEWISE FEE, SUMMED
    //   tiers are {1}, [2..5], [6..]; books do not interact, so sum f(daysLate[i]).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int lateFee(int[] daysLate) {
        int total = 0;
        for (int days : daysLate) {
            if (days == 1) {
                total += 1;
            } else if (days <= 5) {
                total += 2 * days;
            } else {
                total += 3 * days;
            }
        }
        return total;
    }
}
