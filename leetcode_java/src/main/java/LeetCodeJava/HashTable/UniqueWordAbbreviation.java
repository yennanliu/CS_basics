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
//    public boolean isUnique(String word) {
//    }


    // V1-1
    // IDEA: HASHMAP (GPT)
    // TODO: validate
    class ValidWordAbbr_1_1 {

        private Map<String, Set<String>> map;

        public ValidWordAbbr_1_1(String[] dictionary) {
            map = new HashMap<>();

            for (String word : dictionary) {
                String abbr = getAbbr(word);

                map.putIfAbsent(abbr, new HashSet<>());
                map.get(abbr).add(word);
            }
        }

        public boolean isUnique(String word) {
            String abbr = getAbbr(word);

            if (!map.containsKey(abbr)) {
                return true;
            }

            Set<String> set = map.get(abbr);

            // unique if:
            // 1) only this word exists in that bucket
            // 2) OR same word repeated
            return set.size() == 1 && set.contains(word);
        }

        private String getAbbr(String str) {
            if (str.length() <= 2) {
                return str;
            }

            return "" + str.charAt(0)
                    + (str.length() - 2)
                    + str.charAt(str.length() - 1);
        }
    }



    // V1-2
    // IDEA: HASHMAP (gemini)
    // TODO: validate
    // Map of abbreviation -> Set of original words that have this abbreviation
    class ValidWordAbbr_1_2 {
        // Map of abbreviation -> Set of original words that have this abbreviation
        private Map<String, Set<String>> dictMap;

        public ValidWordAbbr_1_2(String[] dictionary) {
            dictMap = new HashMap<>();
            for (String word : dictionary) {
                String abbr = getAbbr(word);
                dictMap.putIfAbsent(abbr, new HashSet<>());
                dictMap.get(abbr).add(word);
            }
        }

        public boolean isUnique(String word) {
            String abbr = getAbbr(word);

            // 1. If the abbreviation doesn't exist in the dictionary, it's unique
            if (!dictMap.containsKey(abbr)) {
                return true;
            }

            // 2. If the abbreviation exists, it's ONLY unique if:
            //    - There is only ONE word in that abbreviation set
            //    - AND that one word is the word we are checking
            Set<String> wordsWithAbbr = dictMap.get(abbr);
            return wordsWithAbbr.size() == 1 && wordsWithAbbr.contains(word);
        }

        private String getAbbr(String str) {
            if (str.length() <= 2) {
                return str;
            }
            return str.charAt(0) + String.valueOf(str.length() - 2) + str.charAt(str.length() - 1);
        }
    }


    // V2
    // IDEA: HASHMAP
    // https://leetcode.ca/2016-09-13-288-Unique-Word-Abbreviation/
    class ValidWordAbbr_2 {
        private Map<String, Set<String>> d = new HashMap<>();

        public ValidWordAbbr_2(String[] dictionary) {
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
    }



    // V3





}
