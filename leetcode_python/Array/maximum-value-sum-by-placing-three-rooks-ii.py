"""

3257. Maximum Value Sum by Placing Three Rooks II
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

3 <= m == board.length <= 500
3 <= n == board[i].length <= 500
-10^9 <= board[i][j] <= 10^9

"""

# V0
# IDEA : FIX THE MIDDLE ROW, THEN TAKE THE BEST ROW ABOVE AND BELOW
#
#   the three rooks sit in three distinct rows, so name them by row order and
#   sweep the MIDDLE one. what is needed from the other two is just "the best
#   single cell in the rows above" and "the best single cell in the rows
#   below" — each avoiding the columns already used.
#
#   at most two columns are ever blocked, so keeping the TOP THREE cells with
#   PAIRWISE DISTINCT COLUMNS in each prefix and suffix is enough : among
#   three distinct columns at least one survives any two exclusions.
#
#   so precompute
#       pre[i] = top-3 distinct-column cells over rows 0..i
#       suf[i] = top-3 distinct-column cells over rows i..m-1
#   and for each middle row try its own top-3 cells against those.
#
#   LC 3256 is the same problem at m = 100 where C(m,3) brute force fits;
#   here m reaches 500 and that would be 2*10^7 row triples.
#
# time = O(m * n log n), space = O(m)
class Solution(object):
    def maximumValueSum(self, board):
        m, n = len(board), len(board[0])

        def merge(entries):
            """keep the 3 best (value, col) with pairwise distinct columns"""
            best = []
            for v, c in sorted(entries, reverse=True):
                if any(c == oc for _, oc in best):
                    continue
                best.append((v, c))
                if len(best) == 3:
                    break
            return best

        rows = [merge((board[i][j], j) for j in range(n)) for i in range(m)]

        pre = [None] * m
        acc = []
        for i in range(m):
            acc = merge(acc + rows[i])
            pre[i] = acc

        suf = [None] * m
        acc = []
        for i in range(m - 1, -1, -1):
            acc = merge(acc + rows[i])
            suf[i] = acc

        res = float('-inf')
        for mid in range(1, m - 1):
            for v2, c2 in rows[mid]:
                for v1, c1 in pre[mid - 1]:
                    if c1 == c2:
                        continue
                    for v3, c3 in suf[mid + 1]:
                        if c3 == c1 or c3 == c2:
                            continue
                        total = v1 + v2 + v3
                        if total > res:
                            res = total
        return res


# V0-1
# IDEA : ROW SWEEP CARRYING "BEST k ROOKS THAT AVOID COLUMN c"
#
#   same top-3 insight as V0 but organised as a single left-to-right (here
#   top-to-bottom) DP instead of prefix + suffix tables. carried state :
#       best1     — 3 best single cells over the rows already seen, columns
#                   pairwise distinct
#       best2[c]  — best 2-rook sum over the rows already seen that touches no
#                   cell in column c
#
#   at row i : the row can hold the LAST rook, worth best2[c] + board[i][c]
#   for one of its top three cells; then best2 is pushed forward with
#   "one rook in row i + one from best1", and best1 absorbs row i.
#
#   only ONE pass and O(n) carried numbers — no suffix array, so it also works
#   streaming the board row by row.
#
# time = O(m * n * log n), space = O(n)
class Solution(object):
    def maximumValueSum(self, board):
        m, n = len(board), len(board[0])
        NEG = float('-inf')

        def keep3(entries):
            """3 best (value, col), columns pairwise distinct, value-desc"""
            best = []
            for v, c in sorted(entries, reverse=True):
                if any(c == oc for _, oc in best):
                    continue
                best.append((v, c))
                if len(best) == 3:
                    break
            return best

        rows = [sorted(((board[i][j], j) for j in range(n)),
                       reverse=True)[:3] for i in range(m)]

        best1 = []
        best2 = [NEG] * n
        res = NEG
        for i in range(m):
            cur = rows[i]

            for v, c in cur:
                if best2[c] > NEG and best2[c] + v > res:
                    res = best2[c] + v

            if best1:
                for c in range(n):
                    top = best2[c]
                    for v2, c2 in cur:
                        if c2 == c:
                            continue
                        for v1, c1 in best1:
                            if c1 == c or c1 == c2:
                                continue
                            if v1 + v2 > top:
                                top = v1 + v2
                            break          # best1 is value-desc
                    best2[c] = top

            best1 = keep3(best1 + cur)
        return res


# V0-2
# IDEA : ENUMERATE THE ROW TRIPLE DIRECTLY OVER EACH ROW'S TOP THREE CELLS
#
#   the reference / baseline version (and the intended solution of LC 3256,
#   where m <= 100) : keep each row's three largest cells, then try every
#   C(m, 3) choice of rows against the 27 cell combinations, skipping any that
#   repeats a column.
#
#   correct here too, but O(m^3) means ~2 * 10^7 row triples at m = 500, so it
#   is the one to check V0 / V0-1 against on small boards rather than to
#   submit at this problem's limits.
#
# time = O(m * n * log n + m^3), space = O(m)
import itertools


class Solution(object):
    def maximumValueSum(self, board):
        m, n = len(board), len(board[0])
        top = [sorted(((board[i][j], j) for j in range(n)),
                      reverse=True)[:3] for i in range(m)]

        res = float('-inf')
        for r1, r2, r3 in itertools.combinations(range(m), 3):
            for v1, c1 in top[r1]:
                for v2, c2 in top[r2]:
                    if c2 == c1:
                        continue
                    for v3, c3 in top[r3]:
                        if c3 == c1 or c3 == c2:
                            continue
                        if v1 + v2 + v3 > res:
                            res = v1 + v2 + v3
        return res
