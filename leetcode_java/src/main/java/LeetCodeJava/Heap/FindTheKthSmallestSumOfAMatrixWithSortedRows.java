package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-the-kth-smallest-sum-of-a-matrix-with-sorted-rows/

import java.util.Arrays;

/**
 *  1439. Find the Kth Smallest Sum of a Matrix With Sorted Rows
 *  Hard
 *
 *  You are given an m x n matrix mat that has its rows sorted in non-decreasing
 *  order and an integer k.
 *
 *  You are allowed to choose exactly one element from each row to form an array.
 *  Return the kth smallest array sum among all possible arrays.
 *
 *  Example 1:
 *    Input: mat = [[1,3,11],[2,4,6]], k = 5
 *    Output: 7
 *    Explanation: the first 5 smallest sums come from [1,2], [1,4], [3,2], [3,4],
 *                 [1,6], so the 5th is 7.
 *
 *  Example 2:
 *    Input: mat = [[1,10,10],[1,4,5],[2,3,6]], k = 7
 *    Output: 9
 *
 *  Constraints:
 *    m == mat.length
 *    n == mat[i].length
 *    1 <= m, n <= 40
 *    1 <= mat[i][j] <= 5000
 *    1 <= k <= min(200, n^m)
 *    mat[i] is a non-decreasing array.
 */
public class FindTheKthSmallestSumOfAMatrixWithSortedRows {

    // V0
    // IDEA: FOLD THE ROWS ONE AT A TIME, KEEPING ONLY THE k SMALLEST SUMS
    //
    //   pre = the k smallest sums using the rows seen so far, then
    //   pre = k smallest of { a + b : a in pre, b in row }.
    //   Since each row is sorted, only its first k entries can ever matter, so the
    //   candidate set stays at k * k per step. The answer is the last (k-th) entry.
    /**
     * time = O(m * k * n log(k * n))
     * space = O(k * n)
     */
    public int kthSmallest(int[][] mat, int k) {
        int[] pre = new int[]{0};
        for (int[] row : mat) {
            int width = Math.min(k, row.length);
            int[] cur = new int[pre.length * width];
            int t = 0;
            for (int a : pre) {
                for (int c = 0; c < width; c++) {
                    cur[t++] = a + row[c];
                }
            }
            Arrays.sort(cur);
            pre = Arrays.copyOf(cur, Math.min(k, cur.length));
        }
        return pre[pre.length - 1];
    }

    // V1
    // IDEA: MIN-HEAP / BEST-FIRST SEARCH OVER THE COLUMN-INDEX TUPLES
    //       start from column 0 of every row (the smallest sum), pop the smallest
    //       sum and push its m "advance one row by one column" neighbours; the k-th
    //       pop is the answer. A visited set keeps each tuple from being pushed
    //       twice. Distinct trick from V0: it never materialises k*k candidates.
    /**
     * time = O(k * m log(k * m))
     * space = O(k * m)
     */
    public int kthSmallestHeap(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;

        int[] start = new int[m];
        int base = 0;
        for (int[] row : mat) {
            base += row[0];
        }

        // {sum, col index of row 0, col index of row 1, ...}
        java.util.PriorityQueue<int[]> heap = new java.util.PriorityQueue<>(
                new java.util.Comparator<int[]>() {
                    @Override
                    public int compare(int[] a, int[] b) {
                        return Integer.compare(a[0], b[0]);
                    }
                });
        int[] first = new int[m + 1];
        first[0] = base;
        System.arraycopy(start, 0, first, 1, m);
        heap.add(first);

        java.util.Set<String> seen = new java.util.HashSet<>();
        seen.add(Arrays.toString(start));

        for (int step = 0; step < k - 1; step++) {
            int[] cur = heap.poll();
            for (int i = 0; i < m; i++) {
                int col = cur[i + 1];
                if (col + 1 >= n) {
                    continue;
                }
                int[] nxt = cur.clone();
                nxt[i + 1] = col + 1;
                nxt[0] = cur[0] - mat[i][col] + mat[i][col + 1];
                String key = Arrays.toString(Arrays.copyOfRange(nxt, 1, m + 1));
                if (seen.add(key)) {
                    heap.add(nxt);
                }
            }
        }
        return heap.peek()[0];
    }
}
