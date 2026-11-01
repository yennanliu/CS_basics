package LeetCodeJava.Math;

// https://leetcode.com/problems/simplified-fractions/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1447. Simplified Fractions
 * Medium
 *
 * Given an integer n, return a list of all simplified fractions between 0 and 1 (exclusive) such that the denominator is less-than-or-equal-to n. You can return the answer in any order.
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: ["1/2"]
 * Explanation: "1/2" is the only unique fraction with a denominator less-than-or-equal-to 2.
 *
 * Example 2:
 *
 * Input: n = 3
 * Output: ["1/2","1/3","2/3"]
 *
 * Example 3:
 *
 * Input: n = 4
 * Output: ["1/2","1/3","1/4","2/3","3/4"]
 * Explanation: "2/4" is not a simplified fraction because it can be simplified to "1/2".
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 *
 */
public class SimplifiedFractions {

    // V0
    // IDEA: ENUMERATE PAIRS + GCD (a fraction is simplified iff gcd == 1)
    //   value strictly between 0 and 1 -> numerator < denominator.
    //   keep i/j only when gcd(i, j) == 1, otherwise the same value already appears in lowest terms.
    /**
     * time = O(N^2 * log N)
     * space = O(1) besides the output
     */
    public List<String> simplifiedFractions(int n) {
        List<String> result = new ArrayList<>();
        for (int numerator = 1; numerator < n; numerator++) {
            for (int denominator = numerator + 1; denominator <= n; denominator++) {
                if (gcd(numerator, denominator) == 1) {
                    result.add(numerator + "/" + denominator);
                }
            }
        }
        return result;
    }

    /** Euclid's algorithm. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
