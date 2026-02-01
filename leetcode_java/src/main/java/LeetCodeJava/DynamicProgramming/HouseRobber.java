package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/house-robber/description/
/**
 * 198. House Robber
 * Solved
 * Medium
 * Topics
 * Companies
 * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.
 *
 * Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 * Total amount you can rob = 1 + 3 = 4.
 * Example 2:
 *
 * Input: nums = [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
 * Total amount you can rob = 2 + 9 + 1 = 12.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 400
 *
 */
public class HouseRobber {

    // V0
    // IDEA: DP (fixed by gemini)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int n = nums.length;
        if (n == 1)
            return nums[0];
        if (n == 2)
            return Math.max(nums[0], nums[1]);

        // dp[i] represents the max money robbed up to house i
        int[] dp = new int[n];

        // Base cases
        dp[0] = nums[0];
        /**  NOTE !!!
         *
         *  this dp[1] should be MAX of nums[0], nums[1].
         *
         *  (but NOT nums[1])
         */
        dp[1] = Math.max(nums[0], nums[1]);

        /**  NOTE !!!
         *
         *  we end at `n idx`
         *
         *  (but NOT `n+1` idx)
         */
        for (int i = 2; i < n; i++) {
            // Choice 1: Rob current house (must add money from 2 houses ago)
            // Choice 2: Skip current house (carry over money from 1 house ago)
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }

        return dp[n - 1];
    }


    // V0-1
    // IDEA : DP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/house-robber.py
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_1(int[] nums) {

        if (nums == null || nums.length == 0){
            return 0;
        }

        if (nums.length < 2){
            return nums[0];
        }

        // double check
        nums[1] = Math.max(nums[0], nums[1]);
        /** NOTE !!!
         *
         *  DP Logic
         *
         *   nums[i-2] + nums[i] : rob nums[i]
         *
         *   nums[i-1] : NOT rob at nums[i]
         *
         */
        for (int i = 2; i < nums.length; i++){
            nums[i] = Math.max(nums[i-1], nums[i-2] + nums[i]);
        }

        return nums[nums.length-1];
    }

    // V0-2
    // IDEA: DP (GPT)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_2(int[] nums) {
        // Edge cases
        if (nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);

        // DP array
        int[] dp = new int[nums.length];

        // Base cases
        dp[0] = nums[0]; // First house
        dp[1] = Math.max(nums[0], nums[1]); // Pick max of first two houses

        // DP transition
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }

        return dp[nums.length - 1]; // Return the last computed value
    }

    // V0-3
    // IDEA : DP
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_3(int[] nums) {

        if (nums.length <= 3){

            if (nums.length == 0){
                return 0;
            }

            if (nums.length == 1){
                return nums[0];
            }

            if (nums.length == 2){
                return Math.max(nums[0], nums[1]);
            }

            return Math.max(nums[0]+nums[2], nums[1]);
        }

        // init dp
        int[] dp = new int[nums.length];

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]); // NOTE !!! dp[1] comes from biggest option (which is Math.max(nums[0], nums[1]))

        // 1, 2, 3, ... k-2, k-1, k
        // dp[k] = max(dp(k-2) + k, dp(k-1))
        /**
         *  2,1,1,2
         *
         *  -> dp[0] = 2
         *  -> dp[1] = 2
         *
         *  -> dp[2] = max(dp[0]+nums[2], dp[1]) = 3
         *  -> dp[3] = max(dp[1]+nums[3], dp[2]) = 4
         *
         */
        for (int i = 2; i < nums.length; i++){
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1]);
        }

        return dp[nums.length-1];
    }

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/house-robber/solutions/4600148/beats-100-c-java-python-js-explained-with-video-dynamic-programming-space-optimized/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_1(int[] nums) {
        int rob = 0;
        int norob = 0;
        for (int i = 0; i < nums.length; i ++) {
            int newRob = norob + nums[i];
            int newNoRob = Math.max(norob, rob);
            rob = newRob;
            norob = newNoRob;
        }
        return Math.max(rob, norob);
    }




}
