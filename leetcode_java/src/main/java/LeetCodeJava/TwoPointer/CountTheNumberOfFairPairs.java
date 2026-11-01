package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-the-number-of-fair-pairs/description/

import java.util.Arrays;

/**
 * 2563. Count the Number of Fair Pairs
 * Medium
 *
 * Given a 0-indexed integer array nums of size n and two integers lower and upper, return the number of fair pairs.
 *
 * A pair (i, j) is fair if:
 *
 * - 0 <= i < j < n, and
 * - lower <= nums[i] + nums[j] <= upper
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,7,4,4,5], lower = 3, upper = 6
 * Output: 6
 * Explanation: There are 6 fair pairs: (0,3), (0,4), (0,5), (1,3), (1,4), and (1,5).
 *
 * Example 2:
 *
 * Input: nums = [1,7,9,2,5], lower = 11, upper = 11
 * Output: 1
 * Explanation: There is a single fair pair: (2,3).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums.length == n
 * -10^9 <= nums[i] <= 10^9
 * -10^9 <= lower <= upper <= 10^9
 *
 */
public class CountTheNumberOfFairPairs {

    // V0
    // IDEA: SORT + TWO POINTERS, ANSWER = countAtMost(upper) - countAtMost(lower - 1)
    //  the condition only involves the sum, so sorting does not change the number of pairs.
    //  countAtMost(x) on a sorted array: with left / right at both ends, if
    //  nums[left] + nums[right] <= x then every index in (left, right] pairs with left
    //  -> add right - left and advance left; otherwise the sum is too big -> move right in.
    /**
     * time = O(N log N)
     * space = O(N)   (the sorted copy)
     */
    public long countFairPairs(int[] nums, int lower, int upper) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        return countAtMost(sorted, upper) - countAtMost(sorted, (long) lower - 1);
    }

    /** Number of pairs i < j in the sorted array with sorted[i] + sorted[j] <= limit. */
    private long countAtMost(int[] sorted, long limit) {
        long count = 0;
        int left = 0;
        int right = sorted.length - 1;
        while (left < right) {
            if ((long) sorted[left] + sorted[right] <= limit) {
                // sorted[left] pairs with every index in (left, right]
                count += right - left;
                left++;
            } else {
                right--;
            }
        }
        return count;
    }

    // V1
    // IDEA: SORT + BINARY SEARCH THE PARTNER RANGE FOR EACH i
    //  for each i, the valid partners j > i satisfy lower - nums[i] <= nums[j] <= upper - nums[i];
    //  in the sorted array they form one contiguous range found with two binary searches.
    /**
     * time = O(N log N)
     * space = O(N)   (the sorted copy)
     */
    public long countFairPairs_1(int[] nums, int lower, int upper) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        long count = 0;
        for (int i = 0; i < n; i++) {
            long minPartner = (long) lower - sorted[i];
            long maxPartner = (long) upper - sorted[i];
            // partners live in (i, n): first index >= minPartner, first index > maxPartner
            int from = firstAtLeast(sorted, i + 1, minPartner);
            int to = firstAtLeast(sorted, i + 1, maxPartner + 1);
            count += to - from;
        }
        return count;
    }

    /** First index in [start, n) whose value is >= target; n if none. */
    private int firstAtLeast(int[] sorted, int start, long target) {
        int left = start;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
