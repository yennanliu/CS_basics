package LeetCodeJava.Math;

// https://leetcode.com/problems/find-greatest-common-divisor-of-array/

/**
 *  1979. Find Greatest Common Divisor of Array
 *  Easy
 *
 *  Given an integer array nums, return the greatest common divisor of the
 *  smallest number and largest number in nums.
 *
 *  The greatest common divisor of two numbers is the largest positive integer
 *  that evenly divides both numbers.
 *
 *
 *  Example 1:
 *
 *  Input: nums = [2,5,6,9,10]
 *  Output: 2
 *  Explanation: min = 2, max = 10, gcd(2, 10) = 2
 *
 *  Example 2:
 *
 *  Input: nums = [7,5,6,8,3]
 *  Output: 1
 *
 *  Example 3:
 *
 *  Input: nums = [3,3]
 *  Output: 3
 *
 *
 *  Constraints:
 *
 *  2 <= nums.length <= 1000
 *  1 <= nums[i] <= 1000
 */
public class FindGreatestCommonDivisorOfArray {

    // V0
    // IDEA: find min & max, then Euclidean algorithm
    /**
     * time = O(n + log m)
     * space = O(1)
     */
    public int findGCD(int[] nums) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int x : nums) {
            if (x < min) {
                min = x;
            }
            if (x > max) {
                max = x;
            }
        }
        // Euclidean algorithm
        int a = min;
        int b = max;
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }
}
