package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/description/

import java.util.HashMap;
import java.util.Map;

/**
 *  1074. Number of Submatrices That Sum to Target
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a matrix and a target, return the number of non-empty submatrices that sum to target.
 *
 * A submatrix x1, y1, x2, y2 is the set of all cells matrix[x][y] with x1 <= x <= x2 and y1 <= y <= y2.
 *
 * Two submatrices (x1, y1, x2, y2) and (x1', y1', x2', y2') are different if they have some coordinate that is different: for example, if x1 != x1'.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[0,1,0],[1,1,1],[0,1,0]], target = 0
 * Output: 4
 * Explanation: The four 1x1 submatrices that only contain 0.
 * Example 2:
 *
 * Input: matrix = [[1,-1],[-1,1]], target = 0
 * Output: 5
 * Explanation: The two 1x2 submatrices, plus the two 2x1 submatrices, plus the 2x2 submatrix.
 * Example 3:
 *
 * Input: matrix = [[904]], target = 0
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= matrix.length <= 100
 * 1 <= matrix[0].length <= 100
 * -1000 <= matrix[i][j] <= 1000
 * -10^8 <= target <= 10^8
 *
 */
public class NumberOfSubmatricesThatSumToTarget {

    // V0
//    public int numSubmatrixSumTarget(int[][] matrix, int target) {
//
//    }

    // V0-1
    // IDEA: PREFIX SUM + HASHMAP (gemini)
    public int numSubmatrixSumTarget_0_1(int[][] matrix, int target) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int count = 0;

        // 1. Calculate prefix sums for each row
        // This allows us to get the sum of any row segment in O(1)
        for (int i = 0; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                matrix[i][j] += matrix[i][j - 1];
            }
        }

        // 2. Iterate through all possible pairs of columns (left and right)
        for (int left = 0; left < cols; left++) {
            for (int right = left; right < cols; right++) {

                // Map stores: current prefix sum -> frequency
                Map<Integer, Integer> map = new HashMap<>();
                map.put(0, 1); // Base case: a sum of 0 is seen once

                int currentRunningSum = 0;

                // 3. For the fixed column range, iterate through rows (1D subarray sum logic)
                for (int i = 0; i < rows; i++) {
                    // Sum of the current row between 'left' and 'right' columns
                    int rowSum = matrix[i][right] - (left > 0 ? matrix[i][left - 1] : 0);
                    currentRunningSum += rowSum;

                    // If (currentRunningSum - target) exists in map, it means there is
                    // a submatrix ending at row 'i' that sums to target.
                    if (map.containsKey(currentRunningSum - target)) {
                        count += map.get(currentRunningSum - target);
                    }

                    // Update the map with the current prefix sum
                    map.put(currentRunningSum, map.getOrDefault(currentRunningSum, 0) + 1);
                }
            }
        }

        return count;
    }


    // V0-2
    // IDEA: PREFIX SUM + HASHMAP (gpt)
    public int numSubmatrixSumTarget_0_2(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        int result = 0;

        // Fix top row
        for (int top = 0; top < m; top++) {

            int[] colSum = new int[n];

            // Expand bottom row
            for (int bottom = top; bottom < m; bottom++) {

                // Build compressed 1D array
                for (int c = 0; c < n; c++) {
                    colSum[c] += matrix[bottom][c];
                }

                // Count subarrays with sum = target
                result += subarraySum_0_2(colSum, target);
            }
        }

        return result;
    }

    // LC 560: Subarray Sum Equals K
    private int subarraySum_0_2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;

            count += map.getOrDefault(sum - target, 0);

            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        return count;
    }


    // V1-1
    // IDEA: HASHMAP
    // https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/solutions/4636406/cjavapythonjavascript-2-approaches-expla-g6af/
    public int numSubmatrixSumTarget_1_1(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        for (int row = 0; row < m; row++) {
            for (int col = 1; col < n; col++) {
                matrix[row][col] += matrix[row][col - 1];
            }
        }

        int count = 0;

        for (int c1 = 0; c1 < n; c1++) {
            for (int c2 = c1; c2 < n; c2++) {
                Map<Integer, Integer> map = new HashMap<>();
                map.put(0, 1);
                int sum = 0;

                for (int row = 0; row < m; row++) {
                    sum += matrix[row][c2] - (c1 > 0 ? matrix[row][c1 - 1] : 0);
                    count += map.getOrDefault(sum - target, 0);
                    map.put(sum, map.getOrDefault(sum, 0) + 1);
                }
            }
        }

        return count;
    }


    // V1-2
    // IDEA: ARRAYS
    // https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/solutions/4636406/cjavapythonjavascript-2-approaches-expla-g6af/
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int sum = 0;
        Map<Integer, Integer> mp = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            if (sum == k) {
                count++;
            }

            if (mp.containsKey(sum - k)) {
                count += mp.get(sum - k);
            }

            mp.put(sum, mp.getOrDefault(sum, 0) + 1);
        }

        return count;
    }

    public int numSubmatrixSumTarget_1_2(int[][] matrix, int target) {
        int count = 0;

        for (int i = 0; i < matrix.length; i++) {
            int[] sum = new int[matrix[0].length];

            for (int j = i; j < matrix.length; j++) {
                for (int k = 0; k < matrix[0].length; k++) {
                    sum[k] += matrix[j][k];
                }

                count += subarraySum(sum, target);
            }
        }

        return count;
    }



    // V2
    // IDEA: PREFIX SUM
    // https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/solutions/7845138/9426-arrays-and-prefix-sum-by-triyaambak-pdd6/

    // How to calculate rolling sum of the matrix ?
    // [ 1 2 ]
    // [ 3 4 ]
    // To find the sum at index 4
    // We need to add sum at index 3 + index 2 - index 1
    // Why ?
    // Because the value at index 1 will be calculated twice while finding the sum of submatrix ending at
    // index 2 and index 3 , hence we need to remove it once

    // [ 1 2 3 ]
    // [ 4 5 6 ]
    // [ 7 8 9 ]
    // Here to find the sum of value of submatrix starting at index 5 and ending at index 9
    // We need to subtract submatrix starting at 1 and ending at 7
    // We need to subtract submatrix starting from 1 and ending at 3
    // We need to add submatrix starting from 1 and ending at 5
    // Since we will subtracting this submatrix twice , hence to nullify it , we need to add once
    public int numSubmatrixSumTarget_2(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int dp[][] = new int[m + 1][n + 1];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++)
                dp[r + 1][c + 1] = matrix[r][c] + dp[r][c + 1] + dp[r + 1][c] - dp[r][c];
        }

        int count = 0;

        for (int sR = 0; sR < m; sR++) {
            for (int sC = 0; sC < n; sC++) {
                for (int eR = sR; eR < m; eR++) {
                    for (int eC = sC; eC < n; eC++) {

                        int s = dp[eR + 1][eC + 1] + dp[sR][sC] - dp[eR + 1][sC] - dp[sR][eC + 1];

                        if (target == s)
                            count++;

                    }
                }
            }
        }

        return count;
    }



    // V3



}
