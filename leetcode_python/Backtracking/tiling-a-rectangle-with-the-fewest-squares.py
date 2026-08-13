"""

1240. Tiling a Rectangle with the Fewest Squares
Hard

Given a rectangle of size n x m, return the minimum number of integer-sided squares that tile the rectangle.


Example 1:

Input: n = 2, m = 3
Output: 3
Explanation: 3 squares are necessary to cover the rectangle.
2 (squares of 1x1)
1 (square of 2x2)

Example 2:

Input: n = 5, m = 8
Output: 5

Example 3:

Input: n = 11, m = 13
Output: 6


Constraints:

1 <= n, m <= 13

"""

# V0
# IDEA: BACKTRACKING + BITMASK (fill the first empty cell, scan row by row)
"""
 Key idea:
   - always work on the FIRST empty cell (i, j) in reading order
   - the square we place must have its top-left corner exactly there
   - enumerate the side length w, bounded by how far we can go
     down (r) and right (c) before hitting a filled cell
   - prune with `t + 1 < ans` (can't beat the current best)

 State:
   filled[i] is a bitmask of row i ; bit j set = cell (i, j) is covered
"""
# time = exponential (pruned) ; n, m <= 13
# space = O(n * m), recursion depth
class Solution(object):
    def tilingRectangle(self, n, m):
        def dfs(i, j, t):
            # end of row -> go to next row
            if j == m:
                i += 1
                j = 0
            # all rows done -> t is a complete tiling (better than ans by pruning)
            if i == n:
                self.ans = t
                return
            # already covered -> move right
            if filled[i] >> j & 1:
                dfs(i, j + 1, t)
                return
            if t + 1 >= self.ans:
                return

            # how far down / right is free from (i, j)
            r = c = 0
            for k in range(i, n):
                if filled[k] >> j & 1:
                    break
                r += 1
            for k in range(j, m):
                if filled[i] >> k & 1:
                    break
                c += 1
            mx = min(r, c)

            """
            NOTE !!!

                we GROW the square from side 1 up to mx.
                going from side w-1 to side w only needs the new
                bottom row + right column of the square to be marked,
                so the marking is incremental (no re-marking).
            """
            for w in range(1, mx + 1):
                for k in range(w):
                    filled[i + w - 1] |= 1 << (j + k)
                    filled[i + k] |= 1 << (j + w - 1)
                dfs(i, j + w, t + 1)

            # undo: the whole mx x mx block was marked by the loop above
            for x in range(i, i + mx):
                for y in range(j, j + mx):
                    filled[x] ^= 1 << y

        self.ans = n * m  # worst case: all 1x1 squares
        filled = [0] * n
        dfs(0, 0, 0)
        return self.ans
