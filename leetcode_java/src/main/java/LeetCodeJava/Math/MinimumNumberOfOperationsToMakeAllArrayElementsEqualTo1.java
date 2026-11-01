package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-all-array-elements-equal-to-1/description/

/**
 * 2654. Minimum Number of Operations to Make All Array Elements Equal to 1
 * Medium
 *
 * You are given a 0-indexed array nums consisting of positive integers. You can do the following operation on the array any number of times:
 *
 * Select an index i such that 0 <= i < n - 1 and replace either of nums[i] or nums[i+1] with their gcd value.
 *
 * Return the minimum number of operations to make all elements of nums equal to 1. If it is impossible, return -1.
 *
 * The gcd of two integers is the greatest common divisor of the two integers.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,6,3,4]
 * Output: 4
 * Explanation: We can do the following operations:
 * - Choose index i = 2 and replace nums[2] with gcd(3,4) = 1. Now we have nums = [2,6,1,4].
 * - Choose index i = 1 and replace nums[1] with gcd(6,1) = 1. Now we have nums = [2,1,1,4].
 * - Choose index i = 0 and replace nums[0] with gcd(2,1) = 1. Now we have nums = [1,1,1,4].
 * - Choose index i = 2 and replace nums[3] with gcd(1,4) = 1. Now we have nums = [1,1,1,1].
 *
 * Example 2:
 *
 * Input: nums = [2,10,6,14]
 * Output: -1
 * Explanation: It can be shown that it is impossible to make all the elements equal to 1.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 50
 * 1 <= nums[i] <= 10^6
 *
 */
public class MinimumNumberOfOperationsToMakeAllArrayElementsEqualTo1 {

    // V0
    // IDEA: GCD + SHORTEST SUBARRAY WHOSE GCD IS 1
    //       once one 1 exists, each other cell becomes 1 in one operation (gcd(1, x) = 1).
    //       1) c ones already present -> n - c
    //       2) gcd of the whole array != 1 -> impossible -> -1
    //       3) else the shortest subarray with gcd 1 has length L: L - 1 operations make a
    //          1, then n - 1 more spread it -> (L - 1) + (n - 1)
    /**
     * time = O(N^2 * log(max(nums)))
     * space = O(1)
     */
    public int minOperations(int[] nums) {
        int n = nums.length;

        // case 1: existing ones only need spreading (must be checked first)
        int ones = 0;
        for (int value : nums) {
            if (value == 1) {
                ones++;
            }
        }
        if (ones > 0) {
            return n - ones;
        }

        // case 2: every reachable value stays a multiple of the overall gcd
        int overallGcd = 0;
        for (int value : nums) {
            overallGcd = gcd(overallGcd, value);
        }
        if (overallGcd != 1) {
            return -1;
        }

        // case 3: shortest subarray with gcd 1; bestSpan = its length - 1
        int bestSpan = n;
        for (int i = 0; i < n; i++) {
            int runningGcd = nums[i];
            for (int j = i + 1; j < n; j++) {
                runningGcd = gcd(runningGcd, nums[j]);
                // the running gcd only shrinks, so the first 1 is the shortest from i
                if (runningGcd == 1) {
                    bestSpan = Math.min(bestSpan, j - i);
                    break;
                }
            }
        }
        return bestSpan + n - 1;
    }

    /** Euclid's greatest common divisor; gcd(0, x) == x. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
