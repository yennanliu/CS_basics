package LeetCodeJava.String;

// https://leetcode.com/problems/process-string-with-special-operations-ii/description/

/**
 * 3614. Process String with Special Operations II
 * Hard
 *
 * You are given a string s consisting of lowercase English letters and the special
 * characters: '*', '#', and '%'.
 *
 * You are also given an integer k.
 *
 * Build a new string result by processing s according to the following rules from
 * left to right:
 *
 * If the letter is a lowercase English letter append it to result.
 * A '*' removes the last character from result, if it exists.
 * A '#' duplicates the current result and appends it to itself.
 * A '%' reverses the current result.
 *
 * Return the kth character of the final string result. If k is out of the bounds
 * of result, return '.'.
 *
 *
 * Example 1:
 *
 * Input: s = "a#b%*", k = 1
 * Output: "a"
 *
 * Example 2:
 *
 * Input: s = "cd%#*#", k = 3
 * Output: "d"
 *
 * Example 3:
 *
 * Input: s = "z*#", k = 0
 * Output: "."
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only lowercase English letters and special characters '*', '#', and '%'.
 * 0 <= k <= 10^15
 * The length of result after processing s will not exceed 10^15.
 *
 */
public class ProcessStringWithSpecialOperationsII {

    // V0
    // IDEA: LENGTH SWEEP FORWARD, THEN WALK THE INDEX BACKWARDS
    //   the string can reach 10^15 characters, but its LENGTH after each operation is
    //   cheap to track. then undo the operations in reverse on the single index k,
    //   keeping the invariant "k is a valid index into the string of length len":
    //     '#'    : len halves; an index in the second half maps to k - len/2
    //     '%'    : reversal is its own inverse: k -> len - 1 - k
    //     '*'    : the previous string was one longer; k is untouched
    //     letter : the previous string was one shorter; if k is the appended slot,
    //              this letter is the answer
    /**
     * time = O(N)
     * space = O(1)
     */
    public char processStr(String s, long k) {
        // forward pass: final length only
        long length = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '*') {
                if (length > 0) {
                    length--;
                }
            } else if (ch == '#') {
                length *= 2;
            } else if (ch != '%') {
                length++;
            }
        }

        if (k >= length) {
            return '.';
        }

        // backward pass: undo each operation on the index
        for (int i = s.length() - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            if (ch == '*') {
                length++;
            } else if (ch == '#') {
                length /= 2;
                // both halves are identical copies of the previous string
                if (k >= length) {
                    k -= length;
                }
            } else if (ch == '%') {
                k = length - 1 - k;
            } else {
                length--;
                // k points at the character this step appended
                if (k == length) {
                    return ch;
                }
            }
        }
        return '.';
    }
}
