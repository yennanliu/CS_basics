package LeetCodeJava.Math;

// https://leetcode.com/problems/excel-sheet-column-title/

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 168. Excel Sheet Column Title
 * Solved
 * Easy
 * Topics
 * Companies
 * Given an integer columnNumber, return its corresponding column title as it appears in an Excel sheet.
 *
 * For example:
 *
 * A -> 1
 * B -> 2
 * C -> 3
 * ...
 * Z -> 26
 * AA -> 27
 * AB -> 28
 * ...
 *
 *
 * Example 1:
 *
 * Input: columnNumber = 1
 * Output: "A"
 * Example 2:
 *
 * Input: columnNumber = 28
 * Output: "AB"
 * Example 3:
 *
 * Input: columnNumber = 701
 * Output: "ZY"
 *
 *
 * Constraints:
 *
 * 1 <= columnNumber <= 231 - 1
 *
 *
 */
public class ExcelSheetColumnTitle {

    // V0
//    public String convertToTitle(int columnNumber) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=X_vJDpCCuoA

    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0168-excel-sheet-column-title.java
    /**
     * time = O(N)
     * space = O(N)
     */
    public String convertToTitle_1(int columnNumber) {
        Map<Integer, Character> map = new HashMap<>();
        char c = 'A';

        for (int i = 1; i <= 26; i++) {
            map.put(i, c);
            c++;
        }

        StringBuilder res = new StringBuilder();

        while (columnNumber > 0) {
            /**
             *  NOTE !!!
             *
             * The key to understanding columnNumber - 1 in:
             *
             * int r = (columnNumber - 1) % 26;
             *
             *
             * is realizing how Excel column naming works
             * versus how a zero-based numerical system
             * (like in programming) works.
             *
             */
            int r = (columnNumber - 1) % 26;
            res.insert(0, map.get(r + 1));
            columnNumber = (columnNumber - 1) / 26;
        }

        return res.toString();
    }


    // V2

    // https://leetcode.com/problems/excel-sheet-column-title/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public String convertToTitle_2(int columnNumber) {
        StringBuilder ans = new StringBuilder();

        while (columnNumber > 0) {
            columnNumber--;
            // Get the last character and append it at the end of the string.
            ans.append((char) (((columnNumber) % 26) + 'A'));
            columnNumber = (columnNumber) / 26;
        }

        // Reverse it, as we appended characters in reverse order.
        return ans.reverse().toString();
    }


}
