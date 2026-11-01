package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-integers-with-popcount-depth-equal-to-k-ii/description/

import java.util.*;

/**
 * 3624. Number of Integers With Popcount-Depth Equal to K II
 * Hard
 *
 * You are given an integer array nums.
 *
 * For any positive integer x, define the following sequence:
 *
 * p0 = x
 * p(i+1) = popcount(pi) for all i >= 0, where popcount(y) is the number of set
 * bits (1's) in the binary representation of y.
 *
 * This sequence will eventually reach the value 1.
 *
 * The popcount-depth of x is defined as the smallest integer d >= 0 such that
 * pd = 1.
 *
 * For example, if x = 7 (binary representation "111"). Then, the sequence is:
 * 7 -> 3 -> 2 -> 1, so the popcount-depth of 7 is 3.
 *
 * You are also given a 2D integer array queries, where each queries[i] is either:
 *
 * [1, l, r, k] - Determine the number of indices j such that l <= j <= r and the
 * popcount-depth of nums[j] is equal to k.
 * [2, idx, val] - Update nums[idx] to val.
 *
 * Return an integer array answer, where answer[i] is the number of indices for
 * the ith query of type [1, l, r, k].
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4], queries = [[1,0,1,1],[2,1,1],[1,0,1,0]]
 * Output: [2,1]
 * Explanation:
 * Query [1,0,1,1] on nums = [2,4] (binary [10, 100], depths [1, 1]) counts both
 * indices 0 and 1, so the answer is 2.
 * Query [2,1,1] updates nums to [2,1].
 * Query [1,0,1,0] on nums = [2,1] (binary [10, 1], depths [1, 0]) counts only
 * index 1, so the answer is 1.
 * Thus, the final answer is [2, 1].
 *
 * Example 2:
 *
 * Input: nums = [3,5,6], queries = [[1,0,2,2],[2,1,4],[1,1,2,1],[1,0,1,0]]
 * Output: [3,1,0]
 * Explanation:
 * Query [1,0,2,2] on nums = [3,5,6] (binary [11, 101, 110], depths [2, 2, 2])
 * counts indices 0, 1 and 2, so the answer is 3.
 * Query [2,1,4] updates nums to [3,4,6].
 * Query [1,1,2,1] on nums = [3,4,6] (binary [11, 100, 110], depths [2, 1, 2])
 * counts only index 1, so the answer is 1.
 * Query [1,0,1,0] on nums = [3,4,6] counts nothing, so the answer is 0.
 * Thus, the final answer is [3, 1, 0].
 *
 * Example 3:
 *
 * Input: nums = [1,2], queries = [[1,0,1,1],[2,0,3],[1,0,0,1],[1,0,0,2]]
 * Output: [1,0,1]
 * Explanation:
 * Query [1,0,1,1] on nums = [1,2] (binary [1, 10], depths [0, 1]) counts only
 * index 1, so the answer is 1.
 * Query [2,0,3] updates nums to [3,2].
 * Query [1,0,0,1] on nums = [3,2] (binary [11, 10], depths [2, 1]) counts
 * nothing in [0, 0], so the answer is 0.
 * Query [1,0,0,2] on nums = [3,2] counts index 0, so the answer is 1.
 * Thus, the final answer is [1, 0, 1].
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 10^15
 * 1 <= queries.length <= 10^5
 * queries[i].length == 3 or 4
 * queries[i] == [1, l, r, k] or,
 * queries[i] == [2, idx, val]
 * 0 <= l <= r <= n - 1
 * 0 <= k <= 5
 * 0 <= idx <= n - 1
 * 1 <= val <= 10^15
 *
 */
public class NumberOfIntegersWithPopcountDepthEqualToKII {

    private static final int MAX_BITS = 50;  // 10^15 < 2^50
    private static final int MAX_DEPTH = 5;  // k <= 5 by the constraints

    // V0
    // IDEA: DEPTH IS TINY, SO KEEP ONE FENWICK TREE (BIT) PER DEPTH
    //       values < 2^50, so one popcount step lands in [1, 50] and no value has depth above 5.
    //       "count depth == k on [l, r]" is then six independent 0/1 arrays, one per depth, each
    //       needing range sums under point updates -> one Fenwick tree per depth.
    //       an update moves index idx from its old depth's tree to its new one (two O(log n)
    //       updates); a query is one prefix-sum difference in tree k.
    //       depth(1) = 0, otherwise depth(x) = 1 + depth(popcount(x)).
    /**
     * time = O((N + Q) log N)
     * space = O(N)
     */
    public int[] popcountDepth(long[] nums, long[][] queries) {
        // smallDepth[v] = depth of v for every v a popcount can produce
        int[] smallDepth = new int[MAX_BITS + 1];
        for (int value = 2; value <= MAX_BITS; value++) {
            smallDepth[value] = smallDepth[Integer.bitCount(value)] + 1;
        }

        int n = nums.length;
        // trees[d] is a 1-indexed Fenwick tree over the indices whose depth is d
        int[][] trees = new int[MAX_DEPTH + 1][n + 1];
        int[] currentDepth = new int[n];
        for (int i = 0; i < n; i++) {
            currentDepth[i] = depthOf(nums[i], smallDepth);
            add(trees[currentDepth[i]], i, 1);
        }

        List<Integer> answers = new ArrayList<>();
        for (long[] query : queries) {
            if (query[0] == 1) {
                int left = (int) query[1];
                int right = (int) query[2];
                int k = (int) query[3];
                if (k > MAX_DEPTH) {
                    answers.add(0);
                } else {
                    int count = prefixSum(trees[k], right) - prefixSum(trees[k], left - 1);
                    answers.add(count);
                }
            } else {
                int index = (int) query[1];
                int newDepth = depthOf(query[2], smallDepth);
                if (newDepth != currentDepth[index]) {
                    // move the index from its old depth's tree to the new one
                    add(trees[currentDepth[index]], index, -1);
                    add(trees[newDepth], index, 1);
                    currentDepth[index] = newDepth;
                }
            }
        }

        int[] result = new int[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            result[i] = answers.get(i);
        }
        return result;
    }

    /** Popcount-depth of x: 0 for 1, otherwise one step plus the depth of its popcount. */
    private int depthOf(long x, int[] smallDepth) {
        if (x == 1) {
            return 0;
        }
        return smallDepth[Long.bitCount(x)] + 1;
    }

    /** Adds delta at 0-based index in a 1-indexed Fenwick tree. */
    private void add(int[] tree, int index, int delta) {
        // i & -i is the lowest set bit of i: the size of the range tree[i] covers
        for (int i = index + 1; i < tree.length; i += i & -i) {
            tree[i] += delta;
        }
    }

    /** Sum over 0-based indices [0, index]; 0 when index < 0. */
    private int prefixSum(int[] tree, int index) {
        int sum = 0;
        for (int i = index + 1; i > 0; i -= i & -i) {
            sum += tree[i];
        }
        return sum;
    }
}
