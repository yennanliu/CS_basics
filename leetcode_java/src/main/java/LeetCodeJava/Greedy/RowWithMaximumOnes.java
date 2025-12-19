package LeetCodeJava.Greedy;

// https://leetcode.com/problems/row-with-maximum-ones/description/
/**
 *  2643. Row With Maximum Ones
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a m x n binary matrix mat, find the 0-indexed position of the row that contains the maximum count of ones, and the number of ones in that row.
 *
 * In case there are multiple rows that have the maximum count of ones, the row with the smallest row number should be selected.
 *
 * Return an array containing the index of the row, and the number of ones in it.
 *
 *
 *
 * Example 1:
 *
 * Input: mat = [[0,1],[1,0]]
 * Output: [0,1]
 * Explanation: Both rows have the same number of 1's. So we return the index of the smaller row, 0, and the maximum count of ones (1). So, the answer is [0,1].
 * Example 2:
 *
 * Input: mat = [[0,0,0],[0,1,1]]
 * Output: [1,2]
 * Explanation: The row indexed 1 has the maximum count of ones (2). So we return its index, 1, and the count. So, the answer is [1,2].
 * Example 3:
 *
 * Input: mat = [[0,0],[1,1],[0,0]]
 * Output: [1,2]
 * Explanation: The row indexed 1 has the maximum count of ones (2). So the answer is [1,2].
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 100
 * mat[i][j] is either 0 or 1.
 *
 *
 *
 */
public class RowWithMaximumOnes {

    // V0
//    public int[] rowAndMaximumOnes(int[][] mat) {
//
//    }

    // V1
    // https://leetcode.ca/2023-02-24-2643-Row-With-Maximum-Ones/
    public int[] rowAndMaximumOnes_1(int[][] mat) {
        int[] ans = new int[2];
        for (int i = 0; i < mat.length; ++i) {
            int cnt = 0;
            for (int x : mat[i]) {
                if (x == 1) {
                    ++cnt;
                }
            }
            if (ans[1] < cnt) {
                ans[0] = i;
                ans[1] = cnt;
            }
        }
        return ans;
    }

    // V2
    // https://leetcode.com/problems/row-with-maximum-ones/solutions/5128881/java-easy-solution-for-beginners-100-1ms-xf3w/
    public int[] rowAndMaximumOnes_2(int[][] mat) {
        int res[] = { 0, 0 };
        int row = mat.length;
        int col = mat[0].length;
        for (int i = 0; i < row; i++) {
            int count = 0;
            for (int j = 0; j < col; j++) {
                count += mat[i][j];
            }
            if (count > res[1]) {
                res[0] = i;
                res[1] = count;
            }
        }
        return res;
    }


    // V3


}
