package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-operations-to-make-numbers-non-positive/description/

/**
 * 2702. Minimum Operations to Make Numbers Non-positive
 * Hard
 *
 * You are given a 0-indexed integer array nums and two integers x and y. In one operation, you must choose an index i such that 0 <= i < nums.length and perform the following:
 *
 * Decrement nums[i] by x.
 * Decrement values by y at all indices except the ith one.
 *
 * Return the minimum number of operations to make all the integers in nums less than or equal to zero.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,1,7,6], x = 4, y = 2
 * Output: 3
 * Explanation: You will need three operations. One of the optimal sequence of operations is:
 * Operation 1: Choose i = 3. Then, nums = [1,2,-1,3,4].
 * Operation 2: Choose i = 3. Then, nums = [-1,0,-3,-1,2].
 * Operation 3: Choose i = 4. Then, nums = [-3,-2,-5,-3,-2].
 * Now all the numbers in nums are non-positive. Therefore, we return 3.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1], x = 2, y = 1
 * Output: 1
 * Explanation: We can perform the operation once on i = 1. Then, nums becomes [0,0,0].
 * All the positive numbers are removed, and therefore, we return 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= y < x <= 10^9
 *
 */
public class MinimumOperationsToMakeNumbersNonPositive {

    // V0
    // IDEA: BINARY SEARCH ON THE NUMBER OF OPERATIONS t
    // after t operations every element has lost t*y, and each time it is the chosen one it
    // loses an extra (x - y). So element v needs ceil((v - t*y) / (x - y)) picks of its own
    // (0 if v <= t*y). t operations suffice <=> the total picks needed <= t, which is
    // monotone in t. t = max(nums) always works (y >= 1), so it is the right bound.
    // NOTE: t*y reaches ~1e18 - use long and integer ceil-division, never floating point.
    /**
     * time = O(N log(max(nums)))
     * space = O(1)
     */
    public int minOperations(int[] nums, int x, int y) {
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }

        long left = 0;
        long right = maxValue;
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (canFinish(nums, x, y, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return (int) left;
    }

    /** Whether t operations are enough to make every element non-positive. */
    private boolean canFinish(int[] nums, long x, long y, long t) {
        long extraPerPick = x - y;
        long commonLoss = t * y;
        long picksNeeded = 0;
        for (int num : nums) {
            if (num > commonLoss) {
                // integer ceil((num - commonLoss) / extraPerPick)
                picksNeeded += (num - commonLoss + extraPerPick - 1) / extraPerPick;
                if (picksNeeded > t) {
                    return false;
                }
            }
        }
        return picksNeeded <= t;
    }
}
