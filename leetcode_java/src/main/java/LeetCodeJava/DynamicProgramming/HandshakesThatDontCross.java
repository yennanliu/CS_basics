package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/handshakes-that-dont-cross/description/

/**
 * 1259. Handshakes That Don't Cross
 * Hard
 *
 * You are given an even number of people numPeople that stand around a circle and each person
 * shakes hands with someone else so that there are numPeople / 2 handshakes total.
 *
 * Return the number of ways these handshakes could occur such that none of the handshakes cross.
 *
 * Since the answer could be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: numPeople = 4
 * Output: 2
 * Explanation: There are two ways to do it, the first way is [(1,2),(3,4)] and the second one is
 * [(2,3),(4,1)].
 *
 * Example 2:
 *
 * Input: numPeople = 6
 * Output: 5
 *
 *
 * Constraints:
 *
 * 2 <= numPeople <= 1000
 * numPeople is even.
 *
 */
public class HandshakesThatDontCross {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP (CATALAN NUMBER RECURRENCE)
    //   dp[i] = number of non-crossing handshake layouts for i people (i even), dp[0] = 1.
    //   person 1 shakes hands with someone, splitting the others into a left group of `left`
    //   people and a right group of i - left - 2; both must be even or a chord has to cross:
    //     dp[i] = sum(dp[left] * dp[i - left - 2]) for left = 0, 2, ..., i - 2
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int numberOfWays(int numPeople) {
        long[] dp = new long[numPeople + 1];
        dp[0] = 1;
        for (int people = 2; people <= numPeople; people += 2) {
            long total = 0;
            for (int left = 0; left < people; left += 2) {
                int right = people - left - 2;
                // both factors are < MOD, so the product fits in a long
                total = (total + dp[left] * dp[right]) % MOD;
            }
            dp[people] = total;
        }
        return (int) dp[numPeople];
    }
}
