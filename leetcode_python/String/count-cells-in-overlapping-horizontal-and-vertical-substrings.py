"""

3529. Count Cells in Overlapping Horizontal and Vertical Substrings
Medium

You are given an m x n matrix grid consisting of characters and a string
pattern.

A horizontal substring is a contiguous sequence of characters read from left to
right. If the end of a row is reached before the substring is complete, it wraps
to the first column of the next row and continues as needed. You do not wrap
from the bottom row back to the top.

A vertical substring is a contiguous sequence of characters read from top to
bottom. If the bottom of a column is reached before the substring is complete,
it wraps to the first row of the next column and continues as needed. You do not
wrap from the last column back to the first.

Count the number of cells in the matrix that satisfy the following condition:

The cell must be part of at least one horizontal substring and at least one
vertical substring, where both substrings are equal to the given pattern.

Return the count of these cells.

Example 1:

Input: grid =
[["a","a","c","c"],["b","b","b","c"],["a","a","b","a"],["c","a","a","c"],["a","a","b","a"]],
pattern = "abaca"

Output: 1

Explanation:

The pattern "abaca" appears once as a horizontal substring (colored blue) and
once as a vertical substring (colored red), intersecting at one cell (colored
purple).

Example 2:

Input: grid =
[["c","a","a","a"],["a","a","b","a"],["b","b","a","a"],["a","a","b","a"]],
pattern = "aba"

Output: 4

Explanation:

The cells colored above are all part of at least one horizontal and one vertical
substring matching the pattern "aba".

Example 3:

Input: grid = [["a"]], pattern = "a"

Output: 1

Constraints:

m == grid.length

n == grid[i].length

1 <= m, n <= 1000

1 <= m * n <= 10^5

1 <= pattern.length <= m * n

grid and pattern consist of only lowercase English letters.

"""

# V0
# IDEA : FLATTEN THE GRID BOTH WAYS AND RUN KMP ON THE TWO STRINGS
#
#   "wraps to the next row but never from the bottom row back to the top" is
#   precisely the description of reading the grid row by row into one long
#   string: every horizontal substring of the grid is a contiguous window of
#   that string, and nothing else is.  the vertical rule does the same for the
#   column-major reading.
#
#   so the problem is two independent pattern searches.  KMP finds every
#   occurrence in linear time; each occurrence covers a contiguous block of
#   flat indices, and overlapping occurrences would make per-cell marking
#   quadratic, so the blocks are recorded in a difference array and turned into
#   a covered/not-covered flag with one prefix pass.
#
#   the only care needed is the index translation: position t of the
#   column-major string is cell (t % m, t // m), which is flat index
#   (t % m) * n + t // m in the row-major numbering the answer is counted in.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countCells(self, grid, pattern):
        m, n = len(grid), len(grid[0])
        total = m * n
        L = len(pattern)
        if L > total:
            return 0

        fail = [0] * L                      # KMP failure function
        j = 0
        for i in range(1, L):
            while j and pattern[i] != pattern[j]:
                j = fail[j - 1]
            if pattern[i] == pattern[j]:
                j += 1
            fail[i] = j

        def starts(text):
            out = []
            j = 0
            for i, ch in enumerate(text):
                while j and ch != pattern[j]:
                    j = fail[j - 1]
                if ch == pattern[j]:
                    j += 1
                if j == L:
                    out.append(i - L + 1)
                    j = fail[j - 1]
            return out

        def cover(text):
            diff = [0] * (total + 1)
            for s in starts(text):
                diff[s] += 1
                diff[s + L] -= 1
            run = 0
            flags = [False] * total
            for i in range(total):
                run += diff[i]
                flags[i] = run > 0
            return flags

        rows = "".join("".join(r) for r in grid)
        cols = "".join(grid[r][c] for c in range(n) for r in range(m))
        h = cover(rows)
        v = cover(cols)

        ans = 0
        for t in range(total):
            if v[t] and h[(t % m) * n + t // m]:
                ans += 1
        return ans
