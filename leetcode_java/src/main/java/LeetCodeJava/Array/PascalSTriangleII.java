package LeetCodeJava.Array;

// https://leetcode.com/problems/pascals-triangle-ii/

import java.util.ArrayList;
import java.util.List;

/**
 *  119. Pascal's Triangle II
 *  Easy
 *
 *  Given an integer rowIndex, return the rowIndex-th (0-indexed) row of
 *  Pascal's triangle.
 *
 *  In Pascal's triangle, each number is the sum of the two numbers
 *  directly above it.
 *
 *  Follow up: could you optimize your algorithm to use only O(rowIndex)
 *  extra space?
 *
 *  Example 1:
 *   Input: rowIndex = 3
 *   Output: [1,3,3,1]
 *
 *  Example 2:
 *   Input: rowIndex = 0
 *   Output: [1]
 *
 *  Constraints:
 *   0 <= rowIndex <= 33
 */
public class PascalSTriangleII {

    // V0
    // IDEA: IN-PLACE UPDATE OF A SINGLE ROW, FROM RIGHT TO LEFT
    /**
     * time = O(rowIndex^2)
     * space = O(rowIndex)
     */
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>();
        row.add(1);

        for (int i = 1; i <= rowIndex; i++) {
            // NOTE !!! go from right to left, so `row.get(j-1)` is still the previous row's value
            row.add(1);
            for (int j = i - 1; j >= 1; j--) {
                row.set(j, row.get(j - 1) + row.get(j));
            }
        }

        return row;
    }

    // V1
    // IDEA: MATH (BINOMIAL COEFFICIENT) - C(n, k) = C(n, k-1) * (n-k+1) / k
    /**
     * time = O(rowIndex)
     * space = O(1) (excluding output)
     */
    public List<Integer> getRow_1(int rowIndex) {
        List<Integer> res = new ArrayList<>();
        long cur = 1;
        for (int k = 0; k <= rowIndex; k++) {
            res.add((int) cur);
            cur = cur * (rowIndex - k) / (k + 1);
        }
        return res;
    }
}
