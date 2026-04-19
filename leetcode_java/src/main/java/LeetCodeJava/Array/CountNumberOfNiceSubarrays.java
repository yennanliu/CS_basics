package LeetCodeJava.Array;

// https://leetcode.com/problems/count-number-of-nice-subarrays/description/

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 1248. Count Number of Nice Subarrays
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array of integers nums and an integer k. A continuous subarray is called nice if there are k odd numbers on it.
 *
 * Return the number of nice sub-arrays.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,1,1], k = 3
 * Output: 2
 * Explanation: The only sub-arrays with 3 odd numbers are [1,1,2,1] and [1,2,1,1].
 * Example 2:
 *
 * Input: nums = [2,4,6], k = 1
 * Output: 0
 * Explanation: There are no odd numbers in the array.
 * Example 3:
 *
 * Input: nums = [2,2,2,1,2,2,1,2,2,2], k = 2
 * Output: 16
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50000
 * 1 <= nums[i] <= 10^5
 * 1 <= k <= nums.length
 *
 */
public class CountNumberOfNiceSubarrays {

    // V0
//    public int numberOfSubarrays(int[] nums, int k) {
//
//    }


    // V1-1
    // IDEA: HASHING
    // https://leetcode.com/problems/count-number-of-nice-subarrays/editorial/
    public int numberOfSubarrays_1_1(int[] nums, int k) {
        int currSum = 0, subarrays = 0;
        Map<Integer, Integer> prefixSum = new HashMap<>();
        prefixSum.put(currSum, 1);

        for (int i = 0; i < nums.length; i++) {
            currSum += nums[i] % 2;
            // Find subarrays with sum k ending at i
            if (prefixSum.containsKey(currSum - k)) {
                subarrays = subarrays + prefixSum.get(currSum - k);
            }
            // Increment the current prefix sum in hashmap
            prefixSum.put(currSum, prefixSum.getOrDefault(currSum, 0) + 1);
        }

        return subarrays;
    }


    // V1-2
    // IDEA: Sliding Window using Queue
    // https://leetcode.com/problems/count-number-of-nice-subarrays/editorial/
    public int numberOfSubarrays_1_2(int[] nums, int k) {
        Queue<Integer> oddIndices = new LinkedList<>();
        int subarrays = 0;
        int lastPopped = -1;
        int initialGap = 0;

        for (int i = 0; i < nums.length; i++) {
            // If element is odd, append its index to the list.
            if (nums[i] % 2 == 1) {
                oddIndices.offer(i);
            }
            // If the number of odd numbers exceeds k, remove the first odd index.
            if (oddIndices.size() > k) {
                lastPopped = oddIndices.poll();
            }
            // If there are exactly k odd numbers, add the number of even numbers
            // in the beginning of the subarray to the result.
            if (oddIndices.size() == k) {
                initialGap = oddIndices.element() - lastPopped;
                subarrays += initialGap;
            }
        }

        return subarrays;
    }


    // V1-3
    // IDEA: Sliding Window (Space Optimisation of queue-based approach)
    // https://leetcode.com/problems/count-number-of-nice-subarrays/editorial/
    public int numberOfSubarrays_1_3(int[] nums, int k) {
        int subarrays = 0, initialGap = 0, qsize = 0, start = 0;
        for (int end = 0; end < nums.length; end++) {
            // If current element is odd, increment qsize by 1.
            if (nums[end] % 2 == 1) {
                qsize++;
            }
            // If qsize is k, calculate initialGap and add it in the answer.
            if (qsize == k) {
                initialGap = 0;
                // Calculate the number of even elements in the beginning of
                // subarray.
                while (qsize == k) {
                    qsize -= nums[start] % 2;
                    initialGap++;
                    start++;
                }
            }
            subarrays += initialGap;
        }
        return subarrays;
    }


    // V1-4
    // IDEA:  Sliding Window (subarray sum at most k)
    // https://leetcode.com/problems/count-number-of-nice-subarrays/editorial/
    public int numberOfSubarrays_1_4(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }

    private int atMost(int[] nums, int k) {
        int windowSize = 0, subarrays = 0, start = 0;

        for (int end = 0; end < nums.length; end++) {
            windowSize += nums[end] % 2;
            // Find the first index start where the window has exactly k odd elements.
            while (windowSize > k) {
                windowSize -= nums[start] % 2;
                start++;
            }
            // Increment number of subarrays with end - start + 1.
            subarrays += end - start + 1;
        }

        return subarrays;
    }



    // V2




}
