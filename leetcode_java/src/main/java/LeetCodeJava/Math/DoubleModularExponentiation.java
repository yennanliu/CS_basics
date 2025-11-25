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

    // VO-1
    // TODO: fix below
    // NOTE !!! below is WRONG
    // since we need to apply the `modulo operation` in the algorithm
    /**
     *  Your current code is incorrect because:
     *
     * ❌ Issues
     * 	1.	Math.pow(ai, bi) overflows immediately for large bi.
     * 	2.	The problem requires modular exponentiation, not raw pow().
     * 	3.	The expression must be computed as:
     *
     * 	  `((ai^bi mod 10)^ci) mod mi`
     *
     * 	4. You must use fast power (modular exponentiation).
     *
     */
//    public List<Integer> getGoodIndices_0_1(int[][] variables, int target) {
//        List<Integer> ans = new ArrayList<>();
//        // edge
//        if(variables == null || variables.length == 0){
//            return ans;
//        }
//
//        for(int i = 0; i < variables.length; i++){
//            int ai = variables[i][0];
//            int bi = variables[i][1];
//            int ci = variables[i][2];
//            int mi = variables[i][3];
//            if(compute(ai, bi, ci, mi) == target){
//                ans.add(i);
//            }
//        }
//
//        return ans;
//    }
//
//    //  ( (ai^bi % 10)^ci ) % mi = target
//    private int compute(int ai, int bi, int ci, int mi){
//        // edge
//        if(mi == 0){
//            return -1; // ???
//        }
//
//        //  ( (ai^bi % 10)^ci ) % mi = target
//
//        // double resultDouble = Math.pow(base, exponent);
//        // ???
//        int x1 = (int) Math.pow(ai, bi);
//        int x2 = x1 % 10;
//        int x3 =  (int) Math.pow(x2, ci);
//
//        return x3 % mi;
//    }


    // V0-2
    // IDEA: MATH (fixed by gpt)
    public List<Integer> getGoodIndices_0_2(int[][] variables, int target) {
        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i < variables.length; i++) {
            int a = variables[i][0];
            int b = variables[i][1];
            int c = variables[i][2];
            int m = variables[i][3];

            if (compute_0_2(a, b, c, m) == target) {
                ans.add(i);
            }
        }

        return ans;
    }

    // Computes: ((a^b % 10)^c) % m
    private int compute_0_2(int a, int b, int c, int m) {
        if (m == 0)
            return -1;

        // Step 1: x1 = a^b % 10
        int x1 = modPow(a, b, 10);

        // Step 2: x2 = (x1^c) % m
        return modPow(x1, c, m);
    }

    // Fast modular exponentiation
    private int modPow(int base, int exp, int mod) {
        long result = 1;
        long cur = base % mod;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * cur) % mod;
            }
            cur = (cur * cur) % mod;
            exp >>= 1;
        }

        return (int) result;
    }


    // V0-3
    // IDEA: MATH (fixed by gemini)
    public List<Integer> getGoodIndices_0_3(int[][] variables, int target) {
        List<Integer> ans = new ArrayList<>();

        // Edge case check is fine, though the loop handles it robustly.
        if (variables == null || variables.length == 0) {
            return ans;
        }

        for (int i = 0; i < variables.length; i++) {
            int ai = variables[i][0];
            int bi = variables[i][1];
            int ci = variables[i][2];
            int mi = variables[i][3];

            if (compute(ai, bi, ci, mi) == target) {
                ans.add(i);
            }
        }

        return ans;
    }

    /**
     * Calculates: ( (ai^bi % 10)^ci ) % mi
     * Requires modular exponentiation to avoid overflow.
     */
    private int compute(int ai, int bi, int ci, int mi) {
        // Step 1: Calculate (ai^bi) % 10
        // We use modularExponentiation(base, exponent, modulus)
        int innerResult = modularExponentiation(ai, bi, 10); // innerResult = (ai^bi) % 10

        // Step 2: Calculate (innerResult^ci) % mi
        // innerResult is now the base, ci is the exponent, and mi is the modulus.
        int finalResult = modularExponentiation(innerResult, ci, mi); // finalResult = (innerResult^ci) % mi

        return finalResult;
    }

    /**
     * Helper method to calculate (base^exponent) % modulus efficiently and safely.
     * Uses the principle of exponentiation by squaring (or binary exponentiation).
     * * @param base The base (a, or innerResult)
     * @param exponent The exponent (b, or c)
     * @param modulus The modulus (10, or m)
     * @return (base^exponent) % modulus
     */
    private int modularExponentiation(int base, int exponent, int modulus) {
        if (modulus == 1)
            return 0; // Trivial case (any number mod 1 is 0)

        long result = 1;
        // Convert base to long to prevent overflow during multiplication
        long b = base % modulus;
        int e = exponent;

        while (e > 0) {
            // If the current exponent bit is 1 (e is odd), multiply the result by the current base.
            if (e % 2 == 1) {
                result = (result * b) % modulus;
            }

            // Square the base for the next iteration, applying modulus.
            b = (b * b) % modulus;

            // Move to the next bit (divide exponent by 2).
            e /= 2;
        }

        return (int) result;
    }

    // V0-3



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
