package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-number-of-ways-to-make-the-sum/description/
// https://leetcode.ca/2024-06-22-3183-The-Number-of-Ways-to-Make-the-Sum/

/**
 * 3183. The Number of Ways to Make the Sum
 * Medium
 *
 * You have an infinite number of coins with values 1, 2, and 6, and there are also two coins with value 4.
 *
 * Given an integer n, return the number of ways to make the sum of n.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * Note that the order of the coins doesn't matter and 2 + 4 + 6 is the same as 4 + 6 + 2.
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: 4
 * Explanation:
 * Here are the four combinations: (1, 1, 1, 1), (1, 1, 2), (2, 2), (4).
 *
 * Example 2:
 *
 * Input: n = 12
 * Output: 22
 * Explanation:
 * Here are the 22 combinations:
 * (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2), (1, 1, 1, 1, 1, 1, 1, 1, 2, 2), (1, 1, 1, 1, 1, 1, 2, 2, 2), (1, 1, 1, 1, 2, 2, 2, 2), (1, 1, 2, 2, 2, 2, 2), (2, 2, 2, 2, 2, 2), (1, 1, 1, 1, 1, 1, 6), (1, 1, 1, 1, 2, 6), (1, 1, 2, 2, 6), (2, 2, 2, 6), (6, 6), (1, 1, 1, 1, 4, 4), (1, 1, 2, 4, 4), (2, 2, 4, 4), (4, 4, 4)... and so on.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class TheNumberOfWaysToMakeTheSum {

    // V0
    // IDEA: UNBOUNDED COIN CHANGE FOR {1, 2, 6}, THEN SPLIT ON HOW MANY 4s ARE USED
    //   ways[t] = number of ways to make t with unlimited 1s, 2s and 6s (combinations:
    //   loop coins outside, amounts inside, amounts upward).
    //   The two 4-coins are interchangeable, so only their count matters:
    //   answer = ways[n] + ways[n - 4] + ways[n - 8].
    /**
     * time = O(N)
     * space = O(N)
     */
    public int numberOfWays(int n) {
        final int MOD = 1_000_000_007;
        int[] coins = {1, 2, 6};
        long[] ways = new long[n + 1];
        // one way to make 0: use no coins
        ways[0] = 1;
        for (int coin : coins) {
            for (int amount = coin; amount <= n; amount++) {
                ways[amount] = (ways[amount] + ways[amount - coin]) % MOD;
            }
        }

        // zero fours
        long total = ways[n];
        // one four
        if (n >= 4) {
            total += ways[n - 4];
        }
        // two fours
        if (n >= 8) {
            total += ways[n - 8];
        }
        return (int) (total % MOD);
    }
}
