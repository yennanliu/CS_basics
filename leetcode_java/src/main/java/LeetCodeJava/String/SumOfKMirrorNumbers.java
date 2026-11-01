package LeetCodeJava.String;

// https://leetcode.com/problems/sum-of-k-mirror-numbers/description/

/**
 * 2081. Sum of k-Mirror Numbers
 * Hard
 *
 * A k-mirror number is a positive integer without leading zeros that reads the same both forward and backward in base-10 as well as in base-k.
 *
 * For example, 9 is a 2-mirror number. The representation of 9 in base-10 and base-2 are 9 and 1001 respectively, which read the same both forward and backward.
 * On the contrary, 4 is not a 2-mirror number. The representation of 4 in base-2 is 100, which does not read the same both forward and backward.
 *
 * Given the base k and the number n, return the sum of the n smallest k-mirror numbers.
 *
 *
 * Example 1:
 *
 * Input: k = 2, n = 5
 * Output: 25
 * Explanation:
 * The 5 smallest 2-mirror numbers and their representations in base-2 are listed as follows:
 *   base-10    base-2
 *     1          1
 *     3          11
 *     5          101
 *     7          111
 *     9          1001
 * Their sum = 1 + 3 + 5 + 7 + 9 = 25.
 *
 * Example 2:
 *
 * Input: k = 3, n = 7
 * Output: 499
 * Explanation:
 * The 7 smallest 3-mirror numbers are and their representations in base-3 are listed as follows:
 *   base-10    base-3
 *     1          1
 *     2          2
 *     4          11
 *     7          21
 *     121        11111
 *     242        22222
 *     ...
 *
 * Example 3:
 *
 * Input: k = 7, n = 17
 * Output: 20379000
 *
 * Constraints:
 *
 * 2 <= k <= 9
 * 1 <= n <= 30
 *
 */
public class SumOfKMirrorNumbers {

    // V0
    // IDEA: GENERATE BASE-10 PALINDROMES IN ASCENDING ORDER, KEEP THOSE THAT ARE BASE-k PALINDROMES
    //       (testing every integer is hopeless: the 30th 7-mirror number is ~10^10)
    /**
     * time = O(C * D), C = base-10 palindromes generated, D = digits per candidate
     * space = O(D)
     */
    public long kMirror(int k, int n) {
        long sum = 0;
        int found = 0;
        // halfLength = number of digits in the left half (including the middle digit for odd lengths)
        for (int halfLength = 1; ; halfLength++) {
            long low = pow10(halfLength - 1);
            long high = pow10(halfLength);
            // odd total length first, then even: within each, increasing halves give increasing palindromes
            for (int odd = 1; odd >= 0; odd--) {
                for (long half = low; half < high; half++) {
                    long candidate = buildPalindrome(half, odd == 1);
                    if (isPalindromeInBase(candidate, k)) {
                        sum += candidate;
                        found++;
                        if (found == n) {
                            return sum;
                        }
                    }
                }
            }
        }
    }

    /** Mirrors half onto itself; for an odd length the last digit of half is the shared middle. */
    private long buildPalindrome(long half, boolean oddLength) {
        long result = half;
        long rest = oddLength ? half / 10 : half;
        while (rest > 0) {
            result = result * 10 + rest % 10;
            rest /= 10;
        }
        return result;
    }

    /** Returns true when the base-k digits of x read the same both ways. */
    private boolean isPalindromeInBase(long x, int base) {
        StringBuilder digits = new StringBuilder();
        while (x > 0) {
            digits.append(x % base);
            x /= base;
        }
        int left = 0;
        int right = digits.length() - 1;
        while (left < right) {
            if (digits.charAt(left) != digits.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /** 10^exp as a long. */
    private long pow10(int exp) {
        long result = 1;
        for (int i = 0; i < exp; i++) {
            result *= 10;
        }
        return result;
    }
}
