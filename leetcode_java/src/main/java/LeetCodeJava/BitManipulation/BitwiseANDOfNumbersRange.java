package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/bitwise-and-of-numbers-range/

/**
 *  201. Bitwise AND of Numbers Range
 *  Medium
 *
 *  Given two integers left and right that represent the range [left, right],
 *  return the bitwise AND of all numbers in this range, inclusive.
 *
 *  Example 1:
 *   Input: left = 5, right = 7
 *   Output: 4
 *
 *  Example 2:
 *   Input: left = 0, right = 0
 *   Output: 0
 *
 *  Example 3:
 *   Input: left = 1, right = 2147483647
 *   Output: 0
 *
 *  Constraints:
 *   0 <= left <= right <= 2^31 - 1
 */
public class BitwiseANDOfNumbersRange {

    // V0
    // IDEA: the answer is the common binary prefix of left and right — any bit
    //       below the first difference flips somewhere inside the range, so
    //       shift both right until they match, then shift back.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int rangeBitwiseAnd(int left, int right) {
        int shift = 0;
        while (left != right) {
            left >>>= 1;
            right >>>= 1;
            shift++;
        }
        return left << shift;
    }

    // V1
    // IDEA: Brian Kernighan — repeatedly clear the lowest set bit of `right`
    //       until it drops to (or below) `left`.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int rangeBitwiseAnd_1(int left, int right) {
        while (left < right) {
            right &= (right - 1);
        }
        return right;
    }
}
