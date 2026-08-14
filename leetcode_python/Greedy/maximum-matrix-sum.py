"""

1975. Maximum Matrix Sum
Medium

You are given an n x n integer matrix. You can do the following operation any number of times:

Choose any two adjacent elements of matrix and multiply each of them by -1.

Two elements are considered adjacent if and only if they share a border.

Your goal is to maximize the summation of the matrix's elements. Return the maximum sum of the matrix's elements using the operation mentioned above.


Example 1:

Input: matrix = [[1,-1],[-1,1]]
Output: 4
Explanation: We can follow the following steps to reach sum equals 4:
- Multiply the 2 elements in the first row by -1.
- Multiply the 2 elements in the first column by -1.

Example 2:

Input: matrix = [[1,2,3],[-1,-2,-3],[1,2,3]]
Output: 16
Explanation: We can follow the following step to reach sum equals 16:
- Multiply the 2 last elements in the second row by -1.


Constraints:

n == matrix.length == matrix[i].length
2 <= n <= 250
-10^5 <= matrix[i][j] <= 10^5

"""

# V0
# IDEA : GREEDY / PARITY INVARIANT
#
#   one operation flips the sign of TWO cells, so the PARITY of the number of
#   negative entries never changes. everything else is reachable : the grid is
#   connected, so any pair of minus signs can be walked together and cancelled.
#
#   - even number of negatives -> we can clear them all
#         answer = sum(|x|)
#   - odd number of negatives  -> exactly one minus sign must survive; park it
#     on the smallest |x| in the whole matrix
#         answer = sum(|x|) - 2 * min(|x|)
#
#   NOTE : a zero entry makes the odd case free (min |x| = 0), which the same
#          formula already handles.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def maxMatrixSum(self, matrix):
        total = 0
        neg = 0
        mi = float("inf")
        for row in matrix:
            for x in row:
                if x < 0:
                    neg += 1
                y = abs(x)
                total += y
                mi = min(mi, y)
        if neg % 2 == 0:
            return total
        return total - 2 * mi
