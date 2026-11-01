package LeetCodeJava.Math;

// https://leetcode.com/problems/count-special-integers/description/

/**
 * 2376. Count Special Integers
 * Hard
 *
 * We call a positive integer special if all of its digits are distinct.
 *
 * Given a positive integer n, return the number of special integers that belong to the interval [1, n].
 *
 *
 * Example 1:
 *
 * Input: n = 20
 * Output: 19
 * Explanation: All the integers from 1 to 20, except 11, are special. Thus, there are 19 special integers.
 *
 * Example 2:
 *
 * Input: n = 5
 * Output: 5
 * Explanation: All the integers from 1 to 5 are special.
 *
 * Example 3:
 *
 * Input: n = 135
 * Output: 110
 * Explanation: There are 110 integers from 1 to 135 that are special.
 * Some of the integers that are not special are: 22, 114, and 131.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 10^9
 *
 */
public class CountSpecialIntegers {

    // V0
    // IDEA: COUNT BY DIGIT LENGTH, THEN WALK n's DIGITS FIXING A COMMON PREFIX (COMBINATORICS)
    //   part 1: any length L below len(n) gives 9 * P(9, L - 1) numbers (no leading zero).
    //   part 2: same length as n - at position i, every unused digit below n[i] frees the
    //           remaining slots to any arrangement of the 9 - i unused digits: P(9 - i, len - i - 1).
    //           then fix n[i] and continue, unless it repeats a used digit.
    //   if the walk never breaks, n itself is special.
    /**
     * time = O(digits^2)
     * space = O(digits)
     */
    public int countSpecialNumbers(int n) {
        String digits = String.valueOf(n);
        int length = digits.length();

        int count = 0;
        // part 1: shorter numbers
        for (int len = 1; len < length; len++) {
            count += 9 * permutations(9, len - 1);
        }

        // part 2: numbers with the same length as n, smaller than n
        boolean[] used = new boolean[10];
        boolean nIsSpecial = true;
        for (int i = 0; i < length; i++) {
            int digit = digits.charAt(i) - '0';
            // the leading digit cannot be 0
            int lowest = (i == 0) ? 1 : 0;
            for (int smaller = lowest; smaller < digit; smaller++) {
                if (!used[smaller]) {
                    count += permutations(9 - i, length - i - 1);
                }
            }
            // n's own digit repeats -> no longer prefix of n can be special
            if (used[digit]) {
                nIsSpecial = false;
                break;
            }
            used[digit] = true;
        }

        if (nIsSpecial) {
            count++;
        }
        return count;
    }

    /** P(m, k) = m * (m - 1) * ... * (m - k + 1). */
    private int permutations(int m, int k) {
        int result = 1;
        for (int t = 0; t < k; t++) {
            result *= m - t;
        }
        return result;
    }
}
