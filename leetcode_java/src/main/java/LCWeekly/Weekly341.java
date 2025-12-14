package LCWeekly;

import java.util.HashMap;

/**
 * LeetCode weekly contest 341
 * https://leetcode.com/contest/weekly-contest-341/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-341/
 *
 */
public class Weekly341 {

    // Q1
    // LC 2643
    // https://leetcode.com/problems/row-with-maximum-ones/description/
    // 14.44 - 54 pm
    /**
     *  -> Return an array containing the
     *     index of the row, and the number of ones in it.
     *
     *   - m x n matrix
     *   - find the 0 indexed position
     *     contains the maximum count of ones,
     *     and the number of ones in that row.
     *
     *     -> [index, max_cnt_of_one]
     *
     *
     * ------------------------
     *
     *  IDEA 1) HASHMAP + ARRAY OP
     *
     *
     * ------------------------
     *
     *
     */
    public int[] rowAndMaximumOnes(int[][] mat) {
        // edge
        if(mat == null || mat.length == 0 || mat[0].length == 0){
            return new int[]{}; // ???
        }
      //  HashMap<Integer, Integer> map = new HashMap<>();
        //int max
        int maxCnt = 0;
        int[] res = new int[2];

        // ???
        for(int i = mat.length - 1; i > 0; i--){
            int cnt = 0;
            for(int x: mat[i]){
                if(x == 1){
                    cnt += 1;
                }
            }
            if(cnt > maxCnt){
                res = new int[]{i, cnt};
            }
//            maxCnt = Math.max(maxCnt, cnt);
//            map.put(i, cnt);
        }

        //int[] res = new int[2];
        //for(int k: ma)

        return res;
    }

    // Q2
    // https://leetcode.com/problems/find-the-maximum-divisibility-score/
    // LC 2644

    // Q3
    // LC 2645
    // https://leetcode.com/problems/minimum-additions-to-make-valid-string/

    // Q4
    // LC 2646
    // https://leetcode.com/problems/minimize-the-total-price-of-the-trips/



}
