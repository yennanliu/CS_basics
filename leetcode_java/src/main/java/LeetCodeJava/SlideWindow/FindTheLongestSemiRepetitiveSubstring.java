package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/find-the-longest-semi-repetitive-substring/description/

/**
 * 2730. Find the Longest Semi-Repetitive Substring
 * Medium
 *
 * You are given a digit string s that consists of digits from 0 to 9.
 *
 * A string is called semi-repetitive if there is at most one adjacent pair of the same digit. For
 * example, "0010", "002020", "0123", "2002", and "54944" are semi-repetitive while the following
 * are not: "00101022" (adjacent same digit pairs are 00 and 22), and "1101234883" (adjacent same
 * digit pairs are 11 and 88).
 *
 * Return the length of the longest semi-repetitive substring of s.
 *
 *
 * Example 1:
 *
 * Input: s = "52233"
 * Output: 4
 * Explanation:
 * The longest semi-repetitive substring is "5223". Picking the whole string "52233" has two
 * adjacent same digit pairs 22 and 33, but at most one is allowed.
 *
 * Example 2:
 *
 * Input: s = "5494"
 * Output: 4
 * Explanation:
 * s is a semi-repetitive string.
 *
 * Example 3:
 *
 * Input: s = "1111111"
 * Output: 2
 * Explanation:
 * The longest semi-repetitive substring is "11". Picking the substring "111" has two adjacent
 * same digit pairs, but at most one is allowed.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * '0' <= s[i] <= '9'
 *
 */
public class FindTheLongestSemiRepetitiveSubstring {

    // V0
    // IDEA: SLIDING WINDOW ON THE NUMBER OF ADJACENT-EQUAL PAIRS INSIDE THE WINDOW
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestSemiRepetitiveSubstring(String s) {
        int n = s.length();
        // a single character is always semi-repetitive
        int best = 1;
        int left = 0;
        // pairs (p - 1, p) with left < p <= right and s[p] == s[p - 1]
        int equalPairs = 0;

        for (int right = 1; right < n; right++) {
            // the pair (right - 1, right) enters the window
            if (s.charAt(right) == s.charAt(right - 1)) {
                equalPairs++;
            }
            // shrink: moving left past `left` drops the pair (left, left + 1)
            while (equalPairs > 1) {
                if (s.charAt(left) == s.charAt(left + 1)) {
                    equalPairs--;
                }
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
