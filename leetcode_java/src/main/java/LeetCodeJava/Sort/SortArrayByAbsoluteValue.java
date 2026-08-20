package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-array-by-absolute-value/

import java.util.Arrays;

/**
 *  3667. Sort Array By Absolute Value
 *  Easy
 *
 *  You are given an integer array nums.
 *
 *  Rearrange elements of nums in non-decreasing order of their absolute value.
 *
 *  Return any rearranged array that satisfies this condition.
 *
 *  Note: The absolute value of an integer x is defined as:
 *    x  if x >= 0
 *    -x if x < 0
 *
 *  Example 1:
 *    Input: nums = [3,-1,-4,1,5]
 *    Output: [-1,1,3,-4,5]
 *    Explanation: the absolute values are 3, 1, 4, 1, 5; sorted -> 1,1,3,4,5.
 *                 [1,-1,3,-4,5] is also accepted.
 *
 *  Example 2:
 *    Input: nums = [-100,100]
 *    Output: [-100,100]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    -100 <= nums[i] <= 100
 */
public class SortArrayByAbsoluteValue {

    // V0
    // IDEA: SORT ON A DERIVED KEY (|x|)
    //       the ordering constraint is stated purely in terms of |x|, so |x| is
    //       exactly the sort key and the sign never enters the comparison —
    //       which is why the problem says "return any" (ties such as -1 / 1 may
    //       come out in either order).
    //       we must sort BY the key, not replace the values: the output has to
    //       carry the original signed numbers, only reordered. Hence boxing to
    //       Integer[] so a comparator can be supplied.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] sortByAbsoluteValue(int[] nums) {
        Integer[] boxed = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            boxed[i] = nums[i];
        }

        Arrays.sort(boxed, (a, b) -> Integer.compare(Math.abs(a), Math.abs(b)));

        int[] res = new int[nums.length];
        for (int i = 0; i < boxed.length; i++) {
            res[i] = boxed[i];
        }
        return res;
    }
}
