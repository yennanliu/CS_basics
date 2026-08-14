"""

1198. Find Smallest Common Element in All Rows
Medium

Given an m x n matrix mat where every row is sorted in strictly increasing order,
return the smallest common element in all rows.

If there is no common element, return -1.


Example 1:

Input: mat = [[1,2,3,4,5],[2,4,5,8,10],[3,5,7,9,11],[1,3,5,7,9]]
Output: 5

Example 2:

Input: mat = [[1,2,3],[2,3,4],[2,3,5]]
Output: 2


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 500
1 <= mat[i][j] <= 10^4
mat[i] is sorted in strictly increasing order.

"""

# V0
# IDEA: HASH TABLE (counting)
"""

 -> each row is STRICTLY increasing,
    so a value can appear AT MOST ONCE per row
    => cnt[x] == m  <=>  x appears in every row

 -> we scan row by row (top -> bottom, left -> right).
    a count can only reach m while scanning the LAST row,
    and that row is sorted ascending
    => the FIRST value hitting m is the smallest common element
"""
# time = O(m * n)
# space = O(m * n), size of the counter
from collections import defaultdict
class Solution(object):
    def smallestCommonElement(self, mat):
        m = len(mat)
        cnt = defaultdict(int)

        for row in mat:
            for x in row:
                cnt[x] += 1
                if cnt[x] == m:
                    return x

        return -1


# V1
# IDEA: ROW POINTERS (k-way merge, no extra hash map)
# advance every pointer whose value is smaller than the current max;
# when all pointers agree, that value is the answer
# time = O(m * n)
# space = O(m)
class Solution(object):
    def smallestCommonElement(self, mat):
        m, n = len(mat), len(mat[0])
        pos = [0] * m

        while True:
            cur = max(mat[i][pos[i]] for i in range(m))
            same = True
            for i in range(m):
                while pos[i] < n and mat[i][pos[i]] < cur:
                    pos[i] += 1
                if pos[i] == n:
                    return -1
                if mat[i][pos[i]] != cur:
                    same = False
            if same:
                return cur
