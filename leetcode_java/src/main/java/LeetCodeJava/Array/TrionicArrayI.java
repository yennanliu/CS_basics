package LeetCodeJava.Array;

// https://leetcode.com/problems/trionic-array-i/description/

/**
 * 3637. Trionic Array I
 * Easy
 *
 * You are given an integer array nums of length n.
 *
 * An array is trionic if there exist indices 0 < p < q < n - 1 such that:
 *
 * nums[0...p] is strictly increasing,
 * nums[p...q] is strictly decreasing,
 * nums[q...n - 1] is strictly increasing.
 *
 * Return true if nums is trionic, otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,4,2,6]
 * Output: true
 * Explanation: Pick p = 2, q = 4.
 *
 * Example 2:
 *
 * Input: nums = [2,1,3]
 * Output: false
 *
 *
 * Constraints:
 *
 * 3 <= n <= 100
 * -1000 <= nums[i] <= 1000
 *
 */
public class TrionicArrayI {

    // V0
    // IDEA: WALK THE THREE MONOTONE RUNS GREEDILY
    //   the sections are forced: the maximal increasing run from index 0, then the
    //   maximal decreasing run, then the rest must be increasing to the end. each
    //   section must actually advance the pointer (p > 0, q > p, n - 1 > q).
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isTrionic(int[] nums) {
        int n = nums.length;
        int i = 0;

        // 1) strictly increasing run from the start
        while (i + 1 < n && nums[i] < nums[i + 1]) {
            i++;
        }
        int peak = i;
        if (peak == 0 || peak == n - 1) {
            return false;
        }

        // 2) strictly decreasing run
        while (i + 1 < n && nums[i] > nums[i + 1]) {
            i++;
        }
        int valley = i;
        if (valley == peak || valley == n - 1) {
            return false;
        }

        // 3) strictly increasing run that must reach the last index
        while (i + 1 < n && nums[i] < nums[i + 1]) {
            i++;
        }
        return i == n - 1;
    }
}
