package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/find-k-length-substrings-with-no-repeated-characters/description/
// https://leetcode.ca/2018-12-04-1100-Find-K-Length-Substrings-With-No-Repeated-Characters/

/**
 * 1100. Find K-Length Substrings With No Repeated Characters
 * Medium
 *
 * Given a string s and an integer k, return the number of substrings in s of
 * length k with no repeated characters.
 *
 *
 * Example 1:
 *
 * Input: s = "havefunonleetcode", k = 5
 * Output: 6
 * Explanation: There are 6 substrings they are:
 * 'havef','avefu','vefun','efuno','etcod','tcode'.
 *
 * Example 2:
 *
 * Input: s = "home", k = 5
 * Output: 0
 * Explanation: Notice k can be larger than the length of s.
 * In this case, it is not possible to find any substring.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s consists of lowercase English letters.
 * 1 <= k <= 10^4
 *
 */
public class FindKLengthSubstringsWithNoRepeatedCharacters {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + CHAR COUNT
    //       keep a window of size k and a count of each char in it; the window is valid
    //       <-> it holds k DISTINCT chars.
    /**
     * time = O(N)
     * space = O(1)   (26 counters)
     */
    public int numKLenSubstrNoRepeats(String s, int k) {
        int n = s.length();
        // window bigger than s, or more distinct letters than the alphabet has
        if (k > n || k > 26) {
            return 0;
        }

        int[] count = new int[26];
        int distinct = 0;
        int result = 0;

        for (int right = 0; right < n; right++) {
            int in = s.charAt(right) - 'a';
            if (count[in] == 0) {
                distinct++;
            }
            count[in]++;

            // NOTE !!! drop the char that left the window, so its size stays k
            if (right >= k) {
                int out = s.charAt(right - k) - 'a';
                count[out]--;
                if (count[out] == 0) {
                    distinct--;
                }
            }

            // a full window with k distinct chars has no repeat
            if (right >= k - 1 && distinct == k) {
                result++;
            }
        }
        return result;
    }
}
