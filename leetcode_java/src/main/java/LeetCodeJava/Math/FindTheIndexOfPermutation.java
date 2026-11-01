package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-index-of-permutation/description/

/**
 * 3109. Find the Index of Permutation
 * Medium
 *
 * Given an array perm of length n which is a permutation of [1, 2, ..., n], return the index of perm in
 * the lexicographically sorted array of all of the permutations of [1, 2, ..., n].
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: perm = [1,2]
 * Output: 0
 * Explanation:
 * There are only two permutations in the following order:
 * [1,2], [2,1]
 * And [1,2] is at index 0.
 *
 * Example 2:
 *
 * Input: perm = [3,1,2]
 * Output: 4
 * Explanation:
 * There are only six permutations in the following order:
 * [1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]
 * And [3,1,2] is at index 4.
 *
 *
 * Constraints:
 *
 * 1 <= n == perm.length <= 10^5
 * perm is a permutation of [1, 2, ..., n].
 *
 */
public class FindTheIndexOfPermutation {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: FACTORIAL NUMBER SYSTEM (LEHMER CODE) + FENWICK TREE
    //   index = sum over i of (unused values smaller than perm[i]) * (n - i - 1)!
    //   a Fenwick tree over the still-unused values answers "how many smaller" in O(log n).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int getPermutationIndex(int[] perm) {
        int n = perm.length;

        // factorial[k] = k! mod MOD
        long[] factorial = new long[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i % MOD;
        }

        // Fenwick tree (1-indexed) holding 1 for every value not yet used
        int[] tree = new int[n + 1];
        for (int value = 1; value <= n; value++) {
            add(tree, value, 1);
        }

        long index = 0;
        for (int i = 0; i < n; i++) {
            // every unused smaller value at slot i starts a block of (n - i - 1)! earlier permutations
            int smallerUnused = prefixSum(tree, perm[i] - 1);
            index = (index + smallerUnused * factorial[n - i - 1]) % MOD;
            // perm[i] is now used
            add(tree, perm[i], -1);
        }
        return (int) index;
    }

    /** Adds delta at position i of the Fenwick tree. */
    private void add(int[] tree, int i, int delta) {
        while (i < tree.length) {
            tree[i] += delta;
            // i & -i is the lowest set bit: jump to the next node that covers i
            i += i & -i;
        }
    }

    /** Sum of positions 1..i of the Fenwick tree. */
    private int prefixSum(int[] tree, int i) {
        int sum = 0;
        while (i > 0) {
            sum += tree[i];
            // drop the lowest set bit: move to the previous disjoint range
            i -= i & -i;
        }
        return sum;
    }
}
