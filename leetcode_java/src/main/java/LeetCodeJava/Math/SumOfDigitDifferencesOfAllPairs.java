package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-digit-differences-of-all-pairs/description/

/**
 * 3153. Sum of Digit Differences of All Pairs
 * Medium
 *
 * You are given an array nums consisting of positive integers where all integers have the same number
 * of digits.
 *
 * The digit difference between two integers is the count of different digits that are in the same
 * position in the two integers.
 *
 * Return the sum of the digit differences between all pairs of integers in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [13,23,12]
 * Output: 4
 * Explanation:
 * We have the following:
 * - The digit difference between 13 and 23 is 1.
 * - The digit difference between 13 and 12 is 1.
 * - The digit difference between 23 and 12 is 2.
 * So the total sum of digit differences between all pairs of integers is 1 + 1 + 2 = 4.
 *
 * Example 2:
 *
 * Input: nums = [10,10,10,10]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] < 10^9
 * All integers in nums have the same number of digits.
 *
 */
public class SumOfDigitDifferencesOfAllPairs {

    // V0
    // IDEA: PER DIGIT POSITION, DIFFERING PAIRS = C(n, 2) - SUM OVER d OF C(count[d], 2)
    /**
     * time = O(N * D)
     * space = O(N)
     */
    public long sumDigitDifferences(int[] nums) {
        int n = nums.length;
        long totalPairs = (long) n * (n - 1) / 2;

        // working copy: each pass peels off the last digit
        int[] remaining = nums.clone();
        long result = 0;

        // all numbers share a digit count, so they run out together
        while (remaining[0] > 0) {
            long[] digitCount = new long[10];
            for (int i = 0; i < n; i++) {
                digitCount[remaining[i] % 10]++;
                remaining[i] /= 10;
            }
            // pairs that agree at this position
            long samePairs = 0;
            for (long count : digitCount) {
                samePairs += count * (count - 1) / 2;
            }
            result += totalPairs - samePairs;
        }
        return result;
    }
}
