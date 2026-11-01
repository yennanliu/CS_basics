package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/form-largest-integer-with-digits-that-add-up-to-target/description/

import java.util.Arrays;

/**
 * 1449. Form Largest Integer With Digits That Add up to Target
 * Hard
 *
 * Given an array of integers cost and an integer target, return the maximum integer
 * you can paint under the following rules:
 *
 * - The cost of painting a digit (i + 1) is given by cost[i] (0-indexed).
 * - The total cost used must be equal to target.
 * - The integer does not have 0 digits.
 *
 * Since the answer may be very large, return it as a string. If there is no way to
 * paint any integer given the condition, return "0".
 *
 *
 * Example 1:
 *
 * Input: cost = [4,3,2,5,6,7,2,5,5], target = 9
 * Output: "7772"
 * Explanation: The cost to paint the digit '7' is 2, and the digit '2' is 3.
 * Then cost("7772") = 2*3 + 3*1 = 9. You could also paint "977", but "7772" is the
 * largest number.
 * Digit    cost
 *   1  ->   4
 *   2  ->   3
 *   3  ->   2
 *   4  ->   5
 *   5  ->   6
 *   6  ->   7
 *   7  ->   2
 *   8  ->   5
 *   9  ->   5
 *
 * Example 2:
 *
 * Input: cost = [7,6,5,5,5,6,8,7,8], target = 12
 * Output: "85"
 * Explanation: The cost to paint the digit '8' is 7, and the digit '5' is 5.
 * Then cost("85") = 7 + 5 = 12.
 *
 * Example 3:
 *
 * Input: cost = [2,4,6,2,4,6,4,4,4], target = 5
 * Output: "0"
 * Explanation: It is impossible to paint any integer with total cost equal to target.
 *
 *
 * Constraints:
 *
 * cost.length == 9
 * 1 <= cost[i], target <= 5000
 *
 */
public class FormLargestIntegerWithDigitsThatAddUpToTarget {

    // V0
    // IDEA: UNBOUNDED KNAPSACK (MAXIMISE DIGIT COUNT) + GREEDY RECONSTRUCTION
    //       step 1) dp[t] = max number of digits whose costs sum to EXACTLY t (-1 = unreachable)
    //               dp[t] = max over d of dp[t - cost[d-1]] + 1
    //       step 2) a longer number is always larger, so rebuild greedily: at each position
    //               take the LARGEST digit d that stays on an optimal path,
    //               i.e. dp[t - cost[d-1]] == dp[t] - 1
    /**
     * time = O(9 * T)
     * space = O(T)
     */
    public String largestNumber(int[] cost, int target) {
        // dp[t] = max digits painted with total cost exactly t; -1 means impossible
        int[] dp = new int[target + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        for (int t = 1; t <= target; t++) {
            for (int digit = 1; digit <= 9; digit++) {
                int digitCost = cost[digit - 1];
                if (t >= digitCost && dp[t - digitCost] >= 0) {
                    dp[t] = Math.max(dp[t], dp[t - digitCost] + 1);
                }
            }
        }

        // no way to spend exactly target
        if (dp[target] <= 0) {
            return "0";
        }

        // greedy rebuild: prefer the biggest digit that keeps the remaining budget optimal
        StringBuilder result = new StringBuilder();
        int remaining = target;
        while (remaining > 0) {
            for (int digit = 9; digit >= 1; digit--) {
                int digitCost = cost[digit - 1];
                if (remaining >= digitCost && dp[remaining - digitCost] == dp[remaining] - 1) {
                    result.append(digit);
                    remaining -= digitCost;
                    break;
                }
            }
        }
        return result.toString();
    }
}
