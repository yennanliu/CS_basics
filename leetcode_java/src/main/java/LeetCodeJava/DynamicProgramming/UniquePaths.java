package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-paths/description/


import java.math.BigInteger;

public class UniquePaths {

    // VO
    // IDEA : MATH
    // -> the UNIQUE combination of x "a", and y "b"
    // -> e.g. [a, a,....a] and [b,b...,,,,b]
    //         <- x count ->    <- y count ->
    // -> so the combination count is
    //        (x+y)! / (x! * y!)
    public int uniquePaths(int m, int n) {
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

    // V1
    // https://leetcode.com/problems/unique-paths/solutions/4795217/memoization-and-tabulation-java-100-beats/
    public int uniquePaths_1(int m, int n) {
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

    // V2
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
    public int uniquePaths_2(int m, int n) {
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
