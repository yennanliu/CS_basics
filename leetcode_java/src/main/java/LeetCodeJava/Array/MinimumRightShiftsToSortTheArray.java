package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-right-shifts-to-sort-the-array/description/

import java.util.*;

/**
 * 2855. Minimum Right Shifts to Sort the Array
 * Easy
 *
 * You are given a 0-indexed array nums of length n containing distinct positive integers. Return the minimum number of right shifts required to sort nums and -1 if this is not possible.
 *
 * A right shift is defined as shifting the element at index i to index (i + 1) % n, for all indices.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,5,1,2]
 * Output: 2
 * Explanation:
 * After the first right shift, nums = [2,3,4,5,1].
 * After the second right shift, nums = [1,2,3,4,5].
 * Now nums is sorted; therefore the answer is 2.
 *
 * Example 2:
 *
 * Input: nums = [1,3,5]
 * Output: 0
 * Explanation: nums is already sorted therefore, the answer is 0.
 *
 * Example 3:
 *
 * Input: nums = [2,1,4]
 * Output: -1
 * Explanation: It's impossible to sort the array using right shifts.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 * nums contains distinct integers.
 *
 */
public class MinimumRightShiftsToSortTheArray {

    // V0
    // IDEA: COUNT THE "DROPS" (rotated sorted array detection)
    //       right shifts rotate the array, so it can be sorted iff it is a rotation of its
    //       sorted order: walking CYCLICALLY there is at most one drop nums[i] > nums[i + 1].
    //       one drop at i -> the sorted run starts at i + 1, which takes n - 1 - i shifts.
    //       the wrap-around pair (last, first) must be included; a sorted array has its only
    //       drop there, giving 0 shifts.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumRightShifts(List<Integer> nums) {
        int n = nums.size();
        int drops = 0;
        int dropIndex = -1;
        for (int i = 0; i < n; i++) {
            int current = nums.get(i);
            int next = nums.get((i + 1) % n);
            if (current > next) {
                drops++;
                dropIndex = i;
            }
        }

        // a single element has no drop at all
        if (drops == 0) {
            return 0;
        }
        if (drops > 1) {
            return -1;
        }
        return n - 1 - dropIndex;
    }
}
