package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-floored-pairs/description/

/**
 * 1862. Sum of Floored Pairs
 * Hard
 *
 * Given an integer array nums, return the sum of floor(nums[i] / nums[j]) for all pairs of indices 0 <= i, j < nums.length in the array. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * The floor() function returns the integer part of the division.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,5,9]
 * Output: 10
 * Explanation:
 * floor(2 / 5) = floor(2 / 9) = floor(5 / 9) = 0
 * floor(2 / 2) = floor(5 / 5) = floor(9 / 9) = 1
 * floor(5 / 2) = 2
 * floor(9 / 2) = 4
 * floor(9 / 5) = 1
 * We calculate the floor of the division for every pair of indices in the array then sum them up.
 *
 * Example 2:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 49
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class SumOfFlooredPairs {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COUNTING + PREFIX SUM OVER MULTIPLES (harmonic series)
    //  for divisor y, floor(x / y) == d exactly when x is in [d*y, d*y + y - 1];
    //  a prefix count answers "how many x in that block" in O(1).
    /**
     * time = O(N + M log M), M = max(nums)
     * space = O(M)
     */
    public int sumOfFlooredPairs(int[] nums) {
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }

        // count[v] = how many times v appears
        int[] count = new int[maxValue + 1];
        for (int num : nums) {
            count[num]++;
        }

        // prefix[v] = how many numbers are <= v
        int[] prefix = new int[maxValue + 1];
        for (int v = 1; v <= maxValue; v++) {
            prefix[v] = prefix[v - 1] + count[v];
        }

        long total = 0;
        for (int divisor = 1; divisor <= maxValue; divisor++) {
            if (count[divisor] == 0) {
                continue;
            }
            // walk the blocks [d*y, d*y + y - 1]: mx/y blocks -> harmonic sum overall
            for (long quotient = 1; quotient * divisor <= maxValue; quotient++) {
                int low = (int) (quotient * divisor);
                int high = Math.min(maxValue, low + divisor - 1);
                long numbersInBlock = prefix[high] - prefix[low - 1];
                total = (total + count[divisor] * quotient % MOD * numbersInBlock) % MOD;
            }
        }
        return (int) total;
    }
}
