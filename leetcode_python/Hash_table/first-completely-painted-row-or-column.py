"""

2661. First Completely Painted Row or Column
Medium

You are given a 0-indexed integer array arr, and an m x n integer matrix mat. arr and mat both contain all the integers in the range [1, m * n].

Go through each index i in arr starting from index 0 and paint the cell in mat containing the integer arr[i].

Return the smallest index i at which either a row or a column will be completely painted in mat.


Example 1:

Input: arr = [1,3,4,2], mat = [[1,4],[2,3]]
Output: 2
Explanation: The moves are shown in order, and both the first row and second column of the matrix become fully painted at arr[2].

Example 2:

Input: arr = [2,8,7,4,1,3,5,6,9], mat = [[3,2,5],[1,4,6],[8,7,9]]
Output: 3
Explanation: The second column becomes fully painted at arr[3].


Constraints:

m == mat.length
n = mat[i].length
arr.length == m * n
1 <= m, n <= 10^5
1 <= m * n <= 10^5
1 <= arr[i], mat[r][c] <= m * n
All the integers of arr are unique.
All the integers of mat are unique.

"""

# V0
# IDEA : HASH TABLE (value -> cell) + PER ROW / PER COLUMN PAINT COUNTERS
#
#   the values are a permutation of [1, m*n] in BOTH arr and mat, so every
#   arr[k] lands on exactly one cell, and each cell is painted exactly once.
#   that means we never need to re-check whether a cell was already painted.
#
#   1) build pos[v] = (i, j) for every value in mat  -- one pass over mat.
#   2) walk arr; for value arr[k] at cell (i, j), bump row[i] and col[j].
#      a row is complete once row[i] == n (its width), a column once
#      col[j] == m (its height). the first k where that happens is the
#      answer, because counters only ever grow.
#
#   NOTE : the completion thresholds are CROSSED -- row[i] is compared to
#          n (number of columns) and col[j] to m (number of rows). Swapping
#          them is the classic bug here.
#
#   NOTE : m*n <= 10^5 but m or n alone can be 10^5, so index by a flat
#          dict/list rather than trying to allocate an m x n grid twice.
#
#   an answer always exists: after painting all m*n values every row is
#   complete, so the loop is guaranteed to return.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def firstCompleteIndex(self, arr, mat):
        m, n = len(mat), len(mat[0])
        pos = {}
        for i in range(m):
            row_i = mat[i]
            for j in range(n):
                pos[row_i[j]] = (i, j)

        row_cnt = [0] * m
        col_cnt = [0] * n
        for k, v in enumerate(arr):
            i, j = pos[v]
            row_cnt[i] += 1
            col_cnt[j] += 1
            if row_cnt[i] == n or col_cnt[j] == m:
                return k
        return -1
