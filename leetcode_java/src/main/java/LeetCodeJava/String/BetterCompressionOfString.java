package LeetCodeJava.String;

// https://leetcode.com/problems/better-compression-of-string/description/

/**
 * 3167. Better Compression of String
 * Medium
 *
 * You are given a string compressed representing a compressed version of a string. The format is a character followed by its frequency. For example, "a3b1a1c2" is a compressed version of the string "aaabacc".
 *
 * We seek a better compression with the following conditions:
 *
 * Each character should appear only once in the compressed version.
 * The characters should be in alphabetical order.
 *
 * Return the better compression of compressed.
 *
 * Note: In the better version of compression, the order of letters may change, which is acceptable.
 *
 *
 * Example 1:
 *
 * Input: compressed = "a3c9b2c1"
 * Output: "a3b2c10"
 * Explanation:
 * Characters "a" and "b" appear only once in the input, but "c" appears twice, once with a size of 9 and once with a size of 1.
 * Hence, in the resulting string, it should have a size of 10.
 *
 * Example 2:
 *
 * Input: compressed = "c2b3a1"
 * Output: "a1b3c2"
 * Explanation:
 * The input does not have any repeated characters, so the output is the same as the input but in alphabetical order.
 *
 * Example 3:
 *
 * Input: compressed = "a2b4c1"
 * Output: "a2b4c1"
 * Explanation:
 * No changes are needed to the input.
 *
 *
 * Constraints:
 *
 * 1 <= compressed.length <= 6 * 10^4
 * compressed consists only of lowercase English letters and digits.
 * compressed is a valid compression, i.e., each character is followed by its frequency.
 * Frequencies are in the range [1, 10^4] and have no leading zeroes.
 *
 */
public class BetterCompressionOfString {

    // V0
    // IDEA: PARSE (LETTER, NUMBER) PAIRS, ACCUMULATE PER LETTER, EMIT IN ALPHABETICAL ORDER
    /**
     * time = O(N)
     * space = O(1)
     */
    public String betterCompression(String compressed) {
        // total frequency per letter; long, since many 10^4 counts of one letter add up
        long[] total = new long[26];
        int n = compressed.length();
        int i = 0;
        while (i < n) {
            // the format is always one letter followed by its multi-digit count
            int letter = compressed.charAt(i) - 'a';
            i++;
            int count = 0;
            while (i < n && Character.isDigit(compressed.charAt(i))) {
                count = count * 10 + (compressed.charAt(i) - '0');
                i++;
            }
            total[letter] += count;
        }

        StringBuilder result = new StringBuilder();
        for (int c = 0; c < 26; c++) {
            if (total[c] > 0) {
                result.append((char) ('a' + c)).append(total[c]);
            }
        }
        return result.toString();
    }
}
