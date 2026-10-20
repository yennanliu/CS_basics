"""

1314. Matrix Block Sum
Medium

Given a m x n matrix mat and an integer k, return a matrix answer where each
answer[i][j] is the sum of all elements mat[r][c] for:

i - k <= r <= i + k,
j - k <= c <= j + k, and
(r, c) is a valid position in the matrix.

Example 1:

Input: mat = [[1,2,3],[4,5,6],[7,8,9]], k = 1
Output: [[12,21,16],[27,45,33],[24,39,28]]

Example 2:

Input: mat = [[1,2,3],[4,5,6],[7,8,9]], k = 2
Output: [[45,45,45],[45,45,45],[45,45,45]]

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n, k <= 100
1 <= mat[i][j] <= 100

"""

# V0
# IDEA : 2D PREFIX SUM (SUMMED-AREA TABLE)
#
#   prefix[i][j] = sum of mat[0..i-1][0..j-1]   (padded by one 0 row / col)
#
#   build:  prefix[i][j] = mat[i-1][j-1] + top + left - top_left
#           (top and left both contain the top-left rectangle -> subtract it once)
#
#   query rectangle rows r1..r2, cols c1..c2 (inclusive, 0-indexed):
#           prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1]
#
#   Each block is that rectangle, clamped to the grid, so every cell is O(1).
#   (brute force sums each (2k+1)^2 block -> O(m * n * k^2))
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def matrixBlockSum(self, mat, k):
        """
        :type mat: List[List[int]]
        :type k: int
        :rtype: List[List[int]]
        """
        if not mat or not mat[0]:
            return []

        rows = len(mat)
        cols = len(mat[0])

        # prefix[i][j] = sum of the rectangle mat[0..i-1][0..j-1]
        prefix = [[0] * (cols + 1) for _ in range(rows + 1)]
        for i in range(1, rows + 1):
            for j in range(1, cols + 1):
                top = prefix[i - 1][j]
                left = prefix[i][j - 1]
                top_left = prefix[i - 1][j - 1]
                prefix[i][j] = mat[i - 1][j - 1] + top + left - top_left

        answer = [[0] * cols for _ in range(rows)]
        for i in range(rows):
            for j in range(cols):
                # clamp the block to the grid (inclusive, 0-indexed bounds)
                r1 = max(0, i - k)
                c1 = max(0, j - k)
                r2 = min(rows - 1, i + k)
                c2 = min(cols - 1, j + k)
                # NOTE !!! prefix is shifted by 1: the bottom-right uses r2+1 / c2+1,
                #          the top-left cut uses r1 / c1 as-is
                answer[i][j] = (prefix[r2 + 1][c2 + 1]
                                - prefix[r1][c2 + 1]
                                - prefix[r2 + 1][c1]
                                + prefix[r1][c1])

        return answer


# V1
# IDEA : 1D PREFIX SUM PER ROW
#
#   row_prefix[r][c] = sum of mat[r][0..c-1]. A block is (2k+1) row slices,
#   and each slice sum is one subtraction. Simpler to derive than V0 and fine
#   for these constraints (100^3 = 10^6), but a factor of k slower - use V0
#   in an interview, mention this as the step between brute force and V0.
#
# time = O(m * n * k), space = O(m * n)
class Solution2(object):
    def matrixBlockSum(self, mat, k):
        """
        :type mat: List[List[int]]
        :type k: int
        :rtype: List[List[int]]
        """
        if not mat or not mat[0]:
            return []

        rows = len(mat)
        cols = len(mat[0])

        # row_prefix[r][c] = sum of mat[r][0..c-1]
        row_prefix = []
        for r in range(rows):
            sums = [0] * (cols + 1)
            for c in range(cols):
                sums[c + 1] = sums[c] + mat[r][c]
            row_prefix.append(sums)

        answer = [[0] * cols for _ in range(rows)]
        for i in range(rows):
            for j in range(cols):
                c1 = max(0, j - k)
                c2 = min(cols - 1, j + k)
                block_sum = 0
                # add the slice [c1, c2] of every row the block covers
                for r in range(max(0, i - k), min(rows - 1, i + k) + 1):
                    block_sum += row_prefix[r][c2 + 1] - row_prefix[r][c1]
                answer[i][j] = block_sum

        return answer
