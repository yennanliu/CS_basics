package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-words-that-can-be-formed-by-characters/description/

/**
 * 1160. Find Words That Can Be Formed by Characters
 * Easy
 *
 * You are given an array of strings words and a string chars.
 *
 * A string is good if it can be formed by characters from chars
 * (each character can only be used once for each word in words).
 *
 * Return the sum of lengths of all good strings in words.
 *
 *
 * Example 1:
 *
 * Input: words = ["cat","bt","hat","tree"], chars = "atach"
 * Output: 6
 * Explanation: The strings that can be formed are "cat" and "hat" so the answer is 3 + 3 = 6.
 *
 * Example 2:
 *
 * Input: words = ["hello","world","leetcode"], chars = "welldonehoneyr"
 * Output: 10
 * Explanation: The strings that can be formed are "hello" and "world"
 * so the answer is 5 + 5 = 10.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length, chars.length <= 100
 * words[i] and chars consist of lowercase English letters.
 *
 */
public class FindWordsThatCanBeFormedByCharacters {

    // V0
    // IDEA: HASH TABLE (letter counts of chars = a budget; each word must fit inside it)
    /**
     * time = O(L), L = total length of chars and all words
     * space = O(1)
     */
    public int countCharacters(String[] words, String chars) {
        int[] budget = new int[26];
        for (char c : chars.toCharArray()) {
            budget[c - 'a']++;
        }

        int totalLength = 0;
        for (String word : words) {
            // the budget is not consumed across words: count each word on its own
            int[] need = new int[26];
            for (char c : word.toCharArray()) {
                need[c - 'a']++;
            }

            boolean canForm = true;
            for (int i = 0; i < 26; i++) {
                if (need[i] > budget[i]) {
                    canForm = false;
                    break;
                }
            }
            if (canForm) {
                totalLength += word.length();
            }
        }
        return totalLength;
    }
}
