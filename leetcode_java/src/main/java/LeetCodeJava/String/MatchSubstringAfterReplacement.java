package LeetCodeJava.String;

// https://leetcode.com/problems/match-substring-after-replacement/description/

/**
 * 2301. Match Substring After Replacement
 * Hard
 *
 * You are given two strings s and sub. You are also given a 2D character array mappings where mappings[i] = [oldi, newi] indicates that you may perform the following operation any number of times:
 *
 * Replace a character oldi of sub with newi.
 *
 * Each character in sub cannot be replaced more than once.
 *
 * Return true if it is possible to make sub a substring of s by replacing zero or more characters according to mappings. Otherwise, return false.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "fool3e7bar", sub = "leet", mappings = [["e","3"],["t","7"],["t","8"]]
 * Output: true
 * Explanation: Replace the first 'e' in sub with '3' and 't' in sub with '7'.
 * Now sub = "l3e7" is a substring of s, so we return true.
 *
 * Example 2:
 *
 * Input: s = "fooleetbar", sub = "f00l", mappings = [["o","0"]]
 * Output: false
 * Explanation: The string "f00l" is not a substring of s and no replacements can be made.
 * Note that we cannot replace '0' with 'o'.
 *
 * Example 3:
 *
 * Input: s = "Fool33tbaR", sub = "leetd", mappings = [["e","3"],["t","7"],["t","8"],["d","b"],["p","b"]]
 * Output: true
 * Explanation: Replace the first and second 'e' in sub with '3' and 'd' in sub with 'b'.
 * Now sub = "l33tb" is a substring of s, so we return true.
 *
 *
 * Constraints:
 *
 * 1 <= sub.length <= s.length <= 5000
 * 0 <= mappings.length <= 1000
 * mappings[i].length == 2
 * oldi != newi
 * s and sub consist of uppercase and lowercase English letters and digits.
 * mappings[i][0] and mappings[i][1] are either uppercase or lowercase English letters or digits.
 *
 */
public class MatchSubstringAfterReplacement {

    // V0
    // IDEA: PRECOMPUTE "WHAT CAN EACH sub CHARACTER BECOME", THEN SLIDE
    //       each position of sub is replaced at most once, so positions are
    //       independent: sub[j] matches s[i + j] iff they are equal or
    //       (sub[j] -> s[i + j]) is one of the mappings.
    //       A boolean[128][128] table makes that test O(1).
    /**
     * time = O(N * M + K)   N = s.length, M = sub.length, K = mappings.length
     * space = O(1)          the 128 x 128 table is fixed size
     */
    public boolean matchReplacement(String s, String sub, char[][] mappings) {
        // canBecome[a][b] == true  <=>  character a of sub may be replaced by b
        boolean[][] canBecome = new boolean[128][128];
        for (char[] mapping : mappings) {
            canBecome[mapping[0]][mapping[1]] = true;
        }

        int n = s.length();
        int m = sub.length();
        // try every window of s with length m
        for (int start = 0; start + m <= n; start++) {
            boolean matches = true;
            for (int j = 0; j < m; j++) {
                char fromSub = sub.charAt(j);
                char fromS = s.charAt(start + j);
                if (fromSub != fromS && !canBecome[fromSub][fromS]) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }
}
