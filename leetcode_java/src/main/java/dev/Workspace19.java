package dev;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Workspace19 {

    // LC 516
    // 6.57 am - 7.07 am

    /**
     * -> Given a string s,
     * find the `longest` palindrome (回文)
     * subsequence's length in s.
     * <p>
     * <p>
     * NOTE:
     * <p>
     * A subsequence is a sequence that can be
     * derived from another sequence by `deleting`
     * some or no elements `without` changing the order
     * of the remaining elements.
     * <p>
     * --------------
     * <p>
     * IDEA 1) BRUTE FORCE
     * <p>
     * IDEA 2) SLIDE WINDOW
     * <p>
     * IDEA 3)  DP
     * <p>
     * - 2D DP ????
     * <p>
     * - DP def:  dp[i][j]:
     * - `longest` palindrome (回文)
     * subsequence between [i, j] (index)
     * <p>
     * - DP eq:
     * - dp[i][j] =
     * - if dp[i][j] == dp[
     * <p>
     * -------------
     * DP:
     * <p>
     * - DP def:
     * dp[i][j]:  longest  palindrome (回文) `subsequence` len in [i,j] idx
     * <p>
     * - DP eq:
     * dp[i][j] =
     * if s[i] == s[j+1]:
     * dp[i][j] = max( dp[i][j], dp[i+1][j-1] + 2) // ???
     * else:
     * dp[i][j] = max( dp[i-1][j], dp[i][j+1]) // ???
     * <p>
     * <p>
     * <p>
     * --------------
     */
    // dp
    public int longestPalindromeSubseq(String s) {
        // edge
        if (s.length() <= 1) {
            return s.length();
        }
        if (s.length() == 2) {
            return s.charAt(0) == s.charAt(1) ? 2 : 0;
        }


        // init dp
        int n = s.length();
        int[][] dp = new int[n + 1][n + 1]; // ??

        // NOTE !!!
        // Base case: every single character is a palindrome of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }


        // NOTE !!! WE MOVE BACKWARD !!!!
        // // Loop i backwards (start of the substring)
        // // Loop j forwards (end of the substring)
        for (int i = n + 1; i > 0; i--) {
            for (int j = n + 1; j > 0; j--) {
                if (s.charAt(i) == s.charAt(j)) {
                    //dp[i][j] = Math.max( dp[i][j], dp[i+1][j-1] + 2 );
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]); // ???
                }
            }
        }

        /**
         *      *     - DP eq:
         *      *         dp[i][j] =
         *      *            if s[i] == s[j+1]:
         *      *              dp[i][j] = max( dp[i][j], dp[i+1][j-1] + 2) // ???
         *      *            else:
         *      *             dp[i][j] = max( dp[i-1][j], dp[i][j+1]) // ???
         *
         */
//        for(int i = 1; i < n + 1; i++){
//            for(int j = 1; j < n + 1; j++){
//                if(s.charAt(i) == s.charAt(j)){
//                    dp[i][j] = Math.max( dp[i][j], dp[i+1][j-1] + 2 );
//                }else{
//                    dp[i][j] =  Math.max( dp[i-1][j], dp[i][j+1] ); // ???
//                }
//            }
//        }


        return dp[n][n];
    }


    // LC 376
    // 7.24 - 34 am

    /**
     * -> Given an integer array nums,
     * return the length of the `longest wiggle subsequence` of nums.
     * <p>
     * <p>
     * - NOTE:
     * - A subsequence
     * - by deleting some elements (possibly zero)
     * from the original sequence, leaving the
     * remaining elements in their original order.
     * <p>
     * -------------------
     * <p>
     * IDEA 1) BRUTE FORCE
     * <p>
     * IDEA 2) DP
     * <p>
     * 1. get diff array
     * [ n1-n0, n2-n1, ...]
     * <p>
     * - dp def:
     * <p>
     * boolean dp[i][j]: [i,j] subsequence is wiggle or not
     * <p>
     * - dp eq:
     * <p>
     * dp[i][j] =
     * if  diff[j-2] * diff[j-1] < 1:
     * dp[i + 1][j - 1] + 2   // ???
     * else:
     * max(dp[i+1][j], dp[i][j-1]) // /??
     * <p>
     * -------------------
     * <p>
     * <p>
     * ex 1)
     * <p>
     * <p>
     * ex 2)
     * <p>
     * Input: nums = [1,17,5,10,13,15,10,5,16,8]
     * Output: 7
     * <p>
     * ->
     * diff = (16, -7, 3, -3, 6, -8)
     */
    public int wiggleMaxLength(int[] nums) {
        // edge

        int n = nums.length;

        int[] diff = new int[n];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }

        // edge: if all diff > 0 or < 0
        // -> not wiggle -> return 0

        int maxWiggleLen = 0;
        int[][] dp = new int[n + 1][n + 1]; // ????


        return maxWiggleLen;
    }


    // LC 416
    // 9.58 - 10.09 am

    /**
     * true if
     * - you can partition the array into
     * two subsets such that the sum of the elements in both subset
     * <p>
     * <p>
     * ------------
     * <p>
     * IDEA 1) DP
     * <p>
     * - dp def:
     * <p>
     * dp[i] : can partition by sum == i
     * <p>
     * - dp eq:
     * <p>
     * ------------
     */
    public boolean canPartition(int[] nums) {
        // edge

        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // 2. If totalSum is odd, it's impossible to split into two equal integers
        if (totalSum % 2 != 0) {
            return false;
        }

        int target = totalSum / 2;

        int n = nums.length;
        //boolean[] dp = new boolean[n + 1]; // /?
        // NOTE !!! `target + 1`
        boolean[] dp = new boolean[target + 1]; // /?

        dp[0] = true; // ??

        // look forward
        for (int num : nums) {
            // look backward ??
//            for(int i = 1; i < n + 1; i++){
//                if(dp[num] || dp[target - num]){
//                    dp[num] = true;
//                }
//            }
            for (int sum = target; sum >= num; sum--) {
                dp[sum] = dp[sum] || dp[sum - num];
            }
        }

        return dp[n]; // ??
    }


    // LC 1905

    /**
     * ------------
     * <p>
     * -> Return the number of
     * islands in grid2 that
     * are considered sub-islands.
     * <p>
     * ------------
     * <p>
     * IDEA 1) 2 pass BFS
     * <p>
     * - 1. first pass: color the `invalid island`
     * 2. 2nd pass:  go through the `valid` island,
     * and get the island cnt
     */
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // ?? 1st pass
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                // ???
                if (grid2[y][x] == 1 && grid1[y][x] == 0) {
                    // color(grid1, grid2, x, y, -1);
                    color(grid2, x, y, -1);
                }
            }
        }

        int subIslandCnt = 0;

        // ?? 2nd pass
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                // ???
                // if(grid2[y][x] == 1 && grid1[y][x] == 1){
                if (grid2[y][x] == 1 && grid1[y][x] == 1) {
                    //color(grid1, grid2, 2);
                    //color(grid1, grid2, x, y, 2);
                    color(grid2, x, y, 2);
                    subIslandCnt += 1;
                }
            }
        }


        return subIslandCnt;
    }

    // dfs ???
    private void color(int[][] grid2, int x, int y, int newColor) {
        int l = grid2.length;
        int w = grid2[0].length;

        int[][] moves = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        // color
        grid2[y][x] = newColor;

        for (int[] m : moves) {
            int y_ = y + m[0];
            int x_ = x + m[1];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l) {
                // if(grid2[y_][x_] != newColor)
                // (grid2[y][x] == 1).
                if (grid2[y_][x_] != newColor && grid2[y_][x_] != 0) {
                    // proceed
                    color(grid2, x_, y_, newColor);
                }
            }
        }

    }


    // LC 300
    // 11.22 - 32 am

    /**
     * -> Given an integer array nums,
     * return the length of the
     * `longest strictly increasing subsequence.`
     * <p>
     * - subsequence:
     * - an array that can be derived
     * from another array by deleting some
     * or no elements without changing the
     * order of the remaining elements.
     * <p>
     * -----------------
     * <p>
     * IDEA 1) DP
     * <p>
     * - DP def:
     * dp[i][j]:
     * - len of longest increasing
     * subsequence in [i,j]
     * <p>
     * - DP eq:
     * dp[i][j] =
     * if  nums[j-1] < nums[j]:
     * dp[i][j] = max ( dp[i][j-1] + 1, dp[i][j] )
     * else:
     * // ???
     * <p>
     * <p>
     * -----------------
     */
    public int lengthOfLIS(int[] nums) {
        // edge

        int maxLenLIS = 1; // ??

        int n = nums.length;
        int[][] dp = new int[n + 1][n + 1]; //??
        // ???
        for (int x = 0; x < n; x++) {
            // if set start idx at every idx in nums,
            // the default LIS len should be 1 ? (ieself)
            dp[0][x] = 1;
        }

        // ???
        /**
         *      *     - DP eq:
         *      *          dp[i][j] =
         *      *             if  nums[j-1] < nums[j]:
         *      *                 dp[i][j] = max ( dp[i][j-1] + 1, dp[i][j] )
         *      *             else:
         *      *                // ???
         *
         */
        // loop start point (forward)
        for (int i = 0; i < n; i++) {
            // ??? backward
            // loop end point (forward) ?????
            // for(int j = i; j > 0; j--){
            for (int j = i; j > 0; j--) {
                if (nums[j - 1] < nums[j]) {
                    // ????
                    dp[i][j] = Math.max(dp[i][j - 1] + 1, dp[i][j]);
                    maxLenLIS = Math.max(maxLenLIS, dp[i][j]);
                }
            }
        }

        return maxLenLIS;
    }


    // LC 1025
    // 8.56 - 9.06
    // idea: dp

    /**
     * -> Return true if
     * and only if Alice wins the game,
     * assuming both players play optimally.
     * <p>
     * n at beginning
     * <p>
     * move:
     * <p>
     * 1. choose any x with
     * -  0 < x < n
     * -  n % x == 0
     * <p>
     * 2. replace n with n - x
     * <p>
     * -----------
     * <p>
     * -----------
     */
    public boolean divisorGame(int n) {

        return false;
    }


    // LC 551
    // 9.11 - 21 am

    /**
     * Return true if the student
     * is eligible for an attendance
     * award, or false otherwise.
     * <p>
     * 'A': Absent.
     * 'L': Late.
     * 'P': Present.
     * <p>
     * <p>
     * -------------
     * <p>
     * IDEA 1) STR OP
     * <p>
     * 1. count if total A >= 2
     * 2. check if L for >= 3 consecutive days
     * - first_l
     * - second_l
     * - third_l
     * <p>
     * - or con_cnt >= 3 ???
     */
    public boolean checkRecord_99999(String s) {
        // edge
        if (s.isEmpty() || s.length() == 1) {
            return true;
        }
        if (s.length() == 2) {
            if (s.charAt(0) == 'A' && s.charAt(1) == 'A') {
                return false;
            }
            return true;
        }

        int absent_cnt = 0;
        int con_late_cnt = 0;

        for (char c : s.toCharArray()) {
            // 'A'
            if (c == 'A') {
                absent_cnt += 1;
                con_late_cnt = 0;
            }
            // 'C'
            else if (c == 'L') {
                con_late_cnt += 1;
            }
            // 'P'
            else {
                con_late_cnt = 0;
            }

            if (absent_cnt >= 2) {
                return false;
            }
            if (con_late_cnt >= 3) {
                return false;
            }

        }

        return true;
    }

    // LC 552
    // 9.25 - 35 am

    /**
     * -> Given an integer n, return the `number` of
     * possible `attendance records` of `length n`
     * that make a student eligible for an attendance award.
     * The answer may be very large, so return it modulo 109 + 7.
     * <p>
     * <p>
     * 'A': Absent.
     * 'L': Late.
     * 'P': Present.
     * <p>
     * <p>
     * ------------------
     * <p>
     * IDEA 1) BRUTE FORCE
     * <p>
     * IDEA 2) DP
     * <p>
     * (with double loop)
     * <p>
     * - DP def:
     * int dp[i][absent_cnt]:
     * combination of  `attendance records`
     * with len = i
     * <p>
     * absent_cnt: total absent_cnt with len i
     * <p>
     * - DP eq:
     * <p>
     * dp[i][j] =
     * if absent_cnt < 2 && ( sum of 'L' in (s[i], s[i-1], s[i-2]) < 3 ):
     * max(dp[i][j], dp[i-1][j])
     * else:
     * 0
     * <p>
     * <p>
     * ------------------
     */
    public int checkRecord(int n) {
        // edge
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 3;
        }
        if (n == 2) {
            return 8;
        }

        // ???
        /**
         *      *     - DP def:
         *      *          int dp[i][absent_cnt]:
         *      *                    combination of  `attendance records`
         *      *                    with len = i
         *      *
         *      *                    absent_cnt: total absent_cnt with len i
         *
         */
        int[][] dp = new int[n + 1][n + 1];
        // init
        for (int y = 0; y < n + 1; y++) {
            dp[y][0] = y; // ???
        }

        String status = "ALP"; // /??

        // ??
        for (char c : status.toCharArray()) {
            // for(int i )
        }

        return 0;
    }


    // LC 62
    // 10.28 - 38 am

    /**
     * -> Given the two integers m and n,
     * return the number of `possible unique paths`
     * that the robot can take to reach the` bottom-right corner.`
     * <p>
     * <p>
     * - m x n grid
     * - The robot is initially located
     * at the top-left corner (i.e., grid[0][0])
     * <p>
     * - NOTE:
     * -  CAN only move either
     * - down
     * - right
     * at any point in time.
     * <p>
     * ------------
     * <p>
     * IDEA 1) MATH
     * <p>
     * IDEA 2) BRUTE FORCE
     * <p>
     * IDEA 3) 2D DP
     * <p>
     * - DP def:
     * <p>
     * - BOTTOM UP:
     * <p>
     * dp[i][j]: total possible unique path
     * reach [i, j] cell
     * <p>
     * - TOP DOWN
     * <p>
     * - DP eq:
     * <p>
     * - dp[i][j]  =
     * dp[i-1][j] + dp[i][j-1]
     * <p>
     * <p>
     * <p>
     * ------------
     */
    public int uniquePaths(int m, int n) {
        // edge
        if (m == 1 || n == 1) {
            return 1;
        }

        /**
         *    *    - NOTE:
         *      *       -  CAN only move either
         *      *          - down
         *      *          - right
         *      *          at any point in time.
         */
        // ???
        int[][] dp = new int[m + 1][n + 1];
        // init
        //dp[0][0] = 1;
        // ???
        for (int x = 0; x < n; x++) {
            dp[0][x] = 1;
        }
        // ???
        // ???
        for (int y = 0; y < m; y++) {
            dp[y][0] = 1;
        }


        for (int y = 1; y < m + 1; y++) {
            for (int x = 1; x < n + 1; x++) {
                dp[y][x] = dp[y - 1][x] + dp[y][x - 1];
            }
        }

        // ???
        return dp[m][n];
    }


    // LC 139
    // 10.57 - 11.18 am

    /**
     * -> return
     * true if s can be segmented
     * into a space-separated sequence
     * of one or more dictionary words.
     * <p>
     * <p>
     * - String s
     * - dict: wordDict
     * <p>
     * <p>
     * NOTE:
     * -  `same` word in the dictionary
     * may be `reused multiple times `
     * in the segmentation.
     * <p>
     * -----------
     * <p>
     * <p>
     * IDEA 1) TOP DOWN DP
     * <p>
     * <p>
     * <p>
     * - DP def:
     * boolean dp[i]:
     * if sub string [0, i] of s
     * can be built up from wordDict
     * - DP eq:
     * <p>
     * dp[i] =
     * if s[i-k, i] in wordDict:
     * dp[i] or dp[i - k]
     * <p>
     * <p>
     * IDEA 1-2) BOTTOM UP DP
     * <p>
     * -  DP def:
     * <p>
     * <p>
     * - DP eq:
     * <p>
     * <p>
     * IDEA 2) BFS
     * -----------
     */
    // IDEA 1) TOP DOWN DP ???
    public boolean wordBreak(String s, List<String> wordDict) {
        // edge

        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        //dp[0] = true; // ???

        // ???
        // `double loop ordering` matter ????
        for (int i = n + 1; i > 0; i--) {
            /**
             *      *   - DP eq:
             *      *
             *      *           dp[i] =
             *      *              if s[i-k, i] in wordDict:
             *      *                dp[i] or dp[i - k]
             *
             */
            for (String w : wordDict) {
                String sub = s.substring(i, i - w.length());
//                char[] charArr = s.toCharArray();
//                charArr.
                // ???
                if (wordDict.contains(sub)) {
                    dp[i] = dp[i] || dp[i - sub.length()];
                }
            }
        }

        return dp[0]; // ????
    }


    // LC 322
    // 6.53 - 7.03 am
    /**
     *
     *  -> Return the
     *       - fewest number of coins that you need to make up that amount.
     *       - otherwise, -1
     *       
     *       NOTE:
     *         - You may assume that you have an 
     *           `infinite` number of each kind of coin.
     *
     *
     *
     * -----------------
     *
     *
     *  IDEA 1) BFS
     *  
     *  
     *  
     *  IDEA 2) DP (bottom up)
     *  
     *    - DP def:
     *       - dp[i]: min number of coins sum up = i
     *    
     *    - DP eq:
     *      - dp[i] = 
     *           - if (num - i) in coins:
     *             = dp[i] = min( dp[i],  dp[num - i] + 1 )
     * 
     *  -----------------
     *
     */
    public int coinChange(int[] coins, int amount) {
        // edge
        if (amount == 0)
            return 0;
        
        int[] dp = new int[amount + 1]; // ???
        // init ??
        //Arrays.fill(dp, Integer.MAX_VALUE); // ????
        Arrays.fill(dp, amount + 1); // ????
        //dp[0] = 1;
        dp[0] = 0;

//        HashSet<Integer> set = new HashSet<>();
//        for (int c: coins){
//            set.add(c);
//        }

        // ???
//        for (int c: coins){
//            for(int i = 0; i < amount + 1; i++){
//                if( set.contains(i - c) ){
//                    // dp[i] = min( dp[i],  dp[num - i] + 1 )
//                    dp[i] = Math.min( dp[i],  dp[i - c] + 1 );
//                }
//            }
//        }

        for(int i = 0; i < amount + 1; i++){
            for(int coin: coins){
                if(i - coin >= 0){
                    dp[i] = Math.min( dp[i],  dp[i - coin] + 1 );
                }
            }
        }

       // return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
        return dp[amount] > amount ? -1 : dp[amount];
    }






}
