"""

3189. Minimum Moves to Get a Peaceful Board
Medium
🔒 (premium)

Given a 2D array rooks of length n, where rooks[i] = [x_i, y_i] indicates the position of a rook on an n x n chess board. Your task is to move the rooks 1 cell at a time vertically or horizontally (to an adjacent cell) such that the board becomes peaceful.

A board is peaceful if there is exactly one rook in each row and each column.

Return the minimum number of moves required to get a peaceful board.

Note that at no point can there be two rooks in the same cell.


Example 1:

Input: rooks = [[0,0],[1,0],[1,1]]
Output: 3
Explanation:
Move the rook from (1,0) to (2,0) and then from (1,1) to (1,2). Together with the rook already at (0,0) each row and column then holds exactly one rook.

Example 2:

Input: rooks = [[0,0],[0,1],[0,2],[0,3]]
Output: 6
Explanation:
The rooks all share row 0, so three of them must move down to rows 1, 2 and 3.


Constraints:

1 <= n == rooks.length <= 500
0 <= x_i, y_i <= n - 1
The input is generated such that there are no 2 rooks in the same cell.

"""

# V0
# IDEA : ROWS AND COLUMNS ARE INDEPENDENT — SORT EACH AND MATCH IN ORDER
#
#   a vertical move changes only a rook's row, a horizontal one only its
#   column, so the two axes never interact and the total cost splits into
#   "fix the rows" plus "fix the columns".
#
#   on one axis, the n rooks must end up on the n distinct values 0..n-1, and
#   the cheapest assignment for points on a line is the SORTED one — sorted
#   coordinates paired with 0, 1, 2, ... (any crossing pair can be uncrossed
#   without increasing the total distance).
#
#   so the answer is sum |sorted_x[i] - i| + sum |sorted_y[i] - i|.
#
#   NOTE : the "no two rooks share a cell mid-way" clause never costs extra —
#          the moves can always be sequenced to avoid collisions.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minMoves(self, rooks):
        xs = sorted(p[0] for p in rooks)
        ys = sorted(p[1] for p in rooks)
        return (sum(abs(v - i) for i, v in enumerate(xs))
                + sum(abs(v - i) for i, v in enumerate(ys)))
