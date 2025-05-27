package LeetCodeJava.HashTable;

// https://leetcode.com/problems/divide-array-into-increasing-sequences/description/
// https://leetcode.ca/all/1121.html

import java.util.HashMap;
import java.util.Map;

/**
 * 1121. Divide Array Into Increasing Sequences
 * Given a non-decreasing array of positive integers nums and an integer K, find out if this array can be divided into one or more disjoint increasing subsequences of length at least K.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,3,3,4,4], K = 3
 * Output: true
 * Explanation:
 * The array can be divided into the two subsequences [1,2,3,4] and [2,3,4] with lengths at least 3 each.
 * Example 2:
 *
 * Input: nums = [5,6,6,7,8], K = 3
 * Output: false
 * Explanation:
 * There is no way to divide the array using the conditions required.
 *
 *
 * Note:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= K <= nums.length
 * 1 <= nums[i] <= 10^5
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Google
 *
 *
 */
public class DivideArrayIntoIncreasingSequences {

    // V0

    // V1

    // V2
    // https://leetcode.ca/2018-12-25-1121-Divide-Array-Into-Increasing-Sequences/
    // TODO: validate below
    public boolean canDivideIntoSubsequences_2(int[] nums, int k) {
        Map<Integer, Integer> cnt = new HashMap<>();
        int mx = 0;
        for (int x : nums) {
            mx = Math.max(mx, cnt.merge(x, 1, Integer::sum));
        }
        return mx * k <= nums.length;
    }

}
