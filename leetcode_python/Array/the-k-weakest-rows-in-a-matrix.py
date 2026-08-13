"""

1337. The K Weakest Rows in a Matrix
Easy

You are given an m x n binary matrix mat of 1's (representing soldiers)
and 0's (representing civilians). The soldiers are positioned in front of
the civilians. That is, all the 1's will appear to the left of all the 0's
in each row.

A row i is weaker than a row j if one of the following is true:

- The number of soldiers in row i is less than the number of soldiers in row j.
- Both rows have the same number of soldiers and i < j.

Return the indices of the k weakest rows in the matrix ordered from
weakest to strongest.


Example 1:

Input: mat =
[[1,1,0,0,0],
 [1,1,1,1,0],
 [1,0,0,0,0],
 [1,1,0,0,0],
 [1,1,1,1,1]],
k = 3
Output: [2,0,3]
Explanation:
The number of soldiers in each row is:
- Row 0: 2
- Row 1: 4
- Row 2: 1
- Row 3: 2
- Row 4: 5
The rows ordered from weakest to strongest are [2,0,3,1,4].

Example 2:

Input: mat =
[[1,0,0,0],
 [1,1,1,1],
 [1,0,0,0],
 [1,0,0,0]],
k = 2
Output: [0,2]
Explanation:
The number of soldiers in each row is:
- Row 0: 1
- Row 1: 4
- Row 2: 1
- Row 3: 1
The rows ordered from weakest to strongest are [0,2,3,1].


Constraints:

m == mat.length
n == mat[i].length
2 <= n, m <= 100
1 <= k <= m
matrix[i][j] is either 0 or 1.

"""

# V0
# IDEA: BINARY SEARCH (count 1's per row) + SORT by (count, idx)
#
#  each row is `1...1 0...0`, so the soldier count is the index of the
#  FIRST 0 -> can be found with binary search in O(log n)
#
#  python sort is stable, so sorting by count alone already keeps
#  the smaller row index first, but we sort by (count, idx) to be explicit.
#
# time = O(m log n + m log m)
# space = O(m)
class Solution(object):
    def kWeakestRows(self, mat, k):
        m, n = len(mat), len(mat[0])

        def count_soldiers(row):
            # find first index whose value is 0
            lo, hi = 0, n
            while lo < hi:
                mid = (lo + hi) // 2
                if row[mid] == 0:
                    hi = mid
                else:
                    lo = mid + 1
            return lo

        pairs = [(count_soldiers(mat[i]), i) for i in range(m)]
        pairs.sort()
        return [i for _, i in pairs[:k]]


# V0-1
# IDEA: MIN HEAP (nsmallest)
#  distinct trick: O(m log k) selection instead of a full sort
# time = O(m * n + m log k)
# space = O(m)
import heapq
class Solution(object):
    def kWeakestRows(self, mat, k):
        pairs = [(sum(row), i) for i, row in enumerate(mat)]
        return [i for _, i in heapq.nsmallest(k, pairs)]
