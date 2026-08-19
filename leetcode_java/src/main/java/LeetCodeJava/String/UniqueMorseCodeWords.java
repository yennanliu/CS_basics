package LeetCodeJava.String;

// https://leetcode.com/problems/unique-morse-code-words/

import java.util.HashSet;
import java.util.Set;

/**
 *  804. Unique Morse Code Words
 *  Easy
 *
 *  International Morse Code defines a standard encoding where each letter is
 *  mapped to a series of dots and dashes, e.g. 'a' -> ".-", 'b' -> "-...".
 *  Given an array of strings words where each word can be written as a
 *  concatenation of the Morse code of each letter, return the number of
 *  different transformations among all words.
 *
 *  Example 1:
 *    Input:  words = ["gin","zen","gig","msg"]
 *    Output: 2
 *    ("gin" -> "--...-.", "zen" -> "--...-.", "gig" -> "--...--.", "msg" -> "--...--.")
 *
 *  Example 2:
 *    Input:  words = ["a"]
 *    Output: 1
 *
 *  Constraints:
 *    1 <= words.length <= 100
 *    1 <= words[i].length <= 12
 *    words[i] consists of lowercase English letters.
 */
public class UniqueMorseCodeWords {

    private static final String[] MORSE = {
            ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."
    };

    // V0
    // IDEA: transform each word to its morse concatenation, count distinct via HashSet
    /**
     * time = O(total number of chars)
     * space = O(total number of chars)
     */
    public int uniqueMorseRepresentations(String[] words) {
        if (words == null || words.length == 0) {
            return 0;
        }
        Set<String> seen = new HashSet<>();
        for (String w : words) {
            StringBuilder sb = new StringBuilder();
            for (char c : w.toCharArray()) {
                sb.append(MORSE[c - 'a']);
            }
            seen.add(sb.toString());
        }
        return seen.size();
    }
}
