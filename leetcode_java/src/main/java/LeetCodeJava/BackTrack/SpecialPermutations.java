package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/special-permutations/

/**
 *  2741. Special Permutations
 *  Medium
 *
 *  You are given a 0-indexed integer array nums containing n distinct positive
 *  integers. A permutation of nums is called special if:
 *    For all indexes 0 <= i < n - 1, either nums[i] % nums[i+1] == 0 or
 *    nums[i+1] % nums[i] == 0.
 *
 *  Return the total number of special permutations. As the answer could be large,
 *  return it modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: nums = [2,3,6]
 *    Output: 2
 *    Explanation: [3,6,2] and [2,6,3] are the two special permutations of nums.
 *
 *  Example 2:
 *    Input: nums = [1,4,3]
 *    Output: 2
 *    Explanation: [3,1,4] and [4,1,3] are the two special permutations of nums.
 *
 *  Constraints:
 *    2 <= nums.length <= 14
 *    1 <= nums[i] <= 10^9
 */
public class SpecialPermutations {

    // V0
    // IDEA: BACKTRACKING COMPRESSED INTO BITMASK DP (STATE COMPRESSION)
    //       Plain backtracking over all n! orders is 14! ~ 8.7e10 -- way too slow.
    //       But the count of ways to FINISH a permutation only depends on:
    //         (1) WHICH numbers are already used -> a 14-bit mask
    //         (2) WHICH number was placed LAST   -> an index
    //       The actual order of the used prefix is irrelevant, so memoizing on
    //       (mask, last) collapses n! branches into n * 2^n states.
    //
    //       dp[mask][last] = number of ways to extend a prefix that used exactly the
    //                        numbers in `mask` and ended with nums[last].
    //       Base case : mask == full -> 1. Answer = sum over every start index.
    //
    //       NOTE : precompute the divisibility adjacency as bitmasks so the inner
    //              loop is a single `ok[last] & ~mask` scan instead of n modulos.
    /**
     * time = O(n^2 * 2^n)
     * space = O(n * 2^n)
     */
    public int specialPerm(int[] nums) {
        final int MOD = 1_000_000_007;
        int n = nums.length;
        int full = (1 << n) - 1;

        // ok[i] = bitmask of the j that may directly follow / precede i
        int[] ok = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && (nums[i] % nums[j] == 0 || nums[j] % nums[i] == 0)) {
                    ok[i] |= 1 << j;
                }
            }
        }

        // iterate masks descending so dp[bigger mask] is ready
        int[][] dp = new int[1 << n][n];
        for (int last = 0; last < n; last++) {
            dp[full][last] = 1;
        }

        for (int mask = full - 1; mask > 0; mask--) {
            int free = full ^ mask;
            for (int last = 0; last < n; last++) {
                if (((mask >> last) & 1) == 0) {
                    continue;
                }
                int cand = ok[last] & free;
                long total = 0;
                while (cand != 0) {
                    int j = Integer.numberOfTrailingZeros(cand);
                    total += dp[mask | (1 << j)][j];
                    cand &= cand - 1;
                }
                dp[mask][last] = (int) (total % MOD);
            }
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            res += dp[1 << i][i];
        }
        return (int) (res % MOD);
    }
}
