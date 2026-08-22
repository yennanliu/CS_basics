"""

2732. Find a Good Subset of the Matrix
Hard

You are given a 0-indexed m x n binary matrix grid.

Let us call a non-empty subset of rows good if the sum of each column of the subset is at most half of the length of the subset.

More formally, if the length of the chosen subset of rows is k, then the sum of each column should be at most floor(k / 2).

Return an integer array that contains row indices of a good subset sorted in ascending order.

If there are multiple good subsets, you can return any of them. If there are no good subsets, return an empty array.

A subset of rows of the matrix grid is any matrix that can be obtained by deleting some (possibly none or all) rows from grid.


Example 1:

Input: grid = [[0,1,1,0],[0,0,0,1],[1,1,1,1]]
Output: [0,1]
Explanation: We can choose the 0th and 1st rows to create a good subset of rows.
The length of the chosen subset is 2.
- The sum of the 0th column is 0 + 0 = 0, which is at most half of the length of the subset.
- The sum of the 1st column is 1 + 0 = 1, which is at most half of the length of the subset.
- The sum of the 2nd column is 1 + 0 = 1, which is at most half of the length of the subset.
- The sum of the 3rd column is 0 + 1 = 1, which is at most half of the length of the subset.

Example 2:

Input: grid = [[0]]
Output: [0]
Explanation: We can choose the 0th row to create a good subset of rows.
The length of the chosen subset is 1.
- The sum of the 0th column is 0, which is at most half of the length of the subset.

Example 3:

Input: grid = [[1,1,1],[1,1,1]]
Output: []
Explanation: It is impossible to choose any subset of rows to create a good subset.


Constraints:

m == grid.length
n == grid[i].length
1 <= m <= 10^4
1 <= n <= 5
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : BITMASK ROWS + PROOF THAT ONLY k = 1 OR k = 2 CAN EVER WIN
#
#   encode each row as a bitmask over its <= 5 columns.
#
#   why only k in {1, 2} matters:
#     k = 1 -> every column sum must be <= 0  => an all-zero row.
#     k = 2 -> every column sum must be <= 1  => two rows whose AND is 0.
#     k = 3 -> cap is still floor(3/2) = 1, so any 2 of the 3 rows already
#              form a valid k = 2 subset. Nothing new. Same for every odd k.
#     k = 4 -> cap is 2. If no valid pair exists, EVERY one of the C(4,2) = 6
#              pairs shares a column where both are 1, so by pigeonhole some
#              column is shared by >= 2 pairs among n <= 5 columns, which
#              forces that column's sum to be >= 3 > 2. Contradiction.
#              The same counting kills every larger even k.
#
#   so: scan for an all-zero row; otherwise test every pair of DISTINCT masks
#   for mask_a & mask_b == 0.
#
#   NOTE : keeping one representative index per mask is enough (any row with
#          the same mask is interchangeable) -> at most 2^n = 32 candidates,
#          so the pair loop is O(4^n), not O(m^2).
#   NOTE : a & b == 0 with a == b would force a == 0, which the all-zero check
#          already returned on — so a pair from the dict is never one row
#          matched against itself.
#   NOTE : the two indices must be returned in ASCENDING order.
#
# time = O(m * n + 4^n), space = O(2^n)
class Solution(object):
    def goodSubsetofBinaryMatrix(self, grid):
        seen = {}
        for i, row in enumerate(grid):
            mask = 0
            for j, v in enumerate(row):
                if v:
                    mask |= 1 << j
            if mask == 0:
                return [i]
            if mask not in seen:
                seen[mask] = i
        masks = list(seen.keys())
        for p in range(len(masks)):
            for q in range(p + 1, len(masks)):
                if masks[p] & masks[q] == 0:
                    i, j = seen[masks[p]], seen[masks[q]]
                    return [min(i, j), max(i, j)]
        return []


# V0-1
# IDEA : BRUTE FORCE - TEST EVERY SINGLE ROW, THEN EVERY PAIR OF ROWS
#
#   same "only k in {1, 2} can ever win" argument as V0, but with no bitmask
#   encoding at all: compare two candidate rows column by column and reject as
#   soon as some column holds a 1 in both (that column would sum to
#   2 > floor(2/2) = 1).
#
#   NOTE : quadratic in the number of rows, so this is the reference-correctness
#          version rather than the one to ship for m = 10^4.
#
# time = O(m^2 * n)
# space = O(1)
class Solution(object):
    def goodSubsetofBinaryMatrix(self, grid):
        m, n = len(grid), len(grid[0])
        for i in range(m):
            if not any(grid[i]):
                return [i]
        for i in range(m):
            for j in range(i + 1, m):
                if all(grid[i][c] + grid[j][c] <= 1 for c in range(n)):
                    return [i, j]
        return []


# V0-2
# IDEA : ONE PASS + SUBMASK ENUMERATION OF THE COMPLEMENT
#
#   walk the rows once keeping seen[mask] = first row index with that mask.
#   a partner for the current mask must share no 1-column with it, i.e. it must
#   be a SUBSET of comp = full ^ mask, so enumerate the submasks of comp with
#   the standard sub = (sub - 1) & comp trick and take the first one already
#   stored. no second pass over pairs of masks.
#
#   NOTE : any partner found is an EARLIER row, so [seen[sub], i] already comes
#          out in ascending order.
#   NOTE : an all-zero row (mask == 0) is the k = 1 answer and is returned
#          before the enumeration, so sub == mask can never happen.
#
# time = O(m * 2^n)
# space = O(2^n)
class Solution(object):
    def goodSubsetofBinaryMatrix(self, grid):
        n = len(grid[0])
        full = (1 << n) - 1
        seen = {}
        for i, row in enumerate(grid):
            mask = 0
            for j, v in enumerate(row):
                if v:
                    mask |= 1 << j
            if mask == 0:
                return [i]
            comp = full ^ mask
            sub = comp
            while True:
                if sub in seen:
                    return [seen[sub], i]
                if sub == 0:
                    break
                sub = (sub - 1) & comp
            seen.setdefault(mask, i)
        return []
