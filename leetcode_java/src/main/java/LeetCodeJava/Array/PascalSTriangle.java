package LeetCodeJava.Array;

// https://leetcode.com/problems/pascals-triangle/

import java.util.ArrayList;
import java.util.List;

/**
 *  118. Pascal's Triangle
 *  Easy
 *
 *  Given an integer numRows, return the first numRows of Pascal's triangle.
 *
 *  In Pascal's triangle, each number is the sum of the two numbers
 *  directly above it.
 *
 *  Example 1:
 *   Input: numRows = 5
 *   Output: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
 *
 *  Example 2:
 *   Input: numRows = 1
 *   Output: [[1]]
 *
 *  Constraints:
 *   1 <= numRows <= 30
 */
public class PascalSTriangle {

    // V0
    // IDEA: BUILD ROW BY ROW, row[j] = prev[j-1] + prev[j]
    /**
     * time = O(numRows^2)
     * space = O(1) (excluding output)
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        if (numRows <= 0) {
            return res;
        }

        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    List<Integer> prev = res.get(i - 1);
                    row.add(prev.get(j - 1) + prev.get(j));
                }
            }
            res.add(row);
        }

        return res;
    }
}
