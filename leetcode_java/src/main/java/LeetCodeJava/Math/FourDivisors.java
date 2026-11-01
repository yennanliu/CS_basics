package LeetCodeJava.Math;

// https://leetcode.com/problems/four-divisors/description/

/**
 * 1390. Four Divisors
 * Medium
 *
 * Given an integer array nums, return the sum of divisors of the integers in that array that have
 * exactly four divisors. If there is no such integer in the array, return 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [21,4,7]
 * Output: 32
 * Explanation:
 * 21 has 4 divisors: 1, 3, 7, 21
 * 4 has 3 divisors: 1, 2, 4
 * 7 has 2 divisors: 1, 7
 * The answer is the sum of divisors of 21 only.
 *
 * Example 2:
 *
 * Input: nums = [21,21]
 * Output: 64
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * 1 <= nums[i] <= 10^5
 *
 */
public class FourDivisors {

    // V0
    // IDEA: TRIAL DIVISION UP TO sqrt(x), STOP AS SOON AS A 5TH DIVISOR APPEARS
    //   divisors come in pairs (d, x / d); start with 1 and x already counted.
    //   when d * d == x the pair is one divisor - count it once.
    /**
     * time = O(N * sqrt(M))
     * space = O(1)
     */
    public int sumFourDivisors(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += divisorSumIfFour(num);
        }
        return total;
    }

    /** Sum of x's divisors if x has exactly four of them, else 0. */
    private int divisorSumIfFour(int x) {
        // 1 and x itself (for x = 1 this over-counts, but 1 never reaches count 4)
        int count = 2;
        int sum = x + 1;
        for (int d = 2; d * d <= x; d++) {
            if (x % d != 0) {
                continue;
            }
            count++;
            sum += d;
            if (d * d != x) {
                count++;
                sum += x / d;
            }
            // more than four divisors: x contributes nothing, stop early
            if (count > 4) {
                return 0;
            }
        }
        if (count == 4) {
            return sum;
        }
        return 0;
    }
}
