package LeetCodeJava.Math;

// https://leetcode.com/problems/4-keys-keyboard/

/**
 *  651. 4 Keys Keyboard
 *  Medium
 *
 *  Imagine you have a special keyboard with the following keys:
 *    Key 1: (A)      Print one 'A' on screen.
 *    Key 2: (Ctrl-A) Select the whole screen.
 *    Key 3: (Ctrl-C) Copy selection to buffer.
 *    Key 4: (Ctrl-V) Print buffer on screen appending it after what has
 *                    already been printed.
 *
 *  Given an integer n, return the maximum number of 'A' you can print on the
 *  screen with at most n presses of the keys.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: 3
 *    Explanation: A, A, A
 *
 *  Example 2:
 *    Input: n = 7
 *    Output: 9
 *    Explanation: A, A, A, Ctrl-A, Ctrl-C, Ctrl-V, Ctrl-V
 *
 *  Constraints:
 *   - 1 <= n <= 50
 */
public class FourKeysKeyboard {

    // V0
    // IDEA: DP. dp[i] = max A's with i presses.
    //       Either press 'A' (dp[i-1] + 1), or pick a break point j (< i - 2),
    //       do Ctrl-A + Ctrl-C at j+1, j+2 then (i - j - 2) Ctrl-V presses,
    //       which multiplies dp[j] by (i - j - 1).
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int maxA(int n) {

        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            // just press 'A'
            dp[i] = dp[i - 1] + 1;

            // Ctrl-A, Ctrl-C after j presses, then (i - j - 2) Ctrl-V
            // => dp[j] * (i - j - 1)
            for (int j = 1; j <= i - 3; j++) {
                dp[i] = Math.max(dp[i], dp[j] * (i - j - 1));
            }
        }

        return dp[n];
    }
}
