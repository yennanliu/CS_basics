package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-good-binary-strings/description/

/**
 * 2533. Number of Good Binary Strings
 * Medium
 *
 * You are given four integers minLength, maxLength, oneGroup and zeroGroup.
 *
 * A binary string is good if it satisfies the following conditions:
 *
 * The length of the string is in the range [minLength, maxLength].
 * The size of each block of consecutive 1's is a multiple of oneGroup.
 *     For example in a binary string 00110111100 sizes of each block of consecutive ones are [2,4].
 * The size of each block of consecutive 0's is a multiple of zeroGroup.
 *     For example, in a binary string 00110111100 sizes of each block of consecutive zeros are [2,1,2].
 *
 * Return the number of good binary strings. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * Note that 0 is considered a multiple of all the numbers.
 *
 *
 * Example 1:
 *
 * Input: minLength = 2, maxLength = 3, oneGroup = 1, zeroGroup = 2
 * Output: 5
 * Explanation: There are 5 good binary strings in this example: "00", "11", "001", "100", and "111".
 *
 * Example 2:
 *
 * Input: minLength = 4, maxLength = 4, oneGroup = 4, zeroGroup = 3
 * Output: 1
 * Explanation: There is only 1 good binary string in this example: "1111".
 *
 *
 * Constraints:
 *
 * 1 <= minLength <= maxLength <= 10^5
 * 1 <= oneGroup, zeroGroup <= maxLength
 *
 */
public class NumberOfGoodBinaryStrings {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 1-D DP BY LENGTH - APPEND ONE CHUNK OF EXACTLY oneGroup ONES OR zeroGroup ZEROS: f[i] = f[i - oneGroup] + f[i - zeroGroup]
    /**
     * time = O(maxLength)
     * space = O(maxLength)
     */
    public int goodBinaryStrings(int minLength, int maxLength, int oneGroup, int zeroGroup) {
        // f[i] = number of good strings of length i; adjacent same-symbol chunks merge into one
        // valid block, and fixed-size chunks decompose a string in exactly one way
        long[] f = new long[maxLength + 1];
        f[0] = 1;   // the empty string

        for (int i = 1; i <= maxLength; i++) {
            if (i >= oneGroup) {
                f[i] += f[i - oneGroup];
            }
            if (i >= zeroGroup) {
                f[i] += f[i - zeroGroup];
            }
            f[i] %= MOD;
        }

        long total = 0;
        for (int length = minLength; length <= maxLength; length++) {
            total = (total + f[length]) % MOD;
        }
        return (int) total;
    }
}
