package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-nice-pairs-in-an-array/description/

import java.util.*;

/**
 * 1814. Count Nice Pairs in an Array
 * Medium
 *
 * You are given an array nums that consists of non-negative integers. Let us define rev(x) as the
 * reverse of the non-negative integer x. For example, rev(123) = 321, and rev(120) = 21. A pair of
 * indices (i, j) is nice if it satisfies all of the following conditions:
 *
 * 0 <= i < j < nums.length
 * nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
 *
 * Return the number of nice pairs of indices. Since that number can be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [42,11,1,97]
 * Output: 2
 * Explanation: The two pairs are:
 *  - (0,3) : 42 + rev(97) = 42 + 79 = 121, 97 + rev(42) = 97 + 24 = 121.
 *  - (1,2) : 11 + rev(1) = 11 + 1 = 12, 1 + rev(11) = 1 + 11 = 12.
 *
 * Example 2:
 *
 * Input: nums = [13,10,35,24,76]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 *
 */
public class CountNicePairsInAnArray {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: REARRANGE INTO A PER-ELEMENT KEY, THEN GROUP-COUNT
    //       nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
    //       <=> nums[i] - rev(nums[i]) == nums[j] - rev(nums[j])
    //       so pairs share key(x) = x - rev(x); counting earlier equal keys counts each pair once
    /**
     * time = O(N * D), D = number of digits (<= 10)
     * space = O(N)
     */
    public int countNicePairs(int[] nums) {
        // key -> how many earlier elements had it
        Map<Integer, Integer> seenCount = new HashMap<>();
        long pairs = 0;

        for (int num : nums) {
            int key = num - reverse(num);
            int earlier = seenCount.getOrDefault(key, 0);
            // the current element pairs with every earlier element of the same key
            pairs = (pairs + earlier) % MOD;
            seenCount.put(key, earlier + 1);
        }
        return (int) pairs;
    }

    /** Reverses the decimal digits of a non-negative int (rev(120) = 21). */
    private int reverse(int x) {
        int reversed = 0;
        while (x > 0) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        return reversed;
    }
}
