package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-threshold-for-inversion-pairs-count/description/

import java.util.Arrays;

/**
 * 3520. Minimum Threshold for Inversion Pairs Count
 * Medium
 *
 * You are given an array of integers nums and an integer k.
 *
 * An inversion pair with a threshold x is defined as a pair of indices (i, j) such
 * that:
 *
 * i < j
 *
 * nums[i] > nums[j]
 *
 * The difference between the two numbers is at most x (i.e. nums[i] - nums[j] <=
 * x).
 *
 * Your task is to determine the minimum integer min_threshold such that there are
 * at least k inversion pairs with threshold min_threshold.
 *
 * If no such integer exists, return -1.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,3,2,1], k = 7
 *
 * Output: 2
 *
 * Explanation:
 *
 * For threshold x = 2, the pairs are:
 *
 * (3, 4) where nums[3] == 4 and nums[4] == 3.
 *
 * (2, 5) where nums[2] == 3 and nums[5] == 2.
 *
 * (3, 5) where nums[3] == 4 and nums[5] == 2.
 *
 * (4, 5) where nums[4] == 3 and nums[5] == 2.
 *
 * (1, 6) where nums[1] == 2 and nums[6] == 1.
 *
 * (2, 6) where nums[2] == 3 and nums[6] == 1.
 *
 * (4, 6) where nums[4] == 3 and nums[6] == 1.
 *
 * (5, 6) where nums[5] == 2 and nums[6] == 1.
 *
 * There are less than k inversion pairs if we choose any integer less than 2 as
 * threshold.
 *
 * Example 2:
 *
 * Input: nums = [10,9,9,9,1], k = 4
 *
 * Output: 8
 *
 * Explanation:
 *
 * For threshold x = 8, the pairs are:
 *
 * (0, 1) where nums[0] == 10 and nums[1] == 9.
 *
 * (0, 2) where nums[0] == 10 and nums[2] == 9.
 *
 * (0, 3) where nums[0] == 10 and nums[3] == 9.
 *
 * (1, 4) where nums[1] == 9 and nums[4] == 1.
 *
 * (2, 4) where nums[2] == 9 and nums[4] == 1.
 *
 * (3, 4) where nums[3] == 9 and nums[4] == 1.
 *
 * There are less than k inversion pairs if we choose any integer less than 8 as
 * threshold.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 *
 * 1 <= nums[i] <= 10^9
 *
 * 1 <= k <= 10^9
 *
 */
public class MinimumThresholdForInversionPairsCount {

    // V0
    // IDEA: BINARY SEARCH ON THE THRESHOLD + FENWICK COUNT OF QUALIFYING PAIRS
    // a wider threshold only admits more pairs, so count(x) is non-decreasing -> binary search
    // the smallest x with count(x) >= k over [1, max - min]; if even max - min falls short,
    // the answer is -1. count(x): sweep j left to right; j's partners are the earlier values in
    // (nums[j], nums[j] + x], which a Fenwick tree over compressed values gives as the
    // difference of two prefix counts.
    /**
     * time = O(N log N log(maxV))
     * space = O(N)
     */
    public int minThreshold(int[] nums, int k) {
        // sorted distinct values for coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int distinct = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[distinct++] = sorted[i];
            }
        }
        int[] values = Arrays.copyOf(sorted, distinct);

        int right = values[distinct - 1] - values[0];
        if (right <= 0 || countPairs(nums, values, right, k) < k) {
            return -1;
        }
        int left = 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (countPairs(nums, values, mid, k) >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Number of pairs i < j with 0 < nums[i] - nums[j] <= threshold, capped at k. */
    private long countPairs(int[] nums, int[] values, int threshold, int k) {
        int[] tree = new int[values.length + 1];
        long total = 0;
        for (int num : nums) {
            // ranks are 1-based; (num, num + threshold] = ranks (rank(num), countAtMost(num + threshold)]
            int rank = countAtMost(values, num);
            int upperRank = countAtMost(values, (long) num + threshold);
            total += prefixCount(tree, upperRank) - prefixCount(tree, rank);
            if (total >= k) {
                return k;
            }
            add(tree, rank);
        }
        return total;
    }

    /** Number of values <= target, which is also the 1-based rank of the last such value. */
    private int countAtMost(int[] values, long target) {
        int left = 0;
        int right = values.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (values[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Fenwick: one more value at `rank`. i & -i is the lowest set bit of i. */
    private void add(int[] tree, int rank) {
        for (int i = rank; i < tree.length; i += i & -i) {
            tree[i]++;
        }
    }

    /** Fenwick: how many seen values have rank <= `rank`. */
    private int prefixCount(int[] tree, int rank) {
        int count = 0;
        for (int i = rank; i > 0; i -= i & -i) {
            count += tree[i];
        }
        return count;
    }
}
