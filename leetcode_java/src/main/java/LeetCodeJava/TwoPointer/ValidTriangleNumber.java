package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/valid-triangle-number/

import java.util.Arrays;

/**
 *  611. Valid Triangle Number
 *  Medium
 *
 *  Given an integer array nums, return the number of triplets chosen from the
 *  array that can make triangles if we take them as side lengths of a triangle.
 *
 *  Example 1:
 *    Input: nums = [2,2,3,4]
 *    Output: 3
 *    Explanation: Valid combinations are:
 *      2,3,4 (using the first 2)
 *      2,3,4 (using the second 2)
 *      2,2,3
 *
 *  Example 2:
 *    Input: nums = [4,2,3,4]
 *    Output: 4
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    0 <= nums[i] <= 1000
 */
public class ValidTriangleNumber {

    // V0
    // IDEA: SORT + 2 POINTERS
    //       after sorting, fix the LONGEST side k, then look for pairs (i, j),
    //       i < j < k with nums[i] + nums[j] > nums[k]
    //       (the other 2 triangle inequalities hold automatically).
    //       if nums[i] + nums[j] > nums[k], then every i' in [i, j) also works
    //       -> add (j - i) at once.
    /**
     * time = O(N^2)
     * space = O(1)   // ignoring the sort
     */
    public int triangleNumber(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }
        Arrays.sort(nums);

        int res = 0;
        for (int k = nums.length - 1; k >= 2; k--) {
            int i = 0;
            int j = k - 1;
            while (i < j) {
                if (nums[i] + nums[j] > nums[k]) {
                    res += (j - i);
                    j--;
                } else {
                    i++;
                }
            }
        }
        return res;
    }
}
