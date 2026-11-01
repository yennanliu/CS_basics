package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-smallest-subarray-sum/description/

/**
 * 1918. Kth Smallest Subarray Sum
 * Medium
 *
 * Given an integer array nums of length n and an integer k, return the kth smallest subarray sum.
 *
 * A subarray is defined as a non-empty contiguous sequence of elements in an array. A subarray sum is
 * the sum of all elements in the subarray.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,3], k = 4
 * Output: 3
 * Explanation: The subarrays of [2,1,3] are:
 * - [2] with sum 2
 * - [1] with sum 1
 * - [3] with sum 3
 * - [2,1] with sum 3
 * - [1,3] with sum 4
 * - [2,1,3] with sum 6
 * Ordering the sums from smallest to largest gives 1, 2, 3, 3, 4, 6. The 4th smallest is 3.
 *
 * Example 2:
 *
 * Input: nums = [3,3,5,5], k = 7
 * Output: 10
 * Explanation: Ordering the subarray sums from smallest to largest gives
 * 3, 3, 5, 5, 6, 8, 10, 11, 13, 16. The 7th smallest is 10.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 2 * 10^4
 * 1 <= nums[i] <= 5 * 10^4
 * 1 <= k <= n * (n + 1) / 2
 *
 */
public class KthSmallestSubarraySum {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + SLIDING WINDOW COUNT
    /**
     *  nums[i] >= 1, so the number of subarrays with sum <= s never decreases as s grows.
     *  Binary search s over [min(nums), sum(nums)] for the smallest s with count(s) >= k.
     *  That s is an achievable sum: otherwise count(s - 1) == count(s) and s - 1 would win.
     *
     *  count(s): for each right end, shrink the left end while the window sum > s; every
     *  window ending at right and starting in [leftEnd, right] is then valid.
     */
    /**
     * time = O(N log S), S = sum(nums)
     * space = O(1)
     */
    public int kthSmallestSubarraySum(int[] nums, int k) {
        int low = Integer.MAX_VALUE;
        int high = 0;
        for (int value : nums) {
            low = Math.min(low, value);
            high += value;   // at most 2 * 10^4 * 5 * 10^4 = 10^9, fits in an int
        }

        while (low < high) {
            int mid = low + (high - low) / 2;
            if (countAtMost(nums, mid) >= k) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** Number of subarrays whose sum is <= limit (all values are positive). */
    private long countAtMost(int[] nums, int limit) {
        long count = 0;
        long windowSum = 0;
        int leftEnd = 0;
        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];
            while (windowSum > limit) {
                windowSum -= nums[leftEnd];
                leftEnd++;
            }
            // windows [leftEnd..right], [leftEnd+1..right], ..., [right..right]
            count += right - leftEnd + 1;
        }
        return count;
    }
}
