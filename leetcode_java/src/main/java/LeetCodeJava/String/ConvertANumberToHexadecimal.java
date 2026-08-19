package LeetCodeJava.String;

// https://leetcode.com/problems/convert-a-number-to-hexadecimal/

/**
 *  405. Convert a Number to Hexadecimal
 *  Easy
 *
 *  Given a 32-bit integer num, return a string representing its hexadecimal
 *  representation. For negative integers, two's complement method is used.
 *
 *  All the letters in the answer string should be lowercase characters, and
 *  there should not be any leading zeros in the answer except for the zero
 *  itself.
 *
 *  Note: You are not allowed to use any built-in library method to directly
 *  solve this problem.
 *
 *  Example 1:
 *    Input: num = 26   Output: "1a"
 *  Example 2:
 *    Input: num = -1   Output: "ffffffff"
 *
 *  Constraints:
 *    -2^31 <= num <= 2^31 - 1
 */
public class ConvertANumberToHexadecimal {

    // V0
    // IDEA: peel 4 bits at a time with & 15 and >>> 4 (logical shift handles negatives)
    /**
     * time = O(1)   // at most 8 nibbles
     * space = O(1)
     */
    public String toHex(int num) {
        if (num == 0) {
            return "0";
        }

        char[] map = {'0', '1', '2', '3', '4', '5', '6', '7',
                      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuilder sb = new StringBuilder();
        // unsigned right shift, so negative numbers become their 2's complement bits
        while (num != 0) {
            sb.append(map[num & 15]);
            num >>>= 4;
        }
        return sb.reverse().toString();
    }
}
