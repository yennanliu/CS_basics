package LeetCodeJava.Set;

// https://leetcode.com/problems/distinct-echo-substrings/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1316. Distinct Echo Substrings
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Return the number of distinct non-empty substrings of text that can be written as the concatenation of some string with itself (i.e. it can be written as a + a where a is some string).
 *
 *
 *
 * Example 1:
 *
 * Input: text = "abcabcabc"
 * Output: 3
 * Explanation: The 3 substrings are "abcabc", "bcabca" and "cabcab".
 * Example 2:
 *
 * Input: text = "leetcodeleetcode"
 * Output: 2
 * Explanation: The 2 substrings are "ee" and "leetcodeleetcode".
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 2000
 * text has only lowercase English letters.
 *
 */
public class DistinctEchoSubstrings {

    // V0
//    public int distinctEchoSubstrings(String text) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/distinct-echo-substrings/solutions/1181698/java-clean-on2-rolling-hash-dynamic-prog-7kmx/
    private static final int PRIME = 101;
    private static final int MOD = 1_000_000_007;

    /**
     * time = O(1)
     * space = O(1)
     */
    public int distinctEchoSubstrings_2(String text) {
        int n = text.length();

        // dp[i][j] : hash value of text[i:j]
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            long hash = 0;
            for (int j = i; j < n; j++) {
                hash = hash * PRIME + (text.charAt(j) - 'a' + 1);
                hash %= MOD;
                dp[i][j] = (int) hash;
            }
        }

        Set<Integer> set = new HashSet<>();
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            // compare text[i:j] with text[j+1: 2j-i+1]
            for (int j = i; 2 * j - i + 1 < n; j++) {
                if (dp[i][j] == dp[j + 1][2 * j - i + 1] && set.add(dp[i][j]))
                    res++;
            }
        }

        return res;
    }

    // V3
    // https://leetcode.com/problems/distinct-echo-substrings/solutions/477137/another-java-solution-by-gyarish-on7k/
    /**
     * time = O(1)
     * space = O(1)
     */
    public int distinctEchoSubstrings_3(String text) {
        char[] word = text.toCharArray();
        Set<String> result = new HashSet<>();

        for (int L = 0; L < word.length - 1; L++) {
            for (int R = L + 1; R < word.length; R++) {
                if (isConCat(L, R, word)) {
                    String candidate = text.substring(L, R + 1);
                    result.add(candidate);
                }
            }
        }
        return result.size();
    }

    private boolean isConCat(int l, int r, char[] word) {
        if ((r - l) % 2 == 0) return false;

        int mid = l + (r - l) / 2 + 1;

        while (mid <= r) {
            if (word[l] != word[mid]) {
                return false;
            }
            mid++;
            l++;
        }
        return true;
    }


}
