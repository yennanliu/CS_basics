package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-index-of-a-valid-split/description/

import java.util.List;

/**
 * 2780. Minimum Index of a Valid Split
 * Medium
 *
 * An element x of an integer array arr of length m is dominant if more than half the elements of arr have a value of x.
 *
 * You are given a 0-indexed integer array nums of length n with one dominant element.
 *
 * You can split nums at an index i into two arrays nums[0, ..., i] and nums[i + 1, ..., n - 1], but the split is only valid if:
 *
 * 0 <= i < n - 1
 * nums[0, ..., i], and nums[i + 1, ..., n - 1] have the same dominant element.
 *
 * Here, nums[i, ..., j] denotes the subarray of nums starting at index i and ending at index j, both ends being inclusive. Particularly, if j < i then nums[i, ..., j] denotes an empty subarray.
 *
 * Return the minimum index of a valid split. If no valid split exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,2]
 * Output: 2
 *
 * Example 2:
 *
 * Input: nums = [2,1,3,1,1,1,7,1,2,1]
 * Output: 4
 *
 * Example 3:
 *
 * Input: nums = [3,3,3,3,7,2,2]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * nums has exactly one dominant element.
 *
 */
public class MinimumIndexOfAValidSplit {

    // V0
    // IDEA: BOYER-MOORE MAJORITY VOTE + PREFIX COUNT SWEEP
    //       only the whole array's dominant value can dominate both halves, so count just that value
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumIndex(List<Integer> nums) {
        int n = nums.size();

        // step 1: Boyer-Moore vote finds the majority candidate
        int candidate = nums.get(0);
        int votes = 0;
        for (int value : nums) {
            if (votes == 0) {
                candidate = value;
                votes = 1;
            } else if (value == candidate) {
                votes++;
            } else {
                votes--;
            }
        }

        int total = 0;
        for (int value : nums) {
            if (value == candidate) {
                total++;
            }
        }
        if (total * 2 <= n) {
            return -1;
        }

        // step 2: first i where the candidate dominates both nums[0..i] and nums[i+1..n-1]
        int leftCount = 0;
        for (int i = 0; i < n - 1; i++) {
            if (nums.get(i) == candidate) {
                leftCount++;
            }
            int leftSize = i + 1;
            int rightSize = n - i - 1;
            if (leftCount * 2 > leftSize && (total - leftCount) * 2 > rightSize) {
                return i;
            }
        }
        return -1;
    }
}
