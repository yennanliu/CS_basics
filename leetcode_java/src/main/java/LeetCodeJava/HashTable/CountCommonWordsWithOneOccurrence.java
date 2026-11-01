package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-common-words-with-one-occurrence/description/

import java.util.*;

/**
 * 2085. Count Common Words With One Occurrence
 * Easy
 *
 * Given two string arrays words1 and words2, return the number of strings that appear exactly once
 * in each of the two arrays.
 *
 *
 * Example 1:
 *
 * Input: words1 = ["leetcode","is","amazing","as","is"], words2 = ["amazing","leetcode","is"]
 * Output: 2
 * Explanation:
 * - "leetcode" appears exactly once in each of the two arrays. We count this string.
 * - "amazing" appears exactly once in each of the two arrays. We count this string.
 * - "is" appears in each of the two arrays, but there are 2 occurrences of it in words1. We do not count this string.
 * - "as" appears once in words1, but does not appear in words2. We do not count this string.
 * Thus, there are 2 strings that appear exactly once in each of the two arrays.
 *
 * Example 2:
 *
 * Input: words1 = ["b","bb","bbb"], words2 = ["a","aa","aaa"]
 * Output: 0
 *
 * Example 3:
 *
 * Input: words1 = ["a","ab"], words2 = ["a","a","a","ab"]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= words1.length, words2.length <= 1000
 * 1 <= words1[i].length, words2[j].length <= 30
 * words1[i] and words2[j] consists only of lowercase English letters.
 *
 */
public class CountCommonWordsWithOneOccurrence {

    // V0
    // IDEA: TWO COUNTERS, COUNT THE WORDS WHOSE COUNT IS 1 IN BOTH
    /**
     * time = O(N1 + N2)
     * space = O(N1 + N2)
     */
    public int countWords(String[] words1, String[] words2) {
        Map<String, Integer> count1 = new HashMap<>();
        for (String word : words1) {
            count1.put(word, count1.getOrDefault(word, 0) + 1);
        }
        Map<String, Integer> count2 = new HashMap<>();
        for (String word : words2) {
            count2.put(word, count2.getOrDefault(word, 0) + 1);
        }

        int common = 0;
        for (Map.Entry<String, Integer> entry : count1.entrySet()) {
            boolean onceInFirst = entry.getValue() == 1;
            boolean onceInSecond = count2.getOrDefault(entry.getKey(), 0) == 1;
            if (onceInFirst && onceInSecond) {
                common++;
            }
        }
        return common;
    }
}
