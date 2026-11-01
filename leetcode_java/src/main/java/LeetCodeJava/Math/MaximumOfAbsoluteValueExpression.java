package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-of-absolute-value-expression/description/

/**
 * 1131. Maximum of Absolute Value Expression
 * Medium
 *
 * Given two arrays of integers with equal lengths, return the maximum value of:
 *
 * |arr1[i] - arr1[j]| + |arr2[i] - arr2[j]| + |i - j|
 *
 * where the maximum is taken over all 0 <= i, j < arr1.length.
 *
 *
 * Example 1:
 *
 * Input: arr1 = [1,2,3,4], arr2 = [-1,4,5,6]
 * Output: 13
 *
 * Example 2:
 *
 * Input: arr1 = [1,-2,-5,0,10], arr2 = [0,-2,-1,-7,-4]
 * Output: 20
 *
 *
 * Constraints:
 *
 * 2 <= arr1.length == arr2.length <= 40000
 * -10^6 <= arr1[i], arr2[i] <= 10^6
 *
 */
public class MaximumOfAbsoluteValueExpression {

    // V0
    // IDEA: MATH -- REMOVE THE ABS BY ENUMERATING SIGNS
    //       WLOG i >= j, then |x_i - x_j| + |y_i - y_j| + (i - j)
    //       = max over a, b in {+1, -1} of (a*x_i + b*y_i + i) - (a*x_j + b*y_j + j)
    //       (a wrong sign choice only UNDER-estimates, so the max is still correct)
    //       -> for each of the 4 (a, b) combos, the candidate is max(f) - min(f),
    //          where f(i) = a*arr1[i] + b*arr2[i] + i
    //       brute force over all (i, j) pairs is O(n^2) and times out at n = 40000.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxAbsValExpr(int[] arr1, int[] arr2) {
        int n = arr1.length;
        int[] signs = { 1, -1 };
        int best = 0;

        for (int a : signs) {
            for (int b : signs) {
                // track the extremes of f(i) = a*arr1[i] + b*arr2[i] + i
                int maxF = Integer.MIN_VALUE;
                int minF = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    // |f| <= 2 * 10^6 + 4 * 10^4, safely inside int
                    int f = a * arr1[i] + b * arr2[i] + i;
                    maxF = Math.max(maxF, f);
                    minF = Math.min(minF, f);
                }
                best = Math.max(best, maxF - minF);
            }
        }
        return best;
    }
}
