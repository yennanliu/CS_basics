package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-count-of-positive-integer-and-negative-integer/description/

/**
 * 2529. Maximum Count of Positive Integer and Negative Integer
 * Easy
 *
 * Given an array nums sorted in non-decreasing order, return the maximum between
 * the number of positive integers and the number of negative integers.
 *
 * In other words, if the number of positive integers in nums is pos and the number
 * of negative integers is neg, then return the maximum of pos and neg.
 *
 * Note that 0 is neither positive nor negative.
 *
 *
 * Example 1:
 *
 * Input: nums = [-2,-1,-1,1,2,3]
 * Output: 3
 * Explanation: There are 3 positive integers and 3 negative integers.
 * The maximum count among them is 3.
 *
 * Example 2:
 *
 * Input: nums = [-3,-2,-1,0,0,1,2]
 * Output: 3
 * Explanation: There are 2 positive integers and 3 negative integers.
 * The maximum count among them is 3.
 *
 * Example 3:
 *
 * Input: nums = [5,20,66,1314]
 * Output: 4
 * Explanation: There are 4 positive integers and 0 negative integers.
 * The maximum count among them is 4.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2000
 * -2000 <= nums[i] <= 2000
 * nums is sorted in a non-decreasing order.
 *
 * Follow up: Can you solve the problem in O(log(n)) time complexity?
 *
 */
public class MaximumCountOfPositiveIntegerAndNegativeInteger {

    // V0
    // IDEA: BINARY SEARCH FOR THE TWO BOUNDARIES OF THE ZERO BLOCK
    // negatives, zeros and positives each form one contiguous block in a sorted array:
    //   neg = first index with value >= 0        (lower bound of 0)
    //   pos = n - first index with value > 0     (n - upper bound of 0)
    // the zeros belong to neither count, so the two bounds are NOT interchangeable.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int maximumCount(int[] nums) {
        int n = nums.length;
        int negatives = firstIndexAtLeast(nums, 0);
        int positives = n - firstIndexAtLeast(nums, 1);
        return Math.max(negatives, positives);
    }

    /** First index whose value is >= target (n if none). */
    private int firstIndexAtLeast(int[] nums, int target) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    // V1
    // IDEA: LINEAR COUNT (simplest; fine for n <= 2000 but not the O(log n) follow-up)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumCount_1(int[] nums) {
        int negatives = 0;
        int positives = 0;
        for (int num : nums) {
            if (num < 0) {
                negatives++;
            } else if (num > 0) {
                positives++;
            }
        }
        return Math.max(negatives, positives);
    }
}
