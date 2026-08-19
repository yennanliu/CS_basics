package LeetCodeJava.String;

// https://leetcode.com/problems/decoded-string-at-index/

/**
 *  880. Decoded String at Index
 *  Medium
 *
 *  You are given an encoded string s. To decode the string to a tape, the
 *  encoded string is read one character at a time:
 *   - If the character read is a letter, that letter is written onto the tape.
 *   - If the character read is a digit d, the entire current tape is
 *     repeatedly written d - 1 more times in total.
 *
 *  Given an integer k, return the k-th letter (1-indexed) in the decoded
 *  string.
 *
 *  Example 1:
 *  Input: s = "leet2code3", k = 10
 *  Output: "o"
 *  Explanation: The decoded string is
 *  "leetleetcodeleetleetcodeleetleetcode", the 10th letter is "o".
 *
 *  Example 2:
 *  Input: s = "ha22", k = 5
 *  Output: "h"
 *
 *  Constraints:
 *   - 2 <= s.length <= 100
 *   - s consists of lowercase English letters and digits 2 through 9.
 *   - The decoded string is guaranteed to have less than 2^63 letters.
 */
public class DecodedStringAtIndex {

    // V0
    // IDEA: compute the total decoded size, then walk backwards shrinking the
    //       tape: k % size lands on the equivalent position in the prefix.
    /**
     * time = O(n)
     * space = O(1)
     */
    public String decodeAtIndex(String s, int k) {
        long size = 0;
        int n = s.length();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                size *= (c - '0');
            } else {
                size++;
            }
        }

        long kk = k;
        for (int i = n - 1; i >= 0; i--) {
            char c = s.charAt(i);
            kk %= size;
            if (kk == 0 && Character.isLetter(c)) {
                return String.valueOf(c);
            }
            if (Character.isDigit(c)) {
                size /= (c - '0');
            } else {
                size--;
            }
        }

        return "";
    }
}
