package dev;

public class Workspace21 {

    // LC 62
    public int uniquePaths(int m, int n) {
        // edge
        if (m == 1 || n == 1) {
            return 1;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            dp[y][0] =  1;
        }
        for(int x = 0; x < n; x++){
            dp[0][x] =  1;
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                dp[y][x] = dp[y-1][x] + dp[y][x-1];
            }
        }

        ///  /???
        return dp[m-1][n-1];
    }

}
