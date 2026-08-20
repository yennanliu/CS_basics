package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-number-that-makes-result-of-bitwise-and-zero/

/**
 *  3125. Maximum Number That Makes Result of Bitwise AND Zero
 *  Medium
 *  (premium)
 *
 *  Given an integer n, return the maximum integer x such that x <= n, and the
 *  bitwise AND of all the numbers in the range [x, n] is equal to 0.
 *
 *  Example 1:
 *    Input: n = 7
 *    Output: 3
 *    Explanation: The bitwise AND of [3, 4, 5, 6, 7] is 0.
 *
 *  Example 2:
 *    Input: n = 9
 *    Output: 7
 *    Explanation: The bitwise AND of [7, 8, 9] is 0.
 *
 *  Example 3:
 *    Input: n = 17
 *    Output: 15
 *    Explanation: The bitwise AND of [15, 16, 17] is 0.
 *
 *  Constraints:
 *    1 <= n <= 10^9
 */
public class MaximumNumberThatMakesResultOfBitwiseAndZero {

    // V0
    // IDEA: THE ANSWER IS ALWAYS 2^h - 1, WHERE h IS n's HIGHEST SET BIT
    //
    //  n has bit h set, so every number in [2^h, n] also has it -> an AND over a
    //  range that never dips below 2^h keeps bit h and cannot be 0. hence x <= 2^h - 1.
    //
    //  and x = 2^h - 1 already works: that number has bit h clear with all lower
    //  bits set, while 2^h (which is <= n, so it lies in the range) is exactly the
    //  opposite -> their AND alone is already 0.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int maxNumber(int n) {
        int h = 31 - Integer.numberOfLeadingZeros(n);   // n >= 1, so h >= 0
        return (1 << h) - 1;
    }
}
