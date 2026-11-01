package LeetCodeJava.Array;

// https://leetcode.com/problems/check-if-array-is-sorted-and-rotated/description/
/**
 * 1752. Check if Array Is Sorted and Rotated
 * Easy
 *
 * Given an array nums, return true if the array was originally sorted in non-decreasing order, then
 * rotated some number of positions (including zero). Otherwise, return false.
 *
 * There may be duplicates in the original array.
 *
 * Note: An array A rotated by x positions results in an array B of the same length such that
 * B[i] == A[(i+x) % A.length] for every valid index i.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,5,1,2]
 * Output: true
 * Explanation: [1,2,3,4,5] is the original sorted array.
 * You can rotate the array by x = 2 positions to begin on the element of value 3: [3,4,5,1,2].
 *
 * Example 2:
 *
 * Input: nums = [2,1,3,4]
 * Output: false
 * Explanation: There is no sorted array once rotated that can make nums.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: true
 * Explanation: [1,2,3] is the original sorted array.
 * You can rotate the array by x = 0 positions (i.e. no rotation) to make nums.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class CheckIfArrayIsSortedAndRotated {

    // V0
    // IDEA: COUNT THE "DROPS" ON THE CIRCULAR ARRAY (a rotated sorted array has at most one)
    // a sorted array has no index with nums[i - 1] > nums[i]; rotating it adds exactly one, at the
    // rotation point. The wrap-around pair (last, first) must be counted too.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean check(int[] nums) {
        int n = nums.length;
        int drops = 0;
        for (int i = 0; i < n; i++) {
            // (i + n - 1) % n is the previous index, wrapping from 0 to n - 1
            int previous = nums[(i + n - 1) % n];
            if (previous > nums[i]) {
                drops++;
            }
        }
        return drops <= 1;
    }
}
