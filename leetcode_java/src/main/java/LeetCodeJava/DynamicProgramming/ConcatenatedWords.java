package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/concatenated-words/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  472. Concatenated Words
 *  Hard
 *
 *  Given an array of strings words (without duplicates), return all the
 *  concatenated words in the given list of words.
 *
 *  A concatenated word is defined as a string that is comprised entirely of at
 *  least two shorter words (not necessarily distinct) in the given array.
 *
 *  Example 1:
 *
 *  Input: words = ["cat","cats","catsdogcats","dog","dogcatsdog",
 *                  "hippopotamuses","rat","ratcatdogcat"]
 *  Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
 *
 *  Example 2:
 *
 *  Input: words = ["cat","dog","catdog"]
 *  Output: ["catdog"]
 *
 *  Constraints:
 *
 *  1 <= words.length <= 10^4
 *  1 <= words[i].length <= 30
 *  words[i] consists of only lowercase English letters.
 *  All the strings of words are unique.
 *  1 <= sum(words[i].length) <= 10^5
 */
public class ConcatenatedWords {

    // V0
    // IDEA: WORD BREAK (DP) per word, using the whole dictionary but forbidding
    //       the word from being "built" out of itself alone.
    /**
     * time = O(n * L^3)  // n words, L = max word length (L^2 substrings of len O(L))
     * space = O(n * L)
     */
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        List<String> res = new ArrayList<>();
        if (words == null || words.length == 0) {
            return res;
        }
        Set<String> dict = new HashSet<>(Arrays.asList(words));
        for (String w : words) {
            if (w == null || w.length() == 0) {
                continue;
            }
            if (canForm(w, dict)) {
                res.add(w);
            }
        }
        return res;
    }

    private boolean canForm(String word, Set<String> dict) {
        int n = word.length();
        boolean[] dp = new boolean[n + 1]; // dp[i]: word[0..i) is concatenable
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            // when i == n, start j at 1 so the word itself is never used as a whole
            for (int j = (i == n ? 1 : 0); j < i; j++) {
                if (dp[j] && dict.contains(word.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

    // V1
    // IDEA: sort by length + growing dictionary; a concatenated word can only be
    //       built from strictly shorter words, so only add words as we pass them.
    /**
     * time = O(n * log n + n * L^3)
     * space = O(n * L)
     */
    public List<String> findAllConcatenatedWordsInADict_1(String[] words) {
        List<String> res = new ArrayList<>();
        if (words == null || words.length == 0) {
            return res;
        }
        String[] sorted = words.clone();
        Arrays.sort(sorted, (a, b) -> a.length() - b.length());

        Set<String> seen = new HashSet<>();
        for (String w : sorted) {
            if (w == null || w.length() == 0) {
                continue;
            }
            if (!seen.isEmpty() && wordBreak(w, seen)) {
                res.add(w);
            }
            seen.add(w);
        }
        return res;
    }

    private boolean wordBreak(String word, Set<String> dict) {
        int n = word.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(word.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}
