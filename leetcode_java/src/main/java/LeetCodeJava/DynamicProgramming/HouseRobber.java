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
