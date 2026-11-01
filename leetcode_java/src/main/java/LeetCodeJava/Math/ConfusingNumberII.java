package LeetCodeJava.Math;

// https://leetcode.com/problems/confusing-number-ii/description/

/**
 * 1088. Confusing Number II
 * Hard
 *
 * A confusing number is a number that when rotated 180 degrees becomes a different number with
 * each digit valid.
 *
 * We can rotate digits of a number by 180 degrees to form new digits.
 *
 * When 0, 1, 6, 8, and 9 are rotated 180 degrees, they become 0, 1, 9, 8, and 6 respectively.
 * When 2, 3, 4, 5, and 7 are rotated 180 degrees, they become invalid.
 *
 * Note that after rotating a number, we can ignore leading zeros.
 *
 * For example, after rotating 8000, we have 0008 which is considered as just 8.
 *
 * Given an integer n, return the number of confusing numbers in the inclusive range [1, n].
 *
 *
 * Example 1:
 *
 * Input: n = 20
 * Output: 6
 * Explanation: The confusing numbers are [6,9,10,16,18,19].
 * 6 converts to 9.
 * 9 converts to 6.
 * 10 converts to 01 which is just 1.
 * 16 converts to 91.
 * 18 converts to 81.
 * 19 converts to 61.
 *
 * Example 2:
 *
 * Input: n = 100
 * Output: 19
 * Explanation: The confusing numbers are
 * [6,9,10,16,18,19,60,61,66,68,80,81,86,89,90,91,98,99,100].
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class ConfusingNumberII {

    // V0
    // IDEA: DFS THAT ONLY BUILDS NUMBERS FROM ROTATABLE DIGITS {0, 1, 6, 8, 9}, BOUNDED BY n
    //   instead of scanning 1..n, extend a candidate one digit at a time (x -> x * 10 + digit)
    //   and stop once it passes n. every candidate is checked: keep it if rotate(x) != x.
    //   at most 5^10 candidates exist, which is why no memo is needed.
    /**
     * time = O(5^L), L = number of digits of n
     * space = O(L) recursion depth
     */
    public int confusingNumberII(int n) {
        int count = 0;
        // start from every non-zero rotatable leading digit
        for (int digit : ROTATABLE) {
            if (digit != 0 && digit <= n) {
                count += dfs(digit, n);
            }
        }
        return count;
    }

    private static final int[] ROTATABLE = {0, 1, 6, 8, 9};
    // ROTATED[d] = d turned upside down; only read for rotatable digits
    private static final int[] ROTATED = {0, 1, -1, -1, -1, -1, 9, -1, 8, 6};

    /** Confusing numbers among current and every rotatable extension of it that is <= n. */
    private int dfs(long current, int n) {
        int count = isConfusing(current) ? 1 : 0;
        for (int digit : ROTATABLE) {
            long next = current * 10 + digit;
            if (next > n) {
                // digits are tried in increasing order, so all later ones are bigger too
                break;
            }
            count += dfs(next, n);
        }
        return count;
    }

    /** Rotating reads x's digits from the right, which reverses it and drops leading zeros. */
    private boolean isConfusing(long x) {
        long rotated = 0;
        long remaining = x;
        while (remaining > 0) {
            int digit = (int) (remaining % 10);
            rotated = rotated * 10 + ROTATED[digit];
            remaining /= 10;
        }
        return rotated != x;
    }
}
