package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-adjacent-swaps-to-make-a-valid-array/description/

import java.util.*;

/**
 * 2340. Minimum Adjacent Swaps to Make a Valid Array
 * Medium
 *
 *
 * You are given a 0-indexed integer array nums.
 *
 * Swaps of adjacent elements are able to be performed on nums.
 *
 * A valid array meets the following conditions:
 *
 * The largest element (any of the largest elements if there are multiple) is at the rightmost position in the array.
 * The smallest element (any of the smallest elements if there are multiple) is at the leftmost position in the array.
 *
 * Return the minimum swaps required to make nums a valid array.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,5,5,3,1]
 * Output: 6
 * Explanation: Perform the following swaps:
 * - Swap 1: Swap the 3rd and 4th elements, nums is then [3,4,5,3,5,1].
 * - Swap 2: Swap the 4th and 5th elements, nums is then [3,4,5,3,1,5].
 * - Swap 3: Swap the 3rd and 4th elements, nums is then [3,4,5,1,3,5].
 * - Swap 4: Swap the 2nd and 3rd elements, nums is then [3,4,1,5,3,5].
 * - Swap 5: Swap the 1st and 2nd elements, nums is then [3,1,4,5,3,5].
 * - Swap 6: Swap the 0th and 1st elements, nums is then [1,3,4,5,3,5].
 * It can be shown that 6 swaps is the minimum swaps required to make a valid array.
 *
 * Example 2:
 *
 * Input: nums = [9]
 * Output: 0
 * Explanation: The array is already valid, so we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MinimumAdjacentSwapsToMakeAValidArray {

    // V0
    // IDEA: MOVE THE LEFTMOST MINIMUM TO THE FRONT, THE RIGHTMOST MAXIMUM TO THE BACK
    //       costs minIdx + (n - 1 - maxIdx) swaps; if minIdx > maxIdx the two cross
    //       once and that one swap is shared, so subtract 1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumSwaps(int[] nums) {
        int n = nums.length;

        // leftmost minimum: strict < keeps the first occurrence
        int minIdx = 0;
        // rightmost maximum: >= moves to the last occurrence
        int maxIdx = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] < nums[minIdx]) {
                minIdx = i;
            }
            if (nums[i] >= nums[maxIdx]) {
                maxIdx = i;
            }
        }

        int swaps = minIdx + (n - 1 - maxIdx);
        // the two elements pass each other in one shared swap
        if (minIdx > maxIdx) {
            swaps--;
        }
        return swaps;
    }
}
