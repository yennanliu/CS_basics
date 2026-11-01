package LeetCodeJava.Math;

// https://leetcode.com/problems/abbreviating-the-product-of-a-range/description/

import java.math.BigInteger;

/**
 * 2117. Abbreviating the Product of a Range
 * Hard
 *
 * You are given two positive integers left and right with left <= right. Calculate the product of all integers in the inclusive range [left, right].
 *
 * Since the product may be very large, you will abbreviate it following these steps:
 *
 * Count all trailing zeros in the product and remove them. Let us denote this count as C.
 * For example, there are 3 trailing zeros in 1000, and there are 0 trailing zeros in 546.
 * Denote the remaining number of digits in the product as d. If d > 10, then express the product as <pre>...<suf> where <pre> denotes the first 5 digits of the product, and <suf> denotes the last 5 digits of the product after removing all trailing zeros. If d <= 10, we keep it unchanged.
 * For example, we express 1234567654321 as 12345...54321, but 1234567 is represented as 1234567.
 * Finally, represent the product as a string "<pre>...<suf>eC".
 * For example, 12345678987600000 will be represented as "12345...89876e5".
 *
 * Return a string denoting the abbreviated product of all integers in the inclusive range [left, right].
 *
 *
 * Example 1:
 *
 * Input: left = 1, right = 4
 * Output: "24e0"
 * Explanation: The product is 1 x 2 x 3 x 4 = 24.
 * There are no trailing zeros, so 24 remains the same. The abbreviation will end with "e0".
 * Since the number of digits is 2, which is less than 10, we do not have to abbreviate it further.
 * Thus, the final representation is "24e0".
 *
 * Example 2:
 *
 * Input: left = 2, right = 11
 * Output: "399168e2"
 * Explanation: The product is 39916800.
 * There are 2 trailing zeros, which we remove to get 399168. The abbreviation will end with "e2".
 * The number of digits after removing the trailing zeros is 6, so we do not abbreviate it further.
 * Hence, the abbreviated product is "399168e2".
 *
 * Example 3:
 *
 * Input: left = 371, right = 375
 * Output: "7219856259e3"
 * Explanation: The product is 7219856259000.
 *
 *
 * Constraints:
 *
 * 1 <= left <= right <= 10^4
 *
 */
public class AbbreviatingTheProductOfARange {

    private static final long SUFFIX_MOD = 100_000L;
    // the truncated prefix keeps 30 significant digits: a 15-digit window is NOT enough,
    // because flooring error accumulates over 10^4 steps (4838..6186 is 36087999... vs 36088000...)
    private static final BigInteger PREFIX_LIMIT = BigInteger.TEN.pow(30);

    // V0
    // IDEA: EXACT BigInteger PRODUCT, THEN STRIP ZEROS AND ABBREVIATE
    //  at most 10^4 multiplications of a <= 36k-digit number: a few million word operations.
    //  the simplest correct answer in Java; use it in an interview unless asked to avoid big numbers.
    /**
     * time = O((right - left) * D), D = digits of the product (<= ~36k)
     * space = O(D)
     */
    public String abbreviateProduct(int left, int right) {
        BigInteger product = BigInteger.ONE;
        for (int i = left; i <= right; i++) {
            product = product.multiply(BigInteger.valueOf(i));
        }

        String digits = product.toString();

        // count and drop the trailing zeros
        int zeros = 0;
        while (digits.charAt(digits.length() - 1 - zeros) == '0') {
            zeros++;
        }
        String core = digits.substring(0, digits.length() - zeros);

        if (core.length() <= 10) {
            return core + "e" + zeros;
        }
        String head = core.substring(0, 5);
        String tail = core.substring(core.length() - 5);
        return head + "..." + tail + "e" + zeros;
    }

    // V1
    // IDEA: HANDLE THE THREE PIECES SEPARATELY - TRAILING ZEROS, SUFFIX, PREFIX (no full product)
    //  1) C = min(#factors of 2, #factors of 5) over the range.
    //  2) suffix: product mod 10^5 after dividing out exactly C twos and C fives.
    //  3) prefix + digit count: a truncated running product (drop low digits, count them).
    //  if at most 10 digits remain, the exact product is small, so compute it directly.
    /**
     * time = O((right - left) * log(right))
     * space = O(1)
     */
    public String abbreviateProduct_1(int left, int right) {
        // 1) trailing zeros
        long twos = 0;
        long fives = 0;
        for (int i = left; i <= right; i++) {
            int x = i;
            while (x % 2 == 0) {
                x /= 2;
                twos++;
            }
            x = i;
            while (x % 5 == 0) {
                x /= 5;
                fives++;
            }
        }
        long zeros = Math.min(twos, fives);

        // 3) truncated running product -> leading digits and the exact digit count
        BigInteger prefix = BigInteger.ONE;
        long droppedDigits = 0;
        for (int i = left; i <= right; i++) {
            prefix = prefix.multiply(BigInteger.valueOf(i));
            while (prefix.compareTo(PREFIX_LIMIT) >= 0) {
                prefix = prefix.divide(BigInteger.TEN);
                droppedDigits++;
            }
        }
        String prefixDigits = prefix.toString();
        long totalDigits = prefixDigits.length() + droppedDigits;
        long remainingDigits = totalDigits - zeros;

        if (remainingDigits <= 10) {
            // small enough to write out exactly
            BigInteger product = BigInteger.ONE;
            for (int i = left; i <= right; i++) {
                product = product.multiply(BigInteger.valueOf(i));
            }
            product = product.divide(BigInteger.TEN.pow((int) zeros));
            return product.toString() + "e" + zeros;
        }

        // 2) last five digits with the C factors of 10 taken out
        long suffix = 1;
        long twosToRemove = zeros;
        long fivesToRemove = zeros;
        for (int i = left; i <= right; i++) {
            long x = i;
            while (twosToRemove > 0 && x % 2 == 0) {
                x /= 2;
                twosToRemove--;
            }
            while (fivesToRemove > 0 && x % 5 == 0) {
                x /= 5;
                fivesToRemove--;
            }
            suffix = suffix * x % SUFFIX_MOD;
        }

        String head = prefixDigits.substring(0, 5);
        String tail = String.format("%05d", suffix);
        return head + "..." + tail + "e" + zeros;
    }
}
