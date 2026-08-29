package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/2-keys-keyboard/

import java.util.ArrayDeque;
import java.util.Deque;

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

    // V2
    // IDEA: BFS over the raw state space (charsOnScreen, charsInClipboard).
    //       Makes no divisor/prime assumption at all, so it is the readable reference
    //       that V0's DP and V1's math shortcut are checked against.
    /**
     * time = O(n^2)
     * space = O(n^2)
     */
    public int minSteps_2(int n) {
        if (n <= 1) {
            return 0;
        }
        boolean[][] visited = new boolean[n + 1][n + 1];
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{1, 0});          // {screen, clipboard}
        visited[1][0] = true;

        int steps = 0;
        while (!q.isEmpty()) {
            steps++;
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                int screen = cur[0];
                int clip = cur[1];

                // op 1: copy all
                if (!visited[screen][screen]) {
                    visited[screen][screen] = true;
                    q.offer(new int[]{screen, screen});
                }
                // op 2: paste
                if (clip > 0 && screen + clip <= n) {
                    if (screen + clip == n) {
                        return steps;
                    }
                    if (!visited[screen + clip][clip]) {
                        visited[screen + clip][clip] = true;
                        q.offer(new int[]{screen + clip, clip});
                    }
                }
            }
        }
        return -1;   // unreachable for n >= 1
    }
}
