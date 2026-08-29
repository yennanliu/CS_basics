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

    // V1
    // IDEA: PREFIX MAX + SUFFIX MIN — the cut is the first i with
    //       max(nums[0..i]) <= min(nums[i+1..n-1])
    /**
     * time = O(n)
     * space = O(n)
     */
    public int partitionDisjoint_1(int[] nums) {
        int n = nums.length;
        int[] suffixMin = new int[n];
        suffixMin[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(nums[i], suffixMin[i + 1]);
        }

        int prefixMax = Integer.MIN_VALUE;
        for (int i = 0; i < n - 1; i++) {
            prefixMax = Math.max(prefixMax, nums[i]);
            if (prefixMax <= suffixMin[i + 1]) {
                return i + 1;
            }
        }
        return n - 1; // the problem guarantees a valid cut exists
    }

    // V2
    // IDEA: brute force O(n^2) — for every cut recompute max(left) and min(right)
    //       from scratch; kept as a readable correctness reference
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int partitionDisjoint_2(int[] nums) {
        int n = nums.length;
        for (int cut = 1; cut < n; cut++) {
            int leftMax = Integer.MIN_VALUE;
            for (int i = 0; i < cut; i++) {
                leftMax = Math.max(leftMax, nums[i]);
            }
            int rightMin = Integer.MAX_VALUE;
            for (int i = cut; i < n; i++) {
                rightMin = Math.min(rightMin, nums[i]);
            }
            if (leftMax <= rightMin) {
                return cut;
            }
        }
        return n - 1;
    }
}
