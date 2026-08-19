package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/largest-divisible-subset/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  368. Largest Divisible Subset
 *  Medium
 *
 *  Given a set of distinct positive integers nums, return the largest subset
 *  answer such that every pair (answer[i], answer[j]) of elements in this
 *  subset satisfies:
 *
 *    answer[i] % answer[j] == 0, or
 *    answer[j] % answer[i] == 0
 *
 *  If there are multiple solutions, return any of them.
 *
 *  Example 1:
 *
 *  Input: nums = [1,2,3]
 *  Output: [1,2]
 *  Explanation: [1,3] is also accepted.
 *
 *  Example 2:
 *
 *  Input: nums = [1,2,4,8]
 *  Output: [1,2,4,8]
 *
 *  Constraints:
 *
 *  1 <= nums.length <= 1000
 *  1 <= nums[i] <= 2 * 10^9
 *  All the integers in nums are unique.
 */
public class LargestDivisibleSubset {

    // V0
    // IDEA: SORT + LIS-LIKE DP + PARENT POINTER BACKTRACK
    //  after sorting, nums[i] % nums[j] == 0 (j < i) means nums[j] can precede nums[i]
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public List<Integer> largestDivisibleSubset(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        Arrays.sort(nums);
        int n = nums.length;
        int[] dp = new int[n];   // dp[i] = size of best chain ending at i
        int[] prev = new int[n]; // predecessor index
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int bestIdx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > dp[bestIdx]) {
                bestIdx = i;
            }
        }

        int cur = bestIdx;
        while (cur != -1) {
            res.add(nums[cur]);
            cur = prev[cur];
        }
        Collections.reverse(res);
        return res;
    }
}
