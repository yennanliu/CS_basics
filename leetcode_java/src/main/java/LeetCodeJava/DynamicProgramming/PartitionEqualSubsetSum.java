package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/partition-equal-subset-sum/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 416. Partition Equal Subset Sum
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return true if you can partition the array into two subsets such that the sum of the elements in both subsets is equal or false otherwise.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,11,5]
 * Output: true
 * Explanation: The array can be partitioned as [1, 5, 5] and [11].
 * Example 2:
 *
 * Input: nums = [1,2,3,5]
 * Output: false
 * Explanation: The array cannot be partitioned into equal sum subsets.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 100
 *
 *
 */
public class PartitionEqualSubsetSum {

    // V0
    // TODO: implement with below idea (optimized brute force)
    // https://youtu.be/IsvocB5BJhw?si=evPYANn0pPicVwu6
//    public boolean canPartition(int[] nums) {
//
//    }



    // V0-1
    // IDEA: Optimal 1D DP Solution (Subset Sum, O(n × target)) (GPT)
    /**  NOTE !!! CORE
     *
     *  1. is there a subset of nums that sums to totalSum / 2 ?
     *
     *     (since odd number is NOT possible to split into 2 val)
     *       -> This is a classic 0/1 knapsack subset-sum problem.
     *
     *  2. dp definition: !!!!
     *
     *     dp[i] = true if there is a subset of nums that sums to i
     *
     *
     *  3. dp definition: !!!!
     *
     *    ```
     *    for i from target down to num:
     *
     *      # `|=` is `or` operator
     *      #dp[i] |= dp[i - num]
     *
     *      dp[i] or dp[i - num]
     *    ```
     *
     */
    /**
     * Here’s a clear, correct Java solution for **LeetCode 416 — Partition Equal Subset Sum**.
     *
     * 👉 Problem: [https://leetcode.com/problems/partition-equal-subset-sum/](https://leetcode.com/problems/partition-equal-subset-sum/)
     *
     * ---
     *
     * ## 🧠 Problem Summary
     *
     * You are given a non-negative integer array `nums`.
     * Return **true** if you can split it into **two subsets** with equal sums.
     *
     * In other words:
     *
     * > Is there a subset whose sum is exactly **half of total sum**?
     *
     * If total sum is **odd**, it’s impossible.
     *
     * ---
     *
     * ## ✅ Optimal DP Solution (Subset Sum, `O(n × target)`)
     *
     * We reduce the problem to:
     *
     * ```
     * is there a subset of nums that sums to totalSum / 2?
     * ```
     *
     * This is a **classic 0/1 knapsack subset-sum problem**.
     *
     * ---
     *
     * ## 🧠 DP Idea (1D)
     *
     * Let:
     *
     * ```
     * target = totalSum / 2
     * ```
     *
     * Use a boolean DP array:
     *
     * ```
     * dp[i] = true if there is a subset of nums that sums to i
     * ```
     *
     * Initialize:
     *
     * ```
     * dp[0] = true  // empty set sums to 0
     * ```
     *
     * For each num:
     *
     * ```
     * for i from target down to num:
     *     dp[i] |= dp[i - num]
     * ```
     *
     * We iterate backwards to ensure each number is only used once.
     *
     *
     * ---
     *
     * ## 🧠 Why It Works
     *
     * We’re checking whether **some subset of `nums` sums to `target`**.
     *
     * * `dp[j]` means there is a subset summing to `j`.
     * * When we process a number `num`, we update possible sums downwards so we don’t reuse a number more than once.
     *
     * ---
     *
     * ## 🧪 Example
     *
     * ```
     * Input: nums = [1, 5, 11, 5]
     * Total sum = 22 → target = 11
     * dp[0] = true
     * Process 1: dp[1] = true
     * Process 5: dp[5] = true, dp[6] = true
     * Process 11: dp[11] = dp[6] (true)
     * Return true
     * ```
     *
     * ---
     *
     * ## ⏱ Complexity
     *
     * | Aspect | Value             |
     * | ------ | ----------------- |
     * | Time   | **O(n × target)** |
     * | Space  | **O(target)**     |
     *
     * Works well within constraints.
     *
     * ---
     *
     * ## 🧠 Alternate View (2D DP)
     *
     * If you want the classic `dp[i][j]`, here it is:
     *
     * ```
     * dp[i][j] = whether we can reach sum j using first i numbers
     * ```
     *
     * But 1D is enough and more efficient.
     *
     */
    public boolean canPartition_0_1(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }

        // edge
        // if total sum is odd, cannot partition evenly
        if (total % 2 != 0) {
            return false;
        }

        /** NOTE !!!
         *
         *  target is `total / 2`
         */
        int target = total / 2;

        /** NOTE !!!  DP definition:
         *
         * `dp[j]`
         *    - means if there is a `subset` summing to `j`.
         *          When we process a number `num`,
         *     we update possible sums `downwards`
         *     so we don’t reuse a number more than once.
         *
         */
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;


        /**  NOTE !!! DP equation:
         *
         *    ```
         *    for i from target down to num:
         *
         *      # `|=` is `or` operator
         *      #dp[i] |= dp[i - num]
         *
         *      dp[i] or dp[i - num]
         *    ```
         */
        for (int num : nums) {
            // NOTE !!!  we loop from target, and sum --
            //           for the inner loop
            for (int sum = target; sum >= num; sum--) {
                dp[sum] = dp[sum] || dp[sum - num];
            }
        }

        return dp[target];
    }


    // V0-2
    // IDEA: (Optimized 1D DP) (GEMINI)
    /**
     * To solve **LeetCode 416: Partition Equal Subset Sum**,
     *
     * -> the core insight is to realize that the problem is a
     *    variation of the classic `**0/1 Knapsack Problem**.`
     *
     * ### The Logic
     *
     * 1. **Check the Sum:** If the total sum of all elements is **odd**, it is mathematically impossible to split them into two equal integer subsets. Return `false` immediately.
     * 2. **The Target:** If the sum is even, your goal is to find if there exists a subset of numbers that adds up to exactly **Half of the Total Sum** (`sum / 2`).
     * 3. **The DP State:** We use a boolean array `dp` where `dp[i]` is `true` if a sum of `i` can be achieved using a subset of the numbers processed so far.
     * 4. **Reverse Iteration:** When updating the `dp` array for each number, we must iterate **backwards** (from `target` down to `num`). This ensures each number is only used **once**. If we went forward, we would accidentally allow a single number to be reused multiple times (like in the Coin Change problem).
     *
     *
     * ### Complexity Analysis
     *
     * * **Time Complexity:** , where  is the number of elements and  is half the total sum.
     * * **Space Complexity:**  to store the 1D DP array.
     *
     */
    public boolean canPartition_0_2(int[] nums) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // 1. If sum is odd, we cannot partition it into two equal integers
        if (totalSum % 2 != 0) {
            return false;
        }

        int target = totalSum / 2;

        // 2. dp[i] will be true if sum i can be formed by a subset
        boolean[] dp = new boolean[target + 1];

        // 3. Base case: sum 0 is always possible (empty subset)
        dp[0] = true;

        // 4. Iterate through each number in the input
        for (int num : nums) {
            // Traverse backwards to ensure each number is used ONLY ONCE
            for (int i = target; i >= num; i--) {
                // If sum (i - num) was possible, then sum i is now possible
                if (dp[i - num]) {
                    dp[i] = true;
                }
            }

            /** NOTE !!!
             *
             *  Early exit
             */
            // Early exit: if we reached our target, no need to check further
            if (dp[target])
                return true;
        }

        return dp[target];
    }


    // V0-3
    // IDEA: 2D DP (gpt)
    public boolean canPartition_0_3(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }

        // If total sum is odd, cannot split evenly
        if (total % 2 != 0) {
            return false;
        }

        int target = total / 2;
        int n = nums.length;

        boolean[][] dp = new boolean[n + 1][target + 1];

        // base case
        dp[0][0] = true;

        for (int i = 1; i <= n; i++) {
            int cur = nums[i - 1];
            for (int j = 0; j <= target; j++) {
                // not take nums[i-1]
                dp[i][j] = dp[i - 1][j];

                // take nums[i-1] if possible
                if (j >= cur) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - cur];
                }
            }
        }

        return dp[n][target];
    }



    // V0-4
    // IDEA: DP
    // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc/lc_416_1.png

    // V0-5
    // IDEA: DP (fixed by gpt)
    public boolean canPartition_0_5(int[] nums) {
        if (nums == null || nums.length < 2)
            return false;

        int total = 0;
        for (int n : nums)
            total += n;

        if (total % 2 != 0)
            return false;

        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }

        return dp[target];
    }

    // V1-1
    // https://neetcode.io/problems/partition-equal-subset-sum
    // IDEA: RECURSION
    public boolean canPartition_1_1(int[] nums) {
        int n = nums.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
        }
        if (sum % 2 != 0) {
            return false;
        }

        return dfs(nums, 0, sum / 2);
    }

    public boolean dfs(int[] nums, int i, int target) {
        if (i == nums.length) {
            return target == 0;
        }
        if (target < 0) {
            return false;
        }

        return dfs(nums, i + 1, target) ||
                dfs(nums, i + 1, target - nums[i]);
    }

    // V1-2
    // https://neetcode.io/problems/partition-equal-subset-sum
    // IDEA: Dynamic Programming (Top-Down)
    Boolean[][] memo;
    public boolean canPartition_1_2(int[] nums) {
        int n = nums.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
        }
        if (sum % 2 != 0) {
            return false;
        }
        memo = new Boolean[n][sum / 2 + 1];

        return dfs_1_2(nums, 0, sum / 2);
    }

    public boolean dfs_1_2(int[] nums, int i, int target) {
        if (i == nums.length) {
            return target == 0;
        }
        if (target < 0) {
            return false;
        }
        if (memo[i][target] != null) {
            return memo[i][target];
        }

        memo[i][target] = dfs_1_2(nums, i + 1, target) ||
                dfs_1_2(nums, i + 1, target - nums[i]);
        return memo[i][target];
    }


    // V1-3
    // https://neetcode.io/problems/partition-equal-subset-sum
    // IDEA: Dynamic Programming (Bottom-Up)
    public boolean canPartition_1_3(int[] nums) {
        int n = nums.length;
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) {
            return false;
        }

        int target = sum / 2;
        boolean[][] dp = new boolean[n + 1][target + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                if (nums[i - 1] <= j) {
                    dp[i][j] = dp[i - 1][j] ||
                            dp[i - 1][j - nums[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[n][target];
    }

    // V1-4
    // https://neetcode.io/problems/partition-equal-subset-sum
    // IDEA: Dynamic Programming (Space Optimized)
    public boolean canPartition_1_4(int[] nums) {
        if (sum(nums) % 2 != 0) {
            return false;
        }

        int target = sum(nums) / 2;
        boolean[] dp = new boolean[target + 1];
        boolean[] nextDp = new boolean[target + 1];

        dp[0] = true;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j >= nums[i]) {
                    nextDp[j] = dp[j] || dp[j - nums[i]];
                } else {
                    nextDp[j] = dp[j];
                }
            }
            boolean[] temp = dp;
            dp = nextDp;
            nextDp = temp;
        }

        return dp[target];
    }

    private int sum(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        return total;
    }


    // V1-5
    // https://neetcode.io/problems/partition-equal-subset-sum
    // IDEA: Dynamic Programming (Hash Set)
    public boolean canPartition_1_5(int[] nums) {
        if (Arrays.stream(nums).sum() % 2 != 0) {
            return false;
        }

        Set<Integer> dp = new HashSet<>();
        dp.add(0);
        int target = Arrays.stream(nums).sum() / 2;

        for (int i = nums.length - 1; i >= 0; i--) {
            Set<Integer> nextDP = new HashSet<>();
            for (int t : dp) {
                if (t + nums[i] == target) {
                    return true;
                }
                nextDP.add(t + nums[i]);
                nextDP.add(t);
            }
            dp = nextDP;
        }
        return false;
    }


    // V2



}
