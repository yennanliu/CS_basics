package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/2-keys-keyboard/

/**
 *  650. 2 Keys Keyboard
 *  Medium
 *
 *  There is only one character 'A' on the screen of a notepad. You can perform
 *  one of two operations on this notepad for each step:
 *
 *   - Copy All: You can copy all the characters present on the screen
 *     (a partial copy is not allowed).
 *   - Paste: You can paste the characters which are copied last time.
 *
 *  Given an integer n, return the minimum number of operations to get the
 *  character 'A' exactly n times on the screen.
 *
 *  Example 1:
 *
 *  Input: n = 3
 *  Output: 3
 *  Explanation: Copy All, Paste, Paste.
 *
 *  Example 2:
 *
 *  Input: n = 1
 *  Output: 0
 *
 *  Constraints:
 *
 *  1 <= n <= 1000
 */
public class TwoKeysKeyboard {

    // V0
    // IDEA: DP
    //  dp[x] = min ops to reach x; if y divides x, we can reach x from y by
    //  one "Copy All" plus (x/y - 1) pastes -> dp[x] = dp[y] + x/y
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int minSteps(int n) {
        int[] dp = new int[n + 1];
        for (int x = 2; x <= n; x++) {
            dp[x] = Integer.MAX_VALUE;
            for (int y = 1; y < x; y++) {
                if (x % y == 0) {
                    dp[x] = Math.min(dp[x], dp[y] + x / y);
                }
            }
        }
        return dp[n];
    }

    // V1
    // IDEA: MATH - the answer is the sum of prime factors of n
    /**
     * time = O(sqrt(n))
     * space = O(1)
     */
    public int minSteps_1(int n) {
        int res = 0;
        int p = 2;
        while (p * p <= n) {
            while (n % p == 0) {
                res += p;
                n /= p;
            }
            p++;
        }
        if (n > 1) {
            res += n;
        }
        return res;
    }
}
