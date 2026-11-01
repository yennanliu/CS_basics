package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-average-difference/description/

/**
 * 2256. Minimum Average Difference
 * Medium
 *
 * You are given a 0-indexed integer array nums of length n.
 *
 * The average difference of the index i is the absolute difference between the average of the first i + 1 elements of nums and the average of the last n - i - 1 elements. Both averages should be rounded down to the nearest integer.
 *
 * Return the index with the minimum average difference. If there are multiple such indices, return the smallest one.
 *
 * Note:
 *
 * The absolute difference of two numbers is the absolute value of their difference.
 * The average of n elements is the sum of the elements divided by n.
 * The average of 0 elements is considered to be 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,3,9,5,3]
 * Output: 3
 * Explanation:
 * - The average difference of index 0 is: |2 / 1 - (5 + 3 + 9 + 5 + 3) / 5| = |2 / 1 - 25 / 5| = |2 - 5| = 3.
 * - The average difference of index 1 is: |(2 + 5) / 2 - (3 + 9 + 5 + 3) / 4| = |7 / 2 - 20 / 4| = |3 - 5| = 2.
 * - The average difference of index 2 is: |(2 + 5 + 3) / 3 - (9 + 5 + 3) / 3| = |10 / 3 - 17 / 3| = |3 - 5| = 2.
 * - The average difference of index 3 is: |(2 + 5 + 3 + 9) / 4 - (5 + 3) / 2| = |19 / 4 - 8 / 2| = |4 - 4| = 0.
 * - The average difference of index 4 is: |(2 + 5 + 3 + 9 + 5) / 5 - 3 / 1| = |24 / 5 - 3 / 1| = |4 - 3| = 1.
 * - The average difference of index 5 is: |(2 + 5 + 3 + 9 + 5 + 3) / 6 - 0| = |27 / 6 - 0| = |4 - 0| = 4.
 * The average difference of index 3 is the minimum average difference so return 3.
 *
 * Example 2:
 *
 * Input: nums = [0]
 * Output: 0
 * Explanation:
 * The only index is 0 so return 0.
 * The average difference of index 0 is: |0 / 1 - 0| = |0 - 0| = 0.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 0 <= nums[i] <= 10^5
 *
 */
public class MinimumAverageDifference {

    // V0
    // IDEA: RUNNING PREFIX SUM, THE SUFFIX FOLLOWS FROM THE TOTAL
    /**
     *  Both averages are floored BEFORE subtracting; the last index has an empty
     *  suffix whose average is defined as 0 (guard the division). Ties go to the
     *  smallest index, so only a strict improvement updates the answer.
     *  Sums reach 10^5 * 10^5 = 10^10, so they are long.
     *
     * time = O(N)
     * space = O(1)
     */
    public int minimumAverageDifference(int[] nums) {
        int n = nums.length;
        long total = 0;
        for (int num : nums) {
            total += num;
        }

        long prefix = 0;
        long bestDiff = Long.MAX_VALUE;
        int bestIndex = 0;
        for (int i = 0; i < n; i++) {
            prefix += nums[i];
            long leftAverage = prefix / (i + 1);
            int rightCount = n - i - 1;
            long rightAverage = rightCount == 0 ? 0 : (total - prefix) / rightCount;
            long diff = Math.abs(leftAverage - rightAverage);
            if (diff < bestDiff) {
                bestDiff = diff;
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}
