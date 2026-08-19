package LeetCodeJava.Math;

// https://leetcode.com/problems/integer-to-english-words/

import java.util.ArrayList;
import java.util.List;

/**
 *  273. Integer to English Words
 *  Hard
 *
 *  Convert a non-negative integer num to its English words representation.
 *
 *  Example 1:
 *
 *  Input: num = 123
 *  Output: "One Hundred Twenty Three"
 *
 *  Example 2:
 *
 *  Input: num = 12345
 *  Output: "Twelve Thousand Three Hundred Forty Five"
 *
 *  Example 3:
 *
 *  Input: num = 1234567
 *  Output: "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
 *
 *  Constraints:
 *
 *  0 <= num <= 2^31 - 1
 */
public class IntegerToEnglishWords {

    private static final String[] BELOW_20 = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] TENS = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] GROUPS = {"", "Thousand", "Million", "Billion"};

    // V0
    // IDEA: split the number into 3-digit groups, spell each group, append its scale word
    /**
     * time = O(1)   (at most 4 groups, each of constant size; O(log n) in digit count)
     * space = O(1)
     */
    public String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        List<String> parts = new ArrayList<>();
        int groupIdx = 0;
        while (num > 0) {
            int group = num % 1000;
            if (group != 0) {
                List<String> cur = new ArrayList<>();
                helper(group, cur);
                if (groupIdx > 0) {
                    cur.add(GROUPS[groupIdx]);
                }
                // groups are produced from the lowest scale upward -> prepend
                parts.addAll(0, cur);
            }
            num /= 1000;
            groupIdx++;
        }
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(p);
        }
        return sb.toString();
    }

    // spell a value in [1, 999]
    private void helper(int n, List<String> out) {
        if (n == 0) {
            return;
        }
        if (n >= 100) {
            out.add(BELOW_20[n / 100]);
            out.add("Hundred");
            helper(n % 100, out);
        } else if (n >= 20) {
            out.add(TENS[n / 10]);
            helper(n % 10, out);
        } else {
            out.add(BELOW_20[n]);
        }
    }
}
