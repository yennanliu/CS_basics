"""

3567. Minimum Absolute Difference in Sliding Submatrix
Medium

You are given an m x n integer matrix grid and an integer k.

For every contiguous k x k submatrix of grid, compute the minimum absolute difference between any two distinct values within that submatrix.

Return a 2D array ans of size (m - k + 1) x (n - k + 1), where ans[i][j] is the minimum absolute difference in the submatrix whose top-left corner is (i, j) in grid.

Note: If all elements in the submatrix have the same value, the answer will be 0.


Example 1:

Input: grid = [[1,8],[3,-2]], k = 2
Output: [[2]]
Explanation:
There is only one possible k x k submatrix: [[1, 8], [3, -2]].
Distinct values in the submatrix are [1, 8, 3, -2].
The minimum absolute difference in the submatrix is |1 - 3| = 2. Thus, the answer is [[2]].

Example 2:

Input: grid = [[3,-1]], k = 1
Output: [[0,0]]
Explanation:
Both k x k submatrix has only one distinct element. Thus, the answer is [[0, 0]].

Example 3:

Input: grid = [[1,-2,3],[2,3,5]], k = 2
Output: [[1,2]]
Explanation:
There are two possible k x k submatrix:
First: [[1, -2], [2, 3]].
Distinct values in the submatrix are [1, -2, 2, 3].
The minimum absolute difference in the submatrix is |1 - 2| = 1.
Second: [[-2, 3], [3, 5]].
Distinct values in the submatrix are [-2, 3, 5].
The minimum absolute difference in the submatrix is |3 - 5| = 2.
Thus, the answer is [[1, 2]].


Constraints:

1 <= m == grid.length <= 30
1 <= n == grid[i].length <= 30
-10^5 <= grid[i][j] <= 10^5
1 <= k <= min(m, n)

"""

# V0
# IDEA : BRUTE FORCE PER WINDOW + SORT THE DISTINCT VALUES
#
#   after sorting, the closest pair of distinct values is always a pair of
#   neighbours in the sorted order, so one linear scan over the sorted
#   distinct list gives the minimum gap. duplicates are dropped first
#   because the problem asks for *distinct* values (otherwise every repeated
#   value would trivially answer 0).
#
#   the grid is at most 30 x 30, so re-collecting each k x k window is
#   cheap enough.
#
# time = O(m * n * k^2 * log(k^2)), space = O(k^2)
class Solution(object):
    def minAbsDiff(self, grid, k):
        m, n = len(grid), len(grid[0])
        ans = [[0] * (n - k + 1) for _ in range(m - k + 1)]
        for i in range(m - k + 1):
            for j in range(n - k + 1):
                vals = set()
                for r in range(i, i + k):
                    row = grid[r]
                    for c in range(j, j + k):
                        vals.add(row[c])
                if len(vals) < 2:
                    ans[i][j] = 0
                    continue
                sv = sorted(vals)
                best = min(sv[t + 1] - sv[t] for t in range(len(sv) - 1))
                ans[i][j] = best
        return ans


# V0-1
# IDEA : COORDINATE COMPRESSION + BUCKET COUNTS SLID ACROSS THE COLUMNS
#
#   at most m * n <= 900 distinct values exist in the whole grid, so compress
#   them once to 0 .. D-1. then, for a fixed band of k rows, slide the window
#   one column at a time: drop the k cells of the leaving column and add the k
#   cells of the entering column in the count array.
#
#   with counts in hand the answer needs no sort — scanning t = 0 .. D-1 walks
#   the present values in ascending order, and the smallest gap between two
#   consecutive present values is the answer (0 when fewer than two are
#   present).
#
# time = O(m * n * k + m * n * D), space = O(m * n + D)
class Solution(object):
    def minAbsDiff(self, grid, k):
        m, n = len(grid), len(grid[0])
        vals = sorted(set(v for row in grid for v in row))
        pos = {v: i for i, v in enumerate(vals)}
        D = len(vals)
        code = [[pos[v] for v in row] for row in grid]

        ans = [[0] * (n - k + 1) for _ in range(m - k + 1)]
        for i in range(m - k + 1):
            cnt = [0] * D
            for r in range(i, i + k):
                for c in range(k):
                    cnt[code[r][c]] += 1
            for j in range(n - k + 1):
                if j:
                    for r in range(i, i + k):
                        cnt[code[r][j - 1]] -= 1
                        cnt[code[r][j + k - 1]] += 1
                best = 0
                prev = -1
                for t in range(D):
                    if cnt[t]:
                        if prev != -1:
                            gap = vals[t] - vals[prev]
                            if best == 0 or gap < best:
                                best = gap
                        prev = t
                ans[i][j] = best
        return ans


# V0-2
# IDEA : BITSET UNION OF ROW SEGMENTS + LOWEST-SET-BIT WALK
#
#   after compression the "set of values in a window" is just a bitmask, and a
#   k x k window is the OR of the k row segments grid[r][j : j+k]. precompute
#   every row segment mask once, then each window costs k big-int ORs.
#
#   extracting the sorted present values from a mask is the classic
#   mask & -mask trick : the lowest set bit is always the next value up, so one
#   walk over the set bits yields the consecutive pairs whose gap we minimise.
#
#   NOTE : OR cannot un-set a bit, so masks are built per (row, start) rather
#          than slid — which is why the row-segment precompute is O(m * n * k).
#
# time = O(m * n * k + m * n * (k + D / 64)), space = O(m * n)
class Solution(object):
    def minAbsDiff(self, grid, k):
        m, n = len(grid), len(grid[0])
        vals = sorted(set(v for row in grid for v in row))
        pos = {v: i for i, v in enumerate(vals)}

        rowmask = [[0] * (n - k + 1) for _ in range(m)]
        for r in range(m):
            bits = [1 << pos[v] for v in grid[r]]
            for j in range(n - k + 1):
                mask = 0
                for c in range(j, j + k):
                    mask |= bits[c]
                rowmask[r][j] = mask

        ans = [[0] * (n - k + 1) for _ in range(m - k + 1)]
        for i in range(m - k + 1):
            for j in range(n - k + 1):
                mask = 0
                for r in range(i, i + k):
                    mask |= rowmask[r][j]
                best = 0
                prev = -1
                while mask:
                    low = mask & -mask
                    t = low.bit_length() - 1
                    if prev != -1:
                        gap = vals[t] - vals[prev]
                        if best == 0 or gap < best:
                            best = gap
                    prev = t
                    mask ^= low
                ans[i][j] = best
        return ans
