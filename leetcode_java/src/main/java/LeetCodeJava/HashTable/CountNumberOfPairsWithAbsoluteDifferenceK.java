package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-number-of-pairs-with-absolute-difference-k/description/

import java.util.*;

/**
 * 2006. Count Number of Pairs With Absolute Difference K
 * Easy
 *
 * Given an integer array nums and an integer k, return the number of pairs (i, j) where i < j such
 * that |nums[i] - nums[j]| == k.
 *
 * The value of |x| is defined as:
 *
 * x if x >= 0.
 * -x if x < 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,1], k = 1
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [1,3], k = 3
 * Output: 0
 * Explanation: There are no pairs with an absolute difference of 3.
 *
 * Example 3:
 *
 * Input: nums = [3,2,1,5,4], k = 2
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 100
 * 1 <= k <= 99
 *
 */
public class CountNumberOfPairsWithAbsoluteDifferenceK {

    // V0
    // IDEA: HASH TABLE, ONE PASS (the current value pairs with every earlier value +/- k)
    //       k >= 1, so value - k and value + k are different buckets: no double counting
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countKDifference(int[] nums, int k) {
        // value -> how many times it appeared before the current index
        Map<Integer, Integer> seenCount = new HashMap<>();
        int pairs = 0;

        for (int value : nums) {
            pairs += seenCount.getOrDefault(value - k, 0);
            pairs += seenCount.getOrDefault(value + k, 0);
            seenCount.put(value, seenCount.getOrDefault(value, 0) + 1);
        }
        return pairs;
    }
}
