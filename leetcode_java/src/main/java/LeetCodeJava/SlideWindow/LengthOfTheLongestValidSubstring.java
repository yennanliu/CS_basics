package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/length-of-the-longest-valid-substring/description/

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 2781. Length of the Longest Valid Substring
 * Hard
 *
 * You are given a string word and an array of strings forbidden.
 *
 * A string is called valid if none of its substrings are present in forbidden.
 *
 * Return the length of the longest valid substring of the string word.
 *
 * A substring is a contiguous sequence of characters in a string, possibly empty.
 *
 *
 * Example 1:
 *
 * Input: word = "cbaaaabc", forbidden = ["aaa","cb"]
 * Output: 4
 *
 * Example 2:
 *
 * Input: word = "leetcode", forbidden = ["de","le","e"]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 10^5
 * word consists only of lowercase English letters.
 * 1 <= forbidden.length <= 10^5
 * 1 <= forbidden[i].length <= 10
 * forbidden[i] consists only of lowercase English letters.
 *
 */
public class LengthOfTheLongestValidSubstring {

    // V0
    // IDEA: SLIDING WINDOW + CHECK ONLY THE SHORT SUFFIXES ENDING AT `right`
    //  growing the window by word[right] adds only suffixes ending at right, and a forbidden
    //  word has length <= 10, so at most 10 suffixes need checking per step.
    /**
     * time = O(N * L^2), L = max forbidden length (<= 10)
     * space = O(total forbidden chars)
     */
    public int longestValidSubstring(String word, List<String> forbidden) {
        Set<String> bad = new HashSet<>(forbidden);
        int maxLen = 0;
        for (String f : forbidden) {
            maxLen = Math.max(maxLen, f.length());
        }

        int best = 0;
        int left = 0;
        for (int right = 0; right < word.length(); right++) {
            // a suffix word[start..right] longer than maxLen can never be forbidden
            int lowestStart = Math.max(left, right - maxLen + 1);
            // walk start from right down: the first hit has the largest start,
            // i.e. it forces the smallest legal left
            for (int start = right; start >= lowestStart; start--) {
                if (bad.contains(word.substring(start, right + 1))) {
                    left = start + 1;
                    break;
                }
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }
}
