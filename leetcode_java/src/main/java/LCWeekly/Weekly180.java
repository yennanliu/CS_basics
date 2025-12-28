package LCWeekly;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode biweekly contest 180
 * https://leetcode.com/contest/weekly-contest-180/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-180/
 *
 */
public class Weekly180 {

    // LC 1380
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/
    // 15.49 - 59 pm
    /**
     *
     *  -> return all lucky numbers in the matrix in any order.
     *
     *    - Given an `m x n` matrix of `distinct` numbers,
     *
     *
     *   - A lucky number is an element of the
     *     matrix such that it's
     *       - min of row
     *       - max of col
     *
     *      -> its val is BOTH max val in `row and col`
     *
     *
     *  ---------------
     *
     *   IDEA 1) BRUTE FORCE + ARRAY OP
     *
     *
     *  ---------------
     *
     *   ex1)
     *
     *    Input: matrix = [[3,7,8],[9,11,13],[15,16,17]]
     *    Output: [15]
     *
     *    ->
     *
     *     [
     *     [3,7,8],
     *     [9,11,13],
     *     [15,16,17]
     *    ]
     *
     *
     *  ex 2)
     *
     *  Input: matrix = [[1,10,4,2],[9,3,8,7],[15,16,17,12]]
     *   Output: [12]
     *
     *   ->
     *
     *   [
     *    [1,10,4,2],
     *    [9,3,8,7],
     *    [15,16,17,12]
     *   ]
     *
     *
     *   ex 3)
     *
     *   Input: matrix = [[7,8],[1,2]]
     *   Output: [7]
     *
     *
     *   ->
     *
     *   [
     *   [7,8],
     *   [1,2]
     *   ]
     *
     *
     *
     */
    public List<Integer> luckyNumbers(int[][] matrix) {
        // edge

        int l = matrix.length;
        int w = matrix[0].length;

        List<Integer> res = new ArrayList<>();
        // get `max of each col
        List<Integer> colMax = new ArrayList<>();

        // ??
        for(int x = 0; x < w; x++){
            int colMaxVal = -1; // ??
            for(int y = 0; y < l; y++){
                colMaxVal = Math.max(colMaxVal, matrix[y][x]);
            }
            colMax.add(colMaxVal);
        }

        for(int y = 0; y < l; y++){
            int tmpVal = 0;
            int[] tmp = matrix[y];
            // ???
            for(int x = 0; x < w; x++){
                tmpVal = Math.min(tmpVal, tmp[x]);
            }
            if(tmpVal == colMax.get(y)){
                res.add(tmpVal);
                return res;
            }

        }


        return res;
    }


    // LC 1381
    // https://leetcode.com/problems/design-a-stack-with-increment-operation/


    // LC 1382
    // https://leetcode.com/problems/balance-a-binary-search-tree/


    // LC 1383
    // https://leetcode.com/problems/maximum-performance-of-a-team/


}
