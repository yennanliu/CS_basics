package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-pattern-ii/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 291. Word Pattern II
 * Medium
 * Lock: Prime
 *
 * Given a pattern and a string s, return true if s matches the pattern.
 *
 * A string s matches a pattern if there is some bijective mapping of single characters to
 * non-empty strings such that if each character in pattern is replaced by the string it maps
 * to, then the resulting string is s. A bijective mapping means that no two characters map to
 * the same string, and no character maps to two different strings.
 *
 *
 * Example 1:
 *
 * Input: pattern = "abab", s = "redblueredblue"
 * Output: true
 * Explanation: One possible mapping is as follows:
 * 'a' -> "red"
 * 'b' -> "blue"
 *
 * Example 2:
 *
 * Input: pattern = "aaaa", s = "asdasdasdasd"
 * Output: true
 * Explanation: One possible mapping is as follows:
 * 'a' -> "asd"
 *
 * Example 3:
 *
 * Input: pattern = "aabb", s = "xyzabcxzyabc"
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length, s.length <= 20
 * pattern and s consist of only lowercase English letters.
 *
 */
public class WordPattern2 {

    // V0
    // IDEA: BACKTRACKING with a bijective (two-way) mapping
    /**
     *  dfs(i, j) = can pattern[i:] match s[j:] ?
     *    - if pattern[i] is ALREADY mapped -> the mapped word MUST be the next slice of s
     *    - otherwise                       -> try every non-empty prefix s[j:end] as the word
     *
     *  `usedWords` enforces the OTHER direction of the bijection: two different pattern
     *  chars may NOT map to the same word.
     *
     *  time  = O(n^m)   // m = pattern.length, n = s.length; heavily pruned in practice
     *  space = O(m + n)
     */

    private Map<Character, String> charToWord;
    private Set<String> usedWords;

    public boolean wordPatternMatch(String pattern, String s) {
        this.charToWord = new HashMap<>();
        this.usedWords = new HashSet<>();
        return dfs(pattern, s, 0, 0);
    }

    private boolean dfs(String pattern, String s, int i, int j) {
        int m = pattern.length();
        int n = s.length();

        // both consumed at the SAME time -> success
        if (i == m && j == n) {
            return true;
        }
        // one ran out before the other -> failure
        if (i == m || j == n) {
            return false;
        }

        char c = pattern.charAt(i);

        /** NOTE !!!
         *
         *  already bound: the next chunk of s must be EXACTLY that word,
         *  there is nothing to branch on here
         */
        if (charToWord.containsKey(c)) {
            String w = charToWord.get(c);
            if (!s.startsWith(w, j)) {
                return false;
            }
            return dfs(pattern, s, i + 1, j + w.length());
        }

        // not bound yet: try EVERY non-empty prefix
        for (int end = j + 1; end <= n; end++) {
            String w = s.substring(j, end);

            /** NOTE !!!
             *
             *  this check is the `bijection` guard:
             *  another pattern char already owns this word
             */
            if (usedWords.contains(w)) {
                continue;
            }

            charToWord.put(c, w);
            usedWords.add(w);

            if (dfs(pattern, s, i + 1, end)) {
                return true;
            }

            // backtrack
            charToWord.remove(c);
            usedWords.remove(w);
        }

        return false;
    }

}
