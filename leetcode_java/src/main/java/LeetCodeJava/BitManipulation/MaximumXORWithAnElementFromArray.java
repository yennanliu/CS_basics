package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-xor-with-an-element-from-array/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  1707. Maximum XOR With an Element From Array
 *  Hard
 *
 *  You are given an array nums consisting of non-negative integers. You are also
 *  given a queries array, where queries[i] = [xi, mi].
 *
 *  The answer to the ith query is the maximum bitwise XOR value of xi and any
 *  element of nums that does not exceed mi. In other words, the answer is
 *  max(nums[j] XOR xi) for all j such that nums[j] <= mi. If all elements in nums
 *  are larger than mi, then the answer is -1.
 *
 *  Return an integer array answer where answer.length == queries.length and
 *  answer[i] is the answer to the ith query.
 *
 *  Example 1:
 *    Input: nums = [0,1,2,3,4], queries = [[3,1],[1,3],[5,6]]
 *    Output: [3,3,7]
 *
 *  Example 2:
 *    Input: nums = [5,2,4,6,6,3], queries = [[12,4],[8,1],[6,3]]
 *    Output: [15,-1,5]
 *
 *  Constraints:
 *    1 <= nums.length, queries.length <= 10^5
 *    queries[i].length == 2
 *    0 <= nums[j], xi, mi <= 10^9
 */
public class MaximumXORWithAnElementFromArray {

    private static final int BITS = 30;      // 10^9 < 2^30

    private int[][] children;                // children[node][bit] -> node id (0 == absent)
    private int size;

    // V0
    // IDEA: OFFLINE QUERIES + BINARY TRIE (sort by m, grow the trie monotonically)
    //
    //  the "nums[j] <= m" filter is the awkward part. handle it OFFLINE:
    //    - sort nums ascending
    //    - sort the queries by m ascending
    //  then a pointer j only ever moves forward: before answering a query, insert
    //  every nums[j] <= m into the trie. the trie therefore always holds exactly
    //  the legal candidates, and never needs a deletion.
    //
    //  inside the trie, max-XOR is the classic greedy: walk bits high -> low and
    //  always take the branch OPPOSITE to x's bit when it exists.
    //
    //  NOTE: an empty trie means every num > m -> answer -1.
    /**
     * time = O(n log n + q log q + (n + q) * 31)
     * space = O(n * 31)
     */
    public int[] maximizeXor(int[] nums, int[][] queries) {
        int n = nums.length;
        int q = queries.length;

        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // {m, x, originalIndex}, ordered by m
        Integer[] order = new Integer[q];
        for (int i = 0; i < q; i++) {
            order[i] = i;
        }
        final int[][] qs = queries;
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(qs[a][1], qs[b][1]);
            }
        });

        children = new int[n * (BITS + 1) + 2][2];
        size = 1;                            // node 0 is the root

        int[] res = new int[q];
        int j = 0;
        for (int t = 0; t < q; t++) {
            int idx = order[t];
            int x = queries[idx][0];
            int m = queries[idx][1];
            while (j < n && sorted[j] <= m) {
                insert(sorted[j]);
                j++;
            }
            res[idx] = query(x);
        }
        return res;
    }

    private void insert(int x) {
        int node = 0;
        for (int i = BITS; i >= 0; i--) {
            int b = (x >> i) & 1;
            if (children[node][b] == 0) {
                children[node][b] = size++;
            }
            node = children[node][b];
        }
    }

    private int query(int x) {
        if (children[0][0] == 0 && children[0][1] == 0) {
            return -1;                       // nothing inserted yet
        }
        int node = 0;
        int res = 0;
        for (int i = BITS; i >= 0; i--) {
            int b = (x >> i) & 1;
            int opposite = children[node][b ^ 1];
            if (opposite != 0) {
                res |= 1 << i;
                node = opposite;
            } else {
                node = children[node][b];
            }
        }
        return res;
    }
}
