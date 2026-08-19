package LeetCodeJava.String;

// https://leetcode.com/problems/uncommon-words-from-two-sentences/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  884. Uncommon Words from Two Sentences
 *  Easy
 *
 *  A sentence is a string of single-space separated words where each word
 *  consists only of lowercase letters.
 *
 *  A word is uncommon if it appears exactly once in one of the sentences, and
 *  does not appear in the other sentence.
 *
 *  Given two sentences s1 and s2, return a list of all the uncommon words.
 *  You may return the answer in any order.
 *
 *  Example 1:
 *  Input: s1 = "this apple is sweet", s2 = "this apple is sour"
 *  Output: ["sweet","sour"]
 *
 *  Example 2:
 *  Input: s1 = "apple apple", s2 = "banana"
 *  Output: ["banana"]
 *
 *  Constraints:
 *   - 1 <= s1.length, s2.length <= 200
 */
public class UncommonWordsFromTwoSentences {

    // V0
    // IDEA: HASHMAP COUNT - merge both sentences and keep words with count == 1.
    /**
     * time = O(m + n)
     * space = O(m + n)
     */
    public String[] uncommonFromSentences(String s1, String s2) {
        Map<String, Integer> cnt = new HashMap<>();
        for (String w : s1.split(" ")) {
            cnt.put(w, cnt.getOrDefault(w, 0) + 1);
        }
        for (String w : s2.split(" ")) {
            cnt.put(w, cnt.getOrDefault(w, 0) + 1);
        }

        List<String> res = new ArrayList<>();
        for (Map.Entry<String, Integer> e : cnt.entrySet()) {
            if (e.getValue() == 1) {
                res.add(e.getKey());
            }
        }
        return res.toArray(new String[0]);
    }
}
