package LeetCodeJava.Math;

// https://leetcode.com/problems/double-modular-exponentiation/description/

import java.util.ArrayList;
import java.util.List;

/**
 *  2961. Double Modular Exponentiation
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given a 0-indexed 2D array variables where variables[i] = [ai, bi, ci, mi], and an integer target.
 *
 * An index i is good if the following formula holds:
 *
 * 0 <= i < variables.length
 * ((aibi % 10)ci) % mi == target
 * Return an array consisting of good indices in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: variables = [[2,3,3,10],[3,3,3,1],[6,1,1,4]], target = 2
 * Output: [0,2]
 * Explanation: For each index i in the variables array:
 * 1) For the index 0, variables[0] = [2,3,3,10], (23 % 10)3 % 10 = 2.
 * 2) For the index 1, variables[1] = [3,3,3,1], (33 % 10)3 % 1 = 0.
 * 3) For the index 2, variables[2] = [6,1,1,4], (61 % 10)1 % 4 = 2.
 * Therefore we return [0,2] as the answer.
 * Example 2:
 *
 * Input: variables = [[39,3,1000,1000]], target = 17
 * Output: []
 * Explanation: For each index i in the variables array:
 * 1) For the index 0, variables[0] = [39,3,1000,1000], (393 % 10)1000 % 1000 = 1.
 * Therefore we return [] as the answer.
 *
 *
 * Constraints:
 *
 * 1 <= variables.length <= 100
 * variables[i] == [ai, bi, ci, mi]
 * 1 <= ai, bi, ci, mi <= 103
 * 0 <= target <= 103
 *
 */
public class DoubleModularExponentiation {

    // V0
//    public List<Integer> getGoodIndices(int[][] variables, int target) {
//
//    }


    // V1
    // https://leetcode.com/problems/double-modular-exponentiation/solutions/4384819/mastering-modular-exponentiation-beginne-rsfw/
    public long customPow(int base, int exponent, int mod) {
        long result = 1;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent /= 2;
        }
        return result;
    }

    public List<Integer> getGoodIndices_1(int[][] variables, int target) {
        List<Integer> ans = new ArrayList<>();
        int i = 0;
        for (int[] row : variables) {
            long ele1 = customPow(row[0], row[1], 10);
            long ele2 = customPow((int) (ele1 % 10), row[2], row[3]);

            if (ele2 == target) {
                ans.add(i);
            }

            i++;
        }
        return ans;
    }

    // V2
    public List<Integer> getGoodIndices_2(int[][] variables, int target) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < variables.length; ++i) {
            int ai = variables[i][0];
            int bi = variables[i][1];
            int ci = variables[i][2];
            int mi = variables[i][3];
            int base = 1;
            for (int j = 0; j < bi; ++j) {
                base = (base * ai) % 10;
            }
            int formulaResult = 1;
            for (int j = 0; j < ci; ++j) {
                formulaResult = (formulaResult * base) % mi;
            }
            if (formulaResult == target) {
                result.add(i);
            }
        }
        return result;
    }


    // V3


}
