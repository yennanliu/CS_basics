package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-paths/description/
/**
 * 62. Unique Paths
 * Solved
 * Medium
 * Topics
 * Companies
 * There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
 *
 * Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the bottom-right corner.
 *
 * The test cases are generated so that the answer will be less than or equal to 2 * 109.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: m = 3, n = 7
 * Output: 28
 * Example 2:
 *
 * Input: m = 3, n = 2
 * Output: 3
 * Explanation: From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
 * 1. Right -> Down -> Down
 * 2. Down -> Down -> Right
 * 3. Down -> Right -> Down
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 100
 *
 */

import java.math.BigInteger;
import java.util.Arrays;

public class UniquePaths {

    // V0
    // IDEA: 1-D DP (fixed by gpt)
    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Initialize all to 1 (first row)

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                /**
                 *  dp equation: dp[i][j] = dp[i-1][j] + dp[i][j-1]
                 */
                dp[j] += dp[j - 1];
            }
        }

        return dp[n - 1];
    }

    // V0-1
    // IDEA: 2-D DP (bottom-up)
    /**
     *  IDEA:
     *
     *  This code calculates the number of unique paths from the
     *  top-left corner (0,0) to the bottom-right corner (m-1, n-1) of an m x n grid,
     *  allowing only right and down movements.
     *
     */
    public int uniquePaths_0_1(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        dp[m - 1][n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
        /**
         *  NOTE !!!
         *
         * - Each dp[i][j] is computed as:
         *   -> `dp[i][j] = dp[i+1][j] + dp[i][j+1]`
         *
         *
         *   reason:
         *    - dp[i+1][j] → paths from the cell below (going down).
         * 	  - dp[i][j+1] → paths from the cell to the right (going right).
         *
         *
         */
        dp[i][j] += dp[i + 1][j] + dp[i][j + 1];
            }
        }

        return dp[0][0];
    }

    // VO-2
    // IDEA : MATH
    // -> the UNIQUE combination of x "a", and y "b"
    // -> e.g. [a, a,....a] and [b,b...,,,,b]
    //         <- x count ->    <- y count ->
    // -> so the combination count is
    //        (x+y)! / (x! * y!)
    public int uniquePaths_0_2(int m, int n) {
        if (m == 0 || n == 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }

        /** NOTE !!! BigInteger op code */
        BigInteger res = getFactorial((m - 1) + (n - 1))
                .divide(getFactorial(m - 1).multiply(getFactorial(n - 1)));
        return res.intValue();
    }

    private BigInteger getFactorial(int x) {
        if (x <= 0) {
            throw new ArithmeticException("x should be equal or bigger than 1");
        }
        /** NOTE !!! BigInteger op code */
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i < x + 1; i++) {
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    // V1-1
    // https://neetcode.io/problems/count-paths
    // IDEA: Recursion
    public int uniquePaths_1_1(int m, int n) {
        return dfs(0, 0, m, n);
    }

    public int dfs(int i, int j, int m, int n) {
        if (i == (m - 1) && j == (n - 1)) {
            return 1;
        }
        if (i >= m || j >= n) return 0;
        return dfs(i, j + 1, m, n) +
                dfs(i + 1, j, m, n);
    }

    // V1-2
    // https://neetcode.io/problems/count-paths
    // IDEA: Dynamic Programming (Top-Down)
    int[][] memo;
    public int uniquePaths_1_2(int m, int n) {
        memo = new int[m][n];
        for(int[] it : memo) {
            Arrays.fill(it, -1);
        }
        return dfs_1_2(0, 0, m, n);
    }

    public int dfs_1_2(int i, int j, int m, int n) {
        if (i == (m - 1) && j == (n - 1)) {
            return 1;
        }
        if (i >= m || j >= n) return 0;
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        return memo[i][j] = dfs_1_2(i, j + 1, m, n) +
                dfs_1_2(i + 1, j, m, n);
    }

    // V1-3
    // https://neetcode.io/problems/count-paths
    // IDEA:  Dynamic Programming (Bottom-Up)
    public int uniquePaths_1_3(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        dp[m - 1][n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                dp[i][j] += dp[i + 1][j] + dp[i][j + 1];
            }
        }

        return dp[0][0];
    }

    // V1-4
    // https://neetcode.io/problems/count-paths
    // IDEA:  Dynamic Programming (Space Optimized)
    public int uniquePaths_1_4(int m, int n) {
        int[] row = new int[n];
        Arrays.fill(row, 1);

        for (int i = 0; i < m - 1; i++) {
            int[] newRow = new int[n];
            Arrays.fill(newRow, 1);
            for (int j = n - 2; j >= 0; j--) {
                newRow[j] = newRow[j + 1] + row[j];
            }
            row = newRow;
        }
        return row[0];
    }

    // V1-5
    // https://neetcode.io/problems/count-paths
    // IDEA: Dynamic Programming (Optimal)
    public int uniquePaths_1_5(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 2; j >= 0; j--) {
                dp[j] += dp[j + 1];
            }
        }

        return dp[0];
    }

    // V1-6
    // https://neetcode.io/problems/count-paths
    // IDEA: MATH
    public int uniquePaths_1_6(int m, int n) {
        if (m == 1 || n == 1) {
            return 1;
        }
        if (m < n) {
            int temp = m;
            m = n;
            n = temp;
        }

        long res = 1;
        int j = 1;
        for (int i = m; i < m + n - 1; i++) {
            res *= i;
            res /= j;
            j++;
        }

        return (int) res;
    }

    // V2
    // https://leetcode.com/problems/unique-paths/solutions/4795217/memoization-and-tabulation-java-100-beats/
    public int uniquePaths_2(int m, int n) {
        Integer[][] memo = new Integer[m][n];
        return findPath(m - 1, n - 1, memo);
    }

    private int findPath(int r, int c, Integer[][] memo){

        if(r == 0 && c == 0)
            return 1;

        if(r < 0 || c < 0)
            return 0;

        if(memo[r][c] != null)
            return memo[r][c];

        int up = findPath(r - 1, c, memo);
        int left = findPath(r, c - 1, memo);

        memo[r][c] = up + left;

        return memo[r][c];
    }

    // V3
    // https://leetcode.com/problems/unique-paths/solutions/4801294/recurrsion-memoization-tabulation-easy-explaination/
    // Recurrsion
    // This is a reccursive solution and will give TLE with reccursive solution, try these with DP
    // public int uniquePaths(int m, int n) {
    //     return f(m, n, m-1, n-1);
    //     }

    // public int f(int m, int n, int r, int c){
    //     if(r ==  || c == n-1){
    //         return 1;
    //     }
    //     if(r < 0 || c < 0){
    //         return 0;
    //     }

    //     int up= f(m, n, r-1, c);
    //     int left= f(m, n, r, c-1);
    //     return up + left;
    // }


    //Memoization
    // public int uniquePaths(int m, int n) {
    //     int[][] dp= new int[m+1][n+1];
    //     for (int[] row : dp) {
    //         Arrays.fill(row, -1); // Initialize each cell of the array individually
    //     }
    //     return f(m, n, m-1, n-1, dp);
    //     }

    // public int f(int m, int n, int r, int c, int[][] dp){
    //     if(r == 0 || c == 0){
    //         return 1;
    //     }
    //     if(r < 0 || c < 0){
    //         return 0;
    //     }

    //     if(dp[r][c] != -1){
    //         return dp[r][c];
    //     }
    //     int up= f(m, n, r-1, c, dp);
    //     int left= f(m, n, r, c-1, dp);
    //     return dp[r][c]= up + left;
    // }

    // Tabulation
    public int uniquePaths_3(int m, int n) {
        int[][] dp= new int[m+1][n+1];
        dp[0][0]= 1;

        for(int i=0; i< m; i++){
            for(int j=0; j< n; j++){
                if(i==0 && j==0) continue;
                int up= 0;
                int left=0;
                if(i>0) up= dp[i-1][j];
                if(j>0) left= dp[i][j-1];
                dp[i][j]= up + left;
            }
        }
        return dp[m-1][n-1];
    }

}
