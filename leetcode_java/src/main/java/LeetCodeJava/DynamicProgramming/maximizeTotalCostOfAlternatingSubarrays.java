package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-total-cost-of-alternating-subarrays/description/

import java.util.*;

/**
 * 3196. Maximize Total Cost of Alternating Subarrays
 * Medium
 *
 * You are given an integer array nums with length n.
 *
 * The cost of a subarray nums[l..r], where 0 <= l <= r < n, is defined as:
 *
 * cost(l, r) = nums[l] - nums[l + 1] + ... + nums[r] * (−1)^r − l
 *
 * Your task is to split nums into subarrays such that the total cost of the subarrays is maximized, ensuring each element belongs to exactly one subarray.
 *
 * Formally, if nums is split into k subarrays, where k > 1, at indices i_1, i_2, ..., i_k − 1, where 0 <= i_1 < i_2 < ... < i_k - 1 < n - 1, then the total cost will be:
 *
 * cost(0, i_1) + cost(i_1 + 1, i_2) + ... + cost(i_k − 1 + 1, n − 1)
 *
 * Return an integer denoting the maximum total cost of the subarrays after splitting the array optimally.
 *
 * Note: If nums is not split into subarrays, i.e. k = 1, the total cost is simply cost(0, n - 1).
 *
 * Example 1:
 *
 * Input: nums = [1,-2,3,4]
 * Output: 10
 * Explanation:
 *
 * One way to maximize the total cost is by splitting [1, -2, 3, 4] into subarrays [1, -2, 3] and [4]. The total cost will be (1 + 2 + 3) + 4 = 10.
 *
 * Example 2:
 *
 * Input: nums = [1,-1,1,-1]
 * Output: 4
 * Explanation:
 *
 * One way to maximize the total cost is by splitting [1, -1, 1, -1] into subarrays [1, -1] and [1, -1]. The total cost will be (1 + 1) + (1 + 1) = 4.
 *
 * Example 3:
 *
 * Input: nums = [0]
 * Output: 0
 * Explanation:
 *
 * We cannot split the array further, so the answer is 0.
 *
 * Example 4:
 *
 * Input: nums = [1,-1]
 * Output: 2
 * Explanation:
 *
 * Selecting the whole array gives a total cost of 1 + 1 = 2, which is the maximum.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
