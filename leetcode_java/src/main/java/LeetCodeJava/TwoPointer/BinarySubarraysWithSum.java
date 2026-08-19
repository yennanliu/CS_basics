package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/binary-subarrays-with-sum/

import java.util.HashMap;
import java.util.Map;

/**
 *  930. Binary Subarrays With Sum
 *  Medium
 *
 *  Given a binary array nums and an integer goal, return the number of non-empty
 *  subarrays with a sum goal.
 *
 *  A subarray is a contiguous part of the array.
 *
 *  Example 1:
 *  Input: nums = [1,0,1,0,1], goal = 2
 *  Output: 4
 *  Explanation: The 4 subarrays are bolded and underlined below:
 *  [1,0,1], [1,0,1,0], [0,1,0,1], [1,0,1]
 *
 *  Example 2:
 *  Input: nums = [0,0,0,0,0], goal = 0
 *  Output: 15
 *
 *  Constraints:
 *   1 <= nums.length <= 3 * 10^4
 *   nums[i] is either 0 or 1.
 *   0 <= goal <= nums.length
 */
public class BinarySubarraysWithSum {

    // V0
    // IDEA: prefix sum counter, res += count(prefix - goal)
    /**
     * time = O(n)
     * space = O(n)
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        Map<Integer, Integer> cnt = new HashMap<>();
        cnt.put(0, 1);
        int preSum = 0;
        int res = 0;
        for (int x : nums) {
            preSum += x;
            res += cnt.getOrDefault(preSum - goal, 0);
            cnt.put(preSum, cnt.getOrDefault(preSum, 0) + 1);
        }
        return res;
    }

    // V1
    // IDEA: sliding window, count(sum <= goal) - count(sum <= goal - 1)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numSubarraysWithSum_1(int[] nums, int goal) {
        return atMost(nums, goal) - atMost(nums, goal - 1);
    }

    // number of subarrays whose sum is at most k
    private int atMost(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }
        int left = 0;
        int sum = 0;
        int res = 0;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum > k) {
                sum -= nums[left];
                left++;
            }
            res += right - left + 1;
        }
        return res;
    }
}
