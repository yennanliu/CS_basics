package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-k-th-smallest-pair-distance/description/

import java.util.Arrays;

/**
 * 719. Find K-th Smallest Pair Distance
 * Hard
 *
 * The distance of a pair of integers a and b is defined as the absolute difference
 * between a and b.
 *
 * Given an integer array nums and an integer k, return the kth smallest distance
 * among all the pairs nums[i] and nums[j] where 0 <= i < j < nums.length.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,1], k = 1
 * Output: 0
 * Explanation: Here are all the pairs:
 * (1,3) -> 2
 * (1,1) -> 0
 * (3,1) -> 2
 * Then the 1st smallest distance pair is (1,1), and its distance is 0.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1], k = 2
 * Output: 0
 *
 * Example 3:
 *
 * Input: nums = [1,6,1], k = 3
 * Output: 5
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 10^4
 * 0 <= nums[i] <= 10^6
 * 1 <= k <= n * (n - 1) / 2
 *
 */
public class FindKthSmallestPairDistance {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + SLIDING WINDOW COUNT
    /**
     *   We NEVER enumerate the O(n^2) pairs. Instead we binary search the distance d
     *   over [0, max - min] and ask `how many pairs have distance <= d ?`.
     *   That count is MONOTONICALLY INCREASING in d, so the smallest d whose count
     *   reaches k IS the k-th smallest distance.
     *
     *   Counting is a TWO POINTER sweep on the SORTED array: for each `right`,
     *   move `left` forward until nums[right] - nums[left] <= d; every index in
     *   [left, right) then forms a valid pair with `right`.
     *
     *   time  = O(n log n + n log W), W = max(nums) - min(nums)
     *   space = O(1) (ignoring the sort)
     */
    public int smallestDistancePair(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;

        int lo = 0;
        int hi = nums[n - 1] - nums[0];

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (countPairs(nums, mid) >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }

    /** number of pairs (i < j) with nums[j] - nums[i] <= dist */
    private int countPairs(int[] nums, int dist) {
        int cnt = 0;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            while (nums[right] - nums[left] > dist) {
                left += 1;
            }
            /** NOTE !!!
             *
             *  every index in [left, right) pairs with `right`,
             *  that is `right - left` pairs at once (NOT one)
             */
            cnt += right - left;
        }

        return cnt;
    }

}
