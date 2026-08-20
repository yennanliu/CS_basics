package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimize-xor/

/**
 *  2429. Minimize XOR
 *  Medium
 *
 *  Given two positive integers num1 and num2, find the positive integer x such that:
 *
 *    - x has the same number of set bits as num2, and
 *    - the value x XOR num1 is minimal.
 *
 *  Return the integer x. The test cases are generated such that x is uniquely
 *  determined.
 *
 *  Example 1:
 *    Input: num1 = 3, num2 = 5
 *    Output: 3
 *    Explanation: 3 (0011) has as many set bits as 5 (0101), and 3 XOR 3 = 0.
 *
 *  Example 2:
 *    Input: num1 = 1, num2 = 12
 *    Output: 3
 *    Explanation: 3 (0011) has as many set bits as 12 (1100), and 3 XOR 1 = 2.
 *
 *  Constraints:
 *    1 <= num1, num2 <= 10^9
 */
public class MinimizeXOR {

    // V0
    // IDEA: SPEND THE BIT BUDGET ON num1's HIGHEST SET BITS, THEN ON THE LOWEST GAPS
    //
    //  x must carry exactly popcount(num2) set bits. every bit shared with num1
    //  cancels in the XOR, so matching a set bit of num1 SAVES that bit's weight —
    //  and the heaviest ones are worth the most. hand out the budget to num1's set
    //  bits from the top down.
    //
    //  if bits remain after that, they must land where num1 is 0, and there the
    //  cheapest choice is the LOWEST such positions.
    //
    //  NOTE: num1, num2 <= 10^9 < 2^30, so bits 30..0 cover everything and no
    //        `1 << 31` overflow can occur.
    /**
     * time = O(31)
     * space = O(1)
     */
    public int minimizeXor(int num1, int num2) {
        int budget = Integer.bitCount(num2);
        int res = 0;

        for (int b = 30; b >= 0; b--) {          // keep num1's heaviest bits
            if (budget > 0 && ((num1 >> b) & 1) == 1) {
                res |= 1 << b;
                budget--;
            }
        }
        for (int b = 0; b <= 30 && budget > 0; b++) {   // then the cheapest free slots
            if (((res >> b) & 1) == 0) {
                res |= 1 << b;
                budget--;
            }
        }
        return res;
    }
}
