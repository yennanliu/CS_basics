package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-number-of-good-subsets/description/
/**
 * 1994. The Number of Good Subsets
 * Hard
 *
 * You are given an integer array nums. We call a subset of nums good if its product can be
 * represented as a product of one or more distinct prime numbers.
 *
 * For example, if nums = [1, 2, 3, 4]:
 * - [2, 3], [1, 2, 3], and [1, 3] are good subsets with products 6 = 2*3, 6 = 2*3, and
 *   3 = 3 respectively.
 * - [1, 4] and [4] are not good subsets with products 4 = 2*2 and 4 = 2*2 respectively.
 *
 * Return the number of different good subsets in nums modulo 10^9 + 7.
 *
 * A subset of nums is any array that can be obtained by deleting some (possibly none or all)
 * elements from nums. Two subsets are different if and only if the chosen indices to delete
 * are different.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 6
 * Explanation: The good subsets are [1,2], [1,2,3], [1,3], [2], [2,3], [3].
 *
 * Example 2:
 *
 * Input: nums = [4,2,3,15]
 * Output: 5
 * Explanation: The good subsets are [2], [2,3], [2,15], [3], [15].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 30
 *
 */
public class TheNumberOfGoodSubsets {

    private static final int MOD = 1_000_000_007;
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
    private static final int MAX_VALUE = 30;

    // V0
    // IDEA: BITMASK KNAPSACK OVER THE 10 PRIMES BELOW 30
    /**
     *  Values are <= 30, so only the 10 primes below 30 matter. A usable value must be
     *  SQUARE-FREE: anything divisible by 4, 9 or 25 repeats a prime and is never good.
     *
     *  DP def:
     *     - ways[state] = number of subsets whose combined prime set is exactly state
     *                     (state = a 10-bit mask, bit i <=> PRIMES[i] is used)
     *
     *  DP eq: for each square-free value x (prime mask m, count[x] copies)
     *     - ways[state] += count[x] * ways[state without m]   for states containing m
     *     state is walked DOWNWARD so each distinct value is used at most once
     *     (0/1 knapsack); count[x] picks which of the interchangeable copies.
     *
     *  NOTE !!! 1 contributes no primes but doubles every subset: ways[0] = 2^count[1].
     *  Answer: sum of ways[state] for state != 0 (a good subset needs >= 1 prime).
     *
     *  time = O(N + 30 * 2^10)
     *  space = O(2^10)
     */
    public int numberOfGoodSubsets(int[] nums) {
        int[] count = new int[MAX_VALUE + 1];
        for (int num : nums) {
            count[num]++;
        }

        int stateCount = 1 << PRIMES.length;
        long[] ways = new long[stateCount];

        // each 1 can independently be in or out of any subset
        ways[0] = 1;
        for (int i = 0; i < count[1]; i++) {
            ways[0] = ways[0] * 2 % MOD;
        }

        for (int value = 2; value <= MAX_VALUE; value++) {
            if (count[value] == 0) {
                continue;
            }
            int primeMask = primeMaskOf(value);
            if (primeMask < 0) {
                continue;  // not square-free
            }
            // downward so ways[state ^ primeMask] still excludes this value
            for (int state = stateCount - 1; state > 0; state--) {
                if ((state & primeMask) == primeMask) {
                    ways[state] = (ways[state] + count[value] * ways[state ^ primeMask]) % MOD;
                }
            }
        }

        long total = 0;
        for (int state = 1; state < stateCount; state++) {
            total = (total + ways[state]) % MOD;
        }
        return (int) total;
    }

    /** Bitmask of the primes dividing value, or -1 if some prime divides it twice. */
    private int primeMaskOf(int value) {
        int mask = 0;
        for (int i = 0; i < PRIMES.length; i++) {
            int prime = PRIMES[i];
            if (value % prime == 0) {
                if (value % (prime * prime) == 0) {
                    return -1;
                }
                mask |= 1 << i;
            }
        }
        return mask;
    }
}
