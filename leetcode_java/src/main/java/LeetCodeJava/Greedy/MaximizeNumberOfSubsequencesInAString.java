package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximize-number-of-subsequences-in-a-string/description/

/**
 * 2207. Maximize Number of Subsequences in a String
 * Medium
 *
 * You are given a 0-indexed string text and another 0-indexed string pattern of length 2, both of which
 * consist of only lowercase English letters.
 *
 * You can add either pattern[0] or pattern[1] anywhere in text exactly once. Note that the character can be
 * added even at the beginning or at the end of text.
 *
 * Return the maximum number of times pattern can occur as a subsequence of the modified text.
 *
 * A subsequence is a string that can be derived from another string by deleting some or no characters
 * without changing the order of the remaining characters.
 *
 *
 * Example 1:
 *
 * Input: text = "abdcdbc", pattern = "ac"
 * Output: 4
 *
 * Example 2:
 *
 * Input: text = "aabb", pattern = "ab"
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 10^5
 * pattern.length == 2
 * text and pattern consist only of lowercase English letters.
 *
 */
public class MaximizeNumberOfSubsequencesInAString {

    // V0
    // IDEA: COUNT PAIRS IN ONE SWEEP, THEN ADD THE BETTER INSERTION (FIRST CHAR AT THE FRONT OR SECOND AT THE END)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maximumSubsequenceCount(String text, String pattern) {
        char first = pattern.charAt(0);
        char second = pattern.charAt(1);

        // the count can reach ~ (10^5)^2 / 4, so it needs a long
        long pairs = 0;
        long firstCount = 0;
        long secondCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // count the pair BEFORE counting c as a `first`, so when first == second
            // a character never pairs with itself
            if (c == second) {
                pairs += firstCount;
                secondCount++;
            }
            if (c == first) {
                firstCount++;
            }
        }

        // `first` at the very front pairs with every `second`; `second` at the very end pairs with every `first`
        return pairs + Math.max(firstCount, secondCount);
    }
}
