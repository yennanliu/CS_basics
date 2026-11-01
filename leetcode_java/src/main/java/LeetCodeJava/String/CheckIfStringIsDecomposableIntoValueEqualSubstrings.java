package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-string-is-decomposable-into-value-equal-substrings/description/
// https://leetcode.ca/2021-03-16-1933-Check-if-String-Is-Decomposable-Into-Value-Equal-Substrings/

/**
 * 1933. Check if String Is Decomposable Into Value-Equal Substrings
 * Easy
 *
 * A value-equal string is a string where all characters are the same.
 *
 * For example, "1111" and "33" are value-equal strings.
 * In contrast, "123" is not a value-equal string.
 *
 * Given a digit string s, decompose the string into some number of consecutive value-equal substrings where exactly one substring has a length of 2 and the remaining substrings have a length of 3.
 *
 * Return true if you can decompose s according to the above rules. Otherwise, return false.
 *
 * A substring is a contiguous sequence of characters in a string.
 *
 *
 * Example 1:
 *
 * Input: s = "000111000"
 * Output: false
 * Explanation: s cannot be decomposed according to the rules because ["000", "111", "000"] does not have a substring of length 2.
 *
 * Example 2:
 *
 * Input: s = "00011111222"
 * Output: true
 * Explanation: s can be decomposed into ["000", "111", "11", "222"].
 *
 * Example 3:
 *
 * Input: s = "011100022233"
 * Output: false
 * Explanation: s cannot be decomposed according to the rules because of the first '0'.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of only digits '0' through '9'.
 *
 */
public class CheckIfStringIsDecomposableIntoValueEqualSubstrings {

    // V0
    // IDEA: GROUP EQUAL RUNS, THEN LOOK AT EACH RUN LENGTH MOD 3
    //       each maximal run of one digit is cut on its own into 3's and at most one 2:
    //         len % 3 == 0 -> all 3's
    //         len % 3 == 2 -> uses the single allowed 2
    //         len % 3 == 1 -> impossible
    //       so reject any len % 3 == 1 run and require exactly one run needing a 2.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isDecomposable(String s) {
        int twos = 0;
        int i = 0;
        while (i < s.length()) {
            // find the end of the run that starts at i
            int end = i;
            while (end < s.length() && s.charAt(end) == s.charAt(i)) {
                end++;
            }
            int runLength = end - i;
            if (runLength % 3 == 1) {
                return false;
            }
            if (runLength % 3 == 2) {
                twos++;
                if (twos > 1) {
                    return false;
                }
            }
            i = end;
        }
        return twos == 1;
    }
}
