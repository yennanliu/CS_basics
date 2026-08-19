package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/generalized-abbreviation/

import java.util.*;

/**
 *  320. Generalized Abbreviation
 *  Medium
 *
 *  A word's generalized abbreviation can be constructed by taking any number of
 *  non-adjacent, non-empty substrings and replacing them with their respective
 *  lengths.
 *
 *  For example, "abcde" can be abbreviated into:
 *   "a3e"   ("bcd" turned into "3")
 *   "1bcd1" ("a" and "e" both turned into "1")
 *   "5"     ("abcde" turned into "5")
 *   "abcde" (no substrings replaced)
 *  However, these are not valid abbreviations:
 *   "23"  ("ab" turned into "2" and "cde" turned into "3" are adjacent)
 *   "22de" ("ab" and "cd" turned into "2" and "2" are adjacent)
 *
 *  Given a string word, return a list of all the possible generalized
 *  abbreviations of word. Return the answer in any order.
 *
 *  Example 1:
 *   Input: word = "word"
 *   Output: ["4","3d","2r1","2rd","1o2","1o1d","1or1","1ord","w3","w2d","w1r1",
 *            "w1rd","wo2","wo1d","wor1","word"]
 *
 *  Example 2:
 *   Input: word = "a"
 *   Output: ["1","a"]
 *
 *  Constraints:
 *   1 <= word.length <= 15
 *   word consists of only lowercase English letters.
 */
public class GeneralizedAbbreviation {

    // V0
    // IDEA: backtracking - at each index either keep the char, or (if the previous
    //       token was NOT a number) abbreviate the next l chars into a number
    /**
     * time = O(n * 2^n)
     * space = O(n) recursion depth (excluding output)
     */
    public List<String> generateAbbreviations(String word) {
        List<String> res = new ArrayList<>();
        if (word == null) {
            return res;
        }
        dfs(word, 0, new StringBuilder(), false, res);
        return res;
    }

    private void dfs(String word, int i, StringBuilder cur, boolean prevIsNum, List<String> res) {
        if (i == word.length()) {
            res.add(cur.toString());
            return;
        }
        int len = cur.length();

        // option 1: keep the current character
        cur.append(word.charAt(i));
        dfs(word, i + 1, cur, false, res);
        cur.setLength(len);

        // option 2: abbreviate word[i .. i+l-1] as the number l (never two numbers in a row)
        if (!prevIsNum) {
            for (int l = 1; i + l <= word.length(); l++) {
                cur.append(l);
                dfs(word, i + l, cur, true, res);
                cur.setLength(len);
            }
        }
    }

    // V1
    // IDEA: bitmask - bit j set means word[j] is abbreviated; consecutive set bits merge
    //       into one count, which automatically avoids adjacent numbers
    /**
     * time = O(n * 2^n)
     * space = O(n) (excluding output)
     */
    public List<String> generateAbbreviations_1(String word) {
        List<String> res = new ArrayList<>();
        if (word == null) {
            return res;
        }
        int n = word.length();
        for (int mask = 0; mask < (1 << n); mask++) {
            StringBuilder sb = new StringBuilder();
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (((mask >> j) & 1) == 1) {
                    cnt += 1;
                } else {
                    if (cnt > 0) {
                        sb.append(cnt);
                        cnt = 0;
                    }
                    sb.append(word.charAt(j));
                }
            }
            if (cnt > 0) {
                sb.append(cnt);
            }
            res.add(sb.toString());
        }
        return res;
    }
}
