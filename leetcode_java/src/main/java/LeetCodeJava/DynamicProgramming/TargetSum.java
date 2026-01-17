package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/target-sum/

import java.util.HashMap;
import java.util.Map;

/**
 * 494. Target Sum
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an integer array nums and an integer target.
 *
 * You want to build an expression out of nums by adding one of the symbols '+' and '-' before each integer in nums and then concatenate all the integers.
 *
 * For example, if nums = [2, 1], you can add a '+' before 2 and a '-' before 1 and concatenate them to build the expression "+2-1".
 * Return the number of different expressions that you can build, which evaluates to target.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,1,1], target = 3
 * Output: 5
 * Explanation: There are 5 ways to assign symbols to make the sum of nums be target 3.
 * -1 + 1 + 1 + 1 + 1 = 3
 * +1 - 1 + 1 + 1 + 1 = 3
 * +1 + 1 - 1 + 1 + 1 = 3
 * +1 + 1 + 1 - 1 + 1 = 3
 * +1 + 1 + 1 + 1 - 1 = 3
 * Example 2:
 *
 * Input: nums = [1], target = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 1000
 * 0 <= sum(nums[i]) <= 1000
 * -1000 <= target <= 1000
 *
 *
 */
public class TargetSum {

    // V0
//    public int findTargetSumWays(int[] nums, int target) {
//
//    }


    // V0-1
    // IDEA: DP (GEMINI)
    /**
     *
     * For **LeetCode 494 (Target Sum)**, the goal is to find the number of ways to assign `+` or `-` signs to each number in an array so they sum up to a `target`.
     *
     * ### 💡 The Mathematical Trick
     *
     * You can transform this problem into
     * a simpler **Subset Sum** problem.
     *
     * 1. Let  be the subset of numbers with a `+` sign.
     * 2. Let  be the subset of numbers with a `-` sign.
     * 3. We know:
     * 4. We also know:
     * 5. Adding these two:
     * 6. Therefore:
     *
     * The problem is now: **How many ways
     * can we find a subset  that sums to this new value?**
     *
     * ---
     *
     * ### 🔍 Key Logic Points
     *
     * * **The Array:** `int[] dp = new int[subsetTarget + 1]` is a **1D array**. This is the space-optimized version of a 2D array `dp[n][subsetTarget]`.
     * * **The Inner Loop Direction:** We loop **backwards** (`i--`). If we looped forwards, we would be allowed to use the same number multiple times (Unbounded Knapsack), which isn't allowed here.
     * * **Time Complexity:** , where  is the number of elements.
     * * **Space Complexity:** .
     *
     * ### 📊 Comparing Stock vs. Target Sum Arrays
     *
     * | Problem | DP Array Shape | What columns represent |
     * | --- | --- | --- |
     * | **Stock (LC 309)** | `n x 3` | Specific States (Hold, Sold, Rest) |
     * | **Target Sum (LC 494)** | `1 x subsetTarget` | Every possible integer sum from 0 to Target |
     *
     */
    public int findTargetSumWays_0_1(int[] nums, int target) {
        int totalSum = 0;
        for (int n : nums)
            totalSum += n;

        // Edge cases:
        // 1. target is too large to reach
        // 2. target + totalSum must be even (from the formula above)
        if (Math.abs(target) > totalSum || (target + totalSum) % 2 != 0) {
            return 0;
        }

        int subsetTarget = (target + totalSum) / 2;

        // dp[i] stores the number of ways to get a sum of i
        int[] dp = new int[subsetTarget + 1];
        dp[0] = 1; // Base case: 1 way to make sum 0 (empty subset)

        for (int num : nums) {
            // Iterate BACKWARDS to avoid using the same number twice
            // in the same row (standard 0/1 Knapsack optimization)
            for (int i = subsetTarget; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }

        return dp[subsetTarget];
    }


    // V0-2
    // IDEA: 1D DP (GPT)
    /**
     * Here’s a **Java dynamic programming solution** for **LeetCode 494 — Target Sum**.
     *
     * ---
     *
     * ## 🧠 Problem Insight
     *
     * Given an array `nums` and a target `S`, assign `+` or `–` to each number to reach `S`.
     * Equivalent to: partition `nums` into two groups with sums `P` and `N` such that:
     *
     * ```
     * P − N = S
     * P + N = sum(nums)
     * ⇒ P = (S + sum) / 2
     * ```
     *
     * So the problem becomes:
     *
     * > Count the number of subsets with sum equal to `target = (S + sum) / 2`.
     *
     * Constraints:
     *
     * * If `(S + sum)` is odd or `target < 0`, return `0`.
     *
     * ---
     *
     * ## 📌 DP (1D Knapsack Count)
     *
     * Let `dp[i]` = number of ways to reach sum `i`.
     *
     * Initialize:
     *
     * ```
     * dp[0] = 1
     * ```
     *
     * For each num:
     *
     * ```
     * for i from target down to num:
     *     dp[i] += dp[i - num]
     * ```
     *
     * ---
     *
     * ## 🧩 Example
     *
     * ```
     * nums = [1,1,1,1,1]
     * S = 3
     * sum = 5
     * target = (3 + 5) / 2 = 4
     * ```
     *
     * Subsets summing to 4 → 5 ways ⇒ output = 5.
     *
     * ---
     *
     * ## ⏱ Complexity
     *
     * * **Time:** O(n × target)
     * * **Space:** O(target)
     *
     */
    public int findTargetSumWays_0_2(int[] nums, int S) {
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }

        // If (sum + S) is odd or S > sum, no valid partition
        if ((S + sum) % 2 != 0 || Math.abs(S) > sum) {
            return 0;
        }

        int target = (S + sum) / 2;

        int[] dp = new int[target + 1];
        dp[0] = 1;

        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }

        return dp[target];
    }



    // V0-10
    // IDEA: RECURSION (gpt)
    public int findTargetSumWays_0_10(int[] nums, int target) {
        return calculateWays(nums, 0, 0, target);
    }

    private int calculateWays(int[] nums, int index, int currentSum, int target) {
        // Base case: all numbers used
        if (index == nums.length) {
            return currentSum == target ? 1 : 0;
        }

        // Choose '+' for current number
        int add = calculateWays(nums, index + 1, currentSum + nums[index], target);

        // Choose '-' for current number
        int subtract = calculateWays(nums, index + 1, currentSum - nums[index], target);

        // Return total ways from both choices
        return add + subtract;
    }

    // V1-1
    // https://neetcode.io/problems/target-sum
    // IDEA: RECURSION
    public int findTargetSumWays_1_1(int[] nums, int target) {
        return backtrack(0, 0, nums, target);
    }

    private int backtrack(int i, int total, int[] nums, int target) {
        if (i == nums.length) {
            return total == target ? 1 : 0;
        }
        return backtrack(i + 1, total + nums[i], nums, target) +
                backtrack(i + 1, total - nums[i], nums, target);
    }


    // V1-2
    // https://neetcode.io/problems/target-sum
    // IDEA: Dynamic Programming (Top-Down)
    private int[][] dp;
    private int totalSum;

    public int findTargetSumWays_1_2(int[] nums, int target) {
        totalSum = 0;
        for (int num : nums) totalSum += num;
        dp = new int[nums.length][2 * totalSum + 1];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < 2 * totalSum + 1; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }
        return backtrack_1_2(0, 0, nums, target);
    }

    private int backtrack_1_2(int i, int total, int[] nums, int target) {
        if (i == nums.length) {
            return total == target ? 1 : 0;
        }
        if (dp[i][total + totalSum] != Integer.MIN_VALUE) {
            return dp[i][total + totalSum];
        }
        dp[i][total + totalSum] = backtrack_1_2(i + 1, total + nums[i], nums, target) +
                backtrack_1_2(i + 1, total - nums[i], nums, target);
        return dp[i][total + totalSum];
    }


    // V1-3
    // https://neetcode.io/problems/target-sum
    // IDEA: Dynamic Programming (Bottom-Up)
    public int findTargetSumWays_1_3(int[] nums, int target) {
        int n = nums.length;
        Map<Integer, Integer>[] dp = new HashMap[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = new HashMap<>();
        }
        dp[0].put(0, 1);

        for (int i = 0; i < n; i++) {
            for (Map.Entry<Integer, Integer> entry : dp[i].entrySet()) {
                int total = entry.getKey();
                int count = entry.getValue();
                dp[i + 1].put(total + nums[i],
                        dp[i + 1].getOrDefault(total + nums[i], 0) + count);
                dp[i + 1].put(total - nums[i],
                        dp[i + 1].getOrDefault(total - nums[i], 0) + count);
            }
        }
        return dp[n].getOrDefault(target, 0);
    }


    // V1-4
    // https://neetcode.io/problems/target-sum
    // IDEA: Dynamic Programming (Space Optimized)
    public int findTargetSumWays_1_4(int[] nums, int target) {
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 1);

        for (int num : nums) {
            Map<Integer, Integer> nextDp = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : dp.entrySet()) {
                int total = entry.getKey();
                int count = entry.getValue();
                nextDp.put(total + num,
                        nextDp.getOrDefault(total + num, 0) + count);
                nextDp.put(total - num,
                        nextDp.getOrDefault(total - num, 0) + count);
            }
            dp = nextDp;
        }
        return dp.getOrDefault(target, 0);
    }


    // V2



}
