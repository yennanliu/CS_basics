package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-number-of-operations-to-make-array-continuous/

import java.util.Arrays;

/**
 *  2009. Minimum Number of Operations to Make Array Continuous
 *  Hard
 *
 *  You are given an integer array nums. In one operation, you can replace any
 *  element in nums with any integer.
 *
 *  nums is considered continuous if both of the following conditions are fulfilled:
 *   - All elements in nums are unique.
 *   - The difference between the maximum element and the minimum element in nums
 *     equals nums.length - 1.
 *
 *  For example, nums = [4,2,5,3] is continuous, but nums = [1,2,3,5,6] is not.
 *
 *  Return the minimum number of operations to make nums continuous.
 *
 *  Example 1:
 *   Input: nums = [4,2,5,3]
 *   Output: 0
 *
 *  Example 2:
 *   Input: nums = [1,2,3,5,6]
 *   Output: 1  (change the last element to 4)
 *
 *  Example 3:
 *   Input: nums = [1,10,100,1000]
 *   Output: 3
 *
 *  Constraints:
 *   1 <= nums.length <= 10^5
 *   1 <= nums[i] <= 10^9
 */
public class MinimumNumberOfOperationsToMakeArrayContinuous {

    // V0
    // IDEA: sort + dedup, then for each unique left end binary search the
    //       furthest unique value that still fits inside window [x, x + n - 1];
    //       keep as many original elements as possible.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minOperations(int[] nums) {
        int n = nums.length;
        int[] uniq = dedup(nums);
        int m = uniq.length;

        int keep = 0;
        for (int i = 0; i < m; i++) {
            // largest value allowed in this window
            long limit = (long) uniq[i] + n - 1;
            // first index whose value > limit
            int j = upperBound(uniq, limit);
            keep = Math.max(keep, j - i);
        }
        return n - keep;
    }

    private int[] dedup(int[] nums) {
        int[] copy = nums.clone();
        Arrays.sort(copy);
        int m = 0;
        for (int i = 0; i < copy.length; i++) {
            if (i == 0 || copy[i] != copy[i - 1]) {
                copy[m++] = copy[i];
            }
        }
        return Arrays.copyOf(copy, m);
    }

    // first index idx in [0, arr.length] with arr[idx] > target
    private int upperBound(int[] arr, long target) {
        int lo = 0, hi = arr.length;
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

    // V1
    // IDEA: same window, but slide the left pointer instead of binary searching
    //       (two pointers over the deduped sorted array).
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minOperations_1(int[] nums) {
        int n = nums.length;
        int[] uniq = dedup(nums);
        int m = uniq.length;

        int keep = 0;
        int left = 0;
        for (int right = 0; right < m; right++) {
            while ((long) uniq[right] - uniq[left] > n - 1) {
                left++;
            }
            keep = Math.max(keep, right - left + 1);
        }
        return n - keep;
    }
}
