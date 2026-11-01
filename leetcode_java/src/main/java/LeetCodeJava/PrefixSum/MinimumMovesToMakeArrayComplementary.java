package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/minimum-moves-to-make-array-complementary/description/

/**
 * 1674. Minimum Moves to Make Array Complementary
 * Medium
 *
 * You are given an integer array nums of even length n and an integer limit. In one
 * move, you can replace any integer from nums with another integer between 1 and
 * limit, inclusive.
 *
 * The array nums is complementary if for all indices i (0-indexed),
 * nums[i] + nums[n - 1 - i] equals the same number. For example, the array
 * [1,2,3,4] is complementary because for all indices i, nums[i] + nums[n - 1 - i] = 5.
 *
 * Return the minimum number of moves required to make nums complementary.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,4,3], limit = 4
 * Output: 1
 * Explanation: In 1 move, you can change nums to [1,2,2,3]; every pair sums to 4.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2,1], limit = 2
 * Output: 2
 * Explanation: In 2 moves, you can change nums to [2,2,2,2]. You cannot change any
 * number to 3 since 3 > limit.
 *
 * Example 3:
 *
 * Input: nums = [1,2,1,2], limit = 2
 * Output: 0
 * Explanation: nums is already complementary.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 10^5
 * 1 <= nums[i] <= limit <= 10^5
 * n is even.
 *
 */
public class MinimumMovesToMakeArrayComplementary {

    // V0
    // IDEA: DIFFERENCE ARRAY / LINE SWEEP OVER EVERY TARGET SUM T IN [2, 2 * limit]
    //       for a pair (x, y) with x <= y:
    //         cost 0 at T == x + y
    //         cost 1 on [x + 1, y + limit]  (change one side)
    //         cost 2 everywhere else
    //       record those range updates in a difference array, prefix-sum it once,
    //       and take the minimum over T.
    /**
     * time = O(N + limit)
     * space = O(limit)
     */
    public int minMoves(int[] nums, int limit) {
        int n = nums.length;
        int maxSum = 2 * limit;
        // diff[t] holds the change in total cost when the target moves from t-1 to t
        int[] diff = new int[maxSum + 2];

        for (int i = 0; i < n / 2; i++) {
            int small = Math.min(nums[i], nums[n - 1 - i]);
            int big = Math.max(nums[i], nums[n - 1 - i]);

            // baseline: both numbers must change, for every target
            diff[2] += 2;
            diff[maxSum + 1] -= 2;
            // one change is enough on [small + 1, big + limit]
            diff[small + 1] -= 1;
            diff[big + limit + 1] += 1;
            // no change at exactly small + big
            diff[small + big] -= 1;
            diff[small + big + 1] += 1;
        }

        int best = Integer.MAX_VALUE;
        int cost = 0;
        for (int target = 2; target <= maxSum; target++) {
            cost += diff[target];
            best = Math.min(best, cost);
        }
        return best;
    }
}
