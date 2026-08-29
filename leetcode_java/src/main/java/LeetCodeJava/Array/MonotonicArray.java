package LeetCodeJava.Array;

import java.util.Arrays;

// https://leetcode.com/problems/monotonic-array/

/**
 *  896. Monotonic Array
 *  Easy
 *
 *  An array is monotonic if it is either monotone increasing or monotone
 *  decreasing.
 *
 *  An array nums is monotone increasing if for all i <= j,
 *  nums[i] <= nums[j]. An array nums is monotone decreasing if for all i <= j,
 *  nums[i] >= nums[j].
 *
 *  Given an integer array nums, return true if the given array is monotonic, or
 *  false otherwise.
 *
 *  Example 1:
 *  Input: nums = [1,2,2,3]
 *  Output: true
 *
 *  Example 2:
 *  Input: nums = [6,5,4,4]
 *  Output: true
 *
 *  Example 3:
 *  Input: nums = [1,3,2]
 *  Output: false
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - -10^5 <= nums[i] <= 10^5
 */
public class MonotonicArray {

    // V0
    // IDEA: one pass, keep two flags; an array is monotonic iff it never both
    //       goes up somewhere and goes down somewhere
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isMonotonic(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return true;
        }
        boolean increasing = true;
        boolean decreasing = true;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                decreasing = false;
            } else if (nums[i] < nums[i - 1]) {
                increasing = false;
            }
            if (!increasing && !decreasing) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: SORT — the array is monotonic iff it already equals its ascending sort
    //       or that same sort read backwards
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public boolean isMonotonic_1(int[] nums) {
        int n = nums.length;
        int[] asc = nums.clone();
        Arrays.sort(asc);
        if (Arrays.equals(nums, asc)) {
            return true;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != asc[n - 1 - i]) {
                return false;
            }
        }
        return true;
    }

    // V2
    // IDEA: brute force O(n^2) — check the definition directly over every pair i < j;
    //       kept as a readable correctness reference
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public boolean isMonotonic_2(int[] nums) {
        return checkAllPairs_2(nums, true) || checkAllPairs_2(nums, false);
    }

    private boolean checkAllPairs_2(int[] nums, boolean nonDecreasing) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nonDecreasing && nums[i] > nums[j]) {
                    return false;
                }
                if (!nonDecreasing && nums[i] < nums[j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
