"""

2500. Delete Greatest Value in Each Row
Easy

You are given an m x n matrix grid consisting of positive integers.

Perform the following operation until grid becomes empty:

Delete the element with the greatest value from each row. If multiple such elements exist, delete any of them.
Add the maximum of deleted elements to the answer.

Note that the number of columns decreases by one after each operation.

Return the answer after performing the operations described above.


Example 1:

Input: grid = [[1,2,4],[3,3,1]]
Output: 8
Explanation: The diagram above shows the removed values in each step.
- In the first operation, we remove 4 from the first row and 3 from the second row (notice that, there are two cells with value 3 and we can remove any of them). We add 4 to the answer.
- In the second operation, we remove 2 from the first row and 3 from the second row. We add 3 to the answer.
- In the third operation, we remove 1 from the first row and 1 from the second row. We add 1 to the answer.
The final answer = 4 + 3 + 1 = 8.

Example 2:

Input: grid = [[10]]
Output: 10
Explanation: The diagram above shows the removed values in each step.
- In the first operation, we remove 10 from the first row. We add 10 to the answer.
The final answer = 10.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
1 <= grid[i][j] <= 100

"""

# V0
# IDEA : SORT EACH ROW + SUM OF COLUMN MAX
#
#   the k-th operation always removes the k-th largest element of every row,
#   independent of the other rows. so sort each row (ascending) and the
#   values removed together are exactly one column of the sorted matrix.
#
#   answer = sum over columns of max(column).
#
#   NOTE : sorting ascending means column j pairs the j-th smallest of each
#          row -- the operations are just read right-to-left, which does not
#          change the sum.
#
# time = O(m * n * log n), space = O(m * n) for the transposed columns
class Solution(object):
    def deleteGreatestValue(self, grid):
        rows = [sorted(row) for row in grid]
        return sum(max(col) for col in zip(*rows))


# V0-1
# IDEA : ONE MAX-HEAP PER ROW, POP ONE ROUND AT A TIME
#
#   run the operations exactly as described instead of pre-sorting: keep a
#   max-heap per row (python only has min-heaps, so store negated values),
#   and each round pop one element from every row and add the largest of
#   them to the answer.
#
#   heapify is O(n) per row and each round costs O(log n) per row, so this
#   never fully orders a row - it only ever extracts the current maximum.
#
# time = O(m * n * log n), space = O(m * n) for the heaps
import heapq


class Solution(object):
    def deleteGreatestValue(self, grid):
        heaps = [[-v for v in row] for row in grid]
        for h in heaps:
            heapq.heapify(h)

        res = 0
        for _ in range(len(grid[0])):
            res += -min(heapq.heappop(h) for h in heaps)
        return res


# V0-2
# IDEA : COUNTING SORT PER ROW (values are bounded by 100)
#
#   same "k-th largest of every row are removed together" observation as V0,
#   but the ordering is done by bucket counting rather than comparison
#   sorting: tally each row into 101 buckets, then read the buckets from 100
#   down to 1 to get the row in descending order.
#
#   that drops the log n factor - column j of the descending rows is exactly
#   the group removed by operation j, so the answer is the sum of the column
#   maxima.
#
# time = O(m * (n + V)), V = 100 the value bound
# space = O(m * n + V)
class Solution(object):
    def deleteGreatestValue(self, grid):
        MAXV = 100
        desc_rows = []
        for row in grid:
            cnt = [0] * (MAXV + 1)
            for v in row:
                cnt[v] += 1
            desc = []
            for v in range(MAXV, 0, -1):
                if cnt[v]:
                    desc += [v] * cnt[v]
            desc_rows.append(desc)

        res = 0
        for j in range(len(grid[0])):
            res += max(row[j] for row in desc_rows)
        return res
