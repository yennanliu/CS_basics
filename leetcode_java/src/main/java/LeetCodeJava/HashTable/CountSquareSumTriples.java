package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-square-sum-triples/description/

import java.util.*;

/**
 * 1925. Count Square Sum Triples
 * Easy
 *
 * A square triple (a,b,c) is a triple where a, b, and c are integers and a^2 + b^2 = c^2.
 *
 * Given an integer n, return the number of square triples such that 1 <= a, b, c <= n.
 *
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 2
 * Explanation: The square triples are (3,4,5) and (4,3,5).
 *
 * Example 2:
 *
 * Input: n = 10
 * Output: 4
 * Explanation: The square triples are (3,4,5), (4,3,5), (6,8,10), and (8,6,10).
 *
 *
 * Constraints:
 *
 * 1 <= n <= 250
 *
 */
public class CountSquareSumTriples {

    // V0
    // IDEA: HASH SET OF SQUARES + ENUMERATE ORDERED (a, b)
    //       the set lookup replaces a floating-point sqrt check, so there is no rounding risk
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int countTriples(int n) {
        Set<Integer> squares = new HashSet<>();
        for (int c = 1; c <= n; c++) {
            squares.add(c * c);
        }

        int triples = 0;
        // a and b are both < c <= n; ordered pairs, so (3,4,5) and (4,3,5) both count
        for (int a = 1; a < n; a++) {
            for (int b = 1; b < n; b++) {
                if (squares.contains(a * a + b * b)) {
                    triples++;
                }
            }
        }
        return triples;
    }
}
