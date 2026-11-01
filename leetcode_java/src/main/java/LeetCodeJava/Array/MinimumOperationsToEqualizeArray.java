package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-operations-to-equalize-array/description/

/**
 * 3674. Minimum Operations to Equalize Array
 * Easy
 *
 * You are given an integer array nums of length n.
 *
 * In one operation, choose any subarray nums[l...r] (0 <= l <= r < n) and replace
 * each element in that subarray with the bitwise AND of all elements.
 *
 * Return the minimum number of operations required to make all elements of nums equal.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2]
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [5,5,5]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 100
 * 1 <= nums[i] <= 10^5
 *
 */
public class MinimumOperationsToEqualizeArray {

    // V0
    // IDEA: ONE WHOLE-ARRAY OPERATION ALWAYS SUFFICES
    //   applying the operation to the whole array makes every element the AND of
    //   all of them, so the answer is at most 1; it is 0 iff the array is already uniform.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations(int[] nums) {
        for (int value : nums) {
            if (value != nums[0]) {
                return 1;
            }
        }
        return 0;
    }
}
