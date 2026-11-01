package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-balanced-permutations/description/
/**
 * 3343. Count Number of Balanced Permutations
 * Hard
 *
 * You are given a string num. A string of digits is called balanced if the sum of the digits at
 * even indices is equal to the sum of the digits at odd indices.
 *
 * Return the number of distinct permutations of num that are balanced.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * A permutation is a rearrangement of all the characters of a string.
 *
 *
 * Example 1:
 *
 * Input: num = "123"
 * Output: 2
 * Explanation:
 * The distinct permutations of num are "123", "132", "213", "231", "312" and "321".
 * Among them, "132" and "231" are balanced. Thus, the answer is 2.
 *
 * Example 2:
 *
 * Input: num = "112"
 * Output: 1
 * Explanation:
 * The distinct permutations of num are "112", "121", and "211".
 * Only "121" is balanced. Thus, the answer is 1.
 *
 * Example 3:
 *
 * Input: num = "12345"
 * Output: 0
 * Explanation:
 * None of the permutations of num are balanced, so the answer is 0.
 *
 *
 * Constraints:
 *
 * 2 <= num.length <= 80
 * num consists of digits
 *
 */
public class CountNumberOfBalancedPermutations {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: KNAPSACK OVER THE 10 DIGITS — CHOOSE HOW MANY COPIES OF EACH GO TO EVEN SLOTS, THEN COUNT ARRANGEMENTS
    /**
     *  Sending j_d copies of digit d to the even positions gives
     *      even! / prod(j_d!)  *  odd! / prod((cnt_d - j_d)!)
     *  distinct permutations, which factors to
     *      even! * odd! / prod(cnt_d!)  *  prod C(cnt_d, j_d)
     *
     *  DP def:
     *     - dp[used][sum] = sum over the digits processed so far of prod C(cnt_d, j_d),
     *                       with `used` even slots filled and `sum` digit-sum on the even side
     *  DP eq:
     *     - dp'[used + j][sum + d * j] += dp[used][sum] * C(cnt[d], j)
     *  Init: dp[0][0] = 1
     *  Answer: even! * odd! / prod(cnt_d!) * dp[evenSlots][total / 2]  (0 if total is odd)
     *
     *  time = O(10 * N * S * N), S = digit sum / 2
     *  space = O(N * S)
     */
    public int countBalancedPermutations(String num) {
        int n = num.length();
        int[] count = new int[10];
        int total = 0;
        for (char ch : num.toCharArray()) {
            int digit = ch - '0';
            count[digit]++;
            total += digit;
        }

        // the two halves can only match when the total is even
        if (total % 2 != 0) {
            return 0;
        }
        int target = total / 2;
        int evenSlots = (n + 1) / 2;
        int oddSlots = n / 2;

        // binomials C[i][j] mod p via Pascal's triangle
        long[][] binom = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            binom[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                binom[i][j] = (binom[i - 1][j - 1] + binom[i - 1][j]) % MOD;
            }
        }

        long[][] dp = new long[evenSlots + 1][target + 1];
        dp[0][0] = 1;
        for (int digit = 0; digit <= 9; digit++) {
            int copies = count[digit];
            if (copies == 0) {
                continue;
            }
            long[][] next = new long[evenSlots + 1][target + 1];
            for (int used = 0; used <= evenSlots; used++) {
                for (int sum = 0; sum <= target; sum++) {
                    long ways = dp[used][sum];
                    if (ways == 0) {
                        continue;
                    }
                    // send j copies of this digit to the even side
                    for (int j = 0; j <= copies; j++) {
                        int newUsed = used + j;
                        int newSum = sum + digit * j;
                        if (newUsed > evenSlots || newSum > target) {
                            break;
                        }
                        next[newUsed][newSum] = (next[newUsed][newSum] + ways * binom[copies][j]) % MOD;
                    }
                }
            }
            dp = next;
        }

        long[] factorial = new long[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i % MOD;
        }

        long result = dp[evenSlots][target] * factorial[evenSlots] % MOD * factorial[oddSlots] % MOD;
        // divide by prod(cnt_d!) using the modular inverse (Fermat: a^(p-2))
        long denominator = 1;
        for (int c : count) {
            denominator = denominator * factorial[c] % MOD;
        }
        return (int) (result * modPow(denominator, MOD - 2) % MOD);
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
