package LeetCodeJava.Array;

// https://leetcode.com/problems/convert-an-array-into-a-2d-array-with-conditions/description/

import java.util.*;

/**
 * 2610. Convert an Array Into a 2D Array With Conditions
 * Medium
 *
 * You are given an integer array nums. You need to create a 2D array from nums satisfying the following conditions:
 *
 * The 2D array should contain only the elements of the array nums.
 * Each row in the 2D array contains distinct integers.
 * The number of rows in the 2D array should be minimal.
 *
 * Return the resulting array. If there are multiple answers, return any of them.
 *
 * Note that the 2D array can have a different number of elements on each row.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,4,1,2,3,1]
 * Output: [[1,3,4,2],[1,3],[1]]
 * Explanation: We can create a 2D array that contains the following rows:
 * - 1,3,4,2
 * - 1,3
 * - 1
 * All elements of nums were used, and each row of the 2D array contains distinct integers, so it is a valid answer.
 * It can be shown that we cannot have less than 3 rows in a valid array.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: [[4,3,2,1]]
 * Explanation: All elements of the array are distinct, so we can keep all of them in the first row of the 2D array.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= nums.length
 *
 */
public class ConvertAnArrayIntoA2DArrayWithConditions {

    // V0
    // IDEA: COUNTING (put the c-th copy of a value into row c)
    //       the minimum number of rows is the max frequency: a value seen f times needs f
    //       different rows, and that many always suffice. when a value shows up for the
    //       (c + 1)-th time it goes into row c, so two equal values never share a row.
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<List<Integer>> findMatrix(int[] nums) {
        List<List<Integer>> rows = new ArrayList<>();
        // 1 <= nums[i] <= nums.length, so an array indexed by value is enough
        int[] seenCount = new int[nums.length + 1];

        for (int value : nums) {
            // this is copy number `copyIndex` (0-based) of value
            int copyIndex = seenCount[value];
            seenCount[value]++;
            // create rows lazily, so the row count ends at exactly the max frequency
            if (copyIndex == rows.size()) {
                rows.add(new ArrayList<>());
            }
            rows.get(copyIndex).add(value);
        }
        return rows;
    }
}
