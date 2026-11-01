package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-subarrays-with-median-k/description/

import java.util.*;

/**
 * 2488. Count Subarrays With Median K
 * Hard
 *
 * You are given an array nums of size n consisting of distinct integers from 1 to n and a positive integer k.
 *
 * Return the number of non-empty subarrays in nums that have a median equal to k.
 *
 * Note:
 *
 * The median of an array is the middle element after sorting the array in ascending order. If the array is of even length, the median is the left middle element.
 * For example, the median of [2,3,1,4] is 2, and the median of [8,4,3,5,1] is 4.
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1,4,5], k = 4
 * Output: 3
 * Explanation: The subarrays that have a median equal to 4 are: [4], [4,5] and [1,4,5].
 *
 * Example 2:
 *
 * Input: nums = [2,3,1], k = 3
 * Output: 1
 * Explanation: [3] is the only subarray that has a median equal to 3.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^5
 * 1 <= nums[i], k <= n
 * The integers in nums are distinct.
 *
 */
public class CountSubarraysWithMedianK {

    // V0
    // IDEA: BALANCE (+1 above k, -1 below k) + HASH MAP OF LEFT-SIDE BALANCES
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countSubarrays(int[] nums, int k) {
        // every valid subarray contains k, so locate it
        int pos = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == k) {
                pos = i;
                break;
            }
        }

        // balance of the part left of k, for every possible left end
        Map<Integer, Integer> leftBalances = new HashMap<>();
        // taking nothing to the left has balance 0
        leftBalances.put(0, 1);
        int balance = 0;
        for (int i = pos - 1; i >= 0; i--) {
            balance += nums[i] > k ? 1 : -1;
            leftBalances.put(balance, leftBalances.getOrDefault(balance, 0) + 1);
        }

        // k is the median iff total balance is 0 (odd length) or 1 (even length,
        // the left middle is the median so the extra element is above k)
        int result = 0;
        balance = 0;
        for (int j = pos; j < nums.length; j++) {
            if (j > pos) {
                balance += nums[j] > k ? 1 : -1;
            }
            result += leftBalances.getOrDefault(-balance, 0);
            result += leftBalances.getOrDefault(1 - balance, 0);
        }
        return result;
    }
}
