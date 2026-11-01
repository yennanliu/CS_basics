package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-cost-to-make-array-equal/description/

import java.util.Arrays;

/**
 * 2448. Minimum Cost to Make Array Equal
 * Hard
 *
 * You are given two 0-indexed arrays nums and cost consisting each of n positive integers.
 *
 * You can do the following operation any number of times:
 *
 * Increase or decrease any element of the array nums by 1.
 *
 * The cost of doing one operation on the ith element is cost[i].
 *
 * Return the minimum total cost such that all the elements of the array nums become equal.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,2], cost = [2,3,1,14]
 * Output: 8
 * Explanation: We can make all the elements equal to 2 in the following way:
 * - Increase the 0th element one time. The cost is 2.
 * - Decrease the 1st element one time. The cost is 3.
 * - Decrease the 2nd element three times. The cost is 1 + 1 + 1 = 3.
 * The total cost is 2 + 3 + 3 = 8.
 * It can be shown that we cannot make the array equal with a smaller cost.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2,2,2], cost = [4,2,8,1,3]
 * Output: 0
 * Explanation: All the elements are already equal, so no operations are needed.
 *
 *
 * Constraints:
 *
 * n == nums.length == cost.length
 * 1 <= n <= 10^5
 * 1 <= nums[i], cost[i] <= 10^6
 * Test cases are generated in a way that the output doesn't exceed 2^53-1
 *
 */
public class MinimumCostToMakeArrayEqual {

    // V0
    // IDEA: WEIGHTED MEDIAN (f(x) = sum cost[i] * |nums[i] - x| IS CONVEX, PIECEWISE LINEAR)
    /**
     *  Treat nums[i] as a point repeated cost[i] times: minimising the sum of weighted absolute
     *  deviations is the median problem, so the optimal x is the weighted median.
     *  Sort by value, accumulate weight, and stop at the first value whose running weight
     *  reaches half the total; then evaluate f once. The optimum is always one of the nums[i],
     *  since f's slope only changes there.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long minCost(int[] nums, int[] cost) {
        int n = nums.length;

        // indices sorted by value, so nums and cost stay paired
        Integer[] order = new Integer[n];
        long totalWeight = 0;
        for (int i = 0; i < n; i++) {
            order[i] = i;
            totalWeight += cost[i];
        }
        Arrays.sort(order, (a, b) -> Integer.compare(nums[a], nums[b]));

        // walk until the running weight reaches half of the total
        long runningWeight = 0;
        int target = nums[order[n - 1]];
        for (int index : order) {
            runningWeight += cost[index];
            // 2 * running >= total avoids a fractional half
            if (2 * runningWeight >= totalWeight) {
                target = nums[index];
                break;
            }
        }
        return totalCost(nums, cost, target);
    }

    /** Cost of moving every value to target. */
    private long totalCost(int[] nums, int[] cost, long target) {
        long total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += Math.abs(nums[i] - target) * cost[i];
        }
        return total;
    }

    // V1
    // IDEA: BINARY SEARCH ON A CONVEX FUNCTION (COMPARE f(mid) WITH f(mid + 1))
    /**
     *  f(x) is convex, so f(mid) <= f(mid + 1) means the minimum is at mid or to its left;
     *  otherwise it is to the right. Search x over [min(nums), max(nums)].
     */
    /**
     * time = O(N log(max(nums)))
     * space = O(1)
     */
    public long minCost_1(int[] nums, int[] cost) {
        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;
        for (int value : nums) {
            low = Math.min(low, value);
            high = Math.max(high, value);
        }

        while (low < high) {
            int mid = low + (high - low) / 2;
            if (totalCost(nums, cost, mid) <= totalCost(nums, cost, mid + 1)) {
                // f is not decreasing past mid: the minimum is at mid or left of it
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return totalCost(nums, cost, low);
    }
}
