"""

3256. Maximum Value Sum by Placing Three Rooks I
Hard

You are given a m x n 2D array board representing a chessboard, where board[i][j] represents the value of the cell (i, j).

Rooks in the same row or column attack each other. You need to place three rooks on the chessboard such that the rooks do not attack each other.

Return the maximum sum of the cell values on which the rooks are placed.


Example 1:

Input: board = [[-3,1,1,1],[-3,1,-3,1],[-3,2,1,1]]
Output: 4
Explanation:
We can place the rooks in the cells (0, 2), (1, 3), and (2, 1) for a sum of 1 + 1 + 2 = 4.

Example 2:

Input: board = [[1,2,3],[4,5,6],[7,8,9]]
Output: 15
Explanation:
We can place the rooks in the cells (0, 0), (1, 1), and (2, 2) for a sum of 1 + 5 + 9 = 15.

Example 3:

Input: board = [[1,1,1],[1,1,1],[1,1,1]]
Output: 3
Explanation:
We can place the rooks in the cells (0, 2), (1, 1), and (2, 0) for a sum of 1 + 1 + 1 = 3.


Constraints:

3 <= m == board.length <= 100
3 <= n == board[i].length <= 100
-10^9 <= board[i][j] <= 10^9

"""

# V0
# IDEA : ONLY EACH ROW'S TOP THREE CELLS CAN EVER BE USED
#
#   the three rooks occupy three distinct rows and three distinct columns. so
#   fix the rows first — then within a chosen row, the cell picked is the
#   best one whose column is free. at most two columns are blocked by the
#   other rooks, so the winner is always among that row's THREE largest
#   cells.
#
#   keeping the top three per row shrinks the search to
#       C(m, 3) row triples x 27 cell combinations
#   which for m = 100 is about 4 million checks — and each combination is
#   valid only when its three columns differ.
#
# time = O(m * n + m^3), space = O(m)
import itertools


class Solution(object):
    def maximumValueSum(self, board):
        m, n = len(board), len(board[0])

        # per row : the three highest cells as (value, column)
        top = []
        for i in range(m):
            best = sorted(((board[i][j], j) for j in range(n)), reverse=True)[:3]
            top.append(best)

        res = float('-inf')
        for r1, r2, r3 in itertools.combinations(range(m), 3):
            for a in top[r1]:
                for b in top[r2]:
                    if b[1] == a[1]:
                        continue
                    for c in top[r3]:
                        if c[1] == a[1] or c[1] == b[1]:
                            continue
                        total = a[0] + b[0] + c[0]
                        if total > res:
                            res = total
        return res
