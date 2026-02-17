package LeetCodeJava.HashTable;

// https://leetcode.com/problems/subarray-sums-divisible-by-k/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 974. Subarray Sums Divisible by K
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums and an integer k, return the number of non-empty subarrays that have a sum divisible by k.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [4,5,0,-2,-3,1], k = 5
 * Output: 7
 * Explanation: There are 7 subarrays with a sum divisible by k = 5:
 * [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
 * Example 2:
 *
 * Input: nums = [5], k = 9
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 104
 * -104 <= nums[i] <= 104
 * 2 <= k <= 104
 *
 *
 */
public class SubarraySumsDivisibleByK {

    // V0
//    public int subarraysDivByK(int[] nums, int k) {
//
//    }

    // V1
    // IDEA: Prefix Sums and Counting
    // https://leetcode.com/problems/subarray-sums-divisible-by-k/editorial/
    public int subarraysDivByK_1(int[] nums, int k) {
        int n = nums.length;
        int prefixMod = 0, result = 0;

        // There are k mod groups 0...k-1.
        int[] modGroups = new int[k];
        modGroups[0] = 1;

        for (int num : nums) {
            // Take modulo twice to avoid negative remainders.
            prefixMod = (prefixMod + num % k + k) % k;
            // Add the count of subarrays that have the same remainder as the current
            // one to cancel out the remainders.
            result += modGroups[prefixMod];
            modGroups[prefixMod]++;
        }

        return result;
    }


    // V2
    // https://leetcode.com/problems/subarray-sums-divisible-by-k/solutions/5281639/fasterless-memdetailed-approachprefix-su-hs5y/
    public int subarraysDivByK_2(int[] nums, int k) {
        // Initialize count of subarrays, prefix sum, and hash map for remainders
        int count = 0;
        int prefixSum = 0;
        HashMap<Integer, Integer> prefixMap = new HashMap<>();
        prefixMap.put(0, 1); // To handle subarrays that start from the beginning

        for (int num : nums) {
            // Update prefix sum
            prefixSum += num;

            // Calculate the remainder of the prefix sum divided by k
            int mod = prefixSum % k;

            // Adjust negative remainders to be positive
            if (mod < 0) {
                mod += k;
            }

            // If this remainder has been seen before, it means there are subarrays ending here that are divisible by k
            if (prefixMap.containsKey(mod)) {
                count += prefixMap.get(mod);
                prefixMap.put(mod, prefixMap.get(mod) + 1);
            } else {
                prefixMap.put(mod, 1);
            }
        }

        return count; // Total number of subarrays divisible by k
    }


    // V3
    // https://leetcode.com/problems/subarray-sums-divisible-by-k/solutions/3070594/java-explained-in-detail-simple-fast-sol-yueg/
    public int subarraysDivBy_3(int[] nums, int k) {

        // Use the HashMap to record the frequency of all the prefix sum remainders.
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0, remainder = 0; i < nums.length; i++) {
            // Note that the integer in 'nums' can be negative.
            // Thus, we need to adjust the negative remainder to positive remainder.
            // Below accounts for both negative and positive remainders.
            // We can also check if the remainder is negative, then add a 'k' to make the remainder positive.
            // For Example, nums = [-2,3,2], k = 5,
            // remainder for the prefix sum of [-2,1,3] are -2, 1 and 3 respectively.
            // We know that [3,2] sum to 5, which is divisible by 5.
            // After converting -2 to 3, by adding 5, it has the same remainder with prefix sum 3.
            remainder = ((remainder + nums[i]) % k + k) % k;
            map.put(remainder, map.getOrDefault(remainder, 0) + 1);
        }
        // The result contains all the prefix sum with remainder 0,
        // as all the prefix sum with remainder of 0 is itself divisible by 'k'.
        // However, do note that the prefix sum with remainder 0 also able to form subarray sums that is divisible by 'k'
        // with one another, which will be calculated next.
        // For Example: nums = [5,5,5,5], k = 5,
        // The prefix sum of [5,10,15,20] are themselves divisible by 5, while also forming subarray sums divisible by 5
        // with 10 [5,5] - 5 [5] == 5, 15 [5,5,5] - 5 [5] == 10, etc.
        int result = map.getOrDefault(0, 0);

        // The prefix sums with the same remainder can form subarray sums that is divisible by 'k' with each other.
        // For each remainder, the number of subarray that is divisible by 'k' is the number of combinations from the frequency.
        // Equation for the number of combinations of n items is n * "(n - 1) / 2".
        for (int frequency : map.values())
            result += frequency * (frequency - 1) / 2;

        return result;
    }


    // V4
    // https://leetcode.com/problems/subarray-sums-divisible-by-k/solutions/3070544/prefix-sum-hashmap-on-java-video-explana-ekfd/
    public int subarraysDivByK_4(int[] nums, int k) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        int prefixSum = 0;
        int ans = 0;
        hashMap.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            prefixSum = (prefixSum + nums[i]) % k;
            if (prefixSum < 0)
                prefixSum += k;
            if (hashMap.containsKey(prefixSum)) {

                ans = ans + hashMap.get(prefixSum);

                hashMap.put(prefixSum, hashMap.get(prefixSum) + 1);
            } else {
                hashMap.put(prefixSum, 1);
            }

        }
        return ans;
    }




}
