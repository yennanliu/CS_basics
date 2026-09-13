package LeetCodeJava.HashTable;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-array-empty/

import java.util.HashMap;
import java.util.Map;

/**
 * 2870. Minimum Number of Operations to Make Array Empty
 * Medium
 *
 * 2244: Minimum Rounds to Complete All Tasks.
 *
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
 */
public class MinimumNumberOfOperationsToMakeArrayEmpty {

    // V0
    // IDEA: HASHMAP (fixed by gpt)
    /**
     *   NOTE !!!  core:
     *
     * Let’s analyze possible remainders when dividing by 3:
     *
     * count % 3	count pattern	how to remove	total ops	note
     * 0	e.g. 3, 6, 9	all 3s	count / 3	perfectly divisible
     * 1	e.g. 4, 7, 10	(3 + 1) → change one 3 to (2 + 2)	(count / 3 - 1) + 2 = count / 3 + 1	e.g. 4 = 2+2, 7 = 3+2+2
     * 2	e.g. 2, 5, 8	add one group of 2	count / 3 + 1	e.g. 5 = 3+2, 8 = 3+3+2
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minOperations(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : nums) {
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        int ops = 0;

        for (int count : freq.values()) {
            // If only one occurrence, impossible to remove
            if (count == 1) {
                return -1;
            }

            // Optimal strategy: use as many groups of 3 as possible
            // Example: count = 8 → 8/3 = 2 groups of 3 + 1 leftover (use +1 group of 2)
            /**
             *  NOTE !!! core logic:
             *
             *  Excellent — that’s a very sharp question 👏
             * Let’s dig deep into why we don’t need to update count or worry about remaining elements after
             *
             * if (count % 3 != 0) ops += 1;
             *
             *
             * ⸻
             *
             * 🧩 Step 1 — The Allowed Moves
             *
             * You can remove groups of 2 or 3 identical numbers.
             *
             * So for each distinct number, the total count count can be expressed as:
             *
             * count = 3 * a + 2 * b
             *
             * where a, b are non negative integers.
             *
             * You need to find minimum (a + b) to cover count.
             *
             * ⸻
             *
             * 🧩 Step 2 — Modulo Patterns
             *
             * Let’s analyze possible remainders when dividing by 3:
             *
             *
             * ------------------------------------
             * NOTE !!!!! BELOW CASES
             * ------------------------------------
             *
             * count % 3	count pattern	how to remove	total ops	note
             * 0	e.g. 3, 6, 9	all 3s	count / 3	perfectly divisible
             * 1	e.g. 4, 7, 10	(3 + 1) → change one 3 to (2 + 2)	(count / 3 - 1) + 2 = count / 3 + 1	e.g. 4 = 2+2, 7 = 3+2+2
             * 2	e.g. 2, 5, 8	add one group of 2	count / 3 + 1	e.g. 5 = 3+2, 8 = 3+3+2
             *
             *
             * ⸻
             *
             * ✅ Step 3 — Unified Formula
             *
             * For all valid counts ≥ 2:
             *
             * ops += count / 3;
             * if (count % 3 != 0) ops += 1;
             *
             * This works because:
             * 	•	If count % 3 == 0 → perfectly divided into groups of 3.
             * 	•	If count % 3 == 1 → need one more op (replace one 3 with two 2s → +1 op).
             * 	•	If count % 3 == 2 → need one more op (just one extra pair → +1 op).
             *
             * Thus, no leftover elements ever remain unremovable after counting ops this way.
             *
             * ⸻
             *
             * 🧮 Step 4 — Examples
             *
             * count	possible grouping	ops	formula result
             * 2	(2)	1	0 + 1
             * 3	(3)	1	1 + 0
             * 4	(2,2)	2	1 + 1
             * 5	(3,2)	2	1 + 1
             * 6	(3,3)	2	2 + 0
             * 7	(3,2,2)	3	2 + 1
             * 8	(3,3,2)	3	2 + 1
             * 9	(3,3,3)	3	3 + 0
             *
             * All match ✅
             *
             * ⸻
             *
             * 🚫 The Only Impossible Case
             *
             * count == 1 → can’t remove a single number with any allowed op.
             *
             * So:
             *
             * if (count == 1) return -1;
             *
             *
             * ⸻
             *
             * ✅ Summary:
             *
             * We don’t update count because:
             * 	•	We’re not simulating removals.
             * 	•	The formula count / 3 + (count % 3 == 0 ? 0 : 1) already encodes the minimal number of operations needed to eliminate that count entirely.
             *
             */
            ops += count / 3;
            if (count % 3 != 0) {
                ops += 1;
            }
        }

        return ops;
    }

    // V1
    // IDEA: HASHMAP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
