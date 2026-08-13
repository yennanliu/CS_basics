"""

699. Falling Squares
Hard

There are several squares being dropped onto the X-axis of a 2D plane.

You are given a 2D integer array positions where positions[i] = [left_i, sideLength_i]
represents the ith square with a side length of sideLength_i that is dropped with its
left edge aligned with X-coordinate left_i.

Each square is dropped one at a time from a height above any landed squares.
It then falls downward (negative Y direction) until it either lands on the top side
of another square or on the X-axis. A square brushing the left/right side of another
square does not count as landing on it. Once it lands, it freezes in place and cannot be moved.

After each square is dropped, you must record the height of the current tallest stack of squares.

Return an integer array ans where ans[i] represents the height described above
after dropping the ith square.


Example 1:

Input: positions = [[1,2],[2,3],[6,1]]
Output: [2,5,5]
Explanation:
After the first drop, the tallest stack is square 1 with a height of 2.
After the second drop, the tallest stack is squares 1 and 2 with a height of 5.
After the third drop, the tallest stack is still squares 1 and 2 with a height of 5.
Thus, we return an answer of [2, 5, 5].

Example 2:

Input: positions = [[100,100],[200,100]]
Output: [100,100]
Explanation:
After the first drop, the tallest stack is square 1 with a height of 100.
After the second drop, the tallest stack is either square 1 or square 2, both with heights of 100.
Thus, we return an answer of [100, 100].
Note that square 2 only brushes the right side of square 1, which does not count as landing on it.


Constraints:

1 <= positions.length <= 1000
1 <= left_i <= 10^8
1 <= sideLength_i <= 10^6

"""

# V0
# IDEA : BRUTE FORCE + INTERVAL OVERLAP
#
#   Keep every landed square as (left, right, top_height).
#   A new square [l, r) lands on the tallest square it *strictly* overlaps with
#   (touching edges do NOT count -> use `l < r_j and l_j < r`).
#   Then track the running max height.
#
# time = O(n^2)
# space = O(n)
class Solution(object):
    def fallingSquares(self, positions):
        landed = []  # (left, right, top height) of already dropped squares
        res = []
        cur_max = 0

        for left, side in positions:
            right = left + side  # half open interval [left, right)

            # the square rests on top of the highest square it overlaps with
            base = 0
            for l, r, h in landed:
                # strict overlap only: brushing a side is not landing on it
                if left < r and l < right:
                    base = max(base, h)

            top = base + side
            landed.append((left, right, top))

            cur_max = max(cur_max, top)
            res.append(cur_max)

        return res
