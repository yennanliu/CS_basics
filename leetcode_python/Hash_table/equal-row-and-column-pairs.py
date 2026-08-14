"""

2352. Equal Row and Column Pairs
Medium

Given a 0-indexed n x n integer matrix grid, return the number of pairs (ri, cj) such that row ri and column cj are equal.

A row and column pair is considered equal if they contain the same elements in the same order (i.e., an equal array).


Example 1:

Input: grid = [[3,2,1],[1,7,6],[2,7,7]]
Output: 1
Explanation: There is 1 equal row and column pair:
- (Row 2, Column 1): [2,7,7]

Example 2:

Input: grid = [[3,1,2,2],[1,4,4,5],[2,4,2,2],[2,4,2,2]]
Output: 3
Explanation: There are 3 equal row and column pairs:
- (Row 0, Column 0): [3,1,2,2]
- (Row 2, Column 2): [2,4,2,2]
- (Row 3, Column 2): [2,4,2,2]


Constraints:

n == grid.length == grid[i].length
1 <= n <= 200
1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : HASH TABLE ON TUPLES (count rows, then look each column up)
#
#   Hash every row as an immutable tuple into a Counter. Then build each column
#   as a tuple and add cnt[column] to the answer -- that is exactly how many
#   rows equal this column, i.e. how many (ri, cj) pairs this column forms.
#
#   NOTE : this beats the naive O(n^3) triple loop; hashing is O(n) per row/col.
#
# time = O(n^2), space = O(n^2)
from collections import Counter
class Solution(object):
    def equalPairs(self, grid):
        n = len(grid)
        cnt = Counter(tuple(row) for row in grid)

        res = 0
        for j in range(n):
            col = tuple(grid[i][j] for i in range(n))
            res += cnt[col]
        return res
