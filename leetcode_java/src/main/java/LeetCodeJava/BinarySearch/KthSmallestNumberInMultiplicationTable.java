package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-smallest-number-in-multiplication-table/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
/**
 * 668. Kth Smallest Number in Multiplication Table
 * Hard
 *
 * Nearly everyone has used the Multiplication Table. The multiplication table of size m x n
 * is an integer matrix mat where mat[i][j] == i * j (1-indexed).
 *
 * Given three integers m, n, and k, return the kth smallest element in the m x n
 * multiplication table.
 *
 * Example 1:
 *
 * Input: m = 3, n = 3, k = 5
 * Output: 3
 * Explanation: The 5th smallest number is 3.
 *
 * Example 2:
 *
 * Input: m = 2, n = 3, k = 6
 * Output: 6
 * Explanation: The 6th smallest number is 6.
 *
 * Constraints:
 *
 * 1 <= m, n <= 3 * 10^4
 * 1 <= k <= m * n
 *
 */
public class KthSmallestNumberInMultiplicationTable {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER VALUE (not on an index)
    /**
     *   The table is FAR too big to materialize (up to 9 * 10^8 cells), but COUNTING
     *   how many cells are <= x is cheap:
     *
     *     row i holds i*1, i*2, ..., i*n  ->  the count of entries <= x in row i
     *     is min(x / i, n)
     *
     *   count(x) is NON-DECREASING in x, so we binary search the SMALLEST x with
     *   count(x) >= k. That x is guaranteed to be a REAL table value: if it were not,
     *   count(x-1) would equal count(x) >= k, contradicting minimality.
     *
     *   NOTE !!! m * n can reach 9 * 10^8 which still fits in int, but we keep the
     *   count as `long` since it can grow past k before the early exit.
     *
     *   time  = O(m * log(m * n))
     *   space = O(1)
     */
    public int findKthNumber(int m, int n, int k) {
        int lo = 1;
        int hi = m * n;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            // how many table entries are <= mid
            long cnt = 0;
            for (int i = 1; i <= m; i++) {
                cnt += Math.min(mid / i, n);
                /** NOTE !!!
                 *
                 *  early exit -- we never need the EXACT total, only `cnt >= k`
                 */
                if (cnt >= k) {
                    break;
                }
            }

            if (cnt >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        return lo;
    }


    // V1
    // IDEA: SPLIT THE COUNT INTO A `SATURATED` BLOCK + A TAIL
    /**
     *  In V0 the count loop runs over ALL m rows. But every row i <= mid / n has
     *  min(mid / i, n) == n -- it is SATURATED -- so those rows can be summed in
     *  one multiplication:
     *
     *      count(mid) = n * min(m, mid / n) + sum over the remaining rows
     *
     *  The surviving loop runs at most O(sqrt(mid)) times, so the whole check drops
     *  from O(m) to O(sqrt(mid)).
     *
     *  time  = O(sqrt(m*n) * log(m*n))
     *  space = O(1)
     */
    public int findKthNumber_1(int m, int n, int k) {
        int lo = 1;
        int hi = m * n;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            int saturated = Math.min(m, mid / n); // rows where the whole row fits
            long cnt = (long) saturated * n;
            for (int i = saturated + 1; i <= m; i++) {
                cnt += mid / i;
            }

            if (cnt >= k) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    // V2
    // IDEA: K-WAY MERGE WITH A MIN HEAP over the rows
    /**
     *  Each row of the table is already sorted, so this is the classic
     *  `k-th smallest in m sorted lists` merge: seed the heap with each row's
     *  first cell and pop k times.
     *
     *  O(k log m) -- much better than V0 when k is small, much worse when k is
     *  near m*n (up to 9 * 10^8). The right choice depends entirely on k.
     *
     *  time  = O(m + k log m)
     *  space = O(m)
     */
    public int findKthNumber_2(int m, int n, int k) {
        // {value, row, col}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        for (int i = 1; i <= m; i++) {
            pq.add(new int[] { i, i, 1 });
        }

        int val = 0;
        for (int t = 0; t < k; t++) {
            int[] cur = pq.poll();
            val = cur[0];
            int row = cur[1];
            int col = cur[2];
            if (col + 1 <= n) {
                pq.add(new int[] { row * (col + 1), row, col + 1 });
            }
        }
        return val;
    }

    // V3
    // IDEA: BRUTE FORCE -- materialise and sort the whole table
    /**
     *  Only viable for tiny m, n (the real constraints allow 9 * 10^8 cells), but
     *  it is the definition of the answer and therefore the oracle.
     *
     *  time  = O(m * n * log(m * n))
     *  space = O(m * n)
     */
    public int findKthNumber_3(int m, int n, int k) {
        int[] all = new int[m * n];
        int idx = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                all[idx++] = i * j;
            }
        }
        Arrays.sort(all);
        return all[k - 1];
    }

}
