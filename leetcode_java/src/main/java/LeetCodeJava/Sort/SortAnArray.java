package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-an-array/description/

import java.util.Arrays;
import java.util.Comparator;

/**
 * 912. Sort an Array
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of integers nums, sort the array in ascending order and return it.
 *
 * You must solve the problem without using any built-in functions in O(nlog(n)) time complexity and with the smallest space complexity possible.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [5,2,3,1]
 * Output: [1,2,3,5]
 * Explanation: After sorting the array, the positions of some numbers are not changed (for example, 2 and 3), while the positions of other numbers are changed (for example, 1 and 5).
 * Example 2:
 *
 * Input: nums = [5,1,1,2,0,0]
 * Output: [0,0,1,1,2,5]
 * Explanation: Note that the values of nums are not necessairly unique.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * -5 * 104 <= nums[i] <= 5 * 104
 *
 *
 */
public class SortAnArray {

    // V0
    // IDEA: int -> Integer, Comparator sorting
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] sortArray(int[] nums) {

        Integer[] nums_ = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums_[i] = nums[i];
        }

        // NOTE !!! below
        /**
         * NOTE !!!
         *
         *
         * In Java, you cannot directly use a Comparator with primitive
         * types like int[] because Comparator works only with objects,
         * not primitives. However, you can convert the int[] to an Integer[]
         * and then use a comparator.
         *
         * Here's an example of how to sort an int[] using a
         * Comparator by converting it to Integer[]:
         *
         */
        Arrays.sort(nums_, new Comparator<Integer>() {
            @Override
            /**
             * time = O(N)
             * space = O(N)
             */
            public int compare(Integer a, Integer b) {
                return a - b; // This will sort in descending order
            }
        });

        for (int i = 0; i < nums_.length; i++) {
            nums[i] = nums_[i]; // ???
        }

        return nums;
    }

    // V1

    // V2
}
