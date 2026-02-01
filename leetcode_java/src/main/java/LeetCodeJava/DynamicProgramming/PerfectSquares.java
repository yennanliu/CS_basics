package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/perfect-squares/description/

import java.util.*;

/**
 * 279. Perfect Squares
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer n, return the least number of perfect square numbers that sum to n.
 *
 * A perfect square is an integer that is the square of an integer; in other words, it is the product of some integer with itself. For example, 1, 4, 9, and 16 are perfect squares while 3 and 11 are not.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 12
 * Output: 3
 * Explanation: 12 = 4 + 4 + 4.
 * Example 2:
 *
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 104
 *
 */
public class PerfectSquares {

    // V0
    // IDEA: BFS (gpt)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(n);
        visited.add(n);

        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            level++; // Level in BFS = number of squares used so far

            for (int i = 0; i < size; i++) {
                int curr = queue.poll();

                for (int j = 1; j * j <= curr; j++) {
                    int next = curr - j * j;

                    if (next == 0) {
                        return level; // Found shortest path
                    }

                    if (!visited.contains(next)) {
                        visited.add(next);
                        queue.offer(next);
                    }
                }
            }
        }

        return -1; // Should never reach here
    }

    // V0-1
    // IDEA: BRUTE FORCE (gpt) (TLE)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_0_1(int n) {
        if (n <= 0)
            return 0;
        return helper(n);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int helper(int n) {
        if (n == 0)
            return 0;

        int min = Integer.MAX_VALUE;

        for (int i = 1; i * i <= n; i++) {
            int square = i * i;
            min = Math.min(min, 1 + helper(n - square));
        }

        return min;
    }

    // V0-2
    // IDEA:  OPTIMIZED BRUTE FORCE (WITH MEMORY) (gpt)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_0_2(int n) {
        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        return helper_0_2(n, memo);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int helper_0_2(int n, int[] memo) {
        if (n == 0)
            return 0;
        if (memo[n] != -1)
            return memo[n];

        int min = Integer.MAX_VALUE;

        for (int i = 1; i * i <= n; i++) {
            min = Math.min(min, 1 + helper_0_2(n - i * i, memo));
        }

        memo[n] = min;
        return min;
    }

    // V0-3
    // IDEA: DP (gpt)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_0_3(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }

        return dp[n];
    }

    // V0-4
    // IDEA: BFS (gpt)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_0_4(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(n);
        visited.add(n);

        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            level++; // Level in BFS = number of squares used so far

            for (int i = 0; i < size; i++) {
                int curr = queue.poll();

                for (int j = 1; j * j <= curr; j++) {
                    int next = curr - j * j;

                    if (next == 0) {
                        return level; // Found shortest path
                    }

                    if (!visited.contains(next)) {
                        visited.add(next);
                        queue.offer(next);
                    }
                }
            }
        }

        return -1; // Should never reach here
    }

    // V1
    // IDEA: DP
    // https://leetcode.com/problems/perfect-squares/solutions/4694883/beats-99-users-cjavapythonjavascript-exp-37yg/
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_1(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; ++i) {
            int min_val = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; ++j) {
                min_val = Math.min(min_val, dp[i - j * j] + 1);
            }
            dp[i] = min_val;
        }
        return dp[n];
    }

    // V2
    // https://leetcode.com/problems/perfect-squares/solutions/1520447/dp-easy-to-understand-js-java-python-c-b-sk59/
    // IDEA: DP
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_2(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        int count = 1;
        while (count * count <= n) {
            int sq = count * count;
            for (int i = sq; i <= n; i++) {
                dp[i] = Math.min(dp[i - sq] + 1, dp[i]);
            }
            count++;
        }
        return dp[n];
    }

    // V3-1
    // https://leetcode.com/problems/perfect-squares/solutions/2837992/java-recursion-memoization-dp-3-square-t-72qb/
    // IDEA: Top Down DP (Recursion + Memoization)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_3_1(int n) {
        int[] memo = new int[n + 1];
        return helper(n, memo);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int helper(int n, int[] memo) {
        if (n < 4)
            return n;

        if (memo[n] != 0)
            return memo[n];

        int ans = n;

        for (int i = 1; i * i <= n; i++) {
            int square = i * i;
            ans = Math.min(ans, 1 + helper(n - square, memo));
        }

        return memo[n] = ans;
    }

    // V3-2
    // https://leetcode.com/problems/perfect-squares/solutions/2837992/java-recursion-memoization-dp-3-square-t-72qb/
    // IDEA: Top Down DP (Recursion + Memoization)
    // Time complexity: O(N * sqrt(N))
    // Space complexity: O(N)
    /**
     * time = O(N * sqrt(N))
     * space = O(N)
     */
    public int numSquares_3_2(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            dp[i] = i;

            for (int j = 1; j * j <= i; j++) {
                int square = j * j;
                dp[i] = Math.min(dp[i], 1 + dp[i - square]);
            }
        }

        return dp[n];
    }

}
