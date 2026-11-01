package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-cost-to-equalize-array/description/

/**
 * 3139. Minimum Cost to Equalize Array
 * Hard
 *
 * You are given an integer array nums and two integers cost1 and cost2. You are allowed to perform either of the following operations any number of times:
 *
 * Choose an index i from nums and increase nums[i] by 1 for a cost of cost1.
 * Choose two different indexes i, j, from nums and increase nums[i] and nums[j] by 1 for a cost of cost2.
 *
 * Return the minimum cost required to make all elements in the array equal.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,1], cost1 = 5, cost2 = 2
 * Output: 15
 * Explanation:
 * The following operations can be performed to make the values equal:
 * Increase nums[1] by 1 for a cost of 5. nums becomes [4,2].
 * Increase nums[1] by 1 for a cost of 5. nums becomes [4,3].
 * Increase nums[1] by 1 for a cost of 5. nums becomes [4,4].
 * The total cost is 15.
 *
 * Example 2:
 *
 * Input: nums = [2,3,3,3,5], cost1 = 2, cost2 = 1
 * Output: 6
 * Explanation:
 * The following operations can be performed to make the values equal:
 * Increase nums[0] and nums[1] by 1 for a cost of 1. nums becomes [3,4,3,3,5].
 * Increase nums[0] and nums[2] by 1 for a cost of 1. nums becomes [4,4,4,3,5].
 * Increase nums[0] and nums[3] by 1 for a cost of 1. nums becomes [5,4,4,4,5].
 * Increase nums[1] and nums[2] by 1 for a cost of 1. nums becomes [5,5,5,4,5].
 * Increase nums[3] by 1 for a cost of 2. nums becomes [5,5,5,5,5].
 * The total cost is 6.
 *
 * Example 3:
 *
 * Input: nums = [3,5,3], cost1 = 1, cost2 = 3
 * Output: 4
 * Explanation:
 * The following operations can be performed to make the values equal:
 * Increase nums[0] by 1 for a cost of 1. nums becomes [4,5,3].
 * Increase nums[0] by 1 for a cost of 1. nums becomes [5,5,3].
 * Increase nums[2] by 1 for a cost of 1. nums becomes [5,5,4].
 * Increase nums[2] by 1 for a cost of 1. nums becomes [5,5,5].
 * The total cost is 4.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 * 1 <= cost1 <= 10^6
 * 1 <= cost2 <= 10^6
 *
 */
public class MinimumCostToEqualizeArray {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: FIX THE TARGET, THEN COUNT HOW MANY INCREMENTS CAN PAIR UP
    /**
     *  For a target T >= max(nums):
     *      total = n * T - sum(nums)   increments needed
     *      worst = T - min(nums)       the biggest single element's share
     *  A pair operation needs two DIFFERENT indices, so
     *      pairs = min(total / 2, total - worst)
     *  and the leftovers are paid as singles. If 2 * cost1 <= cost2 pairing
     *  never pays. Otherwise sweep T from max to 2 * max (the trade-off
     *  bottoms out before that). Compare real costs, apply MOD only at the end.
     *
     *  time = O(N + max(nums))
     *  space = O(1)
     */
    public int minCostToEqualizeArray(int[] nums, int cost1, int cost2) {
        int n = nums.length;
        long max = nums[0];
        long min = nums[0];
        long sum = 0;
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
            sum += num;
        }

        if (n == 1) {
            return 0; // already equal
        }
        if (2L * cost1 <= cost2) {
            // singles are never worse than pairs
            return (int) ((n * max - sum) * cost1 % MOD);
        }

        long best = Long.MAX_VALUE;
        for (long target = max; target <= 2 * max; target++) {
            long total = n * target - sum;
            long worst = target - min;
            long pairs = Math.max(0, Math.min(total / 2, total - worst));
            long cost = pairs * cost2 + (total - 2 * pairs) * cost1;
            best = Math.min(best, cost);
        }
        return (int) (best % MOD);
    }
}
