package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/description/

import java.util.*;

public class maximizeTotalCostOfAlternatingSubarrays {

    // V0
    // TODO : implement, fix
//    public long maximumTotalCost(int[] nums) {
//
//        if (nums.length == 1){
//            //return Math.abs(nums[0]);
//            return nums[0];
//        }
//
//        if (nums.length == 2){
//            int v1 = nums[0] + nums[1];
//            int v2 = nums[0] - nums[1];
//            return Math.max(v1, v2);
//        }
//
//        Long res = 0L;
//
//        List<int []> cache = new ArrayList<>();
//        Queue<Integer> queue = new LinkedList<>();
//        for (int i = 0; i < nums.length; i++){
//            if (nums[i] < 0){
//                queue.add(i);
//            }
//        }
//
//        System.out.println("queue = " + queue); // ?
//
//        int j = 0;
//        while (!queue.isEmpty()){
//            Integer idx = queue.poll();
//            cache.add(Arrays.copyOfRange(nums, j, idx+1));
//            j = idx+1;
//        }
//
//        if (j < nums.length){
//            cache.add(Arrays.copyOfRange(nums, j, nums.length));
//        }
//
//        System.out.println("------>");
//        //System.out.println("cache = " + String.join("\n", cache)); // ?
//        cache.stream().forEach(x -> {System.out.println(Arrays.toString(x));});
//
//        for (int[] item : cache){
//            int cur = Arrays.stream(item).map(x -> Math.abs(x)).sum();
//            res += cur;
//        }
//
//        return res;
//    }

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/solutions/5355800/simple-dp-approach-memorization-java-c-python/
    public long maximumTotalCost_1(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }

        // Initial values for the dynamic programming approach
        long secondPrev = nums[0];
        long firstPrev = Math.max((long) nums[0] + nums[1], (long) nums[0] - nums[1]);

        for (int i = 2; i < nums.length; i++) {
            // Calculate the maximum cost for the subarray ending at the current index
            long current = Math.max(secondPrev + nums[i - 1] - nums[i], firstPrev + nums[i]);
            // Update the previous values for the next iteration
            secondPrev = firstPrev;
            firstPrev = current;
        }

        return firstPrev;
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/solutions/5355138/dynamic-programming-and-space-optimized-beats-100-easy-to-understand/
    public long maximumTotalCost_2(int[] nums) {
        int n = nums.length;
        long addResult = nums[0];
        long subResult = nums[0];
        for (int i = 1; i < n; i++) {
            long tempAdd = Math.max(addResult, subResult) + nums[i];
            long tempSub = addResult - nums[i];

            addResult = tempAdd;
            subResult = tempSub;
        }
        return Math.max(addResult, subResult);
    }

}
