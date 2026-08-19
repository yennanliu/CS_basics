package LeetCodeJava.Math;

// https://leetcode.com/problems/gray-code/

import java.util.ArrayList;
import java.util.List;

/**
 *  89. Gray Code
 *  Medium
 *
 *  An n-bit gray code sequence is a sequence of 2^n integers where:
 *    - Every integer is in the inclusive range [0, 2^n - 1],
 *    - The first integer is 0,
 *    - An integer appears no more than once in the sequence,
 *    - The binary representation of every pair of adjacent integers differs
 *      by exactly one bit, and
 *    - The binary representation of the first and last integers differs by
 *      exactly one bit.
 *
 *  Given an integer n, return any valid n-bit gray code sequence.
 *
 *  Example 1:
 *    Input: n = 2
 *    Output: [0,1,3,2]
 *
 *  Example 2:
 *    Input: n = 1
 *    Output: [0,1]
 *
 *  Constraints:
 *    1 <= n <= 16
 */
public class GrayCode {

    // V0
    // IDEA: standard formula - the i-th gray code is i ^ (i >> 1)
    /**
     * time = O(2^n)
     * space = O(1)   (excluding the output)
     */
    public List<Integer> grayCode(int n) {

        List<Integer> res = new ArrayList<>();
        int total = 1 << n;
        for (int i = 0; i < total; i++) {
            res.add(i ^ (i >> 1));
        }
        return res;
    }

    // V1
    // IDEA: mirror construction - reflect the current list and prefix a 1 bit
    /**
     * time = O(2^n)
     * space = O(1)   (excluding the output)
     */
    public List<Integer> grayCode_1(int n) {

        List<Integer> res = new ArrayList<>();
        res.add(0);

        for (int i = 0; i < n; i++) {
            int highBit = 1 << i;
            // walk backwards over the current list, prefix the new high bit
            for (int j = res.size() - 1; j >= 0; j--) {
                res.add(res.get(j) | highBit);
            }
        }

        return res;
    }
}
