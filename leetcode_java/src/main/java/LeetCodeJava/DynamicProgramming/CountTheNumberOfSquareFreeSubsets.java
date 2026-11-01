package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-the-number-of-square-free-subsets/description/

/**
 * 2572. Count the Number of Square-Free Subsets
 * Medium
 *
 * You are given a positive integer 0-indexed array nums.
 *
 * A subset of the array nums is square-free if the product of its elements is a square-free integer.
 *
 * A square-free integer is an integer that is divisible by no square number other than 1.
 *
 * Return the number of square-free non-empty subsets of the array nums. Since the answer may be
 * too large, return it modulo 10^9 + 7.
 *
 * A non-empty subset of nums is an array that can be obtained by deleting some (possibly none but
 * not all) elements from nums. Two subsets are different if and only if the chosen indices to
 * delete are different.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,4,5]
 * Output: 3
 * Explanation: There are 3 square-free subsets in this example:
 * - The subset consisting of the 0th element [3]. The product of its elements is 3, which is a square-free integer.
 * - The subset consisting of the 3rd element [5]. The product of its elements is 5, which is a square-free integer.
 * - The subset consisting of 0th and 3rd elements [3,5]. The product of its elements is 15, which is a square-free integer.
 *
 * Example 2:
 *
 * Input: nums = [1]
 * Output: 1
 * Explanation: There is 1 square-free subset in this example:
 * - The subset consisting of the 0th element [1]. The product of its elements is 1, which is a square-free integer.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 30
 *
 */
public class CountTheNumberOfSquareFreeSubsets {

    private static final int MOD = 1_000_000_007;

    /** The 10 primes up to 30; a square-free product uses each at most once. */
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    // V0
    // IDEA: BITMASK DP OVER THE 10 PRIMES <= 30 - ways[mask] = #SUBSETS WHOSE PRODUCT USES EXACTLY THE PRIMES IN mask
    /**
     * time = O(N + 30 * 2^10)
     * space = O(2^10)
     */
    public int squareFreeSubsets(int[] nums) {
        // values are only 1..30, so count them and process each distinct value once
        int[] count = new int[31];
        for (int num : nums) {
            count[num]++;
        }

        int fullMask = 1 << PRIMES.length;
        long[] ways = new long[fullMask];

        // every 1 can be taken or not without changing the product: 2^count[1] ways for mask 0
        long onesWays = 1;
        for (int i = 0; i < count[1]; i++) {
            onesWays = onesWays * 2 % MOD;
        }
        ways[0] = onesWays;

        for (int value = 2; value <= 30; value++) {
            if (count[value] == 0) {
                continue;
            }
            // a value divisible by 4, 9 or 25 is itself not square-free and can never be used
            if (value % 4 == 0 || value % 9 == 0 || value % 25 == 0) {
                continue;
            }

            // the set of primes dividing this value, as a bitmask
            int valueMask = 0;
            for (int p = 0; p < PRIMES.length; p++) {
                if (value % PRIMES[p] == 0) {
                    valueMask |= (1 << p);
                }
            }

            // descending masks: at most one copy of this value per subset (copies share primes)
            for (int mask = fullMask - 1; mask > 0; mask--) {
                // mask must contain every prime of value
                if ((mask & valueMask) == valueMask) {
                    int withoutValue = mask ^ valueMask;
                    // choose which copy of `value` to take: count[value] options
                    ways[mask] = (ways[mask] + count[value] * ways[withoutValue]) % MOD;
                }
            }
        }

        long total = 0;
        for (int mask = 0; mask < fullMask; mask++) {
            total = (total + ways[mask]) % MOD;
        }
        // drop the empty subset
        return (int) ((total - 1 + MOD) % MOD);
    }
}
