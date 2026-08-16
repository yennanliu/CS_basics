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


    // V1
    // IDEA: TWO MAPS (char -> word AND word -> char)
    /**
     *  V0 enforces the reverse direction of the bijection with a Set of used words.
     *  Carrying an explicit word -> char map instead makes the bijection symmetric
     *  and lets an already-bound word be REJECTED with the reason available.
     *
     *  time  = O(n^m)
     *  space = O(m + n)
     */
    public boolean wordPatternMatch_1(String pattern, String s) {
        return dfsTwoMaps(pattern, s, 0, 0, new HashMap<>(), new HashMap<>());
    }

    private boolean dfsTwoMaps(String pattern, String s, int i, int j,
                               Map<Character, String> c2w, Map<String, Character> w2c) {
        int m = pattern.length();
        int n = s.length();
        if (i == m && j == n) {
            return true;
        }
        if (i == m || j == n) {
            return false;
        }

        char c = pattern.charAt(i);
        if (c2w.containsKey(c)) {
            String w = c2w.get(c);
            return s.startsWith(w, j) && dfsTwoMaps(pattern, s, i + 1, j + w.length(), c2w, w2c);
        }

        for (int end = j + 1; end <= n; end++) {
            String w = s.substring(j, end);
            if (w2c.containsKey(w)) {
                continue; // this word already belongs to a different char
            }
            c2w.put(c, w);
            w2c.put(w, c);
            if (dfsTwoMaps(pattern, s, i + 1, end, c2w, w2c)) {
                return true;
            }
            c2w.remove(c);
            w2c.remove(w);
        }
        return false;
    }

    // V2
    // IDEA: BACKTRACKING + REMAINING-LENGTH PRUNING
    /**
     *  Before trying a candidate word we check whether the REST can still fit:
     *  every unbound pattern char needs at least 1 character, and every already
     *  bound char needs exactly its word length. If the remaining string is
     *  shorter than that minimum, the branch is dead.
     *
     *  Cuts the search space dramatically on inputs where V0 explodes.
     *
     *  time  = O(n^m) worst case, far less in practice
     *  space = O(m + n)
     */
    public boolean wordPatternMatch_2(String pattern, String s) {
        return dfsPrune(pattern, s, 0, 0, new HashMap<>(), new HashSet<>());
    }

    private boolean dfsPrune(String pattern, String s, int i, int j,
                             Map<Character, String> c2w, Set<String> used) {
        int m = pattern.length();
        int n = s.length();
        if (i == m && j == n) {
            return true;
        }
        if (i == m || j == n) {
            return false;
        }

        /** NOTE !!!
         *
         *  the PRUNE: how many chars does the remaining pattern need at minimum?
         */
        int need = 0;
        for (int t = i; t < m; t++) {
            String bound = c2w.get(pattern.charAt(t));
            need += bound == null ? 1 : bound.length();
        }
        if (need > n - j) {
            return false;
        }

        char c = pattern.charAt(i);
        if (c2w.containsKey(c)) {
            String w = c2w.get(c);
            return s.startsWith(w, j) && dfsPrune(pattern, s, i + 1, j + w.length(), c2w, used);
        }

        // the longest candidate is bounded by what the rest still needs
        int maxLen = n - j - (need - 1);
        for (int len = 1; len <= maxLen; len++) {
            String w = s.substring(j, j + len);
            if (used.contains(w)) {
                continue;
            }
            c2w.put(c, w);
            used.add(w);
            if (dfsPrune(pattern, s, i + 1, j + len, c2w, used)) {
                return true;
            }
            c2w.remove(c);
            used.remove(w);
        }
        return false;
    }

    // V3
    // IDEA: CHAR-INDEXED ARRAY MAPPING (no HashMap on the forward direction)
    /**
     *  The pattern is lowercase-only, so the char -> word map is just a
     *  String[26]. Removes all boxing / hashing on the hot path.
     *
     *  Same search shape as V0; the point is the state representation.
     *
     *  time  = O(n^m)
     *  space = O(26 + n)
     */
    public boolean wordPatternMatch_3(String pattern, String s) {
        return dfsArr(pattern, s, 0, 0, new String[26], new HashSet<>());
    }

    private boolean dfsArr(String pattern, String s, int i, int j,
                           String[] map, Set<String> used) {
        int m = pattern.length();
        int n = s.length();
        if (i == m && j == n) {
            return true;
        }
        if (i == m || j == n) {
            return false;
        }

        int c = pattern.charAt(i) - 'a';
        if (map[c] != null) {
            String w = map[c];
            return s.startsWith(w, j) && dfsArr(pattern, s, i + 1, j + w.length(), map, used);
        }

        for (int end = j + 1; end <= n; end++) {
            String w = s.substring(j, end);
            if (!used.add(w)) {
                continue;
            }
            map[c] = w;
            if (dfsArr(pattern, s, i + 1, end, map, used)) {
                return true;
            }
            map[c] = null;
            used.remove(w);
        }
        return false;
    }

}
