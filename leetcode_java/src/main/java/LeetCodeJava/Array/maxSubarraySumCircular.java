package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-sum-circular-subarray/

import java.util.ArrayList;
import java.util.List;

/**
 * 918. Maximum Sum Circular Subarray
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a circular integer array nums of length n, return the maximum possible sum of a non-empty subarray of nums.
 *
 * A circular array means the end of the array connects to the beginning of the array. Formally, the next element of nums[i] is nums[(i + 1) % n] and the previous element of nums[i] is nums[(i - 1 + n) % n].
 *
 * A subarray may only include each element of the fixed buffer nums at most once. Formally, for a subarray nums[i], nums[i + 1], ..., nums[j], there does not exist i <= k1, k2 <= j with k1 % n == k2 % n.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-2,3,-2]
 * Output: 3
 * Explanation: Subarray [3] has maximum sum 3.
 * Example 2:
 *
 * Input: nums = [5,-3,5]
 * Output: 10
 * Explanation: Subarray [5,5] has maximum sum 5 + 5 = 10.
 * Example 3:
 *
 * Input: nums = [-3,-2,-3]
 * Output: -2
 * Explanation: Subarray [-2] has maximum sum -2.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 3 * 104
 * -3 * 104 <= nums[i] <= 3 * 104
 *
 *
 *
 */
public class maxSubarraySumCircular {

    // V0
    // TODO: fix below
//    public int maxSubarraySumCircular(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//        // double array -> to simulate `circular` array
//        List<Integer> tmp = new ArrayList<>();
////        for(int i = 0; i < 2; i++){
////            for(int j = 0; j < nums.length; j++){
////                tmp.add(nums[j]);
////            }
////        }
//        for(int i = 0; i < nums.length; i++){
//            tmp.add(nums[i]);
//        }
//        // NOTE !!! also append `last` element in nums
//        tmp.add(nums[nums.length - 1]);
//        // list to array
//        Integer[] nums_ = tmp.toArray(new Integer[tmp.size()]);
//
//
//        System.out.println(">>> nums_ = " + nums_);
//
//        // kadane algo
//        int localMax = nums_[0];
//        int globalMax = nums_[0];
//        for(int i = 0; i < nums_.length; i++){
//            localMax = Math.max(nums_[i], localMax + nums_[i]);
//            globalMax = Math.max(globalMax, localMax);
//        }
//
//        return globalMax;
//    }

    // V0-1
    // IDEA: (GEMINI)
    /**
     *
     * Explanation of Changes:
     *
     * 1) Removed Redundant Code:
     *
     * The commented-out code for doubling the array was unnecessary and incorrect for handling circular subarrays.
     *
     * 2_ Optimized Calculation:
     *
     * The code now calculates both the maximum subarray sum (max_sum) and the minimum subarray sum (min_sum) in a single pass through the array.
     * This is more efficient than creating a new array and running Kadane's algorithm twice.
     *
     * 3) Circular Sum Handling:
     *
     * - The code correctly handles the circular sum by checking if max_sum is greater than 0.
     * If max_sum is greater than 0, it means there is a non-circular subarray with a positive sum.
     * - In this case, we need to consider the possibility of a circular subarray that includes the end and beginning of the original array.
     * - The sum of the circular subarray can be calculated as total_sum - min_sum.
     * Finally, the code returns the maximum of max_sum and total_sum - min_sum.
     *
     *
     * Key Points:
     *
     * This improved solution effectively calculates the maximum sum of a subarray in a circular array.
     * It is more efficient and concise than the original code.
     *
     */
    public int maxSubarraySumCircular_0_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int max_sum = Integer.MIN_VALUE;
        int min_sum = Integer.MAX_VALUE;
        int total_sum = 0;
        int curr_max = 0;
        int curr_min = 0;

        for (int i = 0; i < n; i++) {
            total_sum += nums[i];
            curr_max = Math.max(nums[i], curr_max + nums[i]);
            max_sum = Math.max(max_sum, curr_max);

            curr_min = Math.min(nums[i], curr_min + nums[i]);
            min_sum = Math.min(min_sum, curr_min);
        }

        return max_sum > 0 ? Math.max(max_sum, total_sum - min_sum) : max_sum;
    }

    // V0-2
    // IDEA: Kadane algo (gpt)
    public int maxSubarraySumCircular(int[] nums) {
        int totalSum = 0;
        int maxSum = nums[0], curMax = 0;
        int minSum = nums[0], curMin = 0;

        for (int num : nums) {
            totalSum += num;

            curMax = Math.max(curMax + num, num);
            maxSum = Math.max(maxSum, curMax);

            curMin = Math.min(curMin + num, num);
            minSum = Math.min(minSum, curMin);
        }

        // Handle the case where all numbers are negative
        if (maxSum < 0)
            return maxSum;

        return Math.max(maxSum, totalSum - minSum);
    }


    // V1
    // https://www.youtube.com/watch?v=fxT9KjakYPM
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0918-maximum-sum-circular-subarray.java
    public int maxSubarraySumCircular_1(int[] nums) {
        int curMax = 0, curMin = 0;
        int globMax = nums[0], globMin = nums[0];
        int total = 0;
        for (int n : nums) {
            curMax = Math.max(curMax + n, n);
            curMin = Math.min(curMin + n, n);
            total += n;
            globMax = Math.max(curMax, globMax);
            globMin = Math.min(curMin, globMin);
        }
        return globMax > 0 ? Math.max(globMax, total - globMin) : globMax;
    }

    // V2-1
    // https://leetcode.com/problems/maximum-sum-circular-subarray/editorial/
    // IDEA: Enumerate prefix and suffix sums
    public int maxSubarraySumCircular_2_1(int[] nums) {
        final int n = nums.length;
        final int[] rightMax = new int[n];
        rightMax[n - 1] = nums[n - 1];
        int suffixSum = nums[n - 1];

        for (int i = n - 2; i >= 0; --i) {
            suffixSum += nums[i];
            rightMax[i] = Math.max(rightMax[i + 1], suffixSum);
        }

        int maxSum = nums[0];
        int specialSum = nums[0];
        int curMax = 0;
        for (int i = 0, prefixSum = 0; i < n; ++i) {
            // This is Kadane's algorithm.
            curMax = Math.max(curMax, 0) + nums[i];
            maxSum = Math.max(maxSum, curMax);

            prefixSum += nums[i];
            if (i + 1 < n) {
                specialSum = Math.max(specialSum, prefixSum + rightMax[i + 1]);
            }
        }

        return Math.max(maxSum, specialSum);
    }

    // V2-2
    // https://leetcode.com/problems/maximum-sum-circular-subarray/editorial/
    // IDEA: Calculate the "Minimum Subarray"
    public int maxSubarraySumCircular_2_2(int[] nums) {
        int curMax = 0;
        int curMin = 0;
        int maxSum = nums[0];
        int minSum = nums[0];
        int totalSum = 0;

        for (int num : nums) {
            // Normal Kadane's
            curMax = Math.max(curMax, 0) + num;
            maxSum = Math.max(maxSum, curMax);

            // Kadane's but with min to find minimum subarray
            curMin = Math.min(curMin, 0) + num;
            minSum = Math.min(minSum, curMin);

            totalSum += num;
        }

        if (totalSum == minSum) {
            return maxSum;
        }

        return Math.max(maxSum, totalSum - minSum);
    }


}
