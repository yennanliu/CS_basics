package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-count-of-good-integers/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 3272. Find the Count of Good Integers
 * Hard
 *
 * You are given two positive integers n and k.
 *
 * An integer x is called k-palindromic if:
 *
 * x is a palindrome.
 * x is divisible by k.
 *
 * An integer is called good if its digits can be rearranged to form a k-palindromic integer. For
 * example, for k = 2, 2020 can be rearranged to form the k-palindromic integer 0202, whereas 1010 cannot
 * be rearranged to form a k-palindromic integer.
 *
 * Return the count of good integers containing n digits.
 *
 * Note that any integer must not have leading zeros, neither before nor after rearrangement. For
 * example, 1010 cannot be rearranged to form 101.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 5
 * Output: 27
 * Explanation:
 * Some of the good integers are:
 * 551 because it can be rearranged to form 515.
 * 525 because it is already k-palindromic.
 *
 * Example 2:
 *
 * Input: n = 1, k = 4
 * Output: 2
 * Explanation:
 * The two good integers are 4 and 8.
 *
 * Example 3:
 *
 * Input: n = 5, k = 6
 * Output: 2468
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10
 * 1 <= k <= 9
 *
 */
public class FindTheCountOfGoodIntegers {

    // V0
    // IDEA: ENUMERATE PALINDROMES BY THEIR FIRST HALF, KEEP THOSE DIVISIBLE BY k, DEDUPLICATE BY DIGIT
    //   MULTISET, THEN COUNT n-DIGIT ARRANGEMENTS OF EACH MULTISET WITH NO LEADING ZERO:
    //   (n - count[0]) * (n - 1)! / prod(count[d]!)
    /**
     * time = O(10^ceil(N/2) * N)
     * space = O(10^ceil(N/2))
     */
    public long countGoodIntegers(int n, int k) {
        long[] factorial = new long[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i;
        }

        int halfLength = (n + 1) / 2;
        int start = 1;
        for (int i = 1; i < halfLength; i++) {
            start *= 10;
        }
        int end = start * 10;

        // sorted-digit signature of every k-palindromic n-digit number
        Set<String> multisets = new HashSet<>();
        for (int firstHalf = start; firstHalf < end; firstHalf++) {
            String left = String.valueOf(firstHalf);
            String mirrored = new StringBuilder(left).reverse().toString();
            // odd n: the middle digit is shared, so drop it from the mirrored half
            if (n % 2 == 1) {
                mirrored = mirrored.substring(1);
            }
            String palindrome = left + mirrored;
            if (Long.parseLong(palindrome) % k != 0) {
                continue;
            }
            char[] digits = palindrome.toCharArray();
            Arrays.sort(digits);
            multisets.add(new String(digits));
        }

        long goodCount = 0;
        for (String signature : multisets) {
            int[] digitCount = new int[10];
            for (int i = 0; i < signature.length(); i++) {
                digitCount[signature.charAt(i) - '0']++;
            }
            // pick a non-zero leading digit, arrange the remaining n - 1 digits freely,
            // and divide out the orderings of equal digits
            long arrangements = (n - digitCount[0]) * factorial[n - 1];
            for (int count : digitCount) {
                arrangements /= factorial[count];
            }
            goodCount += arrangements;
        }
        return goodCount;
    }
}
