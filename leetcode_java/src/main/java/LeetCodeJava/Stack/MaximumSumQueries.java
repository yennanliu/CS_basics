package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-sum-queries/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  2736. Maximum Sum Queries
 *  Hard
 *
 *  You are given two 0-indexed integer arrays nums1 and nums2, each of length
 *  n, and a 1-indexed 2D array queries where queries[i] = [xi, yi].
 *
 *  For the ith query, find the maximum value of nums1[j] + nums2[j] among all
 *  indices j (0 <= j < n), where nums1[j] >= xi and nums2[j] >= yi, or -1 if
 *  there is no j satisfying the constraints.
 *
 *  Return an array answer where answer[i] is the answer to the ith query.
 *
 *  Example 1:
 *    Input: nums1 = [4,3,1,2], nums2 = [2,4,9,5],
 *           queries = [[4,1],[1,3],[2,5]]
 *    Output: [6,10,7]
 *
 *  Example 2:
 *    Input: nums1 = [2,1], nums2 = [2,3], queries = [[3,3]]
 *    Output: [-1]
 *
 *  Constraints:
 *    nums1.length == nums2.length == n
 *    1 <= n <= 10^5
 *    1 <= nums1[i], nums2[i] <= 10^9
 *    1 <= queries.length <= 10^5
 *    queries[i].length == 2
 *    1 <= xi, yi <= 10^9
 */
public class MaximumSumQueries {

    // V0
    // IDEA: OFFLINE QUERIES (SORT BY x) + BINARY INDEXED TREE (MAX) OVER nums2
    //       This is a 2D dominance query. Peel off ONE dimension by sorting,
    //       handle the other with a Fenwick / BIT:
    //         1) sort the (nums1[j], nums2[j]) pairs by nums1 DESC
    //         2) sort the queries by x DESC, remembering the original index
    //         3) sweep the queries; before answering (x, y) push every pair
    //            whose nums1 >= x into the BIT. From then on "nums1 >= x" is
    //            automatic and only "nums2 >= y" is left.
    //       A BIT natively answers PREFIX max but we need a SUFFIX max over
    //       nums2, so index by the REVERSED rank
    //           k(v) = n - lowerBound(sortedNums2, v)   // #values >= v
    //       Bigger v -> smaller k, hence "nums2 >= y" becomes prefix 1..k(y).
    //       k(v) >= 1 for any v that occurs in nums2 (update never loops on
    //       index 0), while a y larger than every nums2 gives k = 0 and the
    //       query loop returns the -1 default.
    /**
     * time = O((N + M) * log N + M * log M)
     * space = O(N + M)
     */
    public int[] maximumSumQueries(int[] nums1, int[] nums2, int[][] queries) {
        int n = nums1.length;
        int m = queries.length;

        // value list used for the (reversed) rank compression
        int[] sortedN2 = nums2.clone();
        Arrays.sort(sortedN2);

        // pairs by nums1 descending
        int[][] pairs = new int[n][2];
        for (int i = 0; i < n; i++) {
            pairs[i][0] = nums1[i];
            pairs[i][1] = nums2[i];
        }
        Arrays.sort(pairs, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]);
            }
        });

        // query indices by x descending
        Integer[] order = new Integer[m];
        for (int i = 0; i < m; i++) {
            order[i] = i;
        }
        final int[][] q = queries;
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(q[b][0], q[a][0]);
            }
        });

        int[] tree = new int[n + 1]; // BIT of max(nums1 + nums2), -1 = empty
        Arrays.fill(tree, -1);
        int[] res = new int[m];
        int j = 0;

        for (int idx = 0; idx < m; idx++) {
            int qi = order[idx];
            int x = queries[qi][0];
            int y = queries[qi][1];

            // push every pair with nums1 >= x (pointer only moves forward)
            while (j < n && pairs[j][0] >= x) {
                int v = pairs[j][0] + pairs[j][1];
                int k = n - lowerBound(sortedN2, pairs[j][1]);
                while (k <= n) {
                    if (tree[k] < v) {
                        tree[k] = v;
                    }
                    k += k & (-k);
                }
                j++;
            }

            // prefix max over ranks 1..k  <=>  nums2 >= y
            int k = n - lowerBound(sortedN2, y);
            int best = -1;
            while (k > 0) {
                if (tree[k] > best) {
                    best = tree[k];
                }
                k -= k & (-k);
            }
            res[qi] = best;
        }
        return res;
    }

    // first index i with arr[i] >= target
    private int lowerBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
