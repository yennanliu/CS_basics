"""

2133. Check if Every Row and Column Contains All Numbers
Easy

An n x n matrix is valid if every row and every column contains all the integers from 1 to n (inclusive).

Given an n x n integer matrix matrix, return true if the matrix is valid. Otherwise, return false.


Example 1:

Input: matrix = [[1,2,3],[3,1,2],[2,3,1]]
Output: true
Explanation: In this case, n = 3, and every row and column contains the numbers 1, 2, and 3.
Hence, we return true.

Example 2:

Input: matrix = [[1,1,1],[1,2,3],[1,2,3]]
Output: false
Explanation: In this case, n = 3, but the first row and the first column do not contain the numbers 2 or 3.
Hence, we return false.


Constraints:

n == matrix.length == matrix[i].length
1 <= n <= 100
1 <= matrix[i][j] <= n

"""

# V0
# IDEA : EVERY ROW AND EVERY COLUMN MUST BE A SET OF SIZE n
#
#   values are already constrained to [1, n], so "contains all of 1..n" is
#   equivalent to "has n DISTINCT values" — a simple set-size check.
#
#   zip(*matrix) hands over the columns without building them by hand.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def checkValid(self, matrix):
        n = len(matrix)
        return (all(len(set(row)) == n for row in matrix)
                and all(len(set(col)) == n for col in zip(*matrix)))
