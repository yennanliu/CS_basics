package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimum-bit-flips-to-convert-number/

/**
 *  2220. Minimum Bit Flips to Convert Number
 *  Easy
 *
 *  A bit flip of a number x is choosing a bit in the binary representation of x and
 *  flipping it from either 0 to 1 or 1 to 0.
 *
 *  For example, for x = 7, the binary representation is 111 and we may choose any
 *  bit (including any leading zeros not shown) and flip it: flipping the first bit
 *  from the right gives 110, the second gives 101, the fifth (a leading zero) gives
 *  10111, etc.
 *
 *  Given two integers start and goal, return the minimum number of bit flips to
 *  convert start to goal.
 *
 *  Example 1:
 *    Input: start = 10, goal = 7
 *    Output: 3
 *    Explanation: 1010 -> 1011 -> 1111 -> 0111.
 *
 *  Example 2:
 *    Input: start = 3, goal = 4
 *    Output: 3
 *    Explanation: 011 -> 010 -> 000 -> 100.
 *
 *  Constraints:
 *    0 <= start, goal <= 10^9
 *
 *  Note: This question is the same as 461: Hamming Distance.
 */
public class MinimumBitFlipsToConvertNumber {

    // V0
    // IDEA: XOR + POPCOUNT (Hamming distance)
    //
    //  a bit needs flipping exactly when start and goal differ there, and
    //  (start ^ goal) has a 1 in precisely those positions -> the answer is
    //  popcount(start ^ goal).
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minBitFlips(int start, int goal) {
        return Integer.bitCount(start ^ goal);
    }
}
