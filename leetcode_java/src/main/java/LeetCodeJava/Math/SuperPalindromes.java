package LeetCodeJava.Math;

// https://leetcode.com/problems/super-palindromes/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 906. Super Palindromes
 * Hard
 *
 * Let's say a positive integer is a super-palindrome if it is a palindrome,
 * and it is also the square of a palindrome.
 *
 * Given two positive integers left and right represented as strings, return the number
 * of super-palindromes integers in the inclusive range [left, right].
 *
 *
 * Example 1:
 *
 * Input: left = "4", right = "1000"
 * Output: 4
 * Explanation: 4, 9, 121, and 484 are superpalindromes.
 * Note that 676 is not a superpalindrome: 26 * 26 = 676, but 26 is not a palindrome.
 *
 * Example 2:
 *
 * Input: left = "1", right = "2"
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= left.length, right.length <= 18
 * left and right consist of only digits.
 * left and right cannot have leading zeros.
 * left and right represent integers in the range [1, 10^18 - 1].
 * left is less than or equal to right.
 *
 */
public class SuperPalindromes {

    // V0
    // IDEA: ENUMERATE THE PALINDROMIC ROOT (build palindromes, don't test them)
    /**
     *  A super-palindrome x satisfies x = p * p where BOTH x and p are palindromes.
     *  Since x < 10^18, we know p < 10^9. Testing every p up to 10^9 is WAY too slow,
     *  so instead we GENERATE palindromic p directly:
     *
     *     take a seed i (1 .. 10^5) and MIRROR it
     *         even length : str(i) + reverse(str(i))          e.g. 12 -> 1221
     *         odd  length : str(i) + reverse(str(i) minus last) e.g. 12 -> 121
     *
     *  That yields EVERY palindrome below 10^10, which more than covers p < 10^9.
     *  For each such p we only need to check that p * p lands in [left, right]
     *  and is ITSELF a palindrome.
     *
     *  NOTE !!! values reach 10^18 -> `long` throughout, and the `p > 10^9` guard
     *           below keeps `p * p` from overflowing.
     *
     *  time  = O(M^(1/4) * log(M)), M = 10^18 -> ~10^5 roots, each checked in O(18)
     *  space = O(1)
     */
    public int superpalindromesInRange(String left, String right) {
        long lo = Long.parseLong(left);
        long hi = Long.parseLong(right);

        int res = 0;

        // seeds up to 10^5 generate every palindrome with <= 10 digits
        for (int i = 1; i < 100000; i++) {
            String s = String.valueOf(i);
            String rev = new StringBuilder(s).reverse().toString();

            String[] roots = {
                    s + rev,                                                    // even length
                    s + new StringBuilder(s.substring(0, s.length() - 1)).reverse() // odd length
            };

            for (String root : roots) {
                long p = Long.parseLong(root);
                /** NOTE !!!
                 *
                 *  guard against p * p overflowing long
                 *  (anything above 10^9 squares past the 10^18 upper bound anyway)
                 */
                if (p > 1000000000L) {
                    continue;
                }
                long x = p * p;
                if (lo <= x && x <= hi && isPalindrome(x)) {
                    res += 1;
                }
            }
        }

        return res;
    }

    private boolean isPalindrome(long x) {
        String t = String.valueOf(x);
        int i = 0;
        int j = t.length() - 1;
        while (i < j) {
            if (t.charAt(i) != t.charAt(j)) {
                return false;
            }
            i += 1;
            j -= 1;
        }
        return true;
    }


    // V1
    // IDEA: PRECOMPUTE EVERY SUPER-PALINDROME ONCE, then binary search the range
    /**
     *  There are only a few dozen super-palindromes below 10^18. Generating them
     *  ONCE into a sorted list turns each query into two binary searches.
     *
     *  -> answering m queries costs O(10^5 + m log C) instead of O(m * 10^5).
     *
     *  time  = O(10^5) once, then O(log C) per query
     *  space = O(number of super-palindromes)
     */
    private static List<Long> allSuper;

    public int superpalindromesInRange_1(String left, String right) {
        if (allSuper == null) {
            allSuper = buildSuper();
        }
        long lo = Long.parseLong(left);
        long hi = Long.parseLong(right);

        int a = lowerIdx(allSuper, lo);
        int b = upperIdx(allSuper, hi);
        return b - a;
    }

    private List<Long> buildSuper() {
        List<Long> out = new ArrayList<>();
        for (int i = 1; i < 100000; i++) {
            String s = String.valueOf(i);
            String rev = new StringBuilder(s).reverse().toString();
            String[] roots = { s + rev,
                    s + new StringBuilder(s.substring(0, s.length() - 1)).reverse() };
            for (String root : roots) {
                long p = Long.parseLong(root);
                if (p > 1000000000L) {
                    continue;
                }
                long x = p * p;
                if (isPal(x)) {
                    out.add(x);
                }
            }
        }
        Collections.sort(out);
        // the two root families can produce the same square
        List<Long> uniq = new ArrayList<>();
        for (long v : out) {
            if (uniq.isEmpty() || uniq.get(uniq.size() - 1) != v) {
                uniq.add(v);
            }
        }
        return uniq;
    }

    private boolean isPal(long x) {
        String t = Long.toString(x);
        int i = 0;
        int j = t.length() - 1;
        while (i < j) {
            if (t.charAt(i++) != t.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

    private int lowerIdx(List<Long> list, long v) {
        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) < v) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    private int upperIdx(List<Long> list, long v) {
        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) <= v) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // V2
    // IDEA: BUILD PALINDROMES BY MIRRORING A HALF, both parities in one loop
    /**
     *  Rather than two explicit root strings per seed, treat the seed's digits as
     *  the left half and mirror with or without repeating the middle digit,
     *  generating both parities from the same numeric loop.
     *
     *  Numeric mirroring (no String at all) makes the generation allocation free.
     *
     *  time  = O(M^(1/4) * log M)
     *  space = O(1)
     */
    public int superpalindromesInRange_2(String left, String right) {
        long lo = Long.parseLong(left);
        long hi = Long.parseLong(right);
        int res = 0;

        for (int seed = 1; seed < 100000; seed++) {
            for (int odd = 0; odd < 2; odd++) {
                long root = seed;
                int rest = odd == 1 ? seed / 10 : seed;  // skip the middle digit when odd
                while (rest > 0) {
                    root = root * 10 + rest % 10;
                    rest /= 10;
                }
                if (root > 1000000000L) {
                    continue;
                }
                long x = root * root;
                if (x >= lo && x <= hi && isPal(x)) {
                    res += 1;
                }
            }
        }
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE over the palindromic ROOTS by testing every p
    /**
     *  Test every p up to 10^5 (enough for the small ranges) for `p is a
     *  palindrome and p*p is a palindrome`.
     *
     *  Only correct for ranges below 10^10, so it is a partial oracle -- included
     *  to show why the mirroring generation is needed at all.
     *
     *  time  = O(10^5 * log)
     *  space = O(1)
     */
    public int superpalindromesInRange_3(String left, String right) {
        long lo = Long.parseLong(left);
        long hi = Long.parseLong(right);
        int res = 0;
        for (long p = 1; p * p <= hi && p <= 100000; p++) {
            long x = p * p;
            if (x >= lo && isPal(p) && isPal(x)) {
                res += 1;
            }
        }
        return res;
    }

}
