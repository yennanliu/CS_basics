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
