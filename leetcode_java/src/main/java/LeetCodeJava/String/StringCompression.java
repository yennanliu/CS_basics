package LeetCodeJava.String;

// https://leetcode.com/problems/string-compression/

/**
 *  443. String Compression
 *  Medium
 *
 *  Given an array of characters chars, compress it using the following
 *  algorithm:
 *
 *  Begin with an empty string s. For each group of consecutive repeating
 *  characters in chars:
 *    - If the group's length is 1, append the character to s.
 *    - Otherwise, append the character followed by the group's length.
 *
 *  The compressed string s should not be returned separately, but instead
 *  be stored in the input character array chars. Note that group lengths
 *  that are 10 or longer will be split into multiple characters in chars.
 *
 *  After you are done modifying the input array, return the new length of
 *  the array. You must write an algorithm that uses only constant extra space.
 *
 *  Example 1:
 *    Input:  chars = ["a","a","b","b","c","c","c"]
 *    Output: 6, chars = ["a","2","b","2","c","3"]
 *  Example 2:
 *    Input:  chars = ["a"]                Output: 1, chars = ["a"]
 *  Example 3:
 *    Input:  chars = ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
 *    Output: 4, chars = ["a","b","1","2"]
 *
 *  Constraints:
 *    1 <= chars.length <= 2000
 *    chars[i] is a lowercase/uppercase English letter, digit, or symbol.
 */
public class StringCompression {

    // V0
    // IDEA: read pointer scans a run, write pointer emits char + (digits of run length)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int compress(char[] chars) {
        if (chars == null || chars.length == 0) {
            return 0;
        }

        int write = 0;
        int read = 0;

        while (read < chars.length) {
            char cur = chars[read];
            int count = 0;
            while (read < chars.length && chars[read] == cur) {
                read++;
                count++;
            }

            chars[write++] = cur;

            if (count > 1) {
                // write the digits of count, in order
                String cnt = String.valueOf(count);
                for (int k = 0; k < cnt.length(); k++) {
                    chars[write++] = cnt.charAt(k);
                }
            }
        }

        return write;
    }
}
