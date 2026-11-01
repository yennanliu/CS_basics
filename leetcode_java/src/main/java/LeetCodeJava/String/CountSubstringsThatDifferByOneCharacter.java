package LeetCodeJava.String;

// https://leetcode.com/problems/count-substrings-that-differ-by-one-character/description/

/**
 * 1638. Count Substrings That Differ by One Character
 * Medium
 *
 * Given two strings s and t, find the number of ways you can choose a non-empty substring of s and replace a single character by a different character such that the resulting substring is a substring of t. In other words, find the number of substrings in s that differ from some substring in t by exactly one character.
 *
 * For example, the underlined substrings in "computer" and "computation" only differ by the 'e'/'a', so this is a valid way.
 *
 * Return the number of substrings that satisfy the condition above.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "aba", t = "baba"
 * Output: 6
 * Explanation: The following are the pairs of substrings from s and t that differ by exactly 1 character:
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * ("aba", "baba")
 * The underlined portions are the substrings that are chosen from s and t.
 *
 * Example 2:
 *
 * Input: s = "ab", t = "bb"
 * Output: 3
 * Explanation: The following are the pairs of substrings from s and t that differ by 1 character:
 * ("ab", "bb")
 * ("ab", "bb")
 * ("ab", "bb")
 * The underlined portions are the substrings that are chosen from s and t.
 *
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 100
 * s and t consist of lowercase English letters only.
 *
 */
public class CountSubstringsThatDifferByOneCharacter {

    // V0
    // IDEA: PIVOT ON THE MISMATCH, THEN EXPAND BOTH WAYS
    //       every valid pair has exactly one differing position, so enumerate it:
    //       a pair (i, j) with s[i] != t[j]. Let left = how far the strings stay
    //       equal going left, right = going right. Any of the (left + 1) left
    //       extents combines with any of the (right + 1) right extents.
    /**
     * time = O(M * N * min(M, N))
     * space = O(1)
     */
    public int countSubstrings(String s, String t) {
        int m = s.length();
        int n = t.length();
        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (s.charAt(i) == t.charAt(j)) {
                    continue;
                }
                // s[i] vs t[j] is the single mismatch; measure the equal run on each side
                int left = 0;
                while (i - left - 1 >= 0 && j - left - 1 >= 0
                        && s.charAt(i - left - 1) == t.charAt(j - left - 1)) {
                    left++;
                }
                int right = 0;
                while (i + right + 1 < m && j + right + 1 < n
                        && s.charAt(i + right + 1) == t.charAt(j + right + 1)) {
                    right++;
                }
                count += (left + 1) * (right + 1);
            }
        }
        return count;
    }
}
