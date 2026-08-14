"""

1284. Minimum Number of Flips to Convert Binary Matrix to Zero Matrix
Hard

Given a m x n binary matrix mat. In one step, you can choose one cell and flip it
and all the four neighbors of it if they exist
(Flip is changing 1 to 0 and 0 to 1).
A pair of cells are called neighbors if they share one edge.

Return the minimum number of steps required to convert mat to a zero matrix
or -1 if you cannot.

A binary matrix is a matrix with all cells equal to 0 or 1 only.

A zero matrix is a matrix with all cells equal to 0.


Example 1:

Input: mat = [[0,0],[0,1]]
Output: 3
Explanation: One possible solution is to flip (1, 0) then (0, 1) and finally (1, 1).

Example 2:

Input: mat = [[0]]
Output: 0
Explanation: Given matrix is a zero matrix. We do not need to change it.

Example 3:

Input: mat = [[1,0,0],[1,0,0]]
Output: -1
Explanation: Given matrix cannot be a zero matrix.


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 3
mat[i][j] is either 0 or 1.

"""

# V0
# IDEA: BFS over BITMASK states
#
#  -> m, n <= 3, so the whole matrix fits into at most 9 bits.
#     encode the matrix as an int, BFS from the start state,
#     each of the m*n possible clicks is one edge.
#     first time we reach state 0 is the min number of steps.
#
# time = O(2^(m*n) * m * n)
# space = O(2^(m*n))
from collections import deque
class Solution(object):
    def minFlips(self, mat):
        m, n = len(mat), len(mat[0])

        # encode matrix -> int
        start = 0
        for i in range(m):
            for j in range(n):
                if mat[i][j]:
                    start |= 1 << (i * n + j)

        if start == 0:
            return 0

        """
        NOTE !!!

        pre-compute, for every cell, the mask of bits it toggles
        (itself + its 4 neighbours)
        """
        masks = []
        for i in range(m):
            for j in range(n):
                mask = 0
                for dx, dy in [(0, 0), (0, 1), (0, -1), (1, 0), (-1, 0)]:
                    x, y = i + dx, j + dy
                    if 0 <= x < m and 0 <= y < n:
                        mask |= 1 << (x * n + y)
                masks.append(mask)

        q = deque([start])
        visited = {start}
        step = 0
        while q:
            step += 1
            for _ in range(len(q)):
                cur = q.popleft()
                for mask in masks:
                    # XOR flips exactly the cell + its neighbours
                    nxt = cur ^ mask
                    if nxt == 0:
                        return step
                    if nxt not in visited:
                        visited.add(nxt)
                        q.append(nxt)
        return -1


# V0-1
# IDEA: BRUTE FORCE over subsets of clicks
#
#  -> clicking a cell twice is a no-op and order does not matter,
#     so an answer is just a SUBSET of cells to click.
#     enumerate all 2^(m*n) subsets and keep the smallest that zeroes the matrix.
#
# time = O(2^(m*n) * m * n)
# space = O(m * n)
class Solution(object):
    def minFlips(self, mat):
        m, n = len(mat), len(mat[0])
        total = m * n

        start = 0
        for i in range(m):
            for j in range(n):
                if mat[i][j]:
                    start |= 1 << (i * n + j)

        masks = []
        for i in range(m):
            for j in range(n):
                mask = 0
                for dx, dy in [(0, 0), (0, 1), (0, -1), (1, 0), (-1, 0)]:
                    x, y = i + dx, j + dy
                    if 0 <= x < m and 0 <= y < n:
                        mask |= 1 << (x * n + y)
                masks.append(mask)

        res = -1
        for sub in range(1 << total):
            cur = start
            cnt = 0
            for k in range(total):
                if sub & (1 << k):
                    cur ^= masks[k]
                    cnt += 1
            if cur == 0 and (res == -1 or cnt < res):
                res = cnt
        return res
