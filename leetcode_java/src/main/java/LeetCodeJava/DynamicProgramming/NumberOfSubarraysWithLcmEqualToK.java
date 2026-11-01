package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-subarrays-with-lcm-equal-to-k/description/

/**
 * 2470. Number of Subarrays With LCM Equal to K
 * Medium
 *
 * Given an integer array nums and an integer k, return the number of subarrays of nums where the
 * least common multiple of the subarray's elements is k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 * The least common multiple of an array is the smallest positive integer that is divisible by all
 * the elements of the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,6,2,7,1], k = 6
 * Output: 4
 * Explanation: The subarrays of nums where 6 is the least common multiple of all the subarray's elements are:
 * - [3,6,2,7,1]
 * - [3,6,2,7,1]
 * - [3,6,2,7,1]
 * - [3,6,2,7,1]
 *
 * Example 2:
 *
 * Input: nums = [3], k = 2
 * Output: 0
 * Explanation: There are no subarrays of nums where 2 is the least common multiple of all the subarray's elements.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i], k <= 1000
 *
 */
public class NumberOfSubarraysWithLcmEqualToK {

    // V0
    // IDEA: EXTEND EACH START WHILE THE RUNNING LCM STILL DIVIDES k (THE LCM ONLY GROWS, SO BREAK EARLY)
    /**
     * time = O(N^2 * log(max))
     * space = O(1)
     */
    public int subarrayLCM(int[] nums, int k) {
        int n = nums.length;
        int result = 0;

        for (int start = 0; start < n; start++) {
            long runningLcm = 1;
            for (int end = start; end < n; end++) {
                // lcm(a, b) = a / gcd(a, b) * b, dividing first keeps the value small
                runningLcm = runningLcm / gcd(runningLcm, nums[end]) * nums[end];

                // the lcm only grows by multiples; once it stops dividing k it never comes back
                if (k % runningLcm != 0) {
                    break;
                }
                if (runningLcm == k) {
                    result++;
                }
            }
        }
        return result;
    }

    /** Euclid's algorithm. */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
