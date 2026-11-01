package LeetCodeJava.HashTable;

// https://leetcode.com/problems/smallest-missing-non-negative-integer-after-operations/description/

/**
 * 2598. Smallest Missing Non-negative Integer After Operations
 * Medium
 *
 * You are given a 0-indexed integer array nums and an integer value.
 *
 * In one operation, you can add or subtract value from any element of nums.
 *
 * For example, if nums = [1,2,3] and value = 2, you can choose to subtract value from nums[0] to make nums = [-1,2,3].
 *
 * The MEX (minimum excluded) of an array is the smallest missing non-negative integer in it.
 *
 * For example, the MEX of [-1,2,3] is 0 while the MEX of [1,0,3] is 2.
 *
 * Return the maximum MEX of nums after applying the mentioned operation any number of times.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-10,7,13,6,8], value = 5
 * Output: 4
 * Explanation: One can achieve this result by applying the following operations:
 * - Add value to nums[1] twice to make nums = [1,0,7,13,6,8]
 * - Subtract value from nums[2] once to make nums = [1,0,2,13,6,8]
 * - Subtract value from nums[3] twice to make nums = [1,0,2,3,6,8]
 * The MEX of nums is 4. It can be shown that 4 is the maximum MEX we can achieve.
 *
 * Example 2:
 *
 * Input: nums = [1,-10,7,13,6,8], value = 7
 * Output: 2
 * Explanation: One can achieve this result by applying the following operation:
 * - subtract value from nums[2] once to make nums = [1,-10,0,13,6,8]
 * The MEX of nums is 2. It can be shown that 2 is the maximum MEX we can achieve.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length, value <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class SmallestMissingNonNegativeIntegerAfterOperations {

    // V0
    // IDEA: COUNT REMAINDERS mod value, THEN GREEDILY COVER 0, 1, 2, ...
    /**
     * time = O(N)
     * space = O(value)
     */
    public int findSmallestInteger(int[] nums, int value) {
        // adding / subtracting value keeps x mod value, and reaches every
        // non-negative number with that remainder
        int[] bucket = new int[value];
        for (int num : nums) {
            // Java's % can be negative; shift it into [0, value)
            int remainder = ((num % value) + value) % value;
            bucket[remainder]++;
        }

        // the MEX is at most n, so the loop is bounded
        for (int target = 0; target <= nums.length; target++) {
            int remainder = target % value;
            // nothing left that can become `target`: it is the first hole
            if (bucket[remainder] == 0) {
                return target;
            }
            bucket[remainder]--;
        }
        return nums.length;
    }
}
