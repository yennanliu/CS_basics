package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/
// A subarray is a contiguous sequence of elements within an array.
// prefix sum

import java.util.HashMap;
import java.util.Map;

/**
 * 325. Maximum Size Subarray Sum Equals k
 * Given an array nums and a target value k, find the maximum length of a subarray that sums to k. If there isn't one, return 0 instead.
 *
 * Note:
 * The sum of the entire nums array is guaranteed to fit within the 32-bit signed integer range.
 *
 * Example 1:
 *
 * Input: nums = [1, -1, 5, -2, 3], k = 3
 * Output: 4
 * Explanation: The subarray [1, -1, 5, -2] sums to 3 and is the longest.
 * Example 2:
 *
 * Input: nums = [-2, -1, 2, 1], k = 1
 * Output: 2
 * Explanation: The subarray [-1, 2] sums to 1 and is the longest.
 * Follow Up:
 * Can you do it in O(n) time?
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Palantir Technologies
 *
 *
 */
public class MaximumSizeSubarraySumEqualsK {

    // V0
    // TODO : implement

    // V0-1
    // IDEA : HASHMAP (gpt)
    /**
     *  Examples:
     *
     * int[] nums = {1, 2, -1, 3};
     * int k = 3;
     *
     *
     *  1.	Initialization:
     * 	    •	preSumMap: Initially {0: -1} (to handle subarrays starting at index 0).
     *
     * 	2.	Iteration:
     * 	    •	Index 0:
     * 	        •	curSum = 1
     * 	        •	curSum - k = 1 - 3 = -2 (not in map)
     * 	        •	Update map: {0: -1, 1: 0}
     *
     * 	    •	Index 1:
     * 	        •	curSum = 3
     * 	        •	curSum - k = 3 - 3 = 0 (in map at index -1)
     * 	        •	Subarray [0, 1] has sum 3, so maxSize = max(0, 1 - (-1)) = 2
     * 	        •	Update map: {0: -1, 1: 0, 3: 1}
     *
     *
     * 	    •	Index 2:
     * 	        •	curSum = 2
     * 	        •	curSum - k = 2 - 3 = -1 (not in map)
     * 	        •	Update map: {0: -1, 1: 0, 3: 1, 2: 2}
     *
     *
     * 	    •	Index 3:
     * 	        •	curSum = 5
     * 	        •	curSum - k = 5 - 3 = 2 (in map at index 2)
     * 	        •	Subarray [3, 3] has sum 3, so maxSize = max(2, 3 - 2) = 2
     * 	        •	Update map: {0: -1, 1: 0, 3: 1, 2: 2, 5: 3}
     */
    public int maxSubArrayLen_0_1(int[] nums, int k) {
        // Map to store (prefixSum, index)
        Map<Integer, Integer> preSumMap = new HashMap<>();
        preSumMap.put(0, -1); // Initialize for subarrays starting from index 0

        int curSum = 0;
        int maxSize = 0;

        for (int i = 0; i < nums.length; i++) {
            curSum += nums[i];

            // Check if there's a prefix sum such that curSum - prefixSum = k
            /**
             *  Prefix sum
             *
             *
             * The prefix sum approach works because any subarray sum can be expressed in terms of two prefix sums:
             *
             *
             * sum of subarray[i,j] = prefixSum[j] - prefixSum[i-1]
             *
             *
             * Where:
             * 	•	prefixSum[j] is the cumulative sum of the array up to index j.
             * 	•	prefixSum[i-1] is the cumulative sum of the array up to index i-1.
             *
             * Rewriting this:
             *
             * -> prefixSum[j] - prefixSum[i-1] = k
             *
             * -> prefixSum[i-1] = prefixSum[j] - k
             *
             *
             * Thus, the task is to find a previous prefix
             * sum (prefixSum[i-1]) such that the
             * difference between the current
             * prefix sum (prefixSum[j]) and that value equals k.
             *
             *
             *
             *  How the Code Works
             *
             * 	1.	Tracking Prefix Sums:
             * 	    •	curSum is the cumulative prefix sum up to the current index i.
             * 	    •	The map preSumMap stores previously seen prefix sums as keys, with their earliest index as the value.
             * 	2.	Checking for Subarrays:
             * 	    •	At any index i, the condition curSum - k checks if there exists a previously seen prefix sum that, when subtracted from the current cumulative sum, gives the desired subarray sum k.
             *
             * 	3.	Why It Covers All Possible Subarrays******:
             * 	    •	The map contains all prefix sums seen so far, so it inherently includes all potential starting points of subarrays.
             * 	    •	If a subarray [start, i] has a sum equal to k, the difference curSum - k corresponds to the prefix sum at start - 1. Since the map stores all previously seen prefix sums, this difference is guaranteed to be checked.
             *
             */
            if (preSumMap.containsKey(curSum - k)) {
                maxSize = Math.max(maxSize, i - preSumMap.get(curSum - k));
            }

            // Add current prefix sum to the map if not already present
            preSumMap.putIfAbsent(curSum, i);
        }

        return maxSize;
    }

    // V1
    // https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/solutions/1017059/java-prefix-sums/
    public int maxSubArrayLen(int[] nums, int k) {

        int sum = 0;
        int maxLength = 0;
        Map<Integer, Integer> prefixSums = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            int complement = sum - k;
            if (complement == 0) {
                maxLength = i + 1;  // the whole subarray up to the current index totals to `k`.
            } else if (prefixSums.containsKey(complement)) {
                maxLength = Math.max(maxLength, i - prefixSums.get(complement));
            }

            prefixSums.putIfAbsent(sum, i);
        }

        return maxLength;
    }

    // V2
    // https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/solutions/77784/o-n-clean-short-java-python-solution-with-hashmap-with-code-comments/
    public int maxSubArrayLen_2(int[] nums, int k) {
        int currSum = 0, maxLen = 0; // set initial values for cumulative sum and max length sum to k
        HashMap<Integer, Integer> sumToIndexMap = new HashMap<Integer, Integer>(); // key: cumulative sum until index i, value: i
        for (int i = 0; i < nums.length; i++) {
            currSum = currSum + nums[i]; // update cumulative sum

            // two cases where we can update maxLen
            if (currSum == k) maxLen = i + 1; // case 1: cumulative sum is k, update maxLen for sure
            else if (sumToIndexMap.containsKey(currSum - k)) maxLen = Math.max(maxLen, i - sumToIndexMap.get(currSum - k)); // case 2: cumulative sum is more than k, but we can truncate a prefix of the array

            // store cumulative sum in map, only if it is not seen
            // because only the earlier (thus shorter) subarray is valuable, when we want to get the maxLen after truncation
            if (!sumToIndexMap.containsKey(currSum)) sumToIndexMap.put(currSum, i);
        }
        return maxLen;
    }

    // V3
    // https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/solutions/77844/java-with-newer-methods/
    public int maxSubArrayLen_3(int[] nums, int k) {
        Map<Integer, Integer> index = new HashMap();
        index.put(0, -1);
        int sum = 0, max = 0;
        for (int i=0; i<nums.length; i++) {
            sum += nums[i];
            max = Math.max(max, i - index.getOrDefault(sum - k, i));
            index.putIfAbsent(sum, i);
        }
        return max;
    }

    // V4
    // https://leetcode.ca/2016-10-20-325-Maximum-Size-Subarray-Sum-Equals-k/
    public int maxSubArrayLen_4(int[] nums, int k) {
        Map<Long, Integer> d = new HashMap<>();
        d.put(0L, -1);
        int ans = 0;
        long s = 0;
        for (int i = 0; i < nums.length; ++i) {
            s += nums[i];
            ans = Math.max(ans, i - d.getOrDefault(s - k, i));
            d.putIfAbsent(s, i);
        }
        return ans;
    }

}
