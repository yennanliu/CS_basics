package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-subarray-with-equal-products/description/

/**
 * 3411. Maximum Subarray With Equal Products
 * Easy
 *
 * You are given an array of positive integers nums.
 *
 * An array arr is called product equivalent if prod(arr) == lcm(arr) * gcd(arr), where:
 *
 * prod(arr) is the product of all elements of arr.
 * gcd(arr) is the GCD of all elements of arr.
 * lcm(arr) is the LCM of all elements of arr.
 *
 * Return the length of the longest product equivalent subarray of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,2,1,1,1]
 * Output: 5
 * Explanation:
 * The longest product equivalent subarray is [1, 2, 1, 1, 1], where prod([1, 2, 1, 1, 1]) = 2,
 * gcd([1, 2, 1, 1, 1]) = 1, and lcm([1, 2, 1, 1, 1]) = 2.
 *
 * Example 2:
 *
 * Input: nums = [2,3,4,5,6]
 * Output: 3
 * Explanation:
 * The longest product equivalent subarray is [3, 4, 5].
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,1,4,5,1]
 * Output: 5
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 10
 *
 */
public class MaximumSubarrayWithEqualProducts {

    // V0
    // IDEA: BRUTE FORCE EVERY START, EXTENDING prod / gcd / lcm INCREMENTALLY, WITH AN EARLY BREAK
    //   prod / (lcm * gcd) never decreases as the subarray grows (prod gains a factor v, lcm gains at
    //   most v, gcd can only shrink), so once prod > lcm * gcd no longer extension can match. That
    //   break also keeps prod <= lcm(1..10) * 10 * 10, so it never overflows. (The ratio can start
    //   below 1 — a single element v has prod = v but lcm * gcd = v * v — so only "above" breaks.)
    /**
     * time = O(N^2 * log V)
     * space = O(1)
     */
    public int maxLength(int[] nums) {
        int n = nums.length;
        int longest = 1;
        for (int start = 0; start < n; start++) {
            long product = 1;
            long runningGcd = 0;
            long runningLcm = 1;
            for (int end = start; end < n; end++) {
                long value = nums[end];
                product *= value;
                runningGcd = gcd(runningGcd, value);
                runningLcm = runningLcm / gcd(runningLcm, value) * value;
                long lcmTimesGcd = runningLcm * runningGcd;
                // the ratio only grows, so once it passes 1 this start is finished
                if (product > lcmTimesGcd) {
                    break;
                }
                // a lone element v > 1 has prod = v < v * v, so "below" is possible and must not break
                if (product == lcmTimesGcd) {
                    longest = Math.max(longest, end - start + 1);
                }
            }
        }
        return longest;
    }

    /** Euclid; gcd(0, x) = x. */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
