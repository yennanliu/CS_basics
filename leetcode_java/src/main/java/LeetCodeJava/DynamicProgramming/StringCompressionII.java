package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/string-compression-ii/description/

import java.util.Arrays;

/**
 * 1531. String Compression II
 * Hard
 *
 * Run-length encoding is a string compression method that works by replacing consecutive identical characters (repeated 2 or more times) with the concatenation of the character and the number marking the count of the characters (length of the run). For example, to compress the string "aabccc" we replace "aa" by "a2" and replace "ccc" by "c3". Thus the compressed string becomes "a2bc3".
 *
 * Notice that in this problem, we are not adding '1' after single characters.
 *
 * Given a string s and an integer k. You need to delete at most k characters from s such that the run-length encoded version of s has minimum length.
 *
 * Find the minimum length of the run-length encoded version of s after deleting at most k characters.
 *
 *
 * Example 1:
 *
 * Input: s = "aaabcccd", k = 2
 * Output: 4
 * Explanation: Compressing s without deleting anything will give us "a3bc3d" of length 6. Deleting any of the characters 'a' or 'c' would at most decrease the length of the compressed string to 5, for instance delete 2 'a' then we will have s = "abcccd" which compressed is abc3d. Therefore, the optimal way is to delete 'b' and 'd', then the compressed version of s will be "a3c3" of length 4.
 *
 * Example 2:
 *
 * Input: s = "aabbaa", k = 2
 * Output: 2
 * Explanation: If we delete both 'b' characters, the resulting compressed string would be "a4" of length 2.
 *
 * Example 3:
 *
 * Input: s = "aaaaaaaaaaa", k = 0
 * Output: 3
 * Explanation: Since k is zero, we cannot delete anything. The compressed string is "a11" of length 3.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * 0 <= k <= s.length
 * s contains only lowercase English letters.
 *
 */
public class StringCompressionII {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: MEMOIZED DFS ON (INDEX, DELETIONS LEFT) — "KEEP A RUN OF s[i]"
    //       dfs(i, left) = shortest encoding of s[i:] with at most `left` deletions.
    //       option 1: delete s[i]                         -> dfs(i + 1, left - 1)
    //       option 2: s[i] starts a run. Scan j forward, keeping every char equal to s[i] and
    //                 deleting every other char; after each kept char, the run of `kept`
    //                 copies costs encodedLength(kept) + dfs(j + 1, left - deleted).
    //       if the deletions left can remove the whole suffix, the answer is 0.
    /**
     * time = O(N^2 * K)
     * space = O(N * K)
     */
    public int getLengthOfOptimalCompression(String s, int k) {
        int n = s.length();
        // memo[i][left] = dfs(i, left); -1 = not computed
        int[][] memo = new int[n + 1][k + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return shortest(s, 0, k, memo);
    }

    /** Shortest encoding of s[i:] using at most `left` deletions. */
    private int shortest(String s, int i, int left, int[][] memo) {
        if (left < 0) {
            return INF;
        }
        int n = s.length();
        // enough deletions to drop the entire suffix
        if (n - i <= left) {
            return 0;
        }
        if (memo[i][left] != -1) {
            return memo[i][left];
        }

        // option 1: delete s[i]
        int best = shortest(s, i + 1, left - 1, memo);

        // option 2: s[i] heads a run; extend it over equal chars, deleting the others
        int kept = 0;
        int deleted = 0;
        for (int j = i; j < n; j++) {
            if (s.charAt(j) == s.charAt(i)) {
                kept++;
                int rest = shortest(s, j + 1, left - deleted, memo);
                if (rest < INF) {
                    best = Math.min(best, encodedLength(kept) + rest);
                }
            } else {
                deleted++;
                if (deleted > left) {
                    break;
                }
            }
        }

        memo[i][left] = best;
        return best;
    }

    /** Length of "c" + count for a run of `count` equal characters ("a", "a3", "a12", "a100"). */
    private int encodedLength(int count) {
        if (count == 1) {
            return 1;
        }
        if (count < 10) {
            return 2;
        }
        if (count < 100) {
            return 3;
        }
        return 4;
    }
}
