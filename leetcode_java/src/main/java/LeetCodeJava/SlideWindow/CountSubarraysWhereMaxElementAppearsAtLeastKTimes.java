package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 2962. Count Subarrays Where Max Element Appears at Least K Times
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer array nums and a positive integer k.
 *
 * Return the number of subarrays where the maximum element of nums appears at least k times in that subarray.
 *
 * A subarray is a contiguous sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,3,3], k = 2
 * Output: 6
 * Explanation: The subarrays that contain the element 3 at least 2 times are: [1,3,2,3], [1,3,2,3,3], [3,2,3], [3,2,3,3], [2,3,3] and [3,3].
 * Example 2:
 *
 * Input: nums = [1,4,2,1], k = 3
 * Output: 0
 * Explanation: No subarray contains the element 4 at least 3 times.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 106
 * 1 <= k <= 105
 *
 */
public class CountSubarraysWhereMaxElementAppearsAtLeastKTimes {

    // V0
//    public long countSubarrays(int[] nums, int k) {
//
//    }

    // V0-0-1
    // TODO: fix below
    /**  NOTE !!! below code is WRONG
     *
     * ---
     *
     *  Reason:
     *
     *
     * Your current code is incorrect because:
     *
     * ❌ Issues in your attempt
     * 	•	You rebuild cntMap from scratch in every iteration, so you never track the real window.
     * 	•	isValid() checks “any value count ≥ k” → but LC 2962 requires:
     *
     * ✔ Count subarrays where the maximum element in the entire array appears ≥ k times
     *
     * The max element is fixed:
     *
     *        `maxNum = max(nums)`
     *
     *
     * We only count how many times maxNum appears in the sliding window.
     *
     *
     */
//    public long countSubarrays(int[] nums, int k) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0L; // ???
//        }
//        if(nums.length == 1){
////            if(k == 1){
////                return 1;
////            }
////            return 0;
//            return k;
//        }
//
//        int cnt = 0;
//        int l = 0;
//
//        // slide window
//        /**
//         *      *     -> when the sub array meet the condition (e.g. cnt >= k)
//         *      *        -> move left pointer to right, till it NOT meet the condition
//         *      *        -> then move right pointer again
//         *      *        -> repeat above ...
//         *      *
//         */
//        Map<Integer, Integer> cntMap = new HashMap<>();
//        for(int r = 0; r < nums.length; r++){
//            // cnt nums[r] (e.g. add nums[r] to map)
//            //Map<Integer, Integer> cntMap = new HashMap<>();
//            int curVal = nums[r];
//            cntMap.put(curVal, cntMap.getOrDefault(curVal, 0) + 1);
//            // move left pointer while the sub array is still VALID
//            while(isValid(cntMap, k)){
//                int toReduce = nums[l];
//                cntMap.put(toReduce, cntMap.get(toReduce) - 1);
//                l += 1;
//                cnt += 1;
//            }
//            // cnt += 1;
//        }
//
//        return cnt;
//    }
//
//    private boolean isValid(Map<Integer, Integer> cntMap, int k){
//        // edge
//        if(cntMap.isEmpty() || k == 0){
//            return false; // ???
//        }
//        int max = 0;
//        for(int valCnt: cntMap.values()){
////            if(valCnt < k){
////                return false;
////            }
//            max = Math.max(max, valCnt);
//        }
//
//        return max >= k;
//    }



    // V0-1
    // IDEA: SLIDE WINDOW (fixed by gpt)
    public long countSubarrays_0_1(int[] nums, int k) {
        int n = nums.length;
        int maxNum = 0;

        /** NOTE !!!
         *
         *  need to find `max element` from nums first
         */
        // find the max element
        for (int v : nums) {
            maxNum = Math.max(maxNum, v);
        }

        long ans = 0;
        int l = 0;
        /** NOTE !!!
         *
         * cnt: count of maxNum in window
         */
        int cnt = 0; // count of maxNum in window

        for (int r = 0; r < n; r++) {
            if (nums[r] == maxNum) {
                cnt++;
            }

            /** NOTE !!!
             *
             * slide window template:
             *
             *   for(int r = 0; r < nums.len; r++){
             *      while(condition){
             *           // do sth
             *           l += 1;
             *        }
             *      // do sth
             *   }
             */
            // window becomes valid when cnt >= k
            while (cnt >= k) {
                /**
                 *  NOTE !!!
                 *
                 *  // all future subarrays including r are valid
                 *
                 */
                ans += (n - r); // all future subarrays including r are valid
                /**
                 *  NOTE !!!
                 *
                 *    if the `left pointer` val == maxNum,
                 *    we will move left pointer (shrink the left boundary)
                 *    and since we shrink left boundary, we NEED to deduct the cnt by 1
                 *    (e.g. cnt -= 1)
                 *
                 */
                if (nums[l] == maxNum) {
                    cnt--;
                }
                l++;
            }
        }

        return ans;
    }


    // V1-1
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times/editorial/
    public long countSubarrays_1_1(int[] nums, int k) {
        int maxElement = Arrays.stream(nums).max().getAsInt();
        long ans = 0, start = 0;
        int maxElementsInWindow = 0;

        for (int end = 0; end < nums.length; end++) {
            if (nums[end] == maxElement) {
                maxElementsInWindow++;
            }
            while (maxElementsInWindow == k) {
                if (nums[(int) start] == maxElement) {
                    maxElementsInWindow--;
                }
                start++;
            }
            ans += start;
        }
        return ans;
    }


    // V1-2
    // IDEA: Track Indexes of Max Element
    // https://leetcode.com/problems/count-subarrays-where-max-element-appears-at-least-k-times/editorial/
    public long countSubarrays_1_2(int[] nums, int k) {
        // Finding the maximum element in the array

        int maxElement = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > maxElement) {
                maxElement = nums[i];
            }
        }

        ArrayList<Integer> indexesOfMaxElements = new ArrayList<>();
        long ans = 0;

        // Iterating through the array

        for (int index = 0; index < nums.length; index++) {
            if (nums[index] == maxElement) {
                indexesOfMaxElements.add(index);
            }

            int freq = indexesOfMaxElements.size();
            if (freq >= k) {
                ans += indexesOfMaxElements.get(freq - k) + 1;
            }
        }
        return ans;
    }



    // V2



}
