"""

1727. Largest Submatrix With Rearrangements
Medium

You are given a binary matrix matrix of size m x n, and you are allowed to rearrange the columns of the matrix in any order.

Return the area of the largest submatrix within matrix where every element of the submatrix is 1 after reordering the columns optimally.


Example 1:

Input: matrix = [[0,0,1],[1,1,1],[1,0,1]]
Output: 4
Explanation: You can rearrange the columns as shown above.
The largest submatrix of 1s, in bold, has an area of 4.

Example 2:

Input: matrix = [[1,0,1,0,1]]
Output: 3
Explanation: You can rearrange the columns as shown above.
The largest submatrix of 1s, in bold, has an area of 3.

Example 3:

Input: matrix = [[1,1,0],[1,0,1]]
Output: 2
Explanation: Notice that you must rearrange entire columns, and there is no way to make a submatrix of 1s larger than an area of 2.


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m * n <= 10^5
matrix[i][j] is either 0 or 1.

"""

# V0
# IDEA : COLUMN HISTOGRAM + SORT EACH ROW (columns may be permuted freely)
#
#   step 1 - turn the matrix into "histogram" heights, per column:
#       h[i][j] = number of consecutive 1s ending at row i in column j
#               = h[i-1][j] + 1 if matrix[i][j] else 0
#
#   step 2 - fix row i as the BOTTOM edge. because whole columns can be
#   permuted, their order is irrelevant - only the MULTISET of heights in
#   row i matters. sort that row DESCENDING; then taking the k tallest
#   columns gives a rectangle of width k and height h_sorted[k-1], i.e.
#
#       area = k * h_sorted[k - 1]
#
#   scan k = 1..n and keep the max over all rows.
#
#   NOTE : sorting descending is what makes "the k tallest" a prefix, so the
#          shortest of the chosen k is always the last one taken.
#
# time = O(m * n * log n), space = O(n) (heights kept one row at a time)
class Solution(object):
    def largestSubmatrix(self, matrix):
        m, n = len(matrix), len(matrix[0])

        res = 0
        h = [0] * n
        for i in range(m):
            for j in range(n):
                h[j] = h[j] + 1 if matrix[i][j] else 0

            for k, v in enumerate(sorted(h, reverse=True), 1):
                if v == 0:
                    break
                res = max(res, k * v)

        return res
