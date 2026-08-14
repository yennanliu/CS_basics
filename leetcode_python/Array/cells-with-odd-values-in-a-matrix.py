"""

1252. Cells with Odd Values in a Matrix
Easy

There is an m x n matrix that is initialized to all 0's. There is also a 2D array indices where each indices[i] = [ri, ci] represents a 0-indexed location to perform some increment operations on the matrix.

For each location indices[i], do both of the following:

1. Increment all the cells on row ri.
2. Increment all the cells on column ci.

Given m, n, and indices, return the number of odd-valued cells in the matrix after applying the increment to all locations in indices.


Example 1:

Input: m = 2, n = 3, indices = [[0,1],[1,1]]
Output: 6
Explanation: Initial matrix = [[0,0,0],[0,0,0]].
After applying first increment it becomes [[1,2,1],[0,1,0]].
The final matrix is [[1,3,1],[1,3,1]], which contains 6 odd numbers.

Example 2:

Input: m = 2, n = 2, indices = [[1,1],[0,0]]
Output: 0
Explanation: Final matrix = [[2,2],[2,2]]. There are no odd numbers in the final matrix.


Constraints:

1 <= m, n <= 50
1 <= indices.length <= 100
0 <= ri < m
0 <= ci < n


Follow up: Could you solve this in O(n + m + indices.length) time with only O(n + m) extra space?

"""

# V0
# IDEA : COUNT ROWS / COLS ONLY + PARITY MATH
"""
 cell (i, j) value = row[i] + col[j]
   -> it is ODD  <=>  exactly one of row[i], col[j] is odd

 cnt1 = # of odd rows, cnt2 = # of odd cols
   -> answer = cnt1 * (n - cnt2) + cnt2 * (m - cnt1)

 (this hits the follow-up bound: no m x n scan)
"""
# time = O(m + n + k), k = len(indices)
# space = O(m + n)
class Solution(object):
    def oddCells(self, m, n, indices):
        row = [0] * m
        col = [0] * n
        for r, c in indices:
            row[r] += 1
            col[c] += 1

        cnt1 = sum(v & 1 for v in row)
        cnt2 = sum(v & 1 for v in col)
        return cnt1 * (n - cnt2) + cnt2 * (m - cnt1)


# V1
# IDEA : SIMULATION (build the matrix, then count)
# time = O(k * (m + n) + m * n)
# space = O(m * n)
class Solution(object):
    def oddCells(self, m, n, indices):
        g = [[0] * n for _ in range(m)]
        for r, c in indices:
            for j in range(n):
                g[r][j] += 1
            for i in range(m):
                g[i][c] += 1
        return sum(v & 1 for row in g for v in row)
