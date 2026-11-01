package LeetCodeJava.String;

// https://leetcode.com/problems/count-symmetric-integers/description/

/**
 * 2843. Count Symmetric Integers
 * Easy
 *
 * You are given two positive integers low and high.
 *
 * An integer x consisting of 2 * n digits is symmetric if the sum of the first n digits of x is equal to the sum of the last n digits of x. Numbers with an odd number of digits are never symmetric.
 *
 * Return the number of symmetric integers in the range [low, high].
 *
 *
 * Example 1:
 *
 * Input: low = 1, high = 100
 * Output: 9
 * Explanation: There are 9 symmetric integers between 1 and 100: 11, 22, 33, 44, 55, 66, 77, 88, and 99.
 *
 * Example 2:
 *
 * Input: low = 1200, high = 1230
 * Output: 4
 * Explanation: There are 4 symmetric integers between 1200 and 1230: 1203, 1212, 1221, and 1230.
 *
 *
 * Constraints:
 *
 * 1 <= low <= high <= 10^4
 *
 */
public class CountSymmetricIntegers {

    // V0
    // IDEA: BRUTE FORCE ENUMERATION OVER [low, high]
    //       high <= 1e4, so test each number: odd digit count is never
    //       symmetric; otherwise compare the digit sums of the two halves
    /**
     * time = O((high - low) * log(high))
     * space = O(log(high))
     */
    public int countSymmetricIntegers(int low, int high) {
        int count = 0;
        for (int x = low; x <= high; x++) {
            String digits = String.valueOf(x);
            int length = digits.length();
            if (length % 2 == 1) {
                continue;
            }
            int half = length / 2;
            int leftSum = 0;
            int rightSum = 0;
            for (int i = 0; i < half; i++) {
                leftSum += digits.charAt(i) - '0';
                rightSum += digits.charAt(half + i) - '0';
            }
            if (leftSum == rightSum) {
                count++;
            }
        }
        return count;
    }
}
