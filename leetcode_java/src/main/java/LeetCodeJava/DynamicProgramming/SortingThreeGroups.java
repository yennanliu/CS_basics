package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/sorting-three-groups/description/

import java.util.*;

/**
 * 2826. Sorting Three Groups
 * Medium
 *
 * You are given an integer array nums. Each element in nums is 1, 2 or 3. In each operation, you
 * can remove an element from nums. Return the minimum number of operations to make nums
 * non-decreasing.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,2,1]
 * Output: 3
 * Explanation:
 * One of the optimal solutions is to remove nums[0], nums[2] and nums[3].
 *
 * Example 2:
 *
 * Input: nums = [1,3,2,1,3,3]
 * Output: 2
 * Explanation:
 * One of the optimal solutions is to remove nums[1] and nums[2].
 *
 * Example 3:
 *
 * Input: nums = [2,2,2,2,3,3]
 * Output: 0
 * Explanation:
 * nums is already non-decreasing.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 3
 *
 * Follow-up: Can you come up with an algorithm that runs in O(n) time complexity?
 *
 */
public class SortingThreeGroups {

    // V0
    // IDEA: LONGEST NON-DECREASING SUBSEQUENCE OVER 3 VALUES - best[v] = LONGEST KEPT SEQUENCE WHOSE LAST VALUE <= v
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumOperations(List<Integer> nums) {
        // best[0..2] = longest non-decreasing subsequence so far whose last kept value is <= 1 / 2 / 3
        int[] best = new int[3];

        for (int num : nums) {
            int slot = num - 1;
            // keep this element: it extends the best sequence ending in a value <= num
            best[slot]++;
            // "last value <= v" for larger v includes everything that ends at <= num
            for (int bigger = slot + 1; bigger < 3; bigger++) {
                best[bigger] = Math.max(best[bigger], best[bigger - 1]);
            }
        }

        // remove everything not in the longest kept sequence
        return nums.size() - best[2];
    }
}
