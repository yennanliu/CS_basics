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


# V0-1
# IDEA : ONE 1D DIFFERENCE ARRAY PER ROW
#
#   instead of the 4-corner 2D trick, treat every query as x2 - x1 + 1
#   independent 1D range updates -- one per covered row :
#
#       for r in [x1, x2] :  diff[r][y1] += 1 ,  diff[r][y2 + 1] -= 1
#
#   then a single left-to-right running sum per row rebuilds that row. Only
#   ONE prefix direction is needed (columns), because the rows were already
#   expanded explicitly.
#
#   NOTE : the sentinel column n makes the "-1" stamp always in range, so no
#          boundary test is needed at all.
#   NOTE : this is slower than the 2D version (O(n) stamps per query instead
#          of O(1)) but it needs no inclusion-exclusion reasoning, and it is
#          the natural shape when the queries arrive row-by-row.
#
# time = O(q * n + n^2)
# space = O(n^2)
class Solution(object):
    def rangeAddQueries(self, n, queries):
        diff = [[0] * (n + 1) for _ in range(n)]
        for x1, y1, x2, y2 in queries:
            for r in range(x1, x2 + 1):
                diff[r][y1] += 1
                diff[r][y2 + 1] -= 1

        mat = []
        for r in range(n):
            row = [0] * n
            cur = 0
            for c in range(n):
                cur += diff[r][c]
                row[c] = cur
            mat.append(row)
        return mat


# V0-2
# IDEA : BRUTE FORCE -- STAMP EVERY CELL OF EVERY SUBMATRIX
#
#   literally do what the statement says: walk the rectangle of each query and
#   add 1 to each cell. No difference array, no prefix sum.
#
#   With q up to 10^4 and n up to 500 this is up to 2.5 * 10^9 cell writes in
#   the worst case, so it is the baseline that MOTIVATES the difference array
#   rather than a submission-ready solution -- but it is the ground truth the
#   other two versions are checked against.
#
# time = O(q * n^2)
# space = O(1) beyond the output
class Solution(object):
    def rangeAddQueries(self, n, queries):
        mat = [[0] * n for _ in range(n)]
        for x1, y1, x2, y2 in queries:
            for r in range(x1, x2 + 1):
                row = mat[r]
                for c in range(y1, y2 + 1):
                    row[c] += 1
        return mat
