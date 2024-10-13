package LeetCodeJava.String;

// https://leetcode.com/problems/sentence-similarity/description/
// https://leetcode.ca/2017-12-03-734-Sentence-Similarity/

import java.util.*;

/**
 * 734 - Sentence Similarity
 * Posted on December 3, 2017 · 2 minute read
 * Welcome to Subscribe On Youtube
 * <p>
 * 734. Sentence Similarity
 * Description
 * We can represent a sentence as an array of words, for example, the sentence "I am happy with leetcode" can be represented as arr = ["I","am",happy","with","leetcode"].
 * <p>
 * Given two sentences sentence1 and sentence2 each represented as a string array and given an array of string pairs similarPairs where similarPairs[i] = [xi, yi] indicates that the two words xi and yi are similar.
 * <p>
 * Return true if sentence1 and sentence2 are similar, or false if they are not similar.
 * <p>
 * Two sentences are similar if:
 * <p>
 * They have the same length (i.e., the same number of words)
 * sentence1[i] and sentence2[i] are similar.
 * Notice that a word is always similar to itself, also notice that the similarity relation is not transitive. For example, if the words a and b are similar, and the words b and c are similar, a and c are not necessarily similar.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: sentence1 = ["great","acting","skills"], sentence2 = ["fine","drama","talent"], similarPairs = [["great","fine"],["drama","acting"],["skills","talent"]]
 * Output: true
 * Explanation: The two sentences have the same length and each word i of sentence1 is also similar to the corresponding word in sentence2.
 * Example 2:
 * <p>
 * Input: sentence1 = ["great"], sentence2 = ["great"], similarPairs = []
 * Output: true
 * Explanation: A word is similar to itself.
 * Example 3:
 * <p>
 * Input: sentence1 = ["great"], sentence2 = ["doubleplus","good"], similarPairs = [["great","doubleplus"]]
 * Output: false
 * Explanation: As they don't have the same length, we return false.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= sentence1.length, sentence2.length <= 1000
 * 1 <= sentence1[i].length, sentence2[i].length <= 20
 * sentence1[i] and sentence2[i] consist of English letters.
 * 0 <= similarPairs.length <= 1000
 * similarPairs[i].length == 2
 * 1 <= xi.length, yi.length <= 20
 * xi and yi consist of lower-case and upper-case English letters.
 * All the pairs (xi, yi) are distinct.
 */
public class SentenceSimilarity {

    // V0
    // IDEA : HASHMAP (fixed by gpt)
    public boolean areSentencesSimilar(
            String[] sentence1, String[] sentence2, List<List<String>> similarPairs) {
        // Check if sentences are of different lengths
        if (sentence1.length != sentence2.length) {
            return false;
        }

        // Create a map to store similar pairs
        Map<String, String> map = new HashMap<>();
        for (List<String> pair : similarPairs) {
            map.put(pair.get(0), pair.get(1));
            map.put(pair.get(1), pair.get(0)); // Ensure both directions are stored
        }

        // Check if sentences are similar
        for (int j = 0; j < sentence1.length; j++) {
            String s1 = sentence1[j];
            String s2 = sentence2[j];

            if (!s1.equals(s2) &&
                    !(map.containsKey(s1) && map.get(s1).equals(s2)) &&
                    !(map.containsKey(s2) && map.get(s2).equals(s1))) {
                return false;
            }
        }

        return true;
    }

    // V1
    // https://leetcode.ca/2017-12-03-734-Sentence-Similarity/
    public boolean areSentencesSimilar_1(
            String[] sentence1, String[] sentence2, List<List<String>> similarPairs) {
        if (sentence1.length != sentence2.length) {
            return false;
        }
        Set<String> s = new HashSet<>();
        for (List<String> e : similarPairs) {
            s.add(e.get(0) + "." + e.get(1));
        }
        for (int i = 0; i < sentence1.length; ++i) {
            String a = sentence1[i], b = sentence2[i];
            if (!a.equals(b) && !s.contains(a + "." + b) && !s.contains(b + "." + a)) {
                return false;
            }
        }
        return true;
    }

    // V2
}
