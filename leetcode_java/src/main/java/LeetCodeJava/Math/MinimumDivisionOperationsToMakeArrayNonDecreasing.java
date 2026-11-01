package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-division-operations-to-make-array-non-decreasing/description/

/**
 * 3326. Minimum Division Operations to Make Array Non Decreasing
 * Medium
 *
 * You are given an integer array nums.
 *
 * Any positive divisor of a natural number x that is strictly less than x is called a proper divisor
 * of x. For example, 2 is a proper divisor of 4, while 6 is not a proper divisor of 6.
 *
 * You are allowed to perform an operation any number of times on nums, where in each operation you
 * select any one element from nums and divide it by its greatest proper divisor.
 *
 * Return the minimum number of operations required to make the array non-decreasing.
 *
 * If it is not possible to make the array non-decreasing using any number of operations, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [25,7]
 * Output: 1
 * Explanation:
 * Using a single operation, 25 gets divided by 5 and nums becomes [5, 7].
 *
 * Example 2:
 *
 * Input: nums = [7,7,6]
 * Output: -1
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 *
 */
public class MinimumDivisionOperationsToMakeArrayNonDecreasing {

    // V0
    // IDEA: SMALLEST-PRIME-FACTOR SIEVE + GREEDY RIGHT-TO-LEFT
    //   x / (greatest proper divisor of x) == spf(x), and a prime cannot shrink further, so each
    //   element is either itself or spf(x). Scanning from the right, keep the largest value that fits.
    /**
     * time = O(V log log V + N)
     * space = O(V)
     */
    public int minOperations(int[] nums) {
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        // smallestPrimeFactor[x] for every x <= maxValue (x itself when x is prime or 1)
        int[] smallestPrimeFactor = new int[maxValue + 1];
        for (int x = 0; x <= maxValue; x++) {
            smallestPrimeFactor[x] = x;
        }
        for (int p = 2; (long) p * p <= maxValue; p++) {
            // p is prime when nothing smaller has marked it
            if (smallestPrimeFactor[p] == p) {
                for (int multiple = p * p; multiple <= maxValue; multiple += p) {
                    if (smallestPrimeFactor[multiple] == multiple) {
                        smallestPrimeFactor[multiple] = p;
                    }
                }
            }
        }

        int operations = 0;
        // value of the element to the right; the last element has no constraint
        int rightLimit = Integer.MAX_VALUE;
        for (int i = nums.length - 1; i >= 0; i--) {
            int value = nums[i];
            // keeping the original value leaves the most room for the elements further left
            if (value <= rightLimit) {
                rightLimit = value;
                continue;
            }
            // one operation drops it to its smallest prime factor; that is as low as it goes
            int reduced = smallestPrimeFactor[value];
            if (reduced > rightLimit) {
                return -1;
            }
            operations++;
            rightLimit = reduced;
        }
        return operations;
    }
}
