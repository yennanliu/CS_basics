"""

2536. Increment Submatrices by One
Medium

You are given a positive integer n, indicating that we initially have an n x n 0-indexed integer matrix mat filled with zeroes.

You are also given a 2D integer array query. For each query[i] = [row1_i, col1_i, row2_i, col2_i], you should do the following operation:

Add 1 to every element in the submatrix with the top left corner (row1_i, col1_i) and the bottom right corner (row2_i, col2_i). That is, add 1 to mat[x][y] for all row1_i <= x <= row2_i and col1_i <= y <= col2_i.

Return the matrix mat after performing every query.


Example 1:

Input: n = 3, queries = [[1,1,2,2],[0,0,1,1]]
Output: [[1,1,0],[1,2,1],[0,1,1]]
Explanation: The diagram above shows the initial matrix, the matrix after the first query, and the matrix after the second query.
- In the first query, we add 1 to every element in the submatrix with the top left corner (1, 1) and bottom right corner (2, 2).
- In the second query, we add 1 to every element in the submatrix with the top left corner (0, 0) and bottom right corner (1, 1).

Example 2:

Input: n = 2, queries = [[0,0,1,1]]
Output: [[1,1],[1,1]]
Explanation: The diagram above shows the initial matrix and the matrix after the first query.
- In the first query we add 1 to every element in the matrix.


Constraints:

1 <= n <= 500
1 <= queries.length <= 10^4
0 <= row1_i <= row2_i < n
0 <= col1_i <= col2_i < n

"""

# V0
# IDEA : 2D DIFFERENCE ARRAY (+ 2D PREFIX SUM TO RESTORE)
#
#   applying each query directly costs O(n^2) per query. instead mark the
#   query in a difference matrix with 4 corner stamps :
#
#       diff[x1][y1]     += 1
#       diff[x2+1][y1]   -= 1
#       diff[x1][y2+1]   -= 1
#       diff[x2+1][y2+1] += 1
#
#   then a 2D prefix sum over diff rebuilds the real matrix, because a cell
#   (i, j) ends up summing exactly the stamps of the rectangles covering it.
#
#   NOTE : the "-1 / +1" corners fall outside the matrix when x2+1 == n or
#          y2+1 == n; those stamps can simply be skipped (nothing to cancel).
#   NOTE : the prefix sum is done IN PLACE on diff, so the order matters --
#          add top and left first, then subtract the double-counted diagonal.
#
# time = O(q + n^2), space = O(1) beyond the output
class Solution(object):
    def rangeAddQueries(self, n, queries):
        mat = [[0] * n for _ in range(n)]
        for x1, y1, x2, y2 in queries:
            mat[x1][y1] += 1
            if x2 + 1 < n:
                mat[x2 + 1][y1] -= 1
            if y2 + 1 < n:
                mat[x1][y2 + 1] -= 1
            if x2 + 1 < n and y2 + 1 < n:
                mat[x2 + 1][y2 + 1] += 1

        for i in range(n):
            for j in range(n):
                if i > 0:
                    mat[i][j] += mat[i - 1][j]
                if j > 0:
                    mat[i][j] += mat[i][j - 1]
                if i > 0 and j > 0:
                    mat[i][j] -= mat[i - 1][j - 1]
        return mat
