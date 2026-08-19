package LeetCodeJava.Sort;

// https://leetcode.com/problems/wiggle-sort/

/**
 *  280. Wiggle Sort
 *  Medium
 *
 *  Given an integer array nums, reorder it such that
 *  nums[0] <= nums[1] >= nums[2] <= nums[3]....
 *
 *  You may assume the input array always has a valid answer.
 *
 *
 *  Example 1:
 *
 *  Input: nums = [3,5,2,1,6,4]
 *  Output: [3,5,1,6,2,4]
 *  Explanation: [1,6,2,5,3,4] is also accepted.
 *
 *  Example 2:
 *
 *  Input: nums = [6,6,5,6,3,8]
 *  Output: [6,6,5,6,3,8]
 *
 *
 *  Constraints:
 *
 *  1 <= nums.length <= 5 * 10^4
 *  0 <= nums[i] <= 10^4
 *  It is guaranteed that there will be an answer for the given input nums.
 */
public class WiggleSort {

    // V0
    // IDEA: GREEDY ONE PASS.
    //       Walking left to right, whenever the local relation is violated
    //       swap the pair. Swapping never breaks the already fixed prefix,
    //       because the swapped-in element only moves in the "safe" direction.
    /**
     * time = O(n)
     * space = O(1)
     */
    public void wiggleSort(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        for (int i = 1; i < nums.length; i++) {
            // even index -> want nums[i] <= nums[i-1]
            // odd  index -> want nums[i] >= nums[i-1]
            boolean shouldSwap = (i % 2 == 1) ? (nums[i] < nums[i - 1]) : (nums[i] > nums[i - 1]);
            if (shouldSwap) {
                int tmp = nums[i];
                nums[i] = nums[i - 1];
                nums[i - 1] = tmp;
            }
        }
    }
}
