package LeetCodeJava.Array;

// https://leetcode.com/problems/partition-array-into-disjoint-intervals/

/**
 *  915. Partition Array into Disjoint Intervals
 *  Medium
 *
 *  Given an integer array nums, partition it into two (contiguous) subarrays left
 *  and right so that:
 *   - Every element in left is less than or equal to every element in right.
 *   - left and right are non-empty.
 *   - left has the smallest possible size.
 *
 *  Return the length of left after such a partitioning.
 *
 *  Test cases are generated such that partitioning exists.
 *
 *  Example 1:
 *  Input: nums = [5,0,3,8,6]
 *  Output: 3
 *  Explanation: left = [5,0,3], right = [8,6]
 *
 *  Example 2:
 *  Input: nums = [1,1,1,0,6,12]
 *  Output: 4
 *  Explanation: left = [1,1,1,0], right = [6,12]
 *
 *  Constraints:
 *   - 2 <= nums.length <= 10^5
 *   - 0 <= nums[i] <= 10^6
 *   - There is at least one valid answer for the given input.
 */
public class PartitionArrayIntoDisjointIntervals {

    // V0
    // IDEA: one pass greedy. Keep `leftMax` (max of the committed left part) and
    //       `curMax` (max seen so far). Whenever nums[i] < leftMax the partition
    //       must be extended to include i, so commit leftMax = curMax.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int partitionDisjoint(int[] nums) {
        int leftMax = nums[0];
        int curMax = nums[0];
        int partitionIdx = 0;
        for (int i = 1; i < nums.length; i++) {
            curMax = Math.max(curMax, nums[i]);
            if (nums[i] < leftMax) {
                leftMax = curMax;
                partitionIdx = i;
            }
        }
        return partitionIdx + 1;
    }
}
