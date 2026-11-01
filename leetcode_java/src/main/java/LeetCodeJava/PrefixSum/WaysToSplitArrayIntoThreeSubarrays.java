package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/ways-to-split-array-into-three-subarrays/description/

/**
 * 1712. Ways to Split Array Into Three Subarrays
 * Medium
 *
 * A split of an integer array is good if:
 *
 * The array is split into three non-empty contiguous subarrays - named left, mid, right respectively from left to right.
 * The sum of the elements in left is less than or equal to the sum of the elements in mid, and the sum of the elements in mid is less than or equal to the sum of the elements in right.
 *
 * Given nums, an array of non-negative integers, return the number of good ways to split nums. As the number may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1]
 * Output: 1
 * Explanation: The only good way to split nums is [1] [1] [1].
 *
 * Example 2:
 *
 * Input: nums = [1,2,2,2,5,0]
 * Output: 3
 * Explanation: There are three good ways of splitting nums:
 * [1] [2] [2,2,5,0]
 * [1] [2,2] [2,5,0]
 * [1,2] [2,2] [5,0]
 *
 * Example 3:
 *
 * Input: nums = [3,2,1]
 * Output: 0
 * Explanation: There is no good way to split nums.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^4
 *
 */
public class WaysToSplitArrayIntoThreeSubarrays {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PREFIX SUM + BINARY SEARCH (nums are non-negative -> prefix is monotone)
    //       prefix[i] = nums[0] + .. + nums[i], NON-DECREASING since nums[i] >= 0.
    //       fix i = the last index of `left` (left sum = prefix[i]). the last
    //       index m of `mid` must satisfy
    //         left <= mid   ->  prefix[m] >= 2 * prefix[i]
    //         mid <= right  ->  prefix[m] - prefix[i] <= total - prefix[m]
    //       both are monotone in prefix[m], so the valid m form one contiguous
    //       range [lo, hi) inside [i+1, n-1) -> two binary searches.
    //       m > i keeps `mid` non-empty, m < n-1 keeps `right` non-empty.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int waysToSplit(int[] nums) {
        int n = nums.length;
        // long : 2 * prefix can pass Integer.MAX_VALUE
        long[] prefix = new long[n];
        prefix[0] = nums[0];
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        long total = prefix[n - 1];

        long ways = 0;
        for (int i = 0; i < n - 2; i++) {
            // first m with mid >= left
            int lo = firstIndexWhereMidAtLeastLeft(prefix, i, n);
            // first m with mid > right (exclusive upper end)
            int hi = firstIndexWhereMidExceedsRight(prefix, i, n, total);
            if (hi > lo) {
                ways += hi - lo;
            }
        }
        return (int) (ways % MOD);
    }

    /** Smallest m in [i+1, n-1] with prefix[m] >= 2 * prefix[i]; n-1 if none. */
    private int firstIndexWhereMidAtLeastLeft(long[] prefix, int i, int n) {
        int left = i + 1;
        int right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prefix[mid] >= 2 * prefix[i]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Smallest m in [i+1, n-1] with prefix[m] - prefix[i] > total - prefix[m]; n-1 if none. */
    private int firstIndexWhereMidExceedsRight(long[] prefix, int i, int n, long total) {
        int left = i + 1;
        int right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prefix[mid] - prefix[i] > total - prefix[mid]) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    // V1
    // IDEA: PREFIX SUM + THREE POINTERS
    //       as i moves right prefix[i] never decreases, so both bounds of the
    //       valid range [lo, hi) only move right too -> keep lo and hi as
    //       pointers that are never reset, giving O(N) overall.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int waysToSplit_1(int[] nums) {
        int n = nums.length;
        long[] prefix = new long[n];
        prefix[0] = nums[0];
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        long total = prefix[n - 1];

        long ways = 0;
        int lo = 0;
        int hi = 0;
        for (int i = 0; i < n - 2; i++) {
            // mid must be non-empty
            lo = Math.max(lo, i + 1);
            // advance lo until mid >= left
            while (lo < n - 1 && prefix[lo] < 2 * prefix[i]) {
                lo++;
            }
            hi = Math.max(hi, lo);
            // advance hi while mid <= right still holds
            while (hi < n - 1 && prefix[hi] - prefix[i] <= total - prefix[hi]) {
                hi++;
            }
            ways += hi - lo;
        }
        return (int) (ways % MOD);
    }
}
