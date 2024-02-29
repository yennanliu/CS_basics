package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix/description/

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

    // V0'
    // IDEA : array + index op
    public List<Integer> spiralOrder_(int[][] matrix) {

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
