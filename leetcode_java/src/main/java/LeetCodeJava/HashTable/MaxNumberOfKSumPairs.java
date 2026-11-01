package LeetCodeJava.HashTable;

// https://leetcode.com/problems/max-number-of-k-sum-pairs/description/

import java.util.*;

/**
 * 1679. Max Number of K-Sum Pairs
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * In one operation, you can pick two numbers from the array whose sum equals k and remove them from
 * the array.
 *
 * Return the maximum number of operations you can perform on the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], k = 5
 * Output: 2
 * Explanation: Starting with nums = [1,2,3,4]:
 * - Remove numbers 1 and 4, then nums = [2,3]
 * - Remove numbers 2 and 3, then nums = []
 * There are no more pairs that sum up to 5, hence a total of 2 operations.
 *
 * Example 2:
 *
 * Input: nums = [3,1,3,4,3], k = 6
 * Output: 1
 * Explanation: Starting with nums = [3,1,3,4,3]:
 * - Remove the first two 3's, then nums = [1,4,3]
 * There are no more pairs that sum up to 6, hence a total of 1 operation.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= 10^9
 *
 */
public class MaxNumberOfKSumPairs {

    // V0
    // IDEA: HASH TABLE, ONE PASS (close a pair as soon as its partner is waiting)
    //       copies of a value are interchangeable, so matching eagerly never blocks a better pairing
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxOperations(int[] nums, int k) {
        // value -> how many copies are still waiting for a partner
        Map<Integer, Integer> waiting = new HashMap<>();
        int operations = 0;

        for (int value : nums) {
            int partner = k - value;
            int partnerWaiting = waiting.getOrDefault(partner, 0);
            if (partnerWaiting > 0) {
                waiting.put(partner, partnerWaiting - 1);
                operations++;
            } else {
                waiting.put(value, waiting.getOrDefault(value, 0) + 1);
            }
        }
        return operations;
    }

    // V1
    // IDEA: SORT + TWO POINTERS
    //       too small a sum -> move left up; too big -> move right down; equal -> pair them
    /**
     * time = O(N log N)
     * space = O(N), the sorted copy (O(1) if sorting the input in place is allowed)
     */
    public int maxOperations_1(int[] nums, int k) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int left = 0;
        int right = sorted.length - 1;
        int operations = 0;
        while (left < right) {
            // values reach 10^9, so the sum of two can overflow an int
            long sum = (long) sorted[left] + sorted[right];
            if (sum == k) {
                operations++;
                left++;
                right--;
            } else if (sum < k) {
                left++;
            } else {
                right--;
            }
        }
        return operations;
    }
}
