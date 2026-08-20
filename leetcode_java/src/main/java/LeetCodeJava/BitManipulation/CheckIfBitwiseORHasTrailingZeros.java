package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/check-if-bitwise-or-has-trailing-zeros/

/**
 *  2980. Check if Bitwise OR Has Trailing Zeros
 *  Easy
 *
 *  You are given an array of positive integers nums.
 *
 *  You have to check if it is possible to select two or more elements in the
 *  array such that the bitwise OR of the selected elements has at least one
 *  trailing zero in its binary representation.
 *
 *  For example, the binary representation of 5, which is "101", does not have
 *  any trailing zeros, whereas the binary representation of 4, which is "100",
 *  has two trailing zeros.
 *
 *  Return true if it is possible to select two or more elements whose bitwise
 *  OR has trailing zeros, return false otherwise.
 *
 *  Example 1:
 *    Input: nums = [1,2,3,4,5]
 *    Output: true
 *    Explanation: selecting 2 and 4 gives 6 = "110", one trailing zero.
 *
 *  Example 2:
 *    Input: nums = [2,4,8,16]
 *    Output: true
 *
 *  Example 3:
 *    Input: nums = [1,3,5,7,9]
 *    Output: false
 *    Explanation: there is no way to pick 2+ elements whose OR ends in 0.
 *
 *  Constraints:
 *    2 <= nums.length <= 100
 *    1 <= nums[i] <= 100
 */
public class CheckIfBitwiseORHasTrailingZeros {

    // V0
    // IDEA: COUNT THE EVEN NUMBERS
    //       an OR has a trailing zero <=> its lowest bit is 0 <=> EVERY selected
    //       element has lowest bit 0 (OR turns bits on, never off).
    //       so the whole question collapses to "are there >= 2 even numbers?"
    //       picking exactly two evens is always enough - no subset enumeration.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasTrailingZeros(int[] nums) {
        int even = 0;
        for (int x : nums) {
            if ((x & 1) == 0) {
                even++;
                if (even >= 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
