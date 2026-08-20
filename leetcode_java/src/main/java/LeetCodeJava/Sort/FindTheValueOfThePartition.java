package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-the-value-of-the-partition/

import java.util.Arrays;

/**
 *  2740. Find the Value of the Partition
 *  Medium
 *
 *  You are given a positive integer array nums.
 *
 *  Partition nums into two arrays, nums1 and nums2, such that:
 *    - Each element of the array nums belongs to either nums1 or nums2.
 *    - Both arrays are non-empty.
 *    - The value of the partition is minimized.
 *
 *  The value of the partition is |max(nums1) - min(nums2)|.
 *
 *  Return the integer denoting the value of such partition.
 *
 *  Example 1:
 *    Input: nums = [1,3,2,4]
 *    Output: 1
 *    Explanation: nums1 = [1,2], nums2 = [3,4] -> |2 - 3| = 1, the minimum.
 *
 *  Example 2:
 *    Input: nums = [100,1,10]
 *    Output: 9
 *    Explanation: nums1 = [10], nums2 = [100,1] -> |10 - 1| = 9, the minimum.
 *
 *  Constraints:
 *    2 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 */
public class FindTheValueOfThePartition {

    // V0
    // IDEA: SORT + MIN ADJACENT DIFFERENCE
    //       let a = max(nums1) and b = min(nums2); the value is |a - b| where a
    //       and b sit at two DISTINCT positions of the array. so the answer is at
    //       least the smallest |nums[i] - nums[j]| over any pair i != j, i.e.
    //       after sorting the smallest gap between two ADJACENT elements.
    //       that bound is always reachable: sort, pick the adjacent pair with the
    //       smallest gap, and put nums[0..i] into nums1 and nums[i+1..] into
    //       nums2 - then max(nums1) = nums[i], min(nums2) = nums[i+1] and both
    //       halves are non-empty.
    //       no abs() is needed after sorting, and duplicates give a gap of 0.
    /**
     * time = O(n log n)
     * space = O(n)   // for the sort
     */
    public int findValueOfPartition(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);

        int res = arr[1] - arr[0];
        for (int i = 1; i < arr.length - 1; i++) {
            int gap = arr[i + 1] - arr[i];
            if (gap < res) {
                res = gap;
            }
        }
        return res;
    }
}
