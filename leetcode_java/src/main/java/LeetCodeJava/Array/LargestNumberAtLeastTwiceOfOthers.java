package LeetCodeJava.Array;

import java.util.Arrays;

// https://leetcode.com/problems/largest-number-at-least-twice-of-others/

/**
 *  747. Largest Number At Least Twice of Others
 *  Easy
 *
 *  You are given an integer array nums where the largest integer is unique.
 *
 *  Determine whether the largest element in the array is at least twice as much
 *  as every other number in the array. If it is, return the index of the largest
 *  element, or return -1 otherwise.
 *
 *  Example 1:
 *    Input: nums = [3,6,1,0]
 *    Output: 1
 *    Explanation: 6 is the largest integer. For every other number in the array x,
 *    6 is at least twice as big as x. The index of value 6 is 1, so we return 1.
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4]
 *    Output: -1
 *    Explanation: 4 is less than twice the value of 3, so we return -1.
 *
 *  Constraints:
 *    2 <= nums.length <= 50
 *    0 <= nums[i] <= 100
 *    The largest element in nums is unique.
 */
public class LargestNumberAtLeastTwiceOfOthers {

    // V0
    // IDEA: one pass tracking the largest and the second largest value.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int dominantIndex(int[] nums) {
        int best = -1, second = -1, bestIdx = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > best) {
                second = best;
                best = nums[i];
                bestIdx = i;
            } else if (nums[i] > second) {
                second = nums[i];
            }
        }
        return best >= 2 * second ? bestIdx : -1;
    }


    // V1
    // IDEA: two pass - locate the max index, then verify it against every other element
    /**
     * time = O(n)
     * space = O(1)
     */
    public int dominantIndex_1(int[] nums) {
        int maxIdx = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[maxIdx]) {
                maxIdx = i;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (i == maxIdx) {
                continue;
            }
            if (nums[maxIdx] < 2 * nums[i]) {
                return -1;
            }
        }
        return maxIdx;
    }

    // V2
    // IDEA: SORTING a copy - the two biggest values sit at the tail, then map back to an index
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int dominantIndex_2(int[] nums) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int largest = sorted[n - 1];
        int second = n >= 2 ? sorted[n - 2] : 0;
        if (largest < 2 * second) {
            return -1;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] == largest) {
                return i;
            }
        }
        return -1;
    }
}
