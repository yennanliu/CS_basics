package LeetCodeJava.Math;

// https://leetcode.com/problems/total-characters-in-string-after-transformations-ii/description/

import java.util.List;

/**
 * 3337. Total Characters in String After Transformations II
 * Hard
 *
 * You are given a string s consisting of lowercase English letters, an integer t representing the
 * number of transformations to perform, and an array nums of size 26. In one transformation, every
 * character in s is replaced according to the following rules:
 *
 * Replace s[i] with the next nums[s[i] - 'a'] consecutive characters in the alphabet. For example, if
 * s[i] = 'a' and nums[0] = 3, the character 'a' transforms into the next 3 consecutive characters ahead
 * of it, which results in "bcd".
 * The transformation wraps around the alphabet if it exceeds 'z'. For example, if s[i] = 'y' and
 * nums[24] = 3, the character 'y' transforms into the next 3 consecutive characters ahead of it, which
 * results in "zab".
 *
 * Return the length of the resulting string after exactly t transformations.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "abcyy", t = 2, nums = [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2]
 * Output: 7
 * Explanation:
 * String after the first transformation: "bcdzz"
 * String after the second transformation: "cdeabab"
 * Final Length of the string: The string is "cdeabab", which has 7 characters.
 *
 * Example 2:
 *
 * Input: s = "azbk", t = 1, nums = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2]
 * Output: 8
 * Explanation:
 * String after the first transformation: "bccdablm"
 * Final Length of the string: The string is "bccdablm", which has 8 characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists only of lowercase English letters.
 * 1 <= t <= 10^9
 * nums.length == 26
 * 1 <= nums[i] <= 25
 *
 */
public class TotalCharactersInStringAfterTransformationsII {

    private static final int MOD = 1_000_000_007;
    private static final int ALPHABET = 26;

    // V0
    // IDEA: MATRIX EXPONENTIATION — ONE ROUND IS A LINEAR MAP ON THE 26 LETTER COUNTS
    //   transition[(c + j) % 26][c] = 1 for j = 1 .. nums[c]; after t rounds counts = transition^t * counts.
    //   (simulating round by round, as in LC 3335, is O(26 * t) and times out for t = 10^9)
    /**
     * time = O(26^3 * log T + N)
     * space = O(26^2)
     */
    public int lengthAfterTransformations(String s, int t, List<Integer> nums) {
        // column c of the matrix says which letters one copy of letter c turns into
        long[][] transition = new long[ALPHABET][ALPHABET];
        for (int c = 0; c < ALPHABET; c++) {
            int produced = nums.get(c);
            for (int j = 1; j <= produced; j++) {
                transition[(c + j) % ALPHABET][c] += 1;
            }
        }

        long[][] power = matrixPower(transition, t);

        // histogram of the starting string; the string itself is never built
        long[] letterCount = new long[ALPHABET];
        for (int i = 0; i < s.length(); i++) {
            letterCount[s.charAt(i) - 'a']++;
        }

        // total length = sum over all letters of (power * letterCount)
        long length = 0;
        for (int row = 0; row < ALPHABET; row++) {
            for (int col = 0; col < ALPHABET; col++) {
                length = (length + power[row][col] * letterCount[col]) % MOD;
            }
        }
        return (int) length;
    }

    /** base^exponent by repeated squaring. */
    private long[][] matrixPower(long[][] base, int exponent) {
        // start from the identity matrix
        long[][] result = new long[ALPHABET][ALPHABET];
        for (int i = 0; i < ALPHABET; i++) {
            result[i][i] = 1;
        }
        long[][] square = base;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = multiply(result, square);
            }
            square = multiply(square, square);
            exponent /= 2;
        }
        return result;
    }

    /** Matrix product mod MOD; each term is < MOD^2 < Long.MAX_VALUE, so reducing per term is safe. */
    private long[][] multiply(long[][] a, long[][] b) {
        long[][] product = new long[ALPHABET][ALPHABET];
        for (int i = 0; i < ALPHABET; i++) {
            for (int k = 0; k < ALPHABET; k++) {
                if (a[i][k] == 0) {
                    continue;
                }
                for (int j = 0; j < ALPHABET; j++) {
                    product[i][j] = (product[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        return product;
    }
}
