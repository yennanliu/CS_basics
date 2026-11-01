package LeetCodeJava.Array;

// https://leetcode.com/problems/xor-after-range-multiplication-queries-i/description/

/**
 * 3653. XOR After Range Multiplication Queries I
 * Medium
 *
 * You are given an integer array nums of length n and a 2D integer array queries
 * of size q, where queries[i] = [li, ri, ki, vi].
 *
 * For each query, you must apply the following operations in order:
 *
 * Set idx = li.
 * While idx <= ri:
 *   Update: nums[idx] = (nums[idx] * vi) % (10^9 + 7)
 *   Set idx += ki.
 *
 * Return the bitwise XOR of all elements in nums after processing all queries.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1], queries = [[0,2,1,4]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [2,3,1,5,4], queries = [[1,4,2,3],[0,2,1,2]]
 * Output: 31
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^3
 * 1 <= nums[i] <= 10^9
 * 1 <= q == queries.length <= 10^3
 * queries[i] = [li, ri, ki, vi]
 * 0 <= li <= ri < n
 * 1 <= ki <= n
 * 1 <= vi <= 10^5
 *
 */
public class XorAfterRangeMultiplicationQueriesI {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIRECT SIMULATION
    //   one query touches at most n cells, and n, q <= 1000, so replaying every query
    //   is at most 10^6 modular multiplications. (LC 3655 raises the bounds.)
    /**
     * time = O(Q * N)
     * space = O(N)
     */
    public int xorAfterQueries(int[] nums, int[][] queries) {
        long[] values = new long[nums.length];
        for (int i = 0; i < nums.length; i++) {
            values[i] = nums[i];
        }

        for (int[] query : queries) {
            int left = query[0];
            int right = query[1];
            int step = query[2];
            int multiplier = query[3];
            for (int idx = left; idx <= right; idx += step) {
                values[idx] = values[idx] * multiplier % MOD;
            }
        }

        int result = 0;
        for (long value : values) {
            result ^= (int) value;
        }
        return result;
    }
}
