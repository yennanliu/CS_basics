package LeetCodeJava.Sort;

// https://leetcode.com/problems/query-kth-smallest-trimmed-number/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  2343. Query Kth Smallest Trimmed Number
 *  Medium
 *
 *  You are given a 0-indexed array of strings nums, where each string is of equal
 *  length and consists of only digits.
 *
 *  You are also given a 0-indexed 2D integer array queries where
 *  queries[i] = [ki, trimi]. For each queries[i], you need to:
 *    - Trim each number in nums to its rightmost trimi digits.
 *    - Determine the index of the kith smallest trimmed number in nums. If two
 *      trimmed numbers are equal, the number with the lower index is considered
 *      to be smaller.
 *    - Reset each number in nums to its original length.
 *
 *  Return an array answer of the same length as queries, where answer[i] is the
 *  answer to the ith query.
 *
 *  Example 1:
 *    Input: nums = ["102","473","251","814"], queries = [[1,1],[2,3],[4,2],[1,2]]
 *    Output: [2,2,1,0]
 *
 *  Example 2:
 *    Input: nums = ["24","37","96","04"], queries = [[2,1],[2,2]]
 *    Output: [3,0]
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i].length <= 100
 *    nums[i] consists of only digits.
 *    All nums[i].length are equal.
 *    1 <= queries.length <= 100
 *    queries[i].length == 2
 *    1 <= ki <= nums.length
 *    1 <= trimi <= nums[i].length
 */
public class QueryKthSmallestTrimmedNumber {

    // V0
    // IDEA: SORT THE (TRIMMED STRING, INDEX) PAIRS PER QUERY
    //       all the numbers share a length, so trimming to the rightmost `trim`
    //       digits keeps them the same length AS EACH OTHER - which means plain
    //       lexicographic STRING comparison already orders them numerically
    //       (leading zeros included: "02" < "14").
    //
    //       including the index as the tie-break implements the stated rule
    //       ("lower index is smaller") directly.
    //
    //       nums.length and queries.length are both <= 100, so a sort per query
    //       is comfortably fine.
    /**
     * time = O(Q * N log N * L)
     * space = O(N)
     */
    public int[] smallestTrimmedNumbers(String[] nums, int[][] queries) {
        int n = nums.length;
        int len = nums[0].length();

        int[] res = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int k = queries[q][0];
            final int trim = queries[q][1];
            final String[] arr = nums;

            Integer[] order = new Integer[n];
            for (int i = 0; i < n; i++) {
                order[i] = i;
            }
            final int from = len - trim;
            Arrays.sort(order, new Comparator<Integer>() {
                @Override
                public int compare(Integer a, Integer b) {
                    int c = arr[a].substring(from).compareTo(arr[b].substring(from));
                    if (c != 0) {
                        return c;
                    }
                    return Integer.compare(a, b);
                }
            });
            res[q] = order[k - 1];
        }
        return res;
    }
}
