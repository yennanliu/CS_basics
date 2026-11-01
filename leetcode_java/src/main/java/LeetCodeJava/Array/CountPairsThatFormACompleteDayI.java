package LeetCodeJava.Array;

// https://leetcode.com/problems/count-pairs-that-form-a-complete-day-i/description/

/**
 * 3184. Count Pairs That Form a Complete Day I
 * Easy
 *
 * Given an integer array hours representing times in hours, return an integer denoting the number of pairs i, j where i < j and hours[i] + hours[j] forms a complete day.
 *
 * A complete day is defined as a time duration that is an exact multiple of 24 hours.
 *
 * For example, 1 day is 24 hours, 2 days is 48 hours, and so on.
 *
 *
 * Example 1:
 *
 * Input: hours = [12,12,30,24,24]
 * Output: 2
 * Explanation:
 * The pairs of indices that form a complete day are (0, 1) and (3, 4).
 *
 * Example 2:
 *
 * Input: hours = [72,48,24,3]
 * Output: 3
 * Explanation:
 * The pairs of indices that form a complete day are (0, 1), (0, 2), and (1, 2).
 *
 *
 * Constraints:
 *
 * 1 <= hours.length <= 100
 * 1 <= hours[i] <= 10^9
 *
 */
public class CountPairsThatFormACompleteDayI {

    // V0
    // IDEA: n <= 100 - TEST EVERY PAIR
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int countCompleteDayPairs(int[] hours) {
        int n = hours.length;
        int pairs = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // long: two values up to 10^9 overflow an int when added
                long sum = (long) hours[i] + hours[j];
                if (sum % 24 == 0) {
                    pairs++;
                }
            }
        }
        return pairs;
    }

    // V1
    // IDEA: ONE PASS + COMPLEMENTARY RESIDUE COUNTER
    // hours[i] + hours[j] is a multiple of 24 exactly when the residues add up
    // to 0 mod 24, so for each value count the earlier values with the
    // complementary residue (the approach the sequel LC 3185 needs).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countCompleteDayPairs_1(int[] hours) {
        int[] residueCount = new int[24];
        int pairs = 0;
        for (int hour : hours) {
            int residue = hour % 24;
            // residue 0 pairs with residue 0, otherwise with 24 - residue
            int needed = (24 - residue) % 24;
            pairs += residueCount[needed];
            residueCount[residue]++;
        }
        return pairs;
    }
}
