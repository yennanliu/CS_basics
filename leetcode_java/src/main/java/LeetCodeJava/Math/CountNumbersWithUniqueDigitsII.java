package LeetCodeJava.Math;

// https://leetcode.com/problems/count-numbers-with-unique-digits-ii/description/

/**
 * 3032. Count Numbers With Unique Digits II
 * Easy
 *
 * Given two positive integers a and b, return the count of numbers having unique digits in the range [a, b] (inclusive).
 *
 *
 * Example 1:
 *
 * Input: a = 1, b = 20
 * Output: 19
 * Explanation: All the numbers in the range [1, 20] have unique digits except 11. Hence, the answer is 19.
 *
 * Example 2:
 *
 * Input: a = 9, b = 19
 * Output: 10
 *
 * Example 3:
 *
 * Input: a = 80, b = 120
 * Output: 27
 *
 *
 * Constraints:
 *
 * 1 <= a <= b <= 1000
 *
 */
public class CountNumbersWithUniqueDigitsII {

    // V0
    // IDEA: BRUTE FORCE SCAN -- b <= 1000, so test every number's digits with a seen[10] array
    /**
     * time = O((b - a) * D)
     * space = O(1)
     */
    public int numberCount(int a, int b) {
        int count = 0;
        for (int x = a; x <= b; x++) {
            if (hasUniqueDigits(x)) {
                count++;
            }
        }
        return count;
    }

    /** True if no decimal digit appears twice in x. */
    private boolean hasUniqueDigits(int x) {
        boolean[] seen = new boolean[10];
        while (x > 0) {
            int digit = x % 10;
            if (seen[digit]) {
                return false;
            }
            seen[digit] = true;
            x /= 10;
        }
        return true;
    }
}
