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

}
