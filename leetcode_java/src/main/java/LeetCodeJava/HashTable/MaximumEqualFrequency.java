package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-equal-frequency/description/

import java.util.*;

/**
 * 1224. Maximum Equal Frequency
 * Hard
 *
 * Given an array nums of positive integers, return the longest possible length of an array
 * prefix of nums, such that it is possible to remove exactly one element from this prefix
 * so that every number that has appeared in it will have the same number of occurrences.
 *
 * If after removing one element there are no remaining elements, it's still considered that
 * every appeared number has the same number of ocurrences (0).
 *
 *
 * Example 1:
 *
 * Input: nums = [2,2,1,1,5,3,3,5]
 * Output: 7
 * Explanation: For the subarray [2,2,1,1,5,3,3] of length 7, if we remove nums[4] = 5, we
 * will get [2,2,1,1,3,3], so that each number will appear exactly twice.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,2,2,2,3,3,3,4,4,4,5]
 * Output: 13
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MaximumEqualFrequency {

    // V0
    // IDEA: HASH TABLE (count of each value) + HASH TABLE (count of counts)
    //       a prefix of length len is valid iff one of:
    //         1) maxCount == 1                                  -> drop any element
    //         2) one value has maxCount, all others maxCount-1  -> drop one copy of the top value
    //         3) all values have maxCount except one lone value -> drop that lone value
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxEqualFreq(int[] nums) {
        // valueCount[v] = occurrences of v in the prefix
        Map<Integer, Integer> valueCount = new HashMap<>();
        // freqCount[c] = number of distinct values whose occurrence count is exactly c
        Map<Integer, Integer> freqCount = new HashMap<>();
        int maxCount = 0;
        int longest = 0;

        for (int i = 0; i < nums.length; i++) {
            int value = nums[i];
            int prefixLength = i + 1;

            // move value from its old count bucket to the new one
            int oldCount = valueCount.getOrDefault(value, 0);
            if (oldCount > 0) {
                freqCount.put(oldCount, freqCount.get(oldCount) - 1);
            }
            int newCount = oldCount + 1;
            valueCount.put(value, newCount);
            freqCount.put(newCount, freqCount.getOrDefault(newCount, 0) + 1);
            maxCount = Math.max(maxCount, newCount);

            int atMax = freqCount.getOrDefault(maxCount, 0);
            int belowMax = freqCount.getOrDefault(maxCount - 1, 0);
            int singles = freqCount.getOrDefault(1, 0);

            if (maxCount == 1) {
                // case 1: every value appears once
                longest = prefixLength;
            } else if (atMax == 1 && maxCount + belowMax * (maxCount - 1) == prefixLength) {
                // case 2: exactly one value on top, every other value one below it
                longest = prefixLength;
            } else if (singles == 1 && atMax * maxCount + 1 == prefixLength) {
                // case 3: everything at maxCount plus a single value seen once
                longest = prefixLength;
            }
        }
        return longest;
    }
}
