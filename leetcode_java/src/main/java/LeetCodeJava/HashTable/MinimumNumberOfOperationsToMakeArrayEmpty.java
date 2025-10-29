package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty/

import java.util.HashMap;
import java.util.Map;

/**
 * 2870. Minimum Number of Operations to Make Array Empty
 *
 * 2244: Minimum Rounds to Complete All Tasks.
 *
 *
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given a 0-indexed array nums consisting of positive integers.
 *
 * There are two types of operations that you can apply on the array any number of times:
 *
 * Choose two elements with equal values and delete them from the array.
 * Choose three elements with equal values and delete them from the array.
 * Return the minimum number of operations required to make the array empty, or -1 if it is not possible.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,3,2,2,4,2,3,4]
 * Output: 4
 * Explanation: We can apply the following operations to make the array empty:
 * - Apply the first operation on the elements at indices 0 and 3. The resulting array is nums = [3,3,2,4,2,3,4].
 * - Apply the first operation on the elements at indices 2 and 4. The resulting array is nums = [3,3,4,3,4].
 * - Apply the second operation on the elements at indices 0, 1, and 3. The resulting array is nums = [4,4].
 * - Apply the first operation on the elements at indices 0 and 1. The resulting array is nums = [].
 * It can be shown that we cannot make the array empty in less than 4 operations.
 * Example 2:
 *
 * Input: nums = [2,1,2,2,3,3]
 * Output: -1
 * Explanation: It is impossible to empty the array.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 105
 * 1 <= nums[i] <= 106
 *
 *
 * Note: This question is the same as 2244: Minimum Rounds to Complete All Tasks.
 *
 *
 */
public class MinimumNumberOfOperationsToMakeArrayEmpty {

    // V0
//    public int minOperations(int[] nums) {
//
//    }

    // V1
    // IDEA: HASHMAP (fixed by gpt)
    public int minOperations_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int ops = 0;

        for (int count : map.values()) {
            if (count == 1)
                return -1; // cannot remove a single element

            // Greedy rule: use as many groups of 3 as possible
            // but if remainder == 1, adjust by converting one 3-group into 2+2
            /**
             *  NOTE !!!
             *
             *  Now:
             * 	•	count % 3 == 0 → Perfectly divisible by 3 → just use count / 3 operations.
             * 	•	count % 3 == 1 or count % 3 == 2 → There’s a leftover (1 or 2 items).
             *
             *  -> So we’ll need one extra operation to handle the remainder.
             *
             *
             */
            if (count % 3 == 0) {
                ops += count / 3;
            } else {
                ops += count / 3 + 1;
            }
        }

        return ops;
    }

    // V2
    // IDEA: COUNT
    // https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty/editorial/
    public int minOperations_2(int[] nums) {
        Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
        for (int num : nums) {
            counter.put(num, counter.getOrDefault(num, 0) + 1);
        }
        int ans = 0;
        for (int c : counter.values()) {
            if (c == 1) {
                return -1;
            }
            ans += Math.ceil((double) c / 3);
        }
        return ans;
    }


}
