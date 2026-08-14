"""

1222. Queens That Can Attack the King
Medium

On a 0-indexed 8 x 8 chessboard, there can be multiple black queens and one white king.

You are given a 2D integer array queens where queens[i] = [xQueen_i, yQueen_i] represents
the position of the ith black queen on the chessboard. You are also given an integer
array king of length 2 where king = [xKing, yKing] represents the position of the white
king.

Return the coordinates of the black queens that can directly attack the king.
You may return the answer in any order.

Example 1:

Input: queens = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]], king = [0,0]
Output: [[0,1],[1,0],[3,3]]

Example 2:

Input: queens = [[0,0],[1,1],[2,2],[3,4],[3,5],[4,4],[4,5]], king = [3,3]
Output: [[2,2],[3,4],[4,4]]


Constraints:

1 <= queens.length < 64
queens[i].length == king.length == 2
0 <= xQueen_i, yQueen_i, xKing, yKing < 8
All the given positions are unique.

"""

# V0
# IDEA : SIMULATION / RAY CASTING FROM THE KING
#        walk outward from the king in the 8 directions; the FIRST queen met on
#        a ray is the only one attacking on that ray (the rest are blocked by it)
# time = O(8 * 8)
# space = O(n)
class Solution(object):
    def queensAttacktheKing(self, queens, king):
        N = 8
        qs = set((x, y) for x, y in queens)
        res = []
        for dx in (-1, 0, 1):
            for dy in (-1, 0, 1):
                if dx == 0 and dy == 0:
                    continue
                x, y = king[0] + dx, king[1] + dy
                while 0 <= x < N and 0 <= y < N:
                    if (x, y) in qs:
                        res.append([x, y])
                        break
                    x += dx
                    y += dy
        return res
