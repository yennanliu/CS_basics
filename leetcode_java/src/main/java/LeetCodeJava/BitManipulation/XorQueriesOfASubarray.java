package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/xor-queries-of-a-subarray/

/**
 *  1310. XOR Queries of a Subarray
 *  Medium
 *
 *  You are given an array arr of positive integers. You are also given the array
 *  queries where queries[i] = [left_i, right_i].
 *
 *  For each query i compute the XOR of elements from left_i to right_i (that is,
 *  arr[left_i] XOR arr[left_i + 1] XOR ... XOR arr[right_i]).
 *
 *  Return an array answer where answer[i] is the answer to the ith query.
 *
 *  Example 1:
 *    Input: arr = [1,3,4,8], queries = [[0,1],[1,2],[0,3],[3,3]]
 *    Output: [2,7,14,8]
 *    Explanation: [0,1] = 1^3 = 2, [1,2] = 3^4 = 7, [0,3] = 1^3^4^8 = 14,
 *                 [3,3] = 8.
 *
 *  Example 2:
 *    Input: arr = [4,8,2,10], queries = [[2,3],[1,3],[0,0],[0,3]]
 *    Output: [8,0,4,4]
 *
 *  Constraints:
 *    1 <= arr.length, queries.length <= 3 * 10^4
 *    1 <= arr[i] <= 10^9
 *    queries[i].length == 2
 *    0 <= left_i <= right_i < arr.length
 */
public class XorQueriesOfASubarray {

    // V0
    // IDEA: PREFIX XOR
    //       pre[i] = arr[0] ^ ... ^ arr[i-1]; since x ^ x == 0,
    //       arr[l] ^ ... ^ arr[r] == pre[r + 1] ^ pre[l]  -> each query is O(1).
    /**
     * time = O(N + M)
     * space = O(N)
     */
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = arr.length;
        int[] pre = new int[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] ^ arr[i];
        }

        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int l = queries[i][0];
            int r = queries[i][1];
            res[i] = pre[r + 1] ^ pre[l];
        }
        return res;
    }
}
