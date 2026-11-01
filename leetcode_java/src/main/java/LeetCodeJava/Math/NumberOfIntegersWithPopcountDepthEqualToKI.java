package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-integers-with-popcount-depth-equal-to-k-i/description/

/**
 * 3621. Number of Integers With Popcount-Depth Equal to K I
 * Hard
 *
 * You are given two integers n and k.
 *
 * For any positive integer x, define the following sequence:
 *
 * p0 = x
 * p(i+1) = popcount(pi) for all i >= 0, where popcount(y) is the number of set
 * bits (1's) in the binary representation of y.
 *
 * This sequence will eventually reach the value 1.
 *
 * The popcount-depth of x is defined as the smallest integer d >= 0 such that
 * pd = 1.
 *
 * For example, if x = 7 (binary representation "111"). Then, the sequence is:
 * 7 -> 3 -> 2 -> 1, so the popcount-depth of 7 is 3.
 *
 * Your task is to determine the number of integers in the range [1, n] whose
 * popcount-depth is exactly equal to k.
 *
 * Return the number of such integers.
 *
 *
 * Example 1:
 *
 * Input: n = 4, k = 1
 * Output: 2
 * Explanation:
 * The following integers in the range [1, 4] have popcount-depth exactly equal
 * to 1:
 *
 * x    Binary    Sequence
 * 2    "10"      2 -> 1
 * 4    "100"     4 -> 1
 *
 * Thus, the answer is 2.
 *
 * Example 2:
 *
 * Input: n = 7, k = 2
 * Output: 3
 * Explanation:
 * The following integers in the range [1, 7] have popcount-depth exactly equal
 * to 2:
 *
 * x    Binary    Sequence
 * 3    "11"      3 -> 2 -> 1
 * 5    "101"     5 -> 2 -> 1
 * 6    "110"     6 -> 2 -> 1
 *
 * Thus, the answer is 3.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^15
 * 0 <= k <= 5
 *
 */
public class NumberOfIntegersWithPopcountDepthEqualToKI {

    // V0
    // IDEA: COLLAPSE THE DEPTH ONTO THE POPCOUNT, THEN COUNT NUMBERS BY POPCOUNT (COMBINATORICS)
    //       for x > 1, depth(x) = 1 + depth(popcount(x)), and popcount(x) <= 50 for n <= 10^15,
    //       so the depth of every popcount value c is a tiny precomputed table.
    //       answer = sum over c with depth(c) == k - 1 of #{x in [1, n] with popcount(x) == c}.
    //       x = 1 is the exception (popcount 1 but depth 0), so subtract it when k == 1.
    //       counting x <= n with c set bits: walk n's bits from the top; at each set bit i,
    //       putting a 0 there makes x < n with the low i bits free -> C(i, bits still needed).
    //       after all set bits, n itself is checked separately.
    /**
     * time = O((log N)^2)
     * space = O((log N)^2)
     */
    public long popcountDepth(long n, int k) {
        if (k == 0) {
            // only x = 1 has depth 0
            return 1;
        }

        int bitLength = 64 - Long.numberOfLeadingZeros(n);

        // pascal's triangle up to bitLength (C(50, 25) ~ 1.3e14 fits in a long)
        long[][] choose = new long[bitLength + 1][bitLength + 1];
        for (int i = 0; i <= bitLength; i++) {
            choose[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                choose[i][j] = choose[i - 1][j - 1] + choose[i - 1][j];
            }
        }

        // depth of every value a popcount can produce (depth[1] = 0)
        int[] depth = new int[bitLength + 1];
        for (int value = 2; value <= bitLength; value++) {
            depth[value] = depth[Integer.bitCount(value)] + 1;
        }

        long answer = 0;
        for (int bits = 1; bits <= bitLength; bits++) {
            if (depth[bits] == k - 1) {
                answer += countWithPopcount(n, bits, bitLength, choose);
            }
        }
        if (k == 1) {
            // x = 1 has popcount 1 (depth[1] = 0 = k - 1) but its own depth is 0, not 1
            answer--;
        }
        return answer;
    }

    /** How many x in [1, n] have exactly targetBits set bits. */
    private long countWithPopcount(long n, int targetBits, int bitLength, long[][] choose) {
        long count = 0;
        int usedBits = 0; // set bits of n above the current position
        for (int i = bitLength - 1; i >= 0; i--) {
            boolean bitIsSet = ((n >> i) & 1L) == 1L;
            if (!bitIsSet) {
                continue;
            }
            // put 0 here instead of n's 1: the low i bits are free, need the rest of the bits there
            int stillNeeded = targetBits - usedBits;
            if (stillNeeded >= 0 && stillNeeded <= i) {
                count += choose[i][stillNeeded];
            }
            usedBits++;
        }
        // n itself
        if (usedBits == targetBits) {
            count++;
        }
        return count;
    }
}
