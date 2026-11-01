package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-number-of-removals-to-make-mountain-array/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1671. Minimum Number of Removals to Make Mountain Array
 * Hard
 *
 * You may recall that an array arr is a mountain array if and only if:
 *
 * arr.length >= 3
 * There exists some index i (0-indexed) with 0 < i < arr.length - 1 such that:
 *     arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 *     arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 *
 * Given an integer array nums, return the minimum number of elements to remove to make nums a
 * mountain array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,1]
 * Output: 0
 * Explanation: The array itself is a mountain array so we do not need to remove any elements.
 *
 * Example 2:
 *
 * Input: nums = [2,1,1,5,6,2,3,1]
 * Output: 3
 * Explanation: One solution is to remove the elements at indices 0, 1, and 5, making the array
 * nums = [1,5,6,3,1].
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 1000
 * 1 <= nums[i] <= 10^9
 * It is guaranteed that you can make a mountain array out of nums.
 *
 */
public class MinimumNumberOfRemovalsToMakeMountainArray {

    // V0
    // IDEA: LIS FROM BOTH SIDES + BINARY SEARCH (KEEP THE LONGEST MOUNTAIN)
    /**
     *  minimum removals = n - (longest mountain subsequence).
     *
     *  For every candidate peak i:
     *    increasing[i] = longest strictly increasing subsequence ending at i
     *    decreasing[i] = longest strictly decreasing subsequence starting at i
     *    mountain through i = increasing[i] + decreasing[i] - 1  (i counted once)
     *
     *  decreasing[] is the same LIS computed from the right. Each LIS uses the patience
     *  "tails" list + lower bound, where the length ending at i = insertion position + 1.
     *  A peak needs both slopes non-empty: only accept i with both lengths > 1.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimumMountainRemovals(int[] nums) {
        int n = nums.length;

        // left to right: LIS ending at each index
        int[] increasing = new int[n];
        List<Integer> tails = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            increasing[i] = placeInTails(tails, nums[i]) + 1;
        }

        // right to left: LIS ending at each index when read backwards = LDS starting there
        int[] decreasing = new int[n];
        tails = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            decreasing[i] = placeInTails(tails, nums[i]) + 1;
        }

        int longestMountain = 0;
        for (int i = 0; i < n; i++) {
            // a peak needs at least one element on each slope
            if (increasing[i] > 1 && decreasing[i] > 1) {
                longestMountain = Math.max(longestMountain, increasing[i] + decreasing[i] - 1);
            }
        }
        return n - longestMountain;
    }

    /**
     * Puts value into the patience tails list (smallest tail per length, strictly increasing)
     * and returns the position it took.
     */
    private int placeInTails(List<Integer> tails, int value) {
        // lower bound: first tail >= value, so equal values never extend a sequence
        int left = 0;
        int right = tails.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (tails.get(mid) >= value) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (left == tails.size()) {
            tails.add(value);
        } else {
            tails.set(left, value);
        }
        return left;
    }

    // V1
    // IDEA: LIS FROM BOTH SIDES WITH O(N^2) DP (fine for n <= 1000)
    /**
     *  increasing[i] = 1 + max(increasing[j]) over j < i with nums[j] < nums[i]
     *  decreasing[i] = 1 + max(decreasing[j]) over j > i with nums[j] < nums[i]
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minimumMountainRemovals_1(int[] nums) {
        int n = nums.length;
        int[] increasing = new int[n];
        int[] decreasing = new int[n];

        for (int i = 0; i < n; i++) {
            increasing[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    increasing[i] = Math.max(increasing[i], increasing[j] + 1);
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            decreasing[i] = 1;
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) {
                    decreasing[i] = Math.max(decreasing[i], decreasing[j] + 1);
                }
            }
        }

        int longestMountain = 0;
        for (int i = 0; i < n; i++) {
            if (increasing[i] > 1 && decreasing[i] > 1) {
                longestMountain = Math.max(longestMountain, increasing[i] + decreasing[i] - 1);
            }
        }
        return n - longestMountain;
    }
}
