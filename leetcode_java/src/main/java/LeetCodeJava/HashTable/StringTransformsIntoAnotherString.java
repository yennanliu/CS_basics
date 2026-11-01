package LeetCodeJava.HashTable;

// https://leetcode.com/problems/string-transforms-into-another-string/description/

import java.util.*;

/**
 * 1153. String Transforms Into Another String
 * Hard
 *
 * Given two strings str1 and str2 of the same length, determine whether you can transform
 * str1 into str2 by doing zero or more conversions.
 *
 * In one conversion you can convert all occurrences of one character in str1
 * to any other lowercase English character.
 *
 * Return true if and only if you can transform str1 into str2.
 *
 *
 * Example 1:
 *
 * Input: str1 = "aabcc", str2 = "ccdee"
 * Output: true
 * Explanation: Convert 'c' to 'e' then 'b' to 'd' then 'a' to 'c'.
 * Note that the order of conversions matter.
 *
 * Example 2:
 *
 * Input: str1 = "leetcode", str2 = "codeleet"
 * Output: false
 * Explanation: There is no way to transform str1 to str2.
 *
 *
 * Constraints:
 *
 * 1 <= str1.length == str2.length <= 10^4
 * str1 and str2 contain only lowercase English letters.
 *
 */
public class StringTransformsIntoAnotherString {

    // V0
    // IDEA: HASH TABLE (the mapping str1[i] -> str2[i] must be a function) + SPARE LETTER ARGUMENT
    //       a cycle (a->b, b->a) needs a free letter to park on; if str2 uses all 26 letters,
    //       there is none, so only str1 == str2 works.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canConvert(String str1, String str2) {
        if (str1.equals(str2)) {
            return true;
        }

        // no letter is left unused in str2 -> no way to break a cycle
        Set<Character> targetLetters = new HashSet<>();
        for (char c : str2.toCharArray()) {
            targetLetters.add(c);
        }
        if (targetLetters.size() == 26) {
            return false;
        }

        // one source letter cannot be converted into two different targets
        Map<Character, Character> mapping = new HashMap<>();
        for (int i = 0; i < str1.length(); i++) {
            char from = str1.charAt(i);
            char to = str2.charAt(i);
            if (mapping.containsKey(from) && mapping.get(from) != to) {
                return false;
            }
            mapping.put(from, to);
        }
        return true;
    }
}
