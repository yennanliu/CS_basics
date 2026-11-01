package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-integers-appearing-in-a-single-block/description/

import java.util.*;

/**
 * 4038. Count Integers Appearing in a Single Block
 * Easy
 *
 * You are given an integer array nums.
 *
 * An integer x is called special if all occurrences of x in nums
 * appear in a single contiguous block.
 *
 * (in other words, the indices where x shows up are consecutive,
 * with no other value in between)
 *
 * Return the number of distinct special integers in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,2,3]
 * Output: 3
 * Explanation:
 * 1 occupies indices [0,1], 2 occupies indices [2,3] and 3 occupies index [4].
 * Every value sits in one contiguous block, so all 3 distinct values are special.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1]
 * Output: 1
 * Explanation:
 * 1 shows up at indices 0 and 2, which are split by the 2 at index 1,
 * so 1 is NOT special. Only 2 is special.
 *
 * Example 3:
 *
 * Input: nums = [5]
 * Output: 1
 * Explanation:
 * A value that appears once is always a (single element) block.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class CountIntegersAppearingInASingleBlock {

    // V0
    // IDEA: HASH MAP {val : [idx_1, idx_2, ...]} + `span == count` CHECK
    //
    //   collect every index a value lands on; the indices are consecutive
    //   exactly when  lastIdx - firstIdx + 1 == number of occurrences
    //   (any other value inside the span would make the span strictly wider).
    //
    //   e.g. nums = [1,2,1] -> idx of 1 = [0,2] -> 2 - 0 + 1 = 3 != 2 -> NOT special
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countSpecialIntegers(int[] nums) {
        // edge case: nothing to count
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // value -> every index it appears at, in increasing order
        Map<Integer, List<Integer>> indicesByValue = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            indicesByValue.computeIfAbsent(nums[i], x -> new ArrayList<>()).add(i);
        }

        int specialCount = 0;
        for (List<Integer> indices : indicesByValue.values()) {
            int firstIdx = indices.get(0);
            int lastIdx = indices.get(indices.size() - 1);

            // NOTE !!! span == count  <=>  indices are consecutive
            //          (a value that appears once always passes: 0 + 1 == 1)
            if (lastIdx - firstIdx + 1 == indices.size()) {
                specialCount++;
            }
        }

        return specialCount;
    }

    // V0-1
    // IDEA: ONE PASS, KEEP ONLY (firstIdx, lastIdx, count) PER VALUE
    //
    //   same `span == count` check as V0, but the check only ever reads the
    //   first index, the last index and the count, so there is no need to
    //   keep every index: O(distinct) cells instead of O(n) list entries.
    /**
     * time = O(N)
     * space = O(K), K = number of distinct values
     */
    public int countSpecialIntegers_0_1(int[] nums) {
        // edge case: nothing to count
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // value -> {firstIdx, lastIdx, count}
        Map<Integer, int[]> info = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int[] entry = info.get(nums[i]);
            if (entry == null) {
                info.put(nums[i], new int[] { i, i, 1 });
            } else {
                entry[1] = i;   // latest index seen so far is the last one
                entry[2]++;     // one more occurrence
            }
        }

        int specialCount = 0;
        for (int[] entry : info.values()) {
            int firstIdx = entry[0];
            int lastIdx = entry[1];
            int count = entry[2];

            // contiguous exactly when the span equals the number of occurrences
            if (lastIdx - firstIdx + 1 == count) {
                specialCount++;
            }
        }

        return specialCount;
    }
}
