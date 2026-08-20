package LeetCodeJava.Sort;

// https://leetcode.com/problems/most-beautiful-item-for-each-query/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  2070. Most Beautiful Item for Each Query
 *  Medium
 *
 *  You are given a 2D integer array items where items[i] = [price_i, beauty_i]
 *  denotes the price and beauty of an item respectively.
 *
 *  You are also given a 0-indexed integer array queries. For each queries[j], you
 *  want to determine the maximum beauty of an item whose price is less than or
 *  equal to queries[j]. If no such item exists, then the answer to this query is 0.
 *
 *  Return an array answer of the same length as queries where answer[j] is the
 *  answer to the jth query.
 *
 *  Example 1:
 *    Input: items = [[1,2],[3,2],[2,4],[5,6],[3,5]], queries = [1,2,3,4,5,6]
 *    Output: [2,4,5,5,6,6]
 *
 *  Example 2:
 *    Input: items = [[1,2],[1,2],[1,3],[1,4]], queries = [1]
 *    Output: [4]
 *
 *  Example 3:
 *    Input: items = [[10,1000]], queries = [5]
 *    Output: [0]
 *
 *  Constraints:
 *    1 <= items.length, queries.length <= 10^5
 *    items[i].length == 2
 *    1 <= price_i, beauty_i, queries[j] <= 10^9
 */
public class MostBeautifulItemForEachQuery {

    // V0
    // IDEA: SORT BY PRICE + PREFIX MAX OF BEAUTY, THEN BINARY SEARCH PER QUERY
    //       sort the items by price and overwrite each beauty with the running
    //       maximum. that array is now non-decreasing in BOTH columns, so
    //       "best beauty at price <= q" is the prefix-max at the LAST index whose
    //       price is <= q -> an upper-bound binary search on the price column.
    //
    //       NOTE: answer 0 when the query is cheaper than every item (empty prefix).
    /**
     * time = O((N + M) log N)
     * space = O(N)
     */
    public int[] maximumBeauty(int[][] items, int[] queries) {
        Arrays.sort(items, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });

        int n = items.length;
        int[] prices = new int[n];
        int[] best = new int[n];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            cur = Math.max(cur, items[i][1]);
            prices[i] = items[i][0];
            best[i] = cur;
        }

        int[] res = new int[queries.length];
        for (int j = 0; j < queries.length; j++) {
            int cnt = upperBound(prices, queries[j]);   // # of prices <= q
            res[j] = cnt > 0 ? best[cnt - 1] : 0;
        }
        return res;
    }

    // number of elements in the sorted arr that are <= target
    private int upperBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
