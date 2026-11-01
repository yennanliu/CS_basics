package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/number-of-equal-count-substrings/description/

/**
 * 2067. Number of Equal Count Substrings
 * Medium
 *
 * You are given a 0-indexed string s consisting of only lowercase English letters, and an integer count. A substring of s is said to be an equal count substring if, for each unique letter in the substring, it appears exactly count times in the substring.
 *
 * Return the number of equal count substrings in s.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "aaabcbbcc", count = 3
 * Output: 3
 * Explanation:
 * The substring that starts at index 0 and ends at index 2 is "aaa".
 * The letter 'a' in the substring appears exactly 3 times.
 * The substring that starts at index 3 and ends at index 8 is "bcbbcc".
 * The letters 'b' and 'c' in the substring appear exactly 3 times.
 * The substring that starts at index 0 and ends at index 8 is "aaabcbbcc".
 * The letters 'a', 'b', and 'c' in the substring appear exactly 3 times.
 *
 * Example 2:
 *
 * Input: s = "abcd", count = 2
 * Output: 0
 * Explanation:
 * The number of times each letter appears in s is less than count.
 * Therefore, no substrings in s are equal count substrings, so return 0.
 *
 * Example 3:
 *
 * Input: s = "a", count = 5
 * Output: 0
 * Explanation:
 * The number of times each letter appears in s is less than count.
 * Therefore, no substrings in s are equal count substrings, so return 0
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3 * 10^4
 * 1 <= count <= 3 * 10^4
 * s consists only of lowercase English letters.
 *
 */
public class NumberOfEqualCountSubstrings {

    // V0
    // IDEA: ONE FIXED-SIZE SLIDING WINDOW PER NUMBER OF DISTINCT LETTERS
    //  a valid substring with exactly d distinct letters, each appearing `count` times, has
    //  length d * count. d ranges over 1..26, so 26 fixed-width windows cover every candidate.
    //  inside one pass, `exact` = how many letters currently occur exactly `count` times;
    //  the window is valid iff exact == d (d letters fill all d * count characters).
    /**
     * time = O(26 * N)
     * space = O(26)
     */
    public int equalCountSubstrings(String s, int count) {
        int n = s.length();
        int result = 0;

        for (int distinct = 1; distinct <= 26; distinct++) {
            // a longer window cannot fit in s, and neither can any larger distinct
            long windowLong = (long) distinct * count;
            if (windowLong > n) {
                break;
            }
            int window = (int) windowLong;

            int[] freq = new int[26];
            int exact = 0;
            for (int right = 0; right < n; right++) {
                // add s[right]: it may reach `count` (+1) or overshoot it (-1)
                int in = s.charAt(right) - 'a';
                freq[in]++;
                if (freq[in] == count) {
                    exact++;
                } else if (freq[in] == count + 1) {
                    exact--;
                }

                // drop the character that just left the fixed-width window
                if (right >= window) {
                    int out = s.charAt(right - window) - 'a';
                    freq[out]--;
                    if (freq[out] == count) {
                        exact++;
                    } else if (freq[out] == count - 1) {
                        exact--;
                    }
                }

                if (exact == distinct) {
                    result++;
                }
            }
        }
        return result;
    }
}
