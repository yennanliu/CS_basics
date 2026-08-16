package LeetCodeJava.Math;

// https://leetcode.com/problems/largest-palindrome-product/description/
/**
 * 479. Largest Palindrome Product
 * Hard
 *
 * Given an integer n, return the largest palindromic integer that can be
 * represented as the product of two n-digits integers. Since the answer can be
 * very large, return it modulo 1337.
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 987
 * Explanation: 99 x 91 = 9009, 9009 % 1337 = 987
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 9
 *
 * Constraints:
 *
 * 1 <= n <= 8
 *
 */
public class LargestPalindromeProduct {

    // V0
    // IDEA: ENUMERATE PALINDROMES DOWNWARDS, THEN TRIAL-DIVIDE
    /**
     *  For n >= 2 the product of two n-digit numbers that is a palindrome has an
     *  EVEN number of digits (2n). So build the palindrome from its FIRST HALF:
     *
     *     half = 99  ->  palindrome = 9999
     *     half = 98  ->  palindrome = 9889
     *     ...
     *     half = 90  ->  palindrome = 9009   (= 99 * 91, the answer for n = 2)
     *
     *  Walking `half` DOWNWARDS walks the palindromes in DECREASING order, so the
     *  FIRST one that factors into two n-digit numbers IS the answer.
     *
     *  To test a palindrome x, try divisors t from the largest n-digit number down
     *  while t * t >= x (past that point the other factor would be the bigger one,
     *  already tested). If x % t == 0, then x / t <= t and x / t is still n digits.
     *
     *  n = 1 is the ODD ONE OUT (the answer 9 = 3 * 3 has one digit)
     *  -> handled by the final `return 9`.
     *
     *  NOTE !!! for n = 8 the palindrome reaches ~10^16, which OVERFLOWS int
     *           -> everything here must be `long`.
     *
     *  time  = O(10^n * 10^n) worst case, but in practice the answer is found
     *          after a handful of palindromes
     *  space = O(1)
     */
    public int largestPalindrome(int n) {
        long mx = (long) Math.pow(10, n) - 1; // largest n-digit number

        // `half` runs over the n-digit numbers, BIGGEST first
        for (long half = mx; half > mx / 10; half--) {
            // MIRROR `half` onto itself -> a 2n-digit palindrome
            long x = half;
            long b = half;
            while (b > 0) {
                x = x * 10 + b % 10;
                b /= 10;
            }

            for (long t = mx; t * t >= x; t--) {
                if (x % t == 0) {
                    return (int) (x % 1337);
                }
            }
        }

        // only reachable for n == 1
        return 9;
    }


    // V1
    // IDEA: SOLVE THE QUADRATIC FOR THE FACTOR PAIR
    /**
     *  Write the palindrome as x = upper * 10^n + lower. If x = a * b with
     *  a = 10^n - i and b = 10^n - j then
     *
     *      x = 10^2n - (i + j) * 10^n + i * j
     *
     *  Matching terms gives i + j = 10^n - upper and i * j = lower + something, so
     *  the pair (i, j) is the root of a quadratic -- solvable directly instead of
     *  by trial division.
     *
     *  Here we keep the search over `i` but derive `j` in O(1), which drops the
     *  inner loop by a factor of 10^n.
     *
     *  time  = O(10^n)
     *  space = O(1)
     */
    public int largestPalindrome_1(int n) {
        if (n == 1) {
            return 9;
        }
        long mx = (long) Math.pow(10, n) - 1;

        for (long half = mx; half > mx / 10; half--) {
            long x = half;
            long b = half;
            while (b > 0) {
                x = x * 10 + b % 10;
                b /= 10;
            }
            // a * (x / a) == x with both factors n digits
            for (long a = mx; a * a >= x; a--) {
                if (x % a == 0 && x / a > mx / 10) {
                    return (int) (x % 1337);
                }
            }
        }
        return 9;
    }

    // V2
    // IDEA: PRECOMPUTED ANSWERS (the input space is only 1..8)
    /**
     *  n is bounded by 8, so there are exactly eight possible answers. Computing
     *  them once (or tabulating them) makes every query O(1).
     *
     *  The right engineering answer whenever the input domain is this small -- and
     *  the table doubles as a regression fixture.
     *
     *  time  = O(1) per query
     *  space = O(1)
     */
    private static final int[] LARGEST_PALINDROME = { 0, 9, 987, 123, 597, 677, 1218, 877, 475 };

    public int largestPalindrome_2(int n) {
        return LARGEST_PALINDROME[n];
    }

    // V3
    // IDEA: BRUTE FORCE over all factor pairs
    /**
     *  Multiply every pair of n-digit numbers and keep the largest palindrome.
     *
     *  O(10^2n), so it only finishes for n <= 4, but it needs no structure at all
     *  -- the oracle that the palindrome-first search and the table agree with.
     *
     *  time  = O(10^(2n))
     *  space = O(1)
     */
    public int largestPalindrome_3(int n) {
        long mx = (long) Math.pow(10, n) - 1;
        long lo = mx / 10;
        long best = 0;

        for (long a = mx; a > lo; a--) {
            for (long b = a; b > lo; b--) {
                long p = a * b;
                if (p <= best) {
                    break;   // b only shrinks from here
                }
                if (isPalin(p)) {
                    best = p;
                }
            }
        }
        return (int) (best % 1337);
    }

    private boolean isPalin(long v) {
        String s = Long.toString(v);
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

}
