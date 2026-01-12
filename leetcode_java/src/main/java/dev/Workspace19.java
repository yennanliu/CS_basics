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
    public int wiggleMaxLength(int[] nums) {

        return 0;
    }






}
