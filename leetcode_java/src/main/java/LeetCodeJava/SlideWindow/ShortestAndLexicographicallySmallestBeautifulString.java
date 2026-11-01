package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/shortest-and-lexicographically-smallest-beautiful-string/description/

/**
 * 2904. Shortest and Lexicographically Smallest Beautiful String
 * Medium
 *
 * You are given a binary string s and a positive integer k.
 *
 * A substring of s is beautiful if the number of 1's in it is exactly k.
 *
 * Let len be the length of the shortest beautiful substring.
 *
 * Return the lexicographically smallest beautiful substring of string s with length equal to
 * len. If s doesn't contain a beautiful substring, return an empty string.
 *
 * A string a is lexicographically larger than a string b (of the same length) if in the first
 * position where a and b differ, a has a character strictly larger than the corresponding
 * character in b.
 *
 * - For example, "abcd" is lexicographically larger than "abcc" because the first position they
 *   differ is at the fourth character, and d is greater than c.
 *
 *
 * Example 1:
 *
 * Input: s = "100011001", k = 3
 * Output: "11001"
 *
 * Example 2:
 *
 * Input: s = "1011", k = 2
 * Output: "11"
 *
 * Example 3:
 *
 * Input: s = "000", k = 1
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * 1 <= k <= s.length
 *
 */
public class ShortestAndLexicographicallySmallestBeautifulString {

    // V0
    // IDEA: SLIDING WINDOW WHOSE LEFT END IS PINNED TO A '1'
    //  a leading '0' never helps (dropping it keeps the 1-count and makes the string shorter),
    //  so shrink while there are too many 1's or the window starts with '0'.
    /**
     * time = O(N^2)   (copying each candidate window)
     * space = O(N)
     */
    public String shortestBeautifulSubstring(String s, int k) {
        int n = s.length();
        int left = 0;
        int ones = 0;
        String best = "";

        for (int right = 0; right < n; right++) {
            if (s.charAt(right) == '1') {
                ones++;
            }
            // shrink: too many ones, or a useless leading '0'
            while (ones > k || (left < right && s.charAt(left) == '0')) {
                if (s.charAt(left) == '1') {
                    ones--;
                }
                left++;
            }
            if (ones == k) {
                String candidate = s.substring(left, right + 1);
                // shorter wins first; only equal lengths compare lexicographically
                boolean shorter = candidate.length() < best.length();
                boolean sameLengthSmaller = candidate.length() == best.length()
                        && candidate.compareTo(best) < 0;
                if (best.isEmpty() || shorter || sameLengthSmaller) {
                    best = candidate;
                }
            }
        }
        return best;
    }
}
