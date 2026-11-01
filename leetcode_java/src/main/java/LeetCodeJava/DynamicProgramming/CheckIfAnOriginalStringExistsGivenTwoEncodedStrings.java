package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/check-if-an-original-string-exists-given-two-encoded-strings/description/

import java.util.*;

/**
 * 2060. Check if an Original String Exists Given Two Encoded Strings
 * Hard
 *
 * An original string, consisting of lowercase English letters, can be encoded by the
 * following steps:
 *
 * Arbitrarily split it into a sequence of some number of non-empty substrings.
 * Arbitrarily choose some elements (possibly none) of the sequence, and replace each with
 * its length (as a numeric string).
 * Concatenate the sequence as the encoded string.
 *
 * For example, one way to encode an original string "abcdefghijklmnop" might be:
 * Split it as a sequence: ["ab", "cdefghijklmn", "o", "p"].
 * Choose the second and third elements to be replaced by their lengths, respectively. The
 * sequence becomes ["ab", "12", "1", "p"].
 * Concatenate the elements of the sequence to get the encoded string: "ab121p".
 *
 * Given two encoded strings s1 and s2, consisting of lowercase English letters and digits
 * 1-9 (inclusive), return true if there exists an original string that could be encoded
 * as both s1 and s2. Otherwise, return false.
 *
 * Note: The test cases are generated such that the number of consecutive digits in s1
 * and s2 does not exceed 3.
 *
 *
 * Example 1:
 *
 * Input: s1 = "internationalization", s2 = "i18n"
 * Output: true
 * Explanation: It is possible that "internationalization" was the original string.
 *
 * Example 2:
 *
 * Input: s1 = "l123e", s2 = "44"
 * Output: true
 * Explanation: It is possible that "leetcode" was the original string.
 * - "l123e" is ["l", "1", "2", "3", "e"] for the split ["l", "e", "et", "cod", "e"].
 * - "44" is ["4", "4"] for the split ["leet", "code"].
 *
 * Example 3:
 *
 * Input: s1 = "a5b", s2 = "c5b"
 * Output: false
 * Explanation: It is impossible.
 * - The original string encoded as s1 must start with the letter 'a'.
 * - The original string encoded as s2 must start with the letter 'c'.
 *
 *
 * Constraints:
 *
 * 1 <= s1.length, s2.length <= 40
 * s1 and s2 consist of digits 1-9 (inclusive), and lowercase English letters only.
 * The number of consecutive digits in s1 and s2 does not exceed 3.
 *
 */
public class CheckIfAnOriginalStringExistsGivenTwoEncodedStrings {

    /** A digit run has at most 3 digits, so |diff| never exceeds 999. */
    private static final int OFFSET = 1000;

    private static final byte UNKNOWN = 0;
    private static final byte TRUE = 1;
    private static final byte FALSE = 2;

    private String s1;
    private String s2;
    private byte[][][] memo;

    // V0
    // IDEA: MEMOISED DFS ON (i, j, diff) -- "WILDCARD BUDGET" MATCHING
    /**
     *  DP def:
     *     - canMatch(i, j, diff) = can s1[i:] and s2[j:] still decode to the same string,
     *       where diff = (original chars produced by s1[:i]) - (those produced by s2[:j]).
     *       diff > 0 means s1 is ahead by `diff` wildcard characters, and vice versa.
     *
     *  DP eq:
     *     s1[i] starts a digit run      -> try every number the run can mean: diff + x
     *     s2[j] starts a digit run      -> same for s2: diff - x
     *     diff == 0 (both on a letter)  -> letters must match: (i+1, j+1, 0)
     *     diff  > 0                     -> s1's wildcard eats s2's letter: (i, j+1, diff-1)
     *     diff  < 0                     -> s2's wildcard eats s1's letter: (i+1, j, diff+1)
     *
     *  Base: both strings consumed -> true iff diff == 0.
     *  A run like "123" can be cut into several numbers, so it can mean any SUM of
     *  a split: 123, 1+23=24, 12+3=15, 1+2+3=6.
     *
     *  time = O(N * M * D), D = 2 * 1000 possible diff values
     *  space = O(N * M * D)
     */
    public boolean possiblyEquals(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.memo = new byte[s1.length() + 1][s2.length() + 1][2 * OFFSET + 1];
        return canMatch(0, 0, 0);
    }

    private boolean canMatch(int i, int j, int diff) {
        int n = s1.length();
        int m = s2.length();
        if (i == n && j == m) {
            return diff == 0;
        }
        if (memo[i][j][diff + OFFSET] != UNKNOWN) {
            return memo[i][j][diff + OFFSET] == TRUE;
        }

        boolean result = false;

        if (i < n && Character.isDigit(s1.charAt(i))) {
            // s1 starts a digit run: consume the whole run as one or more numbers
            int end = i;
            while (end < n && Character.isDigit(s1.charAt(end))) {
                end++;
            }
            for (int value : splitSums(s1.substring(i, end))) {
                if (canMatch(end, j, diff + value)) {
                    result = true;
                    break;
                }
            }
        } else if (j < m && Character.isDigit(s2.charAt(j))) {
            // s2 starts a digit run: it produces characters on s2's side
            int end = j;
            while (end < m && Character.isDigit(s2.charAt(end))) {
                end++;
            }
            for (int value : splitSums(s2.substring(j, end))) {
                if (canMatch(i, end, diff - value)) {
                    result = true;
                    break;
                }
            }
        } else if (diff == 0) {
            // both sides are on a literal letter and level: the letters must agree
            result = i < n && j < m && s1.charAt(i) == s2.charAt(j) && canMatch(i + 1, j + 1, 0);
        } else if (diff > 0) {
            // s1 is ahead: its pending wildcards absorb s2's next letter
            result = j < m && canMatch(i, j + 1, diff - 1);
        } else {
            // s2's pending wildcards absorb s1's next letter
            result = i < n && canMatch(i + 1, j, diff + 1);
        }

        memo[i][j][diff + OFFSET] = result ? TRUE : FALSE;
        return result;
    }

    /** Every total a digit run can stand for, by cutting it into consecutive numbers. */
    private Set<Integer> splitSums(String digits) {
        Set<Integer> sums = new HashSet<>();
        // no cut: the whole run is a single number
        sums.add(Integer.parseInt(digits));
        // first number is digits[0..cut), the rest is split recursively
        for (int cut = 1; cut < digits.length(); cut++) {
            int head = Integer.parseInt(digits.substring(0, cut));
            for (int tailSum : splitSums(digits.substring(cut))) {
                sums.add(head + tailSum);
            }
        }
        return sums;
    }
}
