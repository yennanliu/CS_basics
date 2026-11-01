package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-occurrence-of-first-almost-equal-substring/description/

/**
 * 3303. Find the Occurrence of First Almost Equal Substring
 * Hard
 *
 * You are given two strings s and pattern.
 *
 * A string x is called almost equal to y if you can change at most one character in x to make it identical to y.
 *
 * Return the smallest starting index of a substring in s that is almost equal to pattern. If no such index exists, return -1.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "abcdefg", pattern = "bcdffg"
 * Output: 1
 * Explanation:
 * The substring s[1..6] == "bcdefg" can be converted to "bcdffg" by changing s[4] to "f".
 *
 * Example 2:
 *
 * Input: s = "ababbababa", pattern = "bacaba"
 * Output: 4
 * Explanation:
 * The substring s[4..9] == "bababa" can be converted to "bacaba" by changing s[6] to "c".
 *
 * Example 3:
 *
 * Input: s = "abcd", pattern = "dba"
 * Output: -1
 *
 * Example 4:
 *
 * Input: s = "dde", pattern = "d"
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length < s.length <= 10^5
 * s and pattern consist only of lowercase English letters.
 *
 */
public class FindTheOccurrenceOfFirstAlmostEqualSubstring {

    // V0
    // IDEA: Z-FUNCTION FROM BOTH ENDS -> prefixMatch + suffixMatch >= m - 1 MEANS AT MOST ONE MISMATCH
    //   forward Z over (pattern + '#' + s) gives how many leading pattern chars match at each start;
    //   the same over both strings REVERSED gives how many trailing chars match at each window end.
    //   If they cover all but one position, changing that one character makes the window equal.
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public int minStartingIndex(String s, String pattern) {
        int n = s.length();
        int m = pattern.length();

        int[] forward = zArray(pattern + "#" + s);
        String reversedPattern = new StringBuilder(pattern).reverse().toString();
        String reversedS = new StringBuilder(s).reverse().toString();
        int[] backward = zArray(reversedPattern + "#" + reversedS);
        int offset = m + 1;

        for (int start = 0; start + m <= n; start++) {
            // leading characters of pattern matching s from `start`
            int prefixMatch = forward[offset + start];
            if (prefixMatch >= m) {
                return start;
            }
            // the window ends at `end`; in the reversed string that index is n - 1 - end
            int end = start + m - 1;
            int suffixMatch = backward[offset + (n - 1 - end)];
            if (prefixMatch + suffixMatch >= m - 1) {
                return start;
            }
        }
        return -1;
    }

    /** Standard Z-algorithm: z[i] = length of the longest common prefix of s and s[i..]. */
    private int[] zArray(String s) {
        int len = s.length();
        int[] z = new int[len];
        int left = 0;
        int right = 0;
        for (int i = 1; i < len; i++) {
            if (i < right) {
                z[i] = Math.min(right - i, z[i - left]);
            }
            while (i + z[i] < len && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > right) {
                left = i;
                right = i + z[i];
            }
        }
        return z;
    }
}
