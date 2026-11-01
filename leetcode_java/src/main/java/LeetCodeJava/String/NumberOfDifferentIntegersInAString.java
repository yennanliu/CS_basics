package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-different-integers-in-a-string/description/

import java.util.*;

/**
 * 1805. Number of Different Integers in a String
 * Easy
 *
 * You are given a string word that consists of digits and lowercase English letters.
 *
 * You will replace every non-digit character with a space. For example, "a123bc34d8ef34" will become " 123  34 8  34". Notice that you are left with some integers that are separated by at least one space: "123", "34", "8", and "34".
 *
 * Return the number of different integers after performing the replacement operations on word.
 *
 * Two integers are considered different if their decimal representations without any leading zeros are different.
 *
 *
 * Example 1:
 *
 * Input: word = "a123bc34d8ef34"
 * Output: 3
 * Explanation: The three different integers are "123", "34", and "8". Notice that "34" is only counted once.
 *
 * Example 2:
 *
 * Input: word = "leet1234code234"
 * Output: 2
 *
 * Example 3:
 *
 * Input: word = "a1b01c001"
 * Output: 1
 * Explanation: The three integers "1", "01", and "001" all represent the same integer because
 * the leading zeros are ignored when comparing their decimal values.
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 1000
 * word consists of digits and lowercase English letters.
 *
 */
public class NumberOfDifferentIntegersInAString {

    // V0
    // IDEA: SCAN EACH DIGIT RUN, STRIP LEADING ZEROS, STORE AS A STRING IN A SET
    //       numbers can be 1000 digits long, so compare them as zero-stripped
    //       strings, never as int / long. "000" strips to "" -- a unique form for 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numDifferentIntegers(String word) {
        Set<String> distinct = new HashSet<>();
        int n = word.length();
        int i = 0;
        while (i < n) {
            if (!Character.isDigit(word.charAt(i))) {
                i++;
                continue;
            }
            // skip leading zeros of this run
            while (i < n && word.charAt(i) == '0') {
                i++;
            }
            // the rest of the run is the canonical form
            int end = i;
            while (end < n && Character.isDigit(word.charAt(end))) {
                end++;
            }
            distinct.add(word.substring(i, end));
            i = end;
        }
        return distinct.size();
    }
}
