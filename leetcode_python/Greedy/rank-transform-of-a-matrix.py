"""

1632. Rank Transform of a Matrix
Hard

Given an m x n matrix, return a new matrix answer where answer[row][col] is the rank of matrix[row][col].

The rank is an integer that represents how large an element is compared to other elements. It is calculated using the following rules:

The rank is an integer starting from 1.
If two elements p and q are in the same row or column, then:
  If p < q then rank(p) < rank(q)
  If p == q then rank(p) == rank(q)
  If p > q then rank(p) > rank(q)
The rank should be as small as possible.

The test cases are generated so that answer is unique under the given rules.


Example 1:

Input: matrix = [[1,2],[3,4]]
Output: [[1,2],[2,3]]
Explanation:
The rank of matrix[0][0] is 1 because it is the smallest integer in its row and column.
The rank of matrix[0][1] is 2 because matrix[0][1] > matrix[0][0] and matrix[0][0] is rank 1.
The rank of matrix[1][0] is 2 because matrix[1][0] > matrix[0][0] and matrix[0][0] is rank 1.
The rank of matrix[1][1] is 3 because matrix[1][1] > matrix[0][1], matrix[1][1] > matrix[1][0], and both matrix[0][1] and matrix[1][0] are rank 2.

Example 2:

Input: matrix = [[7,7],[7,7]]
Output: [[1,1],[1,1]]

Example 3:

Input: matrix = [[20,-21,14],[-19,4,19],[22,-47,24],[-19,4,19]]
Output: [[4,2,3],[1,3,4],[5,1,6],[1,3,4]]


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 500
-10^9 <= matrix[row][col] <= 10^9

"""

# V0
# IDEA : PROCESS VALUES IN INCREASING ORDER + UNION FIND on rows/cols
#
#   ranks only ever depend on strictly smaller values, so sweep the
#   distinct values ascending and keep
#     row_max[i] / col_max[j] = largest rank already assigned in that
#                               row / column
#
#   within ONE value, cells that share a row or a column are forced to the
#   SAME rank, and that ties chain transitively -- so union the m row nodes
#   with the n column nodes (col j is node m + j) for every cell of this
#   value. Each resulting component takes
#     rank = 1 + max(row_max[i], col_max[j]) over its cells
#
#   NOTE : the union-find must be RESET (only for the touched nodes) after
#          each value, otherwise groups leak into the next value.
#
# time = O(m * n * log(m * n)), space = O(m * n)
from collections import defaultdict
class Solution(object):
    def matrixRankTransform(self, matrix):
        m, n = len(matrix), len(matrix[0])
        buckets = defaultdict(list)
        for i in range(m):
            for j in range(n):
                buckets[matrix[i][j]].append((i, j))

        parent = list(range(m + n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        row_max = [0] * m
        col_max = [0] * n
        res = [[0] * n for _ in range(m)]

        for v in sorted(buckets):
            cells = buckets[v]
            for i, j in cells:
                ra, rb = find(i), find(j + m)
                if ra != rb:
                    parent[ra] = rb

            group_rank = defaultdict(int)
            for i, j in cells:
                root = find(i)
                cur = max(row_max[i], col_max[j])
                if cur > group_rank[root]:
                    group_rank[root] = cur

            for i, j in cells:
                r = group_rank[find(i)] + 1
                res[i][j] = r
                row_max[i] = r
                col_max[j] = r

            for i, j in cells:                 # reset only what we touched
                parent[i] = i
                parent[j + m] = j + m

        return res
