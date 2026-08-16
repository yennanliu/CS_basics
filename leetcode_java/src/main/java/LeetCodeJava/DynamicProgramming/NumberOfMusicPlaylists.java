package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-music-playlists/description/
/**
 * 920. Number of Music Playlists
 * Hard
 *
 * Your music player contains n different songs. You want to listen to goal songs (not
 * necessarily different) during your trip. To avoid boredom, you will create a playlist
 * so that:
 *
 * Every song is played at least once.
 * A song can only be played again only if k other songs have been played.
 *
 * Given n, goal, and k, return the number of possible playlists that you can create.
 * Since the answer can be very large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: n = 3, goal = 3, k = 1
 * Output: 6
 * Explanation: There are 6 possible playlists: [1, 2, 3], [1, 3, 2], [2, 1, 3],
 * [2, 3, 1], [3, 1, 2], and [3, 2, 1].
 *
 * Example 2:
 *
 * Input: n = 2, goal = 3, k = 0
 * Output: 6
 * Explanation: There are 6 possible playlists: [1, 1, 2], [1, 2, 1], [2, 1, 1],
 * [2, 2, 1], [2, 1, 2], and [1, 2, 2].
 *
 * Example 3:
 *
 * Input: n = 2, goal = 3, k = 1
 * Output: 2
 * Explanation: There are 2 possible playlists: [1, 2, 1] and [2, 1, 2].
 *
 * Constraints:
 *
 * 0 <= k < n <= goal <= 100
 *
 */
public class NumberOfMusicPlaylists {

    // V0
    // IDEA: 2D DP (combinatorial counting)
    /**
     *  DP def:
     *     - dp[i][j] = number of playlists of length i that use EXACTLY j DISTINCT songs
     *
     *  Init:
     *     - dp[0][0] = 1
     *
     *  DP eq (what did we put in slot i ?):
     *
     *     a) a BRAND NEW song
     *          previous state dp[i-1][j-1], and there are (n - j + 1) unused songs left
     *          -> dp[i-1][j-1] * (n - j + 1)
     *
     *     b) a REPEAT of an already played song
     *          previous state dp[i-1][j], but the last k songs are BLOCKED,
     *          so only (j - k) of the j played songs are legal (needs j > k)
     *          -> dp[i-1][j] * (j - k)
     *
     *  Answer: dp[goal][n]  (all n songs used, playlist length goal)
     *
     *  time  = O(goal * n)
     *  space = O(goal * n)
     */
    public int numMusicPlaylists(int n, int goal, int k) {
        final long MOD = 1_000_000_007L;

        long[][] dp = new long[goal + 1][n + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= goal; i++) {
            for (int j = 1; j <= n; j++) {
                // a) play a song we have NEVER played
                dp[i][j] = dp[i - 1][j - 1] * (n - j + 1);
                // b) REPLAY an old song (the k most recent ones are blocked)
                if (j > k) {
                    dp[i][j] += dp[i - 1][j] * (j - k);
                }
                dp[i][j] %= MOD;
            }
        }

        return (int) dp[goal][n];
    }

}
