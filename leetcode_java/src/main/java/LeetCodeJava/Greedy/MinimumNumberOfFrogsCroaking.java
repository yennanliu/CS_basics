package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-frogs-croaking/description/

/**
 * 1419. Minimum Number of Frogs Croaking
 * Medium
 *
 * You are given the string croakOfFrogs, which represents a combination of the string "croak"
 * from different frogs, that is, multiple frogs can croak at the same time, so multiple "croak"
 * are mixed.
 *
 * Return the minimum number of different frogs to finish all the croaks in the given string.
 *
 * A valid "croak" means a frog is printing five letters 'c', 'r', 'o', 'a', and 'k'
 * sequentially. The frogs have to print all five letters to finish a croak. If the given
 * string is not a combination of a valid "croak" return -1.
 *
 *
 * Example 1:
 *
 * Input: croakOfFrogs = "croakcroak"
 * Output: 1
 * Explanation: One frog yelling "croak" twice.
 *
 * Example 2:
 *
 * Input: croakOfFrogs = "crcoakroak"
 * Output: 2
 * Explanation: The minimum number of frogs is two.
 * The first frog could yell "crcoakroak".
 * The second frog could yell later "crcoakroak".
 *
 * Example 3:
 *
 * Input: croakOfFrogs = "croakcrook"
 * Output: -1
 * Explanation: The given string is an invalid combination of "croak" from different frogs.
 *
 *
 * Constraints:
 *
 * 1 <= croakOfFrogs.length <= 10^5
 * croakOfFrogs is either 'c', 'r', 'o', 'a', or 'k'.
 *
 */
public class MinimumNumberOfFrogsCroaking {

    private static final String CROAK = "croak";

    // V0
    // IDEA: GREEDY COUNTING (frogs waiting at each stage of "croak"; answer = peak active frogs)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minNumberOfFrogs(String croakOfFrogs) {
        // waiting[i] = frogs that have just said letter i of "croak"
        int[] waiting = new int[5];
        // frogs that started a croak and have not finished it
        int active = 0;
        int peak = 0;

        for (int idx = 0; idx < croakOfFrogs.length(); idx++) {
            int stage = CROAK.indexOf(croakOfFrogs.charAt(idx));
            if (stage < 0) {
                return -1;
            }

            if (stage == 0) {
                // 'c' starts a croak (a finished frog can be reused, so count active ones)
                waiting[0]++;
                active++;
                peak = Math.max(peak, active);
            } else {
                // every other letter must continue a frog waiting at the previous stage
                if (waiting[stage - 1] == 0) {
                    return -1;
                }
                waiting[stage - 1]--;
                if (stage == 4) {
                    // 'k' finishes the croak; this frog is free again
                    active--;
                } else {
                    waiting[stage]++;
                }
            }
        }

        // every croak must be complete at the end
        if (active != 0) {
            return -1;
        }
        return peak;
    }
}
