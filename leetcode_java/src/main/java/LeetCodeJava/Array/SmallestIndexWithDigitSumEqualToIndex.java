package LeetCodeJava.Array;

// https://leetcode.com/problems/smallest-index-with-digit-sum-equal-to-index/description/

/**
 * 3550. Smallest Index With Digit Sum Equal to Index
 * Easy
 *
 * You are given an integer array nums.
 *
 * Return the smallest index i such that the sum of the digits of nums[i] is equal to i.
 *
 * If no such index exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2]
 * Output: 2
 *
 * Example 2:
 *
 * Input: nums = [1,10,11]
 * Output: 1
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 1000
 *
 */
public class SmallestIndexWithDigitSumEqualToIndex {

    // V0
    // IDEA: SCAN LEFT TO RIGHT AND STOP AT THE FIRST HIT
    //   we want the smallest index, so the first match of a left-to-right scan is the answer.
    /**
     * time = O(N * D)   (D = number of digits, at most 4)
     * space = O(1)
     */
    public int smallestIndex(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (digitSum(nums[i]) == i) {
                return i;
            }
        }
        return -1;
    }

    /** Sum of the decimal digits of a non-negative number. */
    private int digitSum(int value) {
        int sum = 0;
        while (value > 0) {
            sum += value % 10;
            value /= 10;
        }
        return sum;
    }
}
