"""

1428. Leftmost Column with at Least a One
Medium

A row-sorted binary matrix means that all elements are 0 or 1 and each row of
the matrix is sorted in non-decreasing order.

Given a row-sorted binary matrix binaryMatrix, return the index (0-indexed) of
the leftmost column with a 1 in it. If such an index does not exist, return -1.

You can't access the Binary Matrix directly. You may only access the matrix
using a BinaryMatrix interface:

- BinaryMatrix.get(row, col) returns the element of the matrix at index
  (row, col) (0-indexed).
- BinaryMatrix.dimensions() returns the dimensions of the matrix as a list of
  2 elements [rows, cols], which means the matrix is rows x cols.

Submissions making more than 1000 calls to BinaryMatrix.get will be judged
Wrong Answer.

For custom testing purposes, the input will be the entire binary matrix mat.
You will not have access to the binary matrix directly.


Example 1:

Input: mat = [[0,0],[1,1]]
Output: 0

Example 2:

Input: mat = [[0,0],[0,1]]
Output: 1

Example 3:

Input: mat = [[0,0],[0,0]]
Output: -1


Constraints:

rows == mat.length
cols == mat[i].length
1 <= rows, cols <= 100
mat[i][j] is either 0 or 1.
mat[i] is sorted in non-decreasing order.

"""

# """
# This is BinaryMatrix's API interface.
# You should not implement it, or speculate about its implementation
# """
# class BinaryMatrix(object):
#    def get(self, row: int, col: int) -> int:
#    def dimensions(self) -> list[]:


# V0
# IDEA : STAIRCASE SEARCH (start from TOP-RIGHT corner)
#
#  -> at cell (r, c):
#       if it is 1 -> this column has a 1, move LEFT (c -= 1) and record c + 1
#       if it is 0 -> no 1 at/left of c in this row, move DOWN (r += 1)
#  -> each step consumes one row or one column, so at most (m + n) get() calls
#     (well under the 1000-call budget for 100 x 100)
#
# time = O(m + n)
# space = O(1)
class Solution(object):
    def leftmostColumnWithOne(self, binaryMatrix):
        rows, cols = binaryMatrix.dimensions()
        r, c = 0, cols - 1
        res = -1

        while r < rows and c >= 0:
            if binaryMatrix.get(r, c) == 1:
                res = c
                c -= 1
            else:
                r += 1

        return res


# V1
# IDEA : BINARY SEARCH PER ROW
#
#  -> for each row, binary search the leftmost 1, shrink the answer
#  -> O(m * log n) get() calls (100 * 7 = 700, also within budget)
#
# time = O(m log n)
# space = O(1)
class Solution(object):
    def leftmostColumnWithOne(self, binaryMatrix):
        rows, cols = binaryMatrix.dimensions()
        res = cols

        for r in range(rows):
            lo, hi = 0, res - 1
            while lo <= hi:
                mid = (lo + hi) // 2
                if binaryMatrix.get(r, mid) == 1:
                    res = mid
                    hi = mid - 1
                else:
                    lo = mid + 1

        return res if res < cols else -1
