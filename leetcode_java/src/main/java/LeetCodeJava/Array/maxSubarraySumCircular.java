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


    // V1

    // V2
}
