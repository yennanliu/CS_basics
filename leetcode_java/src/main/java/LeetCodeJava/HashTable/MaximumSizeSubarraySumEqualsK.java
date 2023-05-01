package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/
// A subarray is a contiguous sequence of elements within an array.
// prefix sum

import java.util.HashMap;
import java.util.Map;

public class MaximumSizeSubarraySumEqualsK {

    // V0

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

}
