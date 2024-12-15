package LeetCodeJava.HashTable;

// https://leetcode.com/problems/subarray-sum-equals-k/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 560. Subarray Sum Equals K
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1], k = 2
 * Output: 2
 * Example 2:
 *
 * Input: nums = [1,2,3], k = 3
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 104
 * -1000 <= nums[i] <= 1000
 * -107 <= k <= 107
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 1.5M
 * Submissions
 * 3.4M
 * Acceptance Rate
 * 44.5%
 * Topics
 */
public class SubarraySumEqualsK {

    // V0
    // IDEA : HASH MAP (fixed by gpt)
    /**
     *
     * Explanation of Fixes:
     *
     * 	1.	Using presum - k Logic Properly:
     * 	    •	If presum - k exists in the map, it means there is a subarray ending at the current index whose sum is  k . Add the count of such prefix sums to the total.
     *
     * 	2.	Map Initialization:
     * 	    •	Initializing the map with map.put(0, 1) allows the logic to handle cases where a subarray starting from index 0 equals  k .
     *
     * 	3.	Updating the Map:
     * 	    •	Instead of tracking indices, the map stores the count of occurrences of each prefix sum. This allows us to find how many subarrays can be formed using a specific prefix sum.
     *
     * 	4.	Simplifying Index-Based Logic:
     * 	    •	Removed unnecessary index-based conditions (map.get(presum - k) == i + 1) which were incorrect and not required for this problem.
     */
    public int subarraySum(int[] nums, int k) {
        /**
         * NOTE !!!
         *
         *   use Map to store prefix sum and its count
         *
         *   map : {prefixSum: count}
         *
         *
         *   -> since "same preSum may have multiple combination" within hashMap,
         *      so it's needed to track preSum COUNT, instead of its index
         */
        Map<Integer, Integer> map = new HashMap<>();
        int presum = 0;
        int count = 0;

        /**
         *  NOTE !!!
         *
         *  Initialize the map with prefix sum 0 (to handle subarrays starting at index 0)
         *
         *
         *
         *  Purpose of map.put(0, 1);
         *  
         * 	1.	Handle the Initial Case:
         * 	    •	The prefix sum presum starts at 0 before any elements of the array are processed.
         * 	    •	Adding map.put(0, 1) ensures that if a subarray’s prefix sum equals k (e.g., the subarray itself equals  k ), it is counted correctly.
         *
         * 	2.	Account for Subarrays Starting at Index 0:
         * 	    •	Consider the case where the cumulative sum of elements up to a certain index  j  equals  k : presum[j] = k
         * 	    •	The subarray from index 0 to  j  should count as a valid subarray.
         * 	    •	To check this condition, the code calculates presum - k and looks for it in the map. For subarrays starting at index 0, presum - k equals 0. Adding map.put(0, 1) ensures this case is handled properly.
         *
         * 	3.	Count Prefix Sums:
         * 	    •	The value 1 in map.put(0, 1) represents the fact that there is one prefix sum of 0 initially (before processing any elements). This allows the algorithm to correctly count subarrays that sum to  k  as the prefix sum progresses.
         */
        map.put(0, 1);

        for (int num : nums) {
            presum += num;

            // Check if there's a prefix sum such that presum - k exists
            if (map.containsKey(presum - k)) {
                count += map.get(presum - k);
            }

            // Update the map with the current prefix sum
            map.put(presum, map.getOrDefault(presum, 0) + 1);
        }

        return count;
    }

    // V0-1
    // IDEA : HASH MAP (fixed by gpt)
    public int subarraySum_0_1(int[] nums, int k) {

        if (nums.length == 1){
            if (nums[0] == k){
                return 1;
            }
            return 0;
        }

        // map : {presum : count}
        Map<Integer, Integer> map = new HashMap<>();
        /** NOTE !!!
         *
         *  init map as below
         *
         *  Initialize the map with prefix sum 0 (to handle subarrays starting at index 0)
         */
        map.put(0,1);
        int preusm = 0;
        int cnt = 0;
        for (int i = 0; i < nums.length; i++){
            int cur = nums[i];
            preusm += cur;
            /**
             *  Reason why update map after `count` update (e.g. map.put(preusm, map.getOrDefault(preusm, 0) + 1) after if condition)
             *
             * 	1.	Avoid Overcounting:
             * 	    •	When checking if (map.containsKey(preusm - k)), you are looking for how many previous subarrays have a prefix sum of preusm - k.
             * 	    •	If you update the map before this check (i.e., increment the count for the current preusm), you might mistakenly count the current subarray itself in this operation, leading to incorrect results.
             *
             * 	2.	Logical Order of Operations:
             * 	    •	The purpose of the map is to store the counts of previous prefix sums seen so far.
             * 	    •	When you calculate cnt += map.get(preusm - k), you are determining how many times the subarray sum  k  has been encountered up to this point.
             * 	    •	Only after this check should you update the map to include the current preusm for subsequent iterations.
             *
             * 	3.	Current Subarray Shouldn’t Influence Itself:
             * 	    •	In the current iteration, the subarray being evaluated shouldn’t count itself as contributing to the result. By updating the map after the check, you ensure the current prefix sum becomes available only for future iterations.
             *
             */
            //map.put(preusm, map.getOrDefault(preusm, 0) + 1);
            if (map.containsKey(preusm - k)){
                cnt += map.get(preusm - k);
            }
            // NOTE !! update map after `if condition`
            map.put(preusm, map.getOrDefault(preusm, 0) + 1);
        }

        return cnt;
    }

    // V1
    // IDEA : HASH MAP
    // https://leetcode.com/problems/subarray-sum-equals-k/solutions/6143642/java-beats-9983-by-mohamedhazem3-9yj6/
    public static int subarraySum_1(int[] nums, int k) {
        int[] prefix = new int[nums.length + 1];
        int out = 0;
        HashMap<Integer, Integer> hm = new HashMap<>();

        for (int i = 0; i < nums.length; i++)
            prefix[i + 1] = prefix[i] + nums[i]; // Compute prefix sums

        for (int i = 0; i < prefix.length; i++) {
            if (hm.containsKey(prefix[i] - k)) // Check if required difference exists
                out += hm.get(prefix[i] - k); // Add the frequency to the result

            // Update HashMap with the current prefix sum
            hm.put(prefix[i], hm.getOrDefault(prefix[i], 0) + 1);
        }
        return out;
    }

    // V2
    // IDEA : HASH MAP
    // https://leetcode.com/problems/subarray-sum-equals-k/solutions/3167609/explained-beats-964-using-hashmap-in-jav-0b6o/
    public int subarraySum_2(int[] nums, int k) {
        int sum = 0;
        int ans = 0;
        HashMap<Integer,Integer> map = new HashMap<>();
        map.put(0,1);
        for(int j=0;j<nums.length;j++){
            sum += nums[j];
            if(map.containsKey(sum -k)){
                ans += map.get(sum-k);
            }
            map.put(sum,map.getOrDefault(sum,0)+1);
        }
        return ans;
    }

    // V3
    // IDEA : HASH MAP
    // https://leetcode.com/problems/subarray-sum-equals-k/solutions/803317/java-solution-with-detailed-explanation-yu2hg/
    public int subarraySum_3(int[] nums, int k) {
        int count = 0;

        int[] sum = new int[nums.length + 1];
        sum[0] = 0;
        for (int i = 1; i <= nums.length; i++)
            sum[i] = sum[i - 1] + nums[i - 1];

        for (int start = 0; start < sum.length; start++) {
            for (int end = start + 1; end < sum.length; end++) {
                if (sum[end] - sum[start] == k)
                    count++;
            }
        }
        return count;
    }

}
