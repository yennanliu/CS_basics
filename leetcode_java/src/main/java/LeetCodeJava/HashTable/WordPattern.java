package LeetCodeJava.HashTable;

// https://leetcode.com/problems/word-pattern/

import java.util.*;

/**
 *  290. Word Pattern
 *  Easy
 *
 *  Given a pattern and a string s, find if s follows the same pattern.
 *  Here follow means a full match, such that there is a bijection between a
 *  letter in pattern and a non-empty word in s.
 *
 *  Example 1:
 *  Input: pattern = "abba", s = "dog cat cat dog"
 *  Output: true
 *
 *  Example 2:
 *  Input: pattern = "abba", s = "dog cat cat fish"
 *  Output: false
 *
 *  Example 3:
 *  Input: pattern = "aaaa", s = "dog cat cat dog"
 *  Output: false
 *
 *  Constraints:
 *   - 1 <= pattern.length <= 300
 *   - pattern contains only lower-case English letters.
 *   - 1 <= s.length <= 3000
 *   - s contains only lowercase English letters and spaces ' '.
 *   - s does not contain any leading or trailing spaces.
 *   - All the words in s are separated by a single space.
 */
public class WordPattern {

    // V0
    // IDEA: 2 HASHMAP (char -> word, word -> char), so the mapping is a bijection
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean wordPattern(String pattern, String s) {

        if (pattern == null || s == null) {
            return false;
        }

        String[] words = s.split(" ");

        if (pattern.length() != words.length) {
            return false;
        }

        Map<Character, String> c2w = new HashMap<>();
        Map<String, Character> w2c = new HashMap<>();

        for (int i = 0; i < words.length; i++) {

            char c = pattern.charAt(i);
            String w = words[i];

            /**
             *  NOTE !!!
             *
             *  we need BOTH direction checks,
             *  e.g. pattern = "ab", s = "dog dog" -> false
             */
            if (c2w.containsKey(c) && !c2w.get(c).equals(w)) {
                return false;
            }
            if (w2c.containsKey(w) && w2c.get(w) != c) {
                return false;
            }

            c2w.put(c, w);
            w2c.put(w, c);
        }

        return true;
    }
}
