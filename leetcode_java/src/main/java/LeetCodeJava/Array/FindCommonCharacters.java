package LeetCodeJava.Array;

// https://leetcode.com/problems/find-common-characters/description/

import java.util.*;

/**
 * 1002. Find Common Characters
 * Easy
 *
 * Given a string array words, return an array of all characters that show up in all strings within the words (including duplicates). You may return the answer in any order.
 *
 * Example 1:
 *
 * Input: words = ["bella","label","roller"]
 * Output: ["e","l","l"]
 *
 * Example 2:
 *
 * Input: words = ["cool","lock","cook"]
 * Output: ["c","o"]
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 100
 * words[i] consists of lowercase English letters.
 *
 */
public class FindCommonCharacters {

    // V0
    // IDEA: LETTER COUNT PER WORD + KEEP THE MIN COUNT OF EACH LETTER OVER ALL WORDS
    /**
     * time = O(n * L)
     * space = O(1)
     */
    public List<String> commonChars(String[] words) {
        List<String> result = new ArrayList<>();
        if (words == null || words.length == 0) {
            return result;
        }

        // minCount[c] = how many copies of letter c are present in EVERY word seen so far
        int[] minCount = new int[26];
        Arrays.fill(minCount, Integer.MAX_VALUE);

        for (String word : words) {
            int[] count = new int[26];
            for (char ch : word.toCharArray()) {
                count[ch - 'a']++;
            }
            // a letter can be shared only as many times as the poorest word holds it
            for (int i = 0; i < 26; i++) {
                minCount[i] = Math.min(minCount[i], count[i]);
            }
        }

        // emit each letter once per shared copy (duplicates included)
        for (int i = 0; i < 26; i++) {
            for (int copy = 0; copy < minCount[i]; copy++) {
                result.add(String.valueOf((char) ('a' + i)));
            }
        }
        return result;
    }
}
