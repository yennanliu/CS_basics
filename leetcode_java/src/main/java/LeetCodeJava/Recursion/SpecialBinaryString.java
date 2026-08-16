package LeetCodeJava.Recursion;

// https://leetcode.com/problems/special-binary-string/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 761. Special Binary String
 * Hard
 *
 * Special binary strings are binary strings with the following two properties:
 *
 *   - The number of 0's is equal to the number of 1's.
 *   - Every prefix of the binary string has at least as many 1's as 0's.
 *
 * You are given a special binary string s.
 *
 * A move consists of choosing two consecutive, non-empty, special substrings of s,
 * and swapping them. Two strings are consecutive if the last character of the first
 * string is exactly one index before the first character of the second string.
 *
 * Return the lexicographically largest resulting string possible after applying the
 * mentioned operations on the string.
 *
 *
 * Example 1:
 *
 * Input: s = "11011000"
 * Output: "11100100"
 * Explanation: The strings "10" [occuring at s[1]] and "1100" [at s[3]] are swapped.
 * This is the lexicographically largest string possible after some number of swaps.
 *
 * Example 2:
 *
 * Input: s = "10"
 * Output: "10"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * s[i] is either '0' or '1'.
 * s is a special binary string.
 *
 */
public class SpecialBinaryString {

    // V0
    // IDEA: RECURSION (treat the string as BALANCED PARENTHESES)
    /**
     *   Read '1' as '(' and '0' as ')': a special string is a VALID, BALANCED sequence.
     *
     *   Split s into its TOP-LEVEL balanced blocks (the counter returns to 0).
     *   Each block is  "1" + <inner special string> + "0"
     *   -> recursively make the INNER part largest,
     *   -> then sort the blocks in DESCENDING order and concatenate.
     *
     *   NOTE !!! sorting blocks is LEGAL because adjacent top-level blocks are exactly
     *            the `two consecutive special substrings` the problem lets us swap,
     *            and any permutation is reachable by adjacent swaps.
     *
     *   time  = O(n^2 log n)
     *   space = O(n^2) (recursive slices)
     */
    public String makeLargestSpecial(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        List<String> blocks = new ArrayList<>();
        int count = 0;
        int start = 0;

        for (int i = 0; i < s.length(); i++) {
            count += s.charAt(i) == '1' ? 1 : -1;

            /** NOTE !!!
             *
             *  count back to 0 -> s[start .. i] is a TOP-LEVEL block:
             *  '1' + inner + '0'
             */
            if (count == 0) {
                String inner = makeLargestSpecial(s.substring(start + 1, i));
                blocks.add("1" + inner + "0");
                start = i + 1;
            }
        }

        // lexicographically largest -> put the BIGGEST blocks first
        Collections.sort(blocks, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (String b : blocks) {
            sb.append(b);
        }
        return sb.toString();
    }

}
