package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/house-robber/description/

public class HouseRobber {

    // V0
    // IDEA : DP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/house-robber.py
    public int rob(int[] nums) {

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
         *   nums[i-1] : not rob nums[i]
         *
         */
        for (int i = 2; i < nums.length; i++){
            nums[i] = Math.max(nums[i-1], nums[i-2] + nums[i]);
        }

        return nums[nums.length-1];
    }

    // V0'
    // IDEA : DP
    public int rob_(int[] nums) {

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
