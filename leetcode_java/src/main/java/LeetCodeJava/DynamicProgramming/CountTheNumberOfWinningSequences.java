package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-the-number-of-winning-sequences/description/

/**
 * 3320. Count The Number of Winning Sequences
 * Hard
 *
 * Alice and Bob are playing a fantasy battle game consisting of n rounds where they summon one of three magical creatures each round: a Fire Dragon, a Water Serpent, or an Earth Golem. In each round, players simultaneously summon their creature and are awarded points as follows:
 *
 * If one player summons a Fire Dragon and the other summons an Earth Golem, the player who summoned the Fire Dragon is awarded a point.
 * If one player summons a Water Serpent and the other summons a Fire Dragon, the player who summoned the Water Serpent is awarded a point.
 * If one player summons an Earth Golem and the other summons a Water Serpent, the player who summoned the Earth Golem is awarded a point.
 * If both players summon the same creature, no player is awarded a point.
 *
 * You are given a string s consisting of n characters 'F', 'W', and 'E', representing the sequence of creatures Alice will summon in each round:
 *
 * If s[i] == 'F', Alice summons a Fire Dragon.
 * If s[i] == 'W', Alice summons a Water Serpent.
 * If s[i] == 'E', Alice summons an Earth Golem.
 *
 * Bob's sequence of moves is unknown, but it is guaranteed that Bob will never summon the same creature in two consecutive rounds. Bob beats Alice if the total number of points awarded to Bob after n rounds is strictly greater than the points awarded to Alice.
 *
 * Return the number of distinct sequences Bob can use to beat Alice.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "FFF"
 * Output: 3
 * Explanation:
 * Bob can beat Alice by making one of the following sequences of moves: "WFW", "FWF", or "WEW". Note that other winning sequences like "WWE" or "EWW" are invalid since Bob cannot make the same move twice in a row.
 *
 * Example 2:
 *
 * Input: s = "FWEFW"
 * Output: 18
 * Explanation:
 * Bob can beat Alice by making one of the following sequences of moves: "FWFWF", "FWFWE", "FWEFE", "FWEWE", "FEFWF", "FEFWE", "FEFEW", "FEWFE", "WFEFE", "WFEWE", "WEFWF", "WEFWE", "WEFEF", "WEFEW", "WEWFE", "WEWFW", "EWFWF", or "EWFWE".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is one of 'F', 'W', or 'E'.
 *
 */
public class CountTheNumberOfWinningSequences {

    private static final int MOD = 1_000_000_007;
    private static final String CREATURES = "FWE";

    // V0
    // IDEA: DP OVER (ROUND, BOB'S LAST CREATURE, SCORE DIFFERENCE)
    /**
     *  The only things carried between rounds are what Bob played last (he may
     *  not repeat it) and how far ahead he is.
     *
     *  DP def:
     *     dp[c][d] = number of Bob sequences so far where he last played creature c
     *                and his lead (Bob - Alice) is d - n  (offset n keeps d >= 0)
     *
     *  DP eq (round i, Bob plays c' != c):
     *     next[c'][d + outcome(c', s[i])] += dp[c][d]
     *     -> the incoming count for c' is (sum over all c of dp[c][d]) - dp[c'][d]
     *
     *  Init:   dp[c][n + outcome(c, s[0])] = 1
     *  Answer: sum over c and over leads > 0 of dp[c][d]
     *
     *  time = O(N^2)
     *  space = O(N)
     */
    public int countWinningSequences(String s) {
        int n = s.length();
        int size = 2 * n + 1;
        int offset = n;

        long[][] dp = new long[3][size];

        // round 0: Bob may open with any creature
        for (int c = 0; c < 3; c++) {
            dp[c][offset + outcome(CREATURES.charAt(c), s.charAt(0))] += 1;
        }

        for (int i = 1; i < n; i++) {
            // total[d] = sequences with lead d, whatever Bob played last
            long[] total = new long[size];
            for (int d = 0; d < size; d++) {
                total[d] = (dp[0][d] + dp[1][d] + dp[2][d]) % MOD;
            }

            long[][] next = new long[3][size];
            for (int c = 0; c < 3; c++) {
                int delta = outcome(CREATURES.charAt(c), s.charAt(i));
                for (int d = 0; d < size; d++) {
                    // sequences whose last creature was NOT c may now play c
                    long incoming = (total[d] - dp[c][d] + MOD) % MOD;
                    if (incoming == 0) {
                        continue;
                    }
                    int newLead = d + delta;
                    if (newLead >= 0 && newLead < size) {
                        next[c][newLead] = (next[c][newLead] + incoming) % MOD;
                    }
                }
            }
            dp = next;
        }

        // Bob must finish strictly ahead
        long result = 0;
        for (int c = 0; c < 3; c++) {
            for (int d = offset + 1; d < size; d++) {
                result = (result + dp[c][d]) % MOD;
            }
        }
        return (int) result;
    }

    /** +1 if Bob's creature beats Alice's, -1 if it loses, 0 on a tie. */
    private int outcome(char bob, char alice) {
        if (bob == alice) {
            return 0;
        }
        // Fire beats Earth, Water beats Fire, Earth beats Water
        boolean bobWins = (bob == 'F' && alice == 'E')
                || (bob == 'W' && alice == 'F')
                || (bob == 'E' && alice == 'W');
        if (bobWins) {
            return 1;
        }
        return -1;
    }
}
