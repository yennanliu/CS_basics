package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-smallest-number-in-multiplication-table/description/
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

}
