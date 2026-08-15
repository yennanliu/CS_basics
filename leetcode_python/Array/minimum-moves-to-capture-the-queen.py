"""

3001. Minimum Moves to Capture The Queen
Medium

There is a 1-indexed 8 x 8 chessboard containing 3 pieces.

You are given 6 integers a, b, c, d, e, and f where:

(a, b) denotes the position of the white rook.
(c, d) denotes the position of the white bishop.
(e, f) denotes the position of the black queen.

Given that you can only move the white pieces, return the minimum number of moves required to capture the black queen.

Note that:

Rooks can move any number of squares either vertically or horizontally, but cannot jump over other pieces.
Bishops can move any number of squares diagonally, but cannot jump over other pieces.
A rook or a bishop can capture the queen if it is located in a square that they can move to.
The queen does not move.


Example 1:

Input: a = 1, b = 1, c = 8, d = 8, e = 2, f = 3
Output: 2
Explanation: We can capture the black queen in two moves by moving the white rook to (1, 3) then to (2, 3).
It is impossible to capture the black queen in less than two moves since it is not being attacked by any of the pieces at the beginning.

Example 2:

Input: a = 5, b = 3, c = 3, d = 4, e = 5, f = 2
Output: 1
Explanation: We can capture the black queen in a single move by doing one of the following:
- Move the white rook to (5, 2).
- Move the white bishop to (5, 2).


Constraints:

1 <= a, b, c, d, e, f <= 8

"""

# V0
# IDEA : THE ANSWER IS ONLY EVER 1 OR 2 — SO JUST TEST FOR 1
#
#   two moves ALWAYS suffice : the rook can reach the queen's row (or column)
#   in one move and take it on the next, and no third piece can block both
#   routes. so the only question is whether one move is enough.
#
#   one move works when a piece already attacks the queen :
#     rook   : shares a row or a column, and the bishop is not standing
#              strictly between them on that line
#     bishop : shares a diagonal (equal |dr| and |dc|), and the rook is not
#              standing strictly between them on that diagonal
#
#   "strictly between" on a line is just a coordinate test — the blocker must
#   sit on the same line AND its other coordinate must lie in the open
#   interval between the two pieces.
#
# time = O(1), space = O(1)
class Solution(object):
    def minMovesToCaptureTheQueen(self, a, b, c, d, e, f):

        def between(x, y, z):
            return min(x, z) < y < max(x, z)

        # rook and queen share a row / column, bishop not in the way
        if a == e and not (c == a and between(b, d, f)):
            return 1
        if b == f and not (d == b and between(a, c, e)):
            return 1

        # bishop and queen share a diagonal, rook not in the way
        if abs(c - e) == abs(d - f):
            if not (abs(a - c) == abs(b - d) and between(c, a, e) and between(d, b, f)):
                return 1

        return 2
