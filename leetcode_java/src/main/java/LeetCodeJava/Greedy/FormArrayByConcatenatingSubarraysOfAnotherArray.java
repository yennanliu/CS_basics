package LeetCodeJava.Greedy;

// https://leetcode.com/problems/form-array-by-concatenating-subarrays-of-another-array/description/

/**
 * 1764. Form Array by Concatenating Subarrays of Another Array
 * Medium
 *
 * You are given a 2D integer array groups of length n. You are also given an
 * integer array nums.
 *
 * You are asked if you can choose n disjoint subarrays from the array nums such
 * that the i^th subarray is equal to groups[i] (0-indexed), and if i > 0, the
 * (i-1)^th subarray appears before the i^th subarray in nums (i.e. the subarrays
 * must be in the same order as groups).
 *
 * Return true if you can do this task, and false otherwise.
 *
 * Note that the subarrays are disjoint if and only if there is no index k such
 * that nums[k] belongs to more than one subarray. A subarray is a contiguous
 * sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: groups = [[1,-1,-1],[3,-2,0]], nums = [1,-1,0,1,-1,-1,3,-2,0]
 * Output: true
 * Explanation: You can choose the 0^th subarray as [1,-1,-1] (indices 3..5) and
 * the 1^st one as [3,-2,0] (indices 6..8). These subarrays are disjoint.
 *
 * Example 2:
 *
 * Input: groups = [[10,-2],[1,2,3,4]], nums = [1,2,3,4,10,-2]
 * Output: false
 * Explanation: [10,-2] must come before [1,2,3,4].
 *
 * Example 3:
 *
 * Input: groups = [[1,2,3],[3,4]], nums = [7,7,1,2,3,4,7,7]
 * Output: false
 * Explanation: The two subarrays would share nums[4], so they are not disjoint.
 *
 *
 * Constraints:
 *
 * groups.length == n
 * 1 <= n <= 10^3
 * 1 <= groups[i].length, sum(groups[i].length) <= 10^3
 * 1 <= nums.length <= 10^3
 * -10^7 <= groups[i][j], nums[k] <= 10^7
 *
 */
public class FormArrayByConcatenatingSubarraysOfAnotherArray {

    // V0
    // IDEA: GREEDY TWO POINTERS (match each group at its earliest possible position)
    /**
     * time = O(N * S), N = nums.length, S = sum of group lengths
     * space = O(1)
     */
    public boolean canChoose(int[][] groups, int[] nums) {
        int groupIdx = 0;
        int numsIdx = 0;

        while (groupIdx < groups.length && numsIdx < nums.length) {
            int[] group = groups[groupIdx];
            if (matchesAt(nums, numsIdx, group)) {
                // matching as early as possible leaves the longest suffix for the rest;
                // jumping past the match keeps the subarrays disjoint
                numsIdx += group.length;
                groupIdx++;
            } else {
                numsIdx++;
            }
        }

        // success means every group was consumed
        return groupIdx == groups.length;
    }

    /** True if group appears in nums starting at index start. */
    private boolean matchesAt(int[] nums, int start, int[] group) {
        if (start + group.length > nums.length) {
            return false;
        }
        for (int k = 0; k < group.length; k++) {
            if (nums[start + k] != group[k]) {
                return false;
            }
        }
        return true;
    }
}
