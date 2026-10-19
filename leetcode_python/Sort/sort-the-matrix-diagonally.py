"""

1329. Sort the Matrix Diagonally
Medium

A matrix diagonal is a diagonal line of cells starting from some cell in
either the topmost row or leftmost column and going in the bottom-right
direction until reaching the matrix's end. For example, the matrix diagonal
starting from mat[2][0], where mat is a 6 x 3 matrix, includes cells
mat[2][0], mat[3][1], and mat[4][2].

Given an m x n matrix mat of integers, sort each matrix diagonal in
ascending order and return the resulting matrix.

Example 1:

Input: mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]]

Output: [[1,1,1,1],[1,2,2,2],[1,2,3,3]]

Example 2:

Input: mat = [[11,25,66,1,69,7],[23,55,17,45,15,52],[75,31,36,44,58,8],[22,27,33,25,68,4],[84,28,14,11,5,50]]

Output: [[5,17,4,1,52,7],[11,11,25,45,8,69],[14,23,25,44,58,15],[22,27,31,36,50,66],[84,28,75,33,55,68]]

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 100
1 <= mat[i][j] <= 100

"""

# V0
# IDEA : SORT + HASHMAP, GROUP CELLS BY (r - c)
#
#   every cell on one top-left -> bottom-right diagonal has the SAME r - c
#   (moving down-right adds 1 to both r and c, so the difference is fixed).
#   so r - c is a key that names the diagonal:
#
#   1. bucket every value by r - c
#   2. sort each bucket
#   3. walk the matrix again in row-major order; inside one diagonal that
#      visits cells top-left first, so hand out each bucket's values in
#      ascending order
#
#   e.g. mat = [[3,3],[2,1]] -> key 0: [3,1] -> sorted [1,3]
#        -> (0,0)=1, (1,1)=3
#
# time = O(m * n * log(min(m, n))), space = O(m * n)
from collections import defaultdict


class Solution(object):
    def diagonalSort(self, mat):
        """
        :type mat: List[List[int]]
        :rtype: List[List[int]]
        """
        if not mat or not mat[0]:
            return mat

        rows = len(mat)
        cols = len(mat[0])

        # diagonal key (r - c) -> every value on that diagonal
        diagonals = defaultdict(list)
        for r in range(rows):
            for c in range(cols):
                diagonals[r - c].append(mat[r][c])

        # each diagonal is at most min(m, n) long
        for key in diagonals:
            diagonals[key].sort()

        # next unused position in each sorted diagonal
        next_index = defaultdict(int)
        for r in range(rows):
            for c in range(cols):
                key = r - c
                # NOTE !!! row-major order reaches a diagonal's cells from
                #          top-left to bottom-right, i.e. smallest slot first
                mat[r][c] = diagonals[key][next_index[key]]
                next_index[key] += 1

        # LC expects the matrix itself back; it is sorted in place
        return mat


# V1
# IDEA : SORT ONE DIAGONAL AT A TIME, FROM ITS START CELL
#
#   every diagonal starts on the top row or the left column, so there are
#   exactly m + n - 1 start cells. walk each one down-right, collect its
#   values, sort them, then walk it again writing them back.
#
#   same time as V0, but only one diagonal is held at once, so extra space
#   is O(min(m, n)) instead of a copy of the whole matrix.
#   NOTE : the version to give when the interviewer asks to cut the space
#
# time = O(m * n * log(min(m, n))), space = O(min(m, n))
class Solution2(object):
    def diagonalSort(self, mat):
        """
        :type mat: List[List[int]]
        :rtype: List[List[int]]
        """
        if not mat or not mat[0]:
            return mat

        rows = len(mat)
        cols = len(mat[0])

        # start cells: the whole left column, then the top row minus (0, 0)
        starts = []
        for r in range(rows):
            starts.append((r, 0))
        for c in range(1, cols):
            starts.append((0, c))

        for start_r, start_c in starts:
            self.sort_diagonal(mat, start_r, start_c)

        return mat

    def sort_diagonal(self, mat, start_r, start_c):
        """Sort the single diagonal that begins at (start_r, start_c) in place."""
        rows = len(mat)
        cols = len(mat[0])

        # collect the diagonal's values
        values = []
        r, c = start_r, start_c
        while r < rows and c < cols:
            values.append(mat[r][c])
            r += 1
            c += 1

        values.sort()

        # write them back along the same path
        r, c = start_r, start_c
        for val in values:
            mat[r][c] = val
            r += 1
            c += 1


# V2
# IDEA : COUNTING SORT PER DIAGONAL (values are bounded: 1 <= v <= 100)
#
#   same walk as V1, but the comparison sort is replaced by a counting sort
#   over the value range, which removes the log factor.
#   NOTE : only works because the constraints cap the values; learn V0 / V1
#          first and mention this as the follow-up
#
# time = O(m * n + (m + n) * K), space = O(K), K = 100 (value range)
class Solution3(object):
    MAX_VALUE = 100

    def diagonalSort(self, mat):
        """
        :type mat: List[List[int]]
        :rtype: List[List[int]]
        """
        if not mat or not mat[0]:
            return mat

        rows = len(mat)
        cols = len(mat[0])

        starts = []
        for r in range(rows):
            starts.append((r, 0))
        for c in range(1, cols):
            starts.append((0, c))

        for start_r, start_c in starts:
            # count how many times each value appears on this diagonal
            counts = [0] * (self.MAX_VALUE + 1)
            r, c = start_r, start_c
            while r < rows and c < cols:
                counts[mat[r][c]] += 1
                r += 1
                c += 1

            # rewrite the diagonal, smallest value first
            r, c = start_r, start_c
            value = 0
            while r < rows and c < cols:
                # skip values that are used up
                while counts[value] == 0:
                    value += 1
                mat[r][c] = value
                counts[value] -= 1
                r += 1
                c += 1

        return mat
