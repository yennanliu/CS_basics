package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/soup-servings/

/**
 *  808. Soup Servings
 *  Medium
 *
 *  There are two types of soup, type A and type B. Initially, we have n ml of
 *  each type of soup. There are four kinds of operations:
 *    1. Serve 100 ml of soup A and 0 ml of soup B,
 *    2. Serve 75 ml of soup A and 25 ml of soup B,
 *    3. Serve 50 ml of soup A and 50 ml of soup B, and
 *    4. Serve 25 ml of soup A and 75 ml of soup B.
 *
 *  When we serve some soup, we give it to someone, and we no longer have it.
 *  Each turn, we will choose from the four operations with an equal probability
 *  0.25. If the remaining volume of soup is not enough to complete the
 *  operation, we will serve as much as possible. We stop once we no longer have
 *  some quantity of both types of soup.
 *
 *  Note that we do not have an operation where all 100 ml's of soup B are used
 *  first.
 *
 *  Return the probability that soup A will be empty first, plus half the
 *  probability that A and B become empty at the same time. Answers within 10^-5
 *  of the actual answer will be accepted.
 *
 *  Example 1:
 *    Input: n = 50
 *    Output: 0.62500
 *
 *  Example 2:
 *    Input: n = 100
 *    Output: 0.71875
 *
 *  Constraints:
 *    - 0 <= n <= 10^9
 */
public class SoupServings {

    // V0
    // IDEA: scale volumes to units of 25 ml + memoized recursion.
    //       For large n the answer converges to 1.0 (n >= 4800 is safely within 1e-5).
    /**
     * time = O(M^2) where M = ceil(n / 25), capped at 200
     * space = O(M^2)
     */
    public double soupServings(int n) {
        if (n >= 4800) {
            return 1.0;
        }
        int m = (n + 24) / 25;   // ceil(n / 25)
        double[][] memo = new double[m + 1][m + 1];
        boolean[][] seen = new boolean[m + 1][m + 1];
        return helper(m, m, memo, seen);
    }

    private double helper(int a, int b, double[][] memo, boolean[][] seen) {
        if (a <= 0 && b <= 0) {
            return 0.5;
        }
        if (a <= 0) {
            return 1.0;
        }
        if (b <= 0) {
            return 0.0;
        }
        if (seen[a][b]) {
            return memo[a][b];
        }
        double res = 0.25 * (
                helper(a - 4, b, memo, seen)
                        + helper(a - 3, b - 1, memo, seen)
                        + helper(a - 2, b - 2, memo, seen)
                        + helper(a - 1, b - 3, memo, seen));
        seen[a][b] = true;
        memo[a][b] = res;
        return res;
    }

    // V1
    // IDEA: BOTTOM-UP TABULATION of the same recurrence - no recursion stack.
    //       dp[a][b] = answer when a units of A and b units of B remain (1 unit = 25 ml).
    /**
     * time = O(M^2) where M = ceil(n / 25), capped at 200
     * space = O(M^2)
     */
    public double soupServings_1(int n) {
        if (n >= 4800) {
            return 1.0;
        }
        int m = (n + 24) / 25;   // ceil(n / 25)
        if (m == 0) {
            return 0.5;         // both are already empty
        }
        double[][] dp = new double[m + 1][m + 1];
        // every predecessor has a strictly smaller "a", so ascending a is a valid order
        for (int a = 1; a <= m; a++) {
            for (int b = 1; b <= m; b++) {
                dp[a][b] = 0.25 * (
                        read_1(dp, a - 4, b)
                                + read_1(dp, a - 3, b - 1)
                                + read_1(dp, a - 2, b - 2)
                                + read_1(dp, a - 1, b - 3));
            }
        }
        return dp[m][m];
    }

    private double read_1(double[][] dp, int a, int b) {
        if (a <= 0 && b <= 0) {
            return 0.5;
        }
        if (a <= 0) {
            return 1.0;
        }
        if (b <= 0) {
            return 0.0;
        }
        return dp[a][b];
    }

    // V2
    // IDEA: FORWARD probability propagation - push the probability MASS out of the start
    //       state (m, m) toward the terminal states, instead of pulling answers back.
    //       Each operation strictly decreases "a", so one descending sweep over a suffices.
    /**
     * time = O(M^2) where M = ceil(n / 25), capped at 200
     * space = O(M^2)
     */
    public double soupServings_2(int n) {
        if (n >= 4800) {
            return 1.0;
        }
        int m = (n + 24) / 25;
        if (m == 0) {
            return 0.5;
        }
        double[][] prob = new double[m + 1][m + 1];
        prob[m][m] = 1.0;

        int[][] moves = new int[][]{{4, 0}, {3, 1}, {2, 2}, {1, 3}};
        double res = 0.0;

        for (int a = m; a >= 1; a--) {
            for (int b = m; b >= 1; b--) {
                double p = prob[a][b];
                if (p == 0.0) {
                    continue;
                }
                for (int[] mv : moves) {
                    int na = a - mv[0];
                    int nb = b - mv[1];
                    double q = 0.25 * p;
                    if (na <= 0 && nb <= 0) {
                        res += 0.5 * q;      // both empty at the same time
                    } else if (na <= 0) {
                        res += q;            // A empty first
                    } else if (nb <= 0) {
                        // B empty first -> contributes 0
                    } else {
                        prob[na][nb] += q;
                    }
                }
            }
        }
        return res;
    }
}
