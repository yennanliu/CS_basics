package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-frequency-of-an-element-after-performing-operations-ii/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 3347. Maximum Frequency of an Element After Performing Operations II
 * Hard
 *
 * You are given an integer array nums and two integers k and numOperations.
 *
 * You must perform an operation numOperations times on nums, where in each operation you:
 *
 * Select an index i that was not selected in any previous operations.
 * Add an integer in the range [-k, k] to nums[i].
 *
 * Return the maximum possible frequency of any element in nums after performing the operations.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,5], k = 1, numOperations = 2
 * Output: 2
 * Explanation:
 * We can achieve a maximum frequency of two by:
 * Adding 0 to nums[1], after which nums becomes [1, 4, 5].
 * Adding -1 to nums[2], after which nums becomes [1, 4, 4].
 *
 * Example 2:
 *
 * Input: nums = [5,11,20,20], k = 5, numOperations = 1
 * Output: 2
 * Explanation:
 * We can achieve a maximum frequency of two by:
 * Adding 0 to nums[1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 0 <= k <= 10^9
 * 0 <= numOperations <= nums.length
 *
 */
public class MaximumFrequencyOfAnElementAfterPerformingOperations2 {

    // V0
    // IDEA: CANDIDATE TARGETS + SORT + BINARY SEARCH COUNTS
    // for a target v the reachable frequency is
    //   (# already equal to v) + min(numOperations, # others within [v - k, v + k]).
    // The optimum is attained at some nums[i] or at a window edge nums[i] +- k, so only 3n
    // candidates are tested. Values reach 1e9, so nothing is indexed by value: count with
    // lower/upper bounds on the sorted array instead.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxFrequency(int[] nums, int k, int numOperations) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        Set<Long> candidates = new HashSet<>();
        for (int value : sorted) {
            candidates.add((long) value);
            candidates.add((long) value - k);
            candidates.add((long) value + k);
        }

        int best = 0;
        for (long target : candidates) {
            // elements inside [target - k, target + k] can be moved onto target
            int inRange = firstGreater(sorted, target + k) - firstAtLeast(sorted, target - k);
            int alreadyEqual = firstGreater(sorted, target) - firstAtLeast(sorted, target);
            int movable = inRange - alreadyEqual;
            int frequency = alreadyEqual + Math.min(numOperations, movable);
            best = Math.max(best, frequency);
        }
        return best;
    }

    /** First index with sorted[index] >= target. */
    private int firstAtLeast(int[] sorted, long target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** First index with sorted[index] > target. */
    private int firstGreater(int[] sorted, long target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
