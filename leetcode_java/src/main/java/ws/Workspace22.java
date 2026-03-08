package ws;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;


public class Workspace22 {

    // LC 198
    // 7.33 - 43 am
    /**
     *  -> Given an integer array nums representing
     *  the amount of money of each house,
     *  return the maximum amount of money you can rob
     *  tonight without alerting the police.
     *
     *   NOTE:
     *
     *     it will automatically contact the police if
     *     two adjacent houses were broken into on the same night.
     *
     *
     *  -------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP ????
     *
     *     - DP definition
     *        dp[i] = max money that robber can rob at idx = i
     *
     *     - DP eq
     *         can rob cur idx (idx=i) or not
     *         dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *
     *  -------------------
     *
     *
     */
    //  IDEA 2) DP ????
    public int rob(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
//        if(nums.length == 1){
//            return nums[0];
//        }

        // ??
        int[] dp = new int[nums.length]; // ???
        dp[0] = nums[0]; // ??
        dp[1] = Math.max(nums[0], nums[1]); // ??

        for(int i = 2; i < nums.length; i++){
            // dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
            dp[i] = Math.max( dp[i-2] + nums[i], dp[i-1] );
        }

        return dp[nums.length - 1];
    }




}
