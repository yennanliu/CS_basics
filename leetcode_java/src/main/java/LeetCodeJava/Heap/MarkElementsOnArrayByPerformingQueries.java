package LeetCodeJava.Heap;

// https://leetcode.com/problems/mark-elements-on-array-by-performing-queries/

import java.util.PriorityQueue;

/**
 *  3080. Mark Elements on Array by Performing Queries
 *  Medium
 *
 *  You are given a 0-indexed array nums of size n consisting of positive integers.
 *
 *  You are also given a 2D array queries of size m where queries[i] = [index_i, k_i].
 *
 *  Initially all elements of the array are unmarked.
 *
 *  You need to apply m queries on the array in order, where on the ith query you do
 *  the following:
 *
 *   - Mark the element at index index_i if it is not already marked.
 *   - Then mark k_i unmarked elements in the array with the smallest values. If
 *     multiple such elements exist, mark the ones with the smallest indices. And if
 *     less than k_i unmarked elements exist, then mark all of them.
 *
 *  Return an array answer of size m where answer[i] is the sum of unmarked elements
 *  in the array after the ith query.
 *
 *  Example 1:
 *    Input: nums = [1,2,2,1,2,3,1], queries = [[1,2],[3,3],[4,2]]
 *    Output: [8,3,0]
 *
 *  Example 2:
 *    Input: nums = [1,4,2,3], queries = [[0,1]]
 *    Output: [7]
 *
 *  Constraints:
 *    n == nums.length
 *    m == queries.length
 *    1 <= m <= n <= 10^5
 *    1 <= nums[i] <= 10^5
 *    queries[i].length == 2
 *    0 <= index_i, k_i <= n - 1
 */
public class MarkElementsOnArrayByPerformingQueries {

    // V0
    // IDEA: MIN-HEAP ON (VALUE, INDEX) + LAZY DELETION
    //       "smallest value, then smallest index" is exactly the (value, index)
    //       ordering, so ONE heap over all elements serves every query.
    //       an element can also be marked directly by index, leaving a stale
    //       heap entry -> keep a `marked` flag and discard stale tops.
    //       a running `total` makes each answer an O(1) read.
    /**
     * time = O((n + sum(k)) log n)
     * space = O(n)
     */
    public long[] unmarkedSumArray(int[] nums, int[][] queries) {
        int n = nums.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) ->
                a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

        long total = 0L;
        for (int i = 0; i < n; i++) {
            pq.add(new int[]{nums[i], i});
            total += nums[i];
        }

        boolean[] marked = new boolean[n];
        long[] res = new long[queries.length];

        for (int q = 0; q < queries.length; q++) {
            int idx = queries[q][0];
            int k = queries[q][1];

            if (!marked[idx]) {
                marked[idx] = true;
                total -= nums[idx];
            }

            while (k > 0 && !pq.isEmpty()) {
                int[] cur = pq.poll();
                if (marked[cur[1]]) {
                    continue; // stale entry, already handled
                }
                marked[cur[1]] = true;
                total -= cur[0];
                k--;
            }

            res[q] = total;
        }

        return res;
    }
}
