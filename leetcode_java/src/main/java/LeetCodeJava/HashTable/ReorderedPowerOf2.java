package LeetCodeJava.HashTable;

// https://leetcode.com/problems/reordered-power-of-2/

import java.util.Arrays;

/**
 *  869. Reordered Power of 2
 *  Medium
 *
 *  You are given an integer n. We reorder the digits in any order (including the
 *  original order) such that the leading digit is not zero.
 *
 *  Return true if and only if we can do this so that the resulting number is a
 *  power of two.
 *
 *  Example 1:
 *  Input: n = 1
 *  Output: true
 *
 *  Example 2:
 *  Input: n = 10
 *  Output: false
 *
 *  Constraints:
 *  1 <= n <= 10^9
 */
public class ReorderedPowerOf2 {

    // V0
    // IDEA: DIGIT COUNT SIGNATURE - two numbers are permutations of each other
    //       iff their sorted digit strings are equal. There are only 31 powers of
    //       2 within the int range, so compare n against all of them.
    /**
     * time = O((log n)^2) -> O(1), n is a 32-bit int
     * space = O(log n) -> O(1)
     */
    public boolean reorderedPowerOf2(int n) {

        String target = signature(n);

        for (int i = 0; i < 31; i++) {
            if (target.equals(signature(1 << i))) {
                return true;
            }
        }

        return false;
    }

    /** sorted digit string, e.g. 128 -> "128", 821 -> "128" */
    private String signature(int x) {
        char[] c = String.valueOf(x).toCharArray();
        Arrays.sort(c);
        return new String(c);
    }
}
