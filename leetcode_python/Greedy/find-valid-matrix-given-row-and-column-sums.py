"""

1605. Find Valid Matrix Given Row and Column Sums
Medium

You are given two arrays rowSum and colSum of non-negative integers where rowSum[i] is the sum of the elements in the ith row and colSum[j] is the sum of the elements of the jth column of a 2D matrix. In other words, you do not know the elements of the matrix, but you do know the sums of each row and column.

Find any matrix of non-negative integers of size rowSum.length x colSum.length that satisfies the rowSum and colSum requirements.

Return a 2D array representing any matrix that fulfills the requirements. It's guaranteed that at least one matrix that fulfills the requirements exists.


Example 1:

Input: rowSum = [3,8], colSum = [4,7]
Output: [[3,0],
         [1,7]]
Explanation:
0th row: 3 + 0 = 3 == rowSum[0]
1st row: 1 + 7 = 8 == rowSum[1]
0th column: 3 + 1 = 4 == colSum[0]
1st column: 0 + 7 = 7 == colSum[1]
The row and column sums match, and all matrix elements are non-negative.
Another possible matrix is: [[1,2],
                             [3,5]]

Example 2:

Input: rowSum = [5,7,10], colSum = [8,6,8]
Output: [[0,5,0],
         [6,1,0],
         [2,0,8]]


Constraints:

1 <= rowSum.length, colSum.length <= 500
0 <= rowSum[i], colSum[i] <= 10^8
sum(rowSum) == sum(colSum)

"""

# V0
# IDEA : GREEDY (fill each cell with as much as it can possibly take)
#
#   scan cells row by row and put res[i][j] = min(rowSum[i], colSum[j]),
#   then subtract that amount from both remainders.
#
#   why it works : each step zeroes out at least one of the two remainders,
#   so a row (or a column) is "closed" every step. Since the totals are
#   equal, when we reach the last cell both remainders hit 0 together, and
#   nothing is ever left over.
#
#   NOTE : this mutates the input arrays; copy them if that matters.
#
# time = O(m * n), space = O(1) extra (besides the output)
class Solution(object):
    def restoreMatrix(self, rowSum, colSum):
        m, n = len(rowSum), len(colSum)
        row = list(rowSum)
        col = list(colSum)
        res = [[0] * n for _ in range(m)]
        for i in range(m):
            for j in range(n):
                v = min(row[i], col[j])
                res[i][j] = v
                row[i] -= v
                col[j] -= v
        return res
