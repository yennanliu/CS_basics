package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-maximum-factor-score-of-array/description/

/**
 * 3334. Find the Maximum Factor Score of Array
 * Medium
 *
 * You are given an integer array nums.
 *
 * The factor score of an array is defined as the product of the LCM and GCD of all elements of that
 * array.
 *
 * Return the maximum factor score of nums after removing at most one element from it.
 *
 * Note that both the LCM and GCD of a single number are the number itself, and the factor score of an
 * empty array is 0.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,8,16]
 * Output: 64
 * Explanation:
 * On removing element 2, the GCD of the remaining elements is 4 while the LCM is 16, which gives a
 * maximum factor score of 4 * 16 = 64.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 60
 * Explanation:
 * The maximum factor score of 60 can be obtained without removing any elements.
 *
 * Example 3:
 *
 * Input: nums = [3]
 * Output: 9
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 30
 *
 */
public class FindTheMaximumFactorScoreOfArray {

    // V0
    // IDEA: PREFIX + SUFFIX GCD / LCM — REMOVING ELEMENT i MERGES prefix[i] WITH suffix[i + 1] IN O(1)
    //   gcd(0, x) = x and lcm(0, x) = x act as the identity for an empty side.
    /**
     * time = O(N log V)
     * space = O(N)
     */
    public long maxScore(int[] nums) {
        int n = nums.length;

        // prefixGcd[i] / prefixLcm[i] cover nums[0 .. i-1]; index 0 is the empty prefix
        long[] prefixGcd = new long[n + 1];
        long[] prefixLcm = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixGcd[i + 1] = gcd(prefixGcd[i], nums[i]);
            prefixLcm[i + 1] = lcm(prefixLcm[i], nums[i]);
        }

        // suffixGcd[i] / suffixLcm[i] cover nums[i .. n-1]; index n is the empty suffix
        long[] suffixGcd = new long[n + 1];
        long[] suffixLcm = new long[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            suffixGcd[i] = gcd(suffixGcd[i + 1], nums[i]);
            suffixLcm[i] = lcm(suffixLcm[i + 1], nums[i]);
        }

        // remove nothing
        long best = prefixGcd[n] * prefixLcm[n];
        // remove nums[i]; an empty remainder gives gcd = lcm = 0, i.e. score 0
        for (int i = 0; i < n; i++) {
            long remainingGcd = gcd(prefixGcd[i], suffixGcd[i + 1]);
            long remainingLcm = lcm(prefixLcm[i], suffixLcm[i + 1]);
            best = Math.max(best, remainingGcd * remainingLcm);
        }
        return best;
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

    /** lcm with 0 treated as "no element yet". */
    private long lcm(long a, long b) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        // divide first to keep the intermediate small
        return a / gcd(a, b) * b;
    }
}
