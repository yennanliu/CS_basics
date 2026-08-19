package LeetCodeJava.Math;

// https://leetcode.com/problems/integer-to-roman/

/**
 *  12. Integer to Roman
 *  Medium
 *
 *  Seven different symbols represent Roman numerals with the following values:
 *    I=1, V=5, X=10, L=50, C=100, D=500, M=1000
 *
 *  Roman numerals are formed by appending the conversions of decimal place values
 *  from highest to lowest. If the value starts with 4 or 9, use the subtractive
 *  form (4 -> IV, 9 -> IX, 40 -> XL, 90 -> XC, 400 -> CD, 900 -> CM).
 *  Otherwise use the additive form, and a symbol may not appear more than 3 times
 *  in a row.
 *
 *  Given an integer, convert it to a Roman numeral.
 *
 *  Example 1:
 *    Input: num = 3749
 *    Output: "MMMDCCXLIX"
 *
 *  Example 2:
 *    Input: num = 58
 *    Output: "LVIII"
 *
 *  Example 3:
 *    Input: num = 1994
 *    Output: "MCMXCIV"
 *
 *  Constraints:
 *    1 <= num <= 3999
 */
public class IntegerToRoman {

    // V0
    // IDEA: greedy over a value -> symbol table that already contains the subtractive forms
    /**
     * time = O(1)   (num <= 3999, table size fixed)
     * space = O(1)
     */
    public String intToRoman(int num) {

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL",
                            "X", "IX", "V", "IV", "I"};

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length && num > 0; i++) {
            while (num >= values[i]) {
                num -= values[i];
                sb.append(symbols[i]);
            }
        }

        return sb.toString();
    }
}
