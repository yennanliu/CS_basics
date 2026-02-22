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


    // LC 63
    // 17.18 - 28 pm
    /**
     * -> Return the number of possible unique paths
     *   that the robot can take to reach the bottom-right corner.
     *
     *    NOTE:
     *
     *     - An obstacle and space are marked as 1 or 0 respectively in grid.
     *       A path that the robot takes cannot include any square that is an obstacle.
     *
     *  ---------------------
     *
     *   IDEA 1) MULTI SOURCE BFS  -> TLE ???
     *
     *   IDEA 2) DP
     *
     *
     *  ---------------------
     *
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if(obstacleGrid == null){
            return 0;
        }
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // edge // ???
        if (m == 0 || n == 0) {
            return 0;
        }
        if(obstacleGrid[m-1][n-1] == 1){
            return 0;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            //dp[y][0] =  1;
            if(obstacleGrid[y][0] != 1){
                dp[y][0] =  1;
            }
        }
        for(int x = 0; x < n; x++){
           // dp[0][x] =  1;
            if(obstacleGrid[0][x] != 1){
                dp[0][x] =  1;
            }
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                // case 1) (x,y) is NOT obstacle
                if(obstacleGrid[y][x] != 1){
                    if(dp[y-1][x] != -1){
                        dp[y][x] += dp[y-1][x];
                    }
                    if(dp[y][x-1] != -1){
                        dp[y][x] += dp[y][x-1];
                    }
                   // dp[y][x] = dp[y-1][x] + dp[y][x-1];
                }
                // case 1) (x,y) is obstacle
                else{
                    dp[y][x] = -1; // ??? // mark as `CAN'T visit`
                }
            }
        }

        ///  /???
        return Math.max(dp[m - 1][n - 1], 0);
    }



    // LC 980
    public int uniquePathsIII(int[][] grid) {

        return 0;
    }


    // LC 1920
    // 12.34 - 12.44 PM
    /**
     *  -> build an array ans of the same length where ans[i] = nums[nums[i]]
     *   for each 0 <= i < nums.length and return it.
     *
     *    nums: zero based arr
     *
     *
     *  --------------------
     *
     *
     *
     *  --------------------
     *
     *
     */
    public int[] buildArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        int[] res = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
            res[i] = nums[nums[i]];
        }

        return res;
    }


}
