package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-replacements-to-sort-the-array/description/

import java.util.*;

/**
 * 2366. Minimum Replacements to Sort the Array
 * Hard
 *
 * You are given a 0-indexed integer array nums. In one operation you can replace any element of the array with any two elements that sum to it.
 *
 * For example, consider nums = [5,6,7]. In one operation, we can replace nums[1] with 2 and 4 and convert nums to [5,2,4,7].
 *
 * Return the minimum number of operations to make an array that is sorted in non-decreasing order.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,9,3]
 * Output: 2
 * Explanation: Here are the steps to sort the array in non-decreasing order:
 * - From [3,9,3], replace the 9 with 3 and 6 so the array becomes [3,3,6,3]
 * - From [3,3,6,3], replace the 6 with 3 and 3 so the array becomes [3,3,3,3,3]
 * There are 2 steps to sort the array in non-decreasing order. Therefore, we return 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 0
 * Explanation: The array is already in non-decreasing order. Therefore, we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MinimumReplacementsToSortTheArray {

    // V0
    // IDEA: GREEDY RIGHT TO LEFT (FEWEST PIECES, LEFTMOST PIECE AS LARGE AS POSSIBLE)
    //       keep `limit` = the value the current element must not exceed.
    //       a value v > limit is cut into k = ceil(v / limit) pieces (k - 1 operations),
    //       and the best leftmost piece of a balanced split is v / k, which becomes the new limit.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long minimumReplacement(int[] nums) {
        long operations = 0;
        // the last element is never worth splitting
        long limit = nums[nums.length - 1];

        for (int i = nums.length - 2; i >= 0; i--) {
            long value = nums[i];
            if (value <= limit) {
                // already fits; it becomes the tighter limit for the prefix
                limit = value;
                continue;
            }
            // number of pieces needed so each piece is <= limit
            long pieces = (value + limit - 1) / limit;
            operations += pieces - 1;
            // smallest piece of the most balanced split
            limit = value / pieces;
        }
        return operations;
    }
}
