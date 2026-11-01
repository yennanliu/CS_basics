package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-number-of-occurrences-of-a-substring/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1297. Maximum Number of Occurrences of a Substring
 * Medium
 *
 * Given a string s, return the maximum number of occurrences of any substring
 * under the following rules:
 *
 * The number of unique characters in the substring must be less than or equal
 * to maxLetters.
 * The substring size must be between minSize and maxSize inclusive.
 *
 *
 * Example 1:
 *
 * Input: s = "aababcaab", maxLetters = 2, minSize = 3, maxSize = 4
 * Output: 2
 * Explanation: Substring "aab" has 2 occurrences in the original string.
 * It satisfies the conditions, 2 unique letters and size 3
 * (between minSize and maxSize).
 *
 * Example 2:
 *
 * Input: s = "aaaa", maxLetters = 1, minSize = 3, maxSize = 3
 * Output: 2
 * Explanation: Substring "aaa" occur 2 times in the string. It can overlap.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 1 <= maxLetters <= 26
 * 1 <= minSize <= maxSize <= min(26, s.length)
 * s consists of only lowercase English letters.
 *
 */
public class MaximumNumberOfOccurrencesOfASubstring {

    // V0
    // IDEA: FIXED SIZE SLIDING WINDOW (size minSize) + HASH MAP
    //       KEY INSIGHT : maxSize is a red herring. if a longer valid substring
    //       occurs c times, its length-minSize prefix occurs at least c times and
    //       has no more distinct letters -> only length-minSize windows matter.
    //       roll a letter-frequency table and a `distinct` counter so the
    //       validity check is O(1); hashing the window still costs minSize.
    /**
     * time = O(N * minSize)
     * space = O(N * minSize)
     */
    public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        int n = s.length();
        if (minSize > n) {
            return 0;
        }

        int[] freq = new int[26];
        int distinct = 0;
        Map<String, Integer> occurrences = new HashMap<>();
        int best = 0;

        int left = 0;
        for (int right = 0; right < n; right++) {
            // add s[right] to the window
            int in = s.charAt(right) - 'a';
            if (freq[in] == 0) {
                distinct++;
            }
            freq[in]++;

            // keep the window exactly minSize wide
            if (right - left + 1 > minSize) {
                int out = s.charAt(left) - 'a';
                freq[out]--;
                if (freq[out] == 0) {
                    distinct--;
                }
                left++;
            }

            // a full window with few enough letters is a candidate
            if (right - left + 1 == minSize && distinct <= maxLetters) {
                String window = s.substring(left, right + 1);
                int count = occurrences.getOrDefault(window, 0) + 1;
                occurrences.put(window, count);
                best = Math.max(best, count);
            }
        }
        return best;
    }
}
