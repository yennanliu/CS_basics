"""

1253. Reconstruct a 2-Row Binary Matrix
Medium

Given the following details of a matrix with n columns and 2 rows :

The matrix is a binary matrix, which means each element in the matrix can be 0 or 1.
The sum of elements of the 0-th(upper) row is given as upper.
The sum of elements of the 1-st(lower) row is given as lower.
The sum of elements in the i-th column(0-indexed) is colsum[i], where colsum is given as an integer array with length n.

Your task is to reconstruct the matrix with upper, lower and colsum.

Return it as a 2-D integer array.

If there are more than one valid solution, any of them will be accepted.

If no valid solution exists, return an empty 2-D array.


Example 1:

Input: upper = 2, lower = 1, colsum = [1,1,1]
Output: [[1,1,0],[0,0,1]]
Explanation: [[1,0,1],[0,1,0]], and [[0,1,1],[1,0,0]] are also correct answers.

Example 2:

Input: upper = 2, lower = 3, colsum = [2,2,1,1]
Output: []

Example 3:

Input: upper = 5, lower = 5, colsum = [2,1,2,0,1,0,1,2,0,1]
Output: [[1,1,1,0,1,0,0,1,0,0],[1,0,1,0,0,0,1,1,0,1]]


Constraints:

1 <= colsum.length <= 10^5
0 <= upper, lower <= colsum.length
0 <= colsum[i] <= 2

"""

# V0
# IDEA : GREEDY (columns with sum 2 are forced ; sum 1 goes to the "richer" row)
"""
 - colsum[j] == 2 : both rows must be 1 -> upper--, lower--
 - colsum[j] == 1 : free choice. Give it to whichever row still needs MORE
                    (upper > lower -> upper row, else lower row).
                    Greedy is safe: keeping the two budgets balanced never
                    makes a later forced column (a 2) impossible.
 - colsum[j] == 0 : both rows are 0

 If any budget goes negative, or if they are not both 0 at the end -> no answer.
"""
# time = O(n)
# space = O(1)
# (excluding the output matrix)
class Solution(object):
    def reconstructMatrix(self, upper, lower, colsum):
        n = len(colsum)
        res = [[0] * n, [0] * n]

        for j, v in enumerate(colsum):
            if v == 2:
                res[0][j] = res[1][j] = 1
                upper -= 1
                lower -= 1
            elif v == 1:
                if upper > lower:
                    res[0][j] = 1
                    upper -= 1
                else:
                    res[1][j] = 1
                    lower -= 1
            if upper < 0 or lower < 0:
                return []

        return res if upper == 0 and lower == 0 else []
