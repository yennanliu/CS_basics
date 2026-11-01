package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-array-concatenation-value/description/

/**
 * 2562. Find the Array Concatenation Value
 * Easy
 *
 * You are given a 0-indexed integer array nums.
 *
 * The concatenation of two numbers is the number formed by concatenating their numerals.
 *
 * - For example, the concatenation of 15, 49 is 1549.
 *
 * The concatenation value of nums is initially equal to 0. Perform this operation until nums becomes empty:
 *
 * - If nums has a size greater than one, add the value of the concatenation of the first and the last element to the concatenation value of nums, and remove those two elements from nums.
 * - If only one element exists in nums, add its value to the concatenation value of nums, then remove it.
 *
 * Return the concatenation value of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [7,52,2,4]
 * Output: 596
 *
 * Example 2:
 *
 * Input: nums = [5,14,13,8,12]
 * Output: 673
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 10^4
 *
 */
public class FindTheArrayConcatenationValue {

    // V0
    // IDEA: TWO POINTERS + DIGIT SHIFT -- concat(a, b) = a * 10^(digits of b) + b
    /**
     * time = O(N log M)
     * space = O(1)
     */
    public long findTheArrayConcVal(int[] nums) {
        long total = 0;
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            // shift = 10^(number of digits in nums[right])
            long shift = 1;
            int remaining = nums[right];
            while (remaining > 0) {
                shift *= 10;
                remaining /= 10;
            }
            total += nums[left] * shift + nums[right];
            left++;
            right--;
        }
        // odd length: the middle element is added on its own
        if (left == right) {
            total += nums[left];
        }
        return total;
    }
}
