package LeetCodeJava.String;

// https://leetcode.com/problems/maximum-repeating-substring/description/

/**
 * 1668. Maximum Repeating Substring
 * Easy
 *
 * For a string sequence, a string word is k-repeating if word concatenated k times is a substring of
 * sequence. The word's maximum k-repeating value is the highest value k where word is k-repeating in
 * sequence. If word is not a substring of sequence, word's maximum k-repeating value is 0.
 *
 * Given strings sequence and word, return the maximum k-repeating value of word in sequence.
 *
 *
 * Example 1:
 *
 * Input: sequence = "ababc", word = "ab"
 * Output: 2
 * Explanation: "abab" is a substring in "ababc".
 *
 * Example 2:
 *
 * Input: sequence = "ababc", word = "ba"
 * Output: 1
 * Explanation: "ba" is a substring in "ababc". "baba" is not a substring in "ababc".
 *
 * Example 3:
 *
 * Input: sequence = "ababc", word = "ac"
 * Output: 0
 * Explanation: "ac" is not a substring in "ababc".
 *
 *
 * Constraints:
 *
 * 1 <= sequence.length <= 100
 * 1 <= word.length <= 100
 * sequence and word contains only lowercase English letters.
 *
 */
public class MaximumRepeatingSubstring {

    // V0
    // IDEA: BRUTE FORCE, SEARCHING k DOWNWARD
    //       k is monotone: if word * k is a substring, so is word * (k - 1).
    //       Start from the largest k that fits and return the first that is
    //       contained in sequence; k = 0 (the empty string) always is.
    /**
     * time = O(N^2)   (N = sequence length <= 100)
     * space = O(N)
     */
    public int maxRepeating(String sequence, String word) {
        int k = sequence.length() / word.length();
        while (k > 0) {
            // build word repeated k times
            StringBuilder repeated = new StringBuilder();
            for (int i = 0; i < k; i++) {
                repeated.append(word);
            }
            if (sequence.contains(repeated.toString())) {
                return k;
            }
            k--;
        }
        return 0;
    }
}
