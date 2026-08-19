package LeetCodeJava.HashTable;

// https://leetcode.com/problems/vowel-spellchecker/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  966. Vowel Spellchecker
 *  Medium
 *
 *  Given a wordlist, we want to implement a spellchecker that converts a query
 *  word into a correct word. For a given query word, the spell checker handles
 *  two categories of spelling mistakes:
 *
 *  1) Capitalization: if the query matches a word in the wordlist case-insensitively,
 *     return the FIRST such match in the wordlist.
 *  2) Vowel Errors: if replacing all the vowels ('a','e','i','o','u') of the query
 *     with any vowel makes it match a word in the wordlist (case-insensitively),
 *     return the FIRST such match in the wordlist.
 *
 *  In addition, an exact match (case-sensitive) is always returned as is, and
 *  precedence is: exact match > capitalization > vowel error > "" (no match).
 *
 *  Example 1:
 *  Input: wordlist = ["KiTe","kite","hare","Hare"],
 *         queries = ["kite","Kite","KiTe","Hare","HARE","Hear","hear","keti","keet","keto"]
 *  Output: ["kite","KiTe","KiTe","Hare","hare","","","KiTe","","KiTe"]
 *
 *  Example 2:
 *  Input: wordlist = ["yellow"], queries = ["YellOw"]
 *  Output: ["yellow"]
 *
 *  Constraints:
 *  1 <= wordlist.length, queries.length <= 5000
 *  1 <= wordlist[i].length, queries[i].length <= 7
 *  wordlist[i] and queries[i] consist only of English letters.
 */
public class VowelSpellchecker {

    // V0
    // IDEA: 3 LOOKUPS - exact set, lowercase map, de-vowelled map.
    //       Both maps keep the FIRST word of the wordlist for each key.
    /**
     * time = O(W + Q), W/Q = total chars in wordlist / queries (word len <= 7)
     * space = O(W)
     */
    public String[] spellchecker(String[] wordlist, String[] queries) {

        // edge
        if (wordlist == null || queries == null) {
            return new String[0];
        }

        Set<String> exact = new HashSet<>();
        Map<String, String> capMap = new HashMap<>();   // lowercase   -> first word
        Map<String, String> vowMap = new HashMap<>();   // de-vowelled -> first word

        for (String w : wordlist) {
            exact.add(w);
            String low = w.toLowerCase();
            if (!capMap.containsKey(low)) {
                capMap.put(low, w);
            }
            String dev = devowel(low);
            if (!vowMap.containsKey(dev)) {
                vowMap.put(dev, w);
            }
        }

        String[] res = new String[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String q = queries[i];
            if (exact.contains(q)) {
                res[i] = q;
                continue;
            }
            String low = q.toLowerCase();
            if (capMap.containsKey(low)) {
                res[i] = capMap.get(low);
                continue;
            }
            String dev = devowel(low);
            if (vowMap.containsKey(dev)) {
                res[i] = vowMap.get(dev);
                continue;
            }
            res[i] = "";
        }

        return res;
    }

    /** replace every vowel with '#' (input is expected to be lowercase) */
    private String devowel(String lower) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                sb.append('#');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
