package LeetCodeJava.Math;

// https://leetcode.com/problems/numbers-at-most-n-given-digit-set/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 902. Numbers At Most N Given Digit Set
 * Hard
 *
 * Given an array of digits which is sorted in non-decreasing order.
 * You can write numbers using each digits[i] as many times as we want.
 * For example, if digits = ['1','3','5'], we may write numbers such as '13', '551',
 * and '1351315'.
 *
 * Return the number of positive integers that can be generated that are less than or
 * equal to a given integer n.
 *
 *
 * Example 1:
 *
 * Input: digits = ["1","3","5","7"], n = 100
 * Output: 20
 * Explanation:
 * The 20 numbers that can be written are:
 * 1, 3, 5, 7, 11, 13, 15, 17, 31, 33, 35, 37, 51, 53, 55, 57, 71, 73, 75, 77.
 *
 * Example 2:
 *
 * Input: digits = ["1","4","9"], n = 1000000000
 * Output: 29523
 *
 * Example 3:
 *
 * Input: digits = ["7"], n = 8
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= digits.length <= 9
 * digits[i].length == 1
 * digits[i] is a digit from '1' to '9'.
 * All the values in digits are unique.
 * digits is sorted in non-decreasing order.
 * 1 <= n <= 10^9
 *
 */
public class NumbersAtMostNGivenDigitSet {

    // V0
    // IDEA: COMBINATORIAL COUNTING (digit by digit)
    /**
     *  Let s = str(n), m = s.length, d = digits.length.
     *
     *  1) EVERY number with STRICTLY FEWER digits than n is valid
     *        -> d^1 + d^2 + ... + d^(m-1)
     *
     *  2) numbers with EXACTLY m digits: walk the PREFIX of s.
     *     At position i, if we place a digit STRICTLY SMALLER than s[i]
     *     (and the prefix so far equals s[0..i)), the remaining m-1-i positions
     *     are FREE -> d^(m-1-i) choices.
     *     To keep walking we must be able to place s[i] ITSELF; if s[i] is not
     *     in digits we can STOP.
     *
     *  3) if we survived the whole walk, n ITSELF is constructible -> +1
     *
     *  time  = O(m * d), m = number of digits of n
     *  space = O(1)
     */
    public int atMostNGivenDigitSet(String[] digits, int n) {
        String s = String.valueOf(n);
        int m = s.length();
        int d = digits.length;

        Set<Character> digitSet = new HashSet<>();
        for (String x : digits) {
            digitSet.add(x.charAt(0));
        }

        long res = 0;

        // 1) all numbers STRICTLY shorter than n
        for (int length = 1; length < m; length++) {
            res += pow(d, length);
        }

        // 2) numbers of the SAME length, matching a prefix of n
        boolean prefixOk = true;
        for (int i = 0; i < m; i++) {
            char cur = s.charAt(i);

            int smaller = 0;
            for (String x : digits) {
                if (x.charAt(0) < cur) {
                    smaller += 1;
                }
            }
            res += smaller * pow(d, m - 1 - i);

            if (!digitSet.contains(cur)) {
                /** NOTE !!!
                 *
                 *  we cannot keep the prefix equal to s[0..i] -> STOP
                 *  (and n itself is therefore NOT constructible)
                 */
                prefixOk = false;
                break;
            }
        }

        // 3) n itself
        if (prefixOk) {
            res += 1;
        }

        return (int) res;
    }

    private long pow(int base, int exp) {
        long r = 1;
        for (int i = 0; i < exp; i++) {
            r *= base;
        }
        return r;
    }

}
