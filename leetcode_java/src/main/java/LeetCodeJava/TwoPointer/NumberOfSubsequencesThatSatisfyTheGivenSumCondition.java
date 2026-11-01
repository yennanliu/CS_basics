package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/number-of-subsequences-that-satisfy-the-given-sum-condition/description/

import java.util.Arrays;

/**
 * 1498. Number of Subsequences That Satisfy the Given Sum Condition
 * Medium
 *
 * You are given an array of integers nums and an integer target.
 *
 * Return the number of non-empty subsequences of nums such that the sum of the
 * minimum and maximum element on it is less or equal to target. Since the answer may
 * be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5,6,7], target = 9
 * Output: 4
 * Explanation: There are 4 subsequences that satisfy the condition.
 * [3] -> Min value + max value <= target (3 + 3 <= 9)
 * [3,5] -> (3 + 5 <= 9)
 * [3,5,6] -> (3 + 6 <= 9)
 * [3,6] -> (3 + 6 <= 9)
 *
 * Example 2:
 *
 * Input: nums = [3,3,6,8], target = 10
 * Output: 6
 * Explanation: There are 6 subsequences that satisfy the condition. (nums can have
 * repeated numbers).
 * [3] , [3] , [3,3], [3,6] , [3,6] , [3,3,6]
 *
 * Example 3:
 *
 * Input: nums = [2,3,3,4,6,7], target = 12
 * Output: 61
 * Explanation: There are 63 non-empty subsequences, two of them do not satisfy the
 * condition ([6,7], [7]).
 * Number of valid subsequences (63 - 2 = 61).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^6
 * 1 <= target <= 10^6
 *
 */
public class NumberOfSubsequencesThatSatisfyTheGivenSumCondition {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SORT + TWO POINTERS (only the MIN and MAX matter, so order does not)
    //   after sorting, fix the minimum at left; with right the largest index where
    //   nums[left] + nums[right] <= target, every element strictly between them may be
    //   in or out: 2^(right - left) subsequences have nums[left] as their minimum.
    //   left and right only move inward -> O(N) after the sort; powers of 2 are precomputed.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int numSubseq(int[] nums, int target) {
        // edge case: no elements, no subsequence
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // sort a copy: the caller's array is left untouched
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int n = sorted.length;

        int[] powerOfTwo = precomputePowersOfTwo(n);

        long count = 0;
        int left = 0;
        int right = n - 1;

        while (left <= right) {
            if (sorted[left] + sorted[right] > target) {
                // this max is too big even for the smallest remaining min -> drop it for good
                right--;
            } else {
                // min = sorted[left]; each of the (right - left) elements after it is optional
                count = (count + powerOfTwo[right - left]) % MOD;
                left++;
            }
        }

        return (int) count;
    }

    // V1
    // IDEA: SORT + BINARY SEARCH PER LEFT END
    //   same counting as V0, but for each left end find the last right with
    //   sorted[right] <= target - sorted[left] by binary search instead of a moving pointer.
    //   one log factor slower in the counting phase; V0 is the one to use in an interview.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int numSubseq_1(int[] nums, int target) {
        // edge case: no elements, no subsequence
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int n = sorted.length;

        int[] powerOfTwo = precomputePowersOfTwo(n);

        long count = 0;
        for (int left = 0; left < n; left++) {
            int maxAllowed = target - sorted[left];
            // the min alone already breaks the condition, and every later min is larger
            if (sorted[left] > maxAllowed) {
                break;
            }

            // last index whose value is <= maxAllowed (at least left itself)
            int right = lastIndexAtMost(sorted, left, maxAllowed);
            count = (count + powerOfTwo[right - left]) % MOD;
        }

        return (int) count;
    }

    /** powerOfTwo[i] = 2^i mod MOD, for i in [0, n). */
    private int[] precomputePowersOfTwo(int n) {
        int[] powerOfTwo = new int[n];
        powerOfTwo[0] = 1;
        for (int i = 1; i < n; i++) {
            powerOfTwo[i] = (int) ((powerOfTwo[i - 1] * 2L) % MOD);
        }
        return powerOfTwo;
    }

    /** Largest index in [from, sorted.length) with sorted[index] <= limit; assumes sorted[from] <= limit. */
    private int lastIndexAtMost(int[] sorted, int from, int limit) {
        int low = from;
        int high = sorted.length - 1;
        while (low < high) {
            // round up, so low = mid always makes progress
            int mid = low + (high - low + 1) / 2;
            if (sorted[mid] <= limit) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }
}
