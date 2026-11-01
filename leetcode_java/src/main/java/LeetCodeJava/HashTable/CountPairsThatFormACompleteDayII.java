package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-pairs-that-form-a-complete-day-ii/description/

/**
 * 3185. Count Pairs That Form a Complete Day II
 * Medium
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
 * 1 <= hours.length <= 5 * 10^5
 * 1 <= hours[i] <= 10^9
 *
 */
public class CountPairsThatFormACompleteDayII {

    // V0
    // IDEA: ONLY THE RESIDUE MOD 24 MATTERS - COUNT THE COMPLEMENT ON THE FLY
    //   hours[i] + hours[j] is a multiple of 24 exactly when their residues sum
    //   to 0 mod 24. sweeping left to right and adding the count of the
    //   complement BEFORE inserting the current residue pairs each element with
    //   the ones before it, so every i < j pair is counted once.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countCompleteDayPairs(int[] hours) {
        long[] residueCount = new long[24];
        long pairs = 0;

        for (int hour : hours) {
            int residue = hour % 24;
            // the partner residue; (24 - 0) % 24 keeps residue 0 paired with 0
            int complement = (24 - residue) % 24;
            pairs += residueCount[complement];
            residueCount[residue]++;
        }
        return pairs;
    }
}
