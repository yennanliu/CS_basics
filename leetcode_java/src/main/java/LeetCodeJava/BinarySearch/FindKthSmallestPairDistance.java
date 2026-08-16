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


    // V1
    // IDEA: COUNTING SORT over the VALUE range + prefix counts
    /**
     *  nums[i] <= 10^6, so bucket the values and build a prefix count. For a
     *  candidate distance d the number of pairs within d is then computed WITHOUT
     *  a two pointer sweep -- for each value v it is
     *      cnt[v] * (cnt[v] - 1) / 2   +   cnt[v] * (numbers in (v, v + d])
     *
     *  The count becomes O(W) per check but is independent of n, which wins when
     *  n is much larger than the value range.
     *
     *  time  = O(W log W + n), W = max value
     *  space = O(W)
     */
    public int smallestDistancePair_1(int[] nums, int k) {
        int maxV = 0;
        for (int v : nums) {
            maxV = Math.max(maxV, v);
        }

        int[] cnt = new int[maxV + 1];
        for (int v : nums) {
            cnt[v] += 1;
        }
        int[] prefix = new int[maxV + 2];
        for (int v = 0; v <= maxV; v++) {
            prefix[v + 1] = prefix[v] + cnt[v];
        }

        int lo = 0;
        int hi = maxV;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            long pairs = 0;
            for (int v = 0; v <= maxV && pairs < k; v++) {
                if (cnt[v] == 0) {
                    continue;
                }
                // pairs inside the same value bucket
                pairs += (long) cnt[v] * (cnt[v] - 1) / 2;
                // pairs with a strictly larger value within mid
                int upper = Math.min(maxV, v + mid);
                pairs += (long) cnt[v] * (prefix[upper + 1] - prefix[v + 1]);
            }

            if (pairs >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    // V2
    // IDEA: BINARY SEARCH ON THE ANSWER + BINARY SEARCH THE COUNT
    /**
     *  Same outer search as V0, but the `how many pairs are within mid?` question
     *  is answered per element with an upper-bound binary search instead of a
     *  sliding window.
     *
     *  O(n log n) per check rather than O(n) -- slower here, but this is the shape
     *  you need when the array is only randomly accessible (no two-pointer
     *  monotonicity to exploit).
     *
     *  time  = O(n log n * log W)
     *  space = O(1)
     */
    public int smallestDistancePair_2(int[] nums, int k) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int n = arr.length;

        int lo = 0;
        int hi = arr[n - 1] - arr[0];
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            long pairs = 0;
            for (int i = 0; i < n; i++) {
                // last index j with arr[j] <= arr[i] + mid
                int j = upperBound(arr, arr[i] + mid) - 1;
                pairs += j - i;
            }

            if (pairs >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    /** first index with arr[idx] > target */
    private int upperBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // V3
    // IDEA: BRUTE FORCE -- materialise every pair distance and sort
    /**
     *  All n(n-1)/2 distances, sorted, take the (k-1)-th.
     *
     *  O(n^2 log n) so it dies at n = 10^4, but there is nothing to get wrong --
     *  this is the oracle the three searching versions are validated against.
     *
     *  time  = O(n^2 log n)
     *  space = O(n^2)
     */
    public int smallestDistancePair_3(int[] nums, int k) {
        int n = nums.length;
        int[] all = new int[n * (n - 1) / 2];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                all[idx++] = Math.abs(nums[i] - nums[j]);
            }
        }
        Arrays.sort(all);
        return all[k - 1];
    }

}
