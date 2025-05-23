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
    // IDEA : array + index op
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/spiral-matrix.py
    // TODO : implement below
//    public List<Integer> spiralOrder(int[][] matrix) {
//
//    }

    // V0-1
    // IDEA : array + index op
    // time: O(M*N), space: O(1)
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
    // time: O(M*N), space: O(1)
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
    // time: O(M*N), space: O(1)
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
