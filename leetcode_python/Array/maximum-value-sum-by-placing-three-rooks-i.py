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


# V0-1
# IDEA : ROW SWEEP CARRYING "BEST k ROOKS THAT AVOID COLUMN c"
#
#   walk the rows once and carry two things about the rows already seen :
#       best1  — the 3 best single cells with PAIRWISE DISTINCT columns
#       best2[c] — the best 2-rook sum that uses no cell in column c
#
#   with those, the row currently being visited can act as the last rook :
#   picking cell (i, c) gives best2[c] + board[i][c], and only that row's top
#   three cells are ever worth trying (at most two columns are blocked).
#
#   why 3 candidates is provably enough for best1 : the two other rooks block
#   two columns, and three kept cells have three different columns, so at
#   least one survives — and it is at least as good as anything dropped
#   (dropped cells were either smaller than all three, or shared a column with
#   a larger kept cell).
#
#   this needs no combinations over row triples at all, so it stays linear in
#   the number of board cells and is what LC 3257 (m, n <= 500) requires.
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

            # this row carries the third rook
            for v, c in cur:
                if best2[c] > NEG and best2[c] + v > res:
                    res = best2[c] + v

            # extend best2 to "rows 0..i" : one rook here + one from best1
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
# IDEA : FIX THE MIDDLE ROW, BEST CELL ABOVE + BEST CELL BELOW
#
#   order the three rooks by row and sweep the MIDDLE one. what is needed from
#   the outer two is just the best single cell in the rows above and the best
#   single cell in the rows below, each dodging the columns already taken —
#   again at most two columns, so top-3-with-distinct-columns prefixes and
#   suffixes are enough.
#
#       pre[i] = top-3 distinct-column cells over rows 0..i
#       suf[i] = top-3 distinct-column cells over rows i..m-1
#
#   compared with V0's C(m, 3) enumeration this drops the cubic term entirely,
#   at the price of two auxiliary arrays of triples.
#
# time = O(m * n * log n), space = O(m)
class Solution(object):
    def maximumValueSum(self, board):
        m, n = len(board), len(board[0])

        def keep3(entries):
            best = []
            for v, c in sorted(entries, reverse=True):
                if any(c == oc for _, oc in best):
                    continue
                best.append((v, c))
                if len(best) == 3:
                    break
            return best

        rows = [keep3([(board[i][j], j) for j in range(n)]) for i in range(m)]

        pre = [None] * m
        acc = []
        for i in range(m):
            acc = keep3(acc + rows[i])
            pre[i] = acc

        suf = [None] * m
        acc = []
        for i in range(m - 1, -1, -1):
            acc = keep3(acc + rows[i])
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
                        if v1 + v2 + v3 > res:
                            res = v1 + v2 + v3
        return res
