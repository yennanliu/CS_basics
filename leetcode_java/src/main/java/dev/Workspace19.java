package dev;

public class Workspace19 {

    // LC 516
    // 6.57 am - 7.07 am
    /**
     *  -> Given a string s,
     *   find the `longest` palindrome (回文)
     *   subsequence's length in s.
     *
     *
     *   NOTE:
     *
     *   A subsequence is a sequence that can be
     *   derived from another sequence by `deleting`
     *   some or no elements `without` changing the order
     *   of the remaining elements.
     *
     * --------------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) SLIDE WINDOW
     *
     *  IDEA 3)  DP
     *
     *     - 2D DP ????
     *
     *     - DP def:  dp[i][j]:
     *              - `longest` palindrome (回文)
     *                 subsequence between [i, j] (index)
     *
     *    - DP eq:
     *             - dp[i][j] =
     *                 - if dp[i][j] == dp[
     *
     *    -------------
     *    DP:
     *
     *     - DP def:
     *        dp[i][j]:  longest  palindrome (回文) `subsequence` len in [i,j] idx
     *
     *     - DP eq:
     *         dp[i][j] =
     *            if s[i] == s[j+1]:
     *              dp[i][j] = max( dp[i][j], dp[i+1][j-1] + 2) // ???
     *            else:
     *             dp[i][j] = max( dp[i-1][j], dp[i][j+1]) // ???
     *
     *
     *
     * --------------
     *
     *
     *
     */
    // dp
    public int longestPalindromeSubseq(String s) {
        // edge
        if(s.length() <= 1){
            return s.length();
        }
        if(s.length() == 2){
            return s.charAt(0) == s.charAt(1) ? 2: 0;
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
        for(int i = n + 1; i > 0; i--){
            for(int j = n + 1; j > 0; j--){
                if(s.charAt(i) == s.charAt(j)){
                    //dp[i][j] = Math.max( dp[i][j], dp[i+1][j-1] + 2 );
                    dp[i][j] = dp[i+1][j-1] + 2;
                }else{
                    dp[i][j] =  Math.max( dp[i + 1][j], dp[i][j - 1] ); // ???
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
     *   return the length of the `longest wiggle subsequence` of nums.
     *
     *
     *     - NOTE:
     *       - A subsequence
     *           - by deleting some elements (possibly zero)
     *           from the original sequence, leaving the
     *           remaining elements in their original order.
     *
     *   -------------------
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) DP
     *
     *       1. get diff array
     *         [ n1-n0, n2-n1, ...]
     *
     *     - dp def:
     *
     *        boolean dp[i][j]: [i,j] subsequence is wiggle or not
     *
     *     - dp eq:
     *
     *          dp[i][j] =
     *             if  diff[j-2] * diff[j-1] < 1:
     *                dp[i + 1][j - 1] + 2   // ???
     *             else:
     *                 max(dp[i+1][j], dp[i][j-1]) // /??
     *
     *   -------------------
     *
     *
     *   ex 1)
     *
     *
     *   ex 2)
     *
     *    Input: nums = [1,17,5,10,13,15,10,5,16,8]
     *    Output: 7
     *
     *    ->
     *      diff = (16, -7, 3, -3, 6, -8)
     *
     *
     *
     *
     */
    public int wiggleMaxLength(int[] nums) {
        // edge

        int n = nums.length;

        int[] diff = new int[n];
        for(int i = 1; i < nums.length; i++){
            diff[i] = nums[i] - nums[i-1];
        }

        // edge: if all diff > 0 or < 0
        // -> not wiggle -> return 0

        int maxWiggleLen = 0;
        int[][] dp = new int[n+1][n+1]; // ????



        return maxWiggleLen;
    }






}
