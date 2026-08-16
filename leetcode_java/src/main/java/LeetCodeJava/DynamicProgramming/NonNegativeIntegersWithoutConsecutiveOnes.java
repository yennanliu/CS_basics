package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/description/
/**
 * 600. Non-negative Integers without Consecutive Ones
 * Hard
 *
 * Given a positive integer n, return the number of the integers in the range [0, n]
 * whose binary representations do NOT contain consecutive ones.
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 5
 * Explanation:
 * Here are the non-negative integers <= 5 with their corresponding binary
 * representations:
 * 0 : 0
 * 1 : 1
 * 2 : 10
 * 3 : 11
 * 4 : 100
 * 5 : 101
 * Among them, only integer 3 disobeys the rule (two consecutive ones) and the other 5
 * satisfy the rule.
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 2
 *
 * Example 3:
 *
 * Input: n = 2
 * Output: 3
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class NonNegativeIntegersWithoutConsecutiveOnes {

    // V0
    // IDEA: FIBONACCI + BIT SCAN (greedy digit counting)
    /**
     *   f[i] = # of binary strings of length i with NO two adjacent 1s
     *        -> f[0] = 1 (empty), f[1] = 2 ("0", "1"), f[i] = f[i-1] + f[i-2]
     *      (i.e. the Fibonacci numbers)
     *
     *   Scan the bits of n from HIGH to LOW.
     *   Whenever bit i of n is 1, every number that puts a 0 at bit i (and keeps
     *   the higher bits IDENTICAL to n) is strictly SMALLER than n, and the lower
     *   i bits are then COMPLETELY FREE -> that adds f[i] valid numbers.
     *
     *   Then we `commit` to putting a 1 at bit i and move on.
     *
     *   NOTE !!! if the previously committed bit was ALSO 1, the prefix itself is
     *            already invalid, so no number >= that prefix can be counted
     *            -> STOP EARLY (and n itself is not counted).
     *
     *   If we never break, n itself is valid, so add 1 at the end.
     *
     *   time  = O(32) = O(1)
     *   space = O(32) = O(1)
     */
    public int findIntegers(int n) {
        // f[i] = count of valid binary strings of length i
        int[] f = new int[32];
        f[0] = 1;
        f[1] = 2;
        for (int i = 2; i < 32; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }

        int res = 0;
        int prevBit = 0;

        for (int i = 30; i >= 0; i--) {
            if (((n >> i) & 1) == 1) {
                // put 0 here -> the lower i bits are FREE
                res += f[i];
                if (prevBit == 1) {
                    // prefix "11..." already invalid -> n itself is NOT counted
                    return res;
                }
                prevBit = 1;
            } else {
                prevBit = 0;
            }
        }

        // n itself is valid
        return res + 1;
    }


    // V1
    // IDEA: DIGIT DP over the bits (memoised, tight flag + previous bit)
    /**
     *  The mechanical template: walk the bits high to low choosing 0 or 1, carrying
     *  whether the prefix still matches n and what the previous bit was.
     *
     *  Slower than the Fibonacci scan but immediately adaptable -- change the
     *  forbidden pattern and only the guard moves.
     *
     *  time  = O(32 * 2 * 2 * 2)
     *  space = O(32 * 2 * 2)
     */
    public int findIntegers_1(int n) {
        String bits = Integer.toBinaryString(n);
        Integer[][][] memo = new Integer[bits.length()][2][2];
        return countBits(bits, 0, 0, 1, memo);
    }

    private int countBits(String bits, int pos, int prev, int tight, Integer[][][] memo) {
        if (pos == bits.length()) {
            return 1;
        }
        if (memo[pos][prev][tight] != null) {
            return memo[pos][prev][tight];
        }
        int limit = tight == 1 ? bits.charAt(pos) - '0' : 1;
        int total = 0;
        for (int d = 0; d <= limit; d++) {
            if (d == 1 && prev == 1) {
                continue;                       // no two adjacent ones
            }
            total += countBits(bits, pos + 1, d, (tight == 1 && d == limit) ? 1 : 0, memo);
        }
        memo[pos][prev][tight] = total;
        return total;
    }

    // V2
    // IDEA: BRUTE FORCE -- test every integer in [0, n]
    /**
     *  `(v & (v >> 1)) == 0` is true exactly when v has no two adjacent ones.
     *
     *  O(n), useless at 10^9, but it is the definition and thus the oracle.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findIntegers_2(int n) {
        int res = 0;
        for (int v = 0; v <= n; v++) {
            if ((v & (v >> 1)) == 0) {
                res += 1;
            }
        }
        return res;
    }

    // V3
    // IDEA: ZECKENDORF VIEW -- the count IS a Fibonacci index
    /**
     *  Numbers with no adjacent ones are exactly the Zeckendorf representations, so
     *  scanning n's bits and accumulating Fibonacci terms COUNTS the rank of n in
     *  that enumeration.
     *
     *  Same arithmetic as V0 but framed as `compute n's Zeckendorf rank`, which is
     *  why the answer is a sum of Fibonacci numbers rather than a DP.
     *
     *  time  = O(32)
     *  space = O(32)
     */
    public int findIntegers_3(int n) {
        int[] fib = new int[32];
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }

        int res = 0;
        int prevBit = 0;
        for (int i = 30; i >= 0; i--) {
            if (((n >> i) & 1) == 0) {
                prevBit = 0;
                continue;
            }
            res += fib[i];                 // every valid number with 0 at bit i
            if (prevBit == 1) {
                return res;                // the prefix itself is already invalid
            }
            prevBit = 1;
        }
        return res + 1;
    }

}
