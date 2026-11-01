package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-largest-palindrome-divisible-by-k/description/

/**
 * 3260. Find the Largest Palindrome Divisible by K
 * Hard
 *
 * You are given two positive integers n and k.
 *
 * An integer x is called k-palindromic if:
 *
 * x is a palindrome.
 * x is divisible by k.
 *
 * Return the largest integer having n digits (as a string) that is k-palindromic.
 *
 * Note that the integer must not have leading zeros.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 5
 * Output: "595"
 * Explanation:
 * 595 is the largest k-palindromic integer with 3 digits.
 *
 * Example 2:
 *
 * Input: n = 1, k = 4
 * Output: "8"
 * Explanation:
 * 4 and 8 are the only k-palindromic integers with 1 digit.
 *
 * Example 3:
 *
 * Input: n = 5, k = 6
 * Output: "89898"
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= k <= 9
 *
 */
public class FindTheLargestPalindromeDivisibleByK {

    // V0
    // IDEA: GREEDY DIGIT BY DIGIT + REMAINDER REACHABILITY TABLE
    //       a palindrome is decided by its first ceil(n/2) digits; digit i also sits at
    //       position n-1-i, so its contribution mod k is digit * weight[i] where
    //       weight[i] = (10^(n-1-i) + 10^i) % k   (just 10^i for the middle digit).
    //       reachable[i][r] = can positions i..half-1 sum to remainder r (built backwards).
    //       then walk left to right taking the LARGEST digit that keeps the total
    //       completable to remainder 0. k <= 9, so this is linear in n.
    /**
     * time = O(N * 10 * K)
     * space = O(N * K)
     */
    public String largestPalindrome(int n, int k) {
        int half = (n + 1) / 2;

        // powerOfTen[p] = 10^p % k
        int[] powerOfTen = new int[n];
        powerOfTen[0] = 1 % k;
        for (int p = 1; p < n; p++) {
            powerOfTen[p] = powerOfTen[p - 1] * 10 % k;
        }

        // weight[i] = value mod k contributed by putting digit 1 at mirrored positions i and n-1-i
        int[] weight = new int[half];
        for (int i = 0; i < half; i++) {
            int mirror = n - 1 - i;
            if (i == mirror) {
                weight[i] = powerOfTen[i];
            } else {
                weight[i] = (powerOfTen[i] + powerOfTen[mirror]) % k;
            }
        }

        // reachable[i][r] = positions i .. half-1 can contribute remainder r
        boolean[][] reachable = new boolean[half + 1][k];
        reachable[half][0] = true;
        for (int i = half - 1; i >= 0; i--) {
            for (int digit = 0; digit <= 9; digit++) {
                int add = digit * weight[i] % k;
                for (int r = 0; r < k; r++) {
                    if (reachable[i + 1][r]) {
                        reachable[i][(add + r) % k] = true;
                    }
                }
            }
        }

        // greedy: largest digit at each position that still lets the rest reach remainder 0
        char[] firstHalf = new char[half];
        int currentRemainder = 0;
        for (int i = 0; i < half; i++) {
            int lowestDigit = (i == 0) ? 1 : 0; // no leading zero
            for (int digit = 9; digit >= lowestDigit; digit--) {
                int newRemainder = (currentRemainder + digit * weight[i]) % k;
                int needed = (k - newRemainder) % k;
                if (reachable[i + 1][needed]) {
                    firstHalf[i] = (char) ('0' + digit);
                    currentRemainder = newRemainder;
                    break;
                }
            }
        }

        // mirror the first half (skip the middle digit when n is odd)
        StringBuilder result = new StringBuilder(n);
        result.append(firstHalf);
        int mirrorStart = (n % 2 == 1) ? half - 2 : half - 1;
        for (int i = mirrorStart; i >= 0; i--) {
            result.append(firstHalf[i]);
        }
        return result.toString();
    }
}
