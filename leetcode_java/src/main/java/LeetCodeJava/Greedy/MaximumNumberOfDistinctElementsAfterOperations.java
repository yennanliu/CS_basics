package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-distinct-elements-after-operations/description/

import java.util.*;

/**
 * 3397. Maximum Number of Distinct Elements After Operations
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * You are allowed to perform the following operation on each element of the array
 * at most once:
 *
 * Add an integer in the range [-k, k] to the element.
 *
 * Return the maximum possible number of distinct elements in nums after performing
 * the operations.
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,3,3,4], k = 2
 *
 * Output: 6
 *
 * Explanation:
 *
 * nums changes to [-1, 0, 1, 2, 3, 4] after performing operations on the first
 * four elements.
 *
 * Example 2:
 *
 * Input: nums = [4,4,4,4], k = 1
 *
 * Output: 3
 *
 * Explanation:
 *
 * By adding -1 to nums[0] and 1 to nums[1], nums changes to [3, 5, 4, 4].
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 0 <= k <= 10^9
 *
 */
public class MaximumNumberOfDistinctElementsAfterOperations {

    // V0
    // IDEA: SORT + GREEDY — GIVE EACH VALUE THE SMALLEST FREE SLOT IN [x - k, x + k]
    //       process values ascending; the smallest unused slot it may take is
    //       max(previous + 1, x - k). keeping each assignment minimal never hurts a
    //       later element, whose window starts no earlier. if that slot exceeds
    //       x + k, the element cannot be made distinct and is skipped.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxDistinctElements(int[] nums, int k) {
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);

        long previous = Long.MIN_VALUE / 2; // last assigned value
        int distinct = 0;
        for (int x : sorted) {
            long slot = Math.max(previous + 1, (long) x - k);
            if (slot <= (long) x + k) {
                distinct++;
                previous = slot;
            }
        }
        return distinct;
    }
}
