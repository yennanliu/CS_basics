package LeetCodeJava.Math;

// https://leetcode.com/problems/nth-digit/

/**
 *  400. Nth Digit
 *  Medium
 *
 *  Given an integer n, return the nth digit of the infinite integer sequence
 *  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...].
 *
 *  Example 1:
 *
 *  Input: n = 3
 *  Output: 3
 *
 *  Example 2:
 *
 *  Input: n = 11
 *  Output: 0
 *  Explanation: the 11th digit of the sequence 1, 2, ..., 9, 10, 11, ... is 0,
 *  which is part of the number 10.
 *
 *  Constraints:
 *
 *  1 <= n <= 2^31 - 1
 */
public class NthDigit {

    // V0
    // IDEA: numbers with `len` digits occupy 9 * 10^(len-1) * len digits in total.
    //       Peel those blocks off to find the digit length, then the exact number,
    //       then the digit inside that number.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int findNthDigit(int n) {
        long remain = n;
        long len = 1;          // current digit length
        long count = 9;        // how many numbers have `len` digits
        long start = 1;        // first number with `len` digits

        while (remain > len * count) {
            remain -= len * count;
            len++;
            count *= 10;
            start *= 10;
        }

        long num = start + (remain - 1) / len;
        int idx = (int) ((remain - 1) % len);
        return String.valueOf(num).charAt(idx) - '0';
    }
}
