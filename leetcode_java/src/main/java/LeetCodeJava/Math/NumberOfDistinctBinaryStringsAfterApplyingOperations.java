package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-distinct-binary-strings-after-applying-operations/description/

/**
 * 2450. Number of Distinct Binary Strings After Applying Operations
 * Medium
 *
 * You are given a binary string s and a positive integer k.
 *
 * You can apply the following operation on the string any number of times:
 *
 * Choose any substring of size k from s and flip all its characters, that is, turn all 1's into 0's, and all 0's into 1's.
 *
 * Return the number of distinct strings you can obtain. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * Note that:
 *
 * A binary string is a string that consists only of the characters 0 and 1.
 * A substring is a contiguous part of a string.
 *
 *
 * Example 1:
 *
 * Input: s = "1001", k = 3
 * Output: 4
 * Explanation: We can obtain the following strings:
 * - Applying no operation on the string gives s = "1001".
 * - Applying one operation on the substring starting at index 0 gives s = "0111".
 * - Applying one operation on the substring starting at index 1 gives s = "1110".
 * - Applying one operation on both the substrings starting at indices 0 and 1 gives s = "0000".
 * It can be shown that we cannot obtain any other string, so the answer is 4.
 *
 * Example 2:
 *
 * Input: s = "10110", k = 5
 * Output: 2
 * Explanation: We can obtain the following strings:
 * - Applying no operation on the string gives s = "10110".
 * - Applying one operation on the whole string gives s = "01001".
 * It can be shown that we cannot obtain any other string, so the answer is 2.
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 10^5
 * s[i] is either 0 or 1.
 *
 */
public class NumberOfDistinctBinaryStringsAfterApplyingOperations {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: LINEAR ALGEBRA OVER GF(2) - THE n - k + 1 FLIP WINDOWS ARE INDEPENDENT
    //   flipping the window at i XORs a fixed vector; window i is the only one whose
    //   leftmost one sits at i, so the vectors are independent and every subset of
    //   windows gives a different string -> 2^(n - k + 1).
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int countDistinctStrings(String s, int k) {
        int windows = s.length() - k + 1;
        return (int) modPow(2, windows);
    }

    /** Fast exponentiation: base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            // the lowest bit of the exponent is set -> this power of base is part of the product
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
