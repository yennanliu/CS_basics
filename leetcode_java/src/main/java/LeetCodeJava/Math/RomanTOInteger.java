package LeetCodeJava.Math;

// https://leetcode.com/problems/roman-to-integer/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 13. Roman to Integer
 * Solved
 * Easy
 * Topics
 * Companies
 * Hint
 * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
 *
 * Symbol       Value
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * For example, 2 is written as II in Roman numeral, just two ones added together. 12 is written as XII, which is simply X + II. The number 27 is written as XXVII, which is XX + V + II.
 *
 * Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead, the number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to the number nine, which is written as IX. There are six instances where subtraction is used:
 *
 * I can be placed before V (5) and X (10) to make 4 and 9.
 * X can be placed before L (50) and C (100) to make 40 and 90.
 * C can be placed before D (500) and M (1000) to make 400 and 900.
 * Given a roman numeral, convert it to an integer.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "III"
 * Output: 3
 * Explanation: III = 3.
 * Example 2:
 *
 * Input: s = "LVIII"
 * Output: 58
 * Explanation: L = 50, V= 5, III = 3.
 * Example 3:
 *
 * Input: s = "MCMXCIV"
 * Output: 1994
 * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 15
 * s contains only the characters ('I', 'V', 'X', 'L', 'C', 'D', 'M').
 * It is guaranteed that s is a valid roman numeral in the range [1, 3999].
 *
 *
 */
public class RomanTOInteger {


    // V0

    // IDEA: MAP + STR OP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int romanToInt(String s) {

        // edge
        if (s == null || s.length() == 0) {
            return 0;
        }

        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int res = 0;
        int i = s.length() - 1;

        /**
         *  NOTE !!!
         *
         *   instead of `for loop` and check `whether should sum up i and i - 1 or i`
         *   -> we use `while loop`
         *    -> compare `i-1` and `i`
         *      -> if prev (i-1)  < curr (i)
         *         -> we add (curr - prev)
         *      -> else
         *         -> we add (curr) ONLY
         *
         */
        while (i > 0) {
            int curr = map.get(s.charAt(i));
            int prev = map.get(s.charAt(i - 1));

            if (prev < curr) {
                res += (curr - prev);
                i -= 2;
            } else {
                res += curr;
                i -= 1;
            }
        }

        /**
         *  NOTE !!!
         *
         *   below `odd length` handling
         */
        // If one char left unprocessed (e.g. odd length)
        if (i == 0) {
            res += map.get(s.charAt(0));
        }

        return res;
    }

    // V0-1

    // IDEA: MAP + STR OP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int romanToInt_0_1(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int total = 0;
        int prev = 0;

        /**
         * NOTE !!!
         *
         * loop reversely (from  idx = s.len() - 1)
         */
        for (int i = s.length() - 1; i >= 0; i--) {
            int curr = map.get(s.charAt(i));
            if (curr < prev) {
                total -= curr;
            } else {
                total += curr;
            }
            /**
             * NOTE !!!
             *
             *  set `prev` as curr
             */
            prev = curr;
        }

        return total;
    }

    // V1
    // https://www.youtube.com/watch?v=3jdxYj3DD98&pp=0gcJCdgAo7VqN5tD

    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0013-roman-to-integer.java
    /**
     * time = O(N)
     * space = O(N)
     */
    public int romanToInt_1(String s) {
        HashMap<Character, Integer> map = new HashMap();
        map.put('I', 1);
        map.put('V' ,5);
        map.put('X' ,10);
        map.put('L' ,50);
        map.put('C' ,100);
        map.put('D' ,500);
        map.put('M' ,1000);

        int result = 0;
        for(int i=0; i < s.length(); i++){
            if (i > 0 && map.get(s.charAt(i)) > map.get(s.charAt(i-1)))  {
                result += map.get(s.charAt(i)) - 2*map.get(s.charAt(i-1));
            } else {
                result += map.get(s.charAt(i));
            }
        }
        return result;
    }

    // V2

}
