package LeetCodeJava.HashTable;

// https://leetcode.ca/all/288.html
// https://leetcode.com/problems/unique-word-abbreviation/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 288. Unique Word Abbreviation
 * An abbreviation of a word follows the form <first letter><number><last letter>. Below are some examples of word abbreviations:
 *
 * a) it                      --> it    (no abbreviation)
 *
 *      1
 *      ↓
 * b) d|o|g                   --> d1g
 *
 *               1    1  1
 *      1---5----0----5--8
 *      ↓   ↓    ↓    ↓  ↓
 * c) i|nternationalizatio|n  --> i18n
 *
 *               1
 *      1---5----0
 *      ↓   ↓    ↓
 * d) l|ocalizatio|n          --> l10n
 * Assume you have a dictionary and given a word, find whether its abbreviation is unique in the dictionary. A word's abbreviation is unique if no other word from the dictionary has the same abbreviation.
 *
 * Example:
 *
 * Given dictionary = [ "deer", "door", "cake", "card" ]
 *
 * isUnique("dear") -> false
 * isUnique("cart") -> true
 * isUnique("cane") -> false
 * isUnique("make") -> true
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Google
 * Problem Solution
 * 288-Unique-Word-Abbreviation
 * All Problems:
 *
 */
public class UniqueWordAbbreviation {

    // V0
//    public void ValidWordAbbr(String[] dictionary) {
//    }


    // V1


    // V2
    // IDEA: HASHMAP
    // https://leetcode.ca/2016-09-13-288-Unique-Word-Abbreviation/
    private Map<String, Set<String>> d = new HashMap<>();

    public void ValidWordAbbr_2(String[] dictionary) {
        for (String s : dictionary) {
            d.computeIfAbsent(abbr(s), k -> new HashSet<>()).add(s);
        }
    }

    public boolean isUnique(String word) {
        Set<String> ws = d.get(abbr(word));
        return ws == null || (ws.size() == 1 && ws.contains(word));
    }

    private String abbr(String s) {
        int n = s.length();
        return n < 3 ? s : s.substring(0, 1) + (n - 2) + s.substring(n - 1);
    }




    // V3





}
