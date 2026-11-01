package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/description/

import java.util.*;

/**
 * 1464. Maximum Product of Two Elements in an Array
 * Easy
 *
 * Given the array of integers nums, you will choose two different indices i and j of that
 * array. Return the maximum value of (nums[i]-1)*(nums[j]-1).
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,5,2]
 * Output: 12
 * Explanation: If you choose the indices i=1 and j=2 (indexed from 0), you will get the
 * maximum value, that is, (nums[1]-1)*(nums[2]-1) = (4-1)*(5-1) = 3*4 = 12.
 *
 * Example 2:
 *
 * Input: nums = [1,5,4,5]
 * Output: 16
 *
 * Example 3:
 *
 * Input: nums = [3,7]
 * Output: 12
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 500
 * 1 <= nums[i] <= 10^3
 *
 */
public class MaximumProductOfTwoElementsInAnArray {

    // V0
    // IDEA: TRACK THE TWO LARGEST VALUES — nums[i] >= 1, so (nums[i] - 1) is never negative
    //       and the product is maximised by the two biggest entries (duplicates allowed,
    //       only the indices must differ)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProduct(int[] nums) {
        int largest = 0;
        int secondLargest = 0;

        for (int num : nums) {
            if (num > largest) {
                // the old largest is demoted to second place
                secondLargest = largest;
                largest = num;
            } else if (num > secondLargest) {
                secondLargest = num;
            }
        }
        return (largest - 1) * (secondLargest - 1);
    }

    // V1
    // IDEA: SORT, THEN TAKE THE TWO LARGEST — no case analysis; the shape to reach for when
    //       negatives are possible and both ends of the sorted array must be inspected
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxProduct_1(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        return (sorted[n - 1] - 1) * (sorted[n - 2] - 1);
    }
}
