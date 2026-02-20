package dev;

import LeetCodeJava.DataStructure.TreeNode;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

import javax.print.DocFlavor;
import java.lang.reflect.Array;
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

    // 17.10 - 27 pm
    /**
     *
     *  ->  return true
     *        - if you can partition the array
     *          into two subsets such that the sum of
     *           the elements in both subsets is equal
     *        - false otherwise.
     *
     *
     *  -----------------
     *
     *   IDEA 1) 1D DP
     *
     *    - bottom up
     *
     *    - DP def:
     *       boolean dp[i]: if nums can sum as `i`
     *
     *    - DP eq:
     *
     *      dp[i] =
     *         if sum - dp[i] == true
     *            dp[i] = dp[ sum - dp[i] ] or dp[i]
     *
     *  -----------------
     */

    // 11.56 - 12.06 pm
    /**
     *
     *  ->  return true
     *    if you can partition the array into two subsets
     *    such that the sum of the elements
     *    in both subsets is equal or false otherwise.
     *
     *
     *   IDEA 1) DP
     *
     *
     */
    public boolean canPartition(int[] nums) {
        //

        return false;
    }





    public boolean canPartition_98(int[] nums) {
        //edge
        int sum = 0;
        int n = nums.length;

       // Integer[] nums2 = new Integer[n];

        for(int i = 0; i < n; i++){
            int x = nums[i];
            sum += x;

           // nums2[i] = x;
        }

//        // ?? need sorting ???
//        Arrays.sort(nums2, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        });

        // dp
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1]; // ???
        dp[0] = true; // ??

        // ???
        for(int x: nums){
            // ???
            for(int t = target; t >= x; t--){
                if(dp[t - x]){
                    dp[t] = true;
                }
            }
        }


        // ???
        return dp[target];
    }




//    public boolean canPartition(int[] nums) {
//        //edge
//        int sum = 0;
//        int n = nums.length;
//
//        Integer[] nums2 = new Integer[n];
//
//        for(int i = 0; i < n; i++){
//            int x = nums[i];
//            sum += x;
//
//            nums2[i] = x;
//        }
//
//        // ?? need sorting ???
//        Arrays.sort(nums2, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        });
//
//        // dp
//        boolean[] dp = new boolean[sum + 1]; // ???
//        dp[0] = true; // ??
//
//        // ???
//        int cumsum = 0;
//        for(int x: nums2){
//            cumsum += x;
//            // ???
//            if(dp[x] || dp[sum - cumsum]){
//                dp[x] = true;
//            }
//        }
//
//        // ???
//        return dp[sum];
//    }






    public boolean canPartition_99(int[] nums) {
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
    // LC 322
    // 10.49 - 59 AM
    // // IDEA: bottom up DP ("backward-looking" approach) (fixed by gemini)
    /**
     *  - IDEA: bottom DP
     *
     *   - DP def
     *       - dp[i]: min coin to represent sum = i
     *
     *   - DP eq:
     *      - dp[i] =
     *          if (i < amount):  // ????
     *             min( dp[i-coin] + 1, dp[i] )
     */
    public int coinChange(int[] coins, int amount) {
        // edge

        int[] dp = new int[amount + 1]; // ???
        //Arrays.fill(dp, Integer.MAX_VALUE); // ???
        Arrays.fill(dp, amount + 1); // ???
        dp[0] = 0;


        // NOTE !!! double loop
        // loop 1) : loop over amount
        for(int i = 1; i < amount + 1; i++){
            // loop 2) : loop over coin:
            // ??? loop back ???
            for(int c: coins){
                // ??
                dp[i] = Math.max(dp[i], dp[i - c] + 1); // ?????
            }
        }

        return dp[amount] != amount + 1 ? dp[amount] : -1;
    }








    public int coinChange_99(int[] coins, int amount) {
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
    /**
     *
     * -> Write a function to find the longest
     *    common prefix string amongst an array of strings.
     *    - if no one, return ""
     *
     *
     *  ------------------
     *
     *   IDEA 1) TRIE ??
     *
     *   IDEA 2) BRUTE FORCE + STR OP ???
     *
     *      steps:
     *        1. get 1st str and loop over all its elements
     *           and `cumsum` them and add to a list: prefixList
     *
     *          e.g.  "flower"
     *
     *          -> [f, fl, flo, flow, flowe, flower]
     *
     *       2. loop over all str
     *          and check if any of the prefix in prefixList
     *          can `satisfy` all str,
     *          and maintain the longestPrefix
     *
     *       3. return the longestPrefix as ans
     *
     *  ------------------
     *
     *
     */
    // 16.10 - 20 pm
    // DEA 2) BRUTE FORCE + STR OP ???
    public String longestCommonPrefix(String[] strs) {
        // edge
        if(strs == null || strs.length == 0){
            return "";
        }
        if(strs.length == 1){
            return strs[0];
        }

        String first = strs[0];
        List<String> prefixList = new ArrayList<>();
        String tmp = "";
        for(String x: first.split("")){
            tmp += x;
            prefixList.add(tmp);
        }

        System.out.println(">>> prefixList = " + prefixList);
        String longestPrefix = "";

        int n = strs.length;

        // ???
        for(String prefix: prefixList){
            //boolean allMatched = true;
            int cnt = 0;
            for(String str: strs){
                if(!str.startsWith(prefix)){
                    //allMatched = false;
                    break;
                }
                cnt += 1;
            }
            if(cnt == n){
                longestPrefix = prefix;
            }
        }


        return longestPrefix;
    }









    //  IDEA 4) SET + BRUTE FORCE ???
    public String longestCommonPrefix_99(String[] strs) {
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
    // 11.09 - 19 am
    public boolean isSubsequence(String s, String t) {
        // edge
        if(s.isEmpty()){
            return true;
        }
        if(t.isEmpty()){
            return false;
        }
        // ???
        if(s.equals(t)){
            return true;
        }

        int s_idx = 0;
        int t_idx = 0;

        while (s_idx < s.length() && t_idx < t.length()){
            // if reach the `last idx` in s
//            if(s_idx == s.length() - 1){
//                while (t_idx < t.length()){
//                    if(s.charAt(s_idx) == t.charAt(t_idx)){
//                        return true;
//                    }
//                    t_idx += 1;
//                }
//                return false;
//            }

            System.out.println(">>> s.charAt(s_idx) = " + s.charAt(s_idx) +
                   ", t.charAt(t_idx) = " + t.charAt(t_idx) );


            if(s.charAt(s_idx) == t.charAt(t_idx)){
                // ??? early quit
                if(s_idx == s.length() - 1){
                    return true;
                }
                s_idx += 1;
            }

            t_idx += 1;
        }


//        while (t_idx < t.length()){
//            if(s.charAt(s_idx) == t.charAt(t_idx)){
//                return true;
//            }
//            t_idx += 1;
//        }

        return false;
    }







    // IDEA 1) 2 POINTERS
    public boolean isSubsequence_99(String s, String t) {
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
    // LC 1143
    // 10.46 - 56 AM
    /**
     *  -----------
     *
     *
     *  IDEA 1) 2D DP
     *
     *   - DP def:
*          dp[i][j]:
     *         if text1(0...i) is subsequence of
     *         text2(0...j)
     *
     *
     *   - DP eq:
     *
     *  -----------
     *
     *
     */
    /** NOTE !!!
     *
     *  DP def:
     *
     *  dp[i][j]:
     *    - LCS of text1.substring(0, i)
     *      and text2.substring(0, j)
     *
     */
    public int longestCommonSubsequence(String text1, String text2) {
        // edge

        int l1 = text1.length();
        int l2 = text2.length();

        int[][] dp = new int[l1 + 1][l2 + 1];
        Arrays.fill(dp, -1); // ????

        // init ???
        int maxLCS = 0;

//        // if text2 is null
//        for(int i = 0; i < l1; i++){
//            dp[i][0] = 0; // ???
//        }
//        // if text1 is null
//        for(int i = 0; i < l2; i++){
//            dp[0][i] = 0; // ???
//        }

        // ???
//        for(int i = 1; i < l1; i++){
//            for(int j = 1; j < l2; j++){
//
//            }
        for(int i = 1; i < l1 + 1; i++){
            for(int j = 1; j < l2 + 1; j++){
                // if(text1.charAt(i) == text2.charAt(j)){
                if(text1.charAt(i - 1) == text2.charAt(j - 1)){
                    dp[i][j] = dp[i-1][j-1] + 1; //Math.max(dp[i-1][j], dp[i][j-1]) + 1; // ??? //dp[i-1][j-1] + 1;
                    //maxLCS = Math.max(maxLCS, dp[i][j]);
                }else{
                    // ?????
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]) + 1; //Math.max(dp[i-1][j-1] + 1, dp[i][j]);
                }
                maxLCS = Math.max(maxLCS, dp[i][j]);
            }
        }

        //return maxLCS;
        return dp[l1][l2];
    }








    // IDEA 2) 2d DP
    public int longestCommonSubsequence_97(String text1, String text2) {
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
    // 15.48 - 16.06 pm
    /**
     *  -------------
     *
     *
     *  IDEA 1) 2D DP
     *
     *      *       - dp[i][3] = max profit can get
     *      *                   at i days
     *      *
     *      *                 j:
     *      *                    - 0: buy
     *      *                    - 1: sell
     *      *                    - 2: cooldown
     *
     *  -------------
     *
     */
    public int maxProfit(int[] prices) {
        // edge
        if (prices == null || prices.length <= 1)
            return 0;

        int n = prices.length; // ????
        //int maxProfit = 0;

        // ???
        //int[][] dp = new int[n + 1][3];
        int[][] dp = new int[n][3];



        // init // ???
        dp[0][0] = -1 * prices[0]; // ???
        dp[0][1] = 0;
        dp[0][2] = 0;

        // for(int i = 0; i < n + 1; i++){
        for(int i = 0; i < n; i++){
            // buy
            //dp[i][0] = dp[i-1][2] - prices[i];
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][2]) - prices[i];

            // sell
            //dp[i][1] = dp[i-1][0] + prices[i-1]; // /??
            dp[i][1] = dp[i-1][0] + prices[i]; // /??

            // hold
            dp[i][2] = Math.max(dp[i-1][1], dp[i-1][2]);
        }

        //return Math.max(dp[n][1], dp[n][2]);
        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }







    // 17.08 - 18 pm
    // IDEA 2) DP
    public int maxProfit_99(int[] prices) {

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
    // 7.05 - 15 am
    // 2D DP
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
     *
     *  DP eq:
     *
     *    dp[i][j] =
     *       // if insert
     *       // if delete
     *       // if replace
     *
     *
     */
    public int minDistance(String word1, String word2) {
        // edge

        int l1 = word1.length();
        int l2 = word2.length();

        // ?????
        //int[][] dp = new int[l1][l2];
        int[][] dp = new int[l1 + 1][l2 + 1];

        // init ??
        // if w2 is null
        for(int i = 0; i < l1 + 1; i++){
            dp[i][0] = i;
        }
        // if w1 is null
        for(int i = 0; i < l2 + 1; i++){
            dp[0][i] = i;
        }

        // ??
        /**
         *          *
         *          *       - Replace: dp[i-1][j-1] + 1 (Diagonal)
         *          *
         *          *       - Delete: dp[i-1][j] + 1 (Top)
         *          *
         *          *       -  Insert: dp[i][j-1] + 1 (Left)
         */
        for(int i = 0; i < l1 + 1; i++){
            for(int j = 0; j < l2 + 1; j++){
                // insert
                int val1 = dp[i][j-1] + 1; // dp[i][j+1] + 1; // ???
                // delete
                int val2 = dp[i-1][j] + 1;
                // replace
                int val3 = dp[i-1][j-1] + 1; //dp[i][j-1] + 1;

                // ??
                dp[i][j] = Math.min(val1, Math.max(val2, val3));
            }
        }


        //return dp[l1 - 1][l2 - 1];
        return dp[l1][l2];
    }







    public int minDistance_98(String word1, String word2) {
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


    // LC 846
    // 16.51 - 17.01 pm
    /**
     *  -> return true if she can rearrange the cards,
     *    or false otherwise.
     *
     * -------------------
     *
     *  IDEA 1)  TREEMAP
     *
     *  IDEA 2)  ORDER MAP ???
     *
     *   -> freq map
     *   -> loop over groupSize
     *      -> call map's `next key`
     *         to get the `next big` key
     *         if key NOT existed:
     *            return false
     *         else:
     *            update freq map
     *            and continue loop
     *
     *  IDEA 3) BRUTE FORCE ???
     *
     *
     * -------------------
     *
     *  ex 1)
     *
     *   Input: hand = [1,2,3,6,2,3,4,7,8],
     *   groupSize = 3
     *
     *   -> map = {1: 1, 2: 2, 3: 2, 4: 1, 6:1, 7: 1, 8: 1}
     *
     *
     */
    public boolean isNStraightHand(int[] hand, int groupSize) {
        // edge
        int n = hand.length;
        if(n % groupSize != 0){
            return false;
        }

        // ???
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int h: hand){
            map.put(h, map.getOrDefault(h, 0) + 1);
        }

        System.out.println(">>> map = " + map);

        int key = map.firstKey(); // ????

        // ???
        int loop = n / groupSize;
        for(int i = 0; i < loop; i++){
            //key = map.firstKey(); // ???
            for(int j = 0; j < groupSize; j++){
                //int key = map.firstKey(); // ???
                if(!map.containsKey(key)){
                    return false;
                }
                int newCnt = map.get(key) - 1;
                if(newCnt <= 0){
                    map.remove(key);
                }
                map.put(key, newCnt);
                key += 1; // ????
            }
        }

        /**
         *      *  IDEA 2)  ORDER MAP ???
         *      *
         *      *   -> freq map
         *      *   -> loop over groupSize
         *      *      -> call map's `next key`
         *      *         to get the `next big` key
         *      *         if key NOT existed:
         *      *            return false
         *      *         else:
         *      *            update freq map
         *      *            and continue loop
         */

        return true;
    }


    // LC 367
    // 17.28 - 38 pm
    /**
     *
     *  ->  return true if num is a `perfect square `
     *    or false otherwise.
     *
     *   -  `perfect square `:
     *        is an integer that is the square of an integer.
     *        In other words, it is the product of
     *        some integer with itself.
     *
     *  ----------------
     *
     *   IDEA 1) MATH
     *
     *   IDEA 2) BINARY SEARCH
     *
     *   IDEA 3) BRUTE FORCE
     *
     *
     *  ----------------
     */
    // IDEA 2) BINARY SEARCH - find `left boundary`
    public boolean isPerfectSquare(int num) {
        //edge
        if(num < 0){
            return false;
        }
        if(num <= 1){
            return true;
        }

        int l = 0;
        int r = (int) Math.sqrt(num) + 1; // ??
        System.out.println(">>> l = " + l +
                ", r = " + r);

        // find
        while(r >= l){
            int mid = ( l + r ) / 2;
            int square = mid * mid;
            if(square == num){
                return true;
            }
            if(square < num){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        return false;
    }


    // LC 875
    // 17.36 - 46 pm
    /**
     * -> Return the minimum integer k such that
     *   she can eat all the bananas within h hours.
     *
     *   - but still wants to finish eating all the
     *     bananas before the guards return.
     *
     *  --------------
     *
     *   IDEA 1) BINARY SEARCH
     *
     *
     *   --------------
     *
     */
    public int minEatingSpeed(int[] piles, int h) {
        // edge
        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

//        int sum = 0;
//        for(int p: piles){
//            sum += p;
//        }

        // binary search: `speed`
        // ??
        int l = 1;
        int r = 0; // ??
        for (int p : piles) {
            r = Math.max(r, p); // Maximum pile size is the upper bound
        }

        int minSpeed = r; // ??

        while(r >= l){
            int mid = (l + r) / 2;
            int hours = getHour(piles, mid);
            if(hours == h){
                //minSpeed =
                // ???
                //return mid;
                minSpeed = mid; /// ????
                //r -= 1;
                r = mid - 1;
            }
            else if(hours > h){
                l = mid + 1;
            }
            // ???
            else{
                // ???
               // r -= 1;
               r = mid - 1;
            }
        }

        return minSpeed;
    }


    /**  NOTE !!!
     *
     *  below code is `logically correct`
     *  but may cause `overflow issue`
     */
    private int getHour(int[] piles, int speed){
        int hours = 0;
        for(int p: piles){
            // ???
            int v1 = p / speed;
            int v2 = p % speed;
            if(v2 != 0){
                v1 += 1;
            }
            hours += v1;
        }
        return hours;
    }


    // LC 1899
    // 14.31 - 41 pm
    /**
     *  -> Return `true`
     *      - if it is possible to obtain the target triplet [x, y, z] as an element of triplets,
     *      - false otherwise.
     *
     *
     *    - triplet: arr with 3 int
     *    - 2D arr: triplets
     *            - triplets[i] = [ai, bi, ci]
     *
     *    - target = [x, y, z]
     *
     *
     *    - OP:
     *       To obtain target, you may apply the following operation
     *        on triplets ` any number of times (possibly zero):`
     *
     *         - choose 2 indices (i, j) (i != j)
     *           - update triplets[j] as
     *               - [max(ai, aj), max(bi, bj), max(ci, cj)]
     *
     *
     *  ----------------
     *
     *   IDEA 1) GREEDY ???
     *
     *     -> 1. loop over triplet in triplets,
     *        2. get and update max val in every index (e.g. index = 0, 1, 2)
     *        3. finally, check if the `final array` is as same as target
     *
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *
     *  ----------------
     *
     */
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        // edge
        if(triplets == null && target == null){
            return false;
        }
        // ??
        if(triplets == null){
            return false;
        }
        for(int[] x: triplets){
            if(isSame(x, target)){
                return true;
            }
        }

        // ???
        List<int[]> candidates = new ArrayList<>();
        for(int[] x: triplets){
            boolean isValid = true;
            for(int i = 0; i < x.length; i++){
                if(x[i] > target[i]){
                    isValid = false;
                    break; // ???
                }
            }
            if(isValid){
                candidates.add(x);
            }
        }

        // ???
        System.out.println(">>> candidates = " + candidates);

        int[] tmp = new int[target.length];
        Arrays.fill(tmp, 0);
        // ???
        for(int[] x: candidates){
            for(int i = 0; i < x.length; i++){
                tmp[i] = Math.max(
                        tmp[i],
                        x[i]
                );
            }
        }

        // ???
        System.out.println(">>> tmp = " + Arrays.toString(tmp));

        return isSame(tmp, target);
    }


    private boolean isSame(int[] a, int[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }


    // LC 763
    // 14.58 - 15.08
    /**
     * -> Return a list of integers
     *   representing the `size` of these parts.
     *
     *   - String s
     *   - partition the string into `as many parts as possible` so
     *     that `each letter` appears in `AT MOST ONE part`
     *   -
     *
     *
     * --------------
     *
     *  IDEA 1) GREEDY ???
     *
     *  IDEA 2) HASHMAP + 2 pointers  + dequeue ???
     *
     *    map : { val : [start_idx, end_idx] }
     *
     *
     * --------------
     *
     *  ex 1)
     *
     *   Input: s = "ababcbacadefegdehijhklij"
     *   Output: [9,7,8]
     *
     *   (The partition is "ababcbaca", "defegde", "hijhklij".)
     *
     *   ->
     *
     *    map = { a: [0, 8], b: [1,5], d: [9,14], e: [10, 15],
     *           h: [], i:[], j: [], k:[], ....
     *       }
     *
     *
     *
     */
    public List<Integer> partitionLabels(String s) {
        // edge

        // map : { val : [end_idx] }
        Map<String, Integer> map = new HashMap();

        //???
        for(int i = 0; i < s.toCharArray().length; i++){
            String str = String.valueOf(s.charAt(i));
            map.put(str, i);
        }

        List<Integer> res = new ArrayList<>();

        // NOTE !!! we define below 2 var
        //int max_idx = 0;
        int cur_max_idx = 0;
        // ??
        int left_pointer = 0;

        for(int i = 0; i < s.toCharArray().length; i++){
            String str = String.valueOf(s.charAt(i));
            // ???
            int tmp_max_idx = map.get(str);
            cur_max_idx = Math.max(cur_max_idx, tmp_max_idx);

            // NOTE !!!
            if(cur_max_idx == i){
                res.add(i - left_pointer + 1);
                left_pointer = i + 1;
            }

        }

        return res;
    }






    public List<Integer> partitionLabels_99(String s) {

//        Set<Integer> set = new HashSet<>();
//        set.add(1);
//        set.remove(1); // ???

        List<Integer> res = new ArrayList<>();

        // map : { val : [start_idx, end_idx] }
        Map<String, Integer[]> map = new HashMap();

        //???
        for(int i = 0; i < s.toCharArray().length; i++){
            String str = String.valueOf(s.charAt(i));
            if(!map.containsKey(str)){
                map.put(str, new Integer[]{0,0});
            }
            Integer[] indices = map.get(str);
            // ???
            indices[0] = Math.min(indices[0], i);
            indices[1] = Math.max(indices[1], i);
            map.put(str, indices);
        }

        System.out.println(">>> map = " + map);

        int start = 0;
        int end = 0;

        // ???
        Set<Integer> set = new HashSet<>();

        // ???
        for(int i = 0; i < s.toCharArray().length; i++){
            String str = String.valueOf(s.charAt(i));
            // ???
            if(set.isEmpty()){
                // ???
                res.add(end - start + 1);
                start += 1;
                end = start;
            }
        }


        return res;
    }


    // LC 678
    // 9.37 - 47 am
    /**
     *   ->  return true if s is valid.
     *
     *  Given a string s containing only
     *  three types of characters
     *
     *  NOTE:
     *   - '*' could be treated as a single right parenthesis ')' or a
     *     single left parenthesis '(' or an empty string "".
     *
     * ---------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) BRUTE FORCE
     *
     *  IDEA 3) STACK
     *
     *  IDEA 4) DP ???
     *
     *    - bottom up dp ???
     *       - DP def:
     *          - boolean dp[i]:
     *             - can s[0...i] be
     *               a valid parenthesis string ????
     *
     *       - DP eq:
     *          - dp[i] =
     *              if (s[i] = * or s[i] == s[0]):
     *                  dp[i] = dp[i] or dp[i-1] // ???
     *              else:
     *                 dp[i] = false // ???
     *
     * ---------------
     *
     *
     */
    public boolean checkValidString(String s) {
        // edge

        // ???
        int n = s.length();
        boolean[] dp = new boolean[n];
        dp[0] = true;

        /**
         *      *
         *      *       - DP eq:
         *      *          - dp[i] =
         *      *              if (s[i] = * or s[i] == s[0]):
         *      *                  dp[i] = dp[i] or dp[i-1] // ???
         *      *              else:
         *      *                 dp[i] = false // ???
         */
        // ??
        for(int i = 1; i < n; i++){
            if(s.charAt(i) == '*' || s.charAt(i) == s.charAt(0)){
                dp[i] = dp[i] || dp[i-1];
            }else{
                // ??
                dp[i] = false;
            }
        }

        return dp[n-1];
    }



    // https://leetcode.com/problems/coin-change-ii/
    // LC 518
    // 11.20 - 30 AM
    /**
     *
     * -> Return the `number of combinations` that make up that amount.
     *     - If that amount of money CAN NOT be made up
     *       by any combination of the coins,
     *       - return 0.
     *
     *
     * --------------------
     *
     *  IDEA 1) 1D DP (bottom up)
     *
     *   - DP def:
     *     - dp[i]: combination from coin that has sum = i
     *
     *   - DP eq:
     *      dp[i] =
     *         dp[i-c] + dp[i] // ???
     */
    public int change(int amount, int[] coins) {
        // edge
        if (coins.length == 1) {
            return amount % coins[0] == 0 ? amount / coins[0] : 0;
        }

        int[] dp = new int[amount + 1]; // ???
        // init
        dp[0] = 1;

//        for(int i = 1; i < amount + 1; i++){
//            for(int c: coins){
//                // i - c: remaining amount
//                if(i - c > 0){
//                    dp[i] += dp[i - c];
//                }
//            }
//        }
        // 3. OUTER LOOP: Iterate through each coin
        // This ensures we process coins one by one to avoid duplicate combinations
        for (int c : coins) {
            // 4. INNER LOOP: Update dp table for all amounts reachable by this coin
            for (int i = c; i <= amount; i++) {
                // New ways to make i = existing ways + ways to make (i - current coin)
                dp[i] += dp[i - c];
            }
        }


        System.out.println(">>> dp = " + Arrays.toString(dp));

        return dp[amount];
    }

    // LC 228
    // 9.38 - 48 am
    /**
     *  -> Return the smallest sorted
     *    list of ranges that cover all the
     *    numbers in the array exactly.
     *
     *
     *    nums: sorted unique int array
     *    [a,b]: set of all int from a to b (inclusive)
     *
     *
     *    Each range [a,b] in the list should be output as:
     *
     *    "a->b" if a != b
     *    "a" if a == b
     *
     *
     *  --------------
     *
     *   IDEA 1) INTERVAL OP
     *
     *
     *  --------------
     *
     */
    public List<String> summaryRanges(int[] nums) {
        // edge
        List<String> res = new ArrayList<>();
        if(nums == null || nums.length == 0){
            return res;
        }
        if(nums.length == 1){
            res.add(String.valueOf(nums[0]));
            return res;
        }
//        if(nums.length == 2){
//            res.add(String.valueOf(nums[0]));
//            return res;
//        }

        int l = 0; // left pointer
        int rBackup = 0;

        for(int r = 1; r < nums.length; r++){
            //int cur = nums[r];
            if(nums[r] - nums[r-1] > 1){
                if((r-1) - l + 1 == 1){
                    res.add(String.valueOf(nums[l]));
                }else{
                    String leftVal = String.valueOf(nums[l]);
                    String rightVal = String.valueOf(nums[r-1]);
                    res.add(leftVal + "->" + rightVal);
                }
                l = r;
            }
            // ???
            rBackup = r;
        }

        // handle final idx
        if(rBackup == l){
            res.add(String.valueOf(nums[rBackup]));
        }else{
            String leftVal = String.valueOf(nums[l]);
            String rightVal = String.valueOf(nums[rBackup]);
            res.add(leftVal + "->" + rightVal);
        }

        return res;
    }


    // LC 452
    // 10.10 - 20 am
    /**
     * -> Given the array points,
     *   return the minimum number of arrows
     *   that must be shot to burst all balloons.
     *
     *
     *  --------------
     *
     *   IDEA 1) GREEDY
     *
     *   IDEA 2) BRUTE FORCE
     *
     *   IDEA 3) INTERVAL
     *     -> `merge` interval
     *
     *
     *  --------------
     *
     */
    public int findMinArrowShots(int[][] points) {
        // edge
        if(points == null || points.length == 0){
            return 0;
        }
        if(points.length == 1){
            return 1;
        }
        //List<Integer[]> list = new ArrayList<>();
        Stack<int[]> st = new Stack<>();
        st.add(points[0]);

        // NOTE !!!
        // 1. SORT by the end coordinate.
        // This is the greedy key: finish the earliest balloon first to leave room for others.
        // Use Integer.compare to avoid overflow with negative numbers!
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

        /**  cases prev and cur interval NOT overlap
         *
         *    1.
         *
         *      |---|               prev
         *             |----|       cur
         *
         *  2.
         *             |-----|      prev
         *      |--|                cur
         *
         */
        for(int i = 1; i < points.length; i++){
            int[] prev = st.peek(); // ??
            int[] cur = points[i];

            // if `NOT` NOT overlap
            // -> prev and cur MUST overlap
            if( !(cur[0] > prev[1]) || !(cur[1] < prev[0]) ){
                prev = st.pop();
                st.add( new int[] { Math.min(prev[0], cur[0]), Math.max(prev[1], cur[1]) } );
            }else{
                st.add(cur);
            }
        }


        return st.size();
    }


    // LC 986
    // 7.05 - 15 am
    /**
     * -> Return the intersection
     * of these two interval lists.
     *
     *
     * -----------------
     *
     *  IDEA 1) INTERVAL OP
     *
     *
     * -----------------
     *
     *
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        // edge

        int n = firstList.length;

        // ??
        Deque<int[]> deque = new ArrayDeque<>();

        int[] prev = new int[]{1,1};

        // idx = 0
        int start = Math.max(firstList[0][0], secondList[0][0]);
        int end = Math.min(firstList[0][1], secondList[0][1]);
        deque.add(new int[]{start,end});

        /** NOTE !!!
         *
         * Each list of intervals is
         * pairwise `disjoint` and in sorted order
         **/
        for(int i = 1; i < n; i++){
            int[] cur1 = firstList[i];
            int[] cur2 = secondList[i];

            start = Math.max(cur1[0], cur2[0]);
            end = Math.min(cur1[1], cur2[1]);
            deque.add(new int[]{start,end});

            // check if boundary overlap
            int[] prev1 = firstList[i-1];
            int[] prev2 = secondList[i-1];
            if(cur1[0] == prev2[1]){
                deque.add(new int[]{cur1[0],cur1[0]});
            }
            if(cur2[1] == prev1[0]){
                deque.add(new int[]{cur1[0],cur1[0]});
            }
        }

        // ??
        int[][] res = new int[deque.size()][2];
        int i = 0;
        while (!deque.isEmpty()){
            res[i] = deque.pollFirst(); // ???
        }

        return res;
    }


    // LC 435
    // 9.21 - 45 am
    /**
     * ->  return the minimum number of intervals you
     *     need to remove to make the rest
     *     of the intervals non-overlapping.
     *
     *  ---------------
     *
     *   IDEA 1) SORT + INTETVAL OP
     *
     *
     *   ---------------
     *
     *   ex 1)
     *
     *   Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
     *  Output: 1
     *   Explanation: [1,3] can be removed and the
     *   rest of the intervals are non-overlapping.
     *
     *
     *  -> intervals = [[1,2],[2,3],[3,4],[1,3]]
     *
     *  sort
     *
     *  intervals = [ [1,2],[1,3],[2,3],[3,4] ]
     *                 x     x     x      x
     *
     *   cache = [ [1,2], [2,3], [3,4] ]
     *
     *
     */

    // 10.35 - 47 am
    /**
     *  IDEA 1) SORT + 2 POINTER ???
     *
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        // edge
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        // sort ??
        // sort on 1st element (small -> big)
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        int op = 0;
        List<int[]> collected = new ArrayList<>();
        for(int i = 0; i < intervals.length; i++){

            int[] cur = intervals[i];

            if(i == 0){
                collected.add(cur);
            }
            else{
                int[] prev = collected.get(collected.size() -1);
                // if overlap, and
                // if the cur one if `longer` one,
                // NOT add it
                // else
                // remove the prev one
                /**  since we ALREADY sort on 1st element (small -> big),
                 *   below are overlap case:
                 *
                 *    |----| prev
                 *       |---------| cur
                 *
                 *   |-----|  prev
                 *     |--|   cur
                 *
                 */
                if(prev[1] > cur[0]){
                    if(prev[1] > cur[1]){
                        collected.remove(collected.get(collected.size() -1));
                        collected.add(cur);
                        //op += 1;
                    }else{
                        collected.add(cur);
                    }
                    op += 1;
                }
            }
        }

        return op;
    }







    public int eraseOverlapIntervals_99(int[][] intervals) {
        // edge
        if(intervals == null || intervals.length == 0){
            return 0;
        }
        if(intervals.length == 1){
            return 1;
        }

        // sort ??
        // sort on 1st element (small -> big)
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
//                int diff = o1[0] - o2[0];
//                if(diff != 0){
//                    return diff;
//                }
//                return o1[1] - o2[2]; // ???
                //return o1[0] - o2[0] != 0 ? o1[0] - o2[0] : o1[1] - o2[1];
                return o1[0] - o2[0];
            }
        });

        //int res = 0;
        Deque<int[]> deque = new LinkedList<>();
        for(int[] x: intervals){
            if(deque.isEmpty()){
                deque.add(x);
            }else{
                int[] prev = deque.getLast(); // ???
                // // Case A: NO overlap (New starts after/at prev ends)
                // if NOT overlap: add to dequeue
                if(x[0] >= prev[1]){
                    deque.addLast(x); // ???
                }
                // Case B: OVERLAP detected
                //         -> Keep the one that ends EARLIER
                else{
                    if(x[1] < prev[1]){
                        // pop last oen first
                        deque.pollLast();
                        // add the cur one
                        deque.add(x);
                    }
                }
            }
        }

        System.out.println("deque = " );
        for(int[] x: deque){
            System.out.println(Arrays.toString(x));
        }

        return intervals.length - deque.size();
    }


    // LC 1851
    // 10.01 - 10.11 am
    /**
     *  -> Return an array containing the answers to the queries.
     *
     *  - queries
     *     - The answer of `jth query`
     *        -  the size of the `smallest` interval i
     *            - that lefti <= queries[j] <= righti.
     *        - If NO such interval exists,
     *           - return -1.
     *
     *
     *  ------------
     *
     *   IDEA 1) INTERVAL + HASHMAP ??
     *
     *     { idx: size }
     *
     *     interval: sort on start time ?? (1st idx)
     *
     *
     *  ------------
     *
     *
     */
    // 16.58 - 17.21 pm
    /**  IDEA 1) PQ + SORT ???
     *
     *  1. sort intervals
     *     - 1st idx (small -> big)
     *
     *  2.  `new` queries
     *     - [ i, query ]
     *
     *     // ?? need sort ???
     *
     *  3. PQ:  {  size, idx }
     *      - small PQ, sort on size
     *
     *
     */
    public int[] minInterval(int[][] intervals, int[] queries) {
        // edge

        // sort intervals
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0]; // ???
            }
        });

        // `new` queries:  [ i, query ]
        int[][] queries2 = new int[queries.length][2];
        for(int i = 0; i < queries.length; i++){
            // ???
            queries2[i] = new int[]{i, queries[i]};
        }
        // sort queries2 ???
        // query: small -> big
        Arrays.sort(queries2, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1]; // ???
            }
        });

        // ?? PQ:  {  size, end_time }
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                return o1[0] - o2[1];
            }
        });

        int[] res = new int[queries.length];

        int i = 0; // pointer for interval

        int n = intervals.length;

        // // `new` queries:  [ i, query ]
        for(int[] q: queries2){
            int qIdx = q[0];
            int qVal = q[1];


            while(i < n &&  intervals[i][0] < qVal){
                int size = intervals[i][1] - intervals[i][0] + 1;
                pq.add(new Integer[] { size, intervals[i][1] });
                i += 1;
            }

            while(!pq.isEmpty() && pq.peek()[1] < qVal){
                pq.poll();
            }

            res[qIdx] = pq.isEmpty() ? -1: pq.peek()[0];

        }


        return res;
    }






    public int[] minInterval_99(int[][] intervals, int[] queries) {
        // edge

        // { idx : size }
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < intervals.length; i++){
            map.put(i, intervals[i][1] - intervals[i][0] + 1);
        }

        // sort intervals ??
        // sort on 1st idx (small -> big)
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        int[] res = new int[queries.length];

        for(int i = 0; i < queries.length; i++){
            int q = queries[i];
            // find the interval
            // 1) include q
            // 2) has shortest interval
            res[i] = getInterval(q, intervals, map);
        }

        return res;
    }

    // ????
    private int getInterval(int q, int[][] intervals, Map<Integer, Integer> map){
        //List<Integer> indexList = new ArrayList<>();
        int smallestSize = 100000001; // ???
        for(int i = 0; i < intervals.length; i++){
            int[] x = intervals[i];
            if(x[0] <= q && q <= x[1]){
                //indexList.add(i);
                smallestSize = Math.min(smallestSize, map.get(i));
            }
        }

        // get the one has shortest interval
        return smallestSize != 100000001 ? smallestSize : -1;
    }


    // LC 1929
    // 16.10 - 20 am
    /**
     *
     *  -> Return the array ans.
     *
     *   nums: arr, size = n
     *
     *   to create an array ans of length 2n
     *   where
     *     - ans[i] == nums[i]
     *     - and ans[i + n] == nums[i] for 0 <= i < n (0-indexed).
     *
     *     e.g.
     *
     *     - ans is the concatenation of two nums arrays.
     *
     *
     *  ---------------
     *
     *   IDEA 1) ARRAY OP
     *
     *
     *  ---------------
     *
     */
    public int[] getConcatenation(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return nums;
        }
        int n = nums.length;
        int[] res = new int[2 * n];
        int cnt = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < n; j++){
                res[cnt] = nums[j];
                cnt += 1;
            }
        }

        return res;
    }



    // LC 128
    // 16.18 - 33 am
    /**
     *  -> Given an `unsorted` array of integers nums,
     *    return the length of the
     *    `longest consecutive elements sequence.`
     *
     *
     *  -------------------
     *
     *   IDEA 1) 2 POINTERS ???
     *
     *    -> Set -> sort -> 2 pointers
     *
     *
     *   -------------------
     *
     *   ex 1)
     *
     *   nums = [100,4,200,1,3,2]
     *
     *   -> set, sort
     *   -> nums = [1,2,3,4,100,200]
     *
     *   [1,2,3,4,100,200]
     *    l r                 len = 2
     *
     *   [1,2,3,4,100,200]
     *    l   r               len = 3
     *
     *   [1,2,3,4,100,200]
     *    l     r             len =4
     *
     *   [1,2,3,4,100,200]
     *    l       r            len =4
     *            l
     *
     *   [1,2,3,4,100,200]    len = 4
     *             l   r
     *
     *
     *
     *  ex 2)
     * Input: nums = [0,3,7,2,5,8,4,6,0,1]
     * Output: 9
     *
     *
     * -> set, sort
     *
     * [0,1,2,3,4,5,6,7,8]
     *
     *
     * ex 3)
     *
     *   Input: nums = [1,0,1,2]
     *   Output: 3
     *
     *
     *  -> set, sort
     *  [0,1,2]
     *
     *
     */
    public int longestConsecutive(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            set.add(x);
        }
        // ??
        List<Integer> list = new ArrayList<>();
        for(int x: set){
            list.add(x);
        }
        // sort (small -> big)
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        System.out.println(">>> list = " + list);
        // 2 pointers
        /**  slide window
         *
         *   for(int r = 0; r < nums.len; r++){
         *        // ...
         *       while(conditions){
         *           // ...
         *           l += 1;
         *       }
         *       // ...
         *   }
         *
         */
        int maxConSeLen = 1;
        int l = 0;
        // ????
        for(int r = 1; r < list.size(); r++){
            System.out.println(">>> l = " + l +
                    ", r = " + r);

            if(list.get(r) - list.get(r-1) != 1){
                l = r;
            }
            maxConSeLen = Math.max(maxConSeLen, r - l + 1);

//            if(l >= list.size()){
//                break;
//            }
        }

        return maxConSeLen;
    }


    // LC 165
    // 16.40 - 50 pm
    /**
     * -> return the version comparision
     *   Return the following:
     *
     *   - If version1 < version2, return -1.
     *   - If version1 > version2, return 1.
     *   - Otherwise, return 0.
     *
     *    (left-to-right order. )
     *
     *
     *   2 version str:
     *     - version1, version2
     *
     *
     *
     *  -----------------------
     *
     *   IDEA 1) STR OP
     *
     *
     *
     *  -----------------------
     *
     *
     */
    public int compareVersion(String version1, String version2) {
        // edge

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        int cnt = 0;
        for (String x : version1.split("\\.")) {
            if (cnt == 0) {
                list1.add(Integer.parseInt(x));
            } else {
                if (!x.equals("0")) {
                    list1.add(Integer.parseInt(x));
                }
            }
            cnt += 1;
        }

        cnt = 0;
        for (String x : version2.split("\\.")) {
            if (cnt == 0) {
                list2.add(Integer.parseInt(x));
            } else {
                if (!x.equals("0")) {
                    list2.add(Integer.parseInt(x));
                }
            }
            cnt += 1;
        }

        System.out.println(">>> list1 = " + list1 +
                ", list2 = " + list2);

        int size = Math.min(list1.size(), list2.size());

        for (int i = 0; i < size; i++) {
            if (list1.get(i) > list2.get(i)) {
                return 1;
            }
            if (list1.get(i) < list2.get(i)) {
                return -1;
            }
        }

        if(list1.size() == list2.size()){
            return 0;
        }

        return list1.size() > list2.size() ? 1 : -1;
    }




//    public int compareVersion(String version1, String version2) {
//        // edge
//
//        List<Integer> list1 = new ArrayList<>();
//        List<Integer> list2 = new ArrayList<>();
//
//        int cnt = 0;
//        for(String x: version1.split("")){
//            if(cnt == 0){
//                list1.add(Integer.parseInt(x));
//            }else{
//                if(!x.equals("0")){
//                    list1.add(Integer.parseInt(x));
//                }
//            }
//            cnt += 1;
//        }
//
//        cnt = 0;
//        for(String x: version2.split("")){
//            if(cnt == 0){
//                list2.add(Integer.parseInt(x));
//            }else{
//                if(!x.equals("0")){
//                    list2.add(Integer.parseInt(x));
//                }
//            }
//            cnt += 1;
//        }
//
//        System.out.println(">>> list1 = " + list1 +
//                ", list2 = " + list2);
//
//        int size = Math.min(list1.size(), list2.size());
//
//        for(int i = 0; i < size; i++){
//            if(list1.get(i) > list2.get(i)){
//                return 1;
//            }if(list1.get(i) < list2.get(i)){
//                return -1;
//            }
//        }
//
//
//        return 0;
//    }



    // LC 283
    // 9.53 - 10.03 am
    /**
     *
     *  -> move all 0's to the end of it while
     *    maintaining the relative order
     *    of the non-zero elements.
     *
     *
     *  Note:
     *    that you must do this
     *    `in-place`
     *    without making a copy of the array.
     *
     *
     *  ------------
     *
     *  IDEA 1) 2 POINTERS
     *
     */
    public void moveZeroes(int[] nums) {
        // edge
        if(nums.length <= 1){
            return;
        }

        int l = 0;
        int r = nums.length - 1;

        // 1st op
        while(r > l){
            if(nums[l] == 0){
                while(nums[r] == 0){
                    r -= 1;
                }
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;
            }

            l += 1;
           // r -= 1;
        }

        System.out.println(">>> after 1st op, nums = " + Arrays.toString(nums));
        //System.out.println(">>> l = " + l + ", r = " + r);

        // 2nd op
        int end = 0;
        for (int i = 0; i < nums.length; i++){
            if(nums[i] != 0){
                end = i;
                break;
            }
        }

        int start = 0;
        while (end > 0){
            int tmp = nums[end];
            nums[end] = nums[start];
            nums[start] = tmp;
            start += 1;
            end -= 1;
        }

        System.out.println(">>> after 2nd op, nums = " + Arrays.toString(nums));

    }


//    // LC 416
//    public boolean canPartition(int[] nums) {
//
//        return false;
//    }


    // LC 303
    // 14.12 -22 pm
    /**
     * -> int sumRange(int left, int right) Returns the sum of the
     *    elements of nums between indices left and right
     *    inclusive (i.e. nums[left] + nums[left + 1] + ... + nums[right]).
     *
     *
     *  -----------------
     *
     *  IDEA 1) PRE FIX SUM
     *
     *  IDEA 2) DP
     *    - DP def
     *       - dp[i]: sum in from 0 to i
     *
     *    - DP eq
     *       - dp[i] =
     *           dp[i-1] + nums[i]
     *
     *
     * --------------
     *
     *  ex 1)
     *
     * Input
     * ["NumArray", "sumRange", "sumRange", "sumRange"]
     *  [[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
     * Output
     * [null, 1, -1, -3]
     *
     *  ->
     *   arr = [-2, 0, 3, -5, 2, -1]
     *   pre_fix = [-2, -2, 1, -4, -2, -3]
     *
     *   -> sum_r[i, j] = pre_fix[j] - pre_fix[i-1]
     *
     *    a0
     *    a0 + a1 + a2
     *
     *   -> sum_r(0,2) = pre_fix[2] - pre_fix[0-1]
     *                = 1
     *
     *   -> sum_r(2,5) = pre_fix[5] - pre_fix[2-1]
     *                 = -3 - (-2)  = -1
     *
     *
     *
     */
    // dp
    class NumArray {

        int[] dp;

        public NumArray(int[] nums) {
            int n = nums.length;
            // build dp arr
            this.dp = new int[n]; // ???
            this.dp[0] = nums[0];
            for(int i = 1; i < n; i++){
                this.dp[i] = nums[i] + this.dp[i - 1];
            }
        }

        public int sumRange(int left, int right) {
            if(left > right){
                return -1;
            }
//            if(left == right){
//                return this.nums[right];
//            }
//            if(left == 0){
//                return this.prefix[right];
//            }

            return this.dp[right] - this.dp[left - 1];
        }

    }









    // prefix sum
    class NumArray_100 {

        // attr
        int[] nums;
        int[] prefix;
        int n;

        public NumArray_100(int[] nums) {
            this.nums = nums;
            this.n = this.nums.length;
            this.prefix = new int[this.n];
            // ??
            int prefixSum = 0;
            for(int i = 0; i < n; i++){
                prefixSum += this.nums[i];
                this.prefix[i] = prefixSum;
            }
        }

        public int sumRange(int left, int right) {
            if(left > right){
                return -1;
            }
            if(left == right){
               return this.nums[right];
            }
            if(left == 0){
                return this.prefix[right];
            }

            return this.prefix[right] - this.prefix[left - 1];
        }
    }


    // LC 724
    // 14.55 - 15.05 pm
    /**
     *  -> Return the leftmost pivot index.
     *     If no such index exists, return -1.
     *
     *     - pivot index:
     *         the index where the sum of all the numbers
     *         strictly to the left of the index is equal
     *         to the sum of all the numbers strictly to
     *         the index's right.
     *
     * -------------------
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) ARRAY OP
     *    -  get total sum
     *    - loop over arr
     *    - if ( left sub arr ) * 2 + cur val =  total sum
     *       - add it to cache
     *
     *    - ...
     *    - return the 1st element from cache
     *      (leftmost)
     *
     *
     *  IDEA 3) prefix sum
     *
     * -------------------
     *
     *
     *
     */
    // 17.52 - 18.02 pm
    /**
     *
     *  -> Return the leftmost pivot index. If no such index exists, return -1.
     *
     *
     */
    // IDEA 3) prefix sum
    public int pivotIndex(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1;
        }
        if(nums.length == 1){
            return 0;
        }

        int sum = 0;
        for(int x : nums){
            sum += x;
        }
        // prefix
        /**
         *   pivot idx:
         *
         *   nums = [1,7,3,6,5,6]
         *                 p
         *
         *          |----|   |--|
         *           left     right
         *
         *  -> so sum(left) == sum(right)
         *
         */
        int prefix = 0;
        for(int i = 0; i < nums.length; i++){
            int x = nums[i];
            prefix += x;
            int left = prefix - x;
            int right = sum - prefix;
            if(left == right){
                return i;
            }
        }

        return -1;
    }








    public int pivotIndex_99(int[] nums) {

        int totalSum = 0;
        int[] prefix = new int[nums.length + 1]; // ??
//        for(int i = 0; i < nums.length + 1; i++){
//            if(i < nums.length){
//                totalSum += nums[i];
//            }
//            prefix[i+1] = totalSum;
//        }
        for(int i = 0; i < nums.length ; i++){
            totalSum += nums[i];
            prefix[i+1] = totalSum;
        }

        System.out.println(">>> totalSum = " +  totalSum +
                ", prefix = " + Arrays.toString(prefix));

        for(int i = 0; i < nums.length; i++){

            int leftSum = prefix[i];
            //int rightSum = totalSum - leftSum;
            int rightSum = totalSum - prefix[i] - (nums[i]);

            int prefixSum = prefix[i+1];
            if( (prefixSum - nums[i]) * 2  + nums[i] ==  totalSum){
                return i;
            }
        }

        return -1;
    }


    // LC 560
    // 15.28 - 38
    /**
     *
     *  -> Given an array of integers nums and an integer k,
     *     return the `total number` of `subarrays` whose `sum equals to k.`
     *
     *
     * NOTE:
     *   - subarray: contiguous non-empty sequence of elements within an array.
     *
     *
     * -------------
     *
     *  IDEA 1) HASHMAP
     *
     *  IDEA 2) BRUTE FORCE
     *
     *  IDEA 3) DP ???
     *
     *
     *
     * -------------
     *
     * ex 3)
     *
     *  nums = [1,1,2,3,1] , k = 2
     *
     *   -> [1,1], [2]
     *
     *
     */
    // HASHMAP
    // 18.07 - 17 pm
    /**
     *  -> return the `total number` of `subarrays`
     *  whose sum equals to k.
     *
     *   - NOTE:
     *      - A subarray is a `contiguous`
     *        non-empty sequence of elements within an array.
     *
     *
     *  ---------------
     *
     *   IDEA 1) HASHMAP + PREFIX SUM ???
     *     -> prefix - x = k
     *     -> x = prefix - k
     *     -> so check if  `prefix - k` in map already
     *
     *     -> sum ( nums[i..j] ) = k
     *     -> sum ( nums[i..j] ) = prefix[j] - prefix[i]  = k
     *
     *     prefix[j] is cur prefix sum
     *     prefix[i] is some prefix sum that VISITED before
     *
     *     -> so check if map contains (prefix[j] - k)
     *
     *
     *
     *     map : {val : cnt } // ???
     *
     *
     *   ---------------
     *
     *
     */
    public int subarraySum(int[] nums, int k) {
        // edge


        // { prefixSum : how_many_times_it_occurred }
        Map<Integer, Integer> map = new HashMap<>();
        int prefix = 0;
        int cnt = 0;

        // ????
        map.put(0, 1);


        for(int i = 0; i < nums.length; i++){
            int x = nums[i];
            prefix += x;
            /**
             *      *   IDEA 1) HASHMAP + PREFIX SUM ???
             *      *     -> prefix - x = k
             *      *     -> x = prefix - k
             *      *     -> so check if  `prefix - k` in map already
             */
            if(map.containsKey(prefix - k)){
                cnt += map.get(prefix - k);
                //cnt += 1; // ????
            }
//            else{
//                map.put(prefix - k, map.getOrDefault(prefix - k) + 1); // ????
//            }

            // ???
            map.put(prefix, map.getOrDefault(prefix, 0) + 1);
        }

        return cnt;
    }










    // HASHMAP
    public int subarraySum_99(int[] nums, int k) {
        // edge
        int prefixSum = 0;
        int cnt = 0;

        int totalSum = 0;
        // ???
        for(int i = 0; i < nums.length; i++){
            totalSum += nums[i];
        }


        // map : { idx : prefixsum }
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++){
            prefixSum += nums[i];
            // case 1) if nums[i] == k
            if(nums[i] == k){
                cnt += 1;
            }
            // case 2) if sum[0,i] == k
            if(prefixSum == k){
                cnt += 1;
            }
            // case 3) if sum[i+1, ...] == k
            if(map.containsKey(totalSum - prefixSum)){
                cnt += 1;
            }

            // update map
            map.put(i, prefixSum);
        }

        System.out.println(">>> map = " + map);

        return cnt;
    }



    // LC 449
    // 16.10 - 20 pm
    /**
     *
     * -> Design an algo
     *   - to serialize
     *   - deserialize a binary search tree. (BST)
     *
     *
     *  -----------------
     *
     *   IDEA 1) BST property + recursion
     *
     *
     *  -----------------
     *
     *
     */
    //  BST property + recursion
    public class Codec {

        // attr
//        String endcode = null; // ??
//        TreeNode decodeNode = null; // ??

        // Encodes a tree to a single string.
        // pre-order traverse
        // root -> sub left -> sub right
        public String serialize(TreeNode root) {
            // edge
            if(root == null){
                return "#";
            }
            // ???
            return root.val + "," + serialize(root.left)
                    + "," + serialize(root.right); // ????? needed ? + "#";
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            // edge
            if(data.equals("#")){
                return null; // ???
            }

            // ???
            Queue<String> q = new LinkedList<>();
            // V1
//            for(String x: data.split(",")){
//                q.add(x);
//            }
            // V2
            q.addAll(Arrays.asList(data.split(",")));

            return helper(q);
        }

        private TreeNode helper(Queue<String> q){
            // edge
            if(q.isEmpty()){
                return null; // ??
            }

            // ???
            String node = q.poll();
            if(node.equals("#")){
                return null;
            }

            TreeNode root = new TreeNode(Integer.parseInt(node));
            root.left = helper(q);
            root.right = helper(q);

            return root;
        }

    }




    // LC 297
//    public class Codec {
//
//        // Encodes a tree to a single string.
//        public String serialize(TreeNode root) {
//
//        }
//
//        // Decodes your encoded data to tree.
//        public TreeNode deserialize(String data) {
//
//        }
//    }


    // LC 1470
    // 13.06 - 16 pm
    /**
     * -> Return the array in the
     *  form [x1,y1,x2,y2,...,xn,yn].
     *
     *
     *  ----------------
     *
     *   IDEA 1) ARRAY OP
     *
     *
     *  ----------------
     *
     */
    public int[] shuffle(int[] nums, int n) {
        // edge
        if(nums == null || nums.length == 0){
            return nums;
        }
        if(nums.length == 1){
            return nums;
        }
        // ??
        if(n % 2 != 0){
            return null;
        }

        int[] res = new int[2 * n];

        for(int i = 0; i < n; i++){
            // 1) even idx (0, 2, 4,...)
            res[2 * i] = nums[i];

            // 2) odd idx (1,3,5 ..)
            res[2 * i + 1] = nums[i + n];
        }

//        // ???
//        // 1. add `left half arr` to res
//        for(int i = 0; i < n / 2; i += 1){
//            res[i * 2] = nums[i];
//        }
//
//        // /??
//        int k = 1;
//        // 2. add `right half arr` to res
//        for(int i = n / 2 + 1; i < n; i += 1){
//            res[k] = nums[n / 2 + i];
//            k += 2;
//        }

        return res;
    }


    // LC 66
    // 13.26 - 36 pm
    /**
     *
     *  IDEA 1) ARRAY OP
     *
     */
    // IDEA 1) ARRAY OP
    public int[] plusOne(int[] digits) {
        // edge
        if(digits == null || digits.length == 0){
            return new int[]{1};
        }

        //int extra = 1;
        int plus = 1;
        //int[] res = new int[digits.length];
        List<Integer> list = new ArrayList<>();

        // `inverse` loop over arr
        for(int i = digits.length - 1; i >= 0; i--){
            int val = digits[i] + plus;
            if(val > 9){
                plus = 1;
                val -= 10;
            }else{
                plus = 0;
            }
            //res[i] = val;
            list.add(val); // ???
        }

        if(plus != 0){
            list.add(plus);
        }

        // reverse
        int[] res = new int[list.size()];
        // ???
        System.out.println(">>> before reverse: list = " + list);
        Collections.reverse(list);
        System.out.println(">>> after reverse: list = " + list);
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res;
    }


    // LC 727
    // 13.47 - 57 pm
    /**
     *
     *  -> Given strings S and T,
     *    find the minimum (contiguous) substring W of S,
     *    so that T is a subsequence of W.
     *
     *    NOTE:
     *      - If there is `no such window` in S
     *        that covers all characters in T, return ""
     *
     *      - if there r multiple ans,
     *        return the LEFTMOST one
     *
     *  --------------
     *
     *
     *
     *  IDEA 1) BRUTE FORCE (double loop)
     *
     *  IDEA 2) 2D DP ??
     *
     *
     *    - DP def:
     *
     *        boolean dp[i][j]: can s1[i..j] cover s2 ???
     *
     *    - DP eq:
     *
     *        dp
     *
     *  IDEA 3) 2 POINTERS
     *
     *
     * --------------
     *
     *
     */
    /**  NOTE !!!
     *
     *  - DP def:
     *      dp[i][j] = the starting index (1-based)
     *      of the `shortest window` in S[0…i-1]
     *      that contains T[0…j-1] as a subsequence.
     *
     *   - DP eq:
     *
     *    For i ≥ 1 and j ≥ 1:
     *
     *     - If S[i−1] == T[j−1]:
     *         - If j == 1: start a new subsequence → dp[i][j] = i
     *         - Else: extend existing subsequence → dp[i][j] = dp[i−1][j−1]
     *
     *     - Else:
     *         - Skip this S char → dp[i][j] = dp[i−1][j]
     *
     */
    // IDEA: DP
    // 17.20 - 30 pm
    /**
     *  IDEA 1) 2D DP
     *
     *
     *  - DP def:
     *
     *      dp[i][j] = `the starting index (1-based)` !!!!
     *      (NOTE !!! idx, NOT len)
     *
     *      of the `shortest window` in S[0…i-1]
     *      that contains T[0…j-1] as a subsequence.
     *
     *      e.g. `shortest window` of s[0, ...i-1]  (i)
     *           that contains T[0…j-1]  (j) as a subsequence.
     *
     *  - DP eq
     *
     *     if s1[i] == s2[j]
     *        - dp[i][j] =  dp[i-1][j-1] + 1  // ???
     *     else
     *       - dp[i][j] =  dp[i-1][j-1]  // ???
     *
     *
     */
    public String minWindow(String s1, String s2) {
        // edge
        if(s1.isEmpty() && s2.isEmpty()){
            return "";
        }
        if(s2.length() > s1.length()){
            return "";
        }

        int size1 = s1.length();
        int size2 = s2.length();
        int[][] dp = new int[size1 + 1][size2 + 1];

        // init ???
        // if s2 is null
        // ??????
//        for(int i = 0; i < size1; i++){
//            dp[i][0] = 0;
//        }
//        // if s1 is null
//        for(int j = 0; j < size2; j++){
//            dp[0][j] = 0;
//        }


        // ???
        for(int i = 1; i < size1; i++){
            for(int j = 1; j < size2; j++){
                //if(s1.charAt(i) == s2.charAt(j)){
                if(s1.charAt(i-1) == s2.charAt(j-1)){
                    // ????
                    if(j == 1){
                        dp[i][j] = i; // start new subsequence
                    }else{
                      // ???  dp[i][j] =  dp[i-1][j-1] + 1; // ??
                        dp[i][j] =  dp[i-1][j-1];
                    }

                }else{
                   // dp[i][j] =  dp[i-1][j-1];
                    // ???
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }


        return "";
    }







    // IDEA: DP
    public String minWindow_99(String s1, String s2) {
        // edge
        if(s1.isEmpty() && s2.isEmpty()){
            return "";
        }
        if(s2.length() > s1.length()){
            return "";
        }

        int size1 = s1.length();
        int size2 = s2.length();
        int[][] dp = new int[size1][size2];

        // ??
        for(int i = 1; i < size1; i++){
            for(int j = 1; j < size2; j++){
                if(s1.charAt(i-1) == s2.charAt(j-1)){
                    if(i == 1){
                        dp[i][j] = i; // start new subsequence
                    }else{
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                }else{
                    dp[i][j] = dp[i - 1][j];
                }



//                if(i >= 0 && j > 0){
//                    // ???
//                    if(j == 1){
//                        dp[i][j] = i;
//                    }else{
//                        dp[i][j] = dp[i-1][j];
//                    }
//                }
            }
        }



        return null;
    }





    // BRUTE FORCE
    public String minWindow_1(String s1, String s2) {
        // edge
        if(s1.isEmpty() && s2.isEmpty()){
            return "";
        }
        if(s2.length() > s1.length()){
            return "";
        }

        int minLen = s2.length();
        List<String> list = new ArrayList<>();

        for(int i = 0; i < s1.length(); i++){
            // ???
            int j = i;
            int s2Idx = i; // ???
            while(j < s1.length()){
                // case 1) j meet s2 final idx
                if(j == s2.length() - 1){
                    if(s2.charAt(s2Idx) == s1.charAt(j)){
                        list.add(s1.substring(i, j));
                        minLen = Math.min(minLen, j - i + 1);
                    }
                    break; // ???
                }
                // case 2) s1 j idx == s2 s2Idx idx
                if(s2.charAt(s2Idx) == s1.charAt(j)){
                    //j += 1;
                    s2Idx += 1;
                    //j += 1;
                }
                // move j anyway (s1 right idx)
                j += 1;
            }
        }

        List<String> candidates = new ArrayList<>();
        for(String x: list){
            if(x.length() == minLen){
                candidates.add(x);
            }
        }

        return !candidates.isEmpty() ? candidates.get(0) : "";
    }


    // LC 189
    // 14.41 - 15.01 pm
    /**
     *  -> rotate the array to the right by `k steps`,
     *   where k is non-negative.
     *
     *   nums: int array
     *
     *   --------------
     *
     *   IDEA 1) DEQUEUE
     *
     *   IDEA 2) array op (split + paste)
     *
     *
     *   --------------
     *
     *
     */
    //  IDEA 2) array op (split + paste)
    public void rotate(int[] nums, int k) {
        // edge

        int len = nums.length;
        // adjust k
        k = k % len;

        //int rightArr = Array.
        List<Integer> right = new ArrayList<>();
        for(int i = len - k; i < len; i++){
            right.add(nums[i]);
        }


    }

    // IDEA 1) DEQUEUE
    public void rotate_99(int[] nums, int k) {
        // edge

        Deque<Integer> deque = new LinkedList<>();
        //deque.addAll(nums);
        for(int x: nums){
            deque.add(x);
        }

        int len = nums.length;
        // adjust k
        k = k % len;

        while(k > 0 && !deque.isEmpty()){
            //int val = deque.pollLast();
            deque.addFirst(deque.pollLast());
            k -= 1;
        }

        // ??
        int i = 0;
        while(!deque.isEmpty()){
            nums[i] = deque.pollFirst();
            i += 1;
        }

    }

    // LC 442
    // 15.10 - 20 pm
    /**
     *
     *
     *  NOTE:
     *
     *   - You must write an algorithm that runs in O(n) time
     *     and uses only constant auxiliary space,
     *
     * --------------------
     *
     *  IDEA 1): hashmap
     *
     *  IDEA 2): set
     *
     *
     *  --------------------
     *
     */
    public List<Integer> findDuplicates(int[] nums) {
        // edge
        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        List<Integer> res = new ArrayList<>();
        for(int k: map.keySet()){
            if(map.get(k) > 1){
                res.add(k);
            }
        }

        return res;
    }




    // LC 41
    // 15.20 - 30 pm
    /**
     * -> Return the `smallest` positive integer
     *    that is NOT present in nums.
     *
     *    - nums: `unsorted` int arr
     *
     *
     *    NOTE:
     *      -  algorithm that runs
     *         - O(n) time and
     *         - O(1) auxiliary space.
     *
     *
     *  ---------------------
     *
     *  IDEA 1) MATH
     *
     *   - get smallest, biggest val
     *
     *    sum += val
     *
     *    -> sum1 = (smallest + biggest) * (size) / 2
     *
     *    ->
     *       if sum1 == sum, if yes, return biggest + 1
     *
     *       else: return sum - sum1
     *
     *
     *  IDEA 2) SET ???
     *
     *
     *   ---------------------
     *
     *   ex 1)
     *
     *   ex 2)
     *
     *    s = -1, b = 4
     *
     *    -> sum = 7
     *    -> sum1 =  ((4 + -1) * 6 ) / 2 = 9
     *
     *    sum1 != sum
     *     -> sum - sum1
     *
     *
     *   ex 3)
     *
     *    s = 7, b = 12
     *
     *    -> sum = 47
     *    -> sum1 = ((12 + 7) * 6) / 2 = 57
     *
     *    sum1 != sum
     *      -> sum - sum1 = 10
     *      if s > 1, return 1 // ????
     *
     *
     *
     */
    //  IDEA 2) SET ???
    public int firstMissingPositive(int[] nums) {
        // edge

        // ??
        int smallest = Integer.MAX_VALUE; // ????
        int biggest = -1 * Integer.MAX_VALUE; // ????

        Set<Integer> set = new HashSet<>();
        for(int x: nums){
            if(x > 0){
                set.add(x);
                smallest = Math.min(smallest, x);
                biggest = Math.max(biggest, x);
            }
        }

        // if smallest > 1
        if(smallest > 1){
            return 1;
        }

        while(smallest <= biggest){
            if(!set.contains(smallest) && smallest != 0){
                return smallest;
            }
            smallest += 1;
        }


        return Math.max(biggest + 1, 1); // ?????
    }




    // LC 1991
    // 15.07 - 17 pm
    /**
     * -> Return the leftmost middleIndex
     *   that satisfies the condition, or -1 if there is no such index.
     *
     *    - 0 based idx
     *    - middleIndex:
     *
     *      nums[0] + nums[1] + ... + nums[middleIndex-1] == nums[middleIndex+1] + nums[middleIndex+2] + ... + nums[nums.length-1].
     *
     *
     *  ----------------------------
     *
     *   IDEA 1) PREFIX SUM + ARRAY LOOP
     *
     *
     *  ----------------------------
     *
     *   ex 1)
     *
     *   Input: nums = [2,3,-1,8,4]
     *   Output: 3
     *
     *   -> sum = 16
     *
     *   -> loop over nums
     *
     *
     *   [2,3,-1,8,4]
     *    i             ps=2
     *
     *   [2,3,-1,8,4]
     *      i            ps=5
     *
     *   [2,3,-1,8,4]
     *         i          ps=4
     *
     *
     *  [2,3,-1,8,4]
     *          i      ps=12
     *
     *
     *   ex 1)
     *
     *
     */
    public int findMiddleIndex(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1; // ??
        }
        if(nums.length == 1){
            return 0;
        }

        int sum = 0;
        for(int x: nums){
            sum += x;
        }
        int prefixSum = 0;
        for(int i = 0; i < nums.length; i++){
            int x = nums[i];
            prefixSum += x;
            if( (prefixSum - x) * 2 + x == sum ){
                return i;
            }
        }

        // if the `middleIndex` is in the last element
        if(sum - nums[nums.length - 1] == 0){
            return nums.length -1;
        }

        return -1;
    }



    // LC 525
    // 15.19 -29 pm
    /**
     *
     * -> return the `maximum` length of
     *   a `contiguous` subarray with `an equal number of 0 and 1`.
     *
     *
     *   - nums: binary arr
     *
     *
     *  ----------------
     *
     *   IDEA 1) MATH + ARRAY OP
     *
     *    -> if `satisfied` (e.g. 0 and 1 cnt eqauls)
     *       -> sum * 2 = len
     *
     *
     *   IDEA 2) BRUTE FORCE
     *
     *   IDEA 3) 2 POINTERS
     *
     *   IDEA 4) SLIDE WINDOW
     *
     *
     *   IDEA 5) 2 POINTERS + 0, 1 cnt map
     *
     *
     *   ----------------
     *
     *   ex 1)
     *
     *   ex 2)
     *
     *   ex 3)
     *   Input: nums = [0,1,1,1,1,1,0,0,0]
     *   Output: 6
     *
     *
     *   -> total_z_cnt = 4
     *      total_one_cnt = 5
     *
     *   [0,1,1,1,1,1,0,0,0]
     *    i                    remain_z = 3, remain_one = 5
     *
     *   [0,1,1,1,1,1,0,0,0]
     *      i                remain_z = 4, remain_one = 4
     *
     *
     *  [0,1,1,1,1,1,0,0,0]
     *       i             remain_z = 3, remain_one = 4
     *
     *    ....
     *
     *  [0,1,1,1,1,1,0,0,0]
     *   l         i         remain_z = 3, remain_one = 0
     *                       one_cnt = 5, remain_z = 3
     *
     */
    // IDEA: HASHMAP
    /**
     * 	 IDEA)
     *
     * 	 1. Convert the problem into tracking equal number of
     * 	    1s and 0s as a zero net count.
     *
     * 	 2. Maintain a count where:
     * 	     - Add +1 for each 1
     * 	     - Subtract -1 for each 0
     *
     * 	3.	If the same count value appears again,
     * 	    the subarray between the first and current index is balanced.
     *
     */
    // Map: {count -> first index where this count occurred}
    /**
     *
     * 	•	We use a map to store the first index
     * 	     where a specific count value occurs.
     *
     * 	•	count = 0 is added with index = -1
     *    	to handle subarrays starting at index 0.
     *
     *
     * 	•	Why? If from index 0 to i, the net count is 0,
     * 	    that means the subarray is balanced.
     *
     *
     * 	-> map : { count : first_idx_when_cnt_existed }
     */
    // LC 525
    // 17.31 - 44 PM
    /**
     *  IDEA 1) HASH MAP + prefix sum
     *
     *   -> prefix:
     *     - 0 : -1
     *     - 1: + 1
     *
     *    map : { prefix: idx } // save left most
     *
     *    -> so we loop over nums,
     *       update prefix, map on the same time
     *
     *       when we meet the same `prefix` again
     *       e.g. map has that prefix
     *       we are sure there MUST exist a sub array
     *       that with `equal number of 0 and 1.`
     *
     *       ( j > i )
     *       prefix[0..i] =  prefix_a
     *
     *       prefix[0..j] =  prefix_b
     *
     *       -> prefix[0..j] - prefix[0..i] = 0
     *
     *       -> `equal number of 0 and 1.`
     *
     *
     */
    public int findMaxLength(int[] nums) {
        // edge
        if (nums == null || nums.length == 0)
            return 0;


        int prefix = 0;
        int maxLen = 0;

        // / Map: { PrefixSum : First_Index_It_Occurred }
        //  map : { prefix: idx } // save left most idx ONLY (given same prefix)
        Map<Integer, Integer> map = new HashMap<>();
        // ???? init
        // ???? // Base Case: A sum of 0 exists at index -1
       // map.put(0, -1);


        for(int i = 0; i < nums.length; i++){
            int x = nums[i];
            if(x == 0){
                prefix -=1;
            }else{
                prefix += 1;
            }

            if(map.containsKey(prefix)){
                // ???
                maxLen = Math.max(maxLen, i - map.get(prefix) + 1);
            }else{
                map.put(prefix, i);
            }
        }


        return maxLen;
    }









    public int findMaxLength_97(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 0;
        }
        // -> map : { count : first_idx_when_cnt_existed }
        Map<Integer, Integer> map = new HashMap<>();
        //map.put(0, -1); // ????
        map.put(0, -1); // important: count 0 initially at index -1


        int max_len = 0;
        int count = 0;

        for (int i = 0; i < nums.length; i++) {
            if(nums[i] == 0){
                count -= 1;
            }else{
                count += 1;
            }
            map.put(i, count); // ???

            if (map.containsKey(count)) {
                max_len = Math.max(max_len, i - map.get(count));
            } else {
                map.put(count, i); // only put the first occurrence
            }

        }


        return max_len;
    }



    // brute force ???
    public int findMaxLength_99(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 0;
        }

        int maxLen = 0;
        for(int l = 0; l < nums.length; l++){
            int zeroCnt = 0;
            int oneCnt = 0;
            for(int r = l; r < nums.length; r++){
                if(nums[r] == 0){
                    zeroCnt += 1;
                }else{
                    oneCnt += 1;
                }
                if(zeroCnt == oneCnt){
                    maxLen = Math.max(maxLen, r - l + 1);
                }
            }
        }

        return maxLen;
    }



    // LC 974
    // 16.06 - 16 pm
    /**
     * -> return the `number` of non-empty
     *   subarrays that have a sum divisible by k.
     *
     *    NOTE: A subarray is a contiguous part of an array.
     *
     *    - nums: int arr
     *    - k: int
     *
     *
     *
     *  ---------------
     *
     *   IDEA 1) HASHMAP + PREFIX SUM ???
     *
     *    if  ( prefix_sum - x ) % k == 0
     *      ->  x % k = prefix_sum % k
     *      ->
     *
     *
     *  IDEA 2) BRUTE FORCE
     *
     *  IDEA 3) slide window ???
     *
     *
     *  IDEA 3) prefix sum + cnt ???
     *
     *  ---------------
     *
     */
    // LC 974
    // 17.14 - 24 PM
    /**
     *  IDEA 1) MOD + HASHMAP + PREFIX SUM
     *
     *  -> if
     *      prefix[0..i] % k  = a
     *      prefix[0..j] % k  = a
     *
     *      ( j > i )
     *
     *      ->
     *
     *      ( prefix[0..j] - prefix[0..i] ) % k = 0
     *
     *      -> so it is divisible by k !
     *
     */
    public int subarraysDivByK(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0] % k == 0 ? 1 : 0;
        }

        int cnt = 0;

        // map: { remaining : cnt } // ???
        // map: { remainder -> frequency }
        Map<Integer, Integer> map = new HashMap<>();
        // NOTE !!!
        map.put(0, 1);


        int prefix = 0;

        /**
         *  IDEA 1) MOD + HASHMAP + PREFIX SUM
         *
         *  -> if
         *      prefix[0..i] % k  = a
         *      prefix[0..j] % k  = a
         *
         *      ( j > i )
         *
         *      ->
         *
         *      ( prefix[0..j] - prefix[0..i] ) % k = 0
         *
         *      -> so it is divisible by k !
         *
         */

        for(int x: nums){
            prefix += x;
            int mod = prefix % k; // ???

            // NOTE !!!
            if(mod < 0){
                mod += k;
            }

            if(map.containsKey(mod)){
                // ???
                cnt += map.get(mod);
            }
            // update map
            map.put(mod, map.getOrDefault(mod, 0) + 1);
        }


        return cnt;
    }








    public int subarraysDivByK_99(int[] nums, int k) {
        // edge

        int cnt = 0;

        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        int prefix = 0;
        for(int x: nums){
            prefix += x;
            map.put(prefix, map.getOrDefault(prefix, 0) + 1);
        }
        // ???
        for(int x: nums){
            // ???
            if(x % k == 0){
                cnt += 1;
            }
            /**
             *      *    if  ( prefix_sum - x ) % k == 0
             *      *      ->  x % k = prefix_sum % k
             *      *      ->
             */
            //if(map.containsKey())
        }


        return cnt;
    }


    // LC 229
    // 15.18 - 28 pm
    /**
     *  -> Given an integer array of size n,
     *    find `all elements` that appear more than ⌊ n/3 ⌋ times.
     *
     *
     *    ---------
     *
     *    IDEA 1) HASHMAP
     *
     *
     */
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> res = new ArrayList<>();
        // edge
        if(nums == null || nums.length == 0){
            return res;
        }
        if(nums.length == 1){
            res.add(nums[0]);
        }
        int n = nums.length;
        // { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }
        //List<Integer> res = new ArrayList<>();
        for(int k: map.keySet()){
            if(map.get(k) > n / 3){
                res.add(k);
            }
        }

        return res;
    }

    // LC 137
    public int singleNumber(int[] nums) {

        return 0;
    }


    // LC 659
    // 15.29 - 39 pm
    /**
     *
     * -> Return
     *     - true
     *       - if you can `split` nums according to the above conditions,
     *         (one or more subsequence)
     *    -  false otherwise.
     *
     *    Conditions:
     *     - Each subsequence is a `consecutive increasing` sequence
     *       (i.e. each integer is `exactly one more` than
     *       the previous integer).
     *
     *     - All subsequences have a length of 3 or more.
     *
     *
     *    NOTE:
     *      - nums is sorted in `ascending` order
     *
     *
     *   -------------------
     *
     *   IDEA 1) HASHMAP ?? + GREEDY ???
     *
     *    { val : cnt }
     *
     *
     *
     *    -------------------
     *
     *
     *    ex 1)
     *
     * Input: nums = [1,2,3,3,4,5]
     * Output: true
     *
     * -> map : { 1: 1, 2: 1, 3: 2, 4: 1, 5 : 1}
     *
     *  -> [1,  2, 3  ], {, 3: 1, 4: 1, 5 : 1}
     *
     *
     *
     */
    /**
     * Gemini
     * To solve LC 659 (Split Array into Consecutive Subsequences), the most efficient approach is a Greedy Algorithm using two HashMaps.
     *
     * The goal is to ensure every subsequence has a length of at least 3.
     *
     * 🧠 The Strategy: Greedy with "Future Planning"
     * We use two maps:
     *
     * freqMap: Keeps track of how many of each number are left to be used.
     *
     * hypotheticalMap: Keeps track of how many subsequences are "waiting" for a specific number to continue their chain.
     *
     * The Greedy Rules:
     *
     * If a number x can join an existing subsequence (it was "waiting" for x), prioritize that. This keeps chains long.
     *
     * If it can't join one, try to start a new subsequence of length 3 using (x,x+1,x+2).
     *
     * If it can't do either, return false.
     */


    // 10.18 - 28 am
    /**
     *
     *
     *
     */
    public boolean isPossible(int[] nums) {
        return false;
    }






    // IDEA: PQ + HASHMAP
    // PQ : get smallest key,
    // hashmap : { val : freq }
    public boolean isPossible_00(int[] nums) {
        // edge
        // edge case
        if (nums == null || nums.length == 0) {
            return false;
        }

        // Map to track the subsequences
        Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();

        for(int x: nums){
            // ???
            int subSeqCnt = 0;
            if(map.containsKey(x - 1)){
                subSeqCnt = map.get(x - 1).poll();
                if (map.get(x - 1).isEmpty()) {
                    map.remove(x - 1);  // Remove if no more subsequences end with x-1
                }
            }

            PriorityQueue<Integer> curPQ = map.getOrDefault(x, new PriorityQueue<>());
            curPQ.offer(subSeqCnt + 1);
            map.put(x, curPQ);
        }




        return false;
    }


    public boolean isPossible_99(int[] nums) {
        // edge

        // Keeps track of how many of each number are left to be used.
        Map<Integer, Integer> freqMap = new HashMap<>();
        // Keeps track of how many subsequences are "waiting" for a specific number to continue their chain.
        Map<Integer, Integer> hypoMap = new HashMap<>();

        // 1. Fill the frequency map
        for (int x : nums) {
            freqMap.put(x, freqMap.getOrDefault(x, 0) + 1);
        }


        // 2.  loop greedy
        for (int x : nums) {

            if(freqMap.get(x) == 0){
                continue;
            }

            // // Option A: Join an existing subsequence that needs 'x'
            if(hypoMap.getOrDefault(x, 0) > 0){
               // ???
                freqMap.put(x, freqMap.get(x) - 1);
                // ??
                hypoMap.put(x, hypoMap.getOrDefault(x, 0) + 1);
                hypoMap.put(x + 1, hypoMap.getOrDefault(x + 1, 0) + 1);
            }
            // // Option B: Start a new subsequence of length 3: [x, x+1, x+2]
            else if (freqMap.get(x+1) > 0 && freqMap.get(x+2) > 0){
                freqMap.put(x, freqMap.get(x) - 1);
                freqMap.put(x + 1, freqMap.get(x + 1) - 1);
                freqMap.put(x + 2, freqMap.get(x + 2) - 1);
                //
                hypoMap.put(x + 3, hypoMap.getOrDefault(x + 3, 0) + 1);
            }else{
                return false;
            }

        }



        return true;
    }



    // LC 992
    // 16.11 - 21 pm
    /**
     *
     *  ->  return the `number of `good subarrays` of nums.
     *
     *   - good array:
     *      - is an array where the number of different integers
     *        in that array is `exactly k.`
     *
     *   - nums: int arr
     *   - k: int
     *
     *
     *   -------------
     *
     *   IDEA 1) SLIDE WINDOW
     *
     *
     *   IDEA 2) BRUTE FORCE
     *
     *
     *
     *   -----------------
     *
     *   ex 1)
     *
     *   Input: nums = [1,2,1,2,3], k = 2
     *   Output: 7
     *
     *
     *   [1,2,1,2,3]
     *    l r         [1,2]
     *
     *  [1,2,1,2,3]
     *   l   r        [1,2], [1,2,1]
     *
     *  [1,2,1,2,3]
     *   l     r     [1,2], [1,2,1], [1,2,1,2]
     *
     *
     *  [1,2,1,2,3]
     *     l r      [1,2], [1,2,1], [1,2,1,2] [2,1]
     *
     *
     *  [1,2,1,2,3]
     *     l   r     [1,2], [1,2,1], [1,2,1,2] [2,1] [2,1,2]
     *
     *
     *  [1,2,1,2,3]
     *       l r     [1,2], [1,2,1], [1,2,1,2] [2,1] [2,1,2] [1,2]
     *
     *
     *  [1,2,1,2,3]
     *         l r   [1,2], [1,2,1], [1,2,1,2] [2,1] [2,1,2] [1,2]
     *               [2,3]
     *
     *
     */
    // IDEA 1) SLIDE WINDOW
    /**  // SLIDE WINDOW pattern:
     *
     *  for(int l = 0; l < nums.len; l++){
     *      // ...
     *      while( condition ){
     *          // ...
     *          r += 1;
     *      }
     *      r = l+1; // ???
     *  }
     *
     *
     *
     */


    // 9.42 - 52 am
    /**
     *
     *
     *
     */
    // IDEA 1) SLIDE WINDOW
    // r move first, then adjust l idx ...
    public int subarraysWithKDistinct(int[] nums, int k) {
        // edge

        int n = nums.length;
        int cnt = 0;

        int l = 0;

        //Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> map = new HashMap<>();

        for(int r = 0; r < n; r++){
            //Set<Integer> set = new HashSet<>();
            // ??
            // l = r;
            //set.add(nums[r]);
            map.put(nums[r], map.getOrDefault(nums[r], 0) + 1);


            // ???
            if(map.size() == k){
                cnt += 1;
            }

            while(map.size() > k){
                l += 1;
                // ???
                //set.remove(nums[l]);
                map.put(nums[l], map.get(nums[l])  - 1);
                if(map.get(nums[l]) == 0){
                    map.remove(nums[l]);
                }
            }
        }


        return cnt;
    }









    public int subarraysWithKDistinct_99(int[] nums, int k) {
        // edge

        int cnt = 0;
        //int r = 0; // ??
        int n = nums.length;

        for(int l = 0; l < n; l++){
           // int distinct = 0;
            Set<Integer> set = new HashSet<>();
            for (int r = l; r < n; r++){
                set.add(nums[r]);
                if(set.size() == k){
                    cnt += 1;
                }else if(set.size() > k){
                    break;
                }
            }
//            // ???
//            //int r = 0;
//            int r = l; // ???
//            Set<Integer> set = new HashSet<>();
//            while(set.size() <= k && r < nums.length){
//                System.out.println(">>> l = " + l +
//                        ", r = " + r +
//                        ", set = " + set +
//                        ", cnt = " + cnt);
//                if(set.size() == k){
//                    cnt += 1;
//                }
//                set.add(nums[r]);
//                r += 1;
//            }
        }


        return cnt;
    }


    // LC 1672
    // 7.37 - 47 am
    /**
     *
     *  -> . Return the wealth that the richest customer has.
     *
     *  -----------
     *
     *  IDEA 1) ARRAY OP
     *
     */
    public int maximumWealth(int[][] accounts) {
        // edge

        int l = accounts.length;
        int w = accounts[0].length;

        int maxVal = 0;
        for(int i = 0; i < l; i++){
            int tmp = getSum(accounts[i]);
            maxVal = Math.max(maxVal, tmp);
        }

        return maxVal;
    }

    private int getSum(int[] x){
        int res = 0;
        for(int i: x){
            res += i;
        }
        return res;
    }





    // LC 1431
    // 7.50 - 8.00 am
    /**
     * -> Return a boolean array result of length n,
     *    where result[i] is true if, after giving the ith
     *    kid all the extraCandies,
     *   they will have the greatest number of candies
     *   among all the kids, or false otherwise.
     *
     *  ----------------------
     *
     *   IDEA 1) BRUTE FORCE + ARR OP
     *
     *
     *
     *  ----------------------
     *
     */
    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        // edge
        List<Boolean> res = new ArrayList<>();

        int max = 0;
        for(int x: candies){
            max = Math.max(x, max);
        }

        for(int i = 0; i < candies.length; i++){
            //max = Math.max(x, max);
            int x = candies[i];
            if(x + extraCandies >= max){
                res.add(true);
            }else{
                res.add(false);
            }
        }

        return res;
    }


    // LC 75
    // 8.09 - 19 am
    /**
     *    -> sort them in-place so that objects
     *      of the same color are adjacent,
     *     with the colors in the order red, white, and blue.
     *
     *
     *     NOTE: color needs to be in order:
     *        -  red, white, and blue.
     *
     *        - 0: red, 1: white, 2: blue
     *
     *   --------------------------
     *
     *    IDEA 1) SORTING
     *       - any sort algo
     *        ( small -> big )
     *
     *    IDEA 2) 2 POINTERS ???
     *
     *
     *    --------------------------
     *
     *
     *    ex 1)
     *
     * Input: nums = [2,0,2,1,1,0]
     * Output: [0,0,1,1,2,2]
     *
     *
     *    [2,0,2,1,1,0]
     *     l         r
     *
     *    [0,0,2,1,1,2]
     *       l     r
     *
     *   [0,0,2,1,1,2]
     *        l r
     *
     *  [0,0,2,1,1,2]
     *       l     r
     *
     *  [0,0,2,1,1,2]
     *       l   r
     *
     * [0,0,1,1,2,2]
     *        l r
     *
     *
     */
    // bubble sort (small -> big)
    public void sortColors(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return;
        }
        if(nums.length == 1){
            return;
        }
        // ??
        int n = nums.length;
        for(int l = 0; l < n; l++){
            for(int r = l + 1; r < n; r++){
                int tmp = nums[r];
                if(nums[r] < nums[l]){
                    nums[r] = nums[l];
                    nums[l] = tmp;
                }
            }
        }
    }

    // LC 274
    // 8.33 - 43 am
    /**
     *
     *  -> return the researcher's h-index
     *
     *   - citations[i]
     *     - the number of citations a
     *       researcher received for their ith paper
     *
     *
     *   - h-idx definition:
     *       -  maximum value of h such
     *          that the given researcher
     *          has published at least h papers
     *          that have each been cited at least h times.
     *
     *
     *  ----------------
     *
     *   IDEA 1) SORT
     *
     *
     *
     *
     *  ----------------
     *
     *
     *  ex 1)
     *
     *  citations = [3,0,6,1,5]
     *
     *  -> (sort)
     *
     *   [0,1,3,5,6]
     *
     *   -> loop over sorted arr,
     *      check each val, and see if it is `h-idx`
     *      e.g.
     *         1. cnt(x >= val) = h
     *         2. num above >= h
     *
     *
     *
     *  ex 2)
     *
     *   Input: citations = [1,3,1]
     *
     *   -> (sort)
     *
     *   [1,1,3]
     *
     *
     *
     */
    public int hIndex(int[] citations) {
        // edge ??

        // sort (small -> big)
        System.out.println(">>> before sort  citations = " + Arrays.toString(citations));
        Arrays.sort(citations); // ??
        System.out.println(">>> after sort  citations = " + Arrays.toString(citations));

        // map: { val : last_seen_idx }
        Map<Integer, Integer> map = new HashMap<>();

        // map: { val : cnt }
        Map<Integer, Integer> cntMap = new HashMap<>();
        int n = citations.length;
        for(int i = 0; i < n; i++){
            int val = citations[n];
            map.put(val, i);

            cntMap.put(val, cntMap.getOrDefault(val, 0) + 1);
        }

        for(int i = 0; i < n; i++){
            int val = citations[n];
            /**
             *    *   -> loop over sorted arr,
             *      *      check each val, and see if it is `h-idx`
             *      *      e.g.
             *      *         1. cnt(x >= val) = h
             *      *         2. num above >= h
             */
            boolean isHIndex = (n - map.get(val) + cntMap.get(val) == val);
            if(isHIndex){
                return val;
            }
        }

        return -1;  // ????
    }


    // LC 680
    // 10.39 - 49 am
    /**
     *
     * -> Given a string s, return true if
     *   the s can be palindrome after
     *   `deleting at most one character from it.`
     *
     *
     *  ----------------
     *
     *   IDEA 1) 2 POINTERS
     *    left, right pointers.
     *    0, n-1.
     *
     *    if l_val == r_val:
     *       l++, r--
     *    else:
     *       if r != l + 1:
     *          return false
     *       return true
     *
     *   IDEA 2)  for loop + `extend from middle`
     *  ----------------
     *
     *
     *   ex 1)
     *
     *     s = "abb"
     *
     *     ->
     *
     *      "abb"
     *        l
     *        r
     *
     *
     *      "abb"
     *       lr
     *
     */
    // IDEA 2)  for loop + `extend from middle`
    public boolean validPalindrome(String s) {
        // edge
        if(s == null || s.isEmpty()){
            return true;
        }
        if(s.length() == 1){
            return true;
        }

        for(int i = 0; i < s.length(); i++){
            // ???
            //String sub = s.substring(i);
            String sub = s.substring(0,i) + s.substring(i+1);
            System.out.println(">>> sub str = " + sub);
            if(isPali(sub)){
                return true;
            }
        }

        return false;

    }

    private boolean isPali(String x){
        if(x == null || x.isEmpty()){
            return true;
        }
        int l = 0;
        int r = x.length() -1;
        while(r > l){
            if(x.charAt(l) != x.charAt(r)){
                return false;
            }
            l += 1;
            r -= 1;
        }
        return true;
    }


    // LC 917
    // 11. 24 - 34 am
    /**
     *
     *  -> Return s after reversing it.
     *
     *
     *
     * ------------------
     *
     *  IDEA 1) 2 POINTERS and ONLY
     *         implement on alphabet element
     *
     *
     * ------------------
     *
     *
     *
     */
    public String reverseOnlyLetters(String s) {
        // edge

        int n = s.length();
        int l = 0;
        int r = n -1;

        // ???
        String alpha = "abcdefghijklmnopqrstuvwxyz";

        // ???
        String[] s_arr = s.split("");


        while(r > l && r > 0 && l < n){
//            String lVal = String.valueOf(s.charAt(l));
//            String rVal = String.valueOf(s.charAt(r));
            String lVal = s_arr[l];
            String rVal = s_arr[r];
            while(!alpha.contains(lVal)){
                l += 1;
            }
            while(!alpha.contains(rVal)){
                r -= 1;
            }
            // swap
            String tmp =  s_arr[r];
            s_arr[r] = s_arr[l];
            s_arr[l] = tmp;

            l += 1;
            r -= 1;
        }

        System.out.println(">>> s_arr = " + s_arr);

        // transform back to string
        String res = s_arr.toString(); // ???
        return res;
    }

}
