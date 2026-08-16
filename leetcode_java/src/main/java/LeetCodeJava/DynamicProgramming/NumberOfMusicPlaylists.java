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


    // V1
    // IDEA: ROLLING 1D ARRAY
    /**
     *  dp[i][*] only reads dp[i-1][*], so one row suffices -- iterate j DOWNWARD so
     *  dp[j-1] is still the previous row's value.
     *
     *  O(n) memory instead of O(goal * n).
     *
     *  time  = O(goal * n)
     *  space = O(n)
     */
    public int numMusicPlaylists_1(int n, int goal, int k) {
        final long MOD = 1_000_000_007L;
        long[] dp = new long[n + 1];
        dp[0] = 1;

        for (int i = 1; i <= goal; i++) {
            for (int j = n; j >= 1; j--) {
                long fresh = dp[j - 1] * (n - j + 1) % MOD;
                long repeat = (j > k) ? dp[j] * (j - k) % MOD : 0;
                dp[j] = (fresh + repeat) % MOD;
            }
            dp[0] = 0;   // a non-empty playlist always uses at least one song
        }
        return (int) dp[n];
    }

    // V2
    // IDEA: INCLUSION-EXCLUSION CLOSED FORM
    /**
     *  Counting the sequences that use EXACTLY the n songs by subtracting those
     *  missing at least one:
     *
     *      answer = sum_{i=0..n} (-1)^i * C(n, i) * P(n - i, k) * (n - i - k)^(goal - k)
     *
     *  where P(m, k) is the falling factorial (the first k slots must all be
     *  distinct).
     *
     *  O(n log MOD) rather than O(goal * n) -- and it needs no table at all.
     *
     *  time  = O(n log MOD)
     *  space = O(n)
     */
    public int numMusicPlaylists_2(int n, int goal, int k) {
        final long MOD = 1_000_000_007L;

        long[] fact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }

        long res = 0;
        for (int i = 0; i <= n - k; i++) {
            int m = n - i;                       // songs actually allowed
            // C(n, i) with the falling factorial P(m, k) folded in
            long term = modPow(m - k <= 0 ? 0 : m - k, goal - k, MOD);
            if (m - k == 0) {
                term = (goal - k == 0) ? 1 : 0;
            }
            long ways = fact[n] % MOD
                    * modInverse(fact[i], MOD) % MOD
                    * modInverse(fact[m - k], MOD) % MOD
                    * term % MOD;
            if (i % 2 == 0) {
                res = (res + ways) % MOD;
            } else {
                res = (res - ways + MOD) % MOD;
            }
        }
        return (int) res;
    }

    private long modPow(long base, long exp, long mod) {
        long res = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = res * base % mod;
            }
            base = base * base % mod;
            exp >>= 1;
        }
        return res;
    }

    private long modInverse(long v, long mod) {
        return modPow(v, mod - 2, mod);   // Fermat, mod is prime
    }

    // V3
    // IDEA: TOP-DOWN MEMOISED RECURSION
    /**
     *  ways(i, j) = ways(i-1, j-1) * (n - j + 1) + ways(i-1, j) * max(0, j - k)
     *
     *  The same recurrence as V0 stated as a recursion, so the two cases (a brand
     *  new song / a legal repeat) read as choices rather than as table writes.
     *
     *  time  = O(goal * n)
     *  space = O(goal * n)
     */
    private Long[][] memoPl;

    public int numMusicPlaylists_3(int n, int goal, int k) {
        memoPl = new Long[goal + 1][n + 1];
        return (int) waysPl(goal, n, n, k);
    }

    private long waysPl(int i, int j, int n, int k) {
        final long MOD = 1_000_000_007L;
        if (i == 0) {
            return j == 0 ? 1 : 0;
        }
        if (j <= 0) {
            return 0;
        }
        if (memoPl[i][j] != null) {
            return memoPl[i][j];
        }
        long fresh = waysPl(i - 1, j - 1, n, k) * (n - j + 1) % MOD;
        long repeat = (j > k) ? waysPl(i - 1, j, n, k) * (j - k) % MOD : 0;
        long res = (fresh + repeat) % MOD;
        memoPl[i][j] = res;
        return res;
    }

}
