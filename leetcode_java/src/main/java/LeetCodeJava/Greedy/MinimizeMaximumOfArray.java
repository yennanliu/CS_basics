package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimize-maximum-of-array/description/

import java.util.*;

/**
 * 2439. Minimize Maximum of Array
 * Medium
 *
 * You are given a 0-indexed array nums comprising of n non-negative integers.
 *
 * In one operation, you must:
 *
 * Choose an integer i such that 1 <= i < n and nums[i] > 0.
 * Decrease nums[i] by 1.
 * Increase nums[i - 1] by 1.
 *
 * Return the minimum possible value of the maximum integer of nums after performing any number of operations.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,7,1,6]
 * Output: 5
 * Explanation:
 * One set of optimal operations is as follows:
 * 1. Choose i = 1, and nums becomes [4,6,1,6].
 * 2. Choose i = 3, and nums becomes [4,6,2,5].
 * 3. Choose i = 1, and nums becomes [5,5,2,5].
 * The maximum integer of nums is 5. It can be shown that the maximum number cannot be less than 5.
 * Therefore, we return 5.
 *
 * Example 2:
 *
 * Input: nums = [10,1]
 * Output: 10
 * Explanation:
 * It is optimal to leave nums as is, and since 10 is the maximum value, we return 10.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 10^5
 * 0 <= nums[i] <= 10^9
 *
 */
public class MinimizeMaximumOfArray {

    // V0
    // IDEA: GREEDY / PREFIX AVERAGE (VALUE CAN ONLY MOVE LEFT)
    //       the sum of each prefix [0..i] is trapped inside it, so its max is at least
    //       ceil(prefixSum / (i + 1)); that bound is reachable for every prefix at once.
    //       answer = max over i of ceil(prefixSum(i) / (i + 1)).
    //       (V1-style alternative: binary search on the answer, O(N log max).)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimizeArrayValue(int[] nums) {
        long result = 0;
        long prefixSum = 0;
        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];
            // ceil(prefixSum / (i + 1))
            long needed = (prefixSum + i) / (i + 1);
            result = Math.max(result, needed);
        }
        return (int) result;
    }

    // V1
    // IDEA: BINARY SEARCH ON THE ANSWER
    //       a limit is feasible if, sweeping right to left, every excess above the
    //       limit can be pushed left and nothing is left over at index 0.
    /**
     * time = O(N * log(max(nums)))
     * space = O(1)
     */
    public int minimizeArrayValue_1(int[] nums) {
        int low = 0;
        int high = 0;
        for (int num : nums) {
            high = Math.max(high, num);
        }
        // smallest limit that is feasible
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (canLimit(nums, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** True if every value can be brought to <= limit by moving units left. */
    private boolean canLimit(int[] nums, int limit) {
        // excess carried leftwards from the indices already processed
        long carry = 0;
        for (int i = nums.length - 1; i > 0; i--) {
            long value = nums[i] + carry;
            carry = Math.max(0, value - limit);
        }
        return nums[0] + carry <= limit;
    }
}
