package LeetCodeJava.HashTable;

// https://leetcode.com/problems/before-and-after-puzzle/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *  1181. Before and After Puzzle
 *  Medium
 *
 *  Given a list of phrases, generate a list of Before and After puzzles.
 *
 *  A phrase is a string that consists of lowercase English letters and spaces only.
 *  No space appears in the start or the end of a phrase. There are no consecutive
 *  spaces in a phrase.
 *
 *  Before and After puzzles are phrases that are formed by merging two phrases where
 *  the last word of the first phrase is the same as the first word of the second
 *  phrase. Note that only the last word of the first phrase and the first word of the
 *  second phrase are merged in this process.
 *
 *  Return the Before and After puzzles that can be formed by every two phrases
 *  phrases[i] and phrases[j] where i != j. Note that the order of matching two
 *  phrases matters, we want to consider both orders.
 *
 *  You should return a list of distinct strings sorted lexicographically, after
 *  removing all duplicate phrases in the generated Before and After puzzles.
 *
 *  Example 1:
 *    Input: phrases = ["writing code","code rocks"]
 *    Output: ["writing code rocks"]
 *
 *  Example 2:
 *    Input: phrases = ["a","b","a"]
 *    Output: ["a"]
 *
 *  Example 3:
 *    Input: phrases = ["ab ba","ba ab","ab ba"]
 *    Output: ["ab ba ab","ba ab ba"]
 *
 *  Constraints:
 *    1 <= phrases.length <= 100
 *    1 <= phrases[i].length <= 100
 */
public class BeforeAndAfterPuzzle {

    // V0
    // IDEA: HASH TABLE (index phrases by their FIRST word) + SORTED SET dedup
    //       for each phrase, only look at the phrases whose first word equals
    //       this phrase's LAST word -> no need to try all ordered pairs blindly.
    //       NOTE !!! i != j is on the INDEX, so two IDENTICAL phrases sitting at
    //                different indexes are still a legal pair (see example 2).
    //       TreeSet gives dedup + lexicographic order in one shot.
    /**
     * time = O(N^2 * L)   // N = #phrases, L = phrase length
     * space = O(N * L)
     */
    public List<String> beforeAndAfterPuzzles(String[] phrases) {
        int n = phrases.length;
        String[][] words = new String[n][];
        for (int i = 0; i < n; i++) {
            words[i] = phrases[i].split(" ");
        }

        Map<String, List<Integer>> byFirst = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String first = words[i][0];
            if (!byFirst.containsKey(first)) {
                byFirst.put(first, new ArrayList<Integer>());
            }
            byFirst.get(first).add(i);
        }

        TreeSet<String> res = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            String last = words[i][words[i].length - 1];
            List<Integer> cands = byFirst.get(last);
            if (cands == null) {
                continue;
            }
            for (Integer j : cands) {
                if (i == j) {
                    continue;
                }
                StringBuilder sb = new StringBuilder(phrases[i]);
                // skip words[j][0] : it is the merged word
                for (int k = 1; k < words[j].length; k++) {
                    sb.append(" ").append(words[j][k]);
                }
                res.add(sb.toString());
            }
        }

        return new ArrayList<>(res);
    }
}
