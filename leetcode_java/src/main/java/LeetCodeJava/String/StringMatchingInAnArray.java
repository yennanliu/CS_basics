package LeetCodeJava.String;

// https://leetcode.com/problems/string-matching-in-an-array/description/

import java.util.*;

/**
 * 1408. String Matching in an Array
 * Easy
 *
 * Given an array of string words, return all strings in words that are a substring
 * of another word. You can return the answer in any order.
 *
 *
 * Example 1:
 *
 * Input: words = ["mass","as","hero","superhero"]
 * Output: ["as","hero"]
 * Explanation: "as" is substring of "mass" and "hero" is substring of "superhero".
 * ["hero","as"] is also a valid answer.
 *
 * Example 2:
 *
 * Input: words = ["leetcode","et","code"]
 * Output: ["et","code"]
 * Explanation: "et", "code" are substring of "leetcode".
 *
 * Example 3:
 *
 * Input: words = ["blue","green","bu"]
 * Output: []
 * Explanation: No string of words is substring of another string.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 30
 * words[i] contains only lowercase English letters.
 * All the strings of words are unique.
 *
 */
public class StringMatchingInAnArray {

    // V0
    // IDEA: BRUTE FORCE PAIRS (n <= 100, length <= 30, so this is the intended solution)
    /**
     * time = O(N^2 * L^2), L = max word length
     * space = O(N)
     */
    public List<String> stringMatching(String[] words) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (i != j && words[j].contains(words[i])) {
                    result.add(words[i]);
                    break;   // count each word once
                }
            }
        }
        return result;
    }

    // V1
    // IDEA: SORT BY LENGTH, ONLY COMPARE AGAINST LONGER WORDS
    //       words are unique, so a word can only be inside a strictly longer one
    //       (two distinct words of equal length never contain each other).
    /**
     * time = O(N log N + N^2 * L^2)
     * space = O(N)
     */
    public List<String> stringMatching_1(String[] words) {
        String[] byLength = words.clone();
        Arrays.sort(byLength, (a, b) -> Integer.compare(a.length(), b.length()));

        List<String> result = new ArrayList<>();
        for (int i = 0; i < byLength.length; i++) {
            for (int j = i + 1; j < byLength.length; j++) {
                if (byLength[j].contains(byLength[i])) {
                    result.add(byLength[i]);
                    break;
                }
            }
        }
        return result;
    }
}
