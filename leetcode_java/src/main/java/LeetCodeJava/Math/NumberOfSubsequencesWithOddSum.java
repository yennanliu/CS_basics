package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-subsequences-with-odd-sum/description/

/**
 * 3247. Number of Subsequences with Odd Sum
 * Medium
 *
 * Given an array nums, return the number of subsequences with an odd sum.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1]
 * Output: 4
 * Explanation:
 * The odd-sum subsequences are: [1, 1, 1], [1, 1, 1], [1, 1, 1], [1, 1, 1].
 *
 * Example 2:
 *
 * Input: nums = [1,2,2]
 * Output: 4
 * Explanation:
 * The odd-sum subsequences are: [1, 2, 2], [1, 2, 2], [1, 2, 2], [1, 2, 2].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class NumberOfSubsequencesWithOddSum {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PARITY COUNTING — EVENS ARE FREE (2^even), AN ODD-SIZED PICK OF THE ODDS: 2^(odd - 1)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int subsequenceCount(int[] nums) {
        int oddCount = 0;
        for (int value : nums) {
            if (value % 2 == 1) {
                oddCount++;
            }
        }
        // no odd element: every sum is even
        if (oddCount == 0) {
            return 0;
        }
        int evenCount = nums.length - oddCount;
        // half of the 2^odd subsets of the odd elements have odd size
        long result = modPow(2, oddCount - 1) * modPow(2, evenCount) % MOD;
        return (int) result;
    }

    // V1
    // IDEA: DP OVER PARITY — evenWays / oddWays = NON-EMPTY SUBSEQUENCES ENDING IN EACH PARITY SO FAR
    /**
     * time = O(N)
     * space = O(1)
     */
    public int subsequenceCount_1(int[] nums) {
        // number of non-empty subsequences (of the prefix) whose sum is even / odd
        long evenWays = 0;
        long oddWays = 0;
        for (int value : nums) {
            long newEven;
            long newOdd;
            if (value % 2 == 0) {
                // append an even value: parity unchanged, plus the singleton {value} (even)
                newEven = evenWays + evenWays + 1;
                newOdd = oddWays + oddWays;
            } else {
                // append an odd value: parity flips, plus the singleton {value} (odd)
                newEven = evenWays + oddWays;
                newOdd = oddWays + evenWays + 1;
            }
            evenWays = newEven % MOD;
            oddWays = newOdd % MOD;
        }
        return (int) oddWays;
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, int exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
