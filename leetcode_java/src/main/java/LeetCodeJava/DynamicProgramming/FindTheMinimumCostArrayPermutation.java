package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-minimum-cost-array-permutation/description/

import java.util.*;

/**
 * 3149. Find the Minimum Cost Array Permutation
 * Hard
 *
 * You are given an array nums which is a permutation of [0, 1, 2, ..., n - 1]. The score of any permutation of [0, 1, 2, ..., n - 1] named perm is defined as:
 *
 * score(perm) = |perm[0] - nums[perm[1]]| + |perm[1] - nums[perm[2]]| + ... + |perm[n - 1] - nums[perm[0]]|
 *
 * Return the permutation perm which has the minimum possible score. If multiple permutations exist with this score, return the one that is lexicographically smallest among them.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,0,2]
 * Output: [0,1,2]
 * Explanation:
 * The lexicographically smallest permutation with minimum cost is [0,1,2]. The cost of this permutation is |0 - 0| + |1 - 2| + |2 - 1| = 2.
 *
 * Example 2:
 *
 * Input: nums = [0,2,1]
 * Output: [0,2,1]
 * Explanation:
 * The lexicographically smallest permutation with minimum cost is [0,2,1]. The cost of this permutation is |0 - 1| + |2 - 2| + |1 - 0| = 2.
 *
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 14
 * nums is a permutation of [0, 1, 2, ..., n - 1].
 *
 */
public class FindTheMinimumCostArrayPermutation {

    // V0
    // IDEA: BITMASK DP (TSP-STYLE HAMILTONIAN CYCLE) ANCHORED AT 0 + GREEDY RECONSTRUCTION
    //   The score is cyclic (the last term wraps to perm[0]), so rotating a permutation keeps
    //   its cost; the rotation starting with 0 is also lexicographically smallest -> perm[0] = 0.
    //   cost(mask, last) = cheapest way to place the unused indices after `last`, including
    //   the wrap-around term |lastPlaced - nums[0]|.
    //   cost(mask, last) = min over unused j of |last - nums[j]| + cost(mask | 1<<j, j)
    //   Rebuild by taking, at each step, the SMALLEST j that achieves the optimum.
    /**
     * time = O(2^N * N^2)
     * space = O(2^N * N)
     */
    public int[] findPermutation(int[] nums) {
        int n = nums.length;
        int fullMask = (1 << n) - 1;
        int[][] memo = new int[1 << n][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        int[] perm = new int[n];
        perm[0] = 0;
        int mask = 1;
        int last = 0;
        for (int pos = 1; pos < n; pos++) {
            int target = cost(nums, mask, last, fullMask, memo);
            // ascending j -> the first optimal choice is the lexicographically smallest
            for (int j = 0; j < n; j++) {
                boolean used = (mask & (1 << j)) != 0;
                if (used) {
                    continue;
                }
                int withJ = Math.abs(last - nums[j]) + cost(nums, mask | (1 << j), j, fullMask, memo);
                if (withJ == target) {
                    perm[pos] = j;
                    mask |= 1 << j;
                    last = j;
                    break;
                }
            }
        }
        return perm;
    }

    /** Minimum remaining score when the indices in mask are placed and `last` was placed last. */
    private int cost(int[] nums, int mask, int last, int fullMask, int[][] memo) {
        // every index placed: close the cycle back to perm[0] = 0
        if (mask == fullMask) {
            return Math.abs(last - nums[0]);
        }
        if (memo[mask][last] != -1) {
            return memo[mask][last];
        }
        int best = Integer.MAX_VALUE;
        for (int j = 0; j < nums.length; j++) {
            if ((mask & (1 << j)) == 0) {
                int candidate = Math.abs(last - nums[j]) + cost(nums, mask | (1 << j), j, fullMask, memo);
                best = Math.min(best, candidate);
            }
        }
        memo[mask][last] = best;
        return best;
    }
}
