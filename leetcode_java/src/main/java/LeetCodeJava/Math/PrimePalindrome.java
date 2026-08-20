package LeetCodeJava.Math;

// https://leetcode.com/problems/prime-palindrome/description/
/**
 * 866. Prime Palindrome
 * Medium
 * Topics
 * Companies
 * Given an integer n, return the smallest prime palindrome greater than or equal to n.
 *
 * An integer is prime if it has exactly two divisors: 1 and itself. Note that 1 is not a prime number.
 *
 * For example, 2, 3, 5, 7, 11, and 13 are all primes.
 * An integer is a palindrome if it reads the same from left to right as it does from right to left.
 *
 * For example, 101 and 12321 are palindromes.
 * The test cases are generated so that the answer always exists and is in the range [2, 2 * 108].
 *
 *
 *
 * Example 1:
 *
 * Input: n = 6
 * Output: 7
 * Example 2:
 *
 * Input: n = 8
 * Output: 11
 * Example 3:
 *
 * Input: n = 13
 * Output: 101
 *
 *
 * Constraints:
 *
 * 1 <= n <= 108
 *
 */
public class PrimePalindrome {

    // V0
    // IDEA: GENERATE PALINDROME (from its `left half` / root) + PRIME CHECK
    /**
     *  NOTE !!!
     *
     *   1) we do NOT scan every number >= n and test "is palindrome + is prime"
     *      (that is way too slow), instead we GENERATE palindromes in
     *      INCREASING order and only prime-check those.
     *
     *   2) how to generate ?
     *      -> a palindrome is fully decided by its `left half` (the root)
     *
     *      odd  length : root = 123  -> "123" + reverse("12")  = 12321
     *      even length : root = 123  -> "123" + reverse("123") = 123321
     *
     *   3) ordering !!!
     *      for root length L = 1, 2, 3 ...
     *        - odd  palindromes have (2L - 1) digits
     *        - even palindromes have (2L)     digits
     *      -> so looping L = 1..5 and doing `odd` then `even` inside
     *         visits palindromes by INCREASING digit count,
     *         and within the same digit count, increasing root
     *         => increasing value, so the FIRST hit is the smallest answer
     *
     *   4) since n <= 10^8, the answer always shows up
     *      at or before the 9-digit (L = 5, odd) group
     *
     *   exp 1) n = 6  -> 7
     *   exp 2) n = 8  -> 11   (9 is not prime, 10 is not a palindrome, 11 is both)
     *   exp 3) n = 13 -> 101  (all of 22,33,...99 are divisible by 11)
     */
    /**
     * time = O(sqrt(M) * 10^(L))  ~ scanning at most 10^5 roots,
     *        each with a O(sqrt(x)) prime check
     * space = O(1) (only the palindrome string buffer)
     */
    public int primePalindrome(int n) {
        // edge
        if (n <= 2) {
            return 2;
        }

        for (int L = 1; L <= 5; L++) {
            int lo = (int) Math.pow(10, L - 1);
            int hi = (int) Math.pow(10, L);

            /** NOTE !!! odd length palindrome, e.g. root = 12 -> 121 */
            for (int root = lo; root < hi; root++) {
                long x = buildPalindrome(root, true);
                if (x > Integer.MAX_VALUE) {
                    break;
                }
                if (x >= n && isPrime(x)) {
                    return (int) x;
                }
            }

            /** NOTE !!! even length palindrome, e.g. root = 12 -> 1221 */
            for (int root = lo; root < hi; root++) {
                long x = buildPalindrome(root, false);
                if (x > Integer.MAX_VALUE) {
                    break;
                }
                if (x >= n && isPrime(x)) {
                    return (int) x;
                }
            }
        }

        return -1; // should never reach here (problem guarantees an answer)
    }

    // build a palindrome from its `left half` (root)
    private long buildPalindrome(int root, boolean oddLength) {
        StringBuilder sb = new StringBuilder(String.valueOf(root));
        /**
         *  NOTE !!!
         *
         *   if odd length, we SKIP the last digit of root
         *   when mirroring (that digit is the shared `middle`)
         */
        int start = oddLength ? sb.length() - 2 : sb.length() - 1;
        for (int i = start; i >= 0; i--) {
            sb.append(sb.charAt(i));
        }
        return Long.parseLong(sb.toString());
    }

    private boolean isPrime(long x) {
        if (x < 2) {
            return false;
        }
        if (x % 2 == 0) {
            return x == 2;
        }
        for (long i = 3; i * i <= x; i += 2) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    // V1

    // V2

}
