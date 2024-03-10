package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/house-robber-ii/

import java.util.Arrays;

public class HouseRobber2 {

    // V0
    // IDEA : DP, LC 198
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/house-robber-ii.py
    public int rob(int[] nums) {

        if (nums.length == 1) {
            return nums[0];
        }

        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }

        // Consider the scenario where the first and last houses are adjacent
        // so either
        //  -> we rob first and last 2 idx
        //  -> or rob 2nd and last idx
        int max1 = robRange(nums, 0, nums.length - 2);
        int max2 = robRange(nums, 1, nums.length - 1);

        return Math.max(max1, max2);
    }

    // NOTE !!! define robRange(int[] nums, int start, int end)
    // with start, end idx
    public int robRange(int[] nums, int start, int end) {
        int[] dp = new int[nums.length];
        dp[start] = nums[start];
        dp[start + 1] = Math.max(nums[start], nums[start + 1]);

        for (int i = start + 2; i <= end; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[end];
    }

    // V1
    // https://leetcode.com/problems/house-robber-ii/solutions/4844002/beats-100-simple-to-understand/
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
