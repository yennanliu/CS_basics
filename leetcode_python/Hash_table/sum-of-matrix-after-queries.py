"""

2718. Sum of Matrix After Queries
Medium

You are given an integer n and a 0-indexed 2D array queries where queries[i] = [type_i, index_i, val_i].

Initially, there is a 0-indexed n x n matrix filled with 0's. For each query, you must apply one of the following changes:

if type_i == 0, set the values in the row with index_i to val_i, overwriting any previous values.
if type_i == 1, set the values in the column with index_i to val_i, overwriting any previous values.

Return the sum of integers in the matrix after all queries are applied.


Example 1:

Input: n = 3, queries = [[0,0,1],[1,2,2],[0,2,3],[1,0,4]]
Output: 23
Explanation: The queries are applied one by one to the matrix. The sum of the matrix after all queries are applied is 23.

Example 2:

Input: n = 3, queries = [[0,0,4],[0,1,2],[1,0,1],[0,2,3],[1,2,1]]
Output: 17
Explanation: The queries are applied one by one to the matrix. The sum of the matrix after all queries are applied is 17.


Constraints:

1 <= n <= 10^4
1 <= queries.length <= 5 * 10^4
queries[i].length == 3
0 <= type_i <= 1
0 <= index_i < n
0 <= val_i <= 10^5

"""

# V0
# IDEA : HASH SET + REVERSE SCAN
#
#   simulating the matrix is impossible (n * n can be 10^8 cells), but note
#   that the FINAL value of a cell is decided by whichever of "its row" /
#   "its column" was written LAST.
#
#   so walk the queries BACKWARDS: the first time we meet a row (or column)
#   going backwards is its last write going forwards, hence its final value.
#
#   NOTE : when a row is written (going backwards) at a moment where `col`
#          already holds c distinct columns, those c cells of the row were
#          already claimed by later column writes -> only (n - c) cells of
#          this row still carry val. Symmetric for a column write.
#
#   NOTE : a row / column seen a 2nd time going backwards contributes 0, its
#          write is fully overwritten.
#
# time = O(m), space = O(n)   (m = len(queries))
class Solution(object):
    def matrixSumQueries(self, n, queries):
        row = set()
        col = set()
        res = 0
        for i in range(len(queries) - 1, -1, -1):
            t, idx, val = queries[i]
            if t == 0:
                if idx not in row:
                    res += val * (n - len(col))
                    row.add(idx)
            else:
                if idx not in col:
                    res += val * (n - len(row))
                    col.add(idx)
        return res
