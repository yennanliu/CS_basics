"""

1572. Matrix Diagonal Sum
Easy

Given a square matrix mat, return the sum of the matrix diagonals.

Only include the sum of all the elements on the primary diagonal and all the elements on the secondary diagonal that are not part of the primary diagonal.

Example 1:

Input: mat = [[1,2,3],
              [4,5,6],
              [7,8,9]]
Output: 25
Explanation: Diagonals sum: 1 + 5 + 9 + 3 + 7 = 25
Notice that element mat[1][1] = 5 is counted only once.

Example 2:

Input: mat = [[1,1,1,1],
              [1,1,1,1],
              [1,1,1,1],
              [1,1,1,1]]
Output: 8

Example 3:

Input: mat = [[5]]
Output: 5

Constraints:

n == mat.length == mat[i].length
1 <= n <= 100
1 <= mat[i][j] <= 100

"""

# V0
# IDEA : ARRAY (walk both diagonals at once, fix the double-counted centre)
#
#   row i contributes mat[i][i] (primary) and mat[i][n-1-i] (secondary).
#   NOTE : when n is odd both diagonals meet at mat[n//2][n//2], which
#          therefore gets added twice -> subtract it once.
#
# time = O(n), space = O(1)
class Solution(object):
    def diagonalSum(self, mat):
        n = len(mat)
        res = 0
        for i in range(n):
            res += mat[i][i] + mat[i][n - 1 - i]
        if n % 2:
            res -= mat[n // 2][n // 2]
        return res


# V0-1
# IDEA : FULL O(n^2) SCAN, KEEP THE CELLS THAT SIT ON EITHER DIAGONAL
#
#   visit every cell and add it when i == j (primary) OR i + j == n - 1
#   (secondary). because each cell is visited exactly once, the odd-n centre
#   — which satisfies both tests — is added once and the parity correction
#   of V0 is not needed at all.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def diagonalSum(self, mat):
        n = len(mat)
        res = 0
        for i in range(n):
            for j in range(n):
                if i == j or i + j == n - 1:
                    res += mat[i][j]
        return res


# V0-2
# IDEA : COLLECT THE INDEX PAIRS IN A SET AND LET THE SET DEDUPE
#
#   build {(i, i)} | {(i, n-1-i)} : for odd n the shared centre is produced
#   by both halves and the set keeps one copy, so summing over the set needs
#   no special case — and only the 2n - 1 relevant cells are touched.
#
# time = O(n), space = O(n)
class Solution(object):
    def diagonalSum(self, mat):
        n = len(mat)
        cells = {(i, i) for i in range(n)} | {(i, n - 1 - i) for i in range(n)}
        return sum(mat[i][j] for i, j in cells)
