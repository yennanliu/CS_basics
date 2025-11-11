package LeetCodeJava.Sort;

// https://leetcode.com/problems/wiggle-sort-ii/description/

import java.util.Arrays;

/**
 *
 *   324. Wiggle Sort II
 * Attempted
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....
 *
 * You may assume the input array always has a valid answer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,1,1,6,4]
 * Output: [1,6,1,5,1,4]
 * Explanation: [1,4,1,5,1,6] is also accepted.
 * Example 2:
 *
 * Input: nums = [1,3,2,2,3,1]
 * Output: [2,3,1,3,1,2]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * 0 <= nums[i] <= 5000
 * It is guaranteed that there will be an answer for the given input nums.
 *
 *
 * Follow Up: Can you do it in O(n) time and/or in-place with O(1) extra space?
 *
 */
public class WiggleSort2 {

    // V0
//    public void wiggleSort(int[] nums) {
//
//    }

    // V1
    // https://leetcode.ca/2016-10-19-324-Wiggle-Sort-II/
    public void wiggleSort_1(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int n = nums.length;
        int i = (n - 1) >> 1, j = n - 1;
        for (int k = 0; k < n; ++k) {
            if (k % 2 == 0) {
                nums[k] = arr[i--];
            } else {
                nums[k] = arr[j--];
            }
        }
    }

    // V2
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/wiggle-sort-ii/solutions/6956714/simple-and-beginner-friendly-solution-be-np09/
    public void wiggleSort_2(int[] nums) {
        int n = nums.length-1;
        int[] arr = Arrays.copyOf(nums,nums.length);
        Arrays.sort(arr);
        for(int i=1;i<nums.length;i+=2){
            nums[i]=arr[n--];
        }
        for(int i=0;i<nums.length;i+=2){
            nums[i]=arr[n--];
        }
    }


    // V3


}
