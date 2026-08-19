package LeetCodeJava.HashTable;

// https://leetcode.com/problems/array-partition-i/

import java.util.Arrays;

/**
 *  561. Array Partition I  (a.k.a. Array Partition)
 *  Easy
 *
 *  Given an integer array nums of 2n integers, group these integers into n pairs
 *  (a1, b1), (a2, b2), ..., (an, bn) such that the sum of min(ai, bi) for all i
 *  is maximized. Return the maximized sum.
 *
 *  Example 1:
 *  Input: nums = [1,4,3,2]
 *  Output: 4
 *  Explanation: pairs are (1, 2) and (3, 4) -> min(1,2) + min(3,4) = 1 + 3 = 4
 *
 *  Example 2:
 *  Input: nums = [6,2,6,5,1,2]
 *  Output: 9
 *  Explanation: pairs (2,1), (2,5), (6,6) -> 1 + 2 + 6 = 9
 *
 *  Constraints:
 *  1 <= n <= 10^4, nums.length == 2 * n
 *  -10^4 <= nums[i] <= 10^4
 */
public class ArrayPartitionI {

    // V0
    // IDEA: GREEDY + SORT. pair adjacent elements after sorting,
    //       so every "wasted" larger element is as small as possible -> take nums[0], nums[2], ...
    /**
     * time = O(n log n)
     * space = O(1)   // ignoring sort's internal space
     */
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int res = 0;
        for (int i = 0; i < nums.length; i += 2) {
            res += nums[i];
        }
        return res;
    }

    // V1
    // IDEA: COUNTING SORT over the bounded value range [-10^4, 10^4]
    /**
     * time = O(n + r)   // r = value range size
     * space = O(r)
     */
    public int arrayPairSum_1(int[] nums) {
        final int OFFSET = 10000;
        int[] cnt = new int[2 * OFFSET + 1];
        for (int n : nums) {
            cnt[n + OFFSET]++;
        }

        int res = 0;
        boolean takeNext = true; // true -> next element scanned is a "min" of its pair
        for (int v = 0; v < cnt.length; v++) {
            while (cnt[v] > 0) {
                if (takeNext) {
                    res += v - OFFSET;
                }
                takeNext = !takeNext;
                cnt[v]--;
            }
        }
        return res;
    }
}
