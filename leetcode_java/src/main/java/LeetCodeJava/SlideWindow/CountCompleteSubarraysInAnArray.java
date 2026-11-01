package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-complete-subarrays-in-an-array/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 2799. Count Complete Subarrays in an Array
 * Medium
 *
 * You are given an array nums consisting of positive integers.
 *
 * We call a subarray of an array complete if the following condition is satisfied:
 *
 * The number of distinct elements in the subarray is equal to the number of distinct elements in
 * the whole array.
 *
 * Return the number of complete subarrays.
 *
 * A subarray is a contiguous non-empty part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,1,2,2]
 * Output: 4
 * Explanation: The complete subarrays are the following: [1,3,1,2], [1,3,1,2,2], [3,1,2] and
 * [3,1,2,2].
 *
 * Example 2:
 *
 * Input: nums = [5,5,5,5]
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 2000
 *
 */
public class CountCompleteSubarraysInAnArray {

    // V0
    // IDEA: SLIDING WINDOW + COUNTER
    //  if [left..right] is complete, so is every extension [left..right'] with right' >= right,
    //  so the shortest complete window starting at left contributes n - right subarrays.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countCompleteSubarrays(int[] nums) {
        int n = nums.length;
        Set<Integer> all = new HashSet<>();
        for (int num : nums) {
            all.add(num);
        }
        int totalDistinct = all.size();

        // value -> occurrences inside the window
        Map<Integer, Integer> window = new HashMap<>();
        int count = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            window.put(nums[right], window.getOrDefault(nums[right], 0) + 1);
            while (window.size() == totalDistinct) {
                // [left..right], [left..right+1], ..., [left..n-1] are all complete
                count += n - right;
                // drop nums[left]; remove the key at 0 so size() stays the distinct count
                int remaining = window.get(nums[left]) - 1;
                if (remaining == 0) {
                    window.remove(nums[left]);
                } else {
                    window.put(nums[left], remaining);
                }
                left++;
            }
        }
        return count;
    }
}
