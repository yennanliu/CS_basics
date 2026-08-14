"""

668. Kth Smallest Number in Multiplication Table
Hard

Nearly everyone has used the Multiplication Table. The multiplication table of size m x n
is an integer matrix mat where mat[i][j] == i * j (1-indexed).

Given three integers m, n, and k, return the kth smallest element in the m x n
multiplication table.

Example 1:

Input: m = 3, n = 3, k = 5
Output: 3
Explanation: The 5th smallest number is 3.

Example 2:

Input: m = 2, n = 3, k = 6
Output: 6
Explanation: The 6th smallest number is 6.

Constraints:

1 <= m, n <= 3 * 10^4
1 <= k <= m * n

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER VALUE (not on an index)
#
#   The table is far too big to materialize (up to 9 * 10^8 cells), but counting
#   how many cells are <= x is cheap:
#
#     row i holds i*1, i*2, ..., i*n  ->  the count of entries <= x in row i
#     is min(x // i, n)
#
#   count(x) is non-decreasing in x, so binary search the smallest x with
#   count(x) >= k. That x is guaranteed to be a real table value: if it were not,
#   count(x-1) would equal count(x) >= k, contradicting minimality.
#
# time = O(m * log(m * n))
# space = O(1)
class Solution(object):
    def findKthNumber(self, m, n, k):
        lo, hi = 1, m * n
        while lo < hi:
            mid = (lo + hi) // 2
            # how many table entries are <= mid
            cnt = 0
            for i in range(1, m + 1):
                cnt += min(mid // i, n)
                if cnt >= k:       # early exit, no need for the exact total
                    break
            if cnt >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
