package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/description/

import java.util.TreeSet;

/**
 *
 *  363. Max Sum of Rectangle No Larger Than K
 *
 *
 *  Given an m x n matrix matrix and an integer k, return the max sum of a rectangle in the matrix such that its sum is no larger than k.
 *
 * It is guaranteed that there will be a rectangle with a sum no larger than k.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[1,0,1],[0,-2,3]], k = 2
 * Output: 2
 * Explanation: Because the sum of the blue rectangle [[0, 1], [-2, 3]] is 2, and 2 is the max number no larger than k (k = 2).
 * Example 2:
 *
 * Input: matrix = [[2,2,-1]], k = 3
 * Output: 3
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 100
 * -100 <= matrix[i][j] <= 100
 * -105 <= k <= 105
 *
 *
 * Follow up: What if the number of rows is much larger than the number of columns?
 *
 *
 *
 *
 */

public class MaxSumOfRectangleNoLargerThanK {

    // V0
    // TODO : implement
//    public int maxSumSubmatrix(int[][] matrix, int k) {
//
//    }

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/solutions/2492107/easy-dp-beginner-level-easy-solution/
    public int maxSumSubmatrix_1(int[][] matrix, int tar) {
        int n=matrix.length,m=matrix[0].length,i,j,k,l,dp[][] = new int[n][m],val,max=Integer.MIN_VALUE,target=tar;
        for(i=0;i<n;i++){
            for(j=0;j<m;j++){
                dp[i][j]=matrix[i][j];
                if(j>0) dp[i][j]+=dp[i][j-1];
            }
        }
        for(i=0;i<n;i++){
            for(j=0;j<m;j++){
                if(i>0) dp[i][j]+=dp[i-1][j];
            }
        }
        for(i=0;i<n;i++){
            for(j=0;j<m;j++){
                for(k=i;k<n;k++){
                    for(l=j;l<m;l++){
                        val=dp[k][l];
                        if((i-1)>=0 && (j-1)>=0) val += dp[i-1][j-1];
                        if((i-1)>=0) val=val-dp[i-1][l];
                        if((j-1)>=0) val=val-dp[k][j-1];
                        if(val>max && val<=target) max=val;
                    }
                }
            }
        }
        return max;
    }

    // V2_1
    // IDEA : DP
    // https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/solutions/2488344/java-easy-and-simple-solution-90-faster-code/
    public int maxSumSubmatrix_2_1(int[][] arr, int k) {
        int rows = arr.length;
        int cols = arr[0].length;
        int maxK = Integer.MIN_VALUE;
        for(int i = 0; i < cols; i++){
            int dp[] = new int[rows];
            for(int j = i; j < cols; j++){
                for(int l = 0; l < rows; l++){
                    dp[l] += arr[l][j];
                }
                int currSum = maxSubArray(dp, k);
                maxK = Math.max(maxK, currSum);
                if(maxK == k)
                    return k;
            }
        }
        return maxK;
    }
    public int maxSubArray(int[] arr, int k) {
        int max = Integer.MIN_VALUE;
        int currSum = 0;
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        for (int i = 0; i < arr.length; i++) {
            currSum += arr[i];
            Integer ceilValue = set.ceiling(currSum - k);
            if(ceilValue != null) {
                max = Math.max(max, currSum - ceilValue);
            }
            set.add(currSum);
        }
        return max;
    }

    // V3_1
    // https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/solutions/2488527/java-beats-100-32ms-kadane-s-algorithm-and-faster-approach-w-video-explanation/
    // IDEA : Kadane's Algorithm
    public int maxSumSubmatrix_3_1(int[][] matrix, int k) {
        int result = Integer.MIN_VALUE;

        for(int left =0 ;left<matrix[0].length; left++){

            int[] rSum = new int[matrix.length];

            for(int right = left;right<matrix[0].length;right++){
                for(int row=0; row < matrix.length; row++)
                    rSum[row] += matrix[row][right];

                //explanation of these 3 loops starts from 02:00 in Video

                TreeSet<Integer> set = new TreeSet<>();

                set.add(0);
                int cs = 0;

                for(int a: rSum){
                    cs += a;

                    //if you don't understand what exactly is going on here
                    // then watch the video from 12:27

                    Integer target = set.ceiling(cs-k);

                    if(target !=null)
                        result = Math.max(result,cs-target);
                    set.add(cs);
                }
            }
        }
        return result;
    }

    // V3_2
    // https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/solutions/2488527/java-beats-100-32ms-kadane-s-algorithm-and-faster-approach-w-video-explanation/
    public int maxSumSubmatrix_3_2(int[][] matrix, int k) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i > 0) {
                    matrix[i][j] += matrix[i - 1][j];
                }
                if (j > 0) {
                    matrix[i][j] += matrix[i][j - 1];
                }
                if (i > 0 && j > 0) {
                    matrix[i][j] -= matrix[i - 1][j - 1];
                }
            }
        }
        int result = Integer.MIN_VALUE;

        int[][] dpHigh = new int[matrix.length][matrix.length + 1];
        int[][] dpLow = new int[matrix.length][matrix.length + 1];

        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int h = 1; h <= matrix.length - i; h++) {
                int theValue = getSum(matrix, i, matrix[0].length - 1, h, 1);
                dpLow[i][h] = theValue;
                dpHigh[i][h] = theValue;
                if (theValue == k) {
                    return theValue;
                }
                if (theValue < k) {
                    result = Math.max(result, theValue);
                }
            }
        }

        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int j = matrix[0].length - 2; j >= 0; j--) {
                for (int h = 1; h <= matrix.length - i; h++) {
                    int newSum = getSum(matrix, i, j, h, 1);
                    if (dpLow[i][h] > 0) {
                        dpHigh[i][h] += newSum;
                        dpLow[i][h] = newSum;
                    } else if (dpHigh[i][h] < 0) {
                        dpLow[i][h] += newSum;
                        dpHigh[i][h] = newSum;
                    } else {
                        dpHigh[i][h] += newSum;
                        dpLow[i][h] += newSum;
                    }
                    if (dpHigh[i][h] >= result && dpLow[i][h] <= k) {
                        for (int w = 1; w <= matrix[0].length - j; w++) {
                            int sum = getSum(matrix, i, j, h, w);
                            if (sum == k) {
                                return sum;
                            } else if (sum < k) {
                                result = Math.max(result, sum);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private int getSum(int[][] matrix, int i, int j, int h, int w) {
        int sum = matrix[i + h - 1][j + w - 1];
        if (i > 0) {
            sum -= matrix[i - 1][j + w - 1];
        }
        if (j > 0) {
            sum -= matrix[i + h - 1][j - 1];
        }
        if (i > 0 && j > 0) {
            sum += matrix[i - 1][j - 1];
        }
        return sum;
    }

}
