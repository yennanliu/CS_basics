package LeetCodeJava.HashTable;

// https://leetcode.com/problems/max-pair-sum-in-an-array/description/

/**
 * 2815. Max Pair Sum in an Array
 * Easy
 *
 * You are given an integer array nums. You have to find the maximum sum of a pair of numbers from nums such that the largest digit in both numbers is equal.
 *
 * For example, 2373 is made up of three distinct digits: 2, 3, and 7, where 7 is the largest among them.
 *
 * Return the maximum sum or -1 if no such pair exists.
 *
 *
 * Example 1:
 *
 * Input: nums = [112,131,411]
 *
 * Output: -1
 *
 * Explanation:
 *
 * Each numbers largest digit in order is [2,3,4].
 *
 * Example 2:
 *
 * Input: nums = [2536,1613,3366,162]
 *
 * Output: 5902
 *
 * Explanation:
 *
 * All the numbers have 6 as their largest digit, so the answer is 2536 + 3366 = 5902.
 *
 * Example 3:
 *
 * Input: nums = [51,71,17,24,42]
 *
 * Output: 88
 *
 * Explanation:
 *
 * Each number's largest digit in order is [5,7,7,4,4].
 *
 * So we have only two possible pairs, 71 + 17 = 88 and 24 + 42 = 66.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 10^4
 *
 */
public class MaxPairSumInAnArray {

    // V0
    // IDEA: BUCKET BY LARGEST DIGIT, KEEP THE BEST NUMBER SEEN PER BUCKET (one pass)
    /**
     * time = O(N * log10(M))
     * space = O(1)
     */
    public int maxSum(int[] nums) {
        // bestInBucket[d] = largest number seen so far whose max digit is d (0 = none)
        int[] bestInBucket = new int[10];
        int answer = -1;
        for (int num : nums) {
            int digit = largestDigit(num);
            // pair the current number with the best earlier partner in its bucket
            if (bestInBucket[digit] > 0) {
                answer = Math.max(answer, bestInBucket[digit] + num);
            }
            bestInBucket[digit] = Math.max(bestInBucket[digit], num);
        }
        return answer;
    }

    /** Largest decimal digit of x. */
    private int largestDigit(int x) {
        int largest = 0;
        while (x > 0) {
            largest = Math.max(largest, x % 10);
            x /= 10;
        }
        return largest;
    }
}
