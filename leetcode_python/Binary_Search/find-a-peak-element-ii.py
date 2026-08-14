"""

1901. Find a Peak Element II
Medium

A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors to the left, right, top, and bottom.

Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element mat[i][j] and return the length 2 array [i,j].

You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each cell.

You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.


Example 1:

Input: mat = [[1,4],[3,2]]
Output: [0,1]
Explanation: Both 3 and 4 are peak elements so [1,0] and [0,1] are both acceptable answers.

Example 2:

Input: mat = [[10,20,15],[21,30,14],[7,16,32]]
Output: [1,1]
Explanation: Both 30 and 32 are peak elements so [1,1] and [2,2] are both acceptable answers.


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 500
1 <= mat[i][j] <= 10^5
No two adjacent cells are equal.

"""

# V0
# IDEA : BINARY SEARCH ON ROWS (the row max is a peak or points uphill)
#
#   take the middle row and let j be the index of its MAXIMUM.
#   mat[mid][j] already beats its left/right neighbours, so it can only
#   fail vertically :
#     - if mat[mid][j] > mat[mid+1][j], a peak is guaranteed in rows
#       [lo .. mid]   -> keep the upper half
#     - otherwise the value strictly increases downward, so a peak exists
#       in rows [mid+1 .. hi] -> keep the lower half
#   (the invariant holds because the border is -1, so climbing must stop.)
#
#   each step halves the row range and costs O(n) to find the row maximum.
#
# time = O(n log m), space = O(1)
class Solution(object):
    def findPeakGrid(self, mat):
        lo, hi = 0, len(mat) - 1

        while lo < hi:
            mid = (lo + hi) // 2
            j = mat[mid].index(max(mat[mid]))
            if mat[mid][j] > mat[mid + 1][j]:
                hi = mid
            else:
                lo = mid + 1

        return [lo, mat[lo].index(max(mat[lo]))]
