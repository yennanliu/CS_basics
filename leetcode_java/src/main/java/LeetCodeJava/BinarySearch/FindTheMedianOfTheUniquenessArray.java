package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-the-median-of-the-uniqueness-array/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3134. Find the Median of the Uniqueness Array
 * Hard
 *
 * You are given an integer array nums. The uniqueness array of nums is the sorted array that contains the number of distinct elements of all the subarrays of nums. In other words, it is a sorted array consisting of distinct(nums[i..j]), for all 0 <= i <= j < nums.length.
 *
 * Here, distinct(nums[i..j]) denotes the number of distinct elements in the subarray that starts at index i and ends at index j.
 *
 * Return the median of the uniqueness array of nums.
 *
 * Note that the median of an array is defined as the middle element of the array when it is sorted in non-decreasing order. If there are two choices for a median, the smaller of the two values is taken.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 1
 * Explanation:
 * The uniqueness array of nums is [distinct(nums[0..0]), distinct(nums[1..1]), distinct(nums[2..2]), distinct(nums[0..1]), distinct(nums[1..2]), distinct(nums[0..2])] which is equal to [1, 1, 1, 2, 2, 3]. The uniqueness array has a median of 1. Therefore, the answer is 1.
 *
 * Example 2:
 *
 * Input: nums = [3,4,3,4,5]
 * Output: 2
 * Explanation:
 * The uniqueness array of nums is [1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3]. The uniqueness array has a median of 2. Therefore, the answer is 2.
 *
 * Example 3:
 *
 * Input: nums = [4,3,5,4]
 * Output: 2
 * Explanation:
 * The uniqueness array of nums is [1, 1, 1, 1, 2, 2, 2, 3, 3, 3]. The uniqueness array has a median of 2. Therefore, the answer is 2.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class FindTheMedianOfTheUniquenessArray {

    // V0
    // IDEA: BINARY SEARCH THE MEDIAN VALUE + SLIDING-WINDOW "AT MOST k DISTINCT" COUNT
    // the uniqueness array has n(n+1)/2 entries and is never built. Its median (the smaller
    // middle) is the smallest k with  #{subarrays with <= k distinct} >= (total + 1) / 2.
    // That count is the classic two-pointer: for each right end, shrink left until the window
    // has <= k distinct values, then right - left + 1 subarrays ending at `right` qualify.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int medianOfUniquenessArray(int[] nums) {
        int n = nums.length;
        long total = (long) n * (n + 1) / 2;
        long needed = (total + 1) / 2;

        int left = 1;
        int right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (countAtMostDistinct(nums, mid) >= needed) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Number of subarrays with at most k distinct values. */
    private long countAtMostDistinct(int[] nums, int k) {
        Map<Integer, Integer> windowCount = new HashMap<>();
        long subarrays = 0;
        int windowStart = 0;
        for (int windowEnd = 0; windowEnd < nums.length; windowEnd++) {
            windowCount.put(nums[windowEnd], windowCount.getOrDefault(nums[windowEnd], 0) + 1);
            // too many distinct values: drop from the left
            while (windowCount.size() > k) {
                int dropped = nums[windowStart];
                int remaining = windowCount.get(dropped) - 1;
                if (remaining == 0) {
                    windowCount.remove(dropped);
                } else {
                    windowCount.put(dropped, remaining);
                }
                windowStart++;
            }
            subarrays += windowEnd - windowStart + 1;
        }
        return subarrays;
    }
}
