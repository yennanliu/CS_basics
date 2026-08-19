package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-frequency-of-an-element-after-performing-operations-i/

import java.util.Arrays;

/**
 *  3346. Maximum Frequency of an Element After Performing Operations I
 *  Medium
 *
 *  You are given an integer array nums and two integers k and numOperations.
 *
 *  You must perform an operation numOperations times on nums, where in each
 *  operation you:
 *   - Select an index i that was not selected in any previous operations.
 *   - Add an integer in the range [-k, k] to nums[i].
 *
 *  Return the maximum possible frequency of any element in nums after
 *  performing the operations.
 *
 *  Example 1:
 *    Input: nums = [1,4,5], k = 1, numOperations = 2
 *    Output: 2
 *    Explanation: add 0 to nums[1] and -1 to nums[2] -> [1,4,4].
 *
 *  Example 2:
 *    Input: nums = [5,11,20,20], k = 5, numOperations = 1
 *    Output: 2
 *    Explanation: add 0 to nums[1].
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^5
 *    0 <= k <= 10^5
 *    0 <= numOperations <= nums.length
 */
public class MaximumFrequencyOfAnElementAfterPerformingOperationsI {

    // V0
    // IDEA: FIX THE TARGET VALUE — "ALREADY THERE" PLUS "CAN BE NUDGED"
    //       for a chosen target v the final frequency is
    //           (elements already equal to v)
    //         + min(numOperations, elements within k of v that are not v)
    //       because an element in [v-k, v+k] can be moved onto v with one
    //       operation, and only numOperations of them may be moved.
    //       the best v is always some nums[i] or a boundary nums[i] +- k —
    //       sliding the target between those points never adds anybody — so
    //       those are the only candidates worth testing.
    //       sorting once turns "how many lie in [v-k, v+k]" and "how many
    //       equal v" into binary searches.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxFrequency(int[] nums, int k, int numOperations) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int n = arr.length;

        int[] candidates = new int[3 * n];
        int m = 0;
        for (int i = 0; i < n; i++) {
            candidates[m++] = arr[i];
            candidates[m++] = arr[i] - k;
            candidates[m++] = arr[i] + k;
        }

        int best = 0;
        for (int i = 0; i < m; i++) {
            int v = candidates[i];
            int lo = lowerBound(arr, v - k);
            int hi = upperBound(arr, v + k);
            int inside = hi - lo;
            int same = upperBound(arr, v) - lowerBound(arr, v);
            int movable = inside - same;
            int total = same + Math.min(numOperations, movable);
            if (total > best) {
                best = total;
            }
        }
        return best;
    }

    // first index with arr[idx] >= target
    private int lowerBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // first index with arr[idx] > target
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
}
