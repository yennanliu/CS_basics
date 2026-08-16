package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/scramble-string/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 87. Scramble String
 * Hard
 *
 * We can scramble a string s to get a string t using the following algorithm:
 *
 * 1. If the length of the string is 1, stop.
 * 2. If the length of the string is > 1, do the following:
 *    - Split the string into two non-empty substrings at a random index, i.e., if the
 *      string is s, divide it to x and y where s = x + y.
 *    - Randomly decide to swap the two substrings or to keep them in the same order.
 *      i.e., after this step, s may become s = x + y or s = y + x.
 *    - Apply step 1 recursively on each of the two substrings x and y.
 *
 * Given two strings s1 and s2 of the same length, return true if s2 is a scrambled
 * string of s1, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: s1 = "great", s2 = "rgeat"
 * Output: true
 *
 * Example 2:
 *
 * Input: s1 = "abcde", s2 = "caebd"
 * Output: false
 *
 * Example 3:
 *
 * Input: s1 = "a", s2 = "a"
 * Output: true
 *
 *
 * Constraints:
 *
 * s1.length == s2.length
 * 1 <= s1.length <= 30
 * s1 and s2 consist of lowercase English letters.
 *
 */
public class ScrambleString {

    // V0
    // IDEA: MEMOIZED DFS (interval DP)
    /**
     *  dfs(i, j, k) = can s1[i, i+k) be scrambled into s2[j, j+k) ?
     *
     *  for every split length h in [1, k):
     *     - NO swap : dfs(i, j, h)         and dfs(i+h, j+h, k-h)
     *     - SWAP    : dfs(i, j+k-h, h)     and dfs(i+h, j,   k-h)
     *
     *  NOTE !!! the `sorted letters differ` prune is what makes this fast --
     *           without it the branching explodes.
     *
     *  time  = O(n^4)   // O(n^3) states x O(n) splits
     *  space = O(n^3)
     */

    private String s1;
    private String s2;
    private Map<Integer, Boolean> memo;

    public boolean isScramble(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        this.s1 = s1;
        this.s2 = s2;
        this.memo = new HashMap<>();
        return dfs(0, 0, s1.length());
    }

    private boolean dfs(int i, int j, int k) {
        // pack (i, j, k) into one int key; n <= 30 so 5 bits each is plenty
        int key = (i << 12) | (j << 6) | k;
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        String a = s1.substring(i, i + k);
        String b = s2.substring(j, j + k);

        if (a.equals(b)) {
            memo.put(key, true);
            return true;
        }

        // PRUNE: different letter multisets can NEVER match
        char[] ca = a.toCharArray();
        char[] cb = b.toCharArray();
        Arrays.sort(ca);
        Arrays.sort(cb);
        if (!Arrays.equals(ca, cb)) {
            memo.put(key, false);
            return false;
        }

        for (int h = 1; h < k; h++) {
            // keep the two halves IN ORDER
            if (dfs(i, j, h) && dfs(i + h, j + h, k - h)) {
                memo.put(key, true);
                return true;
            }
            /** NOTE !!!
             *
             *  SWAP the two halves: s1's LEFT part matches s2's RIGHT part,
             *  which is why the s2 offset becomes `j + k - h`
             */
            if (dfs(i, j + k - h, h) && dfs(i + h, j, k - h)) {
                memo.put(key, true);
                return true;
            }
        }

        memo.put(key, false);
        return false;
    }


    // V1
    // IDEA: BOTTOM-UP INTERVAL DP over the length
    /**
     *  dp[len][i][j] = can s1[i..i+len) scramble into s2[j..j+len) ?
     *
     *  Filled by increasing length, so every sub-answer is ready when it is read --
     *  no recursion and no memo lookups.
     *
     *  time  = O(n^4)
     *  space = O(n^3)
     */
    public boolean isScramble_1(String s1, String s2) {
        int n = s1.length();
        if (n != s2.length()) {
            return false;
        }
        boolean[][][] dp = new boolean[n + 1][n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[1][i][j] = s1.charAt(i) == s2.charAt(j);
            }
        }

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                for (int j = 0; j + len <= n; j++) {
                    for (int h = 1; h < len && !dp[len][i][j]; h++) {
                        if (dp[h][i][j] && dp[len - h][i + h][j + h]) {
                            dp[len][i][j] = true;          // halves kept in order
                        } else if (dp[h][i][j + len - h] && dp[len - h][i + h][j]) {
                            dp[len][i][j] = true;          // halves swapped
                        }
                    }
                }
            }
        }
        return dp[n][0][0];
    }

    // V2
    // IDEA: MEMOISED RECURSION KEYED BY THE SUBSTRING PAIR
    /**
     *  The same search as V0 but the memo key is the pair of substrings rather than
     *  packed indices.
     *
     *  Slower to hash, yet it makes the state VISIBLE while debugging -- and it
     *  works unchanged if the two strings ever have different lengths at a level.
     *
     *  time  = O(n^4)
     *  space = O(n^3)
     */
    private Map<String, Boolean> memoStr;

    public boolean isScramble_2(String s1, String s2) {
        memoStr = new HashMap<>();
        return scrambleStr(s1, s2);
    }

    private boolean scrambleStr(String a, String b) {
        if (a.equals(b)) {
            return true;
        }
        String key = a + "#" + b;
        Boolean cached = memoStr.get(key);
        if (cached != null) {
            return cached;
        }

        char[] ca = a.toCharArray();
        char[] cb = b.toCharArray();
        Arrays.sort(ca);
        Arrays.sort(cb);
        if (!Arrays.equals(ca, cb)) {
            memoStr.put(key, false);
            return false;
        }

        int n = a.length();
        for (int h = 1; h < n; h++) {
            if (scrambleStr(a.substring(0, h), b.substring(0, h))
                    && scrambleStr(a.substring(h), b.substring(h))) {
                memoStr.put(key, true);
                return true;
            }
            if (scrambleStr(a.substring(0, h), b.substring(n - h))
                    && scrambleStr(a.substring(h), b.substring(0, n - h))) {
                memoStr.put(key, true);
                return true;
            }
        }
        memoStr.put(key, false);
        return false;
    }

    // V3
    // IDEA: SAME SEARCH, but the prune uses a 26-SLOT COUNT instead of sorting
    /**
     *  V0 sorts both substrings on every state to compare their letter multisets --
     *  O(k log k) per state. A running 26-entry difference counter does the same
     *  check in O(k), and it can be updated INSIDE the split loop so the prune
     *  costs nothing extra.
     *
     *  Same complexity class, materially faster in practice.
     *
     *  time  = O(n^4)
     *  space = O(n^3)
     */
    private Boolean[][][] memoCnt;

    public boolean isScramble_3(String s1, String s2) {
        int n = s1.length();
        if (n != s2.length()) {
            return false;
        }
        memoCnt = new Boolean[n][n][n + 1];
        return scrambleCnt(s1, s2, 0, 0, n);
    }

    private boolean scrambleCnt(String s1, String s2, int i, int j, int len) {
        if (memoCnt[i][j][len] != null) {
            return memoCnt[i][j][len];
        }

        boolean same = true;
        int[] cnt = new int[26];
        for (int t = 0; t < len; t++) {
            if (s1.charAt(i + t) != s2.charAt(j + t)) {
                same = false;
            }
            cnt[s1.charAt(i + t) - 'a'] += 1;
            cnt[s2.charAt(j + t) - 'a'] -= 1;
        }
        if (same) {
            memoCnt[i][j][len] = true;
            return true;
        }
        for (int c : cnt) {
            if (c != 0) {
                memoCnt[i][j][len] = false;
                return false;
            }
        }

        for (int h = 1; h < len; h++) {
            if (scrambleCnt(s1, s2, i, j, h) && scrambleCnt(s1, s2, i + h, j + h, len - h)) {
                memoCnt[i][j][len] = true;
                return true;
            }
            if (scrambleCnt(s1, s2, i, j + len - h, h) && scrambleCnt(s1, s2, i + h, j, len - h)) {
                memoCnt[i][j][len] = true;
                return true;
            }
        }
        memoCnt[i][j][len] = false;
        return false;
    }

}
