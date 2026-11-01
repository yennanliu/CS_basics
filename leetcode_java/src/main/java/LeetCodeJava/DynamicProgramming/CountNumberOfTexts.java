package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-texts/description/

/**
 * 2266. Count Number of Texts
 * Medium
 *
 * Alice is texting Bob using her phone. The mapping of digits to letters is shown in the figure below.
 *
 * In order to add a letter, Alice has to press the key of the corresponding digit i times, where i is the position of the letter in the key.
 *
 * For example, to add the letter 's', Alice has to press '7' four times. Similarly, to add the letter 'k', Alice has to press '5' twice.
 *
 * Note that the digits '0' and '1' do not map to any letters, so Alice does not use them.
 *
 * However, due to an error in transmission, Bob did not receive Alice's text message but received a string of pressed keys instead.
 *
 * For example, when Alice sent the message "bob", Bob received the string "2266622".
 *
 * Given a string pressedKeys representing the string received by Bob, return the total number of possible text messages Alice could have sent.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: pressedKeys = "22233"
 * Output: 8
 * Explanation:
 * The possible text messages Alice could have sent are:
 * "aaadd", "abdd", "badd", "cdd", "aaae", "abe", "bae", and "ce".
 * Since there are 8 possible messages, we return 8.
 *
 * Example 2:
 *
 * Input: pressedKeys = "222222222222222222222222222222222222"
 * Output: 82876089
 * Explanation:
 * There are 2082876103 possible text messages Alice could have sent.
 * Since we need to return the answer modulo 10^9 + 7, we return 2082876103 % (10^9 + 7) = 82876089.
 *
 *
 * Constraints:
 *
 * 1 <= pressedKeys.length <= 10^5
 * pressedKeys only consists of digits from '2' - '9'.
 *
 */
public class CountNumberOfTexts {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SPLIT INTO RUNS OF THE SAME KEY; EACH RUN IS A "CLIMB STAIRS" DP WITH 3 OR 4 STEP SIZES
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countTexts(String pressedKeys) {
        int n = pressedKeys.length();

        // ways3[i] = ways to split a run of length i into groups of size 1..3 (most keys)
        // ways4[i] = same with groups of size 1..4 (keys '7' and '9' carry 4 letters)
        long[] ways3 = new long[n + 1];
        long[] ways4 = new long[n + 1];
        ways3[0] = 1;
        ways4[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int step = 1; step <= 4; step++) {
                if (i - step < 0) {
                    break;
                }
                if (step <= 3) {
                    ways3[i] = (ways3[i] + ways3[i - step]) % MOD;
                }
                ways4[i] = (ways4[i] + ways4[i - step]) % MOD;
            }
        }

        // runs of different keys decode independently, so multiply their counts
        long result = 1;
        int i = 0;
        while (i < n) {
            char key = pressedKeys.charAt(i);
            int runEnd = i;
            while (runEnd < n && pressedKeys.charAt(runEnd) == key) {
                runEnd++;
            }
            int runLength = runEnd - i;

            boolean fourLetters = (key == '7' || key == '9');
            long runWays = fourLetters ? ways4[runLength] : ways3[runLength];
            result = (result * runWays) % MOD;

            i = runEnd;
        }
        return (int) result;
    }
}
