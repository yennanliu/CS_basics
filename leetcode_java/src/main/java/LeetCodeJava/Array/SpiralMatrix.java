package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix/description/
/**
 * 54. Spiral Matrix
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an m x n matrix, return all elements of the matrix in spiral order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [1,2,3,6,9,8,7,4,5]
 * Example 2:
 *
 *
 * Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 10
 * -100 <= matrix[i][j] <= 100
 *
 */

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {

    // V0
    // IDEA : array + index op (4 boundaries: top, bottom, left, right)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/spiral-matrix.py
    /**
     *  KEY IDEA:
     *
     *   maintain 4 `boundaries`, and walk
     *
     *     1) left -> right  (on `top` row),    then top++
     *     2) top  -> bottom (on `right` col),  then right--
     *     3) right -> left  (on `bottom` row), then bottom--
     *     4) bottom -> top  (on `left` col),   then left++
     *
     *   NOTE !!! before step 3) and 4), we MUST re-check
     *            (top <= bottom) / (left <= right),
     *            otherwise a single remaining row/col is visited TWICE
     *
     *
     *  time = O(M * N)
     *  space = O(1) (exclude the output)
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();

        // edge
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return res;
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {

            // 1) go `right` on top row
            for (int j = left; j <= right; j++) {
                res.add(matrix[top][j]);
            }
            top++;

            // 2) go `down` on right col
            for (int i = top; i <= bottom; i++) {
                res.add(matrix[i][right]);
            }
            right--;

            // 3) go `left` on bottom row
            /** NOTE !!! re-check boundary, to avoid double counting */
            if (top <= bottom) {
                for (int j = right; j >= left; j--) {
                    res.add(matrix[bottom][j]);
                }
                bottom--;
            }

            // 4) go `up` on left col
            /** NOTE !!! re-check boundary, to avoid double counting */
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    res.add(matrix[i][left]);
                }
                left++;
            }
        }

        return res;
    }

    // V0-1
    // IDEA : array + index op
    /**
     * time = O(M*N)
     * space = O(1)
     */
    public List<Integer> spiralOrder_0_1(int[][] matrix) {

        int row = matrix.length;
        int col = matrix[0].length;

        List<Integer> ans = new ArrayList<>();

        if(row < 1){
            return ans;
        }

        int startR = 0;
        int startCol = 0;
        int i = 0;

        while(startR <row && startCol<col){
            for(i= startCol; i< col; ++i){
                ans.add(matrix[startR][i]);
            }
            startR++;
            for(i = startR; i<row; ++i){
                ans.add(matrix[i][col-1]);
            }
            col--;
            if(startR < row){
                for(i = col-1; i>= startCol;--i){
                    ans.add(matrix[row-1][i]);
                }
                row--;
            }
            if(startCol < col){
                for(i = row-1; i>= startR;--i){
                    ans.add(matrix[i][startCol]);
                }
                startCol++;
            }
        }
        return ans;
    }

    // V1
    // https://leetcode.com/problems/spiral-matrix/solutions/4700215/easy-solution/
    /**
     * time = O(M*N)
     * space = O(1)
     */
    public List<Integer> spiralOrder_1(int[][] m) {
        int l=0,r=m[0].length-1,u=0,d=m.length-1;
        List<Integer> ll=new ArrayList<>();
        while(true){
            if(l<=r){
                for(int i=l;i<=r;++i){
                    ll.add(m[u][i]);
                }
                u++;
            }else break;
            if(u<=d){
                for(int i=u;i<=d;++i){
                    ll.add(m[i][r]);
                }
                r--;
            }else break;
            if(l<=r){
                for(int i=r;i>=l;--i){
                    ll.add(m[d][i]);
                }
                d--;
            }else break;
            if(u<=d){
                for(int i=d;i>=u;--i){
                    ll.add(m[i][l]);
                }
                l++;
            }else break;
        }
        return ll;
    }

    // V2
    // https://leetcode.com/problems/spiral-matrix/solutions/3503095/java-runtime-0-ms-beats-100-memory-40-8-mb-beats-46-17/
    /**
     * time = O(M*N)
     * space = O(1)
     */
    public List<Integer> spiralOrder_2(int[][] matrix) {
        int row = matrix.length;
        List<Integer> ans = new ArrayList<>();
        if(row<1){
            return ans;
        }
        int col = matrix[0].length;
        int startR = 0;
        int startCol = 0;
        int i =0;

        while(startR<row&& startCol<col){
            for(i= startCol; i< col; ++i){
                ans.add(matrix[startR][i]);
            }
            startR++;
            for(i = startR; i<row;++i){
                ans.add(matrix[i][col-1]);
            }
            col--;
            if(startR<row){
                for(i = col-1; i>=startCol;--i){
                    ans.add(matrix[row-1][i]);
                }
                row--;
            }
            if(startCol<col){
                for(i = row-1; i>=startR;--i){
                    ans.add(matrix[i][startCol]);
                }
                startCol++;
            }
        }
        return ans;
    }
    
}
