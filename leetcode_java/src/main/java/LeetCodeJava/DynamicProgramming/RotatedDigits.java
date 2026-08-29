package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/rotated-digits/

/**
 *  788. Rotated Digits
 *  Medium
 *
 *  An integer x is a good if after rotating each digit individually by 180
 *  degrees, we get a valid number that is different from x. Each digit must be
 *  rotated - we cannot choose to leave it alone.
 *
 *  A number is valid if each digit remains a digit after rotation. For example:
 *    - 0, 1, and 8 rotate to themselves,
 *    - 2 and 5 rotate to each other (in this case they are rotated in a
 *      different direction, in other words, 2 or 5 gets mirrored),
 *    - 6 and 9 rotate to each other, and
 *    - the rest of the numbers do not rotate to any other number and become
 *      invalid.
 *
 *  Given an integer n, return the number of good integers in the range [1, n].
 *
 *  Example 1:
 *    Input: n = 10
 *    Output: 4
 *    Explanation: There are four good numbers in the range [1, 10]:
 *                 2, 5, 6, 9.
 *
 *  Example 2:
 *    Input: n = 1
 *    Output: 0
 *
 *  Constraints:
 *    - 1 <= n <= 10^4
 */
public class RotatedDigits {

    // V0
    // IDEA: DP on digits - state[i] : 0 = invalid, 1 = valid but unchanged, 2 = good
    /**
     * time = O(N)
     * space = O(N)
     */
    public int rotatedDigits(int n) {
        int[] dp = new int[n + 1];
        int res = 0;

        for (int i = 0; i <= n; i++) {
            if (i < 10) {
                if (i == 0 || i == 1 || i == 8) {
                    dp[i] = 1;
                } else if (i == 2 || i == 5 || i == 6 || i == 9) {
                    dp[i] = 2;
                } else {
                    dp[i] = 0;
                }
            } else {
                int a = dp[i / 10];
                int b = dp[i % 10];
                if (a == 0 || b == 0) {
                    dp[i] = 0;
                } else if (a == 2 || b == 2) {
                    dp[i] = 2;
                } else {
                    dp[i] = 1;
                }
            }
            if (i >= 1 && dp[i] == 2) {
                res++;
            }
        }
        return res;
    }

    // V1
    // IDEA: BRUTE FORCE - check every number's digits directly
    /**
     * time = O(N * log N)
     * space = O(1)
     */
    public int rotatedDigits_1(int n) {
        int res = 0;
        for (int i = 1; i <= n; i++) {
            if (isGood(i)) {
                res++;
            }
        }
        return res;
    }

    private boolean isGood(int x) {
        boolean changed = false;
        while (x > 0) {
            int d = x % 10;
            x /= 10;
            if (d == 3 || d == 4 || d == 7) {
                return false;
            }
            if (d == 2 || d == 5 || d == 6 || d == 9) {
                changed = true;
            }
        }
        return changed;
    }

    // V2
    // IDEA: DIGIT COUNTING (no per-number scan) -
    //       good(n) = #(x in [1,n] whose digits all lie in {0,1,2,5,6,8,9})
    //               - #(x in [1,n] whose digits all lie in {0,1,8})
    //       the subtracted set is exactly the "valid but unchanged" numbers.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int rotatedDigits_2(int n) {
        boolean[] rotatable = new boolean[10];
        for (int d : new int[] { 0, 1, 2, 5, 6, 8, 9 }) {
            rotatable[d] = true;
        }
        boolean[] selfMapping = new boolean[10];
        for (int d : new int[] { 0, 1, 8 }) {
            selfMapping[d] = true;
        }
        return countOnlyDigits_2(n, rotatable) - countOnlyDigits_2(n, selfMapping);
    }

    // count x in [1, n] such that every digit of x is in the allowed set
    private int countOnlyDigits_2(int n, boolean[] ok) {
        if (n <= 0) {
            return 0;
        }
        String s = String.valueOf(n);
        int len = s.length();

        int all = 0;
        int nonZero = 0;
        for (int d = 0; d < 10; d++) {
            if (ok[d]) {
                all++;
                if (d > 0) {
                    nonZero++;
                }
            }
        }

        long res = 0;
        // strictly shorter numbers
        for (int l = 1; l < len; l++) {
            res += nonZero * pow_2(all, l - 1);
        }
        // same length, digit by digit
        boolean tight = true;
        for (int i = 0; i < len; i++) {
            int di = s.charAt(i) - '0';
            for (int d = (i == 0 ? 1 : 0); d < di; d++) {
                if (ok[d]) {
                    res += pow_2(all, len - 1 - i);
                }
            }
            if (!ok[di]) {
                tight = false;
                break;
            }
        }
        if (tight) {
            res++; // n itself
        }
        return (int) res;
    }

    private long pow_2(int base, int exp) {
        long r = 1;
        for (int i = 0; i < exp; i++) {
            r *= base;
        }
        return r;
    }
}
