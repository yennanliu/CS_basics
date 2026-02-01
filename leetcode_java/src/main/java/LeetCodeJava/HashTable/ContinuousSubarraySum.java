package LeetCodeJava.HashTable;

// https://leetcode.com/problems/continuous-subarray-sum/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 523. Continuous Subarray Sum
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums and an integer k, return true if nums has a good subarray or false otherwise.
 *
 * A good subarray is a subarray where:
 *
 * its length is at least two, and
 * the sum of the elements of the subarray is a multiple of k.
 * Note that:
 *
 * A subarray is a contiguous part of the array.
 * An integer x is a multiple of k if there exists an integer n such that x = n * k. 0 is always a multiple of k.
 *
 *
 * Example 1:
 *
 * Input: nums = [23,2,4,6,7], k = 6
 * Output: true
 * Explanation: [2, 4] is a continuous subarray of size 2 whose elements sum up to 6.
 * Example 2:
 *
 * Input: nums = [23,2,6,4,7], k = 6
 * Output: true
 * Explanation: [23, 2, 6, 4, 7] is an continuous subarray of size 5 whose elements sum up to 42.
 * 42 is a multiple of 6 because 42 = 7 * 6 and 7 is an integer.
 * Example 3:
 *
 * Input: nums = [23,2,6,4,7], k = 13
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 0 <= sum(nums[i]) <= 231 - 1
 * 1 <= k <= 231 - 1
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 625K
 * Submissions
 * 2M
 * Acceptance Rate
 * 30.5%
 */
public class ContinuousSubarraySum {

    // V0
    // IDEA : HASH MAP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        if (nums.length < 2) {
            return false;
        }

        // Map to store the remainder and its index
        Map<Integer, Integer> map = new HashMap<>();
        // Initialize with remainder 0 at index -1 to handle edge cases
        map.put(0, -1);

        int presum = 0;
        for (int i = 0; i < nums.length; i++) {
            presum += nums[i];

            // Calculate remainder
            /**
             * NOTE:
             *  handle edge case : if k = 0
             */
            int remainder = (k != 0) ? presum % k : presum;

            // If the remainder already exists in the map
            if (map.containsKey(remainder)) {
                // Check if the subarray length is at least 2
                if (i - map.get(remainder) > 1) {
                    return true;
                }
            } else {
                // Store the first occurrence of this remainder
                map.put(remainder, i);
            }
        }

        return false;
    }

    // V0-1
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkSubarraySum_0_1(int[] nums, int k) {

        if (nums.length < 2){
            return false;
        }

        // map : {preSum: idx}
        Map<Integer, Integer> map = new HashMap<>();

        // init the value with init status
        map.put(0,-1);
        int presum = 0;

        for (int i = 0; i < nums.length; i++){
            int cur = nums[i];
            /**
             *
             *  NOTE !!!!!
             *
             *
             *  sum(i,j) = presum(j+1) - presum(i)
             *
             *
             *  remainder : presum = (presum + cur) % k
             *
             *  so if map has "key" with remainder value (AKA (presum + cur) % k)
             *  -> we found a sub-array sum that is multiple of k
             *
             *
             *  e.g. if remainder = (presum + cur) % k
             *  -> we can find a subarray with k multiple
             *
             *  -> idea 1) (presum + cur) % k - (presum) = 0, so it's  k multiple
             *
             *
             */
            presum = (presum + cur) % k;
            // NOTE !!! below
            if (map.containsKey(presum)){
                if (i - map.get(presum) > 1){ // check is sub array length >= 2
                    return true;
                }
            }
            map.putIfAbsent(presum, i);


            // BELOW is wrong !!!
//            map.putIfAbsent(presum, i);
//            /**
//             *  sum(i,j) = presum(j+1) - presum(i)
//             *
//             *  ->  presum(j+1) - presum(i) = k
//             *  -> so, presum(i) = presum(j+1) - k
//             *
//             *  similar idea for "presum mod"
//             */
//            if(map.containsKey(presum - k) && map.get(presum - k) - i >= 2){
//                return true;
//            }
        }

        return false;
    }

    // V0-2
    // IDEA : presum + hashmap
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkSubarraySum_0_2(int[] nums, int k) {

        if (nums.length < 2) {
            return false;
        }

        Map<Integer, Integer> map = new HashMap<>();
        // NOTE!! init as below
        map.put(0, -1);

        int preSum = 0;
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            /**
             * NOTE:
             *  handle edge case : if k = 0
             */
            preSum = k != 0 ? (preSum + cur) % k : preSum;
            if (map.containsKey(preSum)) {
                if (i - map.get(preSum) >= 2) {
                    return true;
                }
            } else {
                map.put(preSum, i);
            }
        }
        return false;
    }


    // V1
    // IDEA : HASHMAP
    // https://leetcode.com/problems/continuous-subarray-sum/editorial/
    // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/presum_mod.png
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkSubarraySum_1(int[] nums, int k) {
        int prefixMod = 0;
        HashMap<Integer, Integer> modSeen = new HashMap<>();
        modSeen.put(0, -1);

        for (int i = 0; i < nums.length; i++) {
            /**
             * NOTE !!! we get `mod of prefixSum`, instead of get prefixSum
             */
            prefixMod = (prefixMod + nums[i]) % k;

            if (modSeen.containsKey(prefixMod)) {
                // ensures that the size of subarray is at least 2
                if (i - modSeen.get(prefixMod) > 1) {
                    return true;
                }
            } else {
                // mark the value of prefixMod with the current index.
                modSeen.put(prefixMod, i);
            }
        }

        return false;
    }


    // V2
    // IDEA : HASHMAP
    // https://leetcode.com/problems/continuous-subarray-sum/solutions/1405425/a-java-implementation-of-prefix-sum-that-si0m/
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean checkSubarraySum_2(int[] nums, int k) {
        // maintain a hash map to store <sum % k, index>
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            sum %= k;
            // case 1
            if (sum == 0 && i > 0) {
                return true;
            }
            // case 2
            if (map.containsKey(sum) && i - map.get(sum) > 1) {
                return true;
            }
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }

        }
        return false;
    }
}
