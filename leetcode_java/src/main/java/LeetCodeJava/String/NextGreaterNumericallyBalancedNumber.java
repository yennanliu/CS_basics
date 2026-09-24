package LeetCodeJava.String;

// https://leetcode.com/problems/next-greater-numerically-balanced-number/

/**
 * 2048. Next Greater Numerically Balanced Number
 * Medium
 *
 * An integer x is numerically balanced if for every digit d in the number x,
 * there are exactly d occurrences of that digit in x.
 *
 * Given an integer n, return the smallest numerically balanced number
 * strictly greater than n.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 22
 * Explanation:
 * 22 is numerically balanced since:
 * - The digit 2 occurs 2 times.
 * It is also the smallest numerically balanced number strictly greater than 1.
 *
 * Example 2:
 *
 * Input: n = 1000
 * Output: 1333
 * Explanation:
 * 1333 is numerically balanced since:
 * - The digit 1 occurs 1 time.
 * - The digit 3 occurs 3 times.
 * It is also the smallest numerically balanced number strictly greater than 1000.
 * Note that 1333 has the same number of occurrences of the digit 1 and 3.
 *
 * Example 3:
 *
 * Input: n = 3000
 * Output: 3133
 * Explanation:
 * 3133 is numerically balanced since:
 * - The digit 1 occurs 1 time.
 * - The digit 3 occurs 3 times.
 * It is also the smallest numerically balanced number strictly greater than 3000.
 *
 *
 * Constraints:
 *
 * 0 <= n <= 10^6
 *
 */
public class NextGreaterNumericallyBalancedNumber {

    // V0
    // IDEA: BRUTE-FORCE SCAN UPWARD (the search space is provably tiny)
    //       n <= 10^6 and 1224444 is balanced, so the answer is < ~1.3 * 10^6;
    //       check every candidate with a digit count: cnt[d] == d for every
    //       digit that appears. a '0' can never appear (it would need 0
    //       occurrences of itself), so any 0 digit rejects the candidate.
    /**
     * time = O(A * log A), A = the answer
     * space = O(1)
     */
    public int nextBeautifulNumber(int n) {
        int x = n + 1;
        while (!isBalanced(x)) {
            x++;
        }
        return x;
    }

    private boolean isBalanced(int x) {
        int[] cnt = new int[10];
        while (x > 0) {
            cnt[x % 10]++;
            x /= 10;
        }
        // NOTE !!! a digit d must appear exactly d times, or not at all
        for (int d = 0; d < 10; d++) {
            if (cnt[d] != 0 && cnt[d] != d) {
                return false;
            }
        }
        return true;
    }
}
