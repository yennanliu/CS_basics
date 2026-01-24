package dev;

import java.util.*;

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
    public boolean wordBreak_99(String s, List<String> wordDict) {
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



    // LC 140
    // 10.17 - 27 am
    /**
     *
     *  ->  Return all such `possible sentences` in any order.
     *
     *     -  add spaces in s to construct a sentence where
     *        each word is a valid dictionary word
     *
     *    - String s
     *    - word dict
     *
     *
     * -------------
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) DP + DFS ???
     *
     *  IDEA 3) RECURSIVE / DP ???
     *
     *   -------------
     *
     */
    public List<String> wordBreak(String s, List<String> wordDict) {
        // edge

        List<String> res = new ArrayList<>();
        // ??
        for(String w: wordDict){
            workBreak2Helper(0, w.length(), res, "", s, wordDict);
        }

        return res;
    }

    private void workBreak2Helper(int startIdx, int endIdx, List<String> res, String cache, String s, List<String> wordDict){
        // edge
        if(endIdx == s.length() - 1){
            // ???
            res.add(cache);
        }
        if(endIdx > s.length() - 1){
            // ???
            return;
        }

        // validate ???
        //if(en)

        // dfs call
        for(String w: wordDict){
            // size in boundary
            if(endIdx + w.length() < s.length()){
                // sub str is the same
                if(s.substring(endIdx + 1, w.length()).equals(w)){
                    workBreak2Helper(endIdx + 1, endIdx + w.length(), res, cache, s, wordDict);
                }
            }
        }


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


    // LC 14
    // 7.08 - 18 am
    /**
     *  -> Write a function to
     *       find the `longest common prefix string`
     *       amongst an array of strings.
     *    if not, return ""
     *
     *
     *    -------------
     *
     *    IDEA 1) BRUTE FORCE
     *       - get the shortest string
     *
     *
     *    IDEA 2) TRIE ???
     *
     *    IDEA 3) DP ????
     *
     *    IDEA 4) SET + BRUTE FORCE ???
     *
     *
     *    -------------
     *
     *
     */
    //  IDEA 4) SET + BRUTE FORCE ???
    public String longestCommonPrefix(String[] strs) {
        // edge

        // ???
       String first = strs[0];

       // ??
        String cur = "";
        List<String> prefixList = new ArrayList<>();
        for(String x: first.split("")){
            cur += x;
            prefixList.add(cur);
        }

        boolean foundCommonPrefix = false;

        for(String prefix: prefixList){
            for(String s: strs){
                if(prefix.length() > s.length()){
                    continue;
                }
                if(!s.startsWith(prefix)){
                    foundCommonPrefix = false;
                }
            }

            if(foundCommonPrefix){
                return prefix;
            }
        }



        return ""; // ???
    }


    // LC 392
    // 7.48 - 58 am
    /**
     *  -> Given two strings s and t,
     * return true if s is a
     * subsequence of t, or false otherwise.
     *
     *
     * --------------
     *
     * IDEA 1) 2 POINTERS
     * IDEA 2) DP
     *
     *
     * --------------
     *
     *
     */
    // IDEA 1) 2 POINTERS
    public boolean isSubsequence(String s, String t) {
        // edge
        if(s.isEmpty()){
            return true;
        }
        if(t.isEmpty()){
            return false;
        }

        // return true if s is a subsequence of t
        int len_s = s.length();
        int len_t = t.length();

        //int idx_s = 0;
        int idx_t = 0;

        for(int idx_s = 0; idx_s < s.length(); idx_s++){
            while(s.charAt(idx_s) != t.charAt(idx_t) && idx_t < len_t){
                idx_t += 1;
            }
            idx_t += 1;
        }

        return t.charAt(idx_t) == s.charAt(len_s - 1);
    }

    // LC 1143
    // 9.07 - 17 am
    /**
     *  ->  return the length of
     *      their longest common subsequence
     *      (if no common, return 0)
     *
     *      2 str:
     *         - text1, text2
     *
     *    NOTE:
     *      - A common subsequence of two strings is
     *      a subsequence that is common to both strings.
     *
     *  --------------
     *
     *   IDEA 1) 2 POINTERS (SLIDE WINDOW)
     *
     *   IDEA 2) 2d DP
     *
     *    - DP def:
     *
     *        dp[i][j]: longest common subsequence
     *                   when
     *                    - i idx in text1
     *                    - j inx in text2
     *
     *    - DP eq:
     *         dp[i][j] =
     *             if( text1[i] == text1[j] ):
     *                  dp[i][j] = max ( dp[i + 1][j], dp[i][j - 1]  )  + 1 // ????
     *
     *  --------------
     *
     *
     */
    // IDEA 2) 2d DP
    public int longestCommonSubsequence(String text1, String text2) {
        // edge
        if(text1.equals(text2)){
            return text1.length();
        }

        int l1 = text1.length();
        int l2 = text2.length();

        //  int[][] dp = new int[m + 1][n + 1];
        //int[][] dp = new int[l1][l2]; // ????
        int[][] dp = new int[l1 + 1][l2 + 2]; // ????


        // init ?? as 0 ???
        //for()

        /**
         *      *    - DP eq:
         *      *         dp[i][j] =
         *      *             if( text1[i] == text1[j] ):
         *      *                  dp[i][j] = max ( dp[i + 1][j], dp[i][j - 1]  )  + 1 // ????

         */
//        for(int i = 0; i < l1; i++){
//            for(int j = 0; j < l2; j++){
//
//            }
        for(int i = 1; i < l1; i++){
            for(int j = 1; j < l2; j++){
                // ????
                if( j < l1 && i < l2 ){
                    if(text1.charAt(i) == text2.charAt(j)){
                        dp[i][j] = Math.max ( dp[i + 1][j], dp[i][j - 1]  )  + 1;
                    }
                }
            }
        }


        return dp[l1][l2]; // ???
    }



    // IDEA 1) 2 POINTERS (SLIDE WINDOW) (wgon
    public int longestCommonSubsequence_99(String text1, String text2) {
        // edge
        if(text1.equals(text2)){
            return text1.length();
        }

        // assume text1 len > text2 len
        // if NOT, swap text1, text2
        if(text1.length() < text2.length()){
            String tmp = text1;
            text1 = text2;
            text2 = tmp;
        }

        int len1 = text1.length();
        int len2 = text2.length();

        int idx_1 = 0;
        int idx_2 = 0;

        int globalLongest = 0;
        int localLongest = 0;

        // NOTE !!!
        while (idx_1 < len1 && idx_2 < len2){
            // NOTE !!!
            if (text1.charAt(idx_1) == text2.charAt(idx_2)){
                idx_1 += 1;
                localLongest += 1;
                // update global len
                globalLongest = Math.max(globalLongest, localLongest);
            }
            // ???
            // reset
            if(text1.charAt(idx_1) != text2.charAt(idx_2)){
                localLongest = 0;
            }

            idx_2 += 1;
        }

        return globalLongest;
    }


    // LC 377
    // 11.13 - 23 am
    /**
     *  ->  return the number
     *     of possible `combinations` that `add up to target.`
     *
     *
     *
     * --------------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) DP
     *
     *    - DP def:
     *         dp[i]: `combinations` that `add up to target = i
     *
     *    - DP eq:
     *        dp[i] =
     *            if ( i - num  >= 0 )
     *                dp[i - num] + 1 ???
     *
     * --------------
     *
     *
     */
    public int combinationSum4(int[] nums, int target) {
        // edge

        // dp[x] : # of ways to sum up equals `x`
        /**  NOTE !!!
         *
         *  dp[x] : # of ways to sum up equals `x`
         *
         *   ->
         *      dp[0]:   number of ways to reach sum 0
         *      dp[1]:   number of ways to reach sum 1
         *      ...
         *      dp[k]:   number of ways to reach sum k
         *
         */
        int[] dp = new int[target + 1]; // ???
        dp[0] = 1;

        /**
         *     *    - DP eq:
         *      *        dp[i] =
         *      *            if ( i - num  >= 0 )
         *      *                dp[i - num] + 1 ???
         *
         */
        // NOTE !!! we loop
        // to avoid `duplicated combination` // ???
        for(int i = 1; i < target; i++){
            // ???
            for(int num: nums){
                if ( i - num  >= 0 ){
                    dp[i] = dp[i - num] + 1;
                }
            }
        }

        return dp[target];
    }

    // LC 309
    // 15.51 - 16.01 pm
    /**
     *
     *
     *  -> Find the maximum profit you can achieve.
     *
     *
     *   - prices[i]: price on i day
     *
     *    - You may complete as many transactions as you like
     *       (as many buy, sell as you want)
     *
     *    - NOTE:
     *
     *       - After you sell your stock,
     *        you CAN NOT buy stock on the next day
     *        (i.e., cooldown one day).
     *
     *
     *        - Note: You may not engage in multiple
     *           transactions simultaneously
     *         (i.e., you must sell the stock before you buy again).
     *
     *
     *  ---------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2-1) 1D bottom DP
     *
     *      - DP def
     *         - dp[i] = max revenue on day i ???
     *
     *      - DP eq
     *          - dp[i] =
     *               max ( dp[i-1
     *
     *   IDEA 2-2) 2D bottom DP
     *
     *    - DP def
     *       - dp[i][j] = max revenue at day i
     *                   with j op ?
     *                     - buy: 1
     *                     - sell: -1
     *
     *    - DP eq
     *       dp[i][j] =
     *           - if dp[i][j-1]
     *
     *
     * ------------
     *
     *
     *      *
     *      *  2. `int[][] dp = new int[n][3]`
     *      *    -> A `n x 3` 2D array
     *      *    -> n rows, and 3 cols  (NOTE !!!!)
     *      *    e.g.:
     *      *      n is the number of Rows (usually representing Time/Days).
     *      *     3 is the number of Columns (representing your specific States).
     *
     *
     *      *
     *      *         // dp[i][0]: Max profit on day i if we HOLD a stock
     *      *         // dp[i][1]: Max profit on day i if we just SOLD a stock
     *      *         // dp[i][2]: Max profit on day i if we are RESTING (doing nothing)
     *
     *
     *
     *  ---------------
     *
     *   - arr:
     *      - prices[i]
     *
     *
     *   - DP def
     *
     *        *
     *      *         // dp[i][0]: Max profit on day i if we HOLD a stock
     *      *         // dp[i][1]: Max profit on day i if we just SOLD a stock
     *      *         // dp[i][2]: Max profit on day i if we are RESTING (doing nothing)
     *
     *
     *
     *       - dp[i][3] = max profit can get
     *                   at i days
     *
     *                 j:
     *                    - 0: buy
     *                    - 1: sell
     *                    - 2: cooldown
     *
     *   - DP eq
     *       - dp[i][j] =
     *            if  dp[i-1][j] == 0:
     *                dp[i][j] = dp[i-1][j]
     *
     *            if  dp[i-1][j] == 1:
     *               dp[i][j] = ( dp[i-1][j] + prices[i-1] )
     *
     *            if  dp[i-1][j] == 0:
     *               dp[i][j] = max( dp[i-1][j],  dp[i][j] )
     *
     */
    // 17.08 - 18 pm
    // IDEA 2) DP
    public int maxProfit(int[] prices) {

        //int n = prices.length + 1; // ????
        int n = prices.length; // ????

        // init ????
        int[][] dp = new int[n][3];
        //int[][] dp = new int[][]{{1,2}};

        // Base Case: Day 0
        dp[0][0] = -prices[0]; // Bought on day 0
        dp[0][1] = 0; // Can't sell on day 0
        dp[0][2] = 0; // Doing nothing



//        for(int i = 0; i < n + 1; i++){
//            // ??? if `always cooldown` then the profit is always 0
//            dp[i][2] = 0;
//
//            // ??? if `always sell` then the profit is always 0
//            dp[i][1] = 0;
//        }

        int maxProfix = 0;

        /**
         *
         *      *      *         // dp[i][0]: Max profit on day i if we HOLD a stock
         *      *      *         // dp[i][1]: Max profit on day i if we just SOLD a stock
         *      *      *         // dp[i][2]: Max profit on day i if we are RESTING (doing nothing)
         *      *
         *
         */
        for(int i = 1; i < n; i++){
            // ??? loop over `op` ???
            for(int j = 0; j < 3; j++){

                // buy ???
                int tmp1 = dp[i-1][2] + prices[i-1];

                // sell ???
                int tmp2 = dp[i-1][0] + prices[i-1];

                // cooldown ???
                int tmp3 = Math.max(dp[i-1][1], dp[i-1][2]);

                dp[i][j] = Math.max(tmp1,
                        Math.max(tmp2, tmp3)
                );

                // ??
                maxProfix = Math.max(maxProfix, dp[i][j]);
            }
        }


        return maxProfix;
    }







    // LC 494
    // 16.34 - 44 pm
    /**
     *  -> Return the `number` of
     *    `different expressions` that you can build,
     *    which evaluates to target.
     *
     *
     *     - nums: int array
     *     - target
     *
     *  -----------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) 2D bottom up DP ??
     *
     *      DP def:
     *
     *          n x 2
     *
     *         - dp[i][j]:
     *             - # of different expressions sum up
     *             to `i`
     *
     *             - j: cur sum ??
     *
     *      DP eq:
     *
     *        dp[i][j] =
     *           if  dp[i - 1][1] < target:
     *               max (dp[i - 1][1], dp[i - 1][j])
     *
     *
     *
     *
     *
     *  -----------------
     *
     *
     */
    public int findTargetSumWays(int[] nums, int target) {

        return 0;
    }


    // LC 97
    // 2.29 - 3.10 pm
    /**
     *
     *   -> An interleaving of two strings s and t is a
     *   configuration where s and t are divided
     *   into n and m substrings respectively
     *
     *
     *   -------------
     *
     *    IDEA 1) DP ??
     *
     *    IDEA 2) brute force ??
     *
     *   -------------
     *
     *
     */
    public boolean isInterleave(String s1, String s2, String s3) {

        return false;
    }


    // LC 329
    // 3.33 - 43 pm
    /**
     *  ->  return the length of the `longest` `increasing `
     *      path in matrix.
     *
     *     - m x n matrix
     *     -
     *
     *   NOTE:
     *
     *    - you can either move in four directions:
     *       - left, right, up, or down
     *
     *    - CAN'T move
     *       - out of boundary
     *       - move diagonally
     *
     *  ---------------
     *
     *    IDEA 1) MULTI SOURCE BFS  ????
     *
     *    IDEA 2) 2D DP ???
     *
     *      -> so we need to loop all cells in matrix,
     *         and calculate below dp:
     *
     *      - DP def:
     *
     *         - dp[i][j]: max longest` `increasing `
     *                     path `at [i, j]` ????   (NOT start from (i,j))
     *
     *      - DP eq:
     *
     *         - dp[i][j] =
     *              if ( matrix[i][j] > matrix[i-1][j] || matrix[i][j] > matrix[i][j-1] ):
     *                 max(dp[i-1][j] , dp[i][j-1]) + 1
     *
     *   ---------------
     *
     *
     *
     */
    // dfs + dp ???
    // 4.28 - 38 pm
    // ???
    public int longestIncreasingPath(int[][] matrix) {
        // edge
        if (matrix == null || matrix.length == 0)
            return 0;


        int l = matrix.length;
        int w = matrix[0].length;

       // int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int maxIncreaseLen = 0; // 1;

        // ???
        int[][] memo = new int[l][w];

        // ???
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                maxIncreaseLen = Math.max(
                        maxIncreaseLen,
                        //dfsIncreasePath(x, y, matrix, memo, 1)
                        dfsIncreasePath(x, y, matrix, memo)
                );
            }
        }


        return maxIncreaseLen;
    }

    private int dfsIncreasePath(int x, int y, int[][] matrix,  int[][] memo){
        // edge
        if(memo[y][x] != -1){
            return memo[y][x];
        }

        int l = matrix.length;
        int w = matrix[0].length;

        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        int max = 1;

        for(int[] m: moves){
            int x_ = x + m[1];
            int y_ = y + m[0];
            // validate
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l){
                // ???
                if(matrix[y_][x_] > matrix[y][x]){
                    // ?????
                    //dfsIncreasePath(x_, y_, matrix, memo, moveLen + 1);
                    int len = 1 + dfsIncreasePath(x_, y_, matrix, memo);
                    max = Math.max(max, len);
                }
            }
        }

        // update memo
        memo[y][x] = max;
        return max;
    }







    public int longestIncreasingPath_99(int[][] matrix) {
        // edge

        int l = matrix.length;
        int w = matrix[0].length;

        // ???
        int[][] dp = new int[l][w]; // ??
        // init
        for(int x = 0; x < w; x++){
            dp[0][x] = 1;
        }
        for(int y = 0; y < l; y++){
            dp[y][0] = 1;
        }

        // ???
        int maxIncreaseLen = 1;
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // ????
                if ( matrix[y][x] > matrix[y-1][x] || matrix[y][x] > matrix[y][x-1] ){
                    dp[y][x] = Math.max(dp[y-1][x] , dp[y][x-1]) + 1;
                    maxIncreaseLen = Math.max(maxIncreaseLen, dp[y][x]);
                }
            }
        }

        return maxIncreaseLen;
    }




     // LC 115
     // 4.48 - 5.08 pm
     /**
      *  -> return the number of
      *     distinct `subsequences of s`
      *        - which equals t.
      *
      *   2 str: s, t
      *
      *
      *
      *  ------------
      *
      *    IDEA 1) 2D DP ???
      *
      *      - DP def:
      *
      *         - boolean dp[i][j]
      *             - can s[0..i] be the sub str in
      *                   t[0...j] ???
      *
      *      - DP eq:
      *
      *
      *    IDEA 2) BRUTE FORCE
      *       - double loop
      *
      *   ------------
      *
      *
      *   ----------
      *
      *   - IDEA 1) 2D DP
      *
      *    - DP def:
      *
      *      - dp[i][j]
      *        - # of distinct subsequences
      *          for s[0... i] equals to t[0... j]
      *
      *    - DP eq
      *
      *       dp[i][j]  =
      *           if s[i] == t[i]:
      *               dp[i-1][j-1]
      *
      *         else:
      *             min( dp[i][j],  dp[i-1][j-1] )
      *
      *
      *
      */
     // 18.01 - 11 pm
     // 2D DP
     /**
      *  - DP def
      *
      *     dp[i][j]:
      *        # of distinct subsequences of s which equals t.
      *        for s[0..i], and t[0...j]
      *
      *  - DP eq:
      *
      *       dp[i][j] =
      *
      *         if s[i] == t[j]:
      *             dp[i][j] =  dp[i-1][j-1]
      *
      *         else:
      *           dp[i][j] =
      *             max( dp[i][j-1], dp[i-1][j]  )
      *
      *
      *
      *
      *
      *
      */
     public int numDistinct(String s, String t) {


         return 0;
     }





     public int numDistinct_99(String s, String t) {
         // edge ???

         int s_len = s.length();
         int t_len = t.length();

         int [][] dp = new int[s_len + 1][t_len + 1]; // ???

         // ??? init
         // NOTE !!! end at `i <= s_len`
         for(int i = 0; i <= s_len; i++){
             dp[i][0] = 1;
         }

//         for(int j = 0; j < t_len; j++){
//             dp[0][j] = 1;
//         }



         /**
          *       *       dp[i][j]  =
          *       *           if s[i] == t[i]:
          *       *               dp[i-1][j-1]
          *       *
          *       *         else:
          *       *             min( dp[i][j],  dp[i-1][j-1] )
          *
          *
          */

         // ???
         // NOTE !!!
         // 1. start from i=1, j=1
         //
         for(int i = 1; i < s_len + 1; i++){
             for(int j = 1; j < t_len + 1; j++){
                 // ???
                 if(s.charAt(i) == t.charAt(j)){
                     dp[i][j] = dp[i-1][j-1];
                 }else{
                     dp[i][j] = Math.min( dp[i][j],  dp[i-1][j-1] );
                 }
             }
         }


         return dp[s_len][t_len];
    }







    // LC 72
    // 15.53 - 16.03 pm
    /**
     *
     *  ->  return the `minimum` number
     *     of operations required to
     *  `  convert word1 to word2`
     *
     *    - 2 str: w1, w2
     *
     *    - can do op below:
     *       - insert
     *       - delete
     *       - replace
     *
     *  -------------
     *
     *   IDEA 0) GREEDY ???
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) 2D DP ???
     *
     *     - DP def:
     *
     *         (2D)
     *        - dp[i][j]:
     *            min op make
     *            w1, w2 are equal at idx = i ???
     *
     *
     *         - j = 0: insert
     *             = 1: delete
     *             = 2: replace
     *             ???
     *
     *         - i:
     *
     *
     *
     *
     *     - DP eq:
     *
     *
     * -------------
     *
     *
     *
     */
    // 10.39 - 49 am
    /**
     *   IDEA 2) 2D DP ???
     *
     *      - DP def:
     *
     *        dp[i][3]
     *
     *        -  dp[i][j]:
     *            min op convert
     *              w1[0..i]
     *              to
     *              w2[0..i]
     *
     *        j = 0: insert
     *        j = 1: delete
     *        j = 2: replace
     *
     *      - DP eq:
     *
     *        dp[i][j] =
     *             // insert
     *             min(dp[i][j], dp[i][j])
     *
     *             // delete
     *             // replace
     *
     *
     */

    /**  NOTE !!!
     *
     *  DP def:
     *
     *   dp[i][j]
     *      - the `minimum` operations to convert
     *        `word1.substring(0, i) `
     *        to
     *        `word2.substring(0, j).`
     *
     */
    public int minDistance(String word1, String word2) {
        // edge

        int l1 = word1.length();
        int l2 = word2.length();

        int[][] dp = new int[l1 + 1][l2 + 1]; // ???
        // init  ???
//        for(int i = 0; i < l1; i++){
//            dp[i][2] = 0;
//        }
        dp[0][0] = 0; //???

        // ??????
        for(int i = 1; i < l1 + 1; i++){
            for(int j = 1; j < l2 + 1; j++){
                // case 1) if equals
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    // no op needed
                    dp[i][j] =  dp[i-1][j-1]; /// ?????
                }
                // case 2) if NOT equals
                else{
                    // insert
                    //int op1 = dp[i-1][j+1] + 1; // ????
                    int op1 = dp[i][j - 1] + 1;
                    // delete
                    int op2 = dp[i-1][j] + 1;
                    // replace
                    int op3 = dp[i-1][j-1] + 1;

                    dp[i][j] = Math.min(op1,
                            Math.min(op2, op3));
                }
            }
        }

        // /???
        return dp[l1][l2];
    }








    public int minDistance_99(String word1, String word2) {
        // edge

        int l1 = word1.length();
        int l2 = word2.length();

        // ???
        int[][] dp = new int[l1][l2];

        // ????
        for(int i = 0; i < l1; i++){
            for(int j = 0; j < l2; j++){
                dp[i][j] = 1000; // ????
            }
        }

        // init
        // case 1) if w2 is null ???
        for(int i = 0; i < l1; i++){
            dp[i][0] = i;
        }

        // case 2) if w1 is null ???
        for(int j = 0; j < l2; j++){
            dp[0][j] = j;
        }

        int minOp = 1000; // ????

        // ??
        for(int i = 0; i < l1; i++){
            for(int j = 0; j < l2; j++){

                /** NOTE !!!
                 *
                 *  if same,
                 */
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // Match: Just take the previous best
                    dp[i][j] = dp[i - 1][j - 1];
                }else{

                    // case 1) replace
                    // ??
                    dp[i][j] = Math.min(dp[i-1][j] + 1, dp[i][j]);

                    // case 2) delete

                    // case 3) insert
                }

//                // ????
//                if(word1.charAt(i) != word1.charAt(j)){
//                    // case 1) replace
//                    // ??
//                    dp[i][j] = Math.min(dp[i-1][j] + 1, dp[i][j]);
//
//                    // case 2) delete
//
//                    // case 3) insert
//                }

                // ???? start update minOp
                if(i >= l2){
                    // ???
                    minOp = Math.min(minOp, dp[i][j]);
                }
            }
        }


         return minOp; // ???
    }


    // LC 312
    // 8.51 - 9.01
    /**
     *
     *  -> Return the maximum coins
     *  you can collect by bursting the balloons wisely.
     *
     *
     *  -----------------
     *
     *   IDEA 1) GREEDY / BRUTE FORCE
     *
     *   IDEA 2) 2D DP
     *
     *      - DP def:
     *         - dp[i][j]:
     *            -
     *
     *
     *      - DP eq:
     *
     *  -----------------
     *
     *
     */
    public int maxCoins(int[] nums) {

        return 0;
    }


    // LC 10
    // 9.19 - 29 am
    /**
     *
     *  -> implement regular expression
     *     matching with support for '.' and '*':

     *      - '.' Matches any single character.
     *      -  '*' Matches zero or more of the preceding element.
     *
     *
     *      - s: str
     *      - p: pattern
     *
     *      NOTE:
     *        - The matching should cover the
     *        `entire` input string (not partial).
     *
     * ---------------
     *
     *    *
     *      *   IDEA 1) GREEDY / BRUTE FORCE
     *      *
     *      *   IDEA 2) 2D DP
     *      *
     *      *      - DP def:
     *      *         - boolean dp[i][j]:
     *      *            - can s(0..i) be fully
     *                     presented by pattern p(0..j) ???
     *      *
     *      *
     *      *      - DP eq:
     *
     *                dp[i][j] =
     *                    if ( s[i] == p[i] or p[i] == "." or p[i] == "*" ):
     *                        dp[i-1][j-1] or dp[i][j]  // ???
     *                    else:
     *                       // ???
     *
     * ---------------
     *
     *
     */
    public boolean isMatch(String s, String p) {
        // edge

        int s_len = s.length();
        int p_len = p.length();

        // default val is `false` ???
        boolean[][] dp = new boolean[s_len + 1][p_len + 1];

        // ??
        /**
         *      *      *      - DP def:
         *      *      *         - boolean dp[i][j]:
         *      *      *            - can s(0..i) be fully
         *      *                     presented by pattern p(0..j) ???
         *      *      *
         *      *      *
         *      *      *      - DP eq:
         *      *
         *      *                dp[i][j] =
         *      *                    if ( s[i] == p[i] or p[i] == "." or p[i] == "*" ):
         *      *                        dp[i-1][j-1] or dp[i][j]  // ???
         *      *                    else:
         *      *                       // ???
         *
         */

        // ???
        for(int i = 0; i < s_len + 1; i++){
            for(int j = 0; j < p_len + 1; j++){
                if ( s.charAt(i) == p.charAt(i) || p.charAt(i) == '.' || p.charAt(i) == '*'){

                    dp[i][j] = dp[i-1][j-1] || dp[i][j]; // ???
                }
            }
        }


        return dp[s_len][p_len];
    }




    // LC 55
    // 9.12 -27 am
    /**
     *
     * -> Return true if you can reach the last index,
     *   or false otherwise.
     *
     * -----------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) DP
     *
     *   - DP def:
     *        - dp[i]: max step can jump from idx = i
     *
     *   - DP eq:
     *        dp[i] =
     *           max( dp[i-1],  nums[i] ) // ????
     *
     *
     * -----------------
     *
     */
    public boolean canJump(int[] nums) {
        // edge

        int n = nums.length;

        int[] dp = new int[n]; // ???
        // ??? init as -1 ???
        Arrays.fill(dp, -1);
        dp[0] = nums[0]; // /?

        for(int i = 1; i < n; i++){

            // NOTE !!!
            if (dp[i - 1] < i){
                return false;
            }

            dp[i] = Math.max( dp[i - 1], nums[i]  + i );
        }

        //return dp[n - 1] > 0; // /???
        return dp[n - 1] >= n - 1;
    }



    // LC 45
    // 10.07 - 17 am
    /**
     *  -> Return the `minimum` number of jumps to reach index n - 1.
     *    The test cases are generated such that you can reach index n - 1.
     *
     *
     *  -------------
     *
     *   IDEA 1) GREEDY
     *
     *   IDEA 2) DP
     *
     *
     *   -------------
     *
     */
    // IDEA 2) DP
    public int jump(int[] nums) {
        // edge

        int n = nums.length;

        // dp[i]:
        // the max `absolute index` can reach
        // at index = i
        int[] dp = new int[n];
        dp[0] = nums[0];

        for(int i = 1; i < nums.length; i++){
            // if already at the `end`
            if(i == n-1){
                return i;
            }

//            // should NOT happen in this LC (jump game 2)
//            if(dp[i-1] < i){
//                return -1;
//            }

            dp[i] = Math.max(dp[i-1], i + nums[i]);

            // if can reach the `end` in next jump
            if(dp[i] >= n - i){
                return i + 1;
            }
        }

        // worst case, jump `1` every time ???
        return n-1; // ????
    }



    // LC 134
    // 16.13 - 23 pm
    /**
     *
     * ->  return
     *       - starting gas station's index if you can travel around the
     *        circuit once in the clockwise direction,
     *      - otherwise return -1
     *
     *    (Given two integer arrays gas and cost)
     *    e.g. given:
     *      - gas[i]
     *      - cost
     *
     *
     *    - n gas stations along a `circular` route
     *    - gas amount = gas[i] at idx = i
     *
     *    - cost:
     *      cost[i] of gas to
     *         travel from the `ith station` to
     *         its `next` (i + 1)th station
     *
     *  ------------
     *
     *   IDEA 1) GREEDY
     *     - hashset record the `visited` idx
     *     - maintain an array
     *        arr[i] = can_move_dist
     *
     *        loop over idx:
     *         if can_move_dist < 0, reset it as 0,
     *         continue the looping
     *
     *
     *   IDEA 2) BRUTE FORCE
     *
     *  ------------
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        // edge
        if (gas == null || cost == null || gas.length == 0 || cost.length == 0){
            return 0;
        }

//        if(gas == null){
//            return -1;
//        }
//        if(gas.length == 1){
//            return 0;
//        }


        int n = gas.length;
        int totalGain = 0;

        for(int g: gas){
            totalGain += g;
        }

        // ????
        int curGain = 0;
        int remain = 0;

        for(int i = 0; i < n; i++){
            // ???
            remain += (gas[i] - cost[i]);
            curGain += (gas[i] - cost[i]);
            // ???
            if(curGain >= -1 * (totalGain - curGain)){
                return i;
            }
            //????
            if(remain < 0){
                remain = 0; // ???

            }
        }

        return -1;
    }






    public int canCompleteCircuit_99(int[] gas, int[] cost) {
        // edge
        if(gas == null){
            return -1;
        }
        if(gas.length == 1){
            return 0;
        }

        HashSet<Integer> visited = new HashSet<>();
        int n = gas.length;
        int[] canMove = new int[2 * n]; // ???

        canMove[0] = gas[0];

        //??
        //List<Integer> list = new ArrayList<>();
        for(int j = 0; j < 2; j++){
            for(int i = 1; i < n; i++){
                if(visited.contains(i)){
                    return i;
                }
                int move = canMove[i-1] - cost[i-1] + canMove[i];
                if(move < 0){
                    move = 0;
                }
                canMove[i] = move;
                visited.add(i);
            }
        }

        // ???
        return -1;
    }




}
