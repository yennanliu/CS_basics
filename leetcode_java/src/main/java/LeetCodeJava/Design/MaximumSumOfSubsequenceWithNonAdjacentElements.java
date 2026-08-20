package LeetCodeJava.Design;

// https://leetcode.com/problems/maximum-sum-of-subsequence-with-non-adjacent-elements/

/**
 *  3165. Maximum Sum of Subsequence With Non-adjacent Elements
 *  Hard
 *
 *  You are given an array nums consisting of integers. You are also given a 2D array
 *  queries, where queries[i] = [pos_i, x_i].
 *
 *  For query i, we first set nums[pos_i] equal to x_i, then we calculate the answer
 *  to query i which is the maximum sum of a subsequence of nums where no two
 *  adjacent elements are selected.
 *
 *  Return the sum of the answers to all queries, modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: nums = [3,5,9], queries = [[1,-2],[0,-3]]
 *    Output: 21
 *    Explanation:
 *      after query 1 nums = [3,-2,9] -> best is 3 + 9 = 12
 *      after query 2 nums = [-3,-2,9] -> best is 9
 *
 *  Example 2:
 *    Input: nums = [0,-1], queries = [[0,-5]]
 *    Output: 0
 *    Explanation: nums = [-5,-1] -> best is 0 (the empty subsequence)
 *
 *  Constraints:
 *    1 <= nums.length <= 5 * 10^4
 *    -10^5 <= nums[i] <= 10^5
 *    1 <= queries.length <= 5 * 10^4
 *    queries[i] == [pos_i, x_i]
 *    0 <= pos_i <= nums.length - 1
 *    -10^5 <= x_i <= 10^5
 */
public class MaximumSumOfSubsequenceWithNonAdjacentElements {

    // V0
    // IDEA: SEGMENT TREE WHOSE NODES CARRY A 2x2 "BOUNDARY" TABLE
    //
    //       the classic house-robber DP is a left-to-right scan, which a point
    //       update would invalidate. to make it updatable, store per segment the
    //       four answers
    //
    //         f[a][b] = best sum inside this segment, where a says whether its
    //                   FIRST element is taken and b whether its LAST is
    //
    //       merging two children is then a small max-plus product: the only illegal
    //       combination is left's last taken TOGETHER WITH right's first taken, so
    //
    //         res[i][j] = max(L[i][0] + R[0][j],
    //                         L[i][0] + R[1][j],
    //                         L[i][1] + R[0][j])
    //
    //       a leaf holding v has f[1][1] = v and f[0][0] = 0, the mixed entries
    //       being impossible (-inf). f[0][0] = 0 is also what allows the EMPTY
    //       subsequence, i.e. answer 0 for an all-negative array.
    //       each query is one leaf write plus O(log n) merges.
    /**
     * time = O((n + q) log n)
     * space = O(n)
     */
    private static final long MOD = 1_000_000_007L;
    private static final long NEG = Long.MIN_VALUE / 4;

    private int size;
    private long[][] tree; // tree[node][a * 2 + b]

    public int maximumSumSubsequence(int[] nums, int[][] queries) {
        int n = nums.length;
        size = 1;
        while (size < n) {
            size <<= 1;
        }
        tree = new long[2 * size][4];
        for (int i = 0; i < size; i++) {
            // padding leaves get the "identity": only f[0][0] = 0 is reachable
            setLeafRaw(i, i < n ? (long) nums[i] : NEG);
        }
        for (int node = size - 1; node >= 1; node--) {
            pull(node);
        }

        long res = 0L;
        for (int[] q : queries) {
            update(q[0], q[1]);
            res = (res + best()) % MOD;
        }
        return (int) res;
    }

    /** best sum over the whole array = max of the root's 4 entries */
    private long best() {
        long[] r = tree[1];
        return Math.max(Math.max(r[0], r[1]), Math.max(r[2], r[3]));
    }

    private void update(int pos, int val) {
        setLeafRaw(pos, val);
        int node = (size + pos) >> 1;
        while (node >= 1) {
            pull(node);
            node >>= 1;
        }
    }

    /** v == NEG marks a padding leaf (nothing can be taken there) */
    private void setLeafRaw(int pos, long v) {
        long[] leaf = tree[size + pos];
        leaf[0] = 0L;            // first not taken, last not taken (empty)
        leaf[1] = NEG;           // impossible for a single element
        leaf[2] = NEG;
        leaf[3] = (v == NEG) ? NEG : v; // the element itself is taken
    }

    private void pull(int node) {
        long[] L = tree[2 * node];
        long[] R = tree[2 * node + 1];
        long[] res = tree[node];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                long v = add(L[i * 2], R[j]);                 // L last=0, R first=0
                v = Math.max(v, add(L[i * 2], R[2 + j]));     // L last=0, R first=1
                v = Math.max(v, add(L[i * 2 + 1], R[j]));     // L last=1, R first=0
                res[i * 2 + j] = v;
            }
        }
    }

    private long add(long a, long b) {
        if (a == NEG || b == NEG) {
            return NEG;
        }
        return a + b;
    }
}
