package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximal-square/description/
/**
 * 221. Maximal Square
 *
 * Medium
 * Topics
 * Companies
 * Given an m x n binary matrix filled with 0's and 1's, find the largest square containing only 1's and return its area.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
 * Output: 4
 * Example 2:
 *
 *
 * Input: matrix = [["0","1"],["1","0"]]
 * Output: 1
 * Example 3:
 *
 * Input: matrix = [["0"]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 300
 * matrix[i][j] is '0' or '1'.
 *
 */
public class MaximalSquare {
    // V0
    // TODO : implement
//    public int maximalSquare(char[][] matrix) {
//
//    }


    // V1
    // IDEA : DP
    // https://leetcode.com/problems/maximal-square/solutions/61876/accepted-clean-java-dp-solution/
    public int maximalSquare_1(char[][] a) {
        if (a == null || a.length == 0 || a[0].length == 0)
            return 0;

        int max = 0, n = a.length, m = a[0].length;

        // dp(i, j) represents the length of the square
        // whose lower-right corner is located at (i, j)
        // dp(i, j) = min{ dp(i-1, j-1), dp(i-1, j), dp(i, j-1) }
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a[i - 1][j - 1] == '1') {
                    /**
                     *  NOTE !!!
                     *
                     *  dp logic:
                     *
                     *  4.	Transition Formula:
                     *
                     * 	  • The idea is to update each cell in dp based on the neighboring cells:
                     *
                     * 	  • If a[i-1][j-1] == '1', the cell at dp[i][j] will be
                     * 	    the minimum of its top (dp[i-1][j]), left (dp[i][j-1]),
                     * 	    and top-left (dp[i-1][j-1]) neighbors, plus 1.
                     *
                     * 	  • This is because a square at position (i, j) can only expand
                     * 	    if all three neighboring squares (above, left, and top-left)
                     * 	    can also form squares.
                     *
                     */
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                    /**
                     * 5. Updating Maximum:
                     * 	- Every time dp[i][j] is updated, the value is compared to
                     * 	  the current maximum side length (max) and updated accordingly
                     * 	  if a larger square is found.
                     *
                     */
                    max = Math.max(max, dp[i][j]);
                }
            }
        }

        // return the area
        /**
         *  example :
         *
         *  input:
         *
         *  [
         *     ['1', '0', '1', '0'],
         *     ['1', '0', '1', '1'],
         *     ['1', '1', '1', '1'],
         *     ['0', '1', '1', '1']
         * ]
         *
         * output:
         * [
         *     [0, 0, 0, 0, 0],
         *     [0, 1, 0, 1, 0],
         *     [0, 1, 0, 1, 1],
         *     [0, 1, 1, 2, 2],
         *     [0, 0, 1, 2, 3]
         * ]
         *
         */
        return max * max;
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/maximal-square/solutions/61805/evolve-from-brute-force-to-dp/
    public int maximalSquare_2(char[][] matrix) {
        int r=matrix.length;
        if(r==0) return 0;
        int c=matrix[0].length,edge=0;
        int[][] dp=new int[r+1][c+1];
        for(int i=1;i<=r;i++)
            for(int j=1;j<=c;j++) {
                if(matrix[i-1][j-1]=='0') continue;
                dp[i][j]=1+Math.min(dp[i-1][j],Math.min(dp[i-1][j-1],dp[i][j-1]));
                edge=Math.max(edge,dp[i][j]);
            }
        return edge*edge;
    }

    // V3_1
    // IDEA : DP
    // https://leetcode.com/problems/maximal-square/solutions/61828/my-java-dp-ac-solution-simple-and-easy-to-understand-with-explanation/
    public int maximalSquare_3_1(char[][] matrix) {

        //illegal check - no square can be formed
        if(matrix == null || matrix.length == 0) return 0;

        int result = 0;
        int[][] count = new int[matrix.length][matrix[0].length];

        //initialize first row and first column
        for(int i = 0; i < matrix.length; i ++) {
            count[i][0] = matrix[i][0] == '0' ? 0 : 1;
            result = Math.max(result, count[i][0]);
        }

        for(int i = 0; i < matrix[0].length; i ++) {
            count[0][i] = matrix[0][i] == '0' ? 0 : 1;
            result = Math.max(result, count[0][i]);
        }

        //start to transfer status to iterate each cell from (1, 1) to (m, n)
        //if i am a 0, the square stops, reset
        for(int i = 1; i < matrix.length; i++) {
            for(int j = 1; j < matrix[0].length; j++) {

                //I break the square reset myself to zero
                if(matrix[i][j] == '0') {
                    count[i][j] = 0;
                    continue;
                }

                //if I am 1, it depends if I can grow the size of the square, if I have a 0 guy around me,
                //I can only be a top left guy
                if(count[i - 1][j - 1] == 0 || count[i - 1][j] == 0 || count[i][j - 1] == 0) {
                    count[i][j] = 1;
                }
                //if guys around are the same size, I can be the right-bottom guy of a bigger square
                else if(count[i - 1][j - 1] == count[i - 1][j] && count[i - 1][j] == count[i][j - 1]) {
                    count[i][j] = count[i - 1][j - 1] + 1;
                }
                //guys around me not the same, I can only be the right-bottom guy of a least square
                else {
                    count[i][j] = Math.min(Math.min(count[i - 1][j - 1], count[i - 1][j]),
                            count[i][j - 1]) + 1;
                }
                result = Math.max(result, count[i][j]);
            }
        }
        return result * result;
    }

    // V3_2
    // https://leetcode.com/problems/maximal-square/solutions/61828/my-java-dp-ac-solution-simple-and-easy-to-understand-with-explanation/
    public int maximalSquare_3_2(char[][] matrix) {

        if(matrix == null || matrix.length == 0) return 0;

        int result = 0;
        int[][] count = new int[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i ++) {
            count[i][0] = matrix[i][0] == '0' ? 0 : 1;
            result = Math.max(result, count[i][0]);
        }

        for(int i = 0; i < matrix[0].length; i ++) {
            count[0][i] = matrix[0][i] == '0' ? 0 : 1;
            result = Math.max(result, count[0][i]);
        }


        for(int i = 1; i < matrix.length; i++) {
            for(int j = 1; j < matrix[0].length; j++) {

                if(matrix[i][j] == '0') {
                    count[i][j] = 0;
                    continue;
                }

                count[i][j] = Math.min(Math.min(count[i - 1][j - 1], count[i - 1][j]),
                        count[i][j - 1]) + 1;
                result = Math.max(result, count[i][j]);
            }
        }
        return result * result;
    }

}
