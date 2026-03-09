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
    public int rob_1_99(int[] nums) {
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


    // LC 213
    // 7.52 - 8.02 am
    /**
     *
     *  Given an integer array nums representing the amount of money of each house,
     *
     *  arranged in a circle.
     *
     *  -> return the maximum amount of money you can rob
     *  tonight without alerting the police.
     *
     *
     *  ------------------
     *
     *      *   IDEA 2) DP ????
     *      *
     *      *     - DP definition
     *      *        dp[i] = max money that robber can rob at idx = i
     *      *
     *      *     - DP eq
     *              2 cases
     *               1. rob at idx = 0
     *
     *                  (for idx in 0 to n-2)  // ????
     *                  dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *               2. rob at idx = n -1
     *
     *                  (for idx in 2 to n-1)
     *                  dp[i] = max( dp[i-2] + nums[i], dp[i-1] )
     *
     *
     *
     *  ------------------
     *
     *
     */
    public int rob(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        // ??
        // dp: rob idx = 0, but NOT rob idx = n-1
        int[] dp = new int[nums.length]; // ???

        // dp2: NOT rob idx = 0, but rob idx = n-1
        int[] dp2 = new int[nums.length]; // ???

        dp[0] = nums[0]; // ??
        //dp[1] = 0;
        // NOTE !!! below
        dp[1] = Math.max(nums[0], nums[1]);

        dp2[0] = 0;
        dp2[1] = nums[1];

        int globalMax = 0;

        for(int i = 2; i < nums.length; i++){
            dp[i] = Math.max( dp[i-2] + nums[i], dp[i-1] );

            globalMax = Math.max(dp[i], Math.max(globalMax, nums[i]));
        }


        // ????
        for(int i = 2; i < nums.length - 1; i++){
            dp2[i] = Math.max( dp2[i-2] + nums[i], dp2[i-1] );

            globalMax = Math.max(dp2[i], Math.max(globalMax, nums[i]));
        }


        // /?????
       // return Math.max(dp[nums.length - 2], dp2[nums.length - 1]);
        return globalMax;

    }


    // LC 437
    // 8.45 - 55 am
    /**
     *  -> return the number of paths
     *  where the sum of the values
     *  along the path equals targetSum.
     *
     *
     *
     *  -------------
     *
     *  IDEA 1) DFS (post order traverse)
     *
     *  IDEA 2) DFS (post order traverse) + prefix sum + hashmap ???????
     *
     *
     */
    int nodeCnt = 0; // ???
    // ????
    Map<Integer, Integer> map = new HashMap<>(); // { prefix_sum : cnt }
    public int pathSum(TreeNode root, int targetSum) {
        // edge
        if(root == null){
            return 0;
        }

        nodeSumDfs(root, targetSum, 0);
        return nodeCnt;
    }

    private void nodeSumDfs(TreeNode root, int targetSum, int curSum){
        if(root == null){
            return;
        }

        nodeSumDfs(root.left, targetSum, curSum);
        nodeSumDfs(root.right, targetSum, curSum);

        // ???
        curSum += root.val;
        // ???

        if(root.val == targetSum){
            nodeCnt += 1;
        }

        // ???
        if(map.containsKey(targetSum - curSum)){
            nodeCnt += map.get(targetSum - curSum);
        }

        // update map
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);


    }



}
