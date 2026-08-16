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
