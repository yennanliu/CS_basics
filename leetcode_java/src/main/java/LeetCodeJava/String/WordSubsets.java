package LeetCodeJava.String;

// https://leetcode.com/problems/word-subsets/

import java.util.ArrayList;
import java.util.List;

/**
 *  916. Word Subsets
 *  Medium
 *
 *  You are given two string arrays words1 and words2.
 *
 *  A string b is a subset of string a if every letter in b occurs in a
 *  including multiplicity.
 *
 *  A string a from words1 is universal if for every string b in words2, b is
 *  a subset of a.
 *
 *  Return an array of all the universal strings in words1. You may return the
 *  answer in any order.
 *
 *  Example 1:
 *  Input: words1 = ["amazon","apple","facebook","google","leetcode"],
 *         words2 = ["e","o"]
 *  Output: ["facebook","google","leetcode"]
 *
 *  Example 2:
 *  Input: words1 = ["amazon","apple","facebook","google","leetcode"],
 *         words2 = ["l","e"]
 *  Output: ["apple","google","leetcode"]
 *
 *  Constraints:
 *   - 1 <= words1.length, words2.length <= 10^4
 *   - 1 <= words1[i].length, words2[i].length <= 10
 */
public class WordSubsets {

    // V0
    // IDEA: COUNTING - collapse words2 into a single per-letter max requirement,
    //       then keep every word in words1 that meets all 26 requirements.
    /**
     * time = O(m + n)   // total characters of words1 + words2
     * space = O(1)      // aside from the output
     */
    public List<String> wordSubsets(String[] words1, String[] words2) {
        int[] need = new int[26];
        for (String b : words2) {
            int[] c = count(b);
            for (int i = 0; i < 26; i++) {
                need[i] = Math.max(need[i], c[i]);
            }
        }

        List<String> res = new ArrayList<>();
        for (String a : words1) {
            int[] c = count(a);
            boolean ok = true;
            for (int i = 0; i < 26; i++) {
                if (c[i] < need[i]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                res.add(a);
            }
        }
        return res;
    }

    private int[] count(String w) {
        int[] c = new int[26];
        for (int i = 0; i < w.length(); i++) {
            c[w.charAt(i) - 'a']++;
        }
        return c;
    }
}
