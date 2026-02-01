package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/house-robber-ii/
/**
 *  213. House Robber II
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed. All houses at this place are arranged in a circle. That means the first house is the neighbor of the last one. Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two adjacent houses were broken into on the same night.
 *
 * Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,2]
 * Output: 3
 * Explanation: You cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses.
 * Example 2:
 *
 * Input: nums = [1,2,3,1]
 * Output: 4
 * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
 * Total amount you can rob = 1 + 3 = 4.
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 1000
 *
 */

import java.util.Arrays;

public class HouseRobber2 {

    // V0
    // IDEA : DP, LC 198
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/house-robber-ii.py
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob(int[] nums) {

        if (nums.length == 1) {
            return nums[0];
        }

        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }

        // Consider the scenario where the first and last houses are adjacent (nums is a "circular" array)
        // so either
        //
        //  -> case 1) we rob first and last (2 idx in total) (e.g. 0 idx, and k-2 idx)
        //  -> case 2) or rob 2nd and last idx (e.g. 1 idx, and k-1 idx)

        // case 1)
        int max1 = robRange(nums, 0, nums.length - 2);
        // case 2)
        int max2 = robRange(nums, 1, nums.length - 1);

        return Math.max(max1, max2);
    }

    // NOTE !!! define robRange(int[] nums, int start, int end)
    // with start, end idx
    /**
     * time = O(N)
     * space = O(1)
     */
    public int robRange(int[] nums, int start, int end) {

        int[] dp = new int[nums.length];
        dp[start] = nums[start];
        dp[start + 1] = Math.max(nums[start], nums[start + 1]);

        for (int i = start + 2; i <= end; i++) {
            // still the same dp logic as LC 198
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[end];
    }

    // V0-1
    // IDEA: 1D DP + LC 198 (fixed by gemini)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_1(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int n = nums.length;
        if (n == 1)
            return nums[0];
        if (n == 2)
            return Math.max(nums[0], nums[1]);

        /**  CORE IDEA:
         *
         * -> by splitting to Scenario 1, and 2.
         *    we can reuse LC 198 logic
         *    and get the max from the 2 Scenario result
         *    as the final answer
         *
         *
         *    - Scenario 1: Rob houses [0, n-2] (NOT rob `the last` house)
         *                  (rob first, and n-1 house)
         *
         *
         *    - Scenario 2: Rob houses [1, n-1] (NOT rob `the first` house)
         *                   (rob second, and last house)
         */
        // Scenario 1: Rob houses [0, n-2] (Ignore the last house)
        int robFirst = robLinear(nums, 0, n - 2);

        // Scenario 2: Rob houses [1, n-1] (Ignore the first house)
        int robLast = robLinear(nums, 1, n - 1);

        return Math.max(robFirst, robLast);
    }

    // Standard House Robber I logic
    private int robLinear(int[] nums, int start, int end) {
        int prev2 = 0;
        int prev1 = 0;

        for (int i = start; i <= end; i++) {
            int current = Math.max(prev2 + nums[i], prev1);
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    // V0-2
    // IDEA: DP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_2(int[] nums) {

        if (nums == null || nums.length == 0) {
            return 0;
        }

        if (nums.length == 1) {
            return nums[0];
        }

        // Case 1: rob houses [0 .. n-2]
        int money1 = robLinear_0_2(nums, 0, nums.length - 2);

        // Case 2: rob houses [1 .. n-1]
        int money2 = robLinear_0_2(nums, 1, nums.length - 1);

        return Math.max(money1, money2);
    }

    private int robLinear_0_2(int[] nums, int start, int end) {
        int prev2 = 0; // dp[i-2]
        int prev1 = 0; // dp[i-1]

        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }



    // V0-3
    // IDEA : BRUTE FORCE (MODIFIED BY GPT)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/house-robber-ii.py
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_0_3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int prevMax = 0;
        int currMax = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            int tmp = currMax;
            currMax = Math.max(currMax, prevMax + nums[i]);
            prevMax = tmp;
        }
        int rec = currMax;

        prevMax = 0;
        currMax = 0;
        for (int i = nums.length - 1; i > 0; i--) {
            int tmp = currMax;
            currMax = Math.max(currMax, prevMax + nums[i]);
            prevMax = tmp;
        }
        return Math.max(rec, currMax);
    }

    // V1
    // https://leetcode.com/problems/house-robber-ii/solutions/4844002/beats-100-simple-to-understand/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int robber(int[] nums) {
        int n=nums.length;
        int prev=nums[0];
        int prev2=0;
        for(int i=1;i<n;i++){
            int pick=nums[i];
            if(i>1) pick+=prev2;

            int notpick=prev;
            int curr=Math.max(pick, notpick);
            prev2=prev;
            prev=curr;
        }
        return prev;
    }
    /**
     * time = O(N)
     * space = O(1)
     */
    public int rob_2(int[] nums) {
        int n=nums.length;
        if(n==1) return nums[0];
        int[] ind1=new int[n-1];
        int[] ind2=new int[n-1];
        for(int i=0;i<n-1;i++){
            ind1[i]=nums[i+1];
        }
        for(int i=0;i<n-1;i++){
            ind2[i]=nums[i];
        }
        int a=robber(ind1);
        int b=robber(ind2);
        return Math.max(a, b);
    }


    // V2
    // https://leetcode.com/problems/house-robber-ii/solutions/4055922/beautiful-hashmap-handling-subproblems/
//    public int rob_3(int[] nums) {
//        int ind=nums.length-1;
//        HashMap<Pair,Integer> map=new HashMap<>();
//        if(ind==0)
//            return nums[0];
//        else
//            return Math.max(f(ind-1,nums,0,map),f(ind,nums,nums[ind],map));
//    }
//    public static int f(int i,int nums[],int curr,HashMap<Pair,Integer> map){
//        Pair key=new Pair(i,curr);
//        if(map.containsKey(key))
//            return map.get(key);
//        if(i<0)
//            return 0;
//        if(i==0){
//            if(curr==0)
//                return nums[0];
//            else
//                return 0;
//        }
//        int take=f(i-2,nums,curr,map)+nums[i];
//        int nottake=f(i-1,nums,curr,map);
//        map.put(key,Math.max(take,nottake));
//        return Math.max(take,nottake);
//    }




}
