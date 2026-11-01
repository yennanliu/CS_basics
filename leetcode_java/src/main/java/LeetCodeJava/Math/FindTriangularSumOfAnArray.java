package LeetCodeJava.Math;

// https://leetcode.com/problems/find-triangular-sum-of-an-array/description/

/**
 * 2221. Find Triangular Sum of an Array
 * Medium
 *
 * You are given a 0-indexed integer array nums, where nums[i] is a digit between 0 and 9 (inclusive).
 *
 * The triangular sum of nums is the value of the only element present in nums after the following process terminates:
 *
 * 1. Let nums comprise of n elements. If n == 1, end the process. Otherwise, create a new 0-indexed integer array newNums of length n - 1.
 * 2. For each index i, where 0 <= i < n - 1, assign the value of newNums[i] as (nums[i] + nums[i+1]) % 10, where % denotes modulo operator.
 * 3. Replace the array nums with newNums.
 * 4. Repeat the entire process starting from step 1.
 *
 * Return the triangular sum of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 8
 * Explanation:
 * The above diagram depicts the process from which we obtain the triangular sum of the array.
 *
 * Example 2:
 *
 * Input: nums = [5]
 * Output: 5
 * Explanation:
 * Since there is only one element in nums, the triangular sum is the value of that element itself.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 9
 *
 */
public class FindTriangularSumOfAnArray {

    // V0
    // IDEA: IN-PLACE SIMULATION (PASCAL-TRIANGLE FOLDING)
    //   each round shrinks the array by one; folding from the left in place keeps memory O(1).
    //   the closed form sum(C(n-1, i) * nums[i]) mod 10 is awkward because 10 is not prime.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int triangularSum(int[] nums) {
        // work on a copy so the caller's array is untouched
        int[] values = nums.clone();

        // after the round that leaves `size` elements, only values[0 .. size-1] are live
        for (int size = values.length - 1; size >= 1; size--) {
            for (int i = 0; i < size; i++) {
                values[i] = (values[i] + values[i + 1]) % 10;
            }
        }
        return values[0];
    }
}
