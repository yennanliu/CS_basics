package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-target-indices-after-sorting-array/

import java.util.ArrayList;
import java.util.List;

/**
 *  2089. Find Target Indices After Sorting Array
 *  Easy
 *
 *  You are given a 0-indexed integer array nums and a target element target.
 *
 *  A target index is an index i such that nums[i] == target.
 *
 *  Return a list of the target indices of nums after sorting nums in
 *  non-decreasing order. If there are no target indices, return an empty list.
 *  The returned list must be sorted in increasing order.
 *
 *  Example 1:
 *    Input: nums = [1,2,5,2,3], target = 2
 *    Output: [1,2]
 *    Explanation: after sorting nums is [1,2,2,3,5]; nums[i] == 2 at i = 1, 2.
 *
 *  Example 2:
 *    Input: nums = [1,2,5,2,3], target = 5
 *    Output: [4]
 *    Explanation: after sorting nums is [1,2,2,3,5]; nums[i] == 5 at i = 4.
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i], target <= 100
 */
public class FindTargetIndicesAfterSortingArray {

    // V0
    // IDEA: COUNT, DON'T SORT - THE POSITIONS FOLLOW FROM TWO TALLIES
    //       after sorting, all values < target come first. so with
    //         smaller = #{x : x < target}
    //         equal   = #{x : x == target}
    //       the target indices are exactly smaller, smaller+1, ...,
    //       smaller + equal - 1. this is O(n) and skips the sort entirely; the
    //       empty range falls out naturally when equal == 0.
    /**
     * time = O(n)
     * space = O(1)   // beyond the output
     */
    public List<Integer> targetIndices(int[] nums, int target) {
        int smaller = 0;
        int equal = 0;
        for (int x : nums) {
            if (x < target) {
                smaller++;
            } else if (x == target) {
                equal++;
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < equal; i++) {
            res.add(smaller + i);
        }
        return res;
    }
}
