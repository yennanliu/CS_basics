package LeetCodeJava.Math;

// https://leetcode.com/problems/numbers-at-most-n-given-digit-set/description/

import java.util.ArrayDeque;
import java.util.Deque;
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


    // V1
    // IDEA: DIGIT DP (memoised, tight / started flags)
    /**
     *  The mechanical template: walk n's digits deciding each one, carrying whether
     *  the prefix is still tight and whether a digit has been placed yet
     *  (so leading blanks model the SHORTER numbers).
     *
     *  Slower than the combinatorial count, but it generalises to `digits with a
     *  property` questions where the closed form disappears.
     *
     *  time  = O(m * 2 * 2 * d)
     *  space = O(m * 2 * 2)
     */
    public int atMostNGivenDigitSet_1(String[] digits, int n) {
        char[] s = String.valueOf(n).toCharArray();
        char[] allowed = new char[digits.length];
        for (int i = 0; i < digits.length; i++) {
            allowed[i] = digits[i].charAt(0);
        }
        Integer[][][] memo = new Integer[s.length][2][2];
        return dp(s, allowed, 0, 1, 0, memo);
    }

    private int dp(char[] s, char[] allowed, int pos, int tight, int started,
                   Integer[][][] memo) {
        if (pos == s.length) {
            return started;   // count it only if at least one digit was placed
        }
        if (memo[pos][tight][started] != null) {
            return memo[pos][tight][started];
        }

        int total = 0;
        // option 1 : place nothing yet (only while the number has not started)
        if (started == 0) {
            total += dp(s, allowed, pos + 1, 0, 0, memo);
        }
        int limit = tight == 1 ? s[pos] : '9';
        for (char c : allowed) {
            if (c > limit) {
                break;   // `allowed` is sorted ascending
            }
            total += dp(s, allowed, pos + 1, (tight == 1 && c == limit) ? 1 : 0, 1, memo);
        }

        memo[pos][tight][started] = total;
        return total;
    }

    // V2
    // IDEA: CLOSED FORM WITH Math.pow-FREE POWERS, counted from the back
    /**
     *  Walk the digits of n from the LEAST significant end, keeping a running power
     *  of d. The `numbers with fewer digits` term then falls out of the same loop
     *  as a geometric series rather than needing a separate pass.
     *
     *  One loop instead of two.
     *
     *  time  = O(m * d)
     *  space = O(1)
     */
    public int atMostNGivenDigitSet_2(String[] digits, int n) {
        String s = String.valueOf(n);
        int m = s.length();
        int d = digits.length;

        long[] pow = new long[m + 1];
        pow[0] = 1;
        for (int i = 1; i <= m; i++) {
            pow[i] = pow[i - 1] * d;
        }

        long res = 0;
        // shorter numbers: d + d^2 + ... + d^(m-1)
        for (int len = 1; len < m; len++) {
            res += pow[len];
        }

        for (int i = 0; i < m; i++) {
            boolean matched = false;
            for (String x : digits) {
                char c = x.charAt(0);
                if (c < s.charAt(i)) {
                    res += pow[m - 1 - i];
                } else if (c == s.charAt(i)) {
                    matched = true;
                    break;
                } else {
                    break;
                }
            }
            if (!matched) {
                return (int) res;
            }
        }
        return (int) (res + 1);   // n itself
    }

    // V3
    // IDEA: BRUTE FORCE -- generate every constructible number up to n
    /**
     *  BFS over the constructible numbers: start from each digit and keep appending
     *  while the value stays <= n.
     *
     *  O(answer), so it is only usable when the count is small, but it produces the
     *  actual NUMBERS rather than just their count -- the oracle for the counting
     *  versions.
     *
     *  time  = O(answer * d)
     *  space = O(answer)
     */
    public int atMostNGivenDigitSet_3(String[] digits, int n) {
        Deque<Long> q = new ArrayDeque<>();
        for (String x : digits) {
            long v = Long.parseLong(x);
            if (v <= n) {
                q.offer(v);
            }
        }

        int count = 0;
        while (!q.isEmpty()) {
            long cur = q.poll();
            count += 1;
            for (String x : digits) {
                long nxt = cur * 10 + Long.parseLong(x);
                if (nxt <= n) {
                    q.offer(nxt);
                }
            }
        }
        return count;
    }

}
